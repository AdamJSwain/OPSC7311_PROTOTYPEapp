package com.example.opsc7311_prototypeapp

class Worker {


   public val objectList = ArrayList<TimeSheetEntry>()

   private val timesheetEntries: MutableList<TimeSheetEntry> = mutableListOf()

   fun addTimesheetEntry(entry: TimeSheetEntry) {
      timesheetEntries.add(entry)
   }

   fun getTimesheetEntries(): List<TimeSheetEntry> {
      return timesheetEntries
   }


}