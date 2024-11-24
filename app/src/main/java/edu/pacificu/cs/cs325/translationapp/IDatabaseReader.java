package edu.pacificu.cs.cs325.translationapp;

import java.io.InputStream;

/**
 * Creates an abstract IDatabaseReader class that stores an InputStream object
 * that determines where the database will read
 *
 * @author Jason Tran
 */

public abstract class IDatabaseReader
{
  private final InputStream mcInputStream;

  /**
   * Initializes IDatabaseReader using parameter list values
   *
   * @param cInputStream input stream from which the database will read
   */

  public IDatabaseReader (InputStream cInputStream)
  {
    mcInputStream = cInputStream;
  }

  /**
   * Pure virtual method that is intended to read from the input stream
   *
   * @param cDAO database to be initialized
   * @return true if the input stream was successfully read from; else, false
   */

  public abstract boolean read (DictionaryDAO cDAO);

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