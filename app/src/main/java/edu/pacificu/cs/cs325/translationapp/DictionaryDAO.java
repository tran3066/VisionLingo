package edu.pacificu.cs.cs325.translationapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Creates a DictionaryDAO class used for creating functions that access the
 * dictionary database
 *
 * @author Jason Tran
 */

@Dao
public interface DictionaryDAO
{
  /**
   * Obtains all the words in the dictionary database
   *
   * @return an ArrayList containing all the words stored in the dictionary
   * database
   */
  @Query ("SELECT * from Word")
  List<Word> getAll ();

  /**
   * Search for a specific word in the dictionary database
   *
   * @param id the id associated with the word
   * @return word if found in the dictionary database
   */

  @Query ("SELECT * FROM Word WHERE mWid=:id")
  Word getWord (int id);

  /**
   * Insert a word into the dictionary database
   *
   * @param cWord word to insert
   */

  @Insert
  void insert (Word cWord);

  /**
   * Delete a word from the dictionary database
   *
   * @param cWord word to delete
   */

  @Delete
  void delete (Word cWord);

  /**
   * Returns the number of items in the dictionary database
   *
   * @return the size of the dictionary database
   */

  @Query ("SELECT COUNT(*) FROM Word")
  int getSize ();

  /**
   * Delete all words in the dictionary database (for debugging)
   */

  @Query ("DELETE FROM Word")
  void deleteAll ();
}