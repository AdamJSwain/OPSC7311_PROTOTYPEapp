package com.example.opsc7311_prototypeapp

import android.graphics.Bitmap
import java.util.Date

//this class will store the entries the user puts into the entries fragment
class TimeSheetEntry {

    var category: String = ""

    var description: String =""

    var startDate: Date

    var startTime:  Date

    var endTime: Date

    var image: Bitmap
        get() {
            TODO()
        }
        set(value) {}
    constructor(
        category: String,
        description: String,
        startDate: Date,
        startTime: Date,
        endTime: Date,
        //image: Bitmap?
    )
    {
        this.category = category.toString()
        this.description = description
        this.startDate = startDate
        this.startTime = startTime
        this.endTime = endTime
    }
    override fun toString(): String {
       return "Category: " + category + "\r\n" + "Description: " + description
    }


}