package eu.baboi.cristian.quiztest;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static boolean disclosed = false; // The disclosure state
    final String SCROLL_POSITION = "SCROLL_POS"; // ScrollView save position key
    private int id = 0; // The new id counter
    private ScrollView sv; // The top ScrollView
    private Quiz q; // The Quiz

    // Traverse the View hierarchy and disclose the questions
    private static void disclose(ViewGroup root) {
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            final View child = root.getChildAt(i);
            if (child instanceof Question) {
                Question qq = (Question) child;
                qq.setDisclose(disclosed);
            }

            if (child instanceof ViewGroup) {
                disclose((ViewGroup) child);
            }
        }
    }

    // This finds the main activity when a view context is given
    static MainActivity getActivity(Context c) {
        Context context = c;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (MainActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the top view
        View v = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        if (!(v instanceof ScrollView))
            throw new IllegalStateException("The top view must be a ScrollView!");
        sv = (ScrollView) v;

        // Find the Quiz
        v = sv.getChildAt(0);
        if (!(v instanceof Quiz))
            throw new IllegalStateException("The ScrollView must contain a Quiz!");
        q = (Quiz) v;
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        // no need to save disclosed

        // Save the scroll position
        out.putIntArray(SCROLL_POSITION, new int[]{sv.getScrollX(), sv.getScrollY()});
    }


    // Utility methods

    @Override
    protected void onRestoreInstanceState(Bundle in) {
        super.onRestoreInstanceState(in);

        // restore the disclosed state
        if (disclosed) disclose();

        // Restore the scroll position
        final int[] pos = in.getIntArray(SCROLL_POSITION);
        if (pos != null) {
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(pos[0], pos[1]);
                }
            });
        }
    }

    // This is where the answers are checked
    public void checkAnswers(View v) {

        // Turn on the disclosed state
        if (!disclosed) {
            sv.scrollTo(0, 0); // Go to the beginning
            disclose();
        }

        // Display the quiz result
        int count = q.getQuestionsNumber();
        int correct = q.getCorrectCount();
        Toast.makeText(this, getString(R.string.app_answer, correct, count), Toast.LENGTH_LONG).show();
    }

    // This generates unique id for the answers that don't have one
    public int genID() {
        id++;
        return id;
    }

    // Turn on the disclosed state
    private void disclose() {// not static because it needs sv
        disclosed = true;
        disclose(sv);
    }

    // The Check Answers button
    @Override
    public void onClick(View v) {
        // Get out of any EditText
        v.requestFocusFromTouch();
        checkAnswers(v);
    }
}
