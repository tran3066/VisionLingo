package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.HashMap;
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
  private ActivityHomeBinding mcBinding;
  private UserDAO mcDAO;
  private UserDB mcDB;
  private HashMap<String, String> usersFromDB = new HashMap<> ();
  private edu.pacificu.cs.cs325.translationapp.User mcUser;

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

    ExecutorService cExec = Executors.newFixedThreadPool (2);
    cExec.execute (() -> {
      try
      {
        mcDB = Room.databaseBuilder (getApplicationContext (), UserDB.class,
            "User-db").fallbackToDestructiveMigrationOnDowngrade ().build ();
        mcDAO = mcDB.userDao ();
        //usersFromDB.put(mcDAO.getAll().)= mcDAO.getAll();
      }
      catch (Exception e)
      {
        throw new RuntimeException (e);
      }

    });

    // shouldn't we have a hashtable so we can look up id and their values?
    // yeah I'll implement that later ^ (Jason)

    Intent cIntentCam = new Intent (this, CameraActivity.class);
    mcBinding.btnLogin.setOnClickListener (v -> {
      //                  for (User check: usersFromDB)
      //                  {
      //                      if (mcBinding.tvUsername.toString())
      //                  }

      Log.d (LOG_TAG, "Launch CameraActivity from Login");
      startActivity (cIntentCam);
      Log.d (LOG_TAG, "Camera Activity started");
    });
  }
}