package com.example.ss_layoutpractice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FaceActivity extends Activity {
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_face);
        mContext = this;

       final RelativeLayout RelativeLayout_main = findViewById(R.id.RelativeLayout_main);

        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();

        final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.chanhk);

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);


        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        // Task completed successfully
                                        // ...
                                        Log.d("FACES", faces.toString());


                                        // 화면과 사진의 사이즈가 다르기 때문에 좌표를 맞춰주기 위한 작업의 이전 작업업
                                        Point p = new Point ();
                                       Display display = getWindowManager().getDefaultDisplay();
                                        display.getSize(p);

                                        for (FirebaseVisionFace face : faces) {

                                            FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                            float lex = leftEye.getPosition().getX();
                                            float ley =leftEye.getPosition().getY();

                                            FirebaseVisionFaceLandmark leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
                                            float lcx =leftCheek.getPosition().getX();
                                            float lcy =leftCheek.getPosition().getY();

                                            FirebaseVisionFaceLandmark rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
                                            float rcx =rightCheek.getPosition().getX();
                                            float rcy =rightCheek.getPosition().getY();


                                            ImageView ImageLE = new ImageView(mContext);
                                            ImageLE.setImageResource(R.drawable.mung);
                                            ImageLE.setX(p.x * lex / bitmap.getWidth()- 100);
                                            ImageLE.setY(p.y * ley / bitmap.getHeight()- 100);
                                            RelativeLayout_main.addView(ImageLE);
                                            ImageLE.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));


                                            ImageView ImageLC = new ImageView(mContext);
                                            ImageLC.setImageResource(R.drawable.left_whiskers);
                                            ImageLC.setX(p.x * lcx / bitmap.getWidth() - 100);
                                            ImageLC.setY(p.y * lcy / bitmap.getHeight()- 100);
                                            RelativeLayout_main.addView(ImageLC);
                                            ImageLC.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));


                                            ImageView ImageRC = new ImageView(mContext);
                                            ImageRC.setImageResource(R.drawable.right_whiskers);
                                            ImageRC.setX(p.x * rcx / bitmap.getWidth()- 100);
                                            ImageRC.setY(p.y * rcy / bitmap.getHeight()- 100);
                                            RelativeLayout_main.addView(ImageRC);
                                            ImageRC.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));




                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
    }
}
