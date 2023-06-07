package com.example.opsc7311_prototypeapp

class Worker {

companion object{
   public val objectList = mutableListOf<TimeSheetEntry>()

   val stringList = mutableListOf<String>()

   val catList = mutableListOf<Category>()

   val updatedObjects = mutableListOf<Category>()

   fun SortObjects(){
      for (Category in catList) {
         val existingObj = updatedObjects.find { it.categoryName == Category.categoryName }
         if (existingObj != null) {
            existingObj.timeSpent += Category.timeSpent
         } else {
            updatedObjects.add(Category)
         }
      }
   }



}
}