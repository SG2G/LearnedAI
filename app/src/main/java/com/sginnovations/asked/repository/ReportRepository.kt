package com.sginnovations.asked.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.data.report.Report
import javax.inject.Inject

class ReportRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    fun sendReport(report: Report, onComplete: (Boolean) -> Unit) {
        db.collection("reports")
            .add(report)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}
