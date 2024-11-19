package edu.pacificu.cs.cs325.translationapp;

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

  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
    EdgeToEdge.enable (this);
    setContentView (R.layout.activity_camera);
    ViewCompat.setOnApplyWindowInsetsListener (findViewById (R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets (
              WindowInsetsCompat.Type.systemBars ());
          v.setPadding (systemBars.left, systemBars.top, systemBars.right,
              systemBars.bottom);
          return insets;
        });

    ObjectDetectorOptions options = new ObjectDetectorOptions.Builder ().setDetectorMode (
        ObjectDetectorOptions.STREAM_MODE).enableClassification ().build ();

    mcCptImageView = findViewById (R.id.cptIMG);
    mcCptTextView = findViewById (R.id.txtTextView);
    mcCameraView = findViewById (R.id.cameraPreview);
    mcTakePicture = findViewById (R.id.btnTakePicture);
    mcTranslate = findViewById (R.id.btnTranslate);
    ObjectDetector objectDetector = ObjectDetection.getClient (options);

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
        ProcessCameraProvider cameraProvider = mcCameraProviderFuture.get ();
        bindPreview (cameraProvider);
      }
      catch (ExecutionException | InterruptedException e)
      {
        throw new RuntimeException (e);
      }
    }, ContextCompat.getMainExecutor (this));
    checkPermissions ();
    mcTakePicture.setOnClickListener ((view) -> {
      ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream ();
      ImageCapture.OutputFileOptions outputFileOptions = null;
      outputFileOptions = new ImageCapture.OutputFileOptions.Builder (
          byteArrayStream).build ();

      mcCameraBackgroundExecutor = Executors.newSingleThreadScheduledExecutor ();

      mcImageCapture.takePicture (outputFileOptions, mcCameraBackgroundExecutor,
          new ImageCapture.OnImageSavedCallback ()
          {
            @Override
            public void onImageSaved (
                @NonNull ImageCapture.OutputFileResults outputFileResults)
            {
              Bitmap cImage = BitmapFactory.decodeByteArray (
                  byteArrayStream.toByteArray (), 0,
                  byteArrayStream.toByteArray ().length);
              InputImage image = InputImage.fromBitmap (cImage, 0);
              objectDetector.process (image).addOnSuccessListener (
                  new OnSuccessListener<List<DetectedObject>> ()
                  {
                    @Override
                    public void onSuccess (List<DetectedObject> detectedObjects)
                    {
                      Log.d ("TEXT", "SUCCESS");

                      for (DetectedObject detectedObject : detectedObjects)
                      {
                        Rect boundingBox = detectedObject.getBoundingBox ();
                        Integer trackingId = detectedObject.getTrackingId ();
                        for (DetectedObject.Label label : detectedObject.getLabels ())
                        {
                          String text = label.getText ();
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
                @Override
                public void onFailure (@NonNull Exception e)
                {
                  Log.d ("TEXT", "FAILURE " + e.getMessage ());
                }
              });

              runOnUiThread (() -> {
                mcCptImageView.setImageBitmap (BitmapFactory.decodeByteArray (
                    byteArrayStream.toByteArray (), 0,
                    byteArrayStream.toByteArray ().length));
                mByteArray = byteArrayStream.toByteArray ();
              });
              Log.d ("Camera:", "Took a picture!");
            }

            @Override
            public void onError (@NonNull ImageCaptureException error)
            {
              Log.d ("Camera: ", "error!" + error.toString ());
            }
          });
    });

    //Intent intentSend = new Intent(this, WordActivity.class);
    mcTranslate.setOnClickListener (view -> {
      Log.d (LOG_TAG, "Launch Translate Button");
      // intentSend.putExtra("Picture", mByteArray);
      //setResult(RESULT_OK, intentSend);
      //                    Log.d(LOG_TAG, "Sent Picture");
      //                    intentSend.putExtra("Text", mcWordFromObject);
      //                    setResult(RESULT_OK, intentSend);
      //                    Log.d(LOG_TAG, "Sent Text");
      finish ();
    });

  }

  private void checkPermissions ()
  {
    if (ActivityCompat.checkSelfPermission (this,
        android.Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
    {
      mCameraPermissionRequest.launch (android.Manifest.permission.CAMERA);
    }
  }

  void bindPreview (@NonNull ProcessCameraProvider cameraProvider)
  {
    checkPermissions ();
    mcImageCapture = new ImageCapture.Builder ().setTargetRotation (
        Objects.requireNonNull (getDisplay ()).getRotation ()).build ();
    Preview preview = new Preview.Builder ().build ();
    CameraSelector cameraSelector = new CameraSelector.Builder ().requireLensFacing (
        CameraSelector.LENS_FACING_BACK).build ();
    preview.setSurfaceProvider (mcCameraView.getSurfaceProvider ());
    Camera camera = cameraProvider.bindToLifecycle (this, cameraSelector,
        preview, mcImageCapture);
  }
}