package com.example.opsc7311_prototypeapp

import android.graphics.Bitmap
import java.util.Date

class TimeSheetEntry {

    var category: String = ""

    var description: String =""

    var startDate = Date()

    var startTime: String = ""

    var endTime: String = ""

    var image: Bitmap
        get() {
            TODO()
        }
        set(value) {}


}