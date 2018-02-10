package eu.baboi.cristian.quiztest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final String SCROLL_POSITION = "SCROLL_POS"; // ScrollView save position key

    int id = 0; // The new id counter
    ScrollView sv; // The top ScrollView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv = findViewById(R.id.scroll);
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);

        // Save the scroll position
        out.putIntArray(SCROLL_POSITION, new int[]{sv.getScrollX(), sv.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle in) {
        super.onRestoreInstanceState(in);

        // Restore the scroll position
        final int[] pos = in.getIntArray(SCROLL_POSITION);
        if (pos != null) {
            sv.scrollTo(pos[0], pos[1]);
        }
    }

    // This generates unique id for the answer variants that don't have one
    public int genID() {
        id++;
        return id;
    }


    // This is where the answers are checked
    public void checkAnswers(View v) {
        Quiz q = findViewById(R.id.quiz);
        //sv.scrollTo(0,0); // Go to the beginning
        // sv.invalidate(); //redraw the questions to show which is true
        int count = q.getQuestionsNumber();
        int correct = q.getCorrectCount();
        Toast.makeText(this, getString(R.string.app_answer, correct, count), Toast.LENGTH_LONG).show();
    }
}
