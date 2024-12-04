package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

     FragmentListBinding mcBinding;


    public ListFragment ()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
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