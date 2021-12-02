package com.hallen.agendadocente.horario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.hallen.agendadocente.R
import com.hallen.agendadocente.User

class HoraryActivity : AppCompatActivity() {
    private lateinit var rvHorary:GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horary)

        //Inicializamos el GridView
        rvHorary = findViewById(R.id.gb_horary_activity)
        //Le asignamos un adaptador
        asignAdapter()
    }

    private fun asignAdapter() {
        //Instanciamos el arrayList de Users
        val userList = ArrayList<User>()

        //Llamamos a la funcion getUserByindex que nos retorna los headers de la tabla en un bucle y los metemos en el arraylist de users
        for(i in 0 until 8){
            val user:User = getUserByIndex(i)
            userList.add(user)

        }

        val prefs = HSaredPreferences(applicationContext)
        var a = 7
        for(i in 1 until 14){
            for(e in 0 until 8){
                a++
                val user = User()
                user.col = e
                user.row = i
                user.id = a
                user.name = prefs.getCell("$i$e")
                userList.add(user)
            }
        }

        val adapter = HoraryActivityAdapter(this, userList)
        rvHorary.adapter = adapter
    }

    private fun getUserByIndex(i: Int): User {
        val user = User()
        when(i){
            0 ->{
                user.name = "T"
                user.id = 0
                user.row = 0
                user.col = 0
            }
            1 ->{
                user.name = "L"
                user.id = 1
                user.row = 0
                user.col = 1
            }
            2 ->{
                user.name = "M"
                user.id = 2
                user.row = 0
                user.col = 2
            }
            3 ->{
                user.name = "X"
                user.id = 3
                user.row = 0
                user.col = 3
            }
            4 ->{
                user.name = "J"
                user.id = 4
                user.row = 0
                user.col = 4
            }
            5 ->{
                user.name = "V"
                user.id = 5
                user.row = 0
                user.col = 5
            }
            6 ->{
                user.name = "_"
                user.id = 6
                user.row = 0
                user.col = 6
            }
            7 ->{
                user.name = "_"
                user.id = 7
                user.row = 0
                user.col = 7
            }
        }
        return user
    }
}