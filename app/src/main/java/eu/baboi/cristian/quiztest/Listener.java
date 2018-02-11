package eu.baboi.cristian.quiztest;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.CompoundButton;

/**
 * Created by cristi on 06.02.2018.
 */

public class Listener implements CompoundButton.OnCheckedChangeListener {

    // This finds the main activity when a view context is given
    static Activity getActivity(Context c) {
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
        // Notify the view of the change
        ((Numbered) v).truthChanged();
    }

}
