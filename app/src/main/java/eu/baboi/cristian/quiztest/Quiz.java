package eu.baboi.cristian.quiztest;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by cristi on 06.02.2018.
 */

public class Quiz extends LinearLayout implements Counter {
    private int count = 0; // The number of questions
    private int correct = 0; // The number of correct questions

    // The constructors
    public Quiz(Context context) {
        this(context, null);
    }

    public Quiz(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Quiz(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Quiz(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(LinearLayout.VERTICAL);
    }

    // Get the Quiz results
    public int getQuestionsNumber() {
        return count;
    }

    public int getCorrectCount() {
        return correct;
    }

    // The Counter interface methods

    // Count the correct questions
    @Override
    public void increment() {
        correct++;
    }

    @Override
    public void decrement() {
        correct--;
    }

    // Count all questions and initialize them
    @Override
    public void countChildren(View v) throws IllegalStateException {
        if (v instanceof Numbered) { // Found a question or an answer

            Numbered question = (Numbered) v;
            if (!question.isNumbered()) { // The question is seen for the first time

                count++; // count the question

                // throw error if it is not a question but an answer
                if (!(question instanceof OneChoiceQuestion) && !(question instanceof MultiChoiceQuestion))
                    throw new IllegalStateException("Only OnceChoiceQuestion and MultiChoiceQuestion can be children of a Quiz! : Question " + String.valueOf(count));

                // set the question number and perform some initialization
                question.number(count);

                // count the correct questions
                if (question.isCorrect()) {
                    increment();
                }
            }
        }
    }

    // Search the children

    // Counts all the questions in the layout, before they are added to their parents
    @Override
    public void addView(View child) {
        countChildren(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        countChildren(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        countChildren(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        countChildren(child);
        super.addView(child, index, params);
    }

}
