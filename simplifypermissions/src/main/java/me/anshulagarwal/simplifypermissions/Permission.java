package me.anshulagarwal.simplifypermissions;

import android.support.annotation.NonNull;

/**
 * Created by Anshul on 04/08/16.
 */
public class Permission {


    private String[] requestedPermissions;
    private int requestCode;
    private PermissionCallback mPermissionCallback = null;

    //Custom Rational Dialog
    private boolean showCustomRationalDialog = true;
    private String rationalDialogTitle;
    private String rationalDialogMessage;
    //Custom Setting Dialog
    private boolean showCustomSettingDialog = true;
    private String settingDialogTitle;
    private String settingDialogMessage;

    private Permission(PermissionBuilder builder) {
        requestedPermissions = builder.requestedPermissions;
        requestCode = builder.requestCode;
        mPermissionCallback = builder.mPermissionCallback;

        showCustomRationalDialog = builder.showCustomRationDialog;
        if (!showCustomRationalDialog) {
            rationalDialogMessage = builder.rationalDialogMessage;
            rationalDialogTitle = builder.rationalDialogTitle;
        }

        showCustomSettingDialog = builder.showCustomSettingDialog;
        if (!showCustomSettingDialog) {
            settingDialogMessage = builder.settingDialogMessage;
            settingDialogTitle = builder.settingDialogTitle;
        }

    }

    public String[] getRequestedPermissions() {
        return requestedPermissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public PermissionCallback getPermissionCallback() {
        return mPermissionCallback;
    }

    public boolean isShowCustomRationalDialog() {
        return showCustomRationalDialog;
    }

    public String getRationalDialogTitle() {
        return rationalDialogTitle;
    }

    public String getRationalDialogMessage() {
        return rationalDialogMessage;
    }

    public boolean isShowCustomSettingDialog() {
        return showCustomSettingDialog;
    }

    public String getSettingDialogTitle() {
        return settingDialogTitle;
    }

    public String getSettingDialogMessage() {
        return settingDialogMessage;
    }

    public static class PermissionBuilder {

        private String[] requestedPermissions;
        private int requestCode;

        //Custom Rational Dialog
        private boolean showCustomRationDialog = true;
        private String rationalDialogTitle;
        private String rationalDialogMessage;

        //Custom Setting Dialog
        private boolean showCustomSettingDialog = true;
        private String settingDialogTitle;
        private String settingDialogMessage;


        public PermissionCallback mPermissionCallback = null;

        public PermissionBuilder(final String[] requestedPermissions, final int requestCode, @NonNull PermissionCallback listener) {
            this.requestedPermissions = requestedPermissions;
            this.requestCode = requestCode;
            this.mPermissionCallback = listener;
        }


        public PermissionBuilder enableDefaultRationalDialog(@NonNull String title, @NonNull String message) {
            showCustomRationDialog = false;
            rationalDialogMessage = message;
            rationalDialogTitle = title;
            return this;
        }

        public PermissionBuilder enableDefaultSettingDialog(@NonNull String title, @NonNull String message) {
            showCustomSettingDialog = false;
            settingDialogMessage = message;
            settingDialogTitle = title;
            return this;
        }

        public Permission build() {
            return new Permission(this);
        }
    }

    public interface PermissionCallback {
        void onPermissionGranted(int requestCode);

        void onPermissionDenied(int requestCode);

        void onPermissionAccessRemoved(int requestCode);

    }

}
