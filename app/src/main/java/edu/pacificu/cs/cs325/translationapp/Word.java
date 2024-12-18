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

  @ColumnInfo (name = "mcEnglishWord")
  private String mcEnglishWord;

  @ColumnInfo (name = "mcDefinition")
  private String mcDefinition;

  @ColumnInfo (name = "mcLexical")
  private String mcLexical;

  /**
   * Initializes Word by initializing the English word, the translated word,
   * the word's definition, the word type, and the language
   *
   * @param mcEnglishWord the word in English
   * @param mcLexical     the word type (e.g. noun, verb, adjective)
   * @param mcDefinition  the definition of the word
   */

  public Word (String mcEnglishWord, String mcLexical, String mcDefinition)
  {
    this.mcEnglishWord = mcEnglishWord;
    this.mcLexical = mcLexical;
    this.mcDefinition = mcDefinition;
  }

  /**
   * Obtains the word in English
   *
   * @return the word in English
   */

  public String getMcEnglishWord ()
  {
    return this.mcEnglishWord;
  }

  /**
   * Obtains the definition of the word
   *
   * @return definition of word
   */

  public String getMcDefinition ()
  {
    return this.mcDefinition;
  }

  /**
   * Obtains the word type
   *
   * @return the word type
   */

  public String getMcLexical ()
  {
    return this.mcLexical;
  }

  /**
   * Sets the WID of the word
   *
   * @param wid the WID to set the new WID
   */

  public void setWid (int wid)
  {
    this.mWid = wid;
  }

  /**
   * Obtains the WID
   *
   * @return the WID
   */

  public int getWid ()
  {
    return this.mWid;
  }

  /**
   * Returns a String representation of a Word object
   *
   * @return String representation of a Word object
   */

  @Override
  public String toString ()
  {
    StringBuilder cTempBuilder = new StringBuilder ();
    cTempBuilder.append (mcEnglishWord);
    cTempBuilder.append ("\n");
    cTempBuilder.append (mcLexical);
    cTempBuilder.append ("\n");
    cTempBuilder.append (mcDefinition);
    return cTempBuilder.toString ();
  }
}