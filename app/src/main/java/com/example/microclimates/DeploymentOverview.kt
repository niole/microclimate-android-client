package com.example.microclimates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import api.DeploymentOuterClass
import api.PeripheralOuterClass
import api.UserOuterClass
import com.example.microclimates.api.Stubs
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import org.w3c.dom.Text

class DeploymentOverview : Fragment() {

    private val LOG_TAG = "DeploymentOverview"
    private var listAdapter: ArrayAdapter<PeripheralOuterClass.Peripheral>? = null

    companion object {
        fun newInstance(): Fragment = DeploymentOverview()
    }

    private lateinit var viewModel: DeploymentOverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentLayout = inflater.inflate(R.layout.deployment_overview_fragment, container, false)
        var resourceId: Int = R.layout.fragment_peripheral

        val adapterPeripherals = mutableListOf<PeripheralOuterClass.Peripheral>()

        listAdapter = object : ArrayAdapter<PeripheralOuterClass.Peripheral>(activity, resourceId, adapterPeripherals) {
            override fun getCount(): Int {
                val total = adapterPeripherals?.size
                if (total != null) {
                    return total
                }
                return 0
            }
            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var baseView = convertView
                if (baseView == null) {
                   baseView = inflater?.inflate(resourceId, null)!!
                }

                val peripheral = getItem(position)
                baseView.findViewById<TextView>(R.id.peripheral_name).text = peripheral.hardwareId
                baseView.findViewById<TextView>(R.id.peripheral_type).text = peripheral.type.toString()
                baseView.findViewById<TextView>(R.id.last_received_event_time).text = "unknown"

                return baseView
            }
        }
        val listView = parentLayout.findViewById(R.id.peripherals) as ListView
        listView.adapter = listAdapter

        return parentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DeploymentOverviewViewModel::class.java)
        viewModel.getDeployment().observeForever {
            view?.findViewById<TextView>(R.id.deployment_status)?.text = it?.status.toString()
        }
        viewModel.getOwner().observeForever {
            view?.findViewById<TextView>(R.id.user_email)?.text = it?.email
        }
        viewModel.getPeripherals().observeForever {
            listAdapter?.clear()
            listAdapter?.addAll(it)
        }

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

                        val peripherals = getPeripherals(deployment.id)
                        view?.post { viewModel.setPeripherals(peripherals) }
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Log.e(LOG_TAG, "Failed to get user, $t")
            }
        }, MoreExecutors.directExecutor())
    }

    private fun getPeripherals(deploymentId: String): List<PeripheralOuterClass.Peripheral> {
        val request = PeripheralOuterClass.GetDeploymentPeripheralsRequest
            .newBuilder()
            .setDeploymentId(deploymentId)
            .build()
         return Stubs.peripheralStub().getDeploymentPeripherals(request).asSequence().toList()
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