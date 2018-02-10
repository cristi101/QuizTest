package eu.baboi.cristian.quiztest;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

/**
 * Created by cristi on 06.02.2018.
 */

public class Listener implements CompoundButton.OnCheckedChangeListener {
    private Context _context;

    Listener(Context context) {
        _context = context;
    }

    // This finds the main activity when a view context is given
    public static Activity getActivity(Context c) {
        Context context = c;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    // This gets called for every OneChoice or MultiChoice variant
    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        OneChoice oc;
        MultiChoice mc;

        // Find the TextAnswer in focus and hide the keyboard
        InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        // Get the view with the focus
        Activity a = getActivity(_context);
        View f = a.getCurrentFocus();

        // Record the text changed
        if ((f != null) && (f instanceof TextAnswer)) ((TextAnswer) f).changed();

        // Must be direct childs
        if (v instanceof OneChoice) {
            oc = (OneChoice) v;

            // update the number of correct variants
            if (oc.isCorrect()) {
                ((OneChoiceQuestion) oc.getParent()).increment();
            } else {
                ((OneChoiceQuestion) oc.getParent()).decrement();
            }


        } else if (v instanceof MultiChoice) {
            mc = (MultiChoice) v;

            // update the number of correct variants
            if (mc.isCorrect()) {
                ((MultiChoiceQuestion) mc.getParent()).increment();
            } else {
                ((MultiChoiceQuestion) mc.getParent()).decrement();
            }

        }

    }

}
