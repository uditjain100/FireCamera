package udit.programmer.co.firecamera

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var firebaseStorage: StorageReference
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        firebaseStorage = FirebaseStorage.getInstance().reference
        snap_btn.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1234)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            image_name_tv.text = "Picture Captured"
            bitmap = data!!.extras!!.get("data") as Bitmap
            image_display.setImageBitmap(bitmap)

            uploadingfunction(data)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uploadingfunction(data: Intent?) {
        upload_btn.setOnClickListener {
            image_name_tv.text = "Uploading Started"
            val uri = data!!.data
            val filePath = firebaseStorage.child("Photos").child(uri!!.lastPathSegment!!)
            filePath.putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Uploading Finished", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Uploading Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}