package edu.pacificu.cs.cs325.translationapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusinessLogic extends ViewModel
{
  private final MutableLiveData<BusinessLogicUIState> uiState;
  private boolean mbPictureTaken;
  private boolean mbSignedIn;
  private Byte[] mcImage;
  private User mcUser;
  private String mcWord;

  public BusinessLogic() {
    mbSignedIn = false;
    mcWord = "";
    mbPictureTaken = false;
    uiState = new MutableLiveData<> (new BusinessLogicUIState (
        new User("",""), null, mcWord, mbPictureTaken, mbSignedIn));
  }

  public void takePicture(Byte[] cImage)
  {
    mbPictureTaken = true;
    this.mcImage = cImage;
    uiState.setValue(new BusinessLogicUIState (
        mcUser, mcImage, mcWord, mbPictureTaken, mbSignedIn));
  }

  public LiveData<BusinessLogicUIState> getUiState() {
    return uiState;
  }

  public void createUser (String username, String password) {
    mcUser = new User(username,password);
    uiState.setValue(new BusinessLogicUIState( mcUser, mcImage, mcWord, 
            mbPictureTaken, mbSignedIn));
  }
  

  public User getUser () {
    return mcUser;
  }
}
