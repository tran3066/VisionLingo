/**
 * Creates a User class that is used to give us information about the user to
 * keep track of including their username, password, their preferences for the app and their vocab.
 * It is then put into the user database for all of the users from our app.
 *
 * @author AaJanae Henry
 */

package edu.pacificu.cs.cs325.translationapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
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
     * @param mcUsername the username of the user from screen
     * @param mcPassword password associated with the username from screen
     */

    public User (String mcUsername, String mcPassword)
    {
        this.mcUsername = mcUsername;
        this.mcPassword = mcPassword;
        mcVocabList = new ArrayList<>();
    }

    /**
     * Sets the UserPreferences
     * @param mcUserPreference the preferences associated with the user
     */
    public void setUserPreference (UserPreference mcUserPreference)
    {
        this.mcUserPreference = mcUserPreference;
    }

    /**
     * Gets Username
     * @return  mcUsername the username of the user
     * */
    public String getmcUsername ()
    {
        return mcUsername;
    }

    /**
     * Gets Password
     * @return  mcPassword the password of the user
     * */

    public String getmcPassword ()
    {
        return mcPassword;
    }

    /**
     * Gets UserPreference
     * @return  mcUserPreference - the preferences associated with the user
     * */

    public UserPreference getMcUserPreference ()
    {
        return mcUserPreference;
    }

    /**
     * Gets Color
     * @param mcUserPreference - the preferences associated with the user
     * @return mcUsernamePreference.getColor() - the color preference of the user
     * */

    public String getColor (UserPreference mcUserPreference)
    {
        return mcUserPreference.getColor();
    }

    /**
     * Gets Language
     * @param mcUserPreference - the preferences associated with the user
     * @return  mcUsernamePreference.getLanguage() - the language preference of the user
     * */
    public String getLanguage (UserPreference mcUserPreference)
    {
        return mcUserPreference.getLanguage();
    }

    /**
     * Returns the mUid
     * @return the mUid
     */
    public int getmUid ()
    {
        return mUid;
    }

    /**
     * Sets the mUid
     * @param mUid - the primary key to set
     */

    public void setmUid (int mUid)
    {
        this.mUid = mUid;
    }

}
