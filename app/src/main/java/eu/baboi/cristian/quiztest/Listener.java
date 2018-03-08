package eu.baboi.cristian.quiztest;

import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by cristi on 06.02.2018.
 */

class Listener implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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
        if (!v.isFocused()) v.requestFocusFromTouch();
    }
}
