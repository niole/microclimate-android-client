package com.example.microclimates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import api.DeploymentOuterClass
import api.UserOuterClass
import com.example.microclimates.api.Stubs
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class DeploymentOverview : Fragment() {

    private val LOG_TAG = "DeploymentOverview"

    companion object {
        fun newInstance(): Fragment = DeploymentOverview()
    }

    private lateinit var viewModel: DeploymentOverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.deployment_overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DeploymentOverviewViewModel::class.java)
        viewModel.getDeployment().observeForever { println(it) }
        viewModel.getOwner().observeForever { println(it) }

        getInitialData()
    }

    private fun getInitialData(): Unit {
        val email = "niolenelson@gmail.com"

        val request = getUser(email)
        request.addListener({ println("Making request for user") }, MoreExecutors.directExecutor())
        Futures.addCallback(request, object : FutureCallback<UserOuterClass.User?> {
            override fun onSuccess(user: UserOuterClass.User?): Unit {
                if (user != null) {
                    view?.post {
                        viewModel.setOwner(user)
                    }
                    val deployment = getDeployment(user.id)
                    if (deployment != null) {
                        view?.post {
                            viewModel.setDeployment(deployment)
                        }
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Log.e(LOG_TAG, "Failed to get user, $t")
            }
        }, MoreExecutors.directExecutor())
    }

    private fun getUser(email: String): ListenableFuture<UserOuterClass.User?> {
        val request = UserOuterClass.GetUserByEmailRequest
            .newBuilder()
            .setEmail(email)
            .build()
        return Stubs.userStub().getUserByEmail(request)
    }

    private fun getDeployment(ownerId: String): DeploymentOuterClass.Deployment? {
        try {
            val request = DeploymentOuterClass
                .GetDeploymentsForUserRequest
                .newBuilder()
                .setUserId(ownerId)
                .build()
            val deployments = Stubs.blockingDeploymentStub().getDeploymentsForUser(request)
            return deployments.asSequence().elementAtOrNull(0)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to get deployment, error: $e")
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            2 ->
                if (resultCode == Activity.RESULT_OK) {
                    print("can access network state")
                    getInitialData()
                } else {
                    print("fail access network state")
                }
            3 ->
                if (resultCode == Activity.RESULT_OK) {
                    print("can access internet")
                    getInitialData()
                } else {
                    print("fail access internet")
                }
            else -> Log.w(LOG_TAG,"Request with code $requestCode not recognized")
        }
    }

}