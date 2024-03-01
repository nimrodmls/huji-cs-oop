package image_char_matching;

import java.util.*;

/**
 * A class for character-brightness classification.
 * @author Nimrod M.
 */
public class SubImgCharMatcher {

    private final TreeMap<Character, Double> charToBrightness;
    private final TreeMap<Character, Double> charToNormalizedBrightness;
    private double maxBrightness = 0;
    private double minBrightness = 2;

    /**
     * Constructs a new SubImgCharMatcher object using the given charset.
     * @param charset The charset to be used for the classification
     */
    public SubImgCharMatcher(char[] charset) {
        charToBrightness = new TreeMap<>();
        charToNormalizedBrightness = new TreeMap<>();

        initBrightnessValues(charset);
    }

    /**
     * @return The charset used for the classification
     */
    public char[] getCharset() {
        char[] charset = new char[charToBrightness.size()];
        int i = 0;
        for (char c : charToBrightness.keySet()) {
            charset[i] = c;
            i++;
        }
        return charset;
    }

    /**
     * Classifies the given brightness value to the closest character in the charset.
     * @param brightness The brightness value to be classified
     * @return The character with the closest brightness to the given brightness
     */
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

    /**
     * Adds the given character to the charset. If the character is already in the charset,
     * it will not be added again. Updates the rest of the values for the charset if needed.
     * @param c The character to be added to the charset
     */
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
            updateMinMax(charBrightness);
            updateLinearStretch();
        }
    }

    /**
     * Removes the given character from the charset. If the character is not in the charset,
     * nothing will happen. Updates the rest of the values for the charset if needed.
     * @param c The character to be removed from the charset
     */
    public void removeChar(char c) {
        if (!charToBrightness.containsKey(c)) {
            return;
        }

        double charBrightness = charToBrightness.remove(c);
        charToNormalizedBrightness.remove(c);

        // If the character's brightness doesn't change the min/max, we just remove it from the map
        if ((charBrightness != maxBrightness) && (charBrightness != minBrightness)) {
            return;
        }

        // Otherwise, finding new min and max brightness values
        for (Map.Entry<Character, Double> entry : charToBrightness.entrySet()) {
            updateMinMax(entry.getValue());
        }

        updateLinearStretch();
    }

    /**
     * @param c The character to be checked
     * @return True if the character is in the charset, false otherwise
     */
    public boolean inCharset(char c) {
        return charToBrightness.containsKey(c);
    }

    /**
     * Initializes the regular & normalized brightness values for the charset.
     * @param charset The charset to be used for the classification
     */
    private void initBrightnessValues(char[] charset) {
        for (char c : charset) {
            double charBrightness = getCharBrightness(c);
            charToBrightness.put(c, charBrightness);

            // Updating the max and min brightness values
            updateMinMax(charBrightness);
        }

        updateLinearStretch();
    }

    /**
     * Updates the min and max brightness values, if needed.
     * @param brightness The brightness value to be checked
     */
    private void updateMinMax(double brightness) {

        if (brightness > maxBrightness) {
            maxBrightness = brightness;
        }
        if (brightness < minBrightness) {
            minBrightness = brightness;
        }
    }

    /**
     * Updates the normalized brightness values for the charset.
     * Max/Min brightness values should be updated before calling this method.
     */
    private void updateLinearStretch() {
        for (Map.Entry<Character, Double> entry : charToBrightness.entrySet()) {
            double newBrightness =
                    getLinearStretch(minBrightness, maxBrightness, entry.getValue());
            charToNormalizedBrightness.put(entry.getKey(), newBrightness);
        }
    }

    /**
     * Calculates the linear stretching of the given brightness value.
     * @param minBrightness The minimum brightness value
     * @param maxBrightness The maximum brightness value
     * @param brightness The brightness value to be normalized
     * @return The normalized brightness value
     */
    private static double getLinearStretch(
            double minBrightness, double maxBrightness, double brightness) {
        // There is an assumption here that maxBrightness > minBrightness,
        // hence that they are not equal and the denominator is not 0.
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /**
     * Calculates the brightness value of the given character.
     * @param c The character to calculate its brightness
     * @return The brightness value of the given character
     */
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
