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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Random;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentQuizBinding;

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

public class QuizFragment extends Fragment {

    private Observer<BusinessLogicUIState> mcObserver;
    private FragmentQuizBinding mcBinding;
    private Word mcTempWord;
    private BusinessLogic mcLogic;

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

            }
        };
        //need to change to language
        TranslatorOptions options = new TranslatorOptions.Builder()
            .setTargetLanguage (mcLogic.getUiState ().getValue ().getLanguage ())
            .setSourceLanguage ("en")
            .build();
        Translator translator = Translation.getClient (options);
        translator.downloadModelIfNeeded ();

        mcTempWord = getRandomWord ();

        String tempString;
        Task<String> result = translator.translate(mcTempWord.getMcEnglishWord ())
            .addOnSuccessListener (new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    getActivity ().runOnUiThread (() -> {
                        tempString = s;

                    });
                }
            });



        mcLogic.getUiState ().observe (getActivity (), mcObserver);
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
            mcBinding.tvQuestionWord.setText (getRandomWord ()
                .getMcEnglishWord ());
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

}