package com.example.lego

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class AuthActivity : AppCompatActivity() {
    lateinit var username:EditText
    lateinit var password:EditText
    lateinit var login_btn:Button
    lateinit var reallogin_btn:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        auth = Firebase.auth
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login_btn = findViewById(R.id.login_btn)
        reallogin_btn = findViewById(R.id.reallogin_btn)
        database=Firebase.database
        setup()
    }
   private fun setup() {
       title  = "Authentication"
       reallogin_btn.setOnClickListener {
           RegisterUs()
       }
       login_btn.setOnClickListener {
           if(username.text.isNotEmpty() && password.text.isNotEmpty()) {
               val usernameIn = username.text.toString()
               val passwordIn = password.text.toString()
               FirebaseAuth.getInstance().signInWithEmailAndPassword(usernameIn, passwordIn).addOnCompleteListener{
                   if(it.isSuccessful){
                           var uid=FirebaseAuth.getInstance().currentUser?.uid
                            val db = FirebaseDatabase.getInstance().getReference("usuarios")
                           if (uid != null) {
                               db.child(uid).addListenerForSingleValueEvent(object :
                                   ValueEventListener {
                                   override fun onDataChange(snapshot: DataSnapshot) {
                                       val tipoUsSnapshot = snapshot.getValue(String::class.java)
                                       val tipoUs = tipoUsSnapshot?.toIntOrNull() ?: 0
                                       ?: 3 // Valor por defecto si no se obtiene ningún valor de la base de datos
                                       showHome(tipoUs,uid)
                                   }

                                   override fun onCancelled(error: DatabaseError) {

                                   }
                               })
                           }

                   } else {
                       showAlert()
                   }
               }
               Log.i("Credenciales revisadas", "Username : $usernameIn and Password: $passwordIn")
           }
       }
   }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se produjo un error de autenticar / contraseña o correo invalidos")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showHome(TipoUsuario:Int,uidd:String){
       // val homeIntent = Intent(this, HomeActivity::class.java).apply {
            //putExtra("usuario",usuario)
        //}
        if(TipoUsuario == 0){
            uid.getInstance().setUid(uidd)
            val homeIntent = Intent(this, HomeDCActivity::class.java)
            startActivity(homeIntent)
        }else
            if(TipoUsuario == 1){
                uid.getInstance().setUid(uidd)
                val homeIntent = Intent(this, DVehiculo::class.java)
                startActivity(homeIntent)
            }else
        if(TipoUsuario == 2){
            uid.getInstance().setUid(uidd)
            val homeIntent = Intent(this, homeConductor::class.java)
            startActivity(homeIntent)
        }

    }

    private fun RegisterUs(){
        // val homeIntent = Intent(this, HomeActivity::class.java).apply {
        //putExtra("usuario",usuario)
        //}

        val RegisterIntent = Intent(this, RegisterActivity::class.java)
        startActivity(RegisterIntent)
    }

}

