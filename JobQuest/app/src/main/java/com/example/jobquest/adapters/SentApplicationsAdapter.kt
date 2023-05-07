package com.example.jobquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.fragments.SentApplicationsFragment
import com.example.jobquest.models.NewApplicationsModel
import com.google.firebase.database.FirebaseDatabase

class SentApplicationsAdapter(private val applications: ArrayList<NewApplicationsModel>, private val sentAppFragment: SentApplicationsFragment) :
/* This one Works. But above new one used because of deleteButton redirect to fragment codings.
class SentApplicationsAdapter(private val applications: ArrayList<NewApplicationsModel>) :*/
/* This one Works. But above new one used because of Firebase codings.
class SentApplicationsAdapter(private val applications: List<SentApplicationsFragment.ApplicationSent>) :*/
//class SentApplicationsAdapter(private val applications: List<SentApplicationsActivity.ApplicationSent>) :
    RecyclerView.Adapter<SentApplicationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sent_application_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applications[position]
        holder.bind(application)
    }

    override fun getItemCount() = applications.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.sentApplicationItemJobTitle)
        private val budgetTextView: TextView = itemView.findViewById(R.id.sentApplicationItemJobBudget)
        private val posterTextView: TextView = itemView.findViewById(R.id.sentApplicationItemJobPoster)
        private val statusTextView: TextView = itemView.findViewById(R.id.sentApplicationItemStatus)
        private val cancelButton: Button = itemView.findViewById(R.id.sentApplicationItemCancelButton)

        fun bind(application: NewApplicationsModel) {
            titleTextView.text = application.jobTitle
            budgetTextView.text = application.jobBudget
            posterTextView.text = application.employer
            statusTextView.text = application.status

            cancelButton.setOnClickListener {
                // TODO: Handle cancel button click
                deleteJobApplication(application.applicationId)
                //This cast can be never succeed:- (itemView.context as SentApplicationsFragment).refreshSentApplicationsUI()
                sentAppFragment.refreshSentApplicationsUI()
            }
        }
        /* Worked. Above new one added for a Firebase situation.
fun bind(application: SentApplicationsFragment.ApplicationSent) {
        //fun bind(application: SentApplicationsActivity.ApplicationSent) {
            titleTextView.text = application.title
            budgetTextView.text = application.budget
            posterTextView.text = application.posterName
            statusTextView.text = application.status

            cancelButton.setOnClickListener {
                // TODO: Handle cancel button click
            }
        }*/


        // New Code for Firebase + Delete
        private fun deleteJobApplication(applicationId:String?){
            val dbReference = FirebaseDatabase.getInstance().getReference("JobApplications").child(applicationId!!)
            val dTask = dbReference.removeValue()

            dTask.addOnSuccessListener {
                Toast.makeText(itemView.context, "Application Deleted!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{error->
                Toast.makeText(itemView.context, "Deleting error ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
        //

    }



}