package com.eric.test.activity

import android.util.Log
import android.widget.EditText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.blankj.utilcode.util.LogUtils
import com.eric.androidstudy.R
import com.eric.androidstudy.RegistrationActivity
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationActivityViewModelTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegistrationActivity::class.java)

    @Test
    fun testFormStateWithViewModel() {
        val scenario = activityRule.scenario
        Log.d("TestLog", "Scenario initialized")

        scenario.onActivity { activity ->
            Log.d("TestLog", "Setting text")
            activity.findViewById<EditText>(R.id.editTextName).setText("John Doe")
        }

        Log.d("TestLog", "Recreating scenario")
        scenario.recreate()

        Log.d("TestLog", "Verifying UI state")
        scenario.onActivity { activity ->
            val name = activity.findViewById<EditText>(R.id.editTextName).text.toString()
            assertEquals("John Doe", name)
        }
    }
}
