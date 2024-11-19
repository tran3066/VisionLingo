/**
 * Creates a UserPreference class that is used to give us information about the users preference to
 * keep track of including their language choice and color
 */
package edu.pacificu.cs.cs325.translationapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UserPreference
{
  private String mcColor;
  private String mcLanguage;

  /**
   * Constructs the UserPreference class members
   *
   * @param colorStream    the color choice from the user
   * @param languageStream the language choice from the user
   **/

  public UserPreference (InputStream colorStream, InputStream languageStream)
  {
    BufferedReader brColor;
    BufferedReader brLanguage;
    String color;
    String language;

    try
    {
      brColor = new BufferedReader (new InputStreamReader (colorStream));

      while ((color = brColor.readLine ()) != null)
      {
        mcColor = color.trim ();

      }
    }
    catch (IOException e)
    {
      System.out.println ("An I/O Error occurred while processing file");
      throw new RuntimeException (e);
    }

    try
    {
      brLanguage = new BufferedReader (new InputStreamReader (languageStream));

      while ((language = brLanguage.readLine ()) != null)
      {

        mcLanguage = language.trim ();

      }
    }
    catch (IOException e)
    {
      System.out.println ("An I/O Error occurred while processing file");
      throw new RuntimeException (e);
    }
  }

  public void setMcLanguage (String mcLanguage)
  {
    this.mcLanguage = mcLanguage;
  }

  public void setMcColor (String mcColor)
  {
    this.mcColor = mcColor;
  }

  public String getColor ()
  {
    return mcColor;
  }

  public String getLanguage ()
  {
    return mcLanguage;
  }

}
