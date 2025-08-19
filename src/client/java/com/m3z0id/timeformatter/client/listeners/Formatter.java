package com.m3z0id.timeformatter.client.listeners;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Formatter {
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

    public static String getTime(long timestamp, char mode) {
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
}
