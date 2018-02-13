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

public class MultiChoiceQuestion extends LinearLayout implements Counter, Numbered {
    private Question q; // reference to the text of the question
    private int no = 0; // question number
    private int count = 0; //the number of variants
    private int qcount = 0; //the number of questions
    private int correct = 0; //the number of correct variants
    private boolean mCorrect; //the previous value of isCorrect

    // The constructors
    public MultiChoiceQuestion(Context context) {
        super(context);
    }

    public MultiChoiceQuestion(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiChoiceQuestion(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiChoiceQuestion(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

            // Throw an error if there is no question found
            if (q == null)
                throw new IllegalStateException("The question is missing! : Question " + String.valueOf(no));

            // initialize the mCorrect
            if (count < 1)
                throw new IllegalStateException("There must be at least one answer! : Question " + q.getText().toString());
            mCorrect = isCorrect();

            // Does not check if there is a solution to the quiz
            // For MultiChoice should be always possible to find one

            // initialize the question
            q.number(no);
            q.setCorrect(mCorrect);
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
            q.setCorrect(cCorrect); // skip calls if not needed
        }
        mCorrect = cCorrect;
    }


    // The Counter interface methods

    // Count the correct answers and update the truth value of the question
    @Override
    public void increment() {
        correct++;
        truthChanged();
    }

    @Override
    public void decrement() {
        correct--;
        truthChanged();
    }

    // Count all the possible answers and initialize them
    @Override
    public void countChildren(View v) throws IllegalStateException {
        if ((v instanceof Numbered) && !(v instanceof Question)) { // Found an answer

            Numbered a = (Numbered) v;
            if (!a.isNumbered()) { // The answer is seen for the first time

                count++; // count the possible answer

                // Don't know the question number yet
                if (!(a instanceof MultiChoice) && !(a instanceof TextAnswer))
                    throw new IllegalStateException("Only MultiChoice and TextAnswer can be children of a MultiChoiceQuestion! : Answer " + String.valueOf(count));

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
        if (v instanceof Question) { //Found a question

            Question qq = (Question) v;
            if (!qq.isSeen()) { //is a new question

                qcount++; // count the question
                qq.setSeen(); // mark as seen

                //ensure there is only one question per group
                if (qcount > 1)
                    throw new IllegalStateException("There can be only one question in a group! : " + qq.getText());
            }
            q = qq; // remember the question
        }
    }


    // Find the question text and count all the possible answers, before they are added to their parents
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
