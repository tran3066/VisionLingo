package edu.pacificu.cs.cs325.translationapp;

import androidx.room.TypeConverter;

public class UserPreferenceConverter
{
  /**
   * Converts a UserPreference to a String
   *
   * @param cUserPreference UserPreference to convert to String
   * @return user preference represented as a string
   */

  @TypeConverter
  public static String fromUserPreference (UserPreference cUserPreference)
  {
    return cUserPreference.getColor () + " " + cUserPreference.getLanguage ();
  }

  /**
   * Converts a String to a UserPreference
   *
   * @param cValue String to convert to UserPreference
   * @return String represented as a UserPreference
   */

  @TypeConverter
  public static UserPreference fromString (String cValue)
  {
    final int TWO_PARTS = 2;

    String[] cSplit = cValue.split (" ", TWO_PARTS);

    return new UserPreference (cSplit[0], cSplit[1]);
  }
}