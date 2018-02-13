package eu.baboi.cristian.quiztest;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by cristi on 13.02.2018.
 */

public class VerifyButton extends AppCompatButton {

    // The constructors
    public VerifyButton(Context context) {
        super(context);
        init(context);
    }

    public VerifyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerifyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // Force some attributes
    private void init(Context c) {
        setFocusable(true);
        setFocusableInTouchMode(false);
        setOnClickListener(MainActivity.getActivity(c));
    }

}
