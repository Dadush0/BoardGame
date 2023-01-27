package org.wit.boardgame.activities.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.boardgame.activities.PlacemarkListActivity
import org.wit.boardgame.activities.data.LoginDataSource
import org.wit.boardgame.activities.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory constructor(context: Context) : ViewModelProvider.Factory {

    lateinit var mContext: Context
    init {
        try {
            mContext = context

        } catch (e: Exception) {

        }
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(mContext)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}