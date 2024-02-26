package image_char_matching;

import java.util.*;

public class SubImgCharMatcher {

    private TreeMap<Character, Double> charToBrightness;
    private double maxBrightness = 0;
    private double minBrightness = 0;

    public SubImgCharMatcher(char[] charset) {
        charToBrightness = new TreeMap<>();
        initBrightnessValues(charset);
    }

    public char getCharByImageBrightness(double brightness) {
        char bestMatch = ' ';
        double minimalDiff = 0;

        // Finding the character with the closest brightness to the given brightness
        for (Map.Entry<Character, Double> entry : charToBrightness.entrySet()) {
            System.out.println(entry.getKey());
            double diff = Math.abs(entry.getValue() - brightness);
            if ((diff < minimalDiff) || (0 == minimalDiff)) {
                minimalDiff = diff;
                bestMatch = entry.getKey();
            }
        }
        return bestMatch;
    }

    public void addChar(char c) {
        // If the character is already in the map, we don't need to add it again
        // (and we don't want to update the brightness values)
        if (charToBrightness.containsKey(c)) {
            return;
        }

        double charBrightness = getCharBrightness(c);
        // If the character's brightness doesn't change the min/max, we just add it to the map
        // after performing the linear stretching
        if ((charBrightness <= maxBrightness) || (charBrightness >= minBrightness)) {
            charToBrightness.put(
                    c, getLinearStretch(minBrightness, maxBrightness, charBrightness));
        } else { // The character's brightness changes the min/max, so we need to update the map
            updateBrightnessValues(c, charBrightness);
        }
    }

    public void removeChar(char c) {
        charToBrightness.remove(c);
    }

    private void initBrightnessValues(char[] charset) {
        maxBrightness = 0;
        minBrightness = 1;

        for (char c : charset) {
            double charBrightness = getCharBrightness(c);
            charToBrightness.put(c, charBrightness);

            // Updating the max and min brightness values
            if (charBrightness > maxBrightness) {
                maxBrightness = charBrightness;
            }
            if (charBrightness < minBrightness) {
                minBrightness = charBrightness;
            }
        }

        // Performing the linear stretching as described in the assignment
        updateLinearStretch();
    }

    private void updateBrightnessValues(char c, double brightness) {

        charToBrightness.put(c, brightness);
        if (brightness > maxBrightness) {
            maxBrightness = brightness;
        }
        if (brightness < minBrightness) {
            minBrightness = brightness;
        }

        updateLinearStretch();
    }

    private void updateLinearStretch() {
        for (Map.Entry<Character, Double> entry : charToBrightness.entrySet()) {
            double newBrightness =
                    getLinearStretch(minBrightness, maxBrightness, entry.getValue());
            charToBrightness.put(entry.getKey(), newBrightness);
        }
    }

    private double getLinearStretch(double minBrightness, double maxBrightness, double brightness) {
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    private static double getCharBrightness(char c) {
        boolean[][] charBoolArr = CharConverter.convertToBoolArray(c);
        int trueValues = 0;
        for (boolean[] row : charBoolArr) {
            for (boolean value : row) {
                if (value) { trueValues++; }
            }
        }
        return (double) trueValues / (charBoolArr.length * charBoolArr[0].length);
    }
}
