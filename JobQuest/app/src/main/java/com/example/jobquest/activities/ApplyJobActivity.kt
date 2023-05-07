package com.example.jobquest.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.jobquest.R

class ApplyJobActivity : AppCompatActivity() {


}

class Application(val name: String, val jobName: String, val paymentType: String, val estCost:Int, val ag: Int, val ct: String, val status: Int) {
    companion object {
        const val STATUS_IN_REVIEW = 0
        const val STATUS_ACCEPTED = 1
    }
}