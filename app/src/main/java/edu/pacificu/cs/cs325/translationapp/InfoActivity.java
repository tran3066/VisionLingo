package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityInfoBinding;

/**
 * Creates a InfoActivity class that opens after the user presses the translate
 * button from CameraActivity. The user is provided a translation of the word
 * detected from the image, the word type (e.g., noun, verb, adjective),
 * a definition of the word, and the picture of the item from CameraActivity.
 * The user can click the button “Speak Word” to learn how to say the word
 * phonetically. The user then has the option to add this word to the vocabulary
 * list (ListActivity) or to search for a completely new word by entering text
 * in the “Search for a word…” box. Searching for a word allows the user to add
 * their own custom words they want to find the translations for, which we will
 * use the network (Oxford Dictionary) to access a dictionary to get this word.
 *
 * @author AaJanae Henry, Jason Tran, Christian Flores
 */

public class InfoActivity extends AppCompatActivity
{
  private final String LOG_TAG = "InfoActivity";
  private final String FRENCH_URL = "https://forvo.com/word/*/#fr";
  private final String SPANISH_URL = "https://forvo.com/word/*/#es";

  private ActivityInfoBinding mcBinding;
  private String mcTranslatedWord;

  /**
   * onCreate method that starts the activity
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  protected void onCreate (Bundle cSavedInstanceState)
  {
    super.onCreate (cSavedInstanceState);
    EdgeToEdge.enable (this);
    mcBinding = ActivityInfoBinding.inflate (getLayoutInflater ());
    View cView = mcBinding.getRoot ();
    setContentView (cView);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

    Intent cIntent = getIntent ();

    if (null != cIntent)
    {
      Log.d (LOG_TAG, "got Picture Intent");
      byte[] byteArray = cIntent.getByteArrayExtra ("Picture");
      String cTextSent = cIntent.getStringExtra ("Text");

      if (cTextSent != null)
      {
        mcBinding.tvWordTranslate.setText (cTextSent);
        Log.d (LOG_TAG, "Text RECEIVED");
      }
      else
      {
        Log.d (LOG_TAG, "No TEXT");
      }
      if (byteArray != null)
      {
        Bitmap cBitmap = BitmapFactory.decodeByteArray (byteArray, 0,
            byteArray.length);
        mcBinding.imgWord.setImageBitmap (cBitmap);
        Log.d (LOG_TAG, "Picture RECEIVED");
      }
      else
      {
        Log.d (LOG_TAG, "No PICTURE");
      }
    }

//    mcBinding.btnSpeak.setOnClickListener (v -> {
//      //String cCurrentLanguage = HomeActivity.mcCurrentUser.getLanguage ();
//      String cUpdatedURL;
//
//      // Make sure to update mcTranslatedWord here
//      if (cCurrentLanguage.equals ("French"))
//      {
//        cUpdatedURL = FRENCH_URL.replace ("*", mcTranslatedWord);
//        openURL (cUpdatedURL);
//      }
//      else if (cCurrentLanguage.equals ("Spanish"))
//      {
//        cUpdatedURL = SPANISH_URL.replace ("*", mcTranslatedWord);
//        openURL (cUpdatedURL);
//      }
//    });

  }

  /**
   * Opens the URL specified by the parameter
   *
   * @param cURL URL to open
   */

  public void openURL (String cURL)
  {
    Uri cUrl = Uri.parse (cURL);
    Intent cBrowserIntent = new Intent (Intent.ACTION_VIEW, cUrl);
    cBrowserIntent.addCategory (Intent.CATEGORY_BROWSABLE);
    startActivity (cBrowserIntent);
  }
}