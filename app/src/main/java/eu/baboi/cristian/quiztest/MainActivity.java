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
    private static final String DISCLOSED = "DISCLOSED"; // ScrollView save position key
    private static final String SCROLL_POSITION = "SCROLL_POS"; // ScrollView save position key
    private boolean disclosed = false; // The disclosure state
    private int id = 0; // The new id counter
    private ScrollView scrollView; // The top ScrollView
    private Quiz quiz; // The Quiz

    // Static methods

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

    // Traverse the View hierarchy and disclose the questions
    private static void disclose(boolean disclosed, ViewGroup root) {
        final int childCount = root.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            final View child = root.getChildAt(i);
            if (child instanceof Question) {
                Question question = (Question) child;
                question.setDisclose(disclosed);
            } else if (child instanceof ViewGroup) {
                disclose(disclosed, (ViewGroup) child);
            }
        }
    }

    // Protected methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the top view
        View view = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        if (!(view instanceof ScrollView))
            throw new IllegalStateException("The top view must be a ScrollView!");
        scrollView = (ScrollView) view;

        // Find the Quiz
        view = scrollView.getChildAt(0);
        if (!(view instanceof Quiz))
            throw new IllegalStateException("The ScrollView must contain a Quiz!");
        quiz = (Quiz) view;

    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        //  save disclosed state
        out.putBoolean(DISCLOSED, disclosed);
        // Save the scroll position
        out.putIntArray(SCROLL_POSITION, new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }


    @Override
    protected void onRestoreInstanceState(Bundle in) {
        super.onRestoreInstanceState(in);

        // restore the disclosed state
        disclosed = in.getBoolean(DISCLOSED);
        if (disclosed) disclose();

        // Restore the scroll position
        final int[] pos = in.getIntArray(SCROLL_POSITION);
        if (pos != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(pos[0], pos[1]);
                }
            });
        }
    }

    // Utility methods

    // Turn on the disclosed state
    private void disclose() {
        disclosed = true;
        disclose(disclosed, scrollView);
    }

    // This is where the answers are checked
    private void checkAnswers(View v) {

        // Turn on the disclosed state
        if (!disclosed) {
            scrollView.scrollTo(0, 0); // Go to the beginning
            disclose();
        }

        // Display the quiz result
        int count = quiz.getQuestionsNumber();
        int correct = quiz.getCorrectCount();
        Toast.makeText(this, getString(R.string.app_answer, correct, count), Toast.LENGTH_LONG).show();
    }

    // This generates unique id for the answers that don't have one
    public int genID() {
        id++;
        return id;
    }

    // The Check Answers button onClick
    @Override
    public void onClick(View v) {
        // Get out of any EditText
        if (!v.isFocused()) v.requestFocusFromTouch();
        checkAnswers(v);
    }
}
