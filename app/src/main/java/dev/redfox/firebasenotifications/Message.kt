package dev.redfox.firebasenotifications

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dev.redfox.firebasenotifications.databinding.ActivityMainBinding
import dev.redfox.firebasenotifications.databinding.ActivityMessageBinding
import dev.redfox.firebasenotifications.push_Notification.Notification
import dev.redfox.firebasenotifications.push_Notification.PushNotification
import dev.redfox.firebasenotifications.push_Notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val  TOPIC = "/topics/myTopic"
private lateinit var binding: ActivityMessageBinding
class Message : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.btnSend.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    Notification(title, message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }

    }


    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(ContentValues.TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(ContentValues.TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(ContentValues.TAG, e.toString())
        }
    }
}

