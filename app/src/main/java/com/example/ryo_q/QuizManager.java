package com.example.ryo_q;

import java.util.List;

public class QuizManager {
    private List<Question> questionList;
    private int currentIndex;
    private int corentCount;

    //โหลดโจทย์ตามภาษา ความยาก
    public void startGame(String language, String difficulty) {
        questionList = QuestionRepository.getQuestions(language, difficulty);
        currentIndex = 0;
        corentCount = 0;
    }

    //ดึงคำถาม   null ถ้าโจทย์หมด
    public Question getCurrentQuestion() {
        if (questionList == null || currentIndex >= questionList.size()) {
            return null;
        }
        return questionList.get(currentIndex);
    }

    //เช็คข้อถัดไป (ุถ้ามี)
    public boolean hasNextQuestion() {
        return questionList != null && currentIndex < questionList.size() - 1;
    }

    //ไปข้อถัดไป
    public void moveToNextQuestion() {
        currentIndex++;
    }

    //ตอบครบทุกข้อ
    public boolean isGameover() {
        return questionList == null || currentIndex >= questionList.size();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTotalQuestions() {
        return questionList == null ? 0 : questionList.size();
    }

    public int getCorrectCount() {
        return corentCount;
    }

    //ตรวจคำตอบ คืนtrueถ้าถูก
    public boolean checkAnswer(int selectedIndex) {
        Question current = getCurrentQuestion();
        if (current == null) {
            return false;
        }
        boolean correct = current.isCorrect(selectedIndex);
        if (correct) {
            corentCount++;
        }
        return correct;
    }

    // แปลงเวลาเป็นดาว 0-3
    public int calculateStars(long timeRemainingMs /*เวลาที่เหลือตอนตอบถูก*/, long totalTimeMs/*เวลาเริ่มของข้อนั้น*/) {
        if (totalTimeMs <= 0 || timeRemainingMs <= 0) {
            return 0;   //หมดเวลา
        }
        double ratio = (double) timeRemainingMs / totalTimeMs;
        if (ratio >= 0.6) {
            return 3;
        } else if (ratio >= 0.3) {
            return 2;
        } else if (ratio >= 0.1) {
            return 1;
        } else {
            return 0;
        }
    }
}
