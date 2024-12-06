package edu.pacificu.cs.cs325.translationapp;

import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityListBinding;
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
  private VocabRecyclerViewAdapter mcAdapter;
  private FragmentListBinding mcBinding;
  private DividerItemDecoration mcDivider;
  private LinearLayoutManager mcLayoutManager;
  private BusinessLogic mcLogic;

  public ListFragment ()
  {
    // Required empty public constructor
  }

  @Override
  public View onCreateView (@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState)
  {
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentListBinding.inflate (
        getLayoutInflater ());
    return mcBinding.getRoot ();
  }

  @Override
  public void onViewCreated (@NonNull View view,
      @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated (view, savedInstanceState);
    BusinessLogic mcLogic = new ViewModelProvider (this).get (
        BusinessLogic.class);

    assert getActivity () != null;
    getActivity ().findViewById (android.R.id.content)
        .setBackgroundResource (mcColor);

    mcBinding.rvWords.setHasFixedSize (true);
    mcBinding.rvWords.setLayoutManager (new LinearLayoutManager (getActivity ()));
    mcLayoutManager = new LinearLayoutManager (getActivity ());
    mcDivider = new DividerItemDecoration (getActivity (),
        mcLayoutManager.getOrientation ());
    mcBinding.rvWords.addItemDecoration (mcDivider);
  }

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);

  }

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
    mcAdapter = new VocabRecyclerViewAdapter (
        mcLogic.getUser ().getMcVocabList ());
    mcBinding.rvWords.setAdapter (mcAdapter);
  }
}