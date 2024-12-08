package edu.pacificu.cs.cs325.translationapp;

import java.util.HashMap;
import java.util.List;

/**
 * Creates a Dictionary class that stores a HashMap containing the Oxford
 * dictionary
 *
 * @author Christian Flores
 */

public class Dictionary
{
  private HashMap<String, Word> mcDictionary;
  //private String mcLanguage;

  /**
   * Initializes Dictionary by initializing HashMap and language
   *
   *
   */

  public Dictionary (List<Word> mcListOfWords)
  {
    mcDictionary = new HashMap<> ();
    for(Word cWord: mcListOfWords)
    {
      mcDictionary.put (cWord.getMcEnglishWord (), cWord);
    }
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




}