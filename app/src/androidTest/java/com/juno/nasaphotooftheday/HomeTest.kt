package com.juno.nasaphotooftheday

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.juno.nasaphotooftheday.view.HomeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeTest {

    @get:Rule
    var rule: ActivityTestRule<HomeActivity> =
        ActivityTestRule(HomeActivity::class.java, false, true)

    @Test
    fun launchActivity(){
        rule.launchActivity(Intent())
    }

    @Test
    fun test_ui() {

        ActivityScenario.launch(HomeActivity::class.java)
        onView(withId(R.id.calendar_TV)).perform(click())
        onView(withId(R.id.action_IV)).perform(click())
        onView(withText("Home"))
        onView(withId(R.id.toolbar_title))
    }

}