package edu.pacificu.cs.cs325.translationapp;

import java.io.InputStream;

/**
 * Creates an abstract IDictionaryReader class that stores an InputStream object
 * that determines where the dictionary will read
 *
 * @author Jason Tran
 */

public abstract class IDictionaryReader
{
  private final InputStream mcInputStream;

  /**
   * Initializes IDictionaryReader using parameter list values
   *
   * @param cInputStream input stream from which the dictionary will read
   */

  public IDictionaryReader (InputStream cInputStream)
  {
    mcInputStream = cInputStream;
  }

  /**
   * Pure virtual method that is intended to read from the input stream
   *
   * @param cDictionary dictionary containing the Oxford dictionary
   * @return true if the input stream was successfully read from; else, false
   */

  public abstract boolean read (Dictionary cDictionary);

  /**
   * Obtain the input stream
   *
   * @return input stream (mcInputStream)
   */

  protected InputStream getInputStream ()
  {
    return mcInputStream;
  }
}