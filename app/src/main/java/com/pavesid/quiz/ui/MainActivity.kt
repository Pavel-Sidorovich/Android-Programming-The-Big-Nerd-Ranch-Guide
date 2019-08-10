package com.pavesid.quiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private var questionBank: ArrayList<Question> = ArrayList()

    private var currentIndex: Int = -1

    init {
        questionBank.add(Question(R.string.question_africa, false))
        questionBank.add(Question(R.string.question_oceans, true))
        questionBank.add(Question(R.string.question_mideast, false))
        questionBank.add(Question(R.string.question_americas, true))
        questionBank.add(Question(R.string.question_australia, true))
        questionBank.add(Question(R.string.question_asia, true))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: -1
        setContentView(R.layout.activity_main)

        trueButton = true_button
        falseButton = false_button
        nextButton = next_button
        prevButton = prev_button
        questionTextView = question_text_view

        nextQuestion()

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
//            val toast = Toast.makeText(this, getString(R.string.incorrect_toast), Toast.LENGTH_SHORT)
//            toast.setGravity(Gravity.TOP, 0, 0)
//            toast.show()
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            nextQuestion()
        }

        prevButton.setOnClickListener {
            prevQuestion()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, currentIndex - 1)
    }

    private fun nextQuestion() {
        if(currentIndex != questionBank.size - 1) {
            currentIndex++
            val question = questionBank[currentIndex].textResId
            questionTextView.setText(question)
        } else {
            val question = questionBank[currentIndex].textResId
            questionTextView.setText(question)
        }
    }

    private fun prevQuestion() {
        if (currentIndex != 0) {
            currentIndex = (currentIndex - 1) % questionBank.size
            val question = questionBank[currentIndex].textResId
            questionTextView.setText(question)
        } else {
            val question = questionBank[currentIndex].textResId
            questionTextView.setText(question)
        }
    }

    private fun checkAnswer(userPressedButton: Boolean) {
        val answer = questionBank[currentIndex].answerTrue

        val messageResId = if (userPressedButton == answer) {
            nextQuestion()
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
