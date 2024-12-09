package edu.pacificu.cs.cs325.translationapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Creates a ImageDB class that serves as the database to store images in
 * individual rows
 *
 * @author Jason Tran
 */

@Database (entities = { Image.class }, version = 2)
public abstract class ImageDB extends RoomDatabase
{
  public abstract ImageDAO imageDao ();
}