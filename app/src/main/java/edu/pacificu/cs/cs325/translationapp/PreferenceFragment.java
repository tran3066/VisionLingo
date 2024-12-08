package edu.pacificu.cs.cs325.translationapp;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentPreferenceBinding;

/**
 * Creates a PreferenceActivity class that allows the user to select the
 * language that they are learning (French, Spanish) and the main color of the
 * app (red, green, blue, etc.). The user changes their learning language or app
 * color in a spinner. Clicking the confirm preferences button updates the
 * user's preferences throughout the app.
 *
 * @author AaJanae Henry, Jason Tran, Christian Flores
 */

public class PreferenceFragment extends Fragment
{
  private final int NUM_THREADS = 1;
  private final String LOG_TAG = "PreferenceFragment";
  private final String FRENCH = "fr";
  private final String SPANISH = "es";

  private FragmentPreferenceBinding mcBinding;
  private Observer<BusinessLogicUIState> mcObserver;
  private UserDAO mcUserDAO;
  private UserDB mcUserDB;
  //private List<User> usersFromDB;
  private String mcSelectedLanguage;
  private String mcSelectedColor;
  private ExecutorService mcRunner;
  //private UserPreference mcUserPref;
  private BusinessLogic mcLogic;
  //private BusinessLogicUIState mcUiLogic;


  private int mColor;

  /**
   * Initializes PreferenceFragment (required empty public constructor)
   */

  public PreferenceFragment ()
  {
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
    // Inflate the layout for this fragment
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentPreferenceBinding.inflate (
        getLayoutInflater ());
    View cView = mcBinding.getRoot ();

    return cView;
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

    mcRunner = Executors.newFixedThreadPool (NUM_THREADS);

    assert getActivity () != null;
    mcLogic = new ViewModelProvider (getActivity ()).get (BusinessLogic.class);

    String[] cLanguageArray = new String[] { "French", "Spanish" };
    assert getActivity () != null;
    ArrayAdapter<String> cLanguageAdapter = new ArrayAdapter<> (getActivity (),
        android.R.layout.simple_spinner_dropdown_item, cLanguageArray);
    cLanguageAdapter.setDropDownViewResource (
        android.R.layout.simple_spinner_dropdown_item);
    mcObserver = new Observer<BusinessLogicUIState> ()
    {
      @Override
      public void onChanged (BusinessLogicUIState businessLogicUIState)
      {
        // update changes here
        int colorInt = mcLogic.getMcUiState ().getValue ().getColor ();
        mcBinding.btnConfirm.setBackgroundColor (colorInt);
      }
    };
    mcLogic.getMcUiState ().observe (getActivity (), mcObserver);

    String[] cColorArray = new String[] { "Pink", "Red", "Green", "Blue" };
    ArrayAdapter<String> cColorAdapter = new ArrayAdapter<> (getActivity (),
        android.R.layout.simple_spinner_dropdown_item, cColorArray);
    cColorAdapter.setDropDownViewResource (
        android.R.layout.simple_spinner_dropdown_item);

    mcBinding.languageSpinner.setAdapter (cLanguageAdapter);
    mcBinding.colorSpinner.setAdapter (cColorAdapter);

    mcBinding.colorSpinner.setOnItemSelectedListener (
        new AdapterView.OnItemSelectedListener ()
        {
          @Override
          public void onItemSelected (AdapterView<?> cAdapterView, View cView,
              int position, long id)
          {
            assert getActivity () != null;
            Toast.makeText (getActivity ().getApplicationContext (),
                    (String) cAdapterView.getSelectedItem (), Toast.LENGTH_SHORT)
                .show ();
            mcSelectedColor = (String) cAdapterView.getItemAtPosition (
                position);
            Log.d (LOG_TAG, "Color selected: " + mcSelectedColor);

            switch (mcSelectedColor)
            {
              case "Pink":
                mColor = Color.MAGENTA;
                break;
              case "Red":
                mColor = Color.RED;
                break;
              case "Green":
                mColor = Color.GREEN;
                break;
              case "Blue":
                mColor = Color.BLUE;
                break;
              default:
                mColor = 0;
                break;
            }
          }

          @Override
          public void onNothingSelected (AdapterView<?> cParentView)
          {
          }
        });

    mcBinding.languageSpinner.setOnItemSelectedListener (
        new AdapterView.OnItemSelectedListener ()
        {
          @Override
          public void onItemSelected (AdapterView<?> cAdapterView, View cView,
              int position, long id)
          {
            assert getActivity () != null;
            Toast.makeText (getActivity ().getApplicationContext (),
                    (String) cAdapterView.getSelectedItem (), Toast.LENGTH_SHORT)
                .show ();
            mcSelectedLanguage = (String) cAdapterView.getItemAtPosition (
                position);
            Log.d (LOG_TAG, "Language selected: " + mcSelectedLanguage);
          }

          @Override
          public void onNothingSelected (AdapterView<?> cParentView)
          {
          }
        });

    mcBinding.btnConfirm.setOnClickListener (v -> {
      mcLogic.setColor (mColor);
      mcLogic.getUser ().setColor (mcSelectedColor);
      if (mcSelectedLanguage.equals ("French"))
      {
        mcLogic.setLanguage (FRENCH);
        mcLogic.getUser ().setLanguage (FRENCH);
      }
      else if (mcSelectedLanguage.equals ("Spanish"))
      {
        mcLogic.setLanguage (SPANISH);
        mcLogic.getUser ().setLanguage (SPANISH);
      }

      //mcUserPref = new UserPreference(mcSelectedColor, mcSelectedLanguage);
      //mcLogic.getUser().setMcUserPreference(mcUserPref);

      mcRunner.execute (() -> {
        try
        {
          mcUserDB = Room.databaseBuilder (
                  getActivity ().getApplicationContext (), UserDB.class, "User-DB")
              .fallbackToDestructiveMigrationOnDowngrade ().build ();
          mcUserDAO = mcUserDB.userDao ();
          //usersFromDB = mcUserDAO.getAll ();
          mcUserDAO.update (mcLogic.getUser ());

          Log.d (LOG_TAG, String.valueOf (mcLogic.getUser ().getMUid ()));
          Log.d (LOG_TAG, "Updated Users: " + mcLogic.getUser ().toString ());
        }
        catch (Exception cException)
        {
          throw new RuntimeException (cException);
        }
      });

      Toast.makeText (getActivity ().getApplicationContext (),
          "Preferences Confirmed: Please Choose Activity Below",
          Toast.LENGTH_SHORT).show ();
    });
  }

}