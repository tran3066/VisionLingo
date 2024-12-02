package edu.pacificu.cs.cs325.translationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transfer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent recieveIntent = getIntent();
        Intent cameraIntent = new Intent (this, CameraActivity.class);
        Intent preferenceIntent = new Intent(this, PreferenceActivity.class);
        if (null == recieveIntent)
        {


            startActivity(cameraIntent);
        }
        else if (recieveIntent.getType().equals("String")){
            startActivity(preferenceIntent);

        }
    }
}