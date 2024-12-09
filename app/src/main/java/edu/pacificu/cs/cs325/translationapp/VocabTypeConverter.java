package edu.pacificu.cs.cs325.translationapp;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Creates a VocabTypeConverter class used to convert a user's list of
 * vocabulary words to a String and vice versa
 *
 * @author Jason Tran
 */

public class VocabTypeConverter
{
  private static final Gson mcGson = new Gson ();

  /**
   * Converts an ArrayList to a String
   *
   * @param cList ArrayList to convert to String
   * @return ArrayList represented as a String
   */

  @TypeConverter
  public static String fromArrayList (ArrayList<Vocab> cList)
  {
    return mcGson.toJson (cList);
  }

  /**
   * Converts a String to an ArrayList
   *
   * @param cString String to convert to ArrayList
   * @return String represented as an ArrayList
   */

  @TypeConverter
  public static ArrayList<Vocab> fromString (String cString)
  {
    Type cListType = new TypeToken<ArrayList<Vocab>> ()
    {
    }.getType ();

    return mcGson.fromJson (cString, cListType);
  }
}