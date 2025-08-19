package com.m3z0id.timeformatter.client.mixins;

import com.m3z0id.timeformatter.client.datatypes.ChatMessageSendActionResult;
import com.m3z0id.timeformatter.client.events.ChatMessageSender;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {

    @Shadow
    private MessageChain.Packer messagePacker;

    @Shadow
    private LastSeenMessagesCollector lastSeenMessagesCollector;

    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    protected void sendChatMessage(String content, CallbackInfo ci) {
        ChatMessageSendActionResult result = ChatMessageSender.EVENT.invoker().send(content);
        if (result.getResult() == ActionResult.FAIL) {
            ci.cancel();
            return;
        }

        Instant instant = Instant.now();
        long l = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = this.lastSeenMessagesCollector.collect();
        MessageSignatureData messageSignatureData = this.messagePacker.pack(new MessageBody(result.getMessage(), instant, l, lastSeenMessages.lastSeen()));

        ((ClientCommonNetworkHandler)(Object)this).sendPacket(new ChatMessageC2SPacket(result.getMessage(), instant, l, messageSignatureData, lastSeenMessages.update()));
        ci.cancel();
    }
}
