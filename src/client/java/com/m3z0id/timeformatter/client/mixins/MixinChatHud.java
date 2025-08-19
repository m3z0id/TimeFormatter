package com.m3z0id.timeformatter.client.mixins;

import com.m3z0id.timeformatter.client.datatypes.ChatMessageReceiveActionResult;
import com.m3z0id.timeformatter.client.events.ChatMessageReceiver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class MixinChatHud {
    @Shadow @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void logChatMessage(ChatHudLine message);

    @Shadow
    protected abstract void addVisibleMessage(ChatHudLine message);

    @Shadow
    protected abstract void addMessage(ChatHudLine message);


    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    private void addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        ChatMessageReceiveActionResult actionResult = ChatMessageReceiver.EVENT.invoker().receive(message, signatureData, indicator);
        if (actionResult.getResult() == ActionResult.FAIL) {
            ci.cancel();
            return;
        }
        ChatHudLine chatHudLine = new ChatHudLine(this.client.inGameHud.getTicks(), actionResult.getMessage(), signatureData, indicator);
        this.logChatMessage(chatHudLine);
        this.addVisibleMessage(chatHudLine);
        this.addMessage(chatHudLine);
        ci.cancel();
    }
}
