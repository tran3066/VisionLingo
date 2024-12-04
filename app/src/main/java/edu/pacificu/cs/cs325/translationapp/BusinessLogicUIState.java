package edu.pacificu.cs.cs325.translationapp;

public class BusinessLogicUIState
{
  private User mcCurrentUser;

  private Byte[] mcImage;

  private String mcCameraWord;

  private boolean mbPictureTaken;

  private boolean mbSignedIn;

  public BusinessLogicUIState(User cCurrentUser, Byte[] cImage,
      String cCameraWord, boolean bPictureTaken, boolean bSignedIn)
  {
    mcCurrentUser = cCurrentUser;
    mcImage = cImage;
    mcCameraWord = cCameraWord;
    mbPictureTaken = bPictureTaken;
    mbSignedIn = bSignedIn;
  }

  public User getUser()
  {
    return mcCurrentUser;
  }

  public boolean isPictureTaken()
  {
    return mbPictureTaken;
  }

  public Byte[] getImage()
  {
    Byte[] tempImage = null;
    if(mbPictureTaken)
    {
      tempImage = mcImage;
    }
    return tempImage;
  }

  public String getCameraWord()
  {
    String tempString = null;
    if(mbPictureTaken)
    {
      tempString = mcCameraWord;
    }

    return tempString;
  }
}
