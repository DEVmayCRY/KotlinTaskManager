package com.komat.randomtodo.model
//Task
data class Task(
    val title: String,
    val hour: String,
    val date: String,
    val description: String,
    val id: Int = 0,
    var type: String,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
