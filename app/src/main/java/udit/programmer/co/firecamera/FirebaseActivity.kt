package udit.programmer.co.firecamera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.UrlQuerySanitizer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_firebase.*
import java.net.URI

class FirebaseActivity : AppCompatActivity() {

    lateinit var firebaseStorage: StorageReference
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)

        firebaseStorage = FirebaseStorage.getInstance().reference

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE),
                2222
            )
        } else {
            pick_btn.setOnClickListener {
                selectionWork()
            }
            if (uri != null)
                retrievingWork(uri)
        }

    }

    private fun selectionWork() {
        startActivityForResult(Intent(Intent.ACTION_PICK), 1234)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            image_name_tv.text = "Image Selected :)"
            uploadingWork(data)
        }
    }

//    : if request.auth != null

    private fun uploadingWork(data: Intent?) {
        upload_btn.setOnClickListener {
            val filePath = firebaseStorage.child("Photos").child(data!!.data!!.lastPathSegment!!)
            filePath.putFile(data.data!!).addOnSuccessListener {
                Toast.makeText(this, "Uploaded Successfully :)", Toast.LENGTH_LONG).show()
                image_name_tv.text = "Uploaded Successfully :)"
                uri = it.storage.downloadUrl.result!!
            }.addOnFailureListener {
                Toast.makeText(this, "Failed :(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun retrievingWork(uri: Uri?) {
        retrieve_btn.setOnClickListener {
            image_display.setImageURI(uri)
            image_name_tv.text = "Retrieved Successfully :)"
        }
    }

}