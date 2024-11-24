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
          if (!cLine.trim ().isEmpty () && !cLine.equals ("A"))
          {
            String[] cSplit = cLine.replaceAll ("  ", " ")
                .split (" ", THREE_PARTS);

            if (cSplit.length == THREE_PARTS)
            {
              Word cWord = new Word (cSplit[WORD], cSplit[LEXICAL],
                  cSplit[DEFINITION]);
              cDAO.insert (cWord);
            }
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