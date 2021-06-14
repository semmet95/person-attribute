package com.semmet.personattribute.personattribute.util;

public class AppUtils {

    private AppUtils() {
    }
    
    public static float tanH(float storedWeight, float newWeight) {
        return (float) Math.tanh((storedWeight + Math.tanh(newWeight))/2);
    }
}
