package edu.pacificu.cs.cs325.translationapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Creates a User class that is used to give us information about the user to
 * keep track of including their username, password, their preferences for the
 * app and their vocab. It is then put into the user database for all of the
 * users from our app.
 *
 * @author AaJanae Henry
 */

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
   * @param mcUsername the username of the user from screen
   * @param mcPassword password associated with the username from screen
   */

  public User (String mcUsername, String mcPassword)
  {
    this.mcUsername = mcUsername;
    this.mcPassword = mcPassword;
    mcVocabList = new ArrayList<> ();
    mcUserPreference = new UserPreference ("Blue", "French");
  }

  /**
   * Sets the UserPreferences
   *
   * @param mcUserPreference the preferences associated with the user
   */

  public void setMcUserPreference (UserPreference mcUserPreference)
  {
    this.mcUserPreference = mcUserPreference;
  }

  /**
   * Sets the VocabList
   *
   * @param mcVocabList the vocab list associated with the user
   */

  public void setMcVocabList (ArrayList<Vocab> mcVocabList)
  {
    this.mcVocabList = mcVocabList;
  }

  /**
   * Gets Username
   *
   * @return mcUsername the username of the user
   */

  public String getMcUsername ()
  {
    return mcUsername;
  }

  /**
   * Gets VocabList
   *
   * @return mcVocabList the vocab list from the user
   */

  public ArrayList<Vocab> getMcVocabList ()
  {
    return mcVocabList;
  }

  /**
   * Gets Password
   *
   * @return mcPassword the password of the user
   */

  public String getMcPassword ()
  {
    return mcPassword;
  }

  /**
   * Gets UserPreference
   *
   * @return mcUserPreference - the preferences associated with the user
   */

  public UserPreference getMcUserPreference ()
  {
    return mcUserPreference;
  }

  /**
   * Gets Color
   *
   * @return mcUsernamePreference.getColor () - the color preference of the user
   */

  public String getColor ()
  {
    return mcUserPreference.getColor ();
  }

  /**
   * Gets Language
   *
   * @return mcUsernamePreference.getLanguage() - the language preference of the
   * user
   */

  public String getLanguage ()
  {
    return mcUserPreference.getLanguage ();
  }

  /**
   * Returns the mUid
   *
   * @return the mUid
   */

  public int getMUid ()
  {
    return mUid;
  }

  /**
   * Sets the mUid
   *
   * @param mUid - the primary key to set
   */

  public void setMUid (int mUid)
  {
    this.mUid = mUid;
  }

  /**
   * Sets the language
   *
   * @param cLanguage - the language to set
   */

  public void setLanguage (String cLanguage)
  {
    mcUserPreference.setMcLanguage (cLanguage);
  }

  /**
   * Sets the color
   *
   * @param cColor - the color to set
   */

  public void setColor (String cColor)
  {
    mcUserPreference.setMcColor (cColor);
  }

  /**
   * Adds a new Vocab object to the user's vocab list
   *
   * @param cNewVocab - the Vocab object to add
   */

  public void addToVocab (Vocab cNewVocab)
  {
    mcVocabList.add (cNewVocab);
  }
}