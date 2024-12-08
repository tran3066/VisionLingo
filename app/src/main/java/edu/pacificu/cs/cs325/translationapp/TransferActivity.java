package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityTransferBinding;

/**
 * Creates a TransferActivity class that handles user transition between
 * different fragments in the app by managing user login, registration, and the
 * navigation bar
 *
 * @author AaJanae Henry, Christian Flores
 */

public class TransferActivity extends AppCompatActivity
{
  private ActivityTransferBinding mcBinding;
  private BusinessLogic mcLogic;
  private MenuItem mcItem;
  private UserDAO mcUserDAO;
  private UserDB mcUserDB;

  private ExecutorService mcRunner;

  /**
   * onCreate method that starts the activity
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  protected void onCreate (Bundle cSavedInstanceState)
  {
    super.onCreate (cSavedInstanceState);
    EdgeToEdge.enable (this);
    mcBinding = ActivityTransferBinding.inflate (getLayoutInflater ());
    View cView = mcBinding.getRoot ();
    setContentView (cView);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (systemBars.left, systemBars.top, systemBars.right,
              systemBars.bottom);
          return insets;
        });

    mcLogic = new ViewModelProvider (this).get (BusinessLogic.class);
    Intent mcReceiveIntent = getIntent ();
    mcRunner = Executors.newFixedThreadPool (2);
    mcRunner.execute (() ->
    {
        DictionaryDB mcDictionaryDB = Room.databaseBuilder (
            getApplicationContext (), DictionaryDB.class, "Dictionary-DB").build ();
        mcLogic.setDAO (mcDictionaryDB.dictionaryDao ());
    });

    mcRunner.execute (() ->
    {
        mcUserDB = Room.databaseBuilder (getApplicationContext (), UserDB.class,
            "User-DB").fallbackToDestructiveMigrationOnDowngrade ().build ();
        mcUserDAO = mcUserDB.userDao ();
    });
//test

    if ("NewUser".equals (mcReceiveIntent.getStringExtra ("Type")))
    {
      String username = mcReceiveIntent.getStringExtra ("Username");
      String password = mcReceiveIntent.getStringExtra ("Password");
      mcLogic.setUser (mcUserDAO.findUserByNamePass (username, password));

      getSupportFragmentManager ().beginTransaction ()
          .setReorderingAllowed (true)
          .replace (R.id.fragment_container_view, PreferenceFragment.class,
              null).commit ();
    }
    else if ("Login".equals (mcReceiveIntent.getStringExtra("Type")))
    {
      String username = mcReceiveIntent.getStringExtra ("Username");
      String password = mcReceiveIntent.getStringExtra ("Password");
      mcLogic.setUser (mcUserDAO.findUserByNamePass (username, password));

      getSupportFragmentManager ().beginTransaction ()
          .setReorderingAllowed (true)
          .add (R.id.fragment_container_view, CameraFragment.class, null)
          .commit ();
    }

    mcBinding.bottomNavigationView.setOnItemSelectedListener (item -> {
      if (item.getItemId () == R.id.camera)
      {
        getSupportFragmentManager ().beginTransaction ()
            .setReorderingAllowed (true)
            .replace (R.id.fragment_container_view, CameraFragment.class, null)
            .commit ();
        return true;
      }
      else if (item.getItemId () == R.id.preferences)
      {
        getSupportFragmentManager ().beginTransaction ()
            .setReorderingAllowed (true)
            .replace (R.id.fragment_container_view, PreferenceFragment.class,
                null).commit ();
        return true;
      }
      else if (item.getItemId () == R.id.wordInformation)
      {
        getSupportFragmentManager ().beginTransaction ()
            .setReorderingAllowed (true)
            .replace (R.id.fragment_container_view, InfoFragment.class, null)
            .commit ();
        return true;
      }
      else if (item.getItemId () == R.id.vocabularyList)
      {
        getSupportFragmentManager ().beginTransaction ()
            .setReorderingAllowed (true)
            .replace (R.id.fragment_container_view, ListFragment.class, null)
            .commit ();
        return true;
      }
      else if (item.getItemId () == R.id.quiz)
      {
        getSupportFragmentManager ().beginTransaction ()
            .setReorderingAllowed (true)
            .replace (R.id.fragment_container_view, QuizFragment.class, null)
            .commit ();
        return true;
      }
      return false;
    });
  }
}