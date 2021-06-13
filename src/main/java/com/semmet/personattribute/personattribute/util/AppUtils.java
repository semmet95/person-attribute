package com.semmet.personattribute.personattribute.util;

public class AppUtils {
    
    public static float tanH(float storedWeight, float newWeight) {
        return (float) Math.tanh(storedWeight + Math.tanh(newWeight));
    }
}
