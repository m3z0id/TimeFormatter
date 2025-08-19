package com.m3z0id.timeformatter.client.listeners;

import com.m3z0id.timeformatter.client.TimeformatterClient;
import com.m3z0id.timeformatter.client.datatypes.ChatMessageSendActionResult;
import com.m3z0id.timeformatter.client.events.ChatMessageSender;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatSendListener {
    public static void register() {
        TimeformatterClient.LOGGER.info("Initializing ChatSendListener");
        ChatMessageSender.EVENT.register((message) -> {
            String origMessage = message;
            if (message == null) return ChatMessageSendActionResult.fail(message);

            Pattern pattern = Pattern.compile("<<t:(\\d{1,13}|now|yesterday|tomorrow)(?::([fFdDtTrR]))?>>");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                long timestamp;
                try {
                    timestamp = Long.parseLong(matcher.group(1));
                } catch (NumberFormatException e) {
                    switch (matcher.group(1)) {
                        case "now" -> timestamp = Instant.now().getEpochSecond();
                        case "yesterday" -> timestamp = Instant.now().getEpochSecond() - 86400;
                        case "tomorrow" -> timestamp = Instant.now().getEpochSecond() + 86400;
                        default -> timestamp = 0L; // Can't happen
                    }
                }

                if(timestamp > 8640000000000L) continue;

                char mode = 'f';
                if (matcher.groupCount() > 1 && matcher.group(2) != null) mode = matcher.group(2).charAt(0);

                message = message.replaceFirst(matcher.group(0), Formatter.getTime(timestamp, mode));
            }

            if(message.length() > 256) return ChatMessageSendActionResult.success(origMessage);

            return ChatMessageSendActionResult.success(message);
        });
    }
}
