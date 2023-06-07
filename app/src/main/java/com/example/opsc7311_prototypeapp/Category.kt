package com.example.opsc7311_prototypeapp

class Category {
    var categoryName: String = ""
    var timeSpent: Int = 0

    constructor(catName: String, tSpent: Int)
    {
        categoryName = catName
        timeSpent = tSpent
    }

    override fun toString(): String {
        return "Category: " + categoryName + "\r\n" + "Time Spent: " + timeSpent
    }
}