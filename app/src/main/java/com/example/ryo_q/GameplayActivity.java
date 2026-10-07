package com.example.ryo_q;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameplayActivity extends AppCompatActivity {

    private static final long QUESTION_TIME_MS = 30000; // 30 วินาทีต่อข้อ

    private QuizManager quizManager;
    private CountDownTimer countDownTimer;
    private long timeLeftMs;

    private TextView textCodeSnippet;
    private TextView textTimer;
    private Button option1Button, option2Button, option3Button;

    private int totalStarsEarned = 0;
    private String language, difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        textCodeSnippet = findViewById(R.id.textCodeSnippet);
        textTimer = findViewById(R.id.textTimer);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);

        language = getIntent().getStringExtra("language");
        difficulty = getIntent().getStringExtra("difficulty");

        quizManager = new QuizManager();
        quizManager.startGame(language, difficulty);

        showCurrentQuestion();
    }

    private void showCurrentQuestion() {
        Question current = quizManager.getCurrentQuestion();

        if (current == null) {
            finishGame();
            return;
        }

        textCodeSnippet.setText(current.getCodeSnippet());

        String[] options = current.getOptions();
        option1Button.setText(options.length > 0 ? options[0] : "");
        option2Button.setText(options.length > 1 ? options[1] : "");
        option3Button.setText(options.length > 2 ? options[2] : "");

        option1Button.setOnClickListener(v -> onAnswerSelected(0));
        option2Button.setOnClickListener(v -> onAnswerSelected(1));
        option3Button.setOnClickListener(v -> onAnswerSelected(2));

        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        //ซ่อนแถบขาวๆ
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        timeLeftMs = QUESTION_TIME_MS;
        countDownTimer = new CountDownTimer(QUESTION_TIME_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMs = millisUntilFinished;
                textTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timeLeftMs = 0;
                onAnswerSelected(-1); // -1 = หมดเวลา ไม่ได้เลือกคำตอบ
            }
        };
        countDownTimer.start();
    }

    private void onAnswerSelected(int selectedIndex) {
        countDownTimer.cancel();

        boolean isCorrect = quizManager.checkAnswer(selectedIndex);
        int stars = isCorrect ? quizManager.calculateStars(timeLeftMs, QUESTION_TIME_MS) : 0;
        totalStarsEarned += stars;

        if (quizManager.hasNextQuestion()) {
            quizManager.moveToNextQuestion();
            showCurrentQuestion();
        } else {
            finishGame();
        }
    }

    private void finishGame() {
        int totalQuestions = quizManager.getTotalQuestions();
        int finalStars = 0;

        if (totalQuestions > 0) {
            double average = (double) totalStarsEarned / totalQuestions;
            finalStars = (int) Math.round(average);
            finalStars = Math.max(0, Math.min(3, finalStars));
        }

        ScoreManager.saveStars(this, language, difficulty, finalStars);


         android.content.Intent intent = new android.content.Intent(this, ResultActivity.class);
         intent.putExtra("stars", finalStars);
         intent.putExtra("correctCount", quizManager.getCorrectCount());
         intent.putExtra("totalQuestions", totalQuestions);
         startActivity(intent);
         finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
