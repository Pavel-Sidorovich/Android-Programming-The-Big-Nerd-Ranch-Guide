package com.pavesid.quiz.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.animation.addListener
import com.pavesid.quiz.R
import kotlinx.android.synthetic.main.activity_cheat.*


class CheatActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.pavesid.quiz.ui.answer_is_true"
        private const val EXTRA_ANSWER_SHOWN = "com.pavesid.quiz.ui.answer_shown"
        private const val EXTRA_COUNT_HINT = "com.pavesid.quiz.ui.count_hint"
        private const val ANSWER_SHOWN = "ANSWER_SHOWN"
        private const val COUNT_HINT = "COUNT_HINT"

        fun newIntent(packageContext: Context, answerIsTrue: Boolean, count: Int): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            intent.putExtra(EXTRA_COUNT_HINT, count)
            return intent
        }

        fun answerShown(result: Intent): Boolean {
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }

        fun countHint(result: Intent): Int {
            return result.getIntExtra(EXTRA_COUNT_HINT, 3)
        }
    }

    lateinit var answerTextView: TextView
    lateinit var showAnswerButton: Button
    lateinit var apiTextView: TextView

    var answerIsTrue = false
    var answerIsShown = false
    private var countHintThis = 3
    private var countHint = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsShown = savedInstanceState?.getBoolean(ANSWER_SHOWN) ?: false

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        countHint = intent.getIntExtra(EXTRA_COUNT_HINT, 3)
        countHintThis = savedInstanceState?.getInt(COUNT_HINT) ?: countHint

        answerTextView = tv_answer
        showAnswerButton = btn_show_answer
        apiTextView = tv_API

        tv_API.text = "Осталось подсказок: $countHintThis \n${getString(R.string.api)}: ${Build.VERSION.SDK_INT}"

        if(countHintThis < 1){
            showAnswerButton.isEnabled = false
        }

        if(answerIsShown){
            showAnswerButton.visibility = View.INVISIBLE
            if (answerIsTrue) {
                answerTextView.setText(R.string.true_button)
            } else {
                answerTextView.setText(R.string.false_button)
            }
        }

        showAnswerButton.setOnClickListener {
            countHintThis--
            if (answerIsTrue) {
                answerTextView.setText(R.string.true_button)
            } else {
                answerTextView.setText(R.string.false_button)
            }
            answerIsShown = true
            setResult(true, countHintThis)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val cx = showAnswerButton.width / 2
                val cy = showAnswerButton.height / 2
                val radius = showAnswerButton.width.toFloat()

                val anim: Animator = ViewAnimationUtils.createCircularReveal(showAnswerButton, cx, cy, radius, 0f)
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        showAnswerButton.visibility = View.INVISIBLE
                    }
                })
                anim.start()
            } else {
                showAnswerButton.visibility = View.INVISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ANSWER_SHOWN, answerIsShown)
        outState.putInt(COUNT_HINT, countHintThis)
    }

    private fun setResult(isAnswerShown: Boolean, countHint: Int) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        data.putExtra(EXTRA_COUNT_HINT, countHint)
        setResult(Activity.RESULT_OK, data)
    }
}
