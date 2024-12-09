package edu.pacificu.cs.cs325.translationapp;

/**
 * Creates a Vocab class that stores a Word object and an image of the word
 * stored as a byte array
 *
 * @author Christian Flores
 */

public class Vocab
{
  private int mImageID;
  private Word mcTheWord;
  private String mcTranslatedWord;

  /**
   * Initializes Vocab by initializing the word and image
   *
   * @param mcTheWord        word object
   * @param mImageID         ID that points to the image in the ImageDB
   * @param mcTranslatedWord translated word
   */

  public Vocab (Word mcTheWord, int mImageID, String mcTranslatedWord)
  {
    this.mcTheWord = mcTheWord;
    this.mImageID = mImageID;
    this.mcTranslatedWord = mcTranslatedWord;
  }

  /**
   * Obtains the image ID
   *
   * @return image ID
   */

  public int getImage ()
  {
    return mImageID;
  }

  /**
   * Obtains the word object
   *
   * @return word object
   */

  public Word getWord ()
  {
    return this.mcTheWord;
  }

  /**
   * Obtains the translated word
   *
   * @return translated word
   */

  public String getTranslatedWord ()
  {
    return mcTranslatedWord;
  }
}