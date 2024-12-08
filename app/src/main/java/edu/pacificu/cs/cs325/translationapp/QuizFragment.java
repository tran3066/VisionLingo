package edu.pacificu.cs.cs325.translationapp;

import static androidx.core.content.ContextCompat.getSystemService;
//import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import static edu.pacificu.cs.cs325.translationapp.HomeActivity.mcDictionaryDAO;

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

public class QuizFragment extends Fragment
{
  private final String LOG_TAG = "QuizFragment";
  private final int POS_ONE = 0;
  private final int POS_TWO = 1;
  private final int POS_THREE = 2;
  private final int SHAKE_THRESHOLD = 14;
  private final int SIZE_DATABASE = 36657;

  private Observer<BusinessLogicUIState> mcObserver;
  private FragmentQuizBinding mcBinding;
  private Word mcTempWord;
  private BusinessLogic mcLogic;
  private TranslatorOptions mcOptions;
  private Translator mcTranslator;

  /**
   * Initializes QuizFragment (required empty public constructor)
   */

  public QuizFragment ()
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
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentQuizBinding.inflate (
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
    Log.d (LOG_TAG, "Quiz OnView Created");
    SensorManager cSensorManager = (SensorManager) requireContext ().getSystemService (
        Context.SENSOR_SERVICE);
    Sensor cSensorShake = cSensorManager.getDefaultSensor (
        Sensor.TYPE_ACCELEROMETER);

    SensorEventListener cSensorEventListener = new SensorEventListener ()
    {
      @SuppressLint ("SetTextI18n")
      @Override
      public void onSensorChanged (SensorEvent cSensorEvent)
      {

        if (cSensorEvent != null)
        {
          float x_accl = cSensorEvent.values[POS_ONE];
          float y_accl = cSensorEvent.values[POS_TWO];
          float z_accl = cSensorEvent.values[POS_THREE];

          float floatSum =
              Math.abs (x_accl) + Math.abs (y_accl) + Math.abs (z_accl);

          if (floatSum > SHAKE_THRESHOLD)
          {
            mcBinding.tvQuestionWord.setText ("Word from Shake");
          }
          else
          {
            //mcBinding.tvQuestionWord.setText ("No shake");
          }
        }
      }

      @Override
      public void onAccuracyChanged (Sensor cSensor, int accuracy)
      {

      }
    };

    cSensorManager.registerListener (cSensorEventListener, cSensorShake,
        SensorManager.SENSOR_DELAY_NORMAL);
    assert getActivity () != null;

    //        mcOptions = new TranslatorOptions.Builder()
    //            .setTargetLanguage (mcLogic.getMcUiState ().getValue ().getLanguage ())
    //            .setSourceLanguage ("en")
    //            .build();
    //        mcTranslator = Translation.getClient (mcOptions);
    //        getLifecycle().addObserver(mcTranslator);
    //        mcTranslator.downloadModelIfNeeded ().addOnSuccessListener (new OnSuccessListener<Void> ()
    //        {
    //            @Override
    //            public void onSuccess (Void unused)
    //            {
    //                getActivity ().runOnUiThread (()->
    //                {
    //                    int duration = Toast.LENGTH_SHORT;
    //                    Toast cToast = Toast.makeText (getActivity (),
    //                        "Model Downloaded",
    //                        duration);
    //                    cToast.show ();
    //                });
    //            }
    //        });

    //getActivity().findViewById(android.R.id.content).setBackgroundResource(mcColor);

    mcObserver = new Observer<BusinessLogicUIState> ()
    {
      @Override
      public void onChanged (BusinessLogicUIState cBusinessLogicUIState)
      {
        Log.d (LOG_TAG, "Things Changed");
        //update changes here
        int colorInt = mcLogic.getMcUiState ().getValue ().getColor ();
        mcBinding.btnNewWord.setBackgroundColor (colorInt);
        mcBinding.btnSubmit.setBackgroundColor (colorInt);
        mcOptions = new TranslatorOptions.Builder ().setTargetLanguage (
            mcLogic.getLanguage ()).setSourceLanguage ("en").build ();
        mcTranslator = Translation.getClient (mcOptions);
      }
    };
    //need to change to language

    mcLogic.getMcUiState ().observe (getActivity (), mcObserver);
    setRandomWord ();
    mcBinding.btnSubmit.setOnClickListener (v -> {
      if (mcBinding.tvAnswerWord.toString ()
          .equals (mcTempWord.getMcEnglishWord ()))
      {
        getActivity ().runOnUiThread (() -> {
          Toast.makeText (getActivity ().getApplicationContext (),
              "Answered Correctly", Toast.LENGTH_LONG);
        });

      }
      else
      {
        getActivity ().runOnUiThread (() -> {
          Toast.makeText (getActivity ().getApplicationContext (),
              "Answered Incorrectly", Toast.LENGTH_LONG);
        });
      }
    });

    mcBinding.btnNewWord.setOnClickListener (v -> {
      setRandomWord ();
    });
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
    mcBinding = null;
    mcLogic.getMcUiState ().removeObserver (mcObserver);
  }

  /**
   * Generates a random number based on the number of items in the database
   *
   * @return a random number from 1 to the size of the database
   */

  public int generateRandomNumber ()
  {
    Log.d (LOG_TAG, "Generating Number");
    Random cTemp = new Random ();
    return cTemp.nextInt (SIZE_DATABASE - 1) + 1;
  }

  /**
   * Obtains a random word from the dictionary
   *
   * @return a random word from the dictionary
   */

  public Word getRandomWord ()
  {
    Log.d(LOG_TAG, "Generating Word");
    return mcDictionaryDAO.getRandomWord ();
  }

  /**
   * Obtains a random word from the dictionary and sets it to the
   * tvQuestionWord textview
   */

  public void setRandomWord ()
  {
    Log.d (LOG_TAG, "Setting RandomWord");
    mcTempWord = getRandomWord ();
    Task<String> cResult = mcTranslator.translate (
            mcTempWord.getMcEnglishWord ())
        .addOnSuccessListener (new OnSuccessListener<String> ()
        {
          @Override
          public void onSuccess (String cS)
          {
            getActivity ().runOnUiThread (() -> {
              mcBinding.tvQuestionWord.setText (cS);
            });
          }
        }).addOnFailureListener (new OnFailureListener ()
        {
          @Override
          public void onFailure (@NonNull Exception cException)
          {
            getActivity ().runOnUiThread (() -> {
              mcBinding.tvQuestionWord.setText (mcTempWord.getMcEnglishWord ());
            });
          }
        });

    //translation
  }
}