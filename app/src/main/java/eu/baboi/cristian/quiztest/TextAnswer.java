package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

/**
 * Created by cristi on 06.02.2018.
 */

public class TextAnswer extends android.support.v7.widget.AppCompatEditText implements Numbered {
    private int no = 0; // the variant number
    private String answer = ""; // the correct answer to the question
    private boolean mCorrect; // previous state of isCorrect

    // The constructors
    public TextAnswer(Context context) {
        super(context);
        init(context);
    }

    public TextAnswer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextAnswer,
                0, 0);

        try {
            // find the value of the answer attribute
            setAnswer(a.getString(R.styleable.TextAnswer_answer));
        } finally {
            a.recycle();
        }
        init(context);
    }

    public TextAnswer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextAnswer,
                0, 0);

        try {
            // find the value of the answer attribute
            setAnswer(a.getString(R.styleable.TextAnswer_answer));
        } finally {
            a.recycle();
        }
        init(context);
    }

    // Perform some initialization
    void init(Context c) {
        setSaveEnabled(true);
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        mCorrect = isCorrect();
        if (getId() == View.NO_ID)
            setId(((MainActivity) Listener.getActivity(c)).genID());
    }

    // Remember the correct answer
    public void setAnswer(String txt) {
        answer = txt;
    }


    // The Numbered interface methods
    public boolean isCorrect() {
        return answer.equalsIgnoreCase(this.getText().toString().trim());
    }

    @Override
    public boolean isNumbered() {
        return no != 0;
    }

    @Override
    public void number(int num) {
        if (no == 0) {
            no = num;
        }
    }

    // Detect if there is a change in the truth value and notify the parent
    @Override
    public void truthChanged() {
        boolean cCorrect = isCorrect();

        // update the number of correct answers
        Counter c = (Counter) getParent();
        if (c != null && mCorrect != cCorrect)
            if (cCorrect)
                c.increment();
            else
                c.decrement();

        mCorrect = cCorrect;
    }

    // This is to update the count when turned from portrait to landscape
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        //In the constructor getText() is the value in the layout file, not the current value
        truthChanged();
    }

    // Check when focus lost
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused) truthChanged();
    }

    // Check when action done
    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            truthChanged();
        }
    }

    // Check when back button
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            truthChanged();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
