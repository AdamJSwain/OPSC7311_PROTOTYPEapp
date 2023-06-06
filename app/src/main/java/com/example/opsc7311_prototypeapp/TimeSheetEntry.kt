package com.example.opsc7311_prototypeapp

import android.graphics.Bitmap
import android.widget.Spinner
import java.util.Date

class TimeSheetEntry {

    var category: String = ""

    var description: String =""

    var startDate: String = ""

    var startTime: String = ""

    var endTime: String = ""

    var image: Bitmap
        get() {
            TODO()
        }
        set(value) {}
    constructor(
        category: Spinner,
        description: String,
        startDate: Date,
        startTime: Date,
        endTime: Date,
        image: Bitmap?
    )
    {
        this.category = category.toString()
        this.description = description
        this.startDate = startDate.toString()
        this.startTime = startTime.toString()
        this.endTime = endTime.toString()
    }
    override fun toString(): String {
       return "Category: " + category + "\r\n" + "Description: " + description
    }


}