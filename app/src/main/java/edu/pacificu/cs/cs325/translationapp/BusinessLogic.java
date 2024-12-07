package edu.pacificu.cs.cs325.translationapp;

import android.graphics.Color;

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
  private boolean mbSignedIn;
  private byte[] mcImage;
  private User mcUser;
  private String mcWordFromCamera;
  private DictionaryDAO mcDictionaryDAO;

  /**
   * Initializes BusinessLogic by assigning its member variables default values
   */

  public BusinessLogic ()
  {
    mbSignedIn = false;
    mcWordFromCamera = "";
    mbPictureTaken = false;
    mcDictionaryDAO = null;
    mcUiState = new MutableLiveData<> (
        new BusinessLogicUIState (0, "", null, mcWordFromCamera, mbPictureTaken,
            mbSignedIn));
  }

  /**
   * Sets the dictionary data access object of the BusinessLogic
   *
   * @param cDictionaryDAO the DictionaryDAO to set the new DictionaryDAO
   */

  public void setDAO (DictionaryDAO cDictionaryDAO)
  {
    mcDictionaryDAO = cDictionaryDAO;
  }

  /**
   * Obtains the DictionaryDAO
   *
   * @return the DictionaryDAO object
   */

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
    mcImage = cImage;

    mcUiState.setValue (
        new BusinessLogicUIState (mcUiState.getValue ().getColor (),
            mcUiState.getValue ().getLanguage (), mcImage, mcWordFromCamera,
            mbPictureTaken, mbSignedIn));
  }

  /**
   * Obtains the image
   *
   * @return the image as a byte array
   */

  public byte[] getImage ()
  {
    return mcImage;
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

  public boolean isMbSignedIn ()
  {
    return mbSignedIn;
  }

  /**
   * Updates the BusinessLogic class by storing the word detected from the image
   *
   * @param cWord word detected from image
   */

  public void detectWord (String cWord)
  {
    mcWordFromCamera = cWord;
    mcUiState.setValue (
        new BusinessLogicUIState (mcUiState.getValue ().getColor (),
            mcUiState.getValue ().getLanguage (), mcImage, mcWordFromCamera,
            mbPictureTaken, mbSignedIn));
  }

  /**
   * Obtains the word detected from the image
   *
   * @return word detected from image
   */

  public String getWordFromCamera ()
  {
    return mcWordFromCamera;
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
    mbSignedIn = true;
    mcUiState.setValue (
        new BusinessLogicUIState (mcUiState.getValue ().getColor (),
            mcUiState.getValue ().getLanguage (), mcImage, mcWordFromCamera,
            mbPictureTaken, mbSignedIn));
  }

  /**
   * Sets the current user member variable (unfinished?)
   *
   * @param cUsername username of the user
   * @param cPassword password of the user
   */

  public void setUser (String cUsername, String cPassword)
  {

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
        new BusinessLogicUIState (color, getLanguage (), mcImage,
            mcWordFromCamera, mbPictureTaken, mbSignedIn));
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
        new BusinessLogicUIState (getColor (), cLanguage, mcImage,
            mcWordFromCamera, mbPictureTaken, mbSignedIn));
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
        tempColor = Color.MAGENTA;
        break;
      case "Red":
        tempColor = Color.RED;
        break;
      case "Green":
        tempColor = Color.GREEN;
        break;
      case "Blue":
        tempColor = Color.BLUE;
        break;
      default:
        tempColor = 0;
        break;
    }

    mcUser = cUser;
    mcUiState.setValue (
        new BusinessLogicUIState (tempColor, cUser.getLanguage (), mcImage,
            mcWordFromCamera, mbPictureTaken, mbSignedIn));
  }
}
