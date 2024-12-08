package edu.pacificu.cs.cs325.translationapp;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Creates a BusinessLogic class that serves as the ViewModel for managing the
 * application's data and operations, including user data (color and language
 * settings, user object) and access to DictionaryDAO for dictionary access
 *
 * @author AaJanae Henry, Christian Flores
 */

public class BusinessLogic extends ViewModel
{
  private final MutableLiveData<BusinessLogicUIState> mcUiState;
  private int mColor = 0;
  private boolean mbPictureTaken;
  private User mcUser;
  private DictionaryDAO mcDictionaryDAO;

  private Dictionary mcDictionary;

  /**
   * Initializes BusinessLogic by assigning its member variables default values
   */

  public BusinessLogic ()
  {
    mbPictureTaken = false;
    mcDictionaryDAO = null;
    mcUiState = new MutableLiveData<> (
        new BusinessLogicUIState (0, "", null, "", mbPictureTaken));
    mcDictionary = null;
  }

  /**
   * Sets the dictionary data access object of the BusinessLogic
   *
   * @param cDictionaryDAO the DictionaryDAO to set the new DictionaryDAO
   */

  public void setDAO (DictionaryDAO cDictionaryDAO)
  {
    mcDictionaryDAO = cDictionaryDAO;
    mcDictionary = new  Dictionary(mcDictionaryDAO.getAll ());
  }

  /**
   * Obtains the DictionaryDAO
   *
   * @return the DictionaryDAO object
   */

  public Word getWord(String cWord)
  {
    return mcDictionary.getWord (cWord);
  }

  public DictionaryDAO getDAO ()
  {
    return mcDictionaryDAO;
  }

  /**
   * Updates the BusinessLogic class by storing the provided image and marking
   * that a picture has been taken
   *
   * @param cImage image taken, represented as a byte array
   */

  public void takePicture (byte[] cImage)
  {
    mbPictureTaken = true;

    mcUiState.setValue (
        new BusinessLogicUIState (mcUiState.getValue ().getColor (),
            mcUiState.getValue ().getLanguage (), cImage, getWordFromCamera (),
            mbPictureTaken));
  }

  /**
   * Obtains the image
   *
   * @return the image as a byte array
   */

  public byte[] getImage ()
  {
    return mcUiState.getValue ().getImage ();
  }

  /**
   * Obtains a boolean variable that tells whether an image has been taken
   *
   * @return the boolean variable
   */

  public boolean isMbPictureTaken ()
  {
    return mbPictureTaken;
  }

  /**
   * Obtains a boolean variable that tells whether the user is signed in
   *
   * @return the boolean variable
   */

  /**
   * Updates the BusinessLogic class by storing the word detected from the image
   *
   * @param cWord word detected from image
   */

  public void detectWord (String cWord)
  {

    mcUiState.setValue (
        new BusinessLogicUIState (mcUiState.getValue ().getColor (),
            mcUiState.getValue ().getLanguage (), getImage (), cWord,
            mbPictureTaken));
  }

  /**
   * Obtains the word detected from the image
   *
   * @return word detected from image
   */

  public String getWordFromCamera ()
  {
    return mcUiState.getValue ().getWordFromCamera ();
  }

  /**
   * Obtains the ui state of the BusinessLogic class
   *
   * @return ui state of BusinessLogic class
   */

  public LiveData<BusinessLogicUIState> getMcUiState ()
  {
    return mcUiState;
  }

  /**
   * Updates the BusinessLogic class by initializing a new User object with the
   * provided username and password and marks that the user is signed in
   *
   * @param cUsername username of the user
   * @param cPassword password of the user
   */

  public void createUser (String cUsername, String cPassword)
  {
    mcUser = new User (cUsername, cPassword);
    mcUiState.setValue (
        new BusinessLogicUIState (mcUiState.getValue ().getColor (),
            mcUiState.getValue ().getLanguage (), getImage (),
            getWordFromCamera (), mbPictureTaken));
  }

  /**
   * Sets the color member variable used to set the color of buttons in the app
   *
   * @param color color of buttons
   */

  public void setColor (int color)
  {
    mColor = color;
    mcUiState.setValue (
        new BusinessLogicUIState (color, getLanguage (), getImage (),
            getWordFromCamera (), mbPictureTaken));
  }

  /**
   * Obtains the color member variable
   *
   * @return color of buttons
   */

  public int getColor ()
  {
    return mColor;
    //test.....
  }

  /**
   * Obtains the current language the user is learning
   *
   * @return current language
   */

  public String getLanguage ()
  {
    return mcUiState.getValue ().getLanguage ();
  }

  /**
   * Sets the current language the user is learning
   *
   * @param cLanguage the current language
   */

  public void setLanguage (String cLanguage)
  {
    mcUiState.setValue (
        new BusinessLogicUIState (getColor (), cLanguage, getImage (),
            getWordFromCamera (), mbPictureTaken));
  }

  /**
   * Obtains the current user logged in
   *
   * @return current user
   */

  public User getUser ()
  {
    return mcUser;
  }

  /**
   * Sets the current user that is logged in
   *
   * @param cUser new user
   */

  public void setUser (User cUser)
  {
    int tempColor;

    switch (cUser.getColor ())
    {
      case "Pink":
        Log.d ("TransferActivity", cUser.getColor () + " SET");
        tempColor = Color.MAGENTA;
        break;
      case "Red":
        Log.d ("TransferActivity", cUser.getColor () + " SET");
        tempColor = Color.RED;
        break;
      case "Green":
        Log.d ("TransferActivity", cUser.getColor () + " SET");
        tempColor = Color.GREEN;
        break;
      case "Blue":
        Log.d ("TransferActivity", cUser.getColor () + " SET");
        tempColor = Color.BLUE;
        break;
      default:
        Log.d("TransferActivity", "Entered Default");
        tempColor = 0;
        break;
    }

    mcUser = cUser;
    Log.d("TransferActivity", String.valueOf (tempColor));
    setColor(tempColor);
    mcUiState.setValue (
        new BusinessLogicUIState (tempColor, cUser.getLanguage (), getImage (),
            getWordFromCamera (), mbPictureTaken));

  }

  /**
   * Resets the picture taken boolean flag
   */

  public void resetImg ()
  {
    mbPictureTaken = false;
    mcUiState.setValue (
        new BusinessLogicUIState (getColor (), getLanguage (), null, null,
            mbPictureTaken));
  }
}