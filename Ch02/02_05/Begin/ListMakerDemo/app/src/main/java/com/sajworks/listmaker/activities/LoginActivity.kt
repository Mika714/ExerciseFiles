package com.sajworks.listmaker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amazonaws.mobile.auth.ui.SignInUI
import com.amazonaws.mobile.client.AWSMobileClient
import com.sajworks.listmaker.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presentSignIn()
    }

    // This method presents the sign-in UI
    fun presentSignIn() {

        AWSMobileClient.getInstance().initialize(this) {
            val ui = AWSMobileClient.getInstance().getClient(this@LoginActivity, SignInUI::class.java) as SignInUI
            ui.login(this@LoginActivity, MainActivity::class.java).execute()
        }.execute()
    }
}