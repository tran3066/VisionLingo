package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityHomeBinding;

/**
 * Creates a HomeActivity class that Allows the user to enter in their login
 * information so that they can access their previously scanned vocabulary they
 * have stored in the vocabulary list in ListActivity. Pressing the login button
 * for the first time redirects the user to the user preferences page
 * (PreferenceActivity), and if the user had already set their user preferences,
 * the user will go to the camera page (CameraActivity) instead. If the user
 * does not have an account, then entering a username and password automatically
 * creates an account in the database.
 *
 * @author AaJanae Henry, Jason Tran
 */

public class HomeActivity extends AppCompatActivity
{
  private final String LOG_TAG = "HomeActivity";
  private final int NUM_THREADS = 2;

  private ActivityHomeBinding mcBinding;
  private ExecutorService mcRunner;
  private UserDAO mcUserDAO;
  private UserDB mcUserDB;
  private List<User> usersFromDB;
  private DictionaryDB mcDictionaryDB;
  private DictionaryDAO mcDictionaryDAO;
  private boolean bUserFound = false;
  // protected static Dictionary mcDictionary;
  protected static User mcCurrentUser;

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
    mcBinding = ActivityHomeBinding.inflate (getLayoutInflater ());
    View cView = mcBinding.getRoot ();
    setContentView (cView);
    //setContentView (R.layout.activity_home);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

    mcRunner = Executors.newFixedThreadPool (NUM_THREADS);
    mcDictionaryDB = Room.databaseBuilder (getApplicationContext (),
        DictionaryDB.class, "Dictionary-DB").build ();
    mcDictionaryDAO = mcDictionaryDB.dictionaryDao ();

    mcRunner.execute (() -> {
      try
      {
        mcUserDB = Room.databaseBuilder (getApplicationContext (), UserDB.class,
            "User-DB").fallbackToDestructiveMigrationOnDowngrade ().build ();
        mcUserDAO = mcUserDB.userDao ();
        usersFromDB = mcUserDAO.getAll ();
      }
      catch (Exception e)
      {
        throw new RuntimeException (e);
      }

    });

    mcRunner.execute (() -> {
      if (mcDictionaryDAO.getSize () == 0)
      {
        try
        {
          URL cDictionaryURL = new URL (
              "https://zeus.cs.pacificu.edu/chadd/cs325/Cleaned_Oxford_Dictionary.txt");
          TXTDatabaseReader cReader = new TXTDatabaseReader (
              cDictionaryURL.openStream ());
          cReader.read (mcDictionaryDAO);

          runOnUiThread (() -> {
            int duration = Toast.LENGTH_SHORT;
            Toast cToast = Toast.makeText (this,
                "Database fully loaded from URL", duration);
            cToast.show ();
          });
        }
        catch (IOException cException)
        {
          throw new RuntimeException (cException);
        }
      }
    });

    Intent cIntentCam = new Intent (this, CameraActivity.class);
    Intent cIntentUserPref = new Intent (this, PreferenceActivity.class);
    mcBinding.btnLogin.setOnClickListener (v -> {
      if (usersFromDB != null) {
        for (User check : usersFromDB) {
          if (mcBinding.ptUsername.toString().equals(check.getMcUsername())) {
            bUserFound = true;
            if (mcBinding.ptPassword.toString().equals(check.getMcPassword())) {
              mcCurrentUser = check;
            } else {
              runOnUiThread(() ->
              {
                int time = Toast.LENGTH_SHORT;
                StringBuilder wordMessage = new StringBuilder();
                wordMessage.append("Incorrect Password for User: ")
                        .append(mcBinding.ptUsername.toString());
                Toast toast = Toast.makeText(this, wordMessage, time);
                toast.show();
                Log.d(LOG_TAG, "Password Incorrect Toast was shown");
              });
            }
          }
        }
          if (!bUserFound) {
            mcCurrentUser = new User(mcBinding.ptUsername.toString(), mcBinding.ptPassword.toString());
            //mcUserDAO.insert(mcCurrentUser);
            Log.d(LOG_TAG, "New user created and inserted into database");
            Log.d(LOG_TAG, "Launch User Preferences");
            //startActivity(cIntentCam);

            startActivity(cIntentUserPref);
            Log.d(LOG_TAG, "User Preferences Activity started");
            //are we inserting into the data base here or when the program ends in order to get user
            // preferences and vocab list?
          }
        }
        if (mcCurrentUser != null && bUserFound) {
          Log.d(LOG_TAG, "Launch CameraActivity from Login");
          startActivity(cIntentCam);
          Log.d(LOG_TAG, "Camera Activity started");
        }
    });
  }
}