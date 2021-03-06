package com.example.cs496_tabbed.mathquiz

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cs496_tabbed.R
import java.util.concurrent.TimeUnit

/**
 * Created by acer on 06-Apr-16.
 */
class NormalQuizActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    val themeKey = "currentTheme"
    var quesList: List<Question>? = null
    var score = 0
    var qid = 0
    var currentQ: Question? = null
    var txtQuestion: TextView? = null
    var times: TextView? = null
    var scored: TextView? = null
    var button1: Button? = null
    var button2: Button? = null
    var button3: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        when (sharedPreferences.getString(themeKey, "Color0")) {
            "Color0" -> theme.applyStyle(R.style.Color0, true)
            "Color1" -> theme.applyStyle(R.style.Color1, true)
            "Color2" -> theme.applyStyle(R.style.Color2, true)
            "Color3" -> theme.applyStyle(R.style.Color3, true)
            "Color4" -> theme.applyStyle(R.style.Color4, true)
            "Color5" -> theme.applyStyle(R.style.Color5, true)
            "Color6" -> theme.applyStyle(R.style.Color6, true)
            "Color7" -> theme.applyStyle(R.style.Color7, true)
            "Color8" -> theme.applyStyle(R.style.Color8, true)
        }
        setContentView(R.layout.activity_quiz)
        val db = QuizHelper(this) // my question bank class
        quesList = db.allQuestions // this will fetch all quetonall questions
        currentQ = quesList?.get(qid) // the current question
        txtQuestion = findViewById<View>(R.id.txtQuestion) as TextView
        // the textview in which the question will be displayed
// the three buttons,
// the idea is to set the text of three buttons with the options from question bank
        button1 = findViewById<View>(R.id.button1) as Button
        button2 = findViewById<View>(R.id.button2) as Button
        button3 = findViewById<View>(R.id.button3) as Button
        // the textview in which score will be displayed
        scored = findViewById<View>(R.id.score) as TextView
        // the timer
        times = findViewById<View>(R.id.timers) as TextView
        // method which will set the things up for our game
        setQuestionView()
        times!!.text = "00:02:00"
        // A timer of 60 seconds to play for, with an interval of 1 second (1000 milliseconds)
        val timer = CounterClass(60000, 1000)
        timer.start()
        // button click listeners
        button1!!.setOnClickListener { // passing the button text to other method
// to check whether the anser is correct or not
// same for all three buttons
            getAnswer(button1!!.text.toString())
        }
        button2!!.setOnClickListener { getAnswer(button2!!.text.toString()) }
        button3!!.setOnClickListener { getAnswer(button3!!.text.toString()) }
    }

    fun getAnswer(AnswerString: String) {
        if (currentQ!!.aNSWER == AnswerString) {
// if conditions matches increase the int (score) by 1
// and set the text of the score view
            score++
            scored!!.text = "Score : $score"
        } else {
// if unlucky start activity and finish the game
            val intent = Intent(
                this@NormalQuizActivity,
                ResultActivity::class.java
            )
            // passing the int value
            val b = Bundle()
            b.putInt("score", score) // Your score
            intent.putExtras(b) // Put your score to your next
            startActivity(intent)
            finish()
        }
        if (qid < 10) {
// if questions are not over then do this
            currentQ = quesList!![qid]
            setQuestionView()
        } else {
// if over do this
            val intent = Intent(
                this@NormalQuizActivity,
                ResultActivity::class.java
            )
            val b = Bundle()
            b.putInt("score", score) // Your score
            intent.putExtras(b) // Put your score to your next
            startActivity(intent)
            finish()
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    inner class CounterClass(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(
        millisInFuture,
        countDownInterval
    ) {
        override fun onFinish() {
            times!!.text = "Time is up"
            val intent = Intent(
                this@NormalQuizActivity,
                ResultActivity::class.java
            )
            val b = Bundle()
            b.putInt("score", score) // Your score
            intent.putExtras(b) // Put your score to your next
            startActivity(intent)
            finish()
        }

        override fun onTick(millisUntilFinished: Long) {
// TODO Auto-generated method stub
            val hms = String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(
                    millisUntilFinished
                )
                        - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS
                        .toHours(millisUntilFinished)
                ), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                        - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS
                        .toMinutes(millisUntilFinished)
                )
            )
            println(hms)
            times!!.text = hms
        }
    }

    private fun setQuestionView() {
// the method which will put all things together
        txtQuestion!!.text = currentQ!!.qUESTION
        button1!!.text = currentQ!!.oPTA
        button2!!.text = currentQ!!.oPTB
        button3!!.text = currentQ!!.oPTC
        qid++
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }
}