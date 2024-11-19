package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PreferenceActivity extends AppCompatActivity
{
  private Spinner mcLanguageSpinner;
  private Spinner mcColorSpinner;

  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
    EdgeToEdge.enable (this);
    setContentView (R.layout.activity_preference);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (systemBars.left, systemBars.top, systemBars.right,
              systemBars.bottom);
          return insets;
        });

    mcLanguageSpinner = findViewById (R.id.languageSpinner);
    mcColorSpinner = findViewById (R.id.colorSpinner);

    String[] languageArray = new String[] { "French", "Spanish", "Chinese" };
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