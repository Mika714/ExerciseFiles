package com.sajworks.listmaker.utilities

import android.content.Context
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

    @JvmStatic
    fun initialize(context: Context) {
        if (instance == null) {
            instance = getAWSProvider(context)
        }
    }

    private fun getAWSProvider(context: Context) : AWSProvider {
        this.awsConfiguration = AWSConfiguration(context)
        return this
    }
}