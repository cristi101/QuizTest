package eu.baboi.cristian.quiztest;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by cristi on 06.02.2018.
 */

public class MultiChoice extends android.support.v7.widget.AppCompatCheckBox implements Numbered {
    private int no = 0; // This is the variant number
    private boolean correct = false; //This is assumed to be incorrect

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
    void init(Context c) {
        setSaveEnabled(true);
        setFocusableInTouchMode(false);

        if (getId() == View.NO_ID)
            setId(((MainActivity) Listener.getActivity(c)).genID());
    }

    public boolean isCorrect() {
        return correct == isChecked();
    }


    // The Numbered interface methods

    // Mark the correct answer
    public void setCorrect(boolean c) {
        correct = c;
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

            // Set the change listener
            setOnCheckedChangeListener(new Listener());
        }
    }

    // Detect if there is a change in the truth value and notify the parent
    @Override
    public void truthChanged() {

        // Find the Numbered in focus and hide the keyboard
        Context t = getContext();

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) t.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);

        // Get the view with the focus
        Activity a = Listener.getActivity(t);
        View f = a.getCurrentFocus();

        // Notify the change to the Numbered in focus
        if ((f != null) && (f instanceof Numbered)) ((Numbered) f).truthChanged();

        // update the number of correct answers
        Counter c = (Counter) getParent();
        if (c != null)
            if (isCorrect()) {
                c.increment();
            } else {
                c.decrement();
            }
    }


}
