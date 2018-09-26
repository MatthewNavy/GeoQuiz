package com.bignerdranch.android.geoquiz;

/**
 * Created by charg on 2/4/2018.
 */

public class Question
{
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsAnswered;
    private boolean mIsCheated;

    public Question(int textResId, boolean answerTrue)
    {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mIsAnswered = false;
        mIsCheated = false;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswered() {
        return mIsAnswered;
    }

    public void setAnswered(boolean answered) {
        mIsAnswered = answered;
    }

    public boolean isCheated() {
        return mIsCheated;
    }

    public void setCheated(boolean cheated) {
        mIsCheated = cheated;
    }
}
