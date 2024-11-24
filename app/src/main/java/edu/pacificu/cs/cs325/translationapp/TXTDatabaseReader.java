package edu.pacificu.cs.cs325.translationapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Creates a TXTDatabaseReader that implements the methods in the abstract
 * class IDatabaseReader. As implied by its name, TXTDatabaseReader reads
 * from a txt file
 *
 * @author Jason Tran
 */

public class TXTDatabaseReader extends IDatabaseReader
{
  /**
   * Initializes TXTDatabaseReader using parameter list values
   *
   * @param cInputStream input stream from which the dictionary will read
   */

  public TXTDatabaseReader (InputStream cInputStream)
  {
    super (cInputStream);
  }

  /**
   * Reads from a text file
   *
   * @param cDAO database to be initialized
   * @return true if the text file was successfully read from; else, false
   */

  @Override
  public boolean read (DictionaryDAO cDAO)
  {
    final int DOES_NOT_EXIST = -1;
    final int SKIP_ONE_SPACE = 1;
    final int SKIP_TWO_SPACES = 2;

    boolean bRetVal = true;

    try
    {
      try (BufferedReader cBR = new BufferedReader (
          new InputStreamReader (this.getInputStream ())))
      {
        String cLine;

        while ((cLine = cBR.readLine ()) != null)
        {
          int twoSpacesIndex = cLine.indexOf ("  ");

          if (!cLine.trim ().isEmpty () && twoSpacesIndex != DOES_NOT_EXIST)
          {
            String cLiteralWord = cLine.substring (0, twoSpacesIndex).trim ();
            String cRest = cLine.substring (twoSpacesIndex + SKIP_TWO_SPACES)
                .trim ();
            int firstSpaceIndex = cRest.indexOf (" ");
            String cWordType = cRest.substring (0, firstSpaceIndex).trim ();
            String cDefinition = cRest.substring (
                firstSpaceIndex + SKIP_ONE_SPACE).trim ();
            Word cWord = new Word (cLiteralWord, cWordType, cDefinition);
            cDAO.insert (cWord);
          }
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