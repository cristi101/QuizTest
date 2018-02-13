package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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
        setTextIsSelectable(true); // This also make the text focusable in both modes !!!
        setFocusable(false);
        setFocusableInTouchMode(false);

    }


    public boolean isSeen() {
        return seen;
    }
    public void setSeen() {
        seen = true;
    }

    // set to true to enable disclosure of the correct answer
    public void setDisclose(boolean c) {
        if (disclose != c) { // Skip calling refresh if nothing changed
            disclose = c;
            refreshDrawableState(); // Update the background
        }
    }

    @Override
    public boolean isCorrect() {
        return correct;
    }

    // The Numbered interface methods

    // used by the parent to inform of the parent correct status
    public void setCorrect(boolean c) {
        if (correct != c) { // Skip calling refresh if nothing changed
            correct = c;
            refreshDrawableState(); // Update the background
        }
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

    // what to do with this ?
    @Override
    public void truthChanged() {
        // no need to inform parent
        // The parent inform us
    }


    // This should change the background of the question if correct and disclose are true
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
