package com.example.jobquest.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jobquest.R
import com.example.jobquest.models.NewApplicationsModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JobApplicationFragment : Fragment(R.layout.fragment_job_application) {
    private lateinit var paymentTypeSpinner: Spinner
    private lateinit var salaryLayout: LinearLayout
    private lateinit var workBasedWageLayout: LinearLayout
    private lateinit var salaryEditText: EditText
    private lateinit var chargingRateEditText: EditText

    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_application, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentTypeSpinner = view.findViewById(R.id.payment_type_spinner)
        salaryLayout = view.findViewById(R.id.salary_layout)
        workBasedWageLayout = view.findViewById(R.id.work_based_wage_layout)
        salaryEditText = view.findViewById(R.id.edit_text_salary)
        chargingRateEditText = view.findViewById(R.id.edit_text_charging_rate)

        dbReference = FirebaseDatabase.getInstance().getReference("JobApplications")

        // Populate the payment type spinner with the payment types defined in strings.xml
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.payment_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            paymentTypeSpinner.adapter = adapter
        }

        // Set an on item selected listener for the payment type spinner
        paymentTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Show or hide the salary layout and the work based wage layout based on the selected payment type
                val selectedPaymentType = parent?.getItemAtPosition(position).toString()
                if (selectedPaymentType == getString(R.string.salary)) {
                    salaryLayout.visibility = View.VISIBLE
                    workBasedWageLayout.visibility = View.GONE
                } else if (selectedPaymentType == getString(R.string.work_based_wage)) {
                    salaryLayout.visibility = View.GONE
                    workBasedWageLayout.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set a click listener for the submit button
        view.findViewById<View>(R.id.submit_button).setOnClickListener {

            fun saveApplicationData(){
                // Get the values entered by the user
                val name = view.findViewById<EditText>(R.id.edit_text_name).text.toString()
                val age = view.findViewById<EditText>(R.id.edit_text_age).text.toString()
                val city = view.findViewById<EditText>(R.id.edit_text_city).text.toString()

                val paymentType = paymentTypeSpinner.selectedItem.toString()
                val expectedSalary = salaryEditText.text.toString()
                val chargingRate = chargingRateEditText.text.toString()



                if(name.isEmpty()){
                    view.findViewById<EditText>(R.id.edit_text_name).error = "Please enter your Name. "
                    return
                }
                if(age.isEmpty()){
                    view.findViewById<EditText>(R.id.edit_text_age).error = "Please enter your Age. "
                    return
                }
                if(city.isEmpty()){
                    view.findViewById<EditText>(R.id.edit_text_city).error = "Please enter your City. "
                    return
                }
                /*if(expectedSalary.isEmpty() && chargingRate.isEmpty()){
                    view.findViewById<EditText>(androidx.constraintlayout.widget.R.id.currentState).error = "Please enter your Salary or Charging Rate. "
                    return
                }*/
                if(view.findViewById<EditText>(R.id.edit_text_salary).text.toString().isEmpty() && view.findViewById<EditText>(R.id.edit_text_charging_rate).text.toString().isEmpty()){
                    if (salaryEditText.parent is TextInputLayout) {
                        (salaryEditText.parent as TextInputLayout).error = "Please enter your Salary. "
                    } else {
                        chargingRateEditText.error = "Please enter your Charging Rate. "
                    }
                    return
                }

                val applicationID = dbReference.push().key!!


                val newApplication = NewApplicationsModel(applicationID,"",name,age,city,"New Job","","Employer Name",paymentType,expectedSalary,chargingRate)
                dbReference.child(applicationID).setValue(newApplication)
                    .addOnCompleteListener {
                        Toast.makeText(requireContext(), "Application is Submitted Successfully.", Toast.LENGTH_SHORT).show()

                        view.findViewById<EditText>(R.id.edit_text_name).text.clear()
                        view.findViewById<EditText>(R.id.edit_text_age).text.clear()
                        view.findViewById<EditText>(R.id.edit_text_city).text.clear()
                        salaryEditText.text.clear()
                        chargingRateEditText.text.clear()

                        // Check that input values are not empty before redirecting
                        if (name.isNotEmpty() && age.isNotEmpty() && city.isNotEmpty() && paymentType.isNotEmpty() && (expectedSalary.isNotEmpty() || chargingRate.isNotEmpty())) {
                            val nextFragment = SentApplicationsFragment()
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.mainContainer, nextFragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }

                    }.addOnFailureListener{err->
                        Toast.makeText(requireContext(),"Error ${err.message}", Toast.LENGTH_SHORT).show()
                    }
            }


            saveApplicationData()

            // Only works for Activities...
           /* val intent = Intent(requireContext(), SentApplicationsFragment::class.java)
            startActivity(intent)*/


            // Worked. Later added to the saveApplicationData() function
            /*val nextFragment = SentApplicationsFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()*/


            // TODO: Validate the input values

            // TODO: Submit the job application
        }



    }

}