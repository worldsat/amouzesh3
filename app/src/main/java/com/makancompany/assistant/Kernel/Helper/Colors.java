package com.makancompany.assistant.Kernel.Helper;

import android.graphics.Color;

public class Colors {

    //int colors
    public static int blue = Color.parseColor("#0000FF");
//    public static int green = Color.parseColor("#7CFC00");
    public static int green = Color.parseColor("#388E3C");
    public static int lawngreen = Color.parseColor("#7cfc00");
    public static int lightblue = Color.parseColor("#ADD8E6");
    public static int red = Color.parseColor("#FF0000");
    public static int orange = Color.parseColor("#FFA500");
    public static int yellow2 = Color.parseColor("#FFFF00");
    //    public static int green2 = Color.parseColor("#7CFC00");
    public static int green2 = Color.parseColor("#388E3C");
    public static int darkgreen = Color.parseColor("#006400");
    public static int pink = Color.parseColor("#FF69B4");

    //strings
    public static String blue_hex = "#0000FF";
    //    public static String green_hex = "#7CFC00";
    public static String green_hex = "#388E3C";
    public static String lawngreen_hex = "#7cfc00";
    public static String lightblue_hex = "#ADD8E6";
    public static String red_hex = "#FF0000";
    public static String orange_hex = "#FFA500";
    public static String yellow2_hex = "#FFFF00";
    public static String green2_hex = "#7CFC00";
    public static String darkgreen_hex = "#006400";
    public static String pink_hex = "#FF69B4";


    //strings
    public static String blue_hex_semi_transparent = "#550000FF";
    //    public static String green_hex_semi_transparent = "#557CFC00";
    public static String green_hex_semi_transparent = "#55388E3C";
    public static String lawngreen_hex_semi_transparent = "#557cfc00";
    public static String lightblue_hex_semi_transparent = "#55ADD8E6";
    public static String red_hex_semi_transparent = "#55FF0000";
    public static String orange_hex_semi_transparent = "#55FFA500";
    public static String yellow2_hex_semi_transparent = "#55FFFF00";
    public static String green2_hex_semi_transparent = "#557CFC00";
    public static String darkgreen_hex_semi_transparent = "#55006400";
    public static String pink_hex_semi_transparent = "#55FF69B4";


    public static int getColorCode(String colorName) {
        switch (colorName) {
            case "blue":
                return blue;
            case "green":
                return green;
            case "lawngreen":
                return lawngreen;
            case "lightblue":
                return lightblue;
            case "red":
                return red;
            case "orange":
                return orange;
            case "yellow2":
                return yellow2;
            case "yellow":
                return yellow2;
            case "green2":
                return green2;
            case "darkgreen":
                return darkgreen;
            case "pink":
                return pink;
            case "hotpink":
                return pink;
            default:

                try {
                    return Color.parseColor(colorName);
                } catch (Exception e) {
                    return blue;
                }
        }
    }


    public static String getColorHexCode(String colorName) {
        switch (colorName) {
            case "blue":
                return blue_hex;
            case "green":
                return green_hex;
            case "lawngreen":
                return lawngreen_hex;
            case "lightblue":
                return lightblue_hex;
            case "red":
                return red_hex;
            case "orange":
                return orange_hex;
            case "yellow2":
                return yellow2_hex;
            case "yellow":
                return yellow2_hex;
            case "green2":
                return green2_hex;
            case "darkgreen":
                return darkgreen_hex;
            case "pink":
                return pink_hex;
            case "hotpink":
                return pink_hex;
            default:
                try {
                    String[] color = colorName.split("#");
                    return "#55" + color[1];
                } catch (Exception e) {
                    return blue_hex;
                }

        }
    }

    public static String getColorHexCodeSemiTransparent(String colorName) {
        switch (colorName) {
            case "blue":
                return blue_hex_semi_transparent;
            case "lawngreen":
                return lawngreen_hex_semi_transparent;
            case "green":
                return green_hex_semi_transparent;
            case "lightblue":
                return lightblue_hex_semi_transparent;
            case "red":
                return red_hex_semi_transparent;
            case "orange":
                return orange_hex_semi_transparent;
            case "yellow2":
                return yellow2_hex_semi_transparent;
            case "yellow":
                return yellow2_hex_semi_transparent;
            case "green2":
                return green2_hex_semi_transparent;
            case "darkgreen":
                return darkgreen_hex_semi_transparent;
            case "pink":
                return pink_hex_semi_transparent;
            case "hotpink":
                return pink_hex_semi_transparent;
            default:
                try {
                    String[] color = colorName.split("#");
                    return "#55" + color[1];
                } catch (Exception e) {
                    return blue_hex_semi_transparent;
                }
        }
    }
}