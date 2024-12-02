package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityListBinding;

/**
 * Creates a ListActivity class that contains a vocabulary list containing words
 * obtained from the camera screen or from the word the user searched on
 * InfoActivity. This screen is similar to InfoActivity, except it provides a
 * history of all the words detected and added, displayed in a RecyclerView.
 * This vocabulary list will be used as a way for the user to refer back to the
 * words they have already scanned. The words in the vocabulary list will be
 * used in the vocabulary quiz in QuizActivity. If the user wants to add more
 * words they can use the navigation bar to take them to either CameraActivity,
 * the camera, to take picture of objects for the words, or to InfoActivity,
 * to search up more words for their vocabulary list.
 *
 * @author Jason Tran
 */

public class ListActivity extends AppCompatActivity
{
  private VocabRecyclerViewAdapter mcAdapter;
  private ActivityListBinding mcBinding;

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
    mcBinding = ActivityListBinding.inflate (getLayoutInflater ());
    View cView = mcBinding.getRoot ();
    setContentView (cView);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

    mcBinding.rvWords.setHasFixedSize (true);
    mcBinding.rvWords.setLayoutManager (new LinearLayoutManager (this));

    mcBinding.bottomNavigationView.setOnItemSelectedListener (item -> {

      if (item.getItemId() == R.id.camera) {
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
        return true;
      }

      else if (item.getItemId() == R.id.preferences) {

        Intent preferencesIntent = new Intent(this, PreferenceActivity.class);
        startActivity(preferencesIntent);
        return true;
      }

      else if (item.getItemId() == R.id.wordInformation) {

        Intent wordIntent = new Intent(this, InfoActivity.class);
        startActivity(wordIntent);
        return true;
      }

      else if (item.getItemId() == R.id.quiz) {
        Intent quizIntent = new Intent(this, QuizActivity.class);
        startActivity(quizIntent);
        return true;
      }

      else if (item.getItemId() == R.id.vocabularyList) {
        Intent listIntent = new Intent(this, ListActivity.class);
        startActivity(listIntent);
        return true;
      }
      return false;
    });

  }

  /**
   * onResume method that reinitializes CourseRecyclerViewAdapter and sets it
   * to the RecyclerView every time the activity becomes visible to the user
   */

  @Override
  public void onResume ()
  {
    super.onResume ();
    mcAdapter = new VocabRecyclerViewAdapter (
        HomeActivity.mcCurrentUser.getMcVocabList ());
    mcBinding.rvWords.setAdapter (mcAdapter);
  }
}