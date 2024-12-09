package edu.pacificu.cs.cs325.translationapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Creates a ImageDAO class used for creating functions that access the
 * image database
 *
 * @author Jason Tran
 */

@Dao
public interface ImageDAO
{
  /**
   * Obtains all the images in the image database
   *
   * @return an ArrayList containing all the images stored in the image database
   */

  @Query ("SELECT * from Image")
  List<Image> getAll ();

  /**
   * Search for a specific image in the image database by ID
   *
   * @param id the id associated with the image
   * @return image if found in the image database
   */

  @Query ("SELECT * FROM Image WHERE mId=:id")
  Image getImage (int id);

  /**
   * Insert an image into the image database
   *
   * @param cImage image to insert
   */

  @Insert
  void insert (Image cImage);

  /**
   * Delete an image from the image database
   *
   * @param cImage image to delete
   */

  @Delete
  void delete (Image cImage);

  /**
   * Returns the number of items in the image database
   *
   * @return the size of the image database
   */

  @Query ("SELECT COUNT(*) FROM Image")
  int getSize ();

  /**
   * Delete all words in the image database (for debugging)
   */

  @Query ("DELETE FROM Image")
  void deleteAll ();
}