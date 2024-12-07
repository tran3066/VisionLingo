package edu.pacificu.cs.cs325.translationapp;

import java.util.HashMap;

/**
 * Creates a Dictionary class that stores a HashMap containing the Oxford
 * dictionary
 *
 * @author Christian Flores
 */

public class Dictionary
{
  private HashMap<String, Word> mcDictionary;
  private String mcLanguage;

  /**
   * Initializes Dictionary by initializing HashMap and language
   *
   * @param cLanguage the language used to translate the word to
   */

  public Dictionary (String cLanguage)
  {
    mcDictionary = new HashMap<> ();
    mcLanguage = cLanguage;
  }

  /**
   * Insert a word into the HashMap
   *
   * @param cWordName literal String representation of the word itself (key)
   * @param cTheWord  word object (value)
   */

  public void insertWord (String cWordName, Word cTheWord)
  {
    mcDictionary.put (cWordName, cTheWord);
  }

  /**
   * Get a word from the HashMap
   *
   * @param cWordName literal String representation of the word itself
   * @return word obtained from the HashMap
   */

  public Word getWord (String cWordName)
  {
    return mcDictionary.get (cWordName);
  }

  //  public ArrayList<Word> getAllWords()
  //  {
  //    ArrayList listOfWords = ArrayList<>(mcDictionary.values());
  //    return mcDictionary.values ();
  //  }

  /**
   * Get the language needed to translate a word to another language
   *
   * @return language needed to translate a word
   */

  public String getLanguage ()
  {
    return this.mcLanguage;
  }
}