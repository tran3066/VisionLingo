package edu.pacificu.cs.cs325.translationapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
{
  private ArrayList<Word> mcWordList;
  private CourseRecyclerViewAdapter mcAdapter;
  private RecyclerView mcRVWords;

  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
    EdgeToEdge.enable (this);
    setContentView (R.layout.activity_list);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (systemBars.left, systemBars.top, systemBars.right,
              systemBars.bottom);
          return insets;
        });

    mcRVWords = findViewById (R.id.rvWords);
    mcRVWords.setHasFixedSize (true);
    mcRVWords.setLayoutManager (new LinearLayoutManager (this));
  }

  @Override
  public void onResume () {
    super.onResume ();
    mcAdapter = new CourseRecyclerViewAdapter (mcWordList); // list of words (?)
    mcRVWords.setAdapter (mcAdapter);
  }
}