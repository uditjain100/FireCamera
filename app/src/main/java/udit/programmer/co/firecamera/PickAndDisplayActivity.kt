package udit.programmer.co.firecamera

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pick_and_display.*
import kotlinx.android.synthetic.main.activity_pick_and_display.image_display
import kotlinx.android.synthetic.main.activity_text_recognizer.*

class PickAndDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_and_display)

        pad_pick_btn.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK), 12345)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 12345) {
            pad_display_btn.setOnClickListener {
                image_display.setImageURI(data!!.data!!)
            }
        }
    }
}