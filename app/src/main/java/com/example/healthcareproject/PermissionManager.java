package com.example.healthcareproject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class PermissionManager {
    private static PermissionManager instance = null;
    private Context context;

    private PermissionManager() {
    }
    public static PermissionManager getInstance(Context context){
        if(instance == null){
            instance = new PermissionManager();
        }
        instance.init(context);
        return instance;
    }

    private void init(Context context){
        this.context = context;
    }

    public boolean checkPermission(String[] permissions){
        int size = permissions.length;

        for(String permission: permissions){
            if(ContextCompat.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }
    public void askPermission(Activity activity, String[] permissions, int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
    public boolean handlerPermission(Activity activity, String[] permissions, int requestCode, int[] grantResult){
        boolean isAllowedGrantResult = true;

        if (grantResult.length > 0) {
            for(int i = 0; i < grantResult.length; i++){
                if(grantResult[i] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else{
                    isAllowedGrantResult = false;
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else{
            isAllowedGrantResult = false;
        }
        return isAllowedGrantResult;
    }
}
