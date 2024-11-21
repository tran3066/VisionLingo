package edu.pacificu.cs.cs325.translationapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Creates a UserDAO class used for creating functions that access the user
 * database
 *
 * @author Jason Tran
 */

@Dao
public interface UserDAO
{
  /**
   * Obtains all the users in the user database
   *
   * @return an ArrayList containing all the users stored in the user database
   */
  @Query ("SELECT * from User")
  List<User> getAll ();

  /**
   * Search for a specific user in the user database
   *
   * @param id the id associated with the user
   * @return user if found in the user database
   */

  @Query ("SELECT * FROM User WHERE mUid=:id")
  User getWord (int id);

  /**
   * Insert a user into the user database
   *
   * @param cUser user to insert
   */

  @Insert
  void insert (User cUser);

  /**
   * Delete a user from the user database
   *
   * @param cUser user to delete
   */

  @Delete
  void delete (User cUser);

  /**
   * Returns the number of items in the user database
   *
   * @return the size of the user database
   */

  @Query ("SELECT COUNT(*) FROM User")
  int getSize ();

  /**
   * Delete all words in the user database (for debugging)
   */

  @Query ("DELETE FROM User")
  void deleteAll ();
}