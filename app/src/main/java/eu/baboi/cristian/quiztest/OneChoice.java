package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cristi on 06.02.2018.
 */

public class OneChoice extends android.support.v7.widget.AppCompatRadioButton implements Numbered {
    private int no = 0; // This is the variant number
    private boolean correct = false; //This is assumed to be incorrect

    // Custom attributes
    // see https://developer.android.com/training/custom-views/create-view.html

    // The constructors
    public OneChoice(Context context) {
        super(context);
        init(context);
    }

    public OneChoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OneChoice,
                0, 0);
        try {
            // find the value of the correct attribute
            setCorrect(a.getBoolean(R.styleable.OneChoice_correct, false));
        } finally {
            a.recycle();
        }
        init(context);
    }

    public OneChoice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OneChoice,
                0, 0);
        try {
            // find the value of the correct attribute
            setCorrect(a.getBoolean(R.styleable.OneChoice_correct, false));
        } finally {
            a.recycle();
        }
        init(context);
    }

    // Perform some initialization
    private void init(Context context) {
        setSaveEnabled(true);
        setFocusable(true);
        setFocusableInTouchMode(false);

        if (getId() == View.NO_ID)
            setId(MainActivity.getActivity(context).genID());
    }


    // Mark the correct answer
    public void setCorrect(boolean answer) {
        correct = answer;
    }

    // The Numbered interface methods

    @Override
    public boolean isCorrect() {
        return correct == isChecked();
    }

    @Override
    public boolean isNumbered() {
        return no != 0;
    }

    // set the number and perform some initialization
    @Override
    public void number(int num) {
        if (no == 0) {
            no = num;
            Listener listener = new Listener();
            // set the click listener
            setOnClickListener(listener);
            // Set the change listener
            setOnCheckedChangeListener(listener);
        }
    }

    // Detect if there is a change in the truth value and notify the parent
    @Override
    public void truthChanged() { // Called from Listener

        // update the number of correct answers
        Counter oneChoiceQuestion = (Counter) getParent();
        if (oneChoiceQuestion != null)
            if (isCorrect())
                oneChoiceQuestion.increment();
            else
                oneChoiceQuestion.decrement();

    }

}

