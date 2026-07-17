package com.example.apilab.repository.util;

public final class SlugGenerator {

    private SlugGenerator() {
        // Utility class
    }

    public static String generate(String name) {
        if (name == null) {
            return null;
        }
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
