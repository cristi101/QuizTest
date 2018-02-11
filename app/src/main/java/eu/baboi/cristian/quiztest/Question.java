package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by cristi on 06.02.2018.
 */

public class Question extends android.support.v7.widget.AppCompatTextView implements Numbered {
    private int no = 0; // the question number
    private boolean seen = false;

    // The constructors
    public Question(Context context) {
        super(context);
    }

    public Question(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Question(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public boolean isSeen() {
        return seen;
    }
    public void setSeen() {
        seen = true;
    }

    // The Numbered interface methods

    public boolean isCorrect() {
        return true;
    }

    public boolean isNumbered() {
        return no != 0;
    }

    // Set the question number
    public void number(int num) {
        if (no == 0) {
            no = num;

            // Set the Question number
            this.setText(String.valueOf(no) + ") " + this.getText().toString());
        }
    }

    @Override
    public void truthChanged() {

    }

}
