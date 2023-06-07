package com.example.opsc7311_prototypeapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//This class will store the data and share it to the necessary fragments
class ShareViewModel: ViewModel() {
    val selectedItem = MutableLiveData<String>()

}