package com.circleimagecrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);

    }

    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(), "image.jpg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, 1);
    }

    public void launchGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                CropImage.activity(Uri.fromFile(file))
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setFixAspectRatio(true)
                        .start(this);
            }
        }else if(requestCode == 2){
            CropImage.activity(data.getData())
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .start(this);
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                iv.setImageURI(resultUri);
            }
        }
    }
}
