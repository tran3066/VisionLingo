package edu.pacificu.cs.cs325.translationapp;

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

import java.util.List;
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
  private final int POS_ONE = 0;
  private final int POS_TWO = 1;
  private final int POS_THREE = 2;
  private final int SHAKE_THRESHOLD = 14;
  private final String LOG_TAG = "QuizFragment";

  private boolean bEmptyList;
  private Vocab mcTempVocab;

  private Observer<BusinessLogicUIState> mcObserver;
  private FragmentQuizBinding mcBinding;
  //private Word mcTempWord;
  private BusinessLogic mcLogic;
  private TranslatorOptions mcOptions;
  //private Translator mcTranslator;

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
            setRandomWordFromVocab ();
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

    mcObserver = new Observer<BusinessLogicUIState> ()
    {
      @Override
      public void onChanged (BusinessLogicUIState cBusinessLogicUIState)
      {
        Log.d (LOG_TAG, "Things Changed");
        // update changes here
        int colorInt = mcLogic.getMcUiState ().getValue ().getColor ();
        mcBinding.btnNewWord.setBackgroundColor (colorInt);
        mcBinding.btnSubmit.setBackgroundColor (colorInt);
        //        mcOptions = new TranslatorOptions.Builder ().setTargetLanguage (
        //            mcLogic.getLanguage ()).setSourceLanguage ("en").build ();
        //        mcTranslator = Translation.getClient (mcOptions);
      }
    };

    mcLogic.getMcUiState ().observe (getActivity (), mcObserver);
    setRandomWordFromVocab ();
    mcBinding.btnSubmit.setOnClickListener (v -> {
      //      if (mcBinding.tvAnswerWord.getText ().toString ()
      //          .equals (mcTempWord.getMcEnglishWord ()))
      //      {
      //        getActivity ().runOnUiThread (() -> {
      //          Log.d (LOG_TAG, "Correct");
      //          Toast.makeText (getActivity ().getApplicationContext (),
      //              "Answered Correctly", Toast.LENGTH_LONG).show ();
      //        });
      //      }
      //      else
      //      {
      //        getActivity ().runOnUiThread (() -> {
      //          Log.d (LOG_TAG, "Incorrect");
      //          Log.d (LOG_TAG, "Correct answer:" + mcTempWord.getMcEnglishWord ());
      //          Log.d (LOG_TAG,
      //              "Incorrect answer:" + mcBinding.tvAnswerWord.toString ());
      //          Toast.makeText (getActivity ().getApplicationContext (),
      //              "Answered Incorrectly", Toast.LENGTH_LONG).show ();
      //        });
      //      }

      if (bEmptyList)
      {
        Log.d (LOG_TAG, "Nothing to Check, Vocab List Empty");
      }
      else
      {
        Log.d (LOG_TAG, "Vocab Word Found");
        if (mcBinding.tvAnswerWord.getText ().toString ()
            .equals (mcTempVocab.getWord ().getMcEnglishWord ()))
        {
          Log.d (LOG_TAG, "Correct");
          Toast.makeText (getActivity ().getApplicationContext (),
              "Answered Correctly", Toast.LENGTH_LONG).show ();
        }
        else
        {
          Log.d (LOG_TAG, "InCorrect");
          Toast.makeText (getActivity ().getApplicationContext (),
              "Answered Incorrectly", Toast.LENGTH_LONG).show ();
        }
      }

    });

    mcBinding.btnNewWord.setOnClickListener (v -> {
      setRandomWordFromVocab ();
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
   * Obtains a random word from the dictionary
   *
   * @return a random word from the dictionary
   */

  //  public Word getRandomWord ()
  //  {
  //    Log.d (LOG_TAG, "Generating Word");
  //    return mcLogic.getDictionaryDAO ().getRandomWord ();
  //  }

  /**
   * Obtains a random word from the dictionary and sets it to the
   * tvQuestionWord textview
   */

  //  public void setRandomWord ()
  //  {
  //    Log.d (LOG_TAG, "Setting RandomWord");
  //    mcTempWord = getRandomWord ();
  //    Task<String> cResult = mcTranslator.translate (
  //            mcTempWord.getMcEnglishWord ())
  //        .addOnSuccessListener (new OnSuccessListener<String> ()
  //        {
  //          @Override
  //          public void onSuccess (String cS)
  //          {
  //            getActivity ().runOnUiThread (() -> {
  //              mcBinding.tvQuestionWord.setText (cS);
  //            });
  //          }
  //        }).addOnFailureListener (new OnFailureListener ()
  //        {
  //          @Override
  //          public void onFailure (@NonNull Exception cException)
  //          {
  //            getActivity ().runOnUiThread (() -> {
  //              mcBinding.tvQuestionWord.setText (mcTempWord.getMcEnglishWord ());
  //            });
  //          }
  //        });
  //  }
  public void setRandomWordFromVocab ()
  {
    List<Vocab> cUserVocab = mcLogic.getUser ().getMcVocabList ();
    Random cRand = new Random ();
    if (!cUserVocab.isEmpty ())
    {
      Vocab cRandomVocab = cUserVocab.get (cRand.nextInt (cUserVocab.size ()));
      mcTempVocab = cRandomVocab;
      getActivity ().runOnUiThread (() -> {
        mcBinding.tvQuestionWord.setText (cRandomVocab.getTranslatedWord ());
        Log.d (LOG_TAG, "Found Vocab Word");
      });

      bEmptyList = false;
    }
    else
    {
      getActivity ().runOnUiThread (() -> {
        mcBinding.tvQuestionWord.setText ("Vocab List Empty");
        Log.d (LOG_TAG, "Vocab List Empty");
        Toast.makeText (getActivity ().getApplicationContext (),
            "Vocab List Empty", Toast.LENGTH_LONG).show ();
      });

      bEmptyList = true;
    }

  }
}