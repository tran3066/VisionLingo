package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import edu.pacificu.cs.cs325.translationapp.databinding.ActivityCameraBinding;
import edu.pacificu.cs.cs325.translationapp.databinding.FragmentCameraBinding;

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

public class CameraFragment extends Fragment {
    private final String LOG_TAG = "CameraActivity";

    private ActivityResultLauncher<String> mCameraPermissionRequest;
    private String mcWordFromObject;
    private ImageCapture mcImageCapture;
    private ScheduledExecutorService mcCameraBackgroundExecutor;
    private ListenableFuture<ProcessCameraProvider> mcCameraProviderFuture;
    private ActivityResultLauncher<Intent> mActivityLauncher;
    private FragmentCameraBinding mcBinding;
    private ObjectDetector mcObjectDetector;
    private boolean mbCameraPermission;
    private byte[] mByteArray;


    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mcBinding = edu.pacificu.cs.cs325.translationapp.databinding.FragmentCameraBinding.inflate(
                getLayoutInflater());
        View view = mcBinding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView ()
    {
        super.onDestroyView ();
        mcBinding = null;

    }
    @Override
    public void onViewCreated (@NonNull View view,
                               @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated (view, savedInstanceState);
        mcObjectDetector = ObjectDetection.getClient (new ObjectDetectorOptions.Builder ().setDetectorMode (
                        ObjectDetectorOptions.SINGLE_IMAGE_MODE).enableMultipleObjects()
                .enableClassification ().build ());

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
        mcCameraProviderFuture = ProcessCameraProvider.getInstance (getActivity());
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
        }, ContextCompat.getMainExecutor (getActivity()));
        checkPermissions ();

        mcBinding.btnTakePicture.setOnClickListener ((view) -> {
            ByteArrayOutputStream cByteArrayStream = new ByteArrayOutputStream ();
            ImageCapture.OutputFileOptions cOutputFileOptions = new ImageCapture
                    .OutputFileOptions.Builder (cByteArrayStream).build ();

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
                                    new OnSuccessListener<List<DetectedObject>>()
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
                                            Log.d("ObjectDetection", "Detection Success: Detected " + cDetectedObjects.size() + " objects.");

                                            StringBuilder detectedWords = new StringBuilder();

                                            for (DetectedObject cDetectedObject : cDetectedObjects)
                                            {
                                                Rect cBoundingBox = cDetectedObject.getBoundingBox ();
                                                Integer cTrackingId = cDetectedObject.getTrackingId ();
                                                //tell user to get close

                                                for (DetectedObject.Label cLabel : cDetectedObject.getLabels ())
                                                {
                                                    String text = cLabel.getText ();
                                                    float confidence = cLabel.getConfidence();
                                                    Log.d ("ObjectDetection", "Label " + text + " , Confidence: "
                                                            + confidence);


                                                    mcWordFromObject = text;
                                                    detectedWords.append(text).append(" ,");
                                                }
                                            }
                                            runOnUiThread (() -> {
                                                if (mcWordFromObject != null
                                                        && !mcWordFromObject.isEmpty ())
                                                {
                                                    Log.d ("Object Word:", mcWordFromObject);
                                                    mcBinding.txtTextView.setText (detectedWords);

                                                    //mcBinding.txtTextView.setText (mcWordFromObject);
                                                }
                                                else
                                                {
                                                    mcBinding.txtTextView.setText ("");
                                                }
                                            });
                                        }
                                    }).addOnFailureListener (new OnFailureListener()
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
                                    getActivity().runOnUiThread(() -> mcBinding.txtTextView.setText("Detection failed."));

                                }
                            });

                            runOnUiThread (() -> {
                                mcBinding.cptIMG.setImageBitmap (BitmapFactory.decodeByteArray (
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

        Intent cIntentInfo = new Intent (getActivity(), InfoActivity.class);
        mcBinding.btnTranslate.setOnClickListener (view -> {
            //businesslogic required

            Log.d (LOG_TAG, "Launch Translate Button");
            cIntentInfo.putExtra ("Picture", mByteArray);
            setResult (RESULT_OK, cIntentInfo);
            Log.d (LOG_TAG, "Sent Picture");
            cIntentInfo.putExtra ("Text", mcWordFromObject);
            setResult (RESULT_OK, cIntentInfo);
            Log.d (LOG_TAG, "Sent Text");
            startActivity (cIntentInfo);
            Log.d (LOG_TAG, "Info Activity started");
        });

    }

    /**
     * Checks if the app has permission to use the device's camera, and if not,
     * requests permission from the user
     */

    private void checkPermissions ()
    {
        if (ActivityCompat.checkSelfPermission (getActivity(),
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
        cPreview.setSurfaceProvider (mcBinding.cameraPreview.getSurfaceProvider ());
        Camera cCamera = cCameraProvider.bindToLifecycle (getActivity(), cCameraSelector,
                cPreview, mcImageCapture);
    }
}