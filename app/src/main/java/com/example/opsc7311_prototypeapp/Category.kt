package com.example.opsc7311_prototypeapp

//this class will display the category and the time spent on the app
class Category {
    var categoryName: String = ""
    var timeSpent: Int = 0

    //connstructor to store the items
    constructor(catName: String, tSpent: Int)
    {
        categoryName = catName
        timeSpent = tSpent
    }

    //this will display and aler and the item in a listview
    override fun toString(): String {
        return "Category: " + categoryName + "\r\n" + "Time Spent: " + timeSpent
    }
}