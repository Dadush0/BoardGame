package org.wit.boardgame.activities.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import org.wit.boardgame.R
import org.wit.boardgame.activities.data.model.LoggedInUser
import java.util.UUID


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource constructor(context: Context) {


    lateinit var mContext: Context
     private lateinit var masterKey: MasterKey
    private lateinit var sharedPreferences: SharedPreferences
    init {
        try {
            mContext = context
            masterKey = MasterKey.Builder(mContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            sharedPreferences  = EncryptedSharedPreferences.create(
                    mContext,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        } catch (_: Exception) {
        }
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        val loggedInUser = LoggedInUser(UUID.randomUUID().toString(), username, password)

        return if(retrieveAccount(loggedInUser)) {
            Result.Success(loggedInUser)
        } else {
            Result.Error(Exception(R.string.login_failed.toString()))
        }
    }



    fun logout() {
        // TODO: revoke authentication
    }

    private fun storeAccount(user: LoggedInUser) {
        // use the shared preferences and editor as you normally would
        val prefsEditor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString(user.displayName, json)
        prefsEditor.apply()
    }

    private fun retrieveAccount(user: LoggedInUser): Boolean {

        val gson = Gson()
        val json: String? = sharedPreferences.getString(user.displayName, "")
        if(json == "") {
            storeAccount(user)
            return true
        }
        val obj: LoggedInUser = gson.fromJson(json, LoggedInUser::class.java)
        if(obj.password == user.password) {
            return true
        }
        return false
    }
}