package com.sajworks.listmaker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amazonaws.mobile.auth.core.SignInStateChangeListener
import com.amazonaws.mobile.auth.ui.SignInUI
import com.amazonaws.mobile.client.AWSMobileClient
import com.sajworks.listmaker.R
import com.sajworks.listmaker.utilities.AWSProvider

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AWSMobileClient.getInstance().initialize(this).execute()

        AWSProvider.getIdentityManager().addSignInStateChangeListener(
                object : SignInStateChangeListener {
                    override fun onUserSignedIn() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onUserSignedOut() {
                        presentSignIn()
                    }
                }
        )

        presentSignIn()
    }

    // This method presents the sign-in UI
    fun presentSignIn() {

        val ui = AWSMobileClient.getInstance().getClient(this@LoginActivity, SignInUI::class.java) as SignInUI
        ui.login(this@LoginActivity, MainActivity::class.java).execute()
    }
}