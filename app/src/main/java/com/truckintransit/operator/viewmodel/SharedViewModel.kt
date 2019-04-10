package com.truckintransit.operator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var inputNumber = MutableLiveData<String>()
    var isMenuEdit = MutableLiveData<String>()
}