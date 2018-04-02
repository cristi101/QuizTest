package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cristi on 06.02.2018.
 */

public class MultiChoice extends android.support.v7.widget.AppCompatCheckBox implements Numbered {
    private int no = 0; // This is the variant number
    private boolean correct = false; //This is assumed to be incorrect

    // Custom attributes
    // see https://developer.android.com/training/custom-views/create-view.html

    // The constructors
    public MultiChoice(Context context) {
        super(context);
        init(context);
    }

    public MultiChoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MultiChoice,
                0, 0);
        try {
            // find the value of the correct attribute
            setCorrect(a.getBoolean(R.styleable.MultiChoice_correct, false));
        } finally {
            a.recycle();
        }
        init(context);
    }

    public MultiChoice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MultiChoice,
                0, 0);
        try {
            // find the value of the correct attribute
            setCorrect(a.getBoolean(R.styleable.MultiChoice_correct, false));
        } finally {
            a.recycle();
        }
        init(context);
    }

    // Perform some initialization
    private void init(Context context) {
        // force some attributes
        setSaveEnabled(true);
        setTextIsSelectable(false);// must be false so that the soft keyboard appear
        setSelectAllOnFocus(false);// selects all field content on focus
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
    // Also called by the system on restore from rotation
    @Override
    public void truthChanged() { // Called from Listener

        // update the number of correct answers
        Counter multiChoiceQuestion = (Counter) getParent();
        if (multiChoiceQuestion != null)
            if (isCorrect())
                multiChoiceQuestion.increment();
            else
                multiChoiceQuestion.decrement();

    }

}
