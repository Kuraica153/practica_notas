package kuraica.christopher.misnotas

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import java.io.File
import java.io.FileOutputStream

class AgregarNotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)
    }

    //nn stands for: 'new note'
    //Initialize UI elements
    val nnTitle: EditText = findViewById(R.id.nnTitle)
    val nnContent: EditText = findViewById(R.id.nnContent)
    val nnSave: Button = findViewById(R.id.btnSave)

    nnSave.setOnClickListener {
        saveNote()
    }



    fun saveNote(){
        //Check permissions
        if (checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 235)
        } else {
            save()
        }
    }

    fun save(){
        //Save the note
        val title = nnTitle.text.toString()
        val content = nnContent.text.toString()
        //Check for empty fields
        if (title.isEmpty() || content.isEmpty()){
            Toast.makeText(this, "Error: Empty fields", Toast.LENGTH_SHORT).show()
            return
        }
        //Save the note

        try {
            val file = File(Location(), "${title}.txt")
            val fos = FileOutputStream(file)
            fos.write(content.toByteArray())
            fos.close()
            Toast.makeText(this, "Note saved on public folder", Toast.LENGTH_SHORT).show()
        }catch (e: Exception) {
            Toast.makeText(this, "Error: there was a problem saving the file", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            235 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    save()
                } else {
                    Toast.makeText(this, "Error: Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun Location(): String{
        val folder = File(getExternalFilesDir(null), "MisNotas")
        if (!folder.exists()){
            folder.mkdir()
        }
        return folder.absolutePath
    }
}