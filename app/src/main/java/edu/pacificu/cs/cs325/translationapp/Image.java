package edu.pacificu.cs.cs325.translationapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Creates an image class used to store byte arrays in their own individual rows
 * in the database
 *
 * @author Jason Tran
 */

@Entity
public class Image
{
  @PrimaryKey (autoGenerate = true)
  private int mId;

  @ColumnInfo (name = "mcImage")
  private byte[] mcImage;

  /**
   * Constructs the Image class members
   *
   * @param mcImage image captured from CameraFragment
   */

  public Image (byte[] mcImage)
  {
    this.mcImage = mcImage;
  }

  /**
   * Gets the id
   *
   * @return the id
   */

  public int getMId ()
  {
    return mId;
  }

  /**
   * Sets the id
   *
   * @param id - the primary key to set
   */

  public void setMId (int id)
  {
    this.mId = id;
  }

  /**
   * Gets image
   *
   * @return the image
   */

  public byte[] getMcImage ()
  {
    return mcImage;
  }

  /**
   * Sets image
   *
   * @param cImage image to set
   */

  public void setMcImage (byte[] cImage)
  {
    this.mcImage = cImage;
  }
}