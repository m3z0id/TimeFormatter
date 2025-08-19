package com.m3z0id.timeformatter.client.events;

import com.m3z0id.timeformatter.client.datatypes.ChatMessageSendActionResult;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

@FunctionalInterface
public interface ChatMessageSender {
    Event<ChatMessageSender> EVENT = EventFactory.createArrayBacked(ChatMessageSender.class, (listeners) -> (message) -> {
        String lastModified = null;
        for (ChatMessageSender sender : listeners) {

            ChatMessageSendActionResult result = sender.send(message);
            lastModified = message;
            if (result.getResult() == ActionResult.FAIL) {
                return result;
            }

            if (result.getResult() == ActionResult.SUCCESS) {
                lastModified = result.getMessage();
            }
        }
        return ChatMessageSendActionResult.success(lastModified);
    });

    ChatMessageSendActionResult send(String message);
}
