package eu.baboi.cristian.quiztest;

/**
 * Created by cristi on 06.02.2018.
 */

interface Numbered {
    void number(int num); // set the question number

    boolean isNumbered(); // return true if the number is already set

    boolean isCorrect();  // return true if the question/variant is correct
}
