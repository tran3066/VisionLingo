package edu.pacificu.cs.cs325.translationapp;

import androidx.room.RoomDatabase;

// @Database (entities = { Word.class }, version = 2)
// @TypeConverters ({ DBTypeConverter.class })
public abstract class DictionaryDB extends RoomDatabase
{
  public abstract DictionaryDAO dictionaryDao ();
}