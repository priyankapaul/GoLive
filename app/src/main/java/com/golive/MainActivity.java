package com.golive;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.golive.utils.CallConstants.EXTRA_ROOM_ID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        Toast alertToast = Toast.makeText(this, "Room Name can't be empty", Toast.LENGTH_SHORT);

        EditText etRoomName = findViewById(R.id.et_room_name);
        findViewById(R.id.btn_enter_room).setOnClickListener(v -> {
            Editable et = etRoomName.getText();
            if (et != null) {
                String roomName = et.toString();
                if (!TextUtils.isEmpty(roomName)) {
                    connectToRoom(roomName);
                } else {
                    alertToast.show();
                }
            } else {
                alertToast.show();
            }
        });
    }


    private void requestPermissions() {
        String[] missingPermissions = getMissingPermissions();
        if (missingPermissions.length != 0) {
            requestPermissions(missingPermissions, PERMISSION_REQUEST);
        } else {
            onPermissionsGranted();
        }
    }

    private String[] getMissingPermissions() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Failed to retrieve permissions.");
            return new String[0];
        }

        if (info.requestedPermissions == null) {
            Log.w(TAG, "No requested permissions.");
            return new String[0];
        }

        ArrayList<String> missingPermissions = new ArrayList<>();
        for (int i = 0; i < info.requestedPermissions.length; i++) {
            if ((info.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0) {
                missingPermissions.add(info.requestedPermissions[i]);
            }
        }
        Log.d(TAG, "Missing permissions: " + missingPermissions);

        return missingPermissions.toArray(new String[0]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            String[] missingPermissions = getMissingPermissions();
            if (missingPermissions.length != 0) {
                // User didn't grant all the permissions. Warn that the application might not work
                // correctly.
                new AlertDialog.Builder(this)
                        .setMessage(R.string.missing_permissions_try_again)
                        .setPositiveButton(R.string.yes,
                                (dialog, id) -> {
                                    // User wants to try giving the permissions again.
                                    dialog.cancel();
                                    requestPermissions();
                                })
                        .setNegativeButton(R.string.no,
                                (dialog, id) -> {
                                    // User doesn't want to give the permissions.
                                    dialog.cancel();
                                    onPermissionsGranted();
                                })
                        .show();
            } else {
                // All permissions granted.
                onPermissionsGranted();
            }
        }
    }

    private void onPermissionsGranted() {
        // TODO -- implementation
    }

    private void connectToRoom(String roomId) {
        /*boolean videoCallEnabled = true;
        boolean useScreencapture = false;
        boolean useCamera2 = true;
        // Get default codecs.
        String videoCodec = "VP8"; // VP9, H264 Baseline, H264 High
        String audioCodec = "OPUS"; // ISAC
        boolean hwCodec = true;
        boolean captureToTexture = false;
        // Check FlexFEC.
        boolean flexfecEnabled = false;

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = false;
        boolean aecDump = false;
        boolean saveInputAudioToFile = false;

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = false;

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = true;

        // Check Disable built-in AGC flag.
        boolean disableBuiltInAGC = false;

        // Check Disable built-in NS flag.
        boolean disableBuiltInNS = false;

        // Check Disable gain control
        boolean disableWebRtcAGCAndHPF = false;

        // Get video resolution from settings.
        int videoWidth = 480;
        int videoHeight = 640;
        int cameraFps = 30;

        boolean captureQualitySlider = false;

        int videoStartBitrate = 1700; //kbps
        int audioStartBitrate = 32; //kbps


        // Check statistics display option.
        boolean displayHud = false;

        boolean tracing = false;

        // Check Enable RtcEventLog.
        boolean rtcEventLogEnabled = false;*/

        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        startActivity(intent);
        finish();
    }
}
