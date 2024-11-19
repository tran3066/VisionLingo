package edu.pacificu.cs.cs325.translationapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Creates a UserDB class that serves as the database to store users
 *
 * @author Jason Tran
 */

@Database (entities = { User.class }, version = 2)
@TypeConverters ({ DBTypeConverter.class })
public abstract class UserDB extends RoomDatabase
{
  public abstract UserDAO userDao ();
}