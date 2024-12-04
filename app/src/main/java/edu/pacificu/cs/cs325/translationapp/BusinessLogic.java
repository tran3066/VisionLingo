package edu.pacificu.cs.cs325.translationapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusinessLogic extends ViewModel
{
  private Byte[] mcImage;
  private User mcUser;
  private String mcWord;
  private final MutableLiveData<BusinessLogicUIState> uiState;

  public BusinessLogic () {
    uiState = new MutableLiveData<>(
            new BusinessLogicUIState (mcUser));
  }

  public LiveData<BusinessLogicUIState> getUiState() {
    return uiState;
  }

  public void createUser (String username, String password) {
    mcUser = new User(username,password);
    uiState.setValue(new BusinessLogicUIState(mcUser));
  }

  public User getUser () {
    uiState.setValue(new BusinessLogicUIState(mcUser));
    return mcUser;
  }
}
