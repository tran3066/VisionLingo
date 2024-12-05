package edu.pacificu.cs.cs325.translationapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusinessLogic extends ViewModel
{
  private final MutableLiveData<BusinessLogicUIState> uiState;
  private boolean mbPictureTaken;
  private boolean mbSignedIn;
  private byte[] mcImage;
  private User mcUser;
  private String mcWordFromCamera;

  public BusinessLogic() {
    mbSignedIn = false;
    mcWordFromCamera = "";
    mbPictureTaken = false;
    uiState = new MutableLiveData<> (new BusinessLogicUIState (
        new User("",""), null, mcWordFromCamera, mbPictureTaken,
            mbSignedIn));
  }

  public void takePicture(byte[] cImage)
  {
    mbPictureTaken = true;
    this.mcImage = cImage;
    uiState.setValue(new BusinessLogicUIState (
        mcUser, mcImage, mcWordFromCamera, mbPictureTaken, mbSignedIn));
  }

  public byte[] getImage() {
    return mcImage;
  }
  
  public void detectWord (String cWord)
  {
    this.mcWordFromCamera = cWord;
    uiState.setValue(new BusinessLogicUIState (
            mcUser, mcImage, mcWordFromCamera, mbPictureTaken, mbSignedIn));
  }
  
  public String getWordFromCamera ()
  {
    return mcWordFromCamera;
  }

  public LiveData<BusinessLogicUIState> getUiState() {
    return uiState;
  }


  public void createUser (String username, String password) {
    mcUser = new User(username,password);
    mbSignedIn = true;
    uiState.setValue(new BusinessLogicUIState( mcUser, mcImage, mcWordFromCamera, 
            mbPictureTaken, mbSignedIn));
  }
  

  public User getUser () {
    return mcUser;
  }
}
