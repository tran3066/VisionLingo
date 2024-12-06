package edu.pacificu.cs.cs325.translationapp;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

public class BusinessLogic extends ViewModel
{
  private final MutableLiveData<BusinessLogicUIState> uiState;
  private boolean mbPictureTaken;
  private boolean mbSignedIn;
  private byte[] mcImage;
  private User mcUser;
  private String mcWordFromCamera;

  private DictionaryDAO mcDictionaryDAO;


  public BusinessLogic() {
    mbSignedIn = false;
    mcWordFromCamera = "";
    mbPictureTaken = false;
    mcDictionaryDAO = null;
    uiState = new MutableLiveData<> (new BusinessLogicUIState (0,"", null, mcWordFromCamera, mbPictureTaken, mbSignedIn));
  }

  public void setDAO(DictionaryDAO cDictionaryDAO)
  {
    mcDictionaryDAO = cDictionaryDAO;
  }
  public DictionaryDAO getDAO()
  {
    return mcDictionaryDAO;
  }

  public void takePicture(byte[] cImage)
  {
    mbPictureTaken = true;
    mcImage = cImage;

    uiState.setValue(new BusinessLogicUIState (
        uiState.getValue ().getColor(), uiState.getValue ().getLanguage(),
        mcImage, mcWordFromCamera, mbPictureTaken, mbSignedIn));
  }

  public byte[] getImage() {
    return mcImage;
  }
  
  public void detectWord (String cWord)
  {
    mcWordFromCamera = cWord;
    uiState.setValue(new BusinessLogicUIState (
        uiState.getValue ().getColor(), uiState.getValue ().getLanguage(),
        mcImage, mcWordFromCamera, mbPictureTaken, mbSignedIn));
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
    uiState.setValue(new BusinessLogicUIState(
        uiState.getValue ().getColor(), uiState.getValue ().getLanguage(),
        mcImage, mcWordFromCamera, mbPictureTaken, mbSignedIn));
  }
  

  public User getUser () {
    return mcUser;
  }
}
