package edu.pacificu.cs.cs325.translationapp;

//import static edu.pacificu.cs.cs325.translationapp.PreferenceFragment.mcColor;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import edu.pacificu.cs.cs325.translationapp.databinding.FragmentCameraBinding;

/**
 * Creates a CameraFragment class that lets the user take a picture and displays
 * the image they have taken in the “display image” box. It also displays the
 * word of the object detected in the image on the same screen.  The user then
 * has the option to translate the word of the object which sends them to
 * InfoFragment to display the information of the word they just took a picture
 * of.
 *
 * @author AaJanae Henry
 */

public class CameraFragment extends Fragment
{
  private final String LOG_TAG = "CameraFragment";

  private boolean mbCameraPermission;
  private Observer<BusinessLogicUIState> mcObserver;
  private ActivityResultLauncher<String> mcCameraPermissionRequest;
  private String mcWordFromObject;
  private ImageCapture mcImageCapture;
  private ScheduledExecutorService mcCameraBackgroundExecutor;
  private ListenableFuture<ProcessCameraProvider> mcCameraProviderFuture;
  private FragmentCameraBinding mcBinding;
  private ObjectDetector mcObjectDetector;
  private byte[] mcByteArray;
  private BusinessLogic mcLogic;
  private ProcessCameraProvider mcCameraProvider;
  private Preview mcPreview;
  private CameraSelector mcCameraSelector;

  /**
   * Initializes CameraFragment (required empty public constructor)
   */

  public CameraFragment ()
  {
  }

  /**
   * onCreate method that starts the fragment
   *
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  public void onCreate (Bundle cSavedInstanceState)
  {
    super.onCreate (cSavedInstanceState);
  }

  /**
   * onCreateView method that creates and returns the root view of the fragment
   *
   * @param cInflater           LayoutInflater object used to inflate the
   *                            fragment's view
   * @param cContainer          ViewGroup object that contains the fragment's UI
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   * @return the root view of the fragment
   */

  @Override
  public View onCreateView (@NonNull LayoutInflater cInflater,
      ViewGroup cContainer, Bundle cSavedInstanceState)
  {
    mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentCameraBinding.inflate (
        getLayoutInflater ());
    View cView = mcBinding.getRoot ();
    return cView;
  }

  /**
   * onDestroyView method that is called when the fragment is destroyed
   */

  @Override
  public void onDestroyView ()
  {
    super.onDestroyView ();
    mcBinding = null;
    //mcLogic.getMcUiState ().removeObserver (mcObserver);
  }

  /**
   * onViewCreated method that is called after the fragment is created
   *
   * @param cView               the root view of the fragment
   * @param cSavedInstanceState Stores a small amount of data needed to reload
   *                            UI state if the system stops and then recreates
   *                            UI
   */

  @Override
  public void onViewCreated (@NonNull View cView,
      @Nullable Bundle cSavedInstanceState)
  {
    super.onViewCreated (cView, cSavedInstanceState);

    assert getActivity () != null;
    mcLogic = new ViewModelProvider (getActivity ()).get (BusinessLogic.class);

    if (mcLogic.getColor () != 0)
    {
      mcBinding.btnTakePicture.setBackgroundColor (mcLogic.getColor ());
      mcBinding.btnTranslate.setBackgroundColor (mcLogic.getColor ());
    }

    mcObjectDetector = ObjectDetection.getClient (
        new ObjectDetectorOptions.Builder ().setDetectorMode (
                ObjectDetectorOptions.SINGLE_IMAGE_MODE).enableMultipleObjects ()
            .enableClassification ().build ());

    mbCameraPermission = false;

    mcCameraPermissionRequest = registerForActivityResult (
        new ActivityResultContracts.RequestPermission (), (result) -> {
          if (result)
          {
            mbCameraPermission = true;
          }
          else
          {
            mbCameraPermission = false;
          }
        });

    assert getActivity () != null;
    mcCameraProviderFuture = ProcessCameraProvider.getInstance (getActivity ());
    mcCameraProviderFuture.addListener (() -> {
      try
      {
        mcCameraProvider = mcCameraProviderFuture.get ();
        bindPreview (mcCameraProvider);
      }
      catch (ExecutionException | InterruptedException e)
      {
        throw new RuntimeException (e);
      }
    }, ContextCompat.getMainExecutor (getActivity ()));

    checkPermissions ();

    mcBinding.btnTakePicture.setOnClickListener ((v) -> {
      ByteArrayOutputStream cByteArrayStream = new ByteArrayOutputStream ();
      ImageCapture.OutputFileOptions cOutputFileOptions = new ImageCapture.OutputFileOptions.Builder (
          cByteArrayStream).build ();

      mcCameraBackgroundExecutor = Executors.newSingleThreadScheduledExecutor ();

      mcImageCapture.takePicture (cOutputFileOptions,
          mcCameraBackgroundExecutor, new ImageCapture.OnImageSavedCallback ()
          {
            /**
             * Saves the image in a ByteArrayStream once it is saved
             *
             * @param cOutputFileResults info about the saved image file
             */

            @Override
            public void onImageSaved (
                @NonNull ImageCapture.OutputFileResults cOutputFileResults)
            {

              Bitmap cImage = BitmapFactory.decodeByteArray (
                  cByteArrayStream.toByteArray (), 0,
                  cByteArrayStream.toByteArray ().length);
              InputImage cInputImage = InputImage.fromBitmap (cImage, 0);
              mcObjectDetector.process (cInputImage).addOnSuccessListener (
                  new OnSuccessListener<List<DetectedObject>> ()
                  {
                    /**
                     * Runs if objects were successfully detected in the image
                     *
                     * @param cDetectedObjects objects detected in the image
                     */

                    @SuppressLint ("SetTextI18n")
                    @Override
                    public void onSuccess (
                        List<DetectedObject> cDetectedObjects)
                    {
                      Log.d ("TEXT", "SUCCESS");
                      Log.d ("ObjectDetection", "Detection Success: Detected "
                          + cDetectedObjects.size () + " objects.");

                      for (DetectedObject cDetectedObject : cDetectedObjects)
                      {
                        for (DetectedObject.Label cLabel : cDetectedObject.getLabels ())
                        {
                          String text = cLabel.getText ();
                          Log.d (LOG_TAG, "Label: " + text);
                          mcWordFromObject = text;
                        }
                      }
                      assert getActivity () != null;
                      getActivity ().runOnUiThread (() -> {
                        if (mcWordFromObject != null
                            && !mcWordFromObject.isEmpty ())
                        {
                          Log.d ("Object Word:", mcWordFromObject);
                          mcBinding.txtTextView.setText (mcWordFromObject);
                        }
                        else
                        {
                          mcBinding.txtTextView.setText ("No Object Detected");
                          Toast.makeText (
                              getActivity ().getApplicationContext (),
                              "Get Closer to Object " + "and Try again",
                              Toast.LENGTH_LONG).show ();
                        }
                      });
                    }
                  }).addOnFailureListener (new OnFailureListener ()
              {
                /**
                 * Runs if an error occurred while trying to detect objects in
                 * the image
                 *
                 * @param cError error obtained attempting to detect objects
                 */

                @SuppressLint ("SetTextI18n")
                @Override
                public void onFailure (@NonNull Exception cError)
                {
                  Log.d ("TEXT", "FAILURE " + cError.getMessage ());
                  assert getActivity () != null;
                  getActivity ().runOnUiThread (
                      () -> mcBinding.txtTextView.setText (
                          "Detection failed."));
                }
              });

              assert getActivity () != null;
              getActivity ().runOnUiThread (() -> {
                mcBinding.cptIMG.setImageBitmap (BitmapFactory.decodeByteArray (
                    cByteArrayStream.toByteArray (), 0,
                    cByteArrayStream.toByteArray ().length));
                mcByteArray = cByteArrayStream.toByteArray ();
              });
              Log.d ("Camera:", "Took a picture!");
            }

            /**
             * Runs only if there has been an error while capturing the image
             *
             * @param cError An exception thrown to indicate an error has
             *               occurred during image capture or while saving the
             *               captured image
             */

            @Override
            public void onError (@NonNull ImageCaptureException cError)
            {
              Log.d ("Camera: ", "error!" + cError.toString ());
            }
          });
    });

    mcBinding.btnTranslate.setOnClickListener (v -> {

      Log.d (LOG_TAG, "Launch Translate Button");

      mcLogic.takePicture (mcByteArray);
      Log.d (LOG_TAG, "Sent Picture");

      mcLogic.detectWord (mcWordFromObject);
      Log.d (LOG_TAG, "Sent Text");

      getActivity ().getSupportFragmentManager ().beginTransaction ()
          .setReorderingAllowed (true)
          .replace (R.id.fragment_container_view, InfoFragment.class, null)
          .commit ();
      Log.d (LOG_TAG, "Info Activity started");
    });
  }

  /**
   * Checks if the app has permission to use the device's camera, and if not,
   * requests permission from the user
   */

  private void checkPermissions ()
  {
    assert getActivity () != null;
    if (ActivityCompat.checkSelfPermission (getActivity (),
        android.Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
    {
      mcCameraPermissionRequest.launch (android.Manifest.permission.CAMERA);
    }
  }

  /**
   * Sets up camera preview and image capture functionality
   *
   * @param cCameraProvider used to bind the lifecycle of cameras
   */

  void bindPreview (@NonNull ProcessCameraProvider cCameraProvider)
  {
    checkPermissions ();
    int rotation = Objects.requireNonNull (requireActivity ().getDisplay ())
        .getRotation ();
    mcImageCapture = new ImageCapture.Builder ().setTargetRotation (rotation)
        .build ();
    mcPreview = new Preview.Builder ().build ();
    mcCameraSelector = new CameraSelector.Builder ().requireLensFacing (
        CameraSelector.LENS_FACING_BACK).build ();
    mcPreview.setSurfaceProvider (
        mcBinding.cameraPreview.getSurfaceProvider ());
    assert getActivity () != null;
    cCameraProvider.bindToLifecycle (getActivity (), mcCameraSelector,
        mcPreview, mcImageCapture);
  }

  /**
   * onPause method that unbinds the lifecycle of the camera when the fragment
   * is paused
   */

  @Override
  public void onPause ()
  {
    super.onPause ();

    if (mcCameraProvider != null)
    {
      mcCameraProvider.unbindAll ();
    }
  }

  /**
   * onResume method that rebinds the lifecycle of the camera when the fragment
   * is resumed
   */

  @Override
  public void onResume ()
  {
    super.onResume ();

    if (mcCameraProvider != null)
    {
      mcCameraProvider.bindToLifecycle (getActivity (), mcCameraSelector,
          mcPreview, mcImageCapture);
    }
  }
}