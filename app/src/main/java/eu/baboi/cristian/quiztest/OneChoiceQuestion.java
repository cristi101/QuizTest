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
    private Question question; // reference to the text of the question
    private int no = 0; // question number
    private int questionCount = 0; //the number of questions
    private int count = 0; //the number of variants
    private int correct = 0; //the number of correct variants
    private boolean oldCorrect; //the previous value of isCorrect

    // The constructors
    public OneChoiceQuestion(Context context) {
        super(context);
        setOrientation(RadioGroup.VERTICAL);
    }
    public OneChoiceQuestion(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(RadioGroup.VERTICAL);
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

            // question might be null at this point
            if (question == null)
                throw new IllegalStateException("The question is missing! : Question " + String.valueOf(no));

            // initialize the oldCorrect
            if (count < 1)
                throw new IllegalStateException("There must be at least one variant for the answer! : Question " + question.getText().toString());

            // Check if there is a solution to the quiz
            int delta = count - correct;
            if (getCheckedRadioButtonId() == View.NO_ID) {// If there is no selection
                if (delta != 1)
                    throw new IllegalStateException("There is no solution! : " + question.getText().toString());
            } else { // If there is an answer selected
                if (delta != 0 && delta != 2)
                    throw new IllegalStateException("There is no solution! : " + question.getText().toString());
            }

            oldCorrect = isCorrect();
            question.number(no);
            question.setCorrect(oldCorrect);
        }
    }

    // Detect if there is a change in the truth value and notify the parent
    @Override
    public void truthChanged() {
        boolean newCorrect = isCorrect();

        // update the number of correct questions
        Counter quiz = (Counter) getParent();
        if (quiz != null && oldCorrect != newCorrect) {
            if (newCorrect)
                quiz.increment();
            else
                quiz.decrement();

            // here should set correct for question
            question.setCorrect(newCorrect);  // skip calls if not needed
        }
        oldCorrect = newCorrect;
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

            Numbered answer = (Numbered) v;
            if (!answer.isNumbered()) {// The answer is seen for the first time

                count++; // count the possible answer

                // Don't know the question number yet
                if (!(answer instanceof OneChoice))
                    throw new IllegalStateException("Only OneChoice can be a child of a OneChoiceQuestion ! : Answer " + String.valueOf(count));

                // set the answer number and perform some initialization
                answer.number(count);

                // count the correct answers
                if (answer.isCorrect()) {
                    increment();
                }
            }
        }
    }

    // Search the children

    // Save the reference to question text
    private void findQuestion(View v) throws IllegalStateException {
        if (v instanceof Question) { // Found a question

            Question newQuestion = (Question) v;
            if (!newQuestion.isSeen()) { // It is a new question

                questionCount++; // count the question
                newQuestion.setSeen(); // mark as seen

                //ensure there is only one question per group
                if (questionCount > 1)
                    throw new IllegalStateException("There can be only one question in a group: " + newQuestion.getText());
            }
            question = newQuestion; // remember the question
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
