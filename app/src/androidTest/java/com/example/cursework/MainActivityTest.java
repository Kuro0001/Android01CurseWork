package com.example.cursework;


import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.Button;
import android.widget.Toast;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.cursework.fragments.KindsFragment;
import com.example.cursework.ui.home.HomeFragment;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest extends TestCase {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);



    public void testOnAddKind() {
        Button btn = mActivityTestRule.getActivity().findViewById(R.id.btnOnKind);
        Espresso.onView(ViewMatchers.withId(R.id.btnOnKind)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.etName)).perform(ViewActions.typeText("Test_add_kind"));
    }
}