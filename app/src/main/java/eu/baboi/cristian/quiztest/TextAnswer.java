package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
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
    private String text = "";
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
        // force some attributes
        setSaveEnabled(true);
        setTextIsSelectable(false);// must be false so that the soft keyboard appear
        setSelectAllOnFocus(true);// selects all field content on focus
        setFocusable(true);
        setFocusableInTouchMode(true);// must be true so that the field is editable in touch mode
        setInputType(EditorInfo.TYPE_CLASS_TEXT); //text input
        setSingleLine();//only one line
        // set ime option
        setImeOptions(EditorInfo.IME_ACTION_DONE|EditorInfo.IME_FLAG_NO_FULLSCREEN);

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

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    private void focusChanged(boolean focused) {
        if (focused) {
            text=getText().toString();// save the current content of the field
        } else {
            hideKeyboard();//needed in case the user click on other control
            setTextKeepState(text);// restore the text value on exit
        }
    }

    // Check when focus lost
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        focusChanged(focused);
    }

    // Action Done or press Enter is the only way to enter new text
    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            text=getText().toString();//save the new content
            truthChanged();//record the change
        }
    }

    // Check when Back button is pressed and cancel the edit
    // Check when Enter is pressed and record the change
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        int action=event.getAction();
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                action == KeyEvent.ACTION_UP) {
            setTextKeepState(text);// restore the previous value
        }else if(keyCode == KeyEvent.KEYCODE_ENTER &&
                action == KeyEvent.ACTION_UP){
            text=getText().toString();
            truthChanged();
        }
        return false;
    }
}
