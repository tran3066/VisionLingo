package edu.pacificu.cs.cs325.translationapp;

import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Random;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment {

    private Observer<BusinessLogicUIState> mcObserver;
    private FragmentQuizBinding mcBinding;
    private Word mcTempWord;
    private BusinessLogic mcLogic;

    TranslatorOptions mcOptions;

    Translator mcTranslator;
    private DictionaryDAO mcDictionaryDAO;

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
        DictionaryDAO tempDAO;
        super.onViewCreated (view, savedInstanceState);
        mcLogic = new ViewModelProvider(this).get(BusinessLogic.class);
        tempDAO = mcLogic.getDAO ();
        assert getActivity() != null;
        mcOptions = new TranslatorOptions.Builder()
            .setTargetLanguage (mcLogic.getUiState ().getValue ().getLanguage ())
            .setSourceLanguage ("en")
            .build();
        mcTranslator = Translation.getClient (mcOptions);
        mcTranslator.downloadModelIfNeeded ();
        getActivity().findViewById(android.R.id.content).setBackgroundResource(mcColor);

        mcObserver = new Observer<BusinessLogicUIState>()
        {
            @Override
            public void onChanged(BusinessLogicUIState businessLogicUIState)
            {
                //update changes here
                int colorInt = mcLogic.getUiState ().getValue ().getColor ();
                mcBinding.btnNewWord.setBackgroundColor (colorInt);
                mcBinding.btnSubmit.setBackgroundColor (colorInt);
                mcOptions = new TranslatorOptions.Builder()
                    .setTargetLanguage (mcLogic.getLanguage ())
                    .setSourceLanguage ("en")
                    .build();
                mcTranslator.downloadModelIfNeeded ();
            }
        };
        //need to change to language


        mcLogic.getUiState ().observe (getActivity (), mcObserver);
        setRandomWord ();
        mcBinding.btnSubmit.setOnClickListener (v->
        {
            if(mcBinding.tvAnswerWord.toString ()
                .equals (mcBinding.tvQuestionWord.toString ()))
            {
                //correct

            }
            else {
                //wrong
            }
        });
        mcBinding.btnNewWord.setOnClickListener (v-> {
            setRandomWord ();
        });


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

    public int generateRandomNumber()
    {
        int size = mcLogic.getDAO ().getSize ();
        Random temp = new Random ();
        return temp.nextInt (size - 1) + 1;
    }

    public Word getRandomWord()
    {
        return  mcDictionaryDAO.getWord (generateRandomNumber ());
    }

    public void setRandomWord()
    {
        mcTempWord = getRandomWord ();
        Task<String> result = mcTranslator.translate(mcTempWord.getMcEnglishWord ())
            .addOnSuccessListener (new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    getActivity ().runOnUiThread (() -> {
                        mcBinding.tvQuestionWord.setText (s);
                    });
                }
            }).addOnFailureListener (new OnFailureListener ()
            {
                @Override
                public void onFailure (@NonNull Exception e)
                {
                    getActivity ().runOnUiThread (() -> {
                        mcBinding.tvQuestionWord.setText(mcTempWord.getMcEnglishWord ());
                    });
                }
            });
    }
}