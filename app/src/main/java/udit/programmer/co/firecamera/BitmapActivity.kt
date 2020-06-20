package udit.programmer.co.firecamera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import kotlinx.android.synthetic.main.activity_bitmap.*

class BitmapActivity : AppCompatActivity() {

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.INTERNET),
                2222
            )
        }

        snap_btn.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1234)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            image_name_tv.text = "Image Captured"
            display_work(data)
        }
    }

    private fun display_work(data: Intent?) {
        display_btn.setOnClickListener {
            bitmap = data!!.extras!!.get("data") as Bitmap
            image_display.setImageBitmap(bitmap)
            image_name_tv.text = "Image Displayed"
            textRecognizingWork()
        }
    }

    private fun textRecognizingWork() {
        text_recognizer_btn.setOnClickListener {
            recognizing_work()
        }
    }

    private fun recognizing_work() {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val recognizer = FirebaseVision.getInstance().cloudTextRecognizer
        recognizer.processImage(image).addOnSuccessListener {
            process_Image(it)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun process_Image(it: FirebaseVisionText?) {
        val blocks = it!!.textBlocks
        if (blocks.size == 0) {
            Toast.makeText(this, "No Text :(", Toast.LENGTH_LONG).show()
            return
        }
        for (block in blocks)
            image_name_tv.text = block.text
    }


}