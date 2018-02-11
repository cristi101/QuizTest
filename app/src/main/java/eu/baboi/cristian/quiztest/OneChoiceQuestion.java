package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by cristi on 06.02.2018.
 */

public class OneChoiceQuestion extends RadioGroup implements Counter, Numbered {
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


    // The Numbered interface methods

    // The question is correct if the number of true variants is the number of variants
    @Override
    public boolean isCorrect() {
        return correct == count;
    }

    // true if numbered
    @Override
    public boolean isNumbered() {
        return no != 0;
    }

    // set the question number and perform some initialization
    @Override
    public void number(int num) throws IllegalStateException {
        if (no == 0) {
            no = num;
            // Here the view doesn't have a parent yet so we cannot update the parent counters here

            // initialize the mCorrect
            if (count < 1)
                throw new IllegalStateException("There must be at least one variant for the answer! : Question " + String.valueOf(no));
            mCorrect = isCorrect();

            // q might be null at this point
            if (q == null)
                throw new IllegalStateException("The question is missing! : Question " + String.valueOf(no));

            q.number(no);
        }
    }

    // Detect if there is a change in the truth value and notify the parent
    @Override
    public void truthChanged() {
        boolean cCorrect = isCorrect();

        // update the number of correct questions
        Counter c = (Counter) getParent();
        if (c != null && mCorrect != cCorrect) {
                if (cCorrect) {
                    c.increment();
                } else {
                    c.decrement();
                }
        }

        mCorrect = cCorrect;
    }

    // The Counter interface methods

    // Count the correct answers and update the truth value of the question
    public void increment() {
        correct++;
        truthChanged();
    }

    public void decrement() {
        correct--;
        truthChanged();
    }

    // Count all the possible answers and initialize them
    @Override
    public void countChildren(View v) throws IllegalStateException {
        if ((v instanceof Numbered) && !(v instanceof Question)) {// Found an answer

            Numbered a = (Numbered) v;
            if (!a.isNumbered()) {// The answer is seen for the first time

                count++; // count the possible answer

                // Don't know the question number yet
                if (!(a instanceof OneChoice))
                    throw new IllegalStateException("Only OneChoice can be a child of a OneChoiceQuestion ! : Answer " + String.valueOf(count));

                // set the answer number and perform some initialization
                a.number(count);

                // count the correct answers
                if (a.isCorrect()) {
                    increment();
                }
            }
        }
    }

    // Search the children

    // Save the reference to question text
    private void findQuestion(View v) throws IllegalStateException {
        if (v instanceof Question) { // Found a question

            Question qq = (Question) v;
            if (!qq.isSeen()) { // It is a new question

                qcount++; // count the question
                qq.setSeen(); // mark as seen

                //ensure there is only one question per group
                if (qcount > 1)
                    throw new IllegalStateException("There can be only one question in a group: " + qq.getText());
            }
            q = qq; // remember the question
        }
    }

    // Find the question text and variant answers, before they are added to their parents
    @Override
    public void addView(View child) {
        findQuestion(child);
        countChildren(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        findQuestion(child);
        countChildren(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        findQuestion(child);
        countChildren(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        findQuestion(child);
        countChildren(child);
        super.addView(child, index, params);
    }
}
