package com.example.hirotoshin.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends Activity {

    private Uri m_uri;
    private static final int REQUEST_CHOOSER = 1000;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int EMO = 999;
    private final int PHY = 998;
    private final int CUL = 997;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setbuttonListener();
        ImageButton stampbutton = (ImageButton)findViewById(R.id.imageButton);
        stampbutton.setImageResource(R.drawable.stampstamp2);
    }

    private void setbuttonListener() {
        Button button1 = (Button) findViewById(R.id.buttonPanel);
        Button button2 = (Button) findViewById(R.id.camera_button);
        ImageButton stampbutton = (ImageButton)findViewById(R.id.imageButton);
        button1.setOnClickListener(button1_onClick);
        button2.setOnClickListener(button2_onClick);
        stampbutton.setOnClickListener(stampbutton_onclick);
    }

    private View.OnClickListener button1_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showGallery();
        }
    };

    private View.OnClickListener button2_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playCamera();
        }
    };

    private View.OnClickListener stampbutton_onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
            int requestcode = 666;
            startActivityForResult(intent, requestcode);
        }
    };

    private void playCamera() {

        //カメラの起動Intentの用意
        String photoName = System.currentTimeMillis() + ".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, photoName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        m_uri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);
        startActivityForResult( intentCamera, REQUEST_CHOOSER);

    }

    private void showGallery() {
        // ギャラリー用のIntent作成
        Intent intentGallery;
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
            intentGallery.setType("image/*");
        } else {
            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
            intentGallery.setType("image/jpeg");
        }
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = Intent.createChooser(intentCamera, "画像の選択");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentGallery});
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CHOOSER) {

                if (resultCode != RESULT_OK) {
                    // キャル時
                    return;
                }

                Uri resultUri = (data != null ? data.getData() : m_uri);

                if (resultUri == null) {
                    // 取得失敗
                    return;
                }

                // ギャラリーへスキャンを促す
                MediaScannerConnection.scanFile(
                        this,
                        new String[]{resultUri.getPath()},
                        new String[]{"image/jpeg"},

                        null
                );

                // 画像を設定
                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                imageView.setImageURI(resultUri);
            }
        if(requestCode == 666){
            if(resultCode == Activity.RESULT_OK){
                int flag = data.getIntExtra("stamp_number", -10);
                FrameLayout frame= (FrameLayout) findViewById(R.id.framelayout);
                FrameLayout.LayoutParams prams = new FrameLayout.LayoutParams(WC,WC);
                DragViewListener dvListener;
                switch (flag){
                    case 1:
                        int emo_id = View.generateViewId();
                        ImageView emotional = new ImageView(this);
                        emotional.setImageResource(R.drawable.stampemotional2);
                        emotional.setId(emo_id);
                        frame.addView(emotional, prams);
                        dvListener = new DragViewListener(emotional);
                        emotional.setOnTouchListener(dvListener);

                        break;
                    case 2:
                        int phy_id = View.generateViewId();
                        ImageView physical = new ImageView(this);
                        physical.setImageResource(R.drawable.stampphysical2);
                        physical.setId(phy_id);
                        frame.addView(physical, prams);
                        dvListener = new DragViewListener(physical);
                        physical.setOnTouchListener(dvListener);
                        break;
                    case 3:
                        int cul_id = View.generateViewId();
                        ImageView culture = new ImageView(this);
                        culture.setImageResource(R.drawable.stumpculture2);
                        culture.setId(cul_id);
                        frame.addView(culture, prams);
                        dvListener = new DragViewListener(culture);
                        culture.setOnTouchListener(dvListener);
                        break;
                }
            }
        }
    }

}