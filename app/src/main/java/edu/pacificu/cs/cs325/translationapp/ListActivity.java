package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
  private ArrayList<Word> mcWordList; // need to change this later
  private CourseRecyclerViewAdapter mcAdapter;
  private RecyclerView mcRVWords;

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
    setContentView (R.layout.activity_list);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

    mcRVWords = findViewById (R.id.rvWords);
    mcRVWords.setHasFixedSize (true);
    mcRVWords.setLayoutManager (new LinearLayoutManager (this));
  }

  /**
   * onResume method that reinitializes CourseRecyclerViewAdapter and sets it
   * to the RecyclerView every time the activity becomes visible to the user
   */

  @Override
  public void onResume ()
  {
    super.onResume ();
    mcAdapter = new CourseRecyclerViewAdapter (mcWordList); // list of words (?)
    mcRVWords.setAdapter (mcAdapter);
  }
}