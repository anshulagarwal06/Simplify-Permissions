package me.anshulagarwal.example;

import android.Manifest;
import android.os.Bundle;

import me.anshulagarwal.simplifypermissions.MarshmallowSupportActivity;
import me.anshulagarwal.simplifypermissions.Permission;

public class MainActivity extends MarshmallowSupportActivity implements Permission.PermissionCallback {

    private static final int PHOTO_ACTIVITY_REQUEST_CARMERA_AND_READ_WRITE = 50;
    private static final String[] PHOTO_ACTIVITY_CAMERA_PERMISSIONS = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionCamera();
    }

    private void checkPermissionCamera() {

        Permission.PermissionBuilder permissionBuilder = new Permission.PermissionBuilder(PHOTO_ACTIVITY_CAMERA_PERMISSIONS, PHOTO_ACTIVITY_REQUEST_CARMERA_AND_READ_WRITE, this);
        permissionBuilder.enableDefaultRationalDialog("Allow camera access", "Without camera permission we are unable to take product image.Go ahead and grand permission.")
                .enableDefaultSettingDialog("Permission Error", "Setting dialog message");
        requestAppPermissions(permissionBuilder.build());
    }


    @Override
    public void onPermissionGranted(int requestCode) {

    }

    @Override
    public void onPermissionDenied(int requestCode) {

    }

    @Override
    public void onPermissionAccessRemoved(int requestCode) {

    }
}
