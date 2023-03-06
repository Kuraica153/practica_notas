package kuraica.christopher.misnotas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    var notes = ArrayList<Nota>()
    //Initialize adapter
    var adapter = NoteAdapter(this, notes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fillNotes()

        //Initialize UI elements
        val listView: ListView = findViewById(R.id.listView)
        val addButton: TextView = findViewById(R.id.btnAdd)

        listView.adapter = adapter

        //Set event listeners
        addButton.setOnClickListener {
            //Navigate to the add note activity
            val intent = Intent(this, AgregarNotaActivity::class.java)
            startActivity(intent)
        }
    }

    fun fillNotes(){
        notes.clear()
        var folder = File(Location())

        if(folder.exists()){
            var files = folder.listFiles()
            if(files != null){
                for(file in files){
                    ReadFile(file)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123){
            fillNotes()
            adapter.notifyDataSetChanged()
        }
    }

    fun ReadFile(file: File){
        var title = file.nameWithoutExtension
        var content = file.readText()
        var note = Nota(title, content)
        notes.add(note)
    }

    private fun Location(): String{
        val folder = File(getExternalFilesDir(null), "MisNotas")
        if (!folder.exists()){
            folder.mkdir()
        }
        return folder.absolutePath
    }

    class NoteAdapter: BaseAdapter{
        var context: Context? = null
        var notes = ArrayList<Nota>()

        constructor(context: Context, notes: ArrayList<Nota>): super(){
            this.context = context
            this.notes = notes
        }

        override fun getCount(): Int {
            return notes.size
        }

        override fun getItem(position: Int): Any {
            return notes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val note = this.notes[position]

            var inflator = LayoutInflater.from(context)
            var noteView = inflator.inflate(R.layout.nota_layout, null)

            noteView.findViewById<TextView>(R.id.noteTitle).text = note.title
            noteView.findViewById<TextView>(R.id.noteContent).text = note.content
            noteView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
                eliminar(note.title)
                notes.remove(note)
                this.notifyDataSetChanged()
            }

            return noteView
        }

    }

    fun eliminar(title: String){

        //Check if title is empty
        if(title == ""){
            Toast.makeText(this, "Error: Empty title", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val file = File(Location(), "${title}.txt")
            file.delete()
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
        }catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}