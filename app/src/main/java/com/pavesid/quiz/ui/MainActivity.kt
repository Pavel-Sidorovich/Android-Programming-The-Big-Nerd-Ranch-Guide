package com.pavesid.quiz.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.pavesid.quiz.R
import com.pavesid.quiz.data.Question
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_INDEX = "KEY_INDEX"
        const val CHEAT_INDEX = "CHEAT_INDEX"
        const val REQUEST_CODE_CHEAT = 0
        const val COUNT = "COUNT"
        const val RIGHT_ANSWER = "RIGHT_ANSWER"
    }

    private lateinit var trueBtn: Button
    private lateinit var falseBtn: Button
    private lateinit var nextBtn: ImageButton
//    private lateinit var prevButton: ImageButton
    private lateinit var cheatBtn: Button
    private lateinit var questionTextView: TextView

    private var questionBank: ArrayList<Question> = ArrayList()

    private var currentIndex: Int = -1
    private var rightAnswer = 0f
    private var isCheater = false
    private var countHint = 3

    init {
        questionBank.add(Question(R.string.question_africa, false))
        questionBank.add(Question(R.string.question_oceans, true))
        questionBank.add(Question(R.string.question_mideast, false))
        questionBank.add(Question(R.string.question_americas, true))
        questionBank.add(Question(R.string.question_australia, true))
        questionBank.add(Question(R.string.question_asia, true))
        questionBank.add(Question(R.string.warning_text, true))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: -1
        isCheater = savedInstanceState?.getBoolean(CHEAT_INDEX) ?: false
        rightAnswer = savedInstanceState?.getFloat(RIGHT_ANSWER) ?: 0f
        countHint = savedInstanceState?.getInt(COUNT) ?: 3
        setContentView(R.layout.activity_main)

        trueBtn = true_button
        falseBtn = false_button
        nextBtn = next_button
        cheatBtn = cheat_button
//        prevButton = prev_button
        questionTextView = question_text_view

        nextQuestion()

        trueBtn.setOnClickListener {
            checkAnswer(true)
        }

        falseBtn.setOnClickListener {
//            val toast = Toast.makeText(this, getString(R.string.incorrect_toast), Toast.LENGTH_SHORT)
//            toast.setGravity(Gravity.TOP, 0, 0)
//            toast.show()
            checkAnswer(false)
        }

        nextBtn.setOnClickListener {
            isCheater = false
            nextQuestion()
        }

        cheatBtn.setOnClickListener {
            val answerIsTrue = questionBank[currentIndex].answerTrue
            val intent = CheatActivity.newIntent(this, answerIsTrue, countHint)
//            startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

//        prevButton.setOnClickListener {
//            prevQuestion()
//        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, currentIndex - 1)
        outState.putBoolean(CHEAT_INDEX, isCheater)
        outState.putFloat(RIGHT_ANSWER, rightAnswer)
        outState.putInt(COUNT, countHint)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return
            }
            isCheater = CheatActivity.answerShown(data)
            countHint = CheatActivity.countHint(data)
        }
    }

    private fun nextQuestion() {
        currentIndex++
        if(currentIndex != questionBank.size - 1) {
            val question = questionBank[currentIndex].textResId
            questionTextView.setText(question)
            trueBtn.isEnabled = true
            falseBtn.isEnabled = true
        } else {
            questionTextView.text = "Ваш счет: ${rightAnswer / (questionBank.size - 1)}"
            trueBtn.visibility = View.INVISIBLE
            falseBtn.visibility = View.INVISIBLE
            nextBtn.visibility = View.INVISIBLE
            cheatBtn.visibility = View.INVISIBLE
        }
    }

//    private fun prevQuestion() {
//        if (currentIndex != 0) {
//            currentIndex = (currentIndex - 1) % questionBank.size
//            val question = questionBank[currentIndex].textResId
//            questionTextView.setText(question)
//        } else {
//            val question = questionBank[currentIndex].textResId
//            questionTextView.setText(question)
//        }
//    }

    private fun checkAnswer(userPressedButton: Boolean) {
        val answer = questionBank[currentIndex].answerTrue

        trueBtn.isEnabled = false
        falseBtn.isEnabled = false

        val messageResId = when {
            isCheater -> R.string.judgment_toast
            userPressedButton == answer -> {
                rightAnswer++
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
