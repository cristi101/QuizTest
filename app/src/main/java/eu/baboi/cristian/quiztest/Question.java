package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cristi on 06.02.2018.
 */

public class Question extends android.support.v7.widget.AppCompatTextView implements Numbered {
    private static final int[] STATE_CORRECT = {R.attr.state_correct};

    private int no = 0; // the question number
    private boolean correct = false; // true if the parent is correct
    private boolean seen = false;
    private boolean disclose = false; // true if we operate in disclosure mode

    // The constructors
    public Question(Context context) {
        super(context);
        init(context);
    }

    public Question(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Question(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // force some attributes
        setSelectAllOnFocus(false);// selects all field content on focus
        setTextIsSelectable(true); // This also make the text focusable in both modes !!!
        setFocusable(true);
        setFocusableInTouchMode(true);
        if (getId() == View.NO_ID)
            setId(MainActivity.getActivity(context).genID());
    }

    // Used to number the question just once
    public boolean isSeen() {
        return seen;
    }
    public void setSeen() {
        seen = true;
    }

    // set to true to enable disclosure of the correct answer
    public void setDisclose(boolean state) {
        if (disclose != state) { // Skip calling refresh if nothing changed
            disclose = state;
            refreshDrawableState(); // Update the background
        }
    }

    // used by the parent to inform of the parent correct status
    public void setCorrect(boolean state) {
        if (correct != state) { // Skip calling refresh if nothing changed
            correct = state;
            refreshDrawableState(); // Update the background
        }
    }

    // The Numbered interface methods

    @Override
    public boolean isCorrect() {
        return correct;
    }

    @Override
    public boolean isNumbered() {
        return no != 0;
    }

    // Set the question number
    @Override
    public void number(int num) {
        if (no == 0) {
            no = num;
            // should set some event handlers to do navigation ?
            // Set the Question number
            this.setText(String.valueOf(no) + ". " + this.getText().toString());
        }
    }

    // Does nothing because we have setCorrect
    @Override
    public void truthChanged() {
    }


    // This changes the background of the question if correct and disclose are true
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (correct && disclose) {
            // We are going to add 1 extra state.
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

            mergeDrawableStates(drawableState, STATE_CORRECT);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }



}
