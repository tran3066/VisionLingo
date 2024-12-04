package edu.pacificu.cs.cs325.translationapp;

public class BusinessLogicUIState
{
  private User mcUser;
  private byte[] mcImage;

  private String mcCameraWord;

  private boolean mbPictureTaken;

  private boolean mbSignedIn;

  public BusinessLogicUIState(User cCurrentUser, byte[] cImage,
      String cCameraWord, boolean bPictureTaken, boolean bSignedIn)
  {
    this.mcUser = cCurrentUser;
    mcImage = cImage;
    mcCameraWord = cCameraWord;
    mbPictureTaken = bPictureTaken;
    mbSignedIn = bSignedIn;
  }

  public User getUser()
  {
    return mcUser;
  }

  public boolean isPictureTaken()
  {
    return mbPictureTaken;
  }

  public byte[] getImage()
  {
    byte[] tempImage = null;
    if(mbPictureTaken)
    {
      tempImage = mcImage;
    }
    return tempImage;
  }

  public String getCameraWord() {
    String tempString = null;
    if (mbPictureTaken) {
      tempString = mcCameraWord;
    }

    return tempString;
  }
}
