package org.wit.boardgame.activities.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import org.wit.boardgame.activities.PlacemarkListActivity
import org.wit.boardgame.activities.data.model.LoggedInUser
import org.wit.boardgame.activities.ui.login.LoginFragment
import java.io.IOException


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

        } catch (e: Exception) {
        }
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser("", username)
            retrieveAccount(fakeUser)
            if(false) {

            }
            else {
                storeAccount(fakeUser)
            }

            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }



    fun logout() {
        // TODO: revoke authentication
    }

    fun storeAccount(user: LoggedInUser) {


        // use the shared preferences and editor as you normally would
        val prefsEditor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("User", json)
        prefsEditor.commit()
    }

    fun retrieveAccount(user: LoggedInUser) {

        val gson = Gson()
        val json: String? = sharedPreferences.getString("User", "")
        val obj: LoggedInUser = gson.fromJson(json, LoggedInUser::class.java)
    }
}