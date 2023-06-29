package com.example.opsc7311_prototypeapp

//this class will store the entries the user puts into the entries fragment
data class TimeSheetEntry(
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val category: String,
    val hoursWorked: Double,
    val totalPrice: Double
) {

    override fun toString(): String {
       return "Category: " + category + "\r\n" + "Description: " + description
    }


}