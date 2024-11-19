package edu.pacificu.cs.cs325.translationapp;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a DBTypeConverter class used to convert a user's list of vocabulary
 * words to a string
 *
 * @author Jason Tran
 */

public class DBTypeConverter
{
  /**
   * Converts an ArrayList to a String
   *
   * @return ArrayList represented as a String
   */

  @TypeConverter
  public static String fromArrayList (ArrayList<Vocab> cList)
  {
    StringBuilder cSB = new StringBuilder ();

    for (Vocab cVocab : cList)
    {
      //cSB.append (cVocab.getWordEnglish () + " "); // how should we convert it?
    }

    return cSB.toString ();
  }

  /**
   * Converts a String to an ArrayList
   *
   * @return String represented as an ArrayList
   */

  @TypeConverter
  public static ArrayList<Vocab> fromString (String cValue)
  {
    ArrayList<Vocab> cArrayList = new ArrayList<>();

    // ???

    return cArrayList;
  }
}