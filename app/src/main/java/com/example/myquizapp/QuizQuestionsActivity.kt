package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var progBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var tvQuestion: TextView? = null
    private var ivImage: ImageView? = null
    private var mUserName: String? = null
    private var mCorrectAnswers: Int = 0

    private var optionOne: TextView? = null
    private var optionTwo: TextView? = null
    private var optionThree: TextView? = null
    private var optionFour: TextView? = null
    private var btnSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        progBar = findViewById(R.id.prog_bar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        ivImage = findViewById(R.id.iv_image)
        optionOne = findViewById(R.id.optionOne)
        optionTwo = findViewById(R.id.optionTwo)
        optionThree = findViewById(R.id.optionThree)
        optionFour = findViewById(R.id.optionFour)
        btnSubmit = findViewById(R.id.btnSubmit)


        mQuestionsList = Constants.getQuestions()

        optionOne?.setOnClickListener(this)
        optionTwo?.setOnClickListener(this)
        optionThree?.setOnClickListener(this)
        optionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        setQuestion()

    }

    private fun setQuestion() {
        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        defaultOptionsView()
        progBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition / ${progBar?.max}"
        tvQuestion?.text = question.question
        optionOne?.text = question.optionOne
        optionTwo?.text = question.optionTwo
        optionThree?.text = question.optionThree
        optionFour?.text = question.optionFour
        ivImage?.setImageResource(question.Image)

        if (mCurrentPosition == mQuestionsList!!.size) {
            btnSubmit?.text = "FINISH"
        } else {
            btnSubmit?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        optionOne?.let {
            options.add(0, it)
        }
        optionTwo?.let {
            options.add(1, it)
        }
        optionThree?.let {
            options.add(2, it)
        }
        optionFour?.let {
            options.add(3, it)
        }

        for (o in options) {
            o.setTextColor(Color.parseColor("#7A8089"))
            o.typeface = Typeface.DEFAULT
            o.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionsView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.optionOne -> {
                optionOne?.let {
                    selectedOptionsView(it, 1)
                }
            }
            R.id.optionTwo -> {
                optionTwo?.let {
                    selectedOptionsView(it, 2)
                }
            }
            R.id.optionThree -> {
                optionThree?.let {
                    selectedOptionsView(it, 3)
                }
            }
            R.id.optionFour -> {
                optionFour?.let {
                    selectedOptionsView(it, 4)
                }
            }
            R.id.btnSubmit -> {
                if(mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when{
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this,ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this, "Congratulations for finishing the quiz!!", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition-1)

                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)


                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btnSubmit?.text = "FINISH"
                    } else {
                        btnSubmit?.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

   private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                optionOne?.background = ContextCompat.getDrawable(this,drawableView)
            }
            2 -> {
                optionTwo?.background = ContextCompat.getDrawable(this,drawableView)
            }
            3 ->{
                optionThree?.background = ContextCompat.getDrawable(this,drawableView)
            }
            4 -> {
                optionFour?.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }
}