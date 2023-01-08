package com.probo.proboassignment1.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import com.probo.proboassignment1.R
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private var etSelectedDate: AppCompatEditText? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val dobButton = findViewById<AppCompatImageButton>(R.id.dob_picker)
        etSelectedDate = findViewById(R.id.et_dob)
        dobButton.setOnClickListener {
            pickDateFromDatePickerDialog()
        }

        val btnSignUp = findViewById<AppCompatButton>(R.id.btn_sign_up)
        btnSignUp.setOnClickListener {
            validateInput()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pickDateFromDatePickerDialog() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth+1}-${selectedYear}"
                etSelectedDate?.setText(selectedDate)

                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

                val theDate = sdf.parse(selectedDate)
                theDate?.let {
                    val selectedDateInMinutes = theDate.time / 60000

                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))

                    currentDate?.let {
                        val currentDateInMinutes = currentDate.time / 60000

                        val difference = currentDateInMinutes - selectedDateInMinutes
                        if(difference < 18*365*24*60)
                            showToast("Valid For 18+ Years Of Age")
                    }
                }

            }, year, month, day)

        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
    }

    private fun validateInput() {
        val etEmail = findViewById<AppCompatEditText>(R.id.et_email).text.toString().trim()
        val etPassword = findViewById<AppCompatEditText>(R.id.et_password).text.toString()
        val etRePassword = findViewById<AppCompatEditText>(R.id.et_re_password).text.toString()


        if(etEmail.isNotEmpty()) {
            if(etEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())) {
                if(etPassword.isNotEmpty() && etRePassword.isNotEmpty()) {
                    if(etPassword == etRePassword) {
                        if(etPassword.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$".toRegex())) {
                            val intent = Intent(this, ImageActivity::class.java)

                            val bundle = Bundle()
                            bundle.putString("email", etEmail)
                            bundle.putString("dob", etSelectedDate?.text.toString())
                            bundle.putString("password", etPassword)

                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                        else showToast("Password must contain 8 letters(a capital and a small alphabet, a digit & one special symbol)")
                    }
                    else showToast("Passwords Doesn't Match")
                }
                else showToast("Passwords Field Can't Be Empty")
            }
            else showToast("Invalid Email Format")
        }
        else showToast("Email Field Can't Be Empty")
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
    }
}