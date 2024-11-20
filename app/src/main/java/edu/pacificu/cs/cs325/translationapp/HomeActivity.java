package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity
{
    private final String LOG_TAG = "HomeActivity";
    private ActivityHomeBinding mcBinding;
    private UserDAO mcDAO;
    private UserDB mcDB;
    private HashMap <String, String> usersFromDB = new HashMap<>();
    private edu.pacificu.cs.cs325.translationapp.User mcUser;


    @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
    EdgeToEdge.enable (this);
    mcBinding = ActivityHomeBinding.inflate(getLayoutInflater());
    View view = mcBinding.getRoot();
    setContentView(view);
    //setContentView (R.layout.activity_home);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (systemBars.left, systemBars.top, systemBars.right,
              systemBars.bottom);
          return insets;
        });

      ExecutorService cExec = Executors.newFixedThreadPool(2);
      cExec.execute(() ->
              {
                  try {
                      mcDB = Room.databaseBuilder(getApplicationContext(),
                                      UserDB.class, "User-db")
                              .fallbackToDestructiveMigrationOnDowngrade().build();
                      mcDAO = mcDB.userDao();
                      //usersFromDB.put(mcDAO.getAll().)= mcDAO.getAll();
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }

              });

    //shouldnt we have a hashtable so we can look up id and their values?

      Intent intentCam = new Intent(this, CameraActivity.class);
      mcBinding.btnLogin.setOnClickListener(
              v -> {
//                  for (User check: usersFromDB)
//                  {
//                      if (mcBinding.tvUsername.toString())
//                  }

                  Log.d(LOG_TAG, "Launch CameraActivity from Login");
                  startActivity(intentCam);
                  Log.d(LOG_TAG, "Camera Activity started");
              });
  }
}