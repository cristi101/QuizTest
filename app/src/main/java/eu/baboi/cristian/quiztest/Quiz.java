package eu.baboi.cristian.quiztest;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.os.Build;


/**
 * Created by cristi on 06.02.2018.
 */

public class Quiz extends LinearLayout {
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
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Quiz(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // Count the number of correct questions
    public void increment() {
        correct++;
    }

    public void decrement() {
        correct--;
    }

    // Perform counting of all questions
    private void countQuestions(View v) throws IllegalStateException {
        if (v instanceof Numbered) {
            if (!((Numbered) v).isNumbered()) {

                count++; // New question detected

                // throw error if it is not OneChoiceQuestion or MultiChoiceQuestion
                if (!(v instanceof OneChoiceQuestion) && !(v instanceof MultiChoiceQuestion))
                    throw new IllegalStateException("Only OnceChoiceQuestion and MultiChoiceQuestion can be childs of a Quiz! : Question " + String.valueOf(count));

                // set the question number
                ((Numbered) v).number(count);

                // Initialize the question
                ((Initialized) v).init();

                // count the correct questions
                if (((Numbered) v).isCorrect()) {
                    //here the previous version should be set
                    increment();
                }
            }
        }
    }

    // Counts all the questions in the layout, before they are added to their parents
    @Override
    public void addView(View child) {
        countQuestions(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        countQuestions(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        countQuestions(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        countQuestions(child);
        super.addView(child, index, params);
    }

    // get the Quiz results
    public int getQuestionsNumber() {
        return count;
    }

    public int getCorrectCount() {
        return correct;
    }

}
