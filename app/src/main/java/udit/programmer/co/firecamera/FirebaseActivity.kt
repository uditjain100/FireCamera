package udit.programmer.co.firecamera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_firebase.*

class FirebaseActivity : AppCompatActivity() {

    lateinit var firebaseStorage: StorageReference
    private lateinit var filePath: StorageReference
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
            retrievingWork()
        }

    }

    private fun selectionWork() {
        startActivityForResult(Intent(Intent.ACTION_PICK), 1234)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            image_name_tv.text = "Image Selected :)"
            uploadingWork(data)
        }
    }

//    : if request.auth != null

    @SuppressLint("SetTextI18n")
    private fun uploadingWork(data: Intent?) {
        upload_btn.setOnClickListener {
            filePath = firebaseStorage.child("Photos").child(data!!.data!!.lastPathSegment!!)
            filePath.putFile(data.data!!).addOnCompleteListener {
                Toast.makeText(this, "Uploaded Successfully :)", Toast.LENGTH_LONG).show()
                image_name_tv.text = "Uploaded Successfully :)"
            }.addOnFailureListener {
                Toast.makeText(this, "Uploading Failed $it :(", Toast.LENGTH_LONG).show()
            }.continueWithTask {
                filePath.downloadUrl
            }.addOnCompleteListener {
                if (it.isComplete) {
                    Toast.makeText(this, "Uri Task Completed :)", Toast.LENGTH_LONG).show()
                    image_name_tv.text = "Uri Task Completed :)"
                    uri = it.result
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Uri Failed $it :(", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun retrievingWork() {
        retrieve_btn.setOnClickListener {
            if (uri != null) {
                try {
                    Picasso.get().load(uri).into(image_display)
                    Toast.makeText(this, "Retrieved Successfully :)", Toast.LENGTH_LONG).show()
                    image_name_tv.text = "Retrieved Successfully :)"
                } catch (e: Exception) {
                    Toast.makeText(this, "Error : $e   :(", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "No Image :(", Toast.LENGTH_LONG).show()
            }
        }
    }

}