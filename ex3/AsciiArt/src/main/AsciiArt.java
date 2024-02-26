package main;

import image_char_matching.SubImgCharMatcher;

public class AsciiArt {

    public static void main(String[] args) {
        SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(new char[]{'a', 'i', 'j', 'e', 'b', 'c', 'd', 'f', 'g', 'h'});
        char c = subImgCharMatcher.getCharByImageBrightness(0.5);
        System.out.println("-----");
        subImgCharMatcher.addChar('k');
        c = subImgCharMatcher.getCharByImageBrightness(0.5);
    }
}
