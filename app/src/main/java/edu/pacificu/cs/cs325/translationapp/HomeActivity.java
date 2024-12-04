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
  private final int SIZE_DATABASE = 36657;
  private ActivityHomeBinding mcBinding;
  private ExecutorService mcRunner;
  private UserDAO mcUserDAO;
  private UserDB mcUserDB;
  private String mcUsername;
  private String mcPassword;
  private List<User> usersFromDB;
  private DictionaryDAO mcDictionaryDAO;
  private boolean bUserFound;
  // protected static Dictionary mcDictionary;
  protected static User mcCurrentUser;

  /**
   * onCreate method that starts the activity
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  //
  @Override
  protected void onCreate (Bundle cSavedInstanceState)
  {
    super.onCreate (cSavedInstanceState);
    EdgeToEdge.enable (this);
    mcBinding = ActivityHomeBinding.inflate (getLayoutInflater ());
    View cView = mcBinding.getRoot ();
    setContentView (cView);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

    bUserFound = false;
      int NUM_THREADS = 4;
      mcRunner = Executors.newFixedThreadPool (NUM_THREADS);


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

    buildDictionary(mcRunner);


    Intent intent = new Intent (this, TransferActivity.class);

    mcBinding.btnLogin.setOnClickListener (v -> {

      login(intent, mcRunner);
    });

    mcBinding.btnNewUser.setOnClickListener( (view -> {

      newUser(intent , mcRunner);

    }));
  }

  private void newUser (Intent intent, ExecutorService mcRunner)
  {
    mcUsername = mcBinding.ptUsername.getText().toString().trim();
    mcPassword = mcBinding.ptPassword.getText().toString().trim();

    if (mcPassword.isEmpty() || mcUsername.isEmpty())
    {
      Toast.makeText(this, "Username and password cannot be empty",
              Toast.LENGTH_SHORT).show();

      return;
    }

    mcRunner.execute (() -> {

      for (User check : usersFromDB) {
        if (mcUsername.equals(check.getMcUsername())) {
          Toast.makeText(this, "Username taken: Please login or Choose a new Username",
                  Toast.LENGTH_SHORT).show();
          return;
        }
      }
    });

    if (!bUserFound) {
      mcCurrentUser = new User(mcUsername,mcPassword);

      Log.d(LOG_TAG, "New user created");
      Log.d(LOG_TAG, "Launch User Preferences");

      intent.setAction(Intent.ACTION_SEND);
      intent.putExtra("Username", mcCurrentUser.getMcUsername());
      intent.putExtra("Password", mcCurrentUser.getMcPassword());
      intent.setType("String");
      startActivity(intent);
      Log.d(LOG_TAG, "User Preferences Activity started");

    }
  }


  private void login (Intent intent, ExecutorService mcRunner)
  {

    mcUsername = mcBinding.ptUsername.getText().toString().trim();
    mcPassword = mcBinding.ptPassword.getText().toString().trim();

    if (mcPassword.isEmpty() || mcUsername.isEmpty())
    {
      Toast.makeText(this, "Username and password cannot be empty",
              Toast.LENGTH_SHORT).show();
      return;
    }

    mcRunner.execute (() -> {
      if (usersFromDB != null) {
        for (User check : usersFromDB) {
          if (mcUsername.equals(check.getMcUsername())) {
            bUserFound = true;
            if (mcPassword.equals(check.getMcPassword())) {
              mcCurrentUser = check;
            } else {
              runOnUiThread(() -> {
                int time = Toast.LENGTH_SHORT;
                StringBuilder wordMessage = new StringBuilder();
                wordMessage.append("Incorrect Password for User: ")
                        .append(mcUsername);
                Toast toast = Toast.makeText(this, wordMessage, time);
                toast.show();
                Log.d(LOG_TAG, "Password Incorrect Toast was shown");
              });
            }
          }
        }
      }
    });

    if (mcCurrentUser != null && bUserFound)
    {
      Log.d (LOG_TAG, "Launch CameraActivity from Login");
      startActivity (intent);
      Log.d (LOG_TAG, "Camera Activity started");
    }
    else if (!bUserFound) {
      runOnUiThread(() -> {
        int time = Toast.LENGTH_SHORT;
        StringBuilder wordMessage = new StringBuilder ();
        wordMessage.append ("User: ")
                .append (mcUsername).append(" not found. Please create a new account");
        Toast toast = Toast.makeText (this, wordMessage, time);
        toast.show ();
        Log.d (LOG_TAG, "User not Found Toast was shown");
      });
    }
  }
  private void buildDictionary (ExecutorService mcRunner)
  {
      DictionaryDB mcDictionaryDB = Room.databaseBuilder(getApplicationContext(),
              DictionaryDB.class, "Dictionary-DB").build();
    mcDictionaryDAO = mcDictionaryDB.dictionaryDao ();

    mcRunner.execute (() -> {
      if (mcDictionaryDAO.getSize () == 0
              || mcDictionaryDAO.getSize () != SIZE_DATABASE)
      {
        mcDictionaryDAO.deleteAll ();

        try
        {
          URL cDictionaryURL = new URL (
                  "https://raw.githubusercontent.com" +
                          "/sujithps/Dictionary/refs/heads/master" +
                          "/Oxford%20English%20Dictionary.txt");
          TXTDatabaseReader cReader = new TXTDatabaseReader (
                  cDictionaryURL.openStream ());
          cReader.read (mcDictionaryDAO);
        }
        catch (IOException cException)
        {
          throw new RuntimeException (cException);
        }
      }

      if (mcDictionaryDAO.getSize () == SIZE_DATABASE)
      {
        runOnUiThread (() -> {
          int duration = Toast.LENGTH_SHORT;
          Toast cToast = Toast.makeText (this, "Database fully loaded from URL",
                  duration);
          cToast.show ();
        });
      }
    });
  }
}