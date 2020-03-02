package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.PatientAdapter
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patients_info.*


class PatientsInfoActivity : AppCompatActivity(), PatientAdapter.OnItemClickListener {
    override fun onItemClick(patient: Patient) {
        val intent = Intent(this@PatientsInfoActivity, SpecificPatientActivity::class.java)
        Log.d("PatientsInfoActivity", "uuid = " + patient.uuid)
        intent.putExtra("uuid", patient.uuid)
        intent.putExtra("title", patient.name.title)
        intent.putExtra("first_name", patient.name.firstName)
        intent.putExtra("last_name", patient.name.name)
        intent.putExtra("age", patient.age.toString())
        intent.putExtra("disease", patient.disease)
        startActivity(intent)
    }

    val patients: ArrayList<Patient> = arrayListOf()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_info)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Patients" )

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val patient = childSnapshot.getValue(Patient::class.java)!!
                    patients.add(patient)

                    patientsRecycler.layoutManager = LinearLayoutManager(this@PatientsInfoActivity)
                    patientsRecycler.adapter = PatientAdapter(patients,this@PatientsInfoActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@PatientsInfoActivity, "Can't read informations from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(postListener)

        addPatientButtonClick()
    }

    fun addPatientButtonClick() {
        addPatientButton.setOnClickListener {
            val intent = Intent(this@PatientsInfoActivity, AddPatientActivity::class.java)
            intent.putExtra("uuid", "")
            intent.putExtra("title", "")
            intent.putExtra("first_name", "")
            intent.putExtra("last_name","")
            intent.putExtra("age", "0")
            intent.putExtra("disease", "")
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this@PatientsInfoActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}
