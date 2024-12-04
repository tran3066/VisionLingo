package edu.pacificu.cs.cs325.translationapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusinessLogic extends ViewModel
{
  private final MutableLiveData<BusinessLogicUIState> uiState;

  private boolean mbPictureTaken;
  private boolean mbSignedIn;

  public BusinessLogic() {
    mbSignedIn = false;
    mbPictureTaken = false;
    uiState = new MutableLiveData<> (new BusinessLogicUIState (
        new User("",""), null, "", mbPictureTaken, mbSignedIn));
  }

  public void takePicture(Byte[] cImage)
  {
    mbPictureTaken = true;
    uiState.setValue(new BusinessLogicUIState (
        new User("",""), null, "", mbPictureTaken, mbSignedIn));
  }

  public LiveData<BusinessLogicUIState> getUiState()
  {
    return uiState;
  }
}
