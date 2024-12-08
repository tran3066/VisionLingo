package edu.pacificu.cs.cs325.translationapp;

//import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentInfoBinding;

/**
 * Creates a InfoFragment class that opens after the user presses the translate
 * button from CameraFragment. The user is provided a translation of the word
 * detected from the image, the word type (e.g., noun, verb, adjective),
 * a definition of the word, and the picture of the item from CameraFragment.
 * The user can click the button “Speak Word” to learn how to say the word
 * phonetically. The user then has the option to add this word to the vocabulary
 * list (ListFragment) or to search for a completely new word by entering text
 * in the “Search for a word…” box. Searching for a word allows the user to add
 * their own custom words they want to find the translations for, which we will
 * use the network (Oxford Dictionary) to access a dictionary to get this word.
 *
 * @author AaJanae Henry, Jason Tran, Christian Flores
 */

public class InfoFragment extends Fragment
{
  private final String LOG_TAG = "InfoFragment";
  private final String FRENCH = "fr";
  private final String SPANISH = "es";
  private final String FRENCH_URL = "https://forvo.com/word/*/#fr";
  private final String SPANISH_URL = "https://forvo.com/word/*/#es";

    private Observer<BusinessLogicUIState> mcObserver;
  private BusinessLogic mcLogic;
  private FragmentInfoBinding mcBinding;
  private TranslatorOptions mcOptions;
  private Translator mcTranslator;
  // Make sure to actually set this variable
  private String mcTranslatedWord;

  private UserDAO mcUserDAO;

  /**
   * Initializes InfoFragment (required empty public constructor)
   */

  public InfoFragment ()
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
  public View onCreateView (LayoutInflater cInflater, ViewGroup cContainer,
      Bundle cSavedInstanceState)
  {
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentInfoBinding.inflate (
        getLayoutInflater ());
    View cView = mcBinding.getRoot ();

    return cView;
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
    assert getActivity () != null;
    ExecutorService mcRunner = Executors.newFixedThreadPool (1);
    mcRunner.execute(() -> {
      try {
        UserDB mcUserDB = Room.databaseBuilder (getActivity().getApplicationContext (),
            UserDB.class,
            "User-DB").fallbackToDestructiveMigrationOnDowngrade ().build ();
        mcUserDAO = mcUserDB.userDao ();
        //usersFromDB = mcUserDAO.getAll ();
        Log.d (LOG_TAG, String.valueOf (mcLogic.getUser().getMUid ()));
        Log.d(LOG_TAG, "Updated Users: " + mcLogic.getUser ().toString ());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    mcLogic = new ViewModelProvider (getActivity ()).get (BusinessLogic.class);

    //getActivity().findViewById(android.R.id.content).setBackgroundResource(mcColor);

    if (mcLogic.getWordFromCamera () != null)
    {
      mcBinding.tvWordTranslate.setText (mcLogic.getWordFromCamera ());
      Log.d (LOG_TAG, "Text RECEIVED");
    }
    else
    {
      Log.d (LOG_TAG, "No TEXT");
    }

    if (mcLogic.getImage () != null)
    {
      Bitmap cBitmap = BitmapFactory.decodeByteArray (mcLogic.getImage (), 0,
          mcLogic.getImage ().length);
        int rotation = 90;
        mcBinding.imgWord.setRotation(rotation);
      mcBinding.imgWord.setImageBitmap (cBitmap);
      Log.d (LOG_TAG, "Picture RECEIVED");
    }
    else
    {
      Log.d (LOG_TAG, "No PICTURE");
    }

    mcObserver = new Observer<BusinessLogicUIState> ()
    {
      @Override
      public void onChanged (BusinessLogicUIState businessLogicUIState)
      {
        // update changes here
        int colorInt = mcLogic.getMcUiState ().getValue ().getColor ();
        mcBinding.btnSearch.setBackgroundColor (colorInt);
        mcBinding.btnAdd.setBackgroundColor (colorInt);
        mcBinding.btnSpeak.setBackgroundColor (colorInt);


        mcOptions = new TranslatorOptions.Builder ().setTargetLanguage (
            mcLogic.getLanguage ()).setSourceLanguage ("en").build ();
        mcTranslator = Translation.getClient (mcOptions);

        mcBinding.btnSpeak.setOnClickListener (v -> {
          String cUpdatedURL;
          String cCurrentLanguage = mcLogic.getLanguage ();

          if (cCurrentLanguage.equals (FRENCH))
          {
            cUpdatedURL = FRENCH_URL.replace ("*", mcTranslatedWord);
            openURL (cUpdatedURL);
          }
          else if (cCurrentLanguage.equals (SPANISH))
          {
            cUpdatedURL = SPANISH_URL.replace ("*", mcTranslatedWord);
            openURL (cUpdatedURL);
          }
        });
      }
    };
    mcBinding.btnAdd.setOnClickListener (v -> {
      Vocab newVocab;
      Word newWord = mcLogic.getDAO ()
          .getWordByString (mcBinding.tvSearch.getText ().toString ());
      newVocab = new Vocab (newWord,
                            mcLogic.getImage (),
                            mcBinding.tvWordTranslate.getText().toString ());
      mcLogic.getUser ().addToVocab (newVocab);
      mcUserDAO.update (mcLogic.getUser ());
    });

    mcLogic.getMcUiState ().observe (getActivity (), mcObserver);

    mcBinding.btnSearch.setOnClickListener (v -> {
      String cTempString;
      Word cTempWord;
      DictionaryDAO cTempDAO = mcLogic.getDAO ();
      cTempString = mcBinding.tvSearch.getText ().toString ();
      cTempWord = cTempDAO.getWordByString (cTempString);
      mcBinding.tvWordInfo.setText (cTempWord.toString ());
      mcLogic.resetImg ();
      Task<String> cResult = mcTranslator.translate (
              cTempWord.getMcEnglishWord ())
          .addOnSuccessListener (new OnSuccessListener<String> ()
          {
            @Override
            public void onSuccess (String cS)
            {
              getActivity ().runOnUiThread (() -> {
                mcBinding.tvWordTranslate.setText (cS);
              });
            }
          }).addOnFailureListener (new OnFailureListener ()
          {
            @Override
            public void onFailure (@NonNull Exception cException)
            {
              getActivity ().runOnUiThread (() -> {
                mcBinding.tvWordTranslate.setText (
                    cTempWord.getMcEnglishWord ());
              });
            }
          });
    });

    mcLogic.getMcUiState ().observe (getActivity (), mcObserver);
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
   * Opens the URL specified by the parameter
   *
   * @param cURL URL to open
   */

  public void openURL (String cURL)
  {
    Uri cUrl = Uri.parse (cURL);
    Intent cBrowserIntent = new Intent (Intent.ACTION_VIEW, cUrl);
    cBrowserIntent.addCategory (Intent.CATEGORY_BROWSABLE);
    startActivity (cBrowserIntent);
  }
}