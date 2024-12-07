package edu.pacificu.cs.cs325.translationapp;

import static androidx.core.content.ContextCompat.getSystemService;
import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
        super.onViewCreated (view, savedInstanceState);
        DictionaryDAO tempDAO;
        mcLogic = new ViewModelProvider(this).get(BusinessLogic.class);
        tempDAO = mcLogic.getDAO ();

        SensorManager sensorManager = (SensorManager) requireContext().getSystemService
                (Context.SENSOR_SERVICE);

        Sensor sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent != null)
                {
                    float x_accl = sensorEvent.values[0];
                    float y_accl = sensorEvent.values[1];
                    float z_accl = sensorEvent.values[2];

                    float floatSum = Math.abs(x_accl) + Math.abs(y_accl) + Math.abs(z_accl);

                    if (floatSum > 14)
                    {
                        mcBinding.tvQuestionWord.setText("Word from Shake");
                    }
                    else {
                        mcBinding.tvQuestionWord.setText("No shake");
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensorShake,
                SensorManager.SENSOR_DELAY_NORMAL);
        assert getActivity() != null;
        mcOptions = new TranslatorOptions.Builder()
            .setTargetLanguage (mcLogic.getMcUiState ().getValue ().getLanguage ())
            .setSourceLanguage ("en")
            .build();
        mcTranslator = Translation.getClient (mcOptions);
        getLifecycle().addObserver(mcTranslator);
        mcTranslator.downloadModelIfNeeded ().addOnSuccessListener (new OnSuccessListener<Void> ()
        {
            @Override
            public void onSuccess (Void unused)
            {
                getActivity ().runOnUiThread (()->
                {
                    int duration = Toast.LENGTH_SHORT;
                    Toast cToast = Toast.makeText (getActivity (),
                        "Model Downloaded",
                        duration);
                    cToast.show ();
                });
            }
        });

        getActivity().findViewById(android.R.id.content).setBackgroundResource(mcColor);

        mcObserver = new Observer<BusinessLogicUIState>()
        {
            @Override
            public void onChanged(BusinessLogicUIState businessLogicUIState)
            {
                //update changes here
                int colorInt = mcLogic.getMcUiState ().getValue ().getColor ();
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


        mcLogic.getMcUiState ().observe (getActivity (), mcObserver);
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

        //translation
    }
}