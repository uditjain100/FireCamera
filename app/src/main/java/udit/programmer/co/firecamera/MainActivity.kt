package udit.programmer.co.firecamera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bitmap_btn.setOnClickListener {
            startActivity(Intent(this, BitmapActivity::class.java))
        }

        HD_btn.setOnClickListener {
            startActivity(Intent(this, HDActivity::class.java))
        }

        camerax_btn.setOnClickListener {
            startActivity(Intent(this, CameraXActivity::class.java))
        }

        text_btn.setOnClickListener {
            startActivity(Intent(this, TextRecognizerActivity::class.java))
        }

        firebase_btn.setOnClickListener {
            startActivity(Intent(this, FirebaseActivity::class.java))
        }

        image_labelling_btn.setOnClickListener {
            startActivity(Intent(this, ImageLabelActivity::class.java))
        }

    }

}