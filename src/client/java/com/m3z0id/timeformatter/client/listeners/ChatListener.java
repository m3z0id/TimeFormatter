package com.m3z0id.timeformatter.client.listeners;

import com.m3z0id.timeformatter.client.TimeformatterClient;
import com.m3z0id.timeformatter.client.datatypes.ChatMessageReceiveActionResult;
import com.m3z0id.timeformatter.client.events.ChatMessageReceiver;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.minecraft.text.Text;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
    public static void register() {
        TimeformatterClient.LOGGER.info("Initializing ChatListener");
        ChatMessageReceiver.EVENT.register((message, signatureData, indicator) -> {

            Component msg = MinecraftClientAudiences.of().asAdventure(message);
            String messageLiteral = message.getString();
            if (messageLiteral == null) return ChatMessageReceiveActionResult.fail(message);

            // Prevent failed literal send from appearing
            Pattern checkFailed = Pattern.compile("<<t:(\\d{1,13}|now|yesterday|tomorrow)(?::([fFdDtTrR]))?>>");
            Matcher failedMatcher = checkFailed.matcher(messageLiteral);
            if (failedMatcher.find()) return ChatMessageReceiveActionResult.success(message);

            Pattern pattern = Pattern.compile("<t:(\\d{1,13}|now|yesterday|tomorrow)(?::([fFdDtTrR]))?>");
            Matcher matcher = pattern.matcher(messageLiteral);

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

                TextReplacementConfig config = TextReplacementConfig.builder()
                        .matchLiteral(matcher.group(0))
                        .replacement(Formatter.getTime(timestamp, mode))
                        .build();

                msg = msg.replaceText(config);
            }
            Text newMessage = MinecraftClientAudiences.of().asNative(msg);
            return ChatMessageReceiveActionResult.success(newMessage);
        });
    }
}
