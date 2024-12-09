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
import android.widget.Toast;

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
  private final int ROTATION = 90;

  private Observer<BusinessLogicUIState> mcObserver;
  private BusinessLogic mcLogic;
  private FragmentInfoBinding mcBinding;
  //private TranslatorOptions mcOptions;
  //private Translator mcTranslator;
  private UserDAO mcUserDAO;
  private boolean bSearched;

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
    bSearched = false;
    ExecutorService mcRunner = Executors.newFixedThreadPool (1);
    mcRunner.execute (() -> {
      try
      {
        UserDB mcUserDB = Room.databaseBuilder (
                getActivity ().getApplicationContext (), UserDB.class, "User-DB")
            .fallbackToDestructiveMigrationOnDowngrade ().build ();
        mcUserDAO = mcUserDB.userDao ();
        //usersFromDB = mcUserDAO.getAll ();
      }
      catch (Exception e)
      {
        throw new RuntimeException (e);
      }
    });

    mcLogic = new ViewModelProvider (getActivity ()).get (BusinessLogic.class);

    if (mcLogic.getWordFromCamera () != null)
    {
      mcRunner.execute (()->
      {
        Word tempWord = mcLogic.getWord (mcLogic.getWordFromCamera ());
        Task<String> cResult = mcLogic.getTranslator ().translate (
                tempWord.getMcEnglishWord ())
            .addOnSuccessListener (new OnSuccessListener<String> ()
            {
              @Override
              public void onSuccess (String cS)
              {
                Log.d (LOG_TAG, "translation successful");
                Log.d (LOG_TAG, cS);
                getActivity ().runOnUiThread (() -> {
                  mcBinding.tvWordTranslate.setText (cS);
                });
              }
            }).addOnFailureListener (new OnFailureListener ()
            {
              @Override
              public void onFailure (@NonNull Exception cException)
              {
                Log.d (LOG_TAG, "translation unsuccessful");
                getActivity ().runOnUiThread (() -> {
                  mcBinding.tvWordTranslate.setText (
                      tempWord.getMcEnglishWord ());
                });
              }
            });
        getActivity ().runOnUiThread (()->
        {
          mcBinding.tvSearch.setText (mcLogic.getWordFromCamera ());
          mcBinding.tvWordInfo.setText (tempWord.toString ());
          bSearched = true;
        });

      });

      //      mcBinding.tvWordInfo.setText()
    }
    else
    {
      Log.d (LOG_TAG, "No TEXT");
    }

    if (mcLogic.getImage () != null)
    {
      Bitmap cBitmap = BitmapFactory.decodeByteArray (mcLogic.getImage (), 0,
          mcLogic.getImage ().length);
      mcBinding.imgWord.setRotation (ROTATION);
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

        mcBinding.btnSpeak.setOnClickListener (v -> {
          String cUpdatedURL;
          String cCurrentLanguage = mcLogic.getLanguage ();

          if (cCurrentLanguage.equals (FRENCH))
          {
            cUpdatedURL = FRENCH_URL.replace ("*",
                mcBinding.tvWordTranslate.getText ().toString ());
            openURL (cUpdatedURL);
          }
          else if (cCurrentLanguage.equals (SPANISH))
          {
            cUpdatedURL = SPANISH_URL.replace ("*",
                mcBinding.tvWordTranslate.getText ().toString ());
            openURL (cUpdatedURL);
          }
        });
      }
    };
    mcBinding.btnAdd.setOnClickListener (v -> {

      mcRunner.execute (() -> {
        if (bSearched)
        {
          String tempString = mcBinding.tvSearch.getText ().toString ();
          Vocab newVocab = new Vocab (mcLogic.getWord (tempString),
              mcLogic.getImage (),
              mcBinding.tvWordTranslate.getText ().toString ());

          mcLogic.getUser ().addToVocab (newVocab);
          mcUserDAO.update (mcLogic.getUser ());
        }

      });

    });

    mcBinding.btnSearch.setOnClickListener (v -> {
      Log.d (LOG_TAG, "btnSearch Pressed");
      bSearched = true;
      mcRunner.execute (() -> {
        String cTempString;
        Word cTempWord;
        cTempString = mcBinding.tvSearch.getText ().toString ();
        try
        {
          cTempWord = mcLogic.getWord (cTempString);
          getActivity ().runOnUiThread (() -> {
            mcBinding.tvWordInfo.setText (cTempWord.toString ());
            Log.d (LOG_TAG, "btnSearch Pressed");
          });
          Task<String> cResult = mcLogic.getTranslator ().translate (
                  cTempWord.getMcEnglishWord ())
              .addOnSuccessListener (new OnSuccessListener<String> ()
              {
                @Override
                public void onSuccess (String cS)
                {
                  Log.d (LOG_TAG, "translation successful");
                  Log.d (LOG_TAG, cS);
                  getActivity ().runOnUiThread (() -> {
                    mcBinding.tvWordTranslate.setText (cS);
                  });
                }
              }).addOnFailureListener (new OnFailureListener ()
              {
                @Override
                public void onFailure (@NonNull Exception cException)
                {
                  Log.d (LOG_TAG, "translation unsuccessful");
                  getActivity ().runOnUiThread (() -> {
                    mcBinding.tvWordTranslate.setText (
                        cTempWord.getMcEnglishWord ());
                  });
                }
              });
        }
        catch (Exception e)
        {
          Toast.makeText (getActivity ().getApplicationContext (),
              "Could Not Find in Dictionary", Toast.LENGTH_LONG).show();
        }
      });

      mcLogic.resetImg ();
      mcBinding.imgWord.setImageResource (R.drawable.baseline_add_a_photo_24);
      mcBinding.imgWord.setRotation (ROTATION);
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