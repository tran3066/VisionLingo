/**
 * Creates a User class that is used to give us information about the user to
 * keep track of including their username, password, their preferences for the app and their vocab.
 * It is then put into the user database for all of the users from our app.
 */

package edu.pacificu.cs.cs325.translationapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Entity
public class User
{

  @PrimaryKey (autoGenerate = true)
  private int mUid;

  @ColumnInfo (name = "mcUsername")
  private String mcUsername;

  @ColumnInfo (name = "mcPassword")
  private String mcPassword;

  @ColumnInfo (name = "mcUserPreference")
  private UserPreference mcUserPreference;

  @ColumnInfo (name = "mcVocabList")
  private ArrayList<Vocab> mcVocabList;

  /**
   * Constructs the User class members
   *
   * @param userStream     the username of the user from screen
   * @param passwordStream password associated with the username from screen
   *                       //     * @param mcUserPreference the preferences that are associated with the username
   *                       //     * @param mcVocabList the list of vocabs words created by user
   */
  public User (InputStream userStream, InputStream passwordStream)
  {
    BufferedReader brUser;
    BufferedReader brPassword;
    String username;
    String password;

    mcUserPreference = new UserPreference (userStream, passwordStream);
    mcVocabList = new ArrayList<Vocab> ();

    try
    {
      brUser = new BufferedReader (new InputStreamReader (userStream));

      while ((username = brUser.readLine ()) != null)
      {
        mcUsername = username.trim ();

      }
    }
    catch (IOException e)
    {
      System.out.println ("An I/O Error occurred while processing file");
      throw new RuntimeException (e);
    }

    try
    {
      brPassword = new BufferedReader (new InputStreamReader (passwordStream));

      while ((password = brPassword.readLine ()) != null)
      {

        mcPassword = password.trim ();

      }
    }
    catch (IOException e)
    {
      System.out.println ("An I/O Error occurred while processing file");
      throw new RuntimeException (e);
    }

  }

  public String getUsername ()
  {
    return mcUsername;
  }

  public String getColor (UserPreference mcUserPreference)
  {
    return mcUserPreference.getColor ();
  }

  public String getLanguage (UserPreference mcUserPreference)
  {
    return mcUserPreference.getLanguage ();
  }

}
