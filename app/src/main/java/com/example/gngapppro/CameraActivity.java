package com.example.gngapppro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CameraActivity extends Activity implements OnClickListener {

    static final int getimagesetting=1001;//for request intent
    static final int camera=672;
    Button btn_capture,btn_ok;
    static ProgressDialog pd;
    String temp="";
    String userID="";
    static Context mContext;
    Bitmap bm;

    private String mCurrentPhotoPath;
    private Uri imgUri;
    public static CameraActivity cameraActivity;
    ///////////////////
    //private static final int REQUEST_IMAGE_CAPTURE = 672;
    //private String imageFilePath;
    //private Uri photoUri;
    //private Uri imguri;

    private TextView tv_name;
    //public static AppCompatActivity cameraActivity;
    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이 것이 필요하다(미디어 스캐닝)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        tv_name=findViewById(R.id.tv_name);
        Intent intent=getIntent();

        init();
        cameraActivity=CameraActivity.this;
        // 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.
        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

        userID=intent.getStringExtra("userID");
        tv_name.setText(userID);
        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=new Intent();
        //Bitmap bm;
        ExifInterface exif = null;
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case camera:
                    /*bm=(Bitmap) data.getExtras().get("data");
                    bm=resize(bm);*/
                    galleryAddPic();
                    ((ImageView) findViewById(R.id.iv_result)).setImageURI(imgUri);//Uri를 비트맵으로 변환
                    try {
                        bm=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imgUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("bitmap",bm);
                    setResult(RESULT_OK, intent);

                    /*nt exifOrientation;
                    int exifDegree;

                    if (exif != null) {
                        exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        exifDegree = exifOrientationToDegreess(exifOrientation);
                    } else {
                        exifDegree = 0;
                    }*/
                    //((ImageView) findViewById(R.id.iv_result)).setImageBitmap(bm);

                    BitMapToString(bm);

                    //finish();
                    break;
                default:
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    break;

            }
        }else{
            setResult(RESULT_CANCELED, intent);
            finish();
        }

    }

    /**
     * bitmap을 string으로
     * @param bitmap
     * @return
     */
    public void BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);   //bitmap compress
        byte [] arr=baos.toByteArray();
        String image= Base64.encodeToString(arr, Base64.DEFAULT);


        try{
            temp= URLEncoder.encode(image,"utf-8");
        }catch (Exception e){
            Log.e("exception",e.toString());
        }



    }


    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm){

        Configuration config=getResources().getConfiguration();
      /*if(config.smallestScreenWidthDp>=800)
         bm = Bitmap.createScaledBitmap(bm, 400, 240, true);//이미지 크기로 인해 용량초과
      else */if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);

        return bm;

    }

    private void init() {
        btn_capture=(Button)findViewById(R.id.btn_capture);

        btn_ok=(Button)findViewById(R.id.btn_ok);


        btn_capture.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        mContext=this;
    }

    @Override
    public void onClick(View v) {
        //Intent intent=new Intent();

        //String state = Environment.getExternalStorageState();
        switch(v.getId()){
            case R.id.btn_capture:
                //intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /*Log.e("외부저장소","접근상태"+state);
                if(Environment.MEDIA_MOUNTED.equals(state)) {
                    intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (photoFile != null) {

                            Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                            imgUri = providerURI;

                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);

                            //startActivityForResult(intent, FROM_CAMERA);
                            startActivityForResult(intent, camera);

                        }

                    } else {
                        Log.v("notice", "저장공간 접근 불가능");
                        return;
                    }*/
                //startActivityForResult(intent, camera);
                takePhoto();
                break;

            case R.id.btn_ok:
                Intent intent=new Intent();
                intent = new Intent(CameraActivity.this, MainActivity.class);
                addImage();//새로 구현한 함수

                Toast.makeText(getApplicationContext(),"촬영이 완료되었습니다.",Toast.LENGTH_SHORT).show();


                if(temp.length()>0) {
                    insert_blob();
                    String userID = String.valueOf(tv_name);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                }
                else
                    Toast.makeText(this,"이미지가 없습니다.",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void takePhoto(){
        String state = Environment.getExternalStorageState();
        Log.e("저장환경",state);
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e("시발","if문 들어왓다");
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null) {
                Log.e("시발2", "if문 들어왓다");
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {

                    Uri providerURI = FileProvider.getUriForFile(this, "com.example.gngapppro.provider", photoFile);

                    imgUri = providerURI;

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);

                    //startActivityForResult(intent, FROM_CAMERA);
                    startActivityForResult(intent, camera);

                }
            }
        } else {
            Log.v("notice", "저장공간 접근 불가능");
            return;
        }
    }

    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis()+".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures","ireh");

        if(!storageDir.exists()){
            Log.v("알림","storageDir 존재 x"+storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림","storageDir존재함"+storageDir.toString());
        imageFile = new File(storageDir,imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    public  void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    void addImage(){
        Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
        startActivityForResult(intent,getimagesetting);
    }

    void insert_blob(){
        pd=new ProgressDialog(this);
        pd.setMessage("이미지를 DB에 저장중입니다. 잠시만 기다리세요.");
        pd.show();
        Log.e("insert image",temp);
        controlMysql adddb=new controlMysql(temp, userID);
        //controlMysql2.active=true;
        // adddb.start();
    }

    static public void add_image(String result){   //이미지 추가 결과
        if(result!=null)
            Log.e("result",result);
        // controlMysql2.active=false;
        if(result.contains("true")){
            Log.e("result",result);
            Toast.makeText(mContext, "이미지가 DB에 추가되었습니다..", Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(mContext, result+" 이미지가 DB에 추가되지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
        pd.cancel();

    }
}