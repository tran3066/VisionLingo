package edu.pacificu.cs.cs325.translationapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Creates a Word class that contains the ID of the word, the word in English,
 * the word translated to another language, the definition of the word, the word
 * type, and the language the user wants to translate the word to
 *
 * @author Christian Flores
 */

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

  @ColumnInfo(name="mcLanguage")
  private String mcLanguage;

  /**
   * Initializes Word by initializing the English word, the translated word,
   * the word's definition, the word type, and the language
   *
   * @param mcEnglishWord    the word in English
   * @param mcTranslatedWord the word translated from English
   * @param mcDefinition     the definition of the word
   * @param mcLexical        the word type (e.g. noun, verb, adjective)
   * @param mcLanguage       the language to translate the word to
   */

  public Word(String mcEnglishWord, String mcTranslatedWord, String mcDefinition,
      String mcLexical, String mcLanguage)
  {
    this.mcEnglishWord = mcEnglishWord;
    // I feel like we should generate mcTranslatedWord here
    this.mcTranslatedWord = mcTranslatedWord;
    this.mcDefinition = mcDefinition;
    this.mcLexical = mcLexical;
    this.mcLanguage = mcLanguage;
  }

  /**
   * Obtains the word in English
   *
   * @return the word in English
   */

  public String getMcEnglishWord()
  {
    return this.mcEnglishWord;
  }

  /**
   * Obtains the translated word from English
   *
   * @return translated word
   */

  public String getMcTranslatedWord()
  {
    return this.mcTranslatedWord;
  }

  /**
   * Obtains the definition of the word
   *
   * @return definition of word
   */

  public String getMcDefinition()
  {
    return this.mcDefinition;
  }

  /**
   * Obtains the word type
   *
   * @return the word type
   */

  public String getMcLexical()
  {
    return this.mcLexical;
  }

  /**
   * Obtains the language to translate to
   *
   * @return the language
   */

  public String getMcLanguage()
  {
    return this.mcLanguage;
  }

  /**
   * Sets the WID of the word
   *
   * @param wid the WID to set the new WID
   */

  public void setWid(int wid)
  {
    this.mWid = wid;
  }

  /**
   * Obtains the WID
   *
   * @return the WID
   */

  public int getWid()
  {
    return this.mWid;
  }
}