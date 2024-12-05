package edu.pacificu.cs.cs325.translationapp;

import android.graphics.Color;

public class BusinessLogicUIState
{
  //private User mcUser;

  private String mcColor;
  private String mcLanguage;
  private byte[] mcImage;

  private String mcCameraWord;

  private boolean mbPictureTaken;

  private boolean mbSignedIn;

  public BusinessLogicUIState(String cColor, String cLanguage, byte[] cImage,
      String cCameraWord, boolean bPictureTaken, boolean bSignedIn)
  {
    mcColor = cColor;
    mcLanguage = cLanguage;
    mcImage = cImage;
    mcCameraWord = cCameraWord;
    mbPictureTaken = bPictureTaken;
    mbSignedIn = bSignedIn;
  }

  public String getLanguage()
  {
    return mcLanguage;
  }

  public String getColor()
  {
    return mcColor;
  }

  public int getColorInt()
  {
    int colorCode = 0;
    if(mcColor.equals ("Green"))
    {
      colorCode = Color.GREEN;
    }
    else if(mcColor.equals ("Red"))
    {
      colorCode = Color.RED;
    }
    else if(mcColor.equals ("Blue"))
    {
      colorCode = Color.BLUE;
    }
    return colorCode;
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
