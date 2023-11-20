package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK

class CountDownActivity : AppCompatActivity() {
    private lateinit var oneTimeWorkRequest: OneTimeWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this)[CountDownViewModel::class.java]

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        viewModel.setInitialTime(habit.minutesFocus)

        val countDown = findViewById<TextView>(R.id.tv_count_down)
        viewModel.currentTimeString.observe(this) {
            countDown.text = it
        }


        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
        val channel = getString(R.string.notify_channel_name)
        val workManager = WorkManager.getInstance(this)

        val dataBuilder = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_TITLE, habit.title)
            .putString(NOTIFICATION_CHANNEL_ID, channel)
            .build()

        oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .addTag(NOTIF_UNIQUE_WORK)
            .setInputData(dataBuilder)
            .build()

        viewModel.eventCountDownFinish.observeForever { count ->
            if (count) {
                workManager.enqueueUniqueWork(
                    NOTIF_UNIQUE_WORK,
                    ExistingWorkPolicy.REPLACE,
                    oneTimeWorkRequest
                )
                updateButtonState(false)
            }
        }

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            updateButtonState(true)
            viewModel.startTimer()
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            updateButtonState(false)
            viewModel.resetTimer()
            workManager.cancelUniqueWork(NOTIF_UNIQUE_WORK)

        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}