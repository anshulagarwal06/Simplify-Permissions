package me.anshulagarwal.simplifypermissions;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;


/**
 * Created by Anshul on 6/24/16.
 */
public abstract class MarshmallowSupportActivity extends AppCompatActivity {


    private Permission.PermissionCallback mPermissionCallback = null;
    private Permission mPermission = null;

    public void requestAppPermissions(@NonNull Permission permission) {
        mPermission = permission;
        mPermissionCallback = mPermission.getPermissionCallback();
        requestAppPermissions(mPermission.getRequestedPermissions(), mPermission.getRequestCode(), mPermission.getPermissionCallback());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (verifyPermissions(grantResults)) {
            mPermissionCallback.onPermissionGranted(requestCode);
        } else {
            boolean showRationale = shouldShowRequestPermissionRationale(permissions);
            if (!showRationale) {
                doNotAskedEnable(requestCode);
            } else {
                showRationalMessage(requestCode);
            }
        }

    }

    private void requestAppPermissions(final String[] requestedPermissions, final int requestCode, @Nullable Permission.PermissionCallback permissionCallback) {
        if (!hasPermissions(requestedPermissions)) {
            if (shouldShowRequestPermissionRationale(requestedPermissions)) {
                showRationalMessage(requestCode);
            } else {
                ActivityCompat.requestPermissions(MarshmallowSupportActivity.this, requestedPermissions, requestCode);
            }
        } else {
            mPermissionCallback.onPermissionGranted(requestCode);
        }
    }

    protected boolean hasPermissions(String[] permissions) {
        int length = permissions.length;
        for (int i = 0; i < length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i])
                    != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    public boolean shouldShowRequestPermissionRationale(String[] permissions) {
        int length = permissions.length;
        for (int i = 0; i < length; i++) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]))
                return true;
        }
        return false;
    }

    private boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1)
            return false;

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void showRequestPermissionDialog() {
        String message = mPermission.getRationalDialogMessage();
        String positiveButton = getString(R.string.rational_permission_proceed);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(mPermission.getRationalDialogTitle())) {
            alertDialogBuilder.setTitle(mPermission.getRationalDialogTitle());

        }
        alertDialogBuilder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ActivityCompat.requestPermissions(MarshmallowSupportActivity.this, mPermission.getRequestedPermissions(), mPermission.getRequestCode());
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void showRationalMessage(int requestCode) {

        if (mPermission.isShowCustomRationalDialog()) {
            mPermissionCallback.onPermissionDenied(requestCode);
        } else {
            showRequestPermissionDialog();
        }
    }

    private void doNotAskedEnable(int requestCode) {

        if (mPermission.isShowCustomSettingDialog()) {
            mPermissionCallback.onPermissionAccessRemoved(requestCode);
        } else {
            showSettingsPermissionDialog();
        }
    }

    private void showSettingsPermissionDialog() {

        String message = mPermission.getSettingDialogMessage();
        String positiveButton = getString(R.string.permission_string_btn);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(mPermission.getSettingDialogTitle())) {
            alertDialogBuilder.setTitle(mPermission.getSettingDialogTitle());

        }
        alertDialogBuilder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startSettingActivity();
            }
        });

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();

    }

    public void startSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

}
