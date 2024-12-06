package edu.pacificu.cs.cs325.translationapp;

import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import edu.pacificu.cs.cs325.translationapp.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment {

    private Observer<BusinessLogicUIState> mcObserver;
    private FragmentQuizBinding mcBinding;


    public QuizFragment ()
    {
        // Required empty public constructor

    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentQuizBinding.inflate (
                getLayoutInflater ());
        return mcBinding.getRoot ();
    }

    @Override
    public void onViewCreated (@NonNull View view,
                               @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated (view, savedInstanceState);
        BusinessLogic mcLogic = new ViewModelProvider(this).get(BusinessLogic.class);

        assert getActivity() != null;
        getActivity().findViewById(android.R.id.content).setBackgroundResource(mcColor);

        mcObserver = new Observer<BusinessLogicUIState>()
        {
            @Override
            public void onChanged(BusinessLogicUIState businessLogicUIState)
            {
                //update changes here
                int colorInt = mcLogic.getUiState ().getValue ().getColorInt ();
                mcBinding.btnNewWord.setBackgroundColor (colorInt);
                mcBinding.btnSubmit.setBackgroundColor (colorInt);

            }
        };


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