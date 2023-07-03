package com.example.opsc7311_prototypeapp

//this class will display the category and the time spent on the app
data class Category (
    val id: String? = null,
    val name: String? = null
        )
{
    var categoryName: String = ""
    var timeSpent: Int = 0

    //constructor to store the items
    constructor(catName: String, tSpent: Int) : this() {
        categoryName = catName
        timeSpent = tSpent
    }

    //this will display and alert and the item in a listview
    override fun toString(): String {
        return "Category: " + categoryName + "\r\n" + "Time Spent: " + timeSpent
    }
}