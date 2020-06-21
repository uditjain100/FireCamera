package udit.programmer.co.firecamera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import kotlinx.android.synthetic.main.activity_face.*

class FaceActivity : AppCompatActivity() {

    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)

        pick_btn.setOnClickListener {
            selectionWork()
        }
    }

    private fun selectionWork() {
        startActivityForResult(Intent(Intent.ACTION_PICK), 1234)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            image_name_tv.text = "Image Selected :)"
            displayWork(data)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayWork(data: Intent?) {
        display_btn.setOnClickListener {
            uri = data!!.data!!
            image_display.setImageURI(uri)
            image_name_tv.text = "Image Displayed :)"
            detectionWork()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun detectionWork() {
        detect_btn.setOnClickListener {
            val image: FirebaseVisionImage
            try {
                val btmp = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                val mutableBitmap = btmp.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutableBitmap)
                image = FirebaseVisionImage.fromFilePath(applicationContext, uri)
                val detector = FirebaseVision.getInstance().visionFaceDetector
                detector.detectInImage(image).addOnCompleteListener {
                    for (face in it.result!!) {
                        val bounds = face.boundingBox
                        val p = Paint()
                        p.color = Color.YELLOW
                        p.style = Paint.Style.STROKE
                        canvas.drawRect(bounds, p)
                        image_name_tv.text = "Face Detected :)"
                        Toast.makeText(this, "Face Detected :)", Toast.LENGTH_LONG).show()
                        var y = face.headEulerAngleY
                        var z = face.headEulerAngleZ
                        val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
                        if (leftEar != null) {
                            var leftEarPosition = leftEar.position
                        }
                        val leftEyeContour =
                            face.getContour(FirebaseVisionFaceContour.LEFT_EYE).points
                        val upperLipBottomContour =
                            face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).points
                        if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val smileProb = face.smilingProbability
                        }
                        if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }
                        if (face.trackingId != FirebaseVisionFace.INVALID_ID) {
                            val id = face.trackingId
                        }
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}