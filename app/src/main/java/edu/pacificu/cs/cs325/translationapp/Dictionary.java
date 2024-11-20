package edu.pacificu.cs.cs325.translationapp;

import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary
{
  private HashMap<String, Word> mcDictionary;

  private String mcLanguage;

  public Dictionary(String cLanguage)
  {
    mcDictionary = new HashMap<> ();
    mcLanguage = cLanguage;
  }

  public void insertWord(String theString, Word theWord)
  {
    mcDictionary.put(theString, theWord);
  }

  public Word getWord(String theWord)
  {
    return mcDictionary.get(theWord);
  }

//  public ArrayList<Word> getAllWords()
//  {
//    ArrayList listOfWords = ArrayList<>(mcDictionary.values());
//    return mcDictionary.values ();
//  }
  public String getLanguage()
  {
    return this.mcLanguage;
  }

}