package com.sajworks.listmaker.utilities

import android.content.Context
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient

object AWSProvider {

    private var instance: AWSProvider? = null
    private var awsConfiguration: AWSConfiguration? = null
    private var dbClient: AmazonDynamoDBClient? = null
    private var dbMapper: DynamoDBMapper? = null

    fun getInstance() : AWSProvider {
        return instance!!
    }

    fun getConfiguration() : AWSConfiguration {
        return awsConfiguration!!
    }

    fun getIdentityManager() : IdentityManager {
        return IdentityManager.getDefaultIdentityManager()
    }

    fun getAmazonDynamoDBClient() : AmazonDynamoDBClient {
        if (dbClient == null) {
            val cp = getIdentityManager().credentialsProvider
            dbClient = AmazonDynamoDBClient(cp)
        }
        return dbClient!!
    }

    fun getDynamoDBMapper() : DynamoDBMapper {
        if (dbMapper == null) {
            dbClient = getAmazonDynamoDBClient()
            dbMapper = DynamoDBMapper.builder()
                    .awsConfiguration(getConfiguration())
                    .dynamoDBClient(dbClient)
                    .build()
        }
        return dbMapper!!
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