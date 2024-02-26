package main;

import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;

public class AsciiArt {

    public static void main(String[] args) {
        /*SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(new char[]{'a', 'i', 'j', 'e', 'b', 'c', 'd', 'f', 'g', 'h'});
        char c = subImgCharMatcher.getCharByImageBrightness(0.5);
        System.out.println("-----");
        subImgCharMatcher.addChar('k');
        c = subImgCharMatcher.getCharByImageBrightness(0.5);*/
        PaddedImage image = null;
        try {
            Color[][] img = new Color[][] {
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)},
                    {new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)}
            };
            image = new PaddedImage(new Image(img, 6, 10));
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    Color current = image.getPixel(i,j);
                    System.out.print("[" + i + "," + j +"](" + current.getRed() + "," + current.getGreen() + "," + current.getBlue() + ")" + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
