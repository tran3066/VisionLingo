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

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityHomeBinding;

public class InfoActivity extends AppCompatActivity
{
    private ActivityHomeBinding mcBinding;


    @Override
  protected void onCreate (Bundle savedInstanceState)
  {

    super.onCreate (savedInstanceState);
    EdgeToEdge.enable (this);
    mcBinding = ActivityHomeBinding.inflate(getLayoutInflater());
    View view = mcBinding.getRoot();
    setContentView(view);
    //setContentView (R.layout.activity_info);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (systemBars.left, systemBars.top, systemBars.right,
              systemBars.bottom);
          return insets;
        });


  }
}