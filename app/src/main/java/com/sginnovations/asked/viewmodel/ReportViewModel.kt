package com.sginnovations.asked.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.data.report.Report
import com.sginnovations.asked.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _reportStatus = MutableLiveData<Boolean>()
    val reportStatus: LiveData<Boolean> get() = _reportStatus

    fun sendReport(report: Report) {
        repository.sendReport(report) { success ->
            _reportStatus.value = success
        }
    }
}
