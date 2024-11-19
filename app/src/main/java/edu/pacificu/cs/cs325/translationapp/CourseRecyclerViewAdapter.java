package edu.pacificu.cs.cs325.translationapp;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseRecyclerViewAdapter
    extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>
{
  private ArrayList<Word> mcData;

  /***
   * Constructor to build the adapter
   * @param data the data to be displayed by the views
   */
  public CourseRecyclerViewAdapter (ArrayList<Word> data)
  {
    this.mcData = data;
  }

  // inflate the XML file, produce a ViewHolder
  @NonNull
  @Override
  public CourseRecyclerViewAdapter.ViewHolder onCreateViewHolder (
      @NonNull ViewGroup parent, int viewType)
  {
    View view = LayoutInflater.from (parent.getContext ())
        .inflate (R.layout.display_word, parent, false);
    ViewHolder holder = new ViewHolder (view);

    return holder;
  }

  // Put on Course into one ViewHolder
  @Override
  public void onBindViewHolder (
      @NonNull CourseRecyclerViewAdapter.ViewHolder holder, int position)
  {
    holder.mcWord = mcData.get (position);
    holder.bindData ();
  }

  // get number of items
  @Override
  public int getItemCount ()
  {
    return mcData.size ();
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    private Word mcWord;
    private TextView mcTvEnglishWord;
    private TextView mcTvTranslation;
    private TextView mcTvWordType;
    private TextView mcTvDefinition;
    private ImageView mcImageWord;

    public ViewHolder (@NonNull View itemView)
    {
      super (itemView);
    }

    public void bindData ()
    {
      if (null == mcTvEnglishWord)
      {
        mcTvEnglishWord = (TextView) itemView.findViewById (R.id.tvEnglishWord);
      }
      if (null == mcTvTranslation)
      {
        mcTvTranslation = (TextView) itemView.findViewById (R.id.tvTranslation);
      }
      if (null == mcTvWordType)
      {
        mcTvWordType = (TextView) itemView.findViewById (R.id.tvWordType);
      }
      if (null == mcTvDefinition)
      {
        mcTvDefinition = (TextView) itemView.findViewById (R.id.tvDefinition);
      }
      if (null == mcImageWord)
      {
        mcImageWord = (ImageView) itemView.findViewById (R.id.imageWord);
      }

      //      mcTvEnglishWord.setText (mcWord.getEnglish ());
      //      mcTvTranslation.setText (mcWord.getTranslated ());
      //      mcTvWordType.setText (mcWord.getLexical ());
      //      mcTvDefinition.setText (mcWord.getDefinition ());
      //      mcImageWord.setImageBitmap (
      //          BitmapFactory.decodeByteArray (mcWord.getImage (), 0,
      //              mcWord.getImage.length));
    }
  }
}