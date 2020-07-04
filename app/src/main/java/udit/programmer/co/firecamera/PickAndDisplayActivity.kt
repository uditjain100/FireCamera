package udit.programmer.co.firecamera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_firebase.*
import kotlinx.android.synthetic.main.activity_pick_and_display.*
import kotlinx.android.synthetic.main.activity_pick_and_display.image_display
import kotlinx.android.synthetic.main.activity_text_recognizer.*

class PickAndDisplayActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    lateinit var firebaseStorage: StorageReference
    private lateinit var filePath: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_and_display)
        firebaseStorage = FirebaseStorage.getInstance().reference
        pad_pick_btn.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            imageUri = CropImage.getActivityResult(data).uri
            Picasso.get().load(imageUri).into(image_display)

            filePath = firebaseStorage.child("Photos")
            filePath.putFile(CropImage.getActivityResult(data).uri).addOnCompleteListener {
                Toast.makeText(this, "Uploaded Successfully :)", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Uploading Failed $it :(", Toast.LENGTH_LONG).show()
            }.continueWithTask {
                filePath.downloadUrl
            }.addOnCompleteListener {
                if (it.isComplete) {
                    Toast.makeText(this, "Uri Task Completed :)", Toast.LENGTH_LONG).show()
                    imageUri = it.result
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Uri Failed $it :(", Toast.LENGTH_LONG).show()
            }
        }
    }
}