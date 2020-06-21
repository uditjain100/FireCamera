package udit.programmer.co.firecamera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_text_recognizer.*
import java.io.File
import java.net.URI

class TextRecognizerActivity : AppCompatActivity() {

    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognizer)

        pick_btn.setOnClickListener {
            selectionWork()
        }
    }

    private fun selectionWork() {
        startActivityForResult(Intent(Intent.ACTION_PICK), 1234)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            displayWork(data)
        }
    }

    private fun displayWork(data: Intent?) {
        display_btn.setOnClickListener {
            uri = data!!.data!!
            image_display.setImageURI(uri)
            textRecognizingWork()
        }
    }


    private fun textRecognizingWork() {
        text_recognizer_btn.setOnClickListener {
            recognizing_work()
        }
    }

    private fun recognizing_work() {
        val image = FirebaseVisionImage.fromFilePath(this, uri)
        val recognizer = FirebaseVision.getInstance().cloudTextRecognizer
        recognizer.processImage(image).addOnSuccessListener {
            process_Image(it)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed $it :(", Toast.LENGTH_LONG).show()
            image_name_tv.text = it.toString()
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