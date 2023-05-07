package com.example.jobquest.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.adapters.SentApplicationsAdapter
import com.example.jobquest.models.NewApplicationsModel
import com.google.firebase.database.*

class SentApplicationsFragment : Fragment(R.layout.fragment_sent_job_applications_rv) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SentApplicationsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    // New Lines
    private lateinit var applications: ArrayList<NewApplicationsModel>
    private lateinit var dbReference: DatabaseReference
    //

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sent_job_applications_rv, container, false)
        recyclerView = view.findViewById(R.id.sentApplicationsRecyclerView)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // New Code for Firebase
        applications = arrayListOf<NewApplicationsModel>()
        getJobApplicationsData()
        //


/*
        val applications = listOf(
            ApplicationSent("Gardener Vacancy", "Salary", "Employer Name", "IN REVIEW"),
            ApplicationSent("Painting Job", "Work Based Wage", "Employer Name","ACCEPTED"),
            ApplicationSent("Cleaning Vacancy", "Salary", "Employer Name","IN REVIEW"),
            ApplicationSent("Driver Job", "Salary", "Employer Name", "IN REVIEW"),
            ApplicationSent("Carpentry", "Work Based Wage", "Employer Name","ACCEPTED")
        )
        adapter = SentApplicationsAdapter(applications)*/
        layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.sentApplicationsRecyclerView)
        //recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

    }

    // New Code for Firebase
    private val fragmentContext = this@SentApplicationsFragment

    private fun getJobApplicationsData(){
        dbReference = FirebaseDatabase.getInstance().getReference("JobApplications")
        dbReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (jobApps in snapshot.children){
                        val applicationData = jobApps.getValue(NewApplicationsModel::class.java)
                        applications.add(applicationData!!)
                    }
                    /* Replaced with the below adapter because SentApplicationsAdapter's parameters were changed for deleteButton method.
                    val newAdapter = SentApplicationsAdapter(applications)*/
                    val newAdapter = SentApplicationsAdapter(applications, fragmentContext)
                    recyclerView.adapter = newAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // Used when deleting a Job Application
     fun refreshSentApplicationsUI(){
        val nextFragment = SentApplicationsFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    //


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class ApplicationSent(val title: String, val budget: String, val posterName:String, val status: String) {
        companion object {
            const val STATUS_IN_REVIEW = 0
            const val STATUS_ACCEPTED = 1
        }
    }

}