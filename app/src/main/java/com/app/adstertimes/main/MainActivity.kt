package com.app.adstertimes.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.adster.sdk.mediation.AdRequestConfiguration
import com.adster.sdk.mediation.AdSter
import com.adster.sdk.mediation.AdapterStatus
import com.adster.sdk.mediation.InitializationListener
import com.app.adstertimes.base.BaseActivity
import com.app.adstertimes.utils.extensionFunctions.log
import com.app.adtertimes.R
import com.app.adtertimes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object{
        lateinit var activity: MainActivity
    }

    override fun setBinding() = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val configuration = AdRequestConfiguration.Companion.builder(this, "appopen_0")

    override fun onViewReady(savedInstanceState: Bundle?) {
        super.onViewReady(savedInstanceState)
        activity = this
        setSupportActionBar(binding.appBarMain.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ),  binding.drawerLayout
        )
        setupActionBarWithNavController(getNavController(), appBarConfiguration)
        binding.navView.setupWithNavController(getNavController())
        initializeAdsterSDK()
    }

    private fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentContainer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun initializeAdsterSDK(){
        "about to init".log()
        AdSter.initializeSdk(applicationContext, object : InitializationListener {
            override fun onInitializationComplete(adapterStatus: List<AdapterStatus>) {
                "adster sdk initialization complete".log()
                getAppOpenAd()
                "$adapterStatus".log("adapter status")
//                Test case for launch mode checking with singletop and singletask
//                (check for not creating 2 instances in recents when launching singltask after singletop)
//                val intent = Intent(applicationContext,InterstitialActivity::class.java)
//                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkAndShowAd()
    }

    fun getAppOpenAd(){
//        AdSter.loadAppOpenAd(this, configuration.build(),
//            object : AppOpenAdEventsListener() {
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    "app open ad loadad".log()
//                    // Here ad can be shown using a call to showAdIfAvailable method
//                }
//
//                override fun onAdLoadFailure(adError: AdError) {
//                    super.onAdLoadFailure(adError)
//                    "app open ad failure ${adError.errorMessage}".log()
//                    //Handle failure callback here
//                }
//            }
//        )
    }

    fun checkAndShowAd(){
//        AdSter.showAdIfAvailable(this, configuration.build(),
//            object : AppOpenAdEventsListener() {
//                override fun onShowAdComplete() {
//                    super.onShowAdComplete()
//                    //Handle show ad complete here
//                    "app open ad show complete".log()
//                }
//
//                override fun onAdLoadFailure(adError: AdError) {
//                    super.onAdLoadFailure(adError)
//                    "app open ad load failure ${adError.errorMessage}".log()
//                    //Handle failure callback here
//                }
//
//                override fun onAdImpression() {
//                    super.onAdImpression()
//                    //Handle impression here
//                }
//
//                override fun onAdClicked() {
//                    super.onAdClicked()
//                    //Handle click here
//                }
//            }
//        )
    }
}