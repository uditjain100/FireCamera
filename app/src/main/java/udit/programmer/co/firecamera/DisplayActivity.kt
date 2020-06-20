package udit.programmer.co.firecamera

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_display.*
import java.io.File
import java.lang.Exception

class DisplayActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val cpu = intent.getStringExtra("Ceased Meteor")
        image_name_tv.text = "Image Captured :)"

        display_btn.setOnClickListener {
            try {
                image_display.setImageURI(Uri.fromFile(File(cpu!!)))
                image_name_tv.text = "Image Displayed :)"
            } catch (e: Exception) {
                Log.d("Ceased Meteor", " ERROR : $e")
            }
        }

    }


}