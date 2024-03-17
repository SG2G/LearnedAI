package com.sginnovations.asked.presentation.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor(

) : ViewModel() {

    val btnEnable = mutableStateOf(true)
    val currentPage = mutableIntStateOf(0)
    val quoteResponse = mutableStateOf(true)

    val childName = mutableStateOf("")

}