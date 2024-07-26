package com.parth.crawlappsdemo.ui.addTask.view

import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.ui.addTask.viewModel.AddTaskViewModel
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AddTaskActivityTest {

    @get:Rule
    val scenario = ActivityScenarioRule(AddTaskActivity::class.java)

    private var addTaskActivity: AddTaskActivity? = null
    private var viewModel: AddTaskViewModel? = null
    private var decorView: View? = null

    @Before
    fun setUp() {
        scenario.scenario.onActivity {
            addTaskActivity = it
        }
        viewModel = addTaskActivity?.viewModel
        scenario.scenario.onActivity { activity -> decorView = activity.window.decorView }
    }

    @After
    fun tearDown() {
    }

    private fun check(msg: String) {
        onView(withText(msg))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyTitle() {
        onView(withId(R.id.btnSubmit)).perform(click())
        check("Please enter task title")
    }

    @Test
    fun testEmptyStatus() {
        onView(withId(R.id.etTitle)).perform(replaceText("Test Title"))
        onView(withId(R.id.btnSubmit)).perform(click())
        check("Please select task status")
    }

    @Test
    fun testEmptyDueDate() {
        onView(withId(R.id.etTitle)).perform(replaceText("Test Title"))
        val latch = CountDownLatch(1)
        scenario.scenario.onActivity {
            viewModel?.taskStatusList?.let {
                latch.countDown()
            }
        }
        latch.await(2, TimeUnit.SECONDS)
        onView(withId(R.id.boxTaskStatus)).perform(click())
        onView(withText("Done"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .perform(click())
        onView(withId(R.id.etDueDates)).perform(replaceText(""))
        viewModel?.selectedTask?.task?.dueDate = 0
        onView(withId(R.id.btnSubmit)).perform(click())
        check("Please select task due date")
    }
}

class ToastMatcher : TypeSafeMatcher<Root>() {

    public override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams.get().type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken
            return windowToken === appToken
        }
        return false
    }

    override fun describeTo(description: org.hamcrest.Description?) {
        description?.appendText("is toast")
    }
}