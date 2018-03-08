package eu.baboi.cristian.quiztest;

import android.view.View;

/**
 * Created by cristi on 11.02.2018.
 */

interface Counter {
    // update the counter of correct children
    void increment();
    void decrement();

    // Count some children and initialize them
    void countChildren(View v) throws IllegalStateException;
}
