package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by cristi on 06.02.2018.
 */

public class TextAnswer extends android.support.v7.widget.AppCompatEditText implements Numbered {
    private int no = 0; // the variant number
    private String answer = ""; // the correct answer to the question
    private boolean oldCorrect; // previous state of isCorrect

    // Custom attributes
    // see https://developer.android.com/training/custom-views/create-view.html

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
    private void init(Context context) {
        setSaveEnabled(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setTextIsSelectable(true); // This also make the text focusable in both modes !!!
        setSelectAllOnFocus(true);

        //setImeOptions(EditorInfo.IME_ACTION_NEXT);
        oldCorrect = isCorrect();

        if (getId() == View.NO_ID)
            setId(MainActivity.getActivity(context).genID());
    }

    // Remember the correct answer
    public void setAnswer(String txt) {
        answer = txt;
    }


    // The Numbered interface methods
    @Override
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
        Log.e("TextAnswer truthChanged", "Updated the answer! " + getText().toString());

        boolean newCorrect = isCorrect();

        // update the number of correct answers
        Counter multiChoiceQuestion = (Counter) getParent();
        if (multiChoiceQuestion != null && oldCorrect != newCorrect)
            if (newCorrect)
                multiChoiceQuestion.increment();
            else
                multiChoiceQuestion.decrement();

        oldCorrect = newCorrect;
    }

    // This is to update the count when turned from portrait to landscape
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        //In the constructor getText() is the value in the layout file, not the current value
        truthChanged(); // called to notify of the restored value of answer
    }

    // SANTIER

    private void showKeyboard() {
        Log.e("TextAnswer", "SHOW keyboard");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    private void hideKeyboard() {
        Log.e("TextAnswer", "HIDE keyboard");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    private void focusChanged(boolean focused) {
        if (focused) {
            Log.e("TextAnswer", "FOCUS On " + getText().toString());
            //showKeyboard();
        } else {
            Log.e("TextAnswer", "FOCUS Off " + getText().toString());
            truthChanged(); //record the changes
            // hideKeyboard();
        }
    }

    // Check when focus lost
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        Log.e("TextAnswer", "onFocusChanged " + String.valueOf(focused) + " - " + getText().toString());
        focusChanged(focused);
    }

    // Check when action done
    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        Log.e("TextAnswer", "onEditorAction: " + String.valueOf(actionCode) + " - " + getText().toString());
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            Log.e("TextAnswer", "DONE " + getText().toString());
            //truthChanged();
        }
    }

    // Check when back button
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Log.e("TextAnswer", "onKeyPreIme " + getText().toString());
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            //truthChanged();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
