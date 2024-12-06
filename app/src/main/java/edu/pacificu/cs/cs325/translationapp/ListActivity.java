package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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
  private DividerItemDecoration mcDivider;
  private LinearLayoutManager mcLayoutManager;
  private BusinessLogic mcLogic;

  /**
   * onCreate method that starts the activity
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  //needs to update the DB as the user adds on to the vocab list
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
    mcLayoutManager = new LinearLayoutManager (this);
    mcDivider = new DividerItemDecoration (this,
        mcLayoutManager.getOrientation ());
    mcBinding.rvWords.addItemDecoration (mcDivider);
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
        mcLogic.getUser ().getMcVocabList ());
    mcBinding.rvWords.setAdapter (mcAdapter);
  }
}