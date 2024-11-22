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

/**
 * Creates a CourseRecyclerViewAdapter class that is used to set up the
 * RecyclerView to display the English word, the translation, word type,
 * definition, and image of the word
 *
 * @author Jason Tran
 */

public class CourseRecyclerViewAdapter
    extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>
{
  private ArrayList<Word> mcData;

  /**
   * Constructor to build the adapter
   *
   * @param cData the data to be displayed by the views
   */

  public CourseRecyclerViewAdapter (ArrayList<Word> cData)
  {
    this.mcData = cData;
  }

  /**
   * Inflates the XML file and produces a ViewHolder
   *
   * @param cParent  ViewGroup object
   * @param viewType type of view
   * @return ViewHolder produced from inflating XML
   */

  @NonNull
  @Override
  public CourseRecyclerViewAdapter.ViewHolder onCreateViewHolder (
      @NonNull ViewGroup cParent, int viewType)
  {
    View cView = LayoutInflater.from (cParent.getContext ())
        .inflate (R.layout.display_word, cParent, false);
    ViewHolder cHolder = new ViewHolder (cView);

    return cHolder;
  }

  /**
   * Put on Word into one ViewHolder
   *
   * @param cHolder  ViewHolder object
   * @param position position of mcData
   */

  @Override
  public void onBindViewHolder (
      @NonNull CourseRecyclerViewAdapter.ViewHolder cHolder, int position)
  {
    cHolder.mcWord = mcData.get (position);
    cHolder.bindData ();
  }

  /**
   * Get number of items
   *
   * @return number of items
   */

  // get number of items
  @Override
  public int getItemCount ()
  {
    return mcData.size ();
  }

  /**
   * Creates a ViewHolder class that displays the English word, the translation,
   * word type, definition, and image of the word in the RecyclerView
   *
   * @author Jason Tran
   */

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    private Word mcWord;
    private TextView mcTvEnglishWord;
    private TextView mcTvTranslation;
    private TextView mcTvWordType;
    private TextView mcTvDefinition;
    private ImageView mcImageWord;

    /**
     * Constructor to build the ViewHolder
     *
     * @param cItemView View object used to build the ViewHolder
     */

    public ViewHolder (@NonNull View cItemView)
    {
      super (cItemView);
    }

    /**
     * Method used to display data in RecyclerView
     */

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