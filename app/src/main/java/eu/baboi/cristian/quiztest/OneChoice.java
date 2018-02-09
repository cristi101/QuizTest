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

    // The constructors
    public OneChoice(Context context) {
        super(context);
        setSaveEnabled(true);
        setFocusableInTouchMode(false);
    }

    public OneChoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OneChoice,
                0, 0);

        try {
            setCorrect(a.getBoolean(R.styleable.OneChoice_correct, false));
            setSaveEnabled(true);
            setFocusableInTouchMode(false);

            if (getId() == View.NO_ID)
                setId(((MainActivity) Listener.getActivity(context)).genID());
        } finally {
            a.recycle();
        }
    }

    public OneChoice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OneChoice,
                0, 0);

        try {
            setCorrect(a.getBoolean(R.styleable.OneChoice_correct, false));
            setSaveEnabled(true);
            setFocusableInTouchMode(false);

            if (getId() == View.NO_ID)
                setId(((MainActivity) Listener.getActivity(context)).genID());
        } finally {
            a.recycle();
        }
    }

    // Mark the correct answer
    public void setCorrect(boolean c) {
        correct = c;
    }

    // The Numbered interface methods
    public boolean isCorrect() {
        return correct == isChecked();
    }

    @Override
    public boolean isNumbered() {
        return no != 0;
    }

    @Override
    public void number(int num) {
        if (no == 0) {
            no = num;
            setOnCheckedChangeListener(new Listener(getContext()));
        }
    }

}
