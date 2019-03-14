package com.sajworks.listmaker.utilities

import android.content.Context
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider
import com.amazonaws.mobile.config.AWSConfiguration

object AWSProvider {

    private var instance: AWSProvider? = null
    private var awsConfiguration: AWSConfiguration? = null

    fun getInstance() : AWSProvider {
        return instance!!
    }

    fun getConfiguration() : AWSConfiguration {
        return awsConfiguration!!
    }

    fun getIdentityManager() : IdentityManager {
        return IdentityManager.getDefaultIdentityManager()
    }

    @JvmStatic
    fun initialize(context: Context) {
        if (instance == null) {
            instance = getAWSProvider(context)
        }
    }

    private fun getAWSProvider(context: Context) : AWSProvider {
        this.awsConfiguration = AWSConfiguration(context)

        // Initialize IdentityManager
        val identityManager = IdentityManager(context, awsConfiguration)
        IdentityManager.setDefaultIdentityManager(identityManager)
        identityManager.addSignInProvider(CognitoUserPoolsSignInProvider::class.java)

        return this
    }
}