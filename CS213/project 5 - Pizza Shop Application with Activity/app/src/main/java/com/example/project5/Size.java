package com.example.project5;

/**
 * Enum class that specifies the size of the pizza.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public enum Size {

    SMALL,MEDIUM,LARGE;

    /**
     * Get the size enum from the string.
     * @param size String format of the size.
     * @return Size enum.
     */
    public static Size getSize(String size) {
        if(size.equals("Small")) {
            return SMALL;
        }
        else if(size.equals("Medium")) {
            return MEDIUM;
        }
        else if(size.equals("Large")) {
            return LARGE;
        }
        else return null;
    }
}
