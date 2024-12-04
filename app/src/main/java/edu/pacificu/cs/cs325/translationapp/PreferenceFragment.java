package edu.pacificu.cs.cs325.translationapp;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;
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

public class PreferenceFragment extends Fragment {

    private FragmentPreferenceBinding mcBinding;
    private final String LOG_TAG = "PreferenceFragment";
    private UserDAO mcUserDAO;
    private UserDB mcUserDB;
    private List<User> usersFromDB;

    private String selectedLanguage;
    private ExecutorService mcRunner;
    private String selectedColor;
    private int NUM_THREADS = 1;
    private UserPreference mcUserPref;
    private BusinessLogic mcLogic;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentPreferenceBinding.inflate(
                getLayoutInflater());
        View view = mcBinding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView ();
    }
    @Override
    public void onViewCreated (@NonNull View view,
                               @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated (view, savedInstanceState);

        mcRunner = Executors.newFixedThreadPool (NUM_THREADS);

        mcLogic = new ViewModelProvider(getActivity()).get(BusinessLogic.class);

        String[] languageArray = new String[] { "French", "Spanish" };
        assert getActivity() != null;
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<> (getActivity(),
                android.R.layout.simple_spinner_dropdown_item, languageArray);
        languageAdapter.setDropDownViewResource (
                android.R.layout.simple_spinner_dropdown_item);

        mcBinding.languageSpinner.setAdapter(languageAdapter);

        String[] colorArray = new String[] { "Red", "Green", "Blue" };
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<> (getActivity(),
                android.R.layout.simple_spinner_dropdown_item, colorArray);
        colorAdapter.setDropDownViewResource (
                android.R.layout.simple_spinner_dropdown_item);

        mcBinding.colorSpinner.setAdapter(colorAdapter);

        mcBinding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                assert getActivity() != null;
                Toast.makeText(getActivity().getApplicationContext(),
                        (String) adapterView.getSelectedItem(),
                        Toast.LENGTH_LONG).show();
                selectedLanguage = (String) adapterView.getItemAtPosition(position);
                Log.d(LOG_TAG, "Language selected: " + selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        mcBinding.colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                assert getActivity() != null;
                Toast.makeText(getActivity().getApplicationContext(),
                        (String) adapterView.getSelectedItem(),
                        Toast.LENGTH_LONG).show();
                selectedColor = (String) adapterView.getItemAtPosition(position);
                Log.d(LOG_TAG, "Color selected: " + selectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        mcUserPref = new UserPreference(selectedColor,selectedLanguage);

        mcLogic.getUser().setMcUserPreference(mcUserPref);

       mcBinding.btnConfirm.setOnClickListener (v -> {
//           mcRunner.execute(() -> {
//               try {
//                   mcUserDB = Room.databaseBuilder(getActivity().getApplicationContext(),
//                           UserDB.class,
//                           "User-DB").fallbackToDestructiveMigrationOnDowngrade().build();
//                   mcUserDAO = mcUserDB.userDao();
//                   usersFromDB = mcUserDAO.getAll();
//                   mcUserDAO.insert(mcLogic.getUser());
//               } catch (Exception e) {
//                   throw new RuntimeException(e);
//               }
//           });

           Toast.makeText(getActivity().getApplicationContext(),
                   "Preferences Confirmed: Please Choose Activity Below",
                   Toast.LENGTH_LONG).show();
       });
    }
}