package edu.pacificu.cs.cs325.translationapp;

/**
 * Creates a UserPreference class that is used to give us information about the
 * users preference to keep track of including their language choice and color
 *
 * @author AaJanae Henry
 */

public class UserPreference
{
  private String mcColor;
  private String mcLanguage;

  /**
   * Constructs the UserPreference class members
   *
   * @param mcColor    the color choice from the user
   * @param mcLanguage the language choice from the user
   */

  public UserPreference (String mcColor, String mcLanguage)
  {
    this.mcColor = mcColor;
    this.mcLanguage = mcLanguage;
  }

  /**
   * Sets the UserPreference language class member
   *
   * @param mcLanguage the language choice from the user
   */

  public void setMcLanguage (String mcLanguage)
  {
    this.mcLanguage = mcLanguage;
  }

  /**
   * Sets the UserPreference color class member
   *
   * @param mcColor the color choice from the user
   */

  public void setMcColor (String mcColor)
  {
    this.mcColor = mcColor;
  }

  /**
   * Gets the UserPreference color class member
   *
   * @return mcColor the color choice from the user
   */

  public String getColor ()
  {
    return mcColor;
  }

  /**
   * Gets the UserPreference language class member
   *
   * @return mcLanguage the language choice from the user
   */

  public String getLanguage ()
  {
    return mcLanguage;
  }
}