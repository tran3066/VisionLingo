package edu.pacificu.cs.cs325.translationapp;

import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment
{

  private final String LOG_TAG = "InfoActivity";
  private final String FRENCH_URL = "https://forvo.com/word/*/#fr";
  private final String SPANISH_URL = "https://forvo.com/word/*/#es";
  private Observer<BusinessLogicUIState> mcObserver;
  private BusinessLogic mcLogic;
  private FragmentInfoBinding mcBinding;
  private String mcTranslatedWord;

  public InfoFragment ()
  {
    // Required empty public constructor
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentInfoBinding.inflate (
        getLayoutInflater ());
    View view = mcBinding.getRoot ();

    return view;
  }

  @Override
  public void onViewCreated (@NonNull View view,
      @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated (view, savedInstanceState);
    assert getActivity() != null;


    mcLogic = new ViewModelProvider (getActivity ()).get(BusinessLogic.class);

    getActivity().findViewById(android.R.id.content).setBackgroundResource(mcColor);

    mcObserver = new Observer<BusinessLogicUIState> ()
    {
      @Override
      public void onChanged (BusinessLogicUIState businessLogicUIState)
      {
        //update changes here
        int colorInt = mcLogic.getUiState ().getValue ().getColorInt ();
        mcBinding.btnSearch.setBackgroundColor (colorInt);
        mcBinding.btnAdd.setBackgroundColor (colorInt);
        mcBinding.btnSpeak.setBackgroundColor (colorInt);

        mcBinding.btnSpeak.setOnClickListener (v -> {
          String cUpdatedURL;
          String cCurrentLanguage = mcLogic.getUiState ().getValue ().getLanguage ();
          if (cCurrentLanguage.equals ("French"))
          {
            cUpdatedURL = FRENCH_URL.replace ("*", mcTranslatedWord);
            openURL (cUpdatedURL);
          }
          else if (cCurrentLanguage.equals ("Spanish"))
          {
            cUpdatedURL = SPANISH_URL.replace ("*", mcTranslatedWord);
            openURL (cUpdatedURL);
          }

        });
      }
    };
    mcLogic.getUiState ().observe (getActivity (), mcObserver);


    if (mcLogic.getWordFromCamera() != null)
    {
      mcBinding.tvWordTranslate.setText (mcLogic.getWordFromCamera());
      Log.d (LOG_TAG, "Text RECEIVED");
    }
    else
    {
      Log.d (LOG_TAG, "No TEXT");
    }

    if (mcLogic.getImage() != null)
    {
      Bitmap cBitmap = BitmapFactory.decodeByteArray (mcLogic.getImage(), 0,
              mcLogic.getImage().length);
      mcBinding.imgWord.setImageBitmap (cBitmap);
      Log.d (LOG_TAG, "Picture RECEIVED");
    }
    else
    {
      Log.d (LOG_TAG, "No PICTURE");
    }



    mcBinding.btnSearch.setOnClickListener (v ->
    {
      String tempString;
      Word tempWord;
      DictionaryDAO tempDAO = mcLogic.getDAO();
      tempString = mcBinding.tvSearch.getText ().toString ();
      tempWord = tempDAO.getWordByString(tempString);
      mcBinding.tvWordInfo.setText (tempWord.toString());
      mcBinding.tvWordTranslate.setText ("spanish");
    });

    mcLogic.getUiState ().observe (getActivity (), mcObserver);
  }

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
  }


  @Override
  public void onDestroyView ()
  {
    super.onDestroyView ();
    mcBinding = null;
    mcLogic.getUiState ().removeObserver (mcObserver);
  }

  public void openURL (String cURL)
  {
    Uri cUrl = Uri.parse (cURL);
    Intent cBrowserIntent = new Intent (Intent.ACTION_VIEW, cUrl);
    cBrowserIntent.addCategory (Intent.CATEGORY_BROWSABLE);
    startActivity (cBrowserIntent);
  }

}