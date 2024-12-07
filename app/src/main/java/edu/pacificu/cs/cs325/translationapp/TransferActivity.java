package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityHomeBinding;
import edu.pacificu.cs.cs325.translationapp.databinding.ActivityTransferBinding;

public class TransferActivity extends AppCompatActivity {

    private ActivityTransferBinding mcBinding;
    private BusinessLogic mcLogic;
    private MenuItem item;

    private UserDAO mcUserDAO;

    private UserDB mcUserDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mcBinding = ActivityTransferBinding.inflate (getLayoutInflater ());
        View cView = mcBinding.getRoot ();
        setContentView (cView);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mcLogic = new ViewModelProvider(this).get(BusinessLogic.class);
        Intent receiveIntent = getIntent();

        DictionaryDB mcDictionaryDB = Room.databaseBuilder(getApplicationContext(),
            DictionaryDB.class, "Dictionary-DB").build();
        mcLogic.setDAO (mcDictionaryDB.dictionaryDao ());


        mcUserDB = Room.databaseBuilder (getApplicationContext (), UserDB.class,
            "User-DB").fallbackToDestructiveMigrationOnDowngrade ().build ();
        mcUserDAO = mcUserDB.userDao ();


        if ("NewUser".equals(receiveIntent.getType()))
        {
            String username = receiveIntent.getStringExtra("Username");
            String password = receiveIntent.getStringExtra("Password");
            mcLogic.setUser (mcUserDAO.findUserByNamePass (username, password));

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, PreferenceFragment.class, null)
                    .commit();
        }
        else if("Login".equals(receiveIntent.getType()))
        {
            String username = receiveIntent.getStringExtra("Username");
            String password = receiveIntent.getStringExtra("Password");
            mcLogic.setUser (mcUserDAO.findUserByNamePass (username, password));

            getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, CameraFragment.class, null)
                .commit();

        }

        mcBinding.bottomNavigationView.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.camera)
            {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, CameraFragment.class, null)
                        .commit();
                return true;
            }
            else if (item.getItemId() == R.id.preferences)
            {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, PreferenceFragment.class, null)
                        .commit();
                return true;
            }
            else if (item.getItemId() == R.id.wordInformation)
            {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, InfoFragment.class, null)
                        .commit();
                return true;
            }
            else if (item.getItemId() == R.id.vocabularyList)
            {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, ListFragment.class, null)
                        .commit();
                return true;
            }
            else if (item.getItemId() == R.id.quiz)
            {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, QuizFragment.class, null)
                        .commit();
                return true;
            }
            return false;
        });
    }
}