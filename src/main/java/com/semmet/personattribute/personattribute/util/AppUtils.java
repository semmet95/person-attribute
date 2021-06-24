package com.semmet.personattribute.personattribute.util;

/**
 * AppUtils class provides utility methods.
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

public class AppUtils {

    private AppUtils() {
    }
    
    /**
     * This method calculates the new averaged out weight given the old
     * averaged out weight and a new weight that has to be accomodated.
     * The method also uses tanh function to keep the returned value
     * from exploding.
     * @see <a href="https://reference.wolfram.com/language/ref/Tanh.html">tanh function</a>
     * 
     * @param storedWeight the already averaged out weight
     * @param newWeight the new weight to be averaged out
     * @return mapping of the detected sentiments
     */

    public static float tanH(float storedWeight, float newWeight) {
        return (float) Math.tanh((storedWeight + Math.tanh(newWeight))/2);
    }
}
