package udit.programmer.co.firecamera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_pick_and_display.*
import kotlinx.android.synthetic.main.activity_pick_and_display.image_display
import kotlinx.android.synthetic.main.activity_text_recognizer.*

class PickAndDisplayActivity : AppCompatActivity() {

    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_and_display)

//        pad_pick_btn.setOnClickListener {
//            startActivityForResult(Intent(Intent.ACTION_PICK), 12345)
//        }
        pad_pick_btn.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 12345) {
            pad_display_btn.setOnClickListener {
                image_display.setImageURI(data!!.data!!)
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            imageUri = result.uri
            Picasso.get().load(imageUri).into(image_display)
        }
    }
}