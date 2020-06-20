package udit.programmer.co.firecamera

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Rational
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_camera_x.*
import java.io.File
import java.util.concurrent.Executor

class CameraXActivity : AppCompatActivity(), Executor {
    override fun execute(command: Runnable?) {
        command!!.run()
    }

    val activity: CameraXActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_x)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            texture_view.post {
                startCamera()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA
                ),
                1234
            )
        }

    }

    private fun startCamera() {
        val imageConfig = ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational.POSITIVE_INFINITY)
            setLensFacing(CameraX.LensFacing.BACK)
        }.build()
        val imageCapture = ImageCapture(imageConfig)
        camera_capture_button.setOnClickListener {
            val fileName = "${System.currentTimeMillis()}.jpg"
            val file = File(externalMediaDirs.firstOrNull(), fileName)
            imageCapture.takePicture(file, object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    activity.runOnUiThread {
                        Toast.makeText(this@CameraXActivity, "Image Captured", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onError(
                    useCaseError: ImageCapture.UseCaseError,
                    message: String,
                    cause: Throwable?
                ) {
                    activity.runOnUiThread {
                        Toast.makeText(
                            this@CameraXActivity,
                            "Image Not Captured",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            })
        }

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational.POSITIVE_INFINITY)
            setLensFacing(CameraX.LensFacing.BACK)
        }.build()
        val preview = Preview(previewConfig)
        preview.setOnPreviewOutputUpdateListener {
            val parent = texture_view.parent as ViewGroup
            parent.removeView(texture_view)
            parent.addView(texture_view, 0)
            texture_view.surfaceTexture = it.surfaceTexture
        }

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

}