package com.m3z0id.timeformatter.client.events;

import com.m3z0id.timeformatter.client.datatypes.ChatMessageActionResult;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ChatMessageReceiver {
    Event<ChatMessageReceiver> EVENT = EventFactory.createArrayBacked(ChatMessageReceiver.class, (listeners) -> (message, signatureData, indicator) -> {
        Text lastModified = message;
        for (ChatMessageReceiver listener : listeners) {
            ChatMessageActionResult result = listener.receive(lastModified, signatureData, indicator);
            if (result.getResult() == ActionResult.FAIL) {
                return result;
            }
            if (result.getResult() == ActionResult.SUCCESS) {
                lastModified = result.getMessage();
            }
        }
        return ChatMessageActionResult.success(lastModified);
    });

    ChatMessageActionResult receive(Text message, @Nullable MessageSignatureData signatureData, @Nullable MessageIndicator indicator);
}

