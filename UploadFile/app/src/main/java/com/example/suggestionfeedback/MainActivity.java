package com.example.suggestionfeedback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    private void checkPermission() {
        int permission_write = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_write != PackageManager.PERMISSION_GRANTED
                || permission_read != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "正在请求权限", Toast.LENGTH_SHORT).show();
            //申请权限，特征码自定义为1，可在回调时进行相关判断
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限已成功申请
                    Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    //用户拒绝授权
                    Toast.makeText(this, "无法获取SD卡读写权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPicture:
                choosePicture();
                Toast.makeText(getApplicationContext(), "添加图片", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {//从相册选取视频
            String videoPath =GetPathFromUri.getPath(this, data.getData());

            Log.d("8888", "視屏路徑："+videoPath);
//            initVideo(videoPath);
        }


    }

    public void choosePicture() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
        }
        startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), 100);

    }

}