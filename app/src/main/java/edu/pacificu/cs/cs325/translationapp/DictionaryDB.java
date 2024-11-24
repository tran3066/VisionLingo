package edu.pacificu.cs.cs325.translationapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Creates a DictionaryDB class that serves as the database to store words
 *
 * @author Jason Tran
 */

@Database (entities = { Word.class }, version = 2)
public abstract class DictionaryDB extends RoomDatabase
{
  public abstract DictionaryDAO dictionaryDao ();
}