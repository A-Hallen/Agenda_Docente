package com.hallen.agendadocente.horario

import android.content.Context

class HSaredPreferences(context:Context) {
    val SHARED_NAME = "MyDatabase"


    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveCell(cell:String, name:String){
        storage.edit().putString(name, cell).apply()
    }

    fun getCell(name:String): String {
        return storage.getString(name, "")!!
    }
}
