package edu.pacificu.cs.cs325.translationapp;

public class BusinessLogicUIState
{
  private User mcUser;

  public BusinessLogicUIState(User mcUser) {
    this.mcUser = mcUser;
  }

  public User getMcUser() {
    return mcUser;
  }
}
