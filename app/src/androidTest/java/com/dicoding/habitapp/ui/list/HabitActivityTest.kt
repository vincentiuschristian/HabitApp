package com.dicoding.habitapp.ui.list

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.add.AddHabitActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test

//TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
class HabitActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(HabitListActivity::class.java)
    }

    @Test
    fun when_tap_add_habit_AddHabitActivity_displayed() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
        onView(withId(R.id.fab)).perform(click())

        val addHabitActivity = showAddHabitActivity()
        Assert.assertTrue(addHabitActivity?.javaClass == AddHabitActivity::class.java)

        onView(withId(R.id.add_ed_title)).check(matches(isDisplayed()))
        onView(withId(R.id.add_ed_minutes_focus)).check(matches(isDisplayed()))
        onView(withId(R.id.sp_priority_level)).check(matches(isDisplayed()))
        onView(withId(R.id.add_tv_start_time)).check(matches(isDisplayed()))

    }

    private fun showAddHabitActivity(): Activity? {
        var activity: Activity? = null
        getInstrumentation().runOnMainSync {
            run {
                activity = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0)
            }
        }
        return activity
    }

}