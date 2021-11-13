package com.muza.mytabel.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;

public class PermissionUtil {

    private static final String[] READ_WRITE_PERMISSIONS =
	{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean checkAndRequest(Activity act, int reqId) {

        boolean hasRead = ContextCompat.checkSelfPermission(act,
															Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean hasWrite = ContextCompat.checkSelfPermission(act,
															 Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasRead || !hasWrite) {
            ActivityCompat.requestPermissions(act, READ_WRITE_PERMISSIONS,
											  reqId);

            return false;
        }

        return true;
    }
}
