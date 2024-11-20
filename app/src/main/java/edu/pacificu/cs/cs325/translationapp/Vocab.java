package edu.pacificu.cs.cs325.translationapp;

public class Vocab
{
  private Word mcTheWord;

  private Byte[] mcImage;

  public Vocab(Word mcTheWord, Byte[] mcImage)
  {
    this.mcTheWord = mcTheWord;
    this.mcImage = mcImage;
  }

  public Byte[] getImage()
  {
    return mcImage;
  }

  public Word getWord()
  {
    return this.mcTheWord;
  }

}
