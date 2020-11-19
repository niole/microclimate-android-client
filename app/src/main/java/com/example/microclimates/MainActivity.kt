package com.example.microclimates

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import api.DeploymentOuterClass
import api.UserOuterClass
import com.example.microclimates.api.Stubs
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : FragmentActivity() {
    private val LOG_TAG = "MainActivity"
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var pagerLayout: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerLayout = findViewById<ViewPager>(R.id.container)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        pagerLayout?.adapter = mSectionsPagerAdapter

        val model: CoreStateViewModel by viewModels()

        val email = "niolenelson@gmail.com"
        val mainHandler = Handler(baseContext.mainLooper)

        Futures.addCallback(getUser(email), object : FutureCallback<UserOuterClass.User?> {
            override fun onSuccess(user: UserOuterClass.User?): Unit {
                if (user != null) {
                    mainHandler.post {
                        model.setOwner(user)
                    }
                    val deployment = getDeployment(user.id)
                    if (deployment != null) {
                        mainHandler.post {
                            model.setDeployment(deployment)
                        }
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Log.e(LOG_TAG, "Failed to get user, message: ${t.message}, cause: ${t.cause}")
            }
        }, MoreExecutors.directExecutor())

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val setupPage = "setupPage"
        private val deploymentOverviewPage = "deploymentPage"
        private val pages = listOf<String>(deploymentOverviewPage, setupPage)

        override fun getItem(position: Int): Fragment {
            val pageName = pages[position]
            return when(pageName) {
                setupPage -> SetupPage.newInstance()
                deploymentOverviewPage -> DeploymentOverview.newInstance()
                else -> SetupPage()
            }
        }

        override fun getCount(): Int {
            return pages.size
        }
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


}
