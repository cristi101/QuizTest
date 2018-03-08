package eu.baboi.cristian.quiztest;

/**
 * Created by cristi on 06.02.2018.
 */

interface Numbered {
    boolean isCorrect();  // return true if the question/answer is correct

    boolean isNumbered(); // return true if the number is already set

    void number(int num) throws IllegalStateException; // set the number and perform some initialization
    void truthChanged(); // Notify the parent if there is a change in truth value
}
