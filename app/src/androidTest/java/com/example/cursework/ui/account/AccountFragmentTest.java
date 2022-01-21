package com.example.cursework.ui.account;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.cursework.MainActivity;
import com.example.cursework.R;
import com.example.cursework.databinding.FragmentAccountBinding;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;


public class AccountFragmentTest {

    @Rule
    public FragmentTestRule<?, AccountFragment> fragmentTestRule =
            FragmentTestRule.create(AccountFragment.class);

    @Test
    public void testCorrectLogIn() {
        onView(ViewMatchers.withId(R.id.etLogin)).perform(ViewActions.typeText("1"));
        onView(ViewMatchers.withId(R.id.etPassword)).perform(ViewActions.typeText("1"));
        onView(ViewMatchers.withId(R.id.btnLogIn)).perform(click());
        onView(ViewMatchers.withId(R.id.btnLogOut)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }

    @Test
    public void testNotCorrectLogIn() {
        onView(ViewMatchers.withId(R.id.etLogin)).perform(ViewActions.typeText("2"));
        onView(ViewMatchers.withId(R.id.etPassword)).perform(ViewActions.typeText("2"));
        onView(ViewMatchers.withId(R.id.btnLogIn)).perform(click());
        onView(ViewMatchers.withId(R.id.btnLogIn)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }
}

