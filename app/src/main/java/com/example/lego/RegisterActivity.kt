package com.example.lego

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.fragment.app.DialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var confirmpass: EditText
    lateinit var nombre: EditText
    lateinit var apellido: EditText
    lateinit var genero: RadioGroup
    lateinit var tipoUs: Spinner
    lateinit var edad: EditText
    lateinit var reallogin_btn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val dbFire = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        username = findViewById(R.id.usernameR)
        password = findViewById(R.id.passwordR)
        edad = findViewById(R.id.fechaNaci)
        edad.setOnClickListener(this)
        reallogin_btn = findViewById(R.id.reallogin_btn)
        tipoUs = findViewById(R.id.tipoUsuario)
        nombre = findViewById(R.id.nombre)
        apellido = findViewById(R.id.apellido)
        confirmpass = findViewById(R.id.ConfirmPasswordR)
        genero = findViewById(R.id.radioGenero)
        database=Firebase.database

        setup()
    }

    private fun setup() {
        title = "Register"
        reallogin_btn.setOnClickListener {
            if(username.text.isNotEmpty() && password.text.isNotEmpty() && edad.text.isNotEmpty() && nombre.text.isNotEmpty() && apellido.text.isNotEmpty() && confirmpass.text.isNotEmpty() && genero.isNotEmpty() && confirmpass.text.toString().equals(password.text.toString())) {
                val usernameIn = username.text.toString()
                val passwordIn = password.text.toString()
                val Nombre = nombre.text.toString()
                val Apellido = apellido.text.toString()
                val cumple = edad.text.toString()
                val TipoUs = tipoUs.id

                var Genero = ""

                genero.setOnCheckedChangeListener{group, checkedId ->
                    val botonselec = findViewById<RadioButton>(checkedId)
                    Genero = botonselec.text.toString()
                }


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(usernameIn, passwordIn).addOnCompleteListener{
                    if(it.isSuccessful){
                        //showHome(it.result?.user?.email ?: "Nobrother")
                        val idUsuario= FirebaseAuth.getInstance().currentUser?.uid

                        val RefDB = FirebaseDatabase.getInstance().getReference("usuarios")
                        val ColeccionDatos: MutableMap<String, Any> = HashMap()
                        ColeccionDatos.put("nombre",Nombre)
                        ColeccionDatos.put("apellido",Apellido)
                        ColeccionDatos.put("correo", usernameIn)
                        ColeccionDatos.put("genero",Genero)
                        ColeccionDatos.put("fecha nacimiento", cumple)
                        ColeccionDatos.put("uid",idUsuario.toString())
                        ColeccionDatos.put("tipoUs",tipoUs.selectedItemId.toInt())
                        val dbRef: CollectionReference = dbFire.collection("detUsuarios")
                        dbRef.add(ColeccionDatos)



                        if (idUsuario != null) {
                            RefDB.child(idUsuario).setValue(tipoUs.selectedItemId.toString())

                        }
                        var tipo: Int
                        tipo = tipoUs.selectedItemId.toInt()
                        if (idUsuario != null) {
                            showHome(tipo,idUsuario)
                        }

                    } else {
                        showAlertCreate()
                    }
                }
                Log.i("Credenciales revisadas", "Username : $usernameIn and Password: $passwordIn")
            }
        }
    }

    override fun onClick(view: View?){
        val dialogdate = DatePickerFragment{year, month, day -> resultadosFecha(year,month,day) }
        dialogdate.show(supportFragmentManager,"DatePicker")
    }

    private fun resultadosFecha(year: Int,month: Int,day: Int){
        edad?.setText("$year/$month/$day")
    }

    private fun showAlertCreate() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se produjo un error al crear la cuenta")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showHome(TipoUsuario:Int,uidd:String){
        //val homeIntent = Intent(this, HomeActivity::class.java).apply {
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

    }

class DatePickerFragment (val listener: (year:Int, month:Int, day:Int) -> Unit) :DialogFragment(), DatePickerDialog.OnDateSetListener{

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(requireActivity(), this, year,month,day)
        }
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
            listener(year, month, day)
        }


    }
