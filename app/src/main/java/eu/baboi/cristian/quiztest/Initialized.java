package eu.baboi.cristian.quiztest;

/**
 * Created by cristi on 09.02.2018.
 */

public interface Initialized {
    void init() throws IllegalStateException; // initialize the value of mCorrect

    void truthChanged(); // Notify the parent if there is a change in truth value
}
