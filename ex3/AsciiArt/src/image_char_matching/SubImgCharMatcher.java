package image_char_matching;

import java.util.*;

public class SubImgCharMatcher {

    private final TreeMap<Character, Double> charToBrightness;
    private final TreeMap<Character, Double> charToNormalizedBrightness;
    private double maxBrightness = 0;
    private double minBrightness = 2;

    public SubImgCharMatcher(char[] charset) {
        charToBrightness = new TreeMap<>();
        charToNormalizedBrightness = new TreeMap<>();

        initBrightnessValues(charset);
    }

    public char[] getCharset() {
        char[] charset = new char[charToBrightness.size()];
        int i = 0;
        for (char c : charToBrightness.keySet()) {
            charset[i] = c;
            i++;
        }
        return charset;
    }

    public char getCharByImageBrightness(double brightness) {
        char bestMatch = ' ';
        double minimalDiff = 2;

        // Finding the character with the closest brightness to the given brightness
        for (Map.Entry<Character, Double> entry : charToNormalizedBrightness.entrySet()) {
            double diff = Math.abs(entry.getValue() - brightness);
            if (diff < minimalDiff) {
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
        charToBrightness.put(c, charBrightness);
        // If the character's brightness doesn't change the min/max, we just add it to the map
        // after performing the linear stretching
        if ((charBrightness <= maxBrightness) && (charBrightness >= minBrightness)) {
            charToNormalizedBrightness.put(
                    c, getLinearStretch(minBrightness, maxBrightness, charBrightness));
        } else { // The character's brightness changes the min/max, so we need to update the map
            updateBrightnessValues(c, charBrightness);
        }
    }

    public void removeChar(char c) {
        // TODO: Need to update values
        charToBrightness.remove(c);
        charToNormalizedBrightness.remove(c);
    }

    private void initBrightnessValues(char[] charset) {
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
            charToNormalizedBrightness.put(entry.getKey(), newBrightness);
        }
    }

    private double getLinearStretch(double minBrightness, double maxBrightness, double brightness) {
        // There is an assumption here that maxBrightness > minBrightness,
        // hence that they are not equal and the denominator is not 0.
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
