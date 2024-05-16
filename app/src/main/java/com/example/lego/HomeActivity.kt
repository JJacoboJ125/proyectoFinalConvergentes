package com.example.lego

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity(){
    // lateinit var usernameIN: EditText
    // lateinit var cerrar_btn:Button
    private lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {

        //usernameIN = findViewById(R.id.usernameIN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //val bundle = intent.extras
        //val username = bundle?.getString("username")
        //setup(username?: "Yipi")

        floatingActionButton = findViewById(R.id.floatingActionButton)

        floatingActionButton.setOnClickListener {
            val FormIntent = Intent(this, FormularioMain::class.java)
            startActivity(FormIntent)
        }



    }

  //  private fun setup(username:String){
    //      title = "Inicio"
      //    usernameIN.setText(username)
//
  //        cerrar_btn.setOnClickListener {
    //          FirebaseAuth.getInstance().signOut()
      //        onBackPressed()
        //  }
      //}

}
