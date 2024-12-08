package edu.pacificu.cs.cs325.translationapp;

/**
 * Creates a Vocab class that stores a Word object and an image of the word
 * stored as a byte array
 *
 * @author Christian Flores
 */

public class Vocab
{
  private Word mcTheWord;
  private byte[] mcImage;
  private String mcTranslatedWord;

  /**
   * Initializes Vocab by initializing the word and image
   *
   * @param mcTheWord word object
   * @param mcImage   image stored as a byte array
   */

  public Vocab (Word mcTheWord, byte[] mcImage, String mcTranslatedWord)
  {
    this.mcTheWord = mcTheWord;
    this.mcImage = mcImage;
    this.mcTranslatedWord = mcTranslatedWord;
  }

  /**
   * Obtains the image of the word
   *
   * @return image of the word
   */

  public byte[] getImage ()
  {
    return mcImage;
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