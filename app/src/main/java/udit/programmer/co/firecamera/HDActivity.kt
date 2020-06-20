package udit.programmer.co.firecamera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_bitmap.*
import kotlinx.android.synthetic.main.activity_h_d.*
import kotlinx.android.synthetic.main.activity_h_d.display_btn
import kotlinx.android.synthetic.main.activity_h_d.image_display
import kotlinx.android.synthetic.main.activity_h_d.image_name_tv
import kotlinx.android.synthetic.main.activity_h_d.snap_btn
import kotlinx.android.synthetic.main.activity_h_d.text_recognizer_btn
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HDActivity : AppCompatActivity() {

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h_d)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                2222
            )
        }

        snap_btn.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 2222) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Grant Premissions", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK)
            display_work()
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("SetTextI18n")
    private fun display_work() {
        image_name_tv.text = "Image Captured"
        display_btn.setOnClickListener {
            image_display.setImageURI(Uri.fromFile(File(currentPhotoPath)))
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
        val image = FirebaseVisionImage.fromFilePath(this, Uri.fromFile(File(currentPhotoPath)))
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

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_${format}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        currentPhotoPath = image.absolutePath
        return image
    }

    private fun dispatchTakePictureIntent() {
        val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (photoIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                Log.d("Ceased Meteor", "ERROR : $e")
            }
            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.android.fileprovider",
                    photoFile
                )
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(photoIntent, 1234)
            }
        }
    }

}