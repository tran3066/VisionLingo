package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Creates a QuizActivity class that tests the user's knowledge on the
 * collection of words that they received from the app so far. The user is able
 * to shake the phone to display a new word from the translated dictionary or
 * simply press the "New Word" button. The "New Word" button will grab words
 * from the Vocabulary list, to display the translated word in the first text
 * box. The user must then correctly put that word in English in the box below.
 * Once the user clicks submit, we will then show a toast to tell the user if
 * what they entered is the correct English word or not.
 *
 * @author AaJanae Henry, Jason Tran, Christian Flores
 */

public class QuizActivity extends AppCompatActivity
{
  /**
   * onCreate method that starts the activity
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  protected void onCreate (Bundle cSavedInstanceState)
  {
    super.onCreate (cSavedInstanceState);
    EdgeToEdge.enable (this);
    setContentView (R.layout.activity_quiz);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

  }
}