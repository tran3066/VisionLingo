package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Creates a CameraActivity class that lets the user take a picture and displays
 * the image they have taken in the “display image” box. It also displays the
 * word of the object detected in the image on the same screen.  The user then
 * has the option to translate the word of the object which sends them to
 * InfoActivity to display the information of the word they just took a picture
 * of.
 *
 * @author AaJanae Henry
 */

public class CameraActivity extends AppCompatActivity
{
  private final String LOG_TAG = "CameraActivity";

  private ActivityResultLauncher<String> mCameraPermissionRequest;
  private boolean mbCameraPermission;
  private String mcWordFromObject;
  private ImageCapture mcImageCapture;
  private Button mcTakePicture;
  private Button mcTranslate;
  private ImageView mcCptImageView;
  private TextView mcCptTextView;
  private PreviewView mcCameraView;
  private ScheduledExecutorService mcCameraBackgroundExecutor;
  private ListenableFuture<ProcessCameraProvider> mcCameraProviderFuture;
  private byte[] mByteArray;
  private ActivityResultLauncher<Intent> mActivityLauncher;

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
    setContentView (R.layout.activity_camera);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets cSystemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (cSystemBars.left, cSystemBars.top, cSystemBars.right,
              cSystemBars.bottom);
          return insets;
        });

    ObjectDetectorOptions cOptions = new ObjectDetectorOptions.Builder ().setDetectorMode (
        ObjectDetectorOptions.STREAM_MODE).enableClassification ().build ();

    mcCptImageView = findViewById (R.id.cptIMG);
    mcCptTextView = findViewById (R.id.txtTextView);
    mcCameraView = findViewById (R.id.cameraPreview);
    mcTakePicture = findViewById (R.id.btnTakePicture);
    mcTranslate = findViewById (R.id.btnTranslate);
    ObjectDetector cObjectDetector = ObjectDetection.getClient (cOptions);

    mbCameraPermission = false;

    mCameraPermissionRequest = registerForActivityResult (
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
    mcCameraProviderFuture = ProcessCameraProvider.getInstance (this);
    mcCameraProviderFuture.addListener (() -> {
      try
      {
        ProcessCameraProvider cCameraProvider = mcCameraProviderFuture.get ();
        bindPreview (cCameraProvider);
      }
      catch (ExecutionException | InterruptedException e)
      {
        throw new RuntimeException (e);
      }
    }, ContextCompat.getMainExecutor (this));
    checkPermissions ();
    mcTakePicture.setOnClickListener ((view) -> {
      ByteArrayOutputStream cByteArrayStream = new ByteArrayOutputStream ();
      ImageCapture.OutputFileOptions cOutputFileOptions = null;
      cOutputFileOptions = new ImageCapture.OutputFileOptions.Builder (
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
              cObjectDetector.process (cInputImage).addOnSuccessListener (
                  new OnSuccessListener<List<DetectedObject>> ()
                  {
                    /**
                     * Runs if objects were successfully detected in the image
                     *
                     * @param cDetectedObjects objects detected in the image
                     */

                    @Override
                    public void onSuccess (
                        List<DetectedObject> cDetectedObjects)
                    {
                      Log.d ("TEXT", "SUCCESS");

                      for (DetectedObject cDetectedObject : cDetectedObjects)
                      {
                        Rect cBoundingBox = cDetectedObject.getBoundingBox ();
                        Integer cTrackingId = cDetectedObject.getTrackingId ();
                        for (DetectedObject.Label cLabel : cDetectedObject.getLabels ())
                        {
                          String text = cLabel.getText ();
                          if (PredefinedCategory.FOOD.equals (text))
                          {
                            mcWordFromObject = text;
                          }
                        }
                      }
                      Log.d ("Object Word:", mcWordFromObject);
                      runOnUiThread (() -> {
                        if (mcWordFromObject != null
                            && !mcWordFromObject.isEmpty ())
                        {
                          mcCptTextView.setText (mcWordFromObject);
                        }
                        else
                        {
                          mcCptTextView.setText ("");
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

                @Override
                public void onFailure (@NonNull Exception cError)
                {
                  Log.d ("TEXT", "FAILURE " + cError.getMessage ());
                }
              });

              runOnUiThread (() -> {
                mcCptImageView.setImageBitmap (BitmapFactory.decodeByteArray (
                    cByteArrayStream.toByteArray (), 0,
                    cByteArrayStream.toByteArray ().length));
                mByteArray = cByteArrayStream.toByteArray ();
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

    Intent cIntentSend = new Intent (this, HomeActivity.class);
    mcTranslate.setOnClickListener (view -> {
      Log.d (LOG_TAG, "Launch Translate Button");
      mActivityLauncher.launch (cIntentSend);
      Log.d (LOG_TAG, "Info Activity started");
    });
  }

  /**
   * Checks if the app has permission to use the device's camera, and if not,
   * requests permission from the user
   */

  private void checkPermissions ()
  {
    if (ActivityCompat.checkSelfPermission (this,
        android.Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
    {
      mCameraPermissionRequest.launch (android.Manifest.permission.CAMERA);
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
    mcImageCapture = new ImageCapture.Builder ().setTargetRotation (
        Objects.requireNonNull (getDisplay ()).getRotation ()).build ();
    Preview cPreview = new Preview.Builder ().build ();
    CameraSelector cCameraSelector = new CameraSelector.Builder ().requireLensFacing (
        CameraSelector.LENS_FACING_BACK).build ();
    cPreview.setSurfaceProvider (mcCameraView.getSurfaceProvider ());
    Camera cCamera = cCameraProvider.bindToLifecycle (this, cCameraSelector,
        cPreview, mcImageCapture);
  }
}