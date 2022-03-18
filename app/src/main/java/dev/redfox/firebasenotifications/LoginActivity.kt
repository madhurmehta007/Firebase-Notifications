package dev.redfox.firebasenotifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.redfox.firebasenotifications.databinding.ActivityLoginBinding
import dev.redfox.firebasenotifications.databinding.ActivityMainBinding


private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegister.setOnClickListener {

            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@LoginActivity, "Please enter email", Toast.LENGTH_SHORT)
                        .show()
                }

                TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }


                else -> {
                    val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task->
                                var analytics = FirebaseAnalytics.getInstance(applicationContext)
                                if (task.isSuccessful) {

                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@LoginActivity,
                                        "You are logged in successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }

    }
}