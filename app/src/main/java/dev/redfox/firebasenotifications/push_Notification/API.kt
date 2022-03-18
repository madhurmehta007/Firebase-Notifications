package dev.redfox.firebasenotifications.push_Notification

import dev.redfox.firebasenotifications.push_Notification.Constants.Companion.CONTENT_TYPE
import dev.redfox.firebasenotifications.push_Notification.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface API {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}