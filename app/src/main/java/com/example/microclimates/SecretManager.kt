package com.example.microclimates.secretmanager

import com.google.cloud.secretmanager.v1.*
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.protobuf.ByteString

class SecretManager {
    @Throws(Exception::class)
    fun init() {
        val projectId = "microclimates-299418"
        val secretId = "hwid"
        quickstart(projectId, secretId)
    }

    @Throws(Exception::class)
    fun quickstart(projectId: String?, secretId: String?) {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        SecretManagerServiceClient.create().use { client ->
            // Build the parent name from the project.
            val projectName =
                ProjectName.of(projectId)

            // Create the parent secret.
            val secret =
                Secret.newBuilder()
                    .setReplication(
                        Replication.newBuilder()
                            .setAutomatic(
                                Replication.Automatic.newBuilder()
                                    .build()
                            )
                            .build()
                    )
                    .build()
            val createdSecret =
                client.createSecret(projectName, secretId, secret)

            // Add a secret version.
            val payload =
                SecretPayload.newBuilder()
                    .setData(ByteString.copyFromUtf8("hello world!")).build()
            val addedVersion =
                client.addSecretVersion(createdSecret.name, payload)

            // Access the secret version.
            val response =
                client.accessSecretVersion(addedVersion.name)

            // Print the secret payload.
            //
            // WARNING: Do not print the secret in a production environment - this
            // snippet is showing how to access the secret material.
            val data = response.payload.data.toStringUtf8()
            System.out.printf("Plaintext: %s\n", data)
        }
    }
}