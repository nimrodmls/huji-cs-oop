package main;

import image.SimpleImage;
import image.PaddedImage;

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
            image = new PaddedImage(new SimpleImage(img, 6, 10));
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    Color current = image.getPixel(i,j);
                    System.out.print("[" + i + "," + j +"](" + current.getRed() + "," + current.getGreen() + "," + current.getBlue() + ")" + " ");
                }
                System.out.println();
            }
            SimpleImage[][] subImages = image.getSubImages(2);
            for (int i = 0; i < subImages.length; i++) {
                for (int j = 0; j < subImages[i].length; j++) {
                    System.out.println("SubImage " + i + " " + j);
                    for (int k = 0; k < subImages[i][j].getHeight(); k++) {
                        for (int l = 0; l < subImages[i][j].getWidth(); l++) {
                            Color current = subImages[i][j].getPixel(k,l);
                            System.out.print("[" + k + "," + l +"](" + current.getRed() + "," + current.getGreen() + "," + current.getBlue() + ")" + " ");
                        }
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
