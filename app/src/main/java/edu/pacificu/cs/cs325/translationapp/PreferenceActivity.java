package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityPreferenceBinding;

/**
 * Creates a PreferenceActivity class that allows the user to select the
 * language that they are learning (French, Spanish) and the main color of the
 * app (red, green, blue, etc.). The user changes their learning language or app
 * color in a spinner. Clicking the confirm preferences button updates the
 * user's preferences throughout the app.
 *
 * @author AaJanae Henry, Jason Tran, Christian Flores
 */

public class PreferenceActivity extends AppCompatActivity
{
  private ActivityPreferenceBinding mcBinding;

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
    mcBinding = ActivityPreferenceBinding.inflate (getLayoutInflater ());
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

    String[] languageArray = new String[] { "French", "Spanish" };
    ArrayAdapter<String> languageAdapter = new ArrayAdapter<> (this,
        android.R.layout.simple_spinner_dropdown_item, languageArray);
    languageAdapter.setDropDownViewResource (
        android.R.layout.simple_spinner_dropdown_item);

    String[] colorArray = new String[] { "Red", "Green", "Blue" };
    ArrayAdapter<String> colorAdapter = new ArrayAdapter<> (this,
        android.R.layout.simple_spinner_dropdown_item, colorArray);
    colorAdapter.setDropDownViewResource (
        android.R.layout.simple_spinner_dropdown_item);
  }
}