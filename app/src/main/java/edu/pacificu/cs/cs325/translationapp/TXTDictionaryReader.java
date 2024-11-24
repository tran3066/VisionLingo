package edu.pacificu.cs.cs325.translationapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a TXTDictionaryReader that implements the methods in the abstract
 * class IDictionaryReader. As implied by its name, TXTDictionaryReader reads
 * from a txt file
 *
 * @author Jason Tran
 */

public class TXTDictionaryReader extends IDictionaryReader
{
  /**
   * Initializes TXTDictionaryReader using parameter list values
   *
   * @param cInputStream input stream from which the dictionary will read
   */

  public TXTDictionaryReader (InputStream cInputStream)
  {
    super (cInputStream);
  }

  /**
   * Reads from a text file
   *
   * @param cDictionary dictionary to be initialized
   * @return true if the text file was successfully read from; else, false
   */

  @Override
  public boolean read (Dictionary cDictionary)
  {
    final int WORD = 0;
    final int LEXICAL = 1;
    final int DEFINITION = 2;
    final int THREE_PARTS = 3;

    boolean bRetVal = true;

    try
    {
      try (BufferedReader cBR = new BufferedReader (
          new InputStreamReader (this.getInputStream ())))
      {
        String cLine;

        while ((cLine = cBR.readLine ()) != null)
        {
          String[] cSplit = cLine.split (" ", THREE_PARTS);
          Word cWord = new Word (cSplit[WORD], cSplit[LEXICAL],
              cSplit[DEFINITION]);
          cDictionary.insertWord (cSplit[WORD], cWord);
        }
      }
    }
    catch (IOException cError)
    {
      bRetVal = false;
      System.out.println ("Error reading dictionary: " + cError);
    }

    return bRetVal;
  }
}