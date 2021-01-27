package com.example.microclimates

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
    private lateinit var stubs: Stubs
    private val LOG_TAG = "MainActivity"
    private val model: CoreStateViewModel by viewModels()
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var pagerLayout: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stubs = Stubs(applicationContext)

        setContentView(R.layout.activity_main)

        val loadingScreen = LoadingScreenFragment
            .newInstance(null)
            .show(supportFragmentManager, R.id.main_content)

        model.getFetchOwnerState().observe({ lifecycle }) { fetchState ->
            if (fetchState == FetchState.FAILED) {
                loadingScreen.setFailed("Failed to fetch your data. Try again later")
            } else if (fetchState == FetchState.SUCCESS) {
                loadingScreen.hide(supportFragmentManager)
            } else {
                loadingScreen.show(supportFragmentManager, R.id.main_content)
            }
        }

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

        val email = intent.getStringExtra("email")
        val jwt = intent.getStringExtra("jwt")

        // TODO do this in the login activity
        applicationContext
            .getSharedPreferences("microclimate-prefs", MODE_PRIVATE)
            .edit()
            .putString("jwt", jwt)
            .commit()

        val mainHandler = Handler(baseContext.mainLooper)

        model.setFetchOwnerState(FetchState.FETCHING)
        val request = UserOuterClass.GetUserByEmailRequest
            .newBuilder()
            .setEmail(email)
            .build()

        val userChannel = Channels.getInstance(applicationContext).userChannel()
        val userFetch = stubs.userStub(userChannel).getUserByEmail(request)
        Futures.addCallback(userFetch, object : FutureCallback<UserOuterClass.User?> {
            override fun onSuccess(user: UserOuterClass.User?): Unit {
                mainHandler.post {
                    model.setFetchOwnerState(FetchState.SUCCESS)
                    model.setOwner(user)
                }

                if (user != null) {
                    getDeployment(user.id, mainHandler)
                }

                userChannel.shutdown()
                userChannel.awaitTermination(5, TimeUnit.SECONDS)
            }

            override fun onFailure(t: Throwable) {
                Log.e(LOG_TAG, "Failed to get user, message: ${t.message}, cause: ${t.cause}")
                mainHandler.post {
                    model.setFetchOwnerState(FetchState.FAILED)
                }

                userChannel.shutdown()
                userChannel.awaitTermination(5, TimeUnit.SECONDS)
            }
        }, MoreExecutors.directExecutor())
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val deploymentOverviewPage = "deploymentPage"
        private val eventsPage = "eventsPage"
        private val pages = listOf<Pair<String, String>>(
            Pair("Deployment", deploymentOverviewPage),
            Pair("Sensors", eventsPage)
        )

        override fun getPageTitle(position: Int): String {
            return pages[position].first
        }

        override fun getItem(position: Int): Fragment {
            return when(pages[position].second) {
                deploymentOverviewPage -> DeploymentOverview.newInstance()
                eventsPage -> EventsView.newInstance()
                else -> DeploymentOverview.newInstance()
            }
        }

        override fun getCount(): Int {
            return pages.size
        }
    }

    private fun getDeployment(ownerId: String, messageLoop: Handler): DeploymentOuterClass.Deployment? {
        messageLoop.post {
            model.setFetchDeploymentState(FetchState.FETCHING)
        }

        try {
            val request = DeploymentOuterClass
                .GetDeploymentsForUserRequest
                .newBuilder()
                .setUserId(ownerId)
                .build()

            val deploymentChannel = Channels.getInstance(applicationContext).deploymentChannel()

            val deployments = stubs
                .blockingDeploymentStub(deploymentChannel)
                .getDeploymentsForUser(request)

            val deployment = deployments.asSequence().elementAtOrNull(0)

            messageLoop.post {
                model.setDeployment(deployment)
                model.setFetchDeploymentState(FetchState.SUCCESS)
            }

            deploymentChannel.shutdown()
            deploymentChannel.awaitTermination(5, TimeUnit.SECONDS)

            return deployment
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to get deployment, error: $e")
            messageLoop.post {
                model.setFetchDeploymentState(FetchState.FAILED)
            }
        }
        return null
    }

}
