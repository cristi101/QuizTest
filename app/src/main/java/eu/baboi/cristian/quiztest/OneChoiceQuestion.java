package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Created by cristi on 06.02.2018.
 */

public class OneChoiceQuestion extends RadioGroup implements Numbered, Initialized {
    private Question q; // reference to the text of the question
    private int no = 0; // question number
    private int count = 0; //the number of variants
    private int qcount = 0; //the number of questions
    private int correct = 0; //the number of correct variants
    private boolean mCorrect; //the previous value of isCorrect

    // The constructors
    public OneChoiceQuestion(Context context) {
        super(context);
    }

    public OneChoiceQuestion(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Initialize the mCorrect
    @Override
    public void init() throws IllegalStateException {
        if (count < 1)
            throw new IllegalStateException("There must be at least one variant for the answer! : Question " + String.valueOf(no));
        mCorrect = isCorrect();
    }

    // Detect if there is a change in the truth value and notify the parent
    @Override
    public void truthChanged() {

        Quiz qz = (Quiz) getParent();
        if (qz != null) {
            boolean cCorrect = isCorrect();
            if (mCorrect != cCorrect)
                if (cCorrect) {
                    qz.increment();
                } else {
                    qz.decrement();
                }
            mCorrect = cCorrect;
        }
    }

    // Count the correct questions
    public void increment() {
        correct++;
        truthChanged();
    }

    public void decrement() {
        correct--;
        truthChanged();
    }

    // The Numbered interface methods

    // The question is correct if the number of true variants is the number of variants
    public boolean isCorrect() {
        return correct == count;
    }

    // true if numbered
    public boolean isNumbered() {
        return no != 0;
    }

    // set the question number
    public void number(int num) {
        if (no == 0) {
            no = num;

            // q might be null at this point
            if (q == null)
                throw new IllegalStateException("The question is missing! : Question " + String.valueOf(no));

            q.number(no);
        }
    }

    // Save the reference to question text
    private void findQuestion(View v) throws IllegalStateException {
        if (v instanceof Question) {
            if (!((Question) v).isSeen()) {
                qcount++; // found a new question
                ((Question) v).setSeen();

                //ensure there is only one question per group
                if (qcount > 1)
                    throw new IllegalStateException("There can be only one question in a group: " + ((Question) v).getText());
            }
            q = (Question) v;
        }
    }

    // Perform counting
    private void countVariants(View v) throws IllegalStateException {
        if ((v instanceof Numbered) && !(v instanceof Question)) {
            if (!((Numbered) v).isNumbered()) {
                count++; // found a new variant

                if (!(v instanceof OneChoice))
                    throw new IllegalStateException("Only OneChoice can be a child of a OneChoiceQuestion ! : Variant " + String.valueOf(count));

                // set the variant number
                ((Numbered) v).number(count);

                // count the correct variants
                if (((Numbered) v).isCorrect()) {
                    increment();

                }
            }
        }
    }


    // Find the question text and variant answers, before they are added to their parents
    @Override
    public void addView(View child) {
        findQuestion(child);
        countVariants(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        findQuestion(child);
        countVariants(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        findQuestion(child);
        countVariants(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        findQuestion(child);
        countVariants(child);
        super.addView(child, index, params);
    }
}
