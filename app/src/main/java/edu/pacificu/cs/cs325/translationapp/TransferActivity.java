package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityHomeBinding;
import edu.pacificu.cs.cs325.translationapp.databinding.ActivityTransferBinding;

public class TransferActivity extends AppCompatActivity {

    private ActivityTransferBinding mcBinding;

    private BusinessLogic mcLogic;


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
        Intent recieveIntent = getIntent();


        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, CameraFragment.class, null)
                .commit();

        if ("String".equals(recieveIntent.getType())) {
            String username = recieveIntent.getStringExtra("Username");
            String password = recieveIntent.getStringExtra("Password");
            mcLogic.createUser(username,password);

//            mcLogic.getUiState().observe(this, uisate -> {
//                mcBinding
//            });

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, PreferenceFragment.class, null)
                    .commit();
        }

        mcBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
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