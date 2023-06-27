package com.example.opsc7311_prototypeapp

//the worker class will store the arrays
class Worker {

companion object{
   public val objectList = mutableListOf<TimeSheetEntry>()

   val catList = mutableListOf<Category>()

   val updatedObjects = mutableListOf<Category>()

   var userInfo = ""

   fun isValidPassword(password: String): Boolean {

      if(password.length < 8){
         return false
      }
      var hasLower = false
      var hasUpper = false
      var hasNumber = false
      for (cha in password){
         if (Character.isLowerCase(cha)){
            hasLower = true
         }
         else if (Character.isUpperCase(cha)){
            hasUpper = true
         }
         else if (Character.isDigit(cha)){
            hasNumber = true
         }

      }

      return hasLower && hasUpper && hasNumber
   }
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