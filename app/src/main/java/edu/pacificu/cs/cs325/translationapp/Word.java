package edu.pacificu.cs.cs325.translationapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word
{
  @PrimaryKey (autoGenerate = true)
  private int mWid;

  @ColumnInfo(name="mcEnglishWord")
  private String mcEnglishWord;

  @ColumnInfo(name="mcTranslatedWord")
  private String mcTranslatedWord;

  @ColumnInfo(name="mcDefinition")
  private String mcDefinition;

  @ColumnInfo(name="mcLexical")
  private String mcLexical;

  @ColumnInfo(name="mcLexical")
  private String mcLanguage;

  public Word(String mcEnglishWord, String mcTranslatedWord, String mcDefinition,
      String mcLexical, String mcLanguage)
  {
    this.mcEnglishWord = mcEnglishWord;
    this.mcTranslatedWord = mcTranslatedWord;
    this.mcDefinition = mcDefinition;
    this.mcLexical = mcLexical;
    this.mcLanguage = mcLanguage;

  }

  public String getEnglishWord()
  {
    return this.mcEnglishWord;
  }

  public String getTranslatedWord()
  {
    return this.mcTranslatedWord;
  }

  public String getDefinition()
  {
    return this.mcDefinition;
  }

  public String getLexical()
  {
    return this.mcLexical;
  }

  public String getLanguage()
  {
    return this.mcLanguage;
  }

  public void setWid(int wid)
  {
    this.mWid = wid;
  }

  public int getWid()
  {
    return this.mWid;
  }



}