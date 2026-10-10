package com.example.ryo_q;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ซ่อน Status Bar
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_review);

        LinearLayout container = findViewById(R.id.reviewContainer);
        View btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        List<Question> questionList = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        List<Integer> selectedAnswers = (ArrayList<Integer>) getIntent().getSerializableExtra("selectedAnswers");

        if (questionList == null || selectedAnswers == null || container == null) {
            return;
        }

        for (int i = 0; i < questionList.size(); i++) {
            Question q = questionList.get(i);
            int selectedIndex = (i < selectedAnswers.size()) ? selectedAnswers.get(i) : -1;
            container.addView(buildQuestionCard(i + 1, q, selectedIndex));
        }
    }

    private View buildQuestionCard(int questionNumber, Question q, int selectedIndex) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card_button);
        card.setPadding(dp(12), dp(12), dp(12), dp(12));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = dp(12);
        card.setLayoutParams(cardParams);

        // หัวข้อ: ข้อที่ + ถูก/ผิด
        TextView header = new TextView(this);
        boolean isCorrect = selectedIndex == q.getCorrectAnswerIndex();
        header.setText("Question " + questionNumber + "  " + (isCorrect ? "✓ Correct" : "✗ Incorrect"));
        header.setTextColor(getColor(isCorrect ? R.color.correct_color : R.color.wrong_color));
        header.setTextSize(14f);
        header.setTypeface(null, Typeface.BOLD);
        card.addView(header);

        // คำถาม/โจทย์ (Prompt / Comment)
        if (q.getQuestionText() != null && !q.getQuestionText().isEmpty()) {
            TextView promptView = new TextView(this);
            promptView.setText(q.getQuestionText());
            promptView.setTextColor(getColor(R.color.gold_accent));
            promptView.setTextSize(13f);
            promptView.setTypeface(null, Typeface.BOLD);
            promptView.setPadding(0, dp(4), 0, dp(4));
            card.addView(promptView);
        }

        // โค้ด
        TextView code = new TextView(this);
        code.setText(q.getCodeSnippet());
        code.setTextColor(getColor(R.color.text_primary));
        code.setTypeface(Typeface.MONOSPACE);
        code.setTextSize(13f);
        code.setPadding(0, dp(8), 0, dp(8));
        card.addView(code);

        // ตัวเลือกทั้งหมด
        String[] options = q.getOptions();
        for (int j = 0; j < options.length; j++) {
            TextView optionView = new TextView(this);
            String prefix;
            int color;

            if (j == q.getCorrectAnswerIndex()) {
                prefix = "✓ ";
                color = R.color.correct_color;
            } else if (j == selectedIndex) {
                prefix = "✗ ";
                color = R.color.wrong_color;
            } else {
                prefix = "   ";
                color = R.color.text_secondary;
            }

            optionView.setText(prefix + options[j]);
            optionView.setTextColor(getColor(color));
            optionView.setTypeface(Typeface.MONOSPACE);
            optionView.setTextSize(13f);
            optionView.setPadding(0, dp(2), 0, dp(2));
            card.addView(optionView);
        }

        if (selectedIndex == -1) {
            TextView noAnswer = new TextView(this);
            noAnswer.setText("(No answer — time's up)");
            noAnswer.setTextColor(getColor(R.color.text_secondary));
            noAnswer.setTextSize(11f);
            noAnswer.setPadding(0, dp(4), 0, 0);
            card.addView(noAnswer);
        }

        return card;
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}