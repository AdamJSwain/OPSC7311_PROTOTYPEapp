package com.example.opsc7311_prototypeapp

import android.graphics.Bitmap
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
    constructor(category: String, description: String, startDate: String, startTime: String, endTime: String)
    {
        this.category = category
        this.description = description
        this.startDate = startDate
        this.startTime = startTime
        this.endTime = endTime
    }
    override fun toString(): String {
       return "Category: " + category + "\r\n" + "Description: " + description
    }


}