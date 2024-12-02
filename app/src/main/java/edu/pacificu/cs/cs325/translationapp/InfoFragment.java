package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment
{

  private final String LOG_TAG = "InfoActivity";
  private final String FRENCH_URL = "https://forvo.com/word/*/#fr";
  private final String SPANISH_URL = "https://forvo.com/word/*/#es";

  private FragmentInfoBinding mcBinding;

  private String mcTranslatedWord;

  public InfoFragment ()
  {
    // Required empty public constructor
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentInfoBinding.inflate (
        getLayoutInflater ());
    View view = mcBinding.getRoot ();
    return view;
  }

  @Override
  public void onViewCreated (@NonNull View view,
      @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated (view, savedInstanceState);
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

}