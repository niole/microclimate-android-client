package com.example.microclimates

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import api.DeploymentOuterClass
import api.UserOuterClass
import com.example.microclimates.api.Channels
import com.example.microclimates.api.Stubs
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "MainActivity"
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var pagerLayout: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerLayout = findViewById<ViewPager>(R.id.container)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        pagerLayout?.adapter = mSectionsPagerAdapter

        pagerLayout?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                title = pagerLayout?.adapter?.getPageTitle(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        val model: CoreStateViewModel by viewModels()
        val email = intent.getStringExtra("email")
        val mainHandler = Handler(baseContext.mainLooper)

        val request = UserOuterClass.GetUserByEmailRequest
            .newBuilder()
            .setEmail(email)
            .build()

        val userChannel = Channels.userChannel()
        val userFetch = Stubs.userStub(userChannel).getUserByEmail(request)
        Log.i(LOG_TAG, "Requesting user by email $email")
        Futures.addCallback(userFetch, object : FutureCallback<UserOuterClass.User?> {
            override fun onSuccess(user: UserOuterClass.User?): Unit {
                Log.i(LOG_TAG, "Got user ${user?.id}")
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
                userChannel.shutdown()
                userChannel.awaitTermination(5, TimeUnit.SECONDS)
            }

            override fun onFailure(t: Throwable) {
                Log.e(LOG_TAG, "Failed to get user, message: ${t.message}, cause: ${t.cause}")
                userChannel.shutdown()
                userChannel.awaitTermination(5, TimeUnit.SECONDS)
            }
        }, MoreExecutors.directExecutor())

        userChannel.notifyWhenStateChanged(userChannel.getState(true)) {
            println("stat changed")
            println(userChannel.getState(true))
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val setupPage = "setupPage"
        private val deploymentOverviewPage = "deploymentPage"
        private val eventsPage = "eventsPage"
        private val pages = listOf<Pair<String, String>>(
            Pair("Deployment", deploymentOverviewPage),
            Pair("Sensors", eventsPage),
            Pair("Add New Sensor", setupPage)
        )

        override fun getPageTitle(position: Int): String {
            return pages[position].first
        }

        override fun getItem(position: Int): Fragment {
            return when(pages[position].second) {
                setupPage -> SetupPage.newInstance()
                deploymentOverviewPage -> DeploymentOverview.newInstance()
                eventsPage -> EventsView.newInstance()
                else -> SetupPage()
            }
        }

        override fun getCount(): Int {
            return pages.size
        }
    }

    private fun getDeployment(ownerId: String): DeploymentOuterClass.Deployment? {
        try {
            val request = DeploymentOuterClass
                .GetDeploymentsForUserRequest
                .newBuilder()
                .setUserId(ownerId)
                .build()

            val deploymentChannel = Channels.deploymentChannel()
            val deployments = Stubs.blockingDeploymentStub(deploymentChannel).getDeploymentsForUser(request)
            val deployment = deployments.asSequence().elementAtOrNull(0)

            deploymentChannel.shutdown()
            deploymentChannel.awaitTermination(5, TimeUnit.SECONDS)

            return deployment
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to get deployment, error: $e")
        }
        return null
    }

}
