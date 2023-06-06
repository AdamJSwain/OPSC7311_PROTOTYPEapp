package com.example.opsc7311_prototypeapp

class Worker {


   public val objectList = ArrayList<TimeSheetEntry>()

   public val TimeSheetEntry: MutableList<TimeSheetEntry> = mutableListOf()

   fun addTimesheetEntry(entry: TimeSheetEntry) {
      TimeSheetEntry.add(entry)
   }

   fun getTimesheetEntries(): List<TimeSheetEntry> {
      return TimeSheetEntry
   }

}