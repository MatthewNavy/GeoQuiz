package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATBOOL = "cheatbool";
    private static final String KEY_CHEATSLEFT = "cheatsleft";
    private static final int REQUEST_CODE_CHEAT = 0;



    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mCheatsLeftTextView;


    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_australia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_africa, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_asia, true),

            };

    private int mCurrentIndex = 0;
    private int mSumCorrect = 0;
    private int mAnswerCounter = 0;
    private boolean mIsCheater;
    private int mCheatsLeft = 3;
    private boolean mIsStopped = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATBOOL, false);
            mCheatsLeft = savedInstanceState.getInt(KEY_CHEATSLEFT);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }

        });


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mQuestionBank[mCurrentIndex].isAnswered())
                {
                    checkAnswer(true);
                    mQuestionBank[mCurrentIndex].setAnswered(true);
                    mAnswerCounter++;
                }
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mQuestionBank[mCurrentIndex].isAnswered())
                {
                    checkAnswer(false);
                    mQuestionBank[mCurrentIndex].setAnswered(true);
                    mAnswerCounter++;
                }
            }

        });



        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
           {
               mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
               mIsCheater = false;
               updateQuestion();
           }


        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mCurrentIndex = (mCurrentIndex + (mQuestionBank.length - 1)) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });


        if(!mIsStopped) {
            CharSequence cheatCounterText = "Cheats left: " + mCheatsLeft;
            mCheatsLeftTextView = (TextView) findViewById(R.id.cheats_left_view);
            mCheatsLeftTextView.append(cheatCounterText);


            if (mCheatsLeft > 0) {
                mCheatButton = (Button) findViewById(R.id.cheat_button);
                mCheatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                        Intent intent = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue);
                        startActivityForResult(intent, REQUEST_CODE_CHEAT);
                    }

                });
            }

        }


        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT)
        {
            if (data == null)
            {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if(mIsCheater)
            {
                mCheatsLeft--;
            }
            mQuestionBank[mCurrentIndex].setCheated(true);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        if (mIsStopped)
        {
            CharSequence cheatsLeftText = "" + mCheatsLeft;
            mCheatsLeftTextView.append(cheatsLeftText);

            if (mCheatsLeft > 0)
            {
                mCheatButton.invalidate();
            }
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG,"onResume() called");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATBOOL, mIsCheater);
        savedInstanceState.putInt(KEY_CHEATSLEFT, mCheatsLeft);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(TAG,"onStop() called");
        mIsStopped = true;

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }


    private void updateQuestion()
    {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        boolean cheatedQuestion = mQuestionBank[mCurrentIndex].isCheated();
        int messageResId = 0;

        if(mIsCheater || cheatedQuestion)
        {
            messageResId = R.string.judgement_toast;
        }
        else
        {
            if(userPressedTrue == answerIsTrue)
            {
                mSumCorrect++;
                messageResId = R.string.correct_toast;
            }
            else
            {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();


        if(mAnswerCounter == mQuestionBank.length-1)
        {
            float grade = (float)mSumCorrect / (float)mQuestionBank.length * 100;
            CharSequence gradeText = "You got " + (int)grade + "% correct!";
            Toast.makeText(QuizActivity.this, gradeText, Toast.LENGTH_LONG).show();
        }

    }

}
