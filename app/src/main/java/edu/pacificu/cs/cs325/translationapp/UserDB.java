package edu.pacificu.cs.cs325.translationapp;

import androidx.room.RoomDatabase;

// @Database (entities = { Word.class }, version = 2)
// @TypeConverters ({ DBTypeConverter.class })
public abstract class UserDB extends RoomDatabase
{
  public abstract UserDAO userDao ();
}