package edu.pacificu.cs.cs325.translationapp;

/**
 * Creates a BusinessUIState class that represents the UI state of the
 * BusinessLogic class, storing the current color, language, captured image,
 * detected word, and flags for if a picture was taken and login status
 *
 * @author Christian Flores
 */

public class BusinessLogicUIState
{
  private int mColor;
  private int mImageID;
  private boolean mbPictureTaken;
  private String mcLanguage;
  private String mcCameraWord;

  /**
   * Initializes BusinessLogicUIState by initializing the color, language,
   * image, word, and flags
   *
   * @param color         color used to set button colors
   * @param cLanguage     language the user is learning
   * @param imageID       ID associated with image
   * @param cCameraWord   word detected from the camera
   * @param bPictureTaken flag that tells if a picture was taken
   */

  public BusinessLogicUIState (int color, String cLanguage, int imageID,
      String cCameraWord, boolean bPictureTaken)
  {
    mColor = color;
    mcLanguage = cLanguage;
    mImageID = imageID;
    mcCameraWord = cCameraWord;
    mbPictureTaken = bPictureTaken;
  }

  /**
   * Obtains the current language the user is learning
   *
   * @return current language
   */

  public String getLanguage ()
  {
    return mcLanguage;
  }

  /**
   * Obtains the color member variable
   *
   * @return color of buttons
   */

  public int getColor ()
  {
    return mColor;
  }

  /**
   * Obtains a boolean variable that tells whether an image has been taken
   *
   * @return the boolean variable
   */

  public boolean isPictureTaken ()
  {
    return mbPictureTaken;
  }

  /**
   * Obtains the image ID
   *
   * @return the image ID
   */

  public int getImage ()
  {
    return mImageID;
  }

  /**
   * Obtains the word detected from the image
   *
   * @return word detected from image
   */

  public String getWordFromCamera ()
  {
    String cTempString = null;

    if (mbPictureTaken)
    {
      cTempString = mcCameraWord;
    }

    return cTempString;
  }
}