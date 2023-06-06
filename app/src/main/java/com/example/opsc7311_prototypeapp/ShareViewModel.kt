package com.example.opsc7311_prototypeapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel: ViewModel() {
    val selectedItem = MutableLiveData<String>()
    val dataList = mutableListOf<String>()
}