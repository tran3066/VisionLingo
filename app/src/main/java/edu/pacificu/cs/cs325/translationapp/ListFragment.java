package edu.pacificu.cs.cs325.translationapp;

//import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentListBinding;

/**
 * Creates a ListFragment class that contains a vocabulary list containing words
 * obtained from the camera screen or from the word the user searched on
 * InfoFragment. This screen is similar to InfoFragment, except it provides a
 * history of all the words detected and added, displayed in a RecyclerView.
 * This vocabulary list will be used as a way for the user to refer back to the
 * words they have already scanned. The words in the vocabulary list will be
 * used in the vocabulary quiz in QuizFragment. If the user wants to add more
 * words they can use the navigation bar to take them to either CameraFragment,
 * the camera, to take picture of objects for the words, or to InfoFragment,
 * to search up more words for their vocabulary list.
 *
 * @author Jason Tran
 */

public class ListFragment extends Fragment
{
  private final int NUM_THREADS = 1;

  private VocabRecyclerViewAdapter mcAdapter;
  private FragmentListBinding mcBinding;
  private DividerItemDecoration mcDivider;
  private LinearLayoutManager mcLayoutManager;
  private BusinessLogic mcLogic;
  private ExecutorService mcRunner;

  /**
   * Initializes ListFragment (required empty public constructor)
   */

  public ListFragment ()
  {
  }

  /**
   * onCreateView method that creates and returns the root view of the fragment
   *
   * @param cInflater           LayoutInflater object used to inflate the
   *                            fragment's view
   * @param cContainer          ViewGroup object that contains the fragment's UI
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   * @return the root view of the fragment
   */

  @Override
  public View onCreateView (@NonNull LayoutInflater cInflater,
      ViewGroup cContainer, Bundle cSavedInstanceState)
  {
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentListBinding.inflate (
        getLayoutInflater ());

    return mcBinding.getRoot ();
  }

  /**
   * onViewCreated method that is called after the fragment is created
   *
   * @param cView               the root view of the fragment
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  public void onViewCreated (@NonNull View cView,
      @Nullable Bundle cSavedInstanceState)
  {
    super.onViewCreated (cView, cSavedInstanceState);
    mcLogic = new ViewModelProvider (getActivity ()).get (BusinessLogic.class);
    mcRunner = Executors.newFixedThreadPool (NUM_THREADS);
    mcBinding.rvWords.setHasFixedSize (true);
    mcBinding.rvWords.setLayoutManager (
        new LinearLayoutManager (getActivity ()));
    mcLayoutManager = new LinearLayoutManager (getActivity ());
    mcDivider = new DividerItemDecoration (getActivity (),
        mcLayoutManager.getOrientation ());
    mcBinding.rvWords.addItemDecoration (mcDivider);

    // testing
    DictionaryDAO mcDictionaryDAO = mcLogic.getDAO ();
    Log.d ("Quiz", String.valueOf (mcDictionaryDAO == null)); // false
  }

  /**
   * onCreate method that starts the fragment
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  public void onCreate (Bundle cSavedInstanceState)
  {
    super.onCreate (cSavedInstanceState);
  }

  /**
   * onDestroyView method that is called when the fragment is destroyed
   */

  @Override
  public void onDestroyView ()
  {
    super.onDestroyView ();
  }

  /**
   * onResume method that reinitializes VocabRecyclerViewAdapter and sets it
   * to the RecyclerView every time the fragment becomes visible to the user
   */

  @Override
  public void onResume ()
  {
    super.onResume ();

    mcRunner.execute (() -> {
      mcAdapter = new VocabRecyclerViewAdapter (
          mcLogic.getUser ().getMcVocabList ());
    });

    mcBinding.rvWords.setAdapter (mcAdapter);
  }
}