package com.example.ryo_q;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ซ่อน Status Bar
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_result);

        int stars = getIntent().getIntExtra("stars", 0);
        int correctCount = getIntent().getIntExtra("correctCount", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        String language = getIntent().getStringExtra("language");
        String difficulty = getIntent().getStringExtra("difficulty");

        TextView resultTitle = findViewById(R.id.resultTitle);
        TextView correctText = findViewById(R.id.correctText);
        ImageView star1 = findViewById(R.id.star1);
        ImageView star2 = findViewById(R.id.star2);
        ImageView star3 = findViewById(R.id.star3);
        View btnPlayAgain = findViewById(R.id.btnPlayAgain);
        View btnExitHome = findViewById(R.id.btnExitHome);
        View btnReview = findViewById(R.id.btnReview);
        if (btnReview != null) {
            btnReview.setOnClickListener(v -> {
                Intent reviewIntent = new Intent(ResultActivity.this, ReviewActivity.class);
                reviewIntent.putExtra("questionList", (java.io.Serializable) getIntent().getSerializableExtra("questionList"));
                reviewIntent.putExtra("selectedAnswers", (java.io.Serializable) getIntent().getSerializableExtra("selectedAnswers"));
                startActivity(reviewIntent);
            });
        }
        if (resultTitle != null) {
            resultTitle.setText(stars > 0 ? "Robot Repaired!" : "Try Again");
        }

        if (correctText != null) {
            correctText.setText("Correct: " + correctCount + " / " + totalQuestions);
        }

        setStarFilled(star1, stars >= 1);
        setStarFilled(star2, stars >= 2);
        setStarFilled(star3, stars >= 3);

        if (btnPlayAgain != null) {
            btnPlayAgain.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, GameplayActivity.class);
                intent.putExtra("language", language);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
                finish();
            });
        }

        if (btnExitHome != null) {
            btnExitHome.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }

    private void setStarFilled(ImageView star, boolean filled) {
        if (star == null) return;
        star.setImageResource(R.drawable.star);
        star.setAlpha(filled ? 1.0f : 0.3f);
    }
}