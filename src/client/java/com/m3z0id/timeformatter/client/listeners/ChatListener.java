package com.m3z0id.timeformatter.client.listeners;

import com.m3z0id.timeformatter.client.TimeformatterClient;
import com.m3z0id.timeformatter.client.datatypes.ChatMessageActionResult;
import com.m3z0id.timeformatter.client.events.ChatMessageReceiver;
import net.minecraft.text.Text;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
    private static String formatRelativeTime(Instant timestamp) {
        if(timestamp.getEpochSecond() == System.currentTimeMillis() / 1000) return "now";

        Instant now = Instant.now();
        Duration duration = Duration.between(timestamp, now);

        boolean isFuture = duration.isNegative();

        duration = duration.abs();

        int years = (int) (duration.toDays() / 365.25);
        int days = (int) (duration.toDays() % 365.25);
        int hours = (int) duration.toHours() % 24;
        int minutes = (int) duration.toMinutes() % 60;
        int seconds = (int) duration.toSeconds() % 60;

        StringBuilder result = new StringBuilder();
        if (years > 0) result.append(years).append(" year").append(years > 1 ? "s" : "").append(", ");
        if (days > 0) result.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        if (hours > 0) result.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        if (minutes > 0) result.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        if (seconds > 0) result.append(seconds).append(" second").append(seconds > 1 ? "s" : "");

        return result.toString().replaceAll(", $", "") + (isFuture ? " from now" : " ago");
    }

    private static String getTime(long timestamp, char mode) {
        ZoneId zone = ZoneId.systemDefault();
        Instant requestedInstant = Instant.ofEpochSecond(timestamp);

        return switch (mode) {
            case 'f' -> DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm").format(requestedInstant.atZone(zone));
            case 'F' -> DateTimeFormatter.ofPattern("EEE, MMMM dd, yyyy 'at' HH:mm").format(requestedInstant.atZone(zone));
            case 'd' -> DateTimeFormatter.ofPattern("dd.MM.yyyy").format(requestedInstant.atZone(zone));
            case 'D' -> DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(requestedInstant.atZone(zone));
            case 't' -> DateTimeFormatter.ofPattern("HH:mm").format(requestedInstant.atZone(zone));
            case 'T' -> DateTimeFormatter.ofPattern("HH:mm:ss").format(requestedInstant.atZone(zone));
            case 'R' -> formatRelativeTime(requestedInstant);
            default -> ""; // Can't happen
        };
    }

    public static void register() {
        TimeformatterClient.LOGGER.info("Initializing ChatListener");
        ChatMessageReceiver.EVENT.register((message, signatureData, indicator) -> {
            String messageLiteral = message.getString();
            if (messageLiteral == null) return ChatMessageActionResult.fail(message);

            Pattern pattern = Pattern.compile("<t:(\\d{1,13}|now)(?::([fFdDtTrR]))?>");
            Matcher matcher = pattern.matcher(messageLiteral);

            StringBuilder sb = new StringBuilder();

            while (matcher.find()) {
                long timestamp;
                try {
                    timestamp = Long.parseLong(matcher.group(1));
                } catch (NumberFormatException e) {
                    timestamp = Instant.now().getEpochSecond();
                }

                char mode = 'f';
                if (matcher.groupCount() > 1 && matcher.group(2) != null) mode = matcher.group(2).charAt(0);

                if(timestamp > 8640000000000L) {
                    matcher.appendReplacement(sb, matcher.group(0));
                    continue;
                }

                String replacement = getTime(timestamp, mode);
                replacement = Matcher.quoteReplacement(replacement);
                matcher.appendReplacement(sb, replacement);
            }
            matcher.appendTail(sb);

            Text newMessage = Text.literal(sb.toString()).setStyle(message.getStyle());

            return ChatMessageActionResult.success(newMessage);
        });
    }
}
