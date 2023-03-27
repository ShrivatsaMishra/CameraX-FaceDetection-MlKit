package com.androchef.cameraxfacedetection

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androchef.cameraxfacedetection.camerax.CameraManager
import kotlinx.android.synthetic.main.activity_main.*
import com.google.vr.sdk.audio.GvrAudioEngine


class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var gvrAudioEngine: GvrAudioEngine
    private var soundId: Int = GvrAudioEngine.INVALID_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createCameraManager()
        checkForPermission()
        onClicks()
        val gvrAudioEngine = GvrAudioEngine(this, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY)

        val objectSoundFile = "cube_sound.wav"
        Log.i(">>>", "Working Directory = " + System.getProperty("user.dir"))
        gvrAudioEngine.preloadSoundFile(objectSoundFile)
        val sourceId = gvrAudioEngine.createSoundObject(objectSoundFile)
        Log.i(">>>", "" + sourceId)
        gvrAudioEngine.setSoundObjectPosition(sourceId, 0f, 10f, 0f)
        gvrAudioEngine.setHeadRotation(0f, 0f, 0f, 0f)
        gvrAudioEngine.setHeadPosition(0f, 0f, 0f)
        gvrAudioEngine.playSound(sourceId, true)
    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun onClicks() {
        btnSwitch.setOnClickListener {
            cameraManager.changeCameraSelector()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            previewView_finder,
            this,
            graphicOverlay_finder
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

}