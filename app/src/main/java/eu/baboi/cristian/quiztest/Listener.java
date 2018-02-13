package eu.baboi.cristian.quiztest;

import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by cristi on 06.02.2018.
 */

public class Listener implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    // This gets called for every OneChoice or MultiChoice variant
    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        // Notify the view of the change
        ((Numbered) v).truthChanged();
    }

    // This gets called for every OneChoice or MultiChoice variant
    @Override
    public void onClick(View v) {
        // move the focus to the current control
        v.requestFocusFromTouch();
        // Get the view with the focus
        // Activity a = MainActivity.getActivity(getContext());
        // View f = a.getCurrentFocus();
        // or
        // getRootView().findFocus()  instead ?
    }
}
