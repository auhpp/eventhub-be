package com.auhpp.event_management.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressUtils {

    private static final List<String> STREET_PREFIXES = Arrays.asList(
            "phố", "đường", "ngõ", "hẻm", "ngách", "hẻm", "đại lộ", "quốc lộ", "tỉnh lộ"
    );

    public static String formatTomTomAddress(String rawAddress) {
        if (rawAddress == null || rawAddress.isEmpty()) {
            return "";
        }
        String[] parts = rawAddress.split(",");
        List<String> cleanedParts = new ArrayList<>();

        for (String part : parts) {
            String current = part.trim();

            // Regex "\\d+" check if current string is number (Postal code)
            // Ex: 11210
            if (current.isEmpty() || current.matches("\\d+")) {
                continue;
            }

            // Check same name
            if (!cleanedParts.isEmpty()) {
                String lastAdded = cleanedParts.getLast();
                // Ex: "Hà Nội", "Hà Nội"
                if (lastAdded.equalsIgnoreCase(current)) {
                    continue;
                }
                // Ex: "Ninh Kiều Cần Thơ", "Cần Thơ"
                if (lastAdded.toLowerCase().endsWith(" " + current.toLowerCase())) {
                    String remainingPrefix = lastAdded.substring(0, lastAdded.length() - current.length()).trim();

                    // Ex: "Phố A", "A"
                    if (!STREET_PREFIXES.contains(remainingPrefix.toLowerCase())) {
                        cleanedParts.set(cleanedParts.size() - 1, remainingPrefix);
                    }
                }
            }
            cleanedParts.add(current);
        }
        return String.join(", ", cleanedParts);
    }
}
