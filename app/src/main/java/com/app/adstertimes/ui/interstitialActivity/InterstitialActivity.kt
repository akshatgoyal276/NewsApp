package com.app.adstertimes.ui.interstitialActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.app.adstertimes.base.BaseActivity
import com.adster.sdk.mediation.AdError
import com.adster.sdk.mediation.AdEventsListener
import com.adster.sdk.mediation.AdRequestConfiguration
import com.adster.sdk.mediation.AdSter
import com.adster.sdk.mediation.AdSterAdLoader
import com.adster.sdk.mediation.AdapterStatus
import com.adster.sdk.mediation.InitializationListener
import com.adster.sdk.mediation.MediationAdListener
import com.adster.sdk.mediation.MediationNativeAd
import com.adster.sdk.mediation.MediationNativeAdView
import com.app.adstertimes.utils.extensionFunctions.log
import com.app.adstertimes.utils.extensionFunctions.show
import com.app.adtertimes.databinding.ActivityInterstitialBinding
import com.app.adtertimes.databinding.AdNativeFulscreenLayoutBinding

class InterstitialActivity : BaseActivity<ActivityInterstitialBinding>() {

    override fun setBinding(): ActivityInterstitialBinding = ActivityInterstitialBinding.inflate(layoutInflater)


    override fun onViewReady(savedInstanceState: Bundle?) {
        super.onViewReady(savedInstanceState)
        AdSter.initializeSdk(applicationContext, object : InitializationListener {
            override fun onInitializationComplete(adapterStatus: List<AdapterStatus>) {
                "adster sdk initialization complete".log()
                val configuration = AdRequestConfiguration.Companion.builder(
                    applicationContext,
                    "native_0"
                )

                AdSterAdLoader.builder().withAdsListener(object : MediationAdListener() {

                    override fun onNativeAdLoaded(ad: MediationNativeAd) {
                        super.onNativeAdLoaded(ad)
                        "onNativeAdLoaded: ${ad.headLine}".log()
                        // Create AdSter MediationNativeAdView object
                        val adView = MediationNativeAdView(applicationContext)
                        adView.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        // Add this layout as a parent to your native ad layout
                        val nativeAdView = AdNativeFulscreenLayoutBinding.inflate(
                            LayoutInflater.from(applicationContext),
                            adView,
                            true
                        )

                        // If MediaView is present add AdSter's MediaView as a child to given MediaView
                        if (ad.mediaView != null) {
                            nativeAdView.mediaView.apply {
                                addView(ad.mediaView)
                                show()
                            }
                        }

                        adView.apply {
                            nativeAdView.apply {
                                headlineView = tvTitle
                                bodyView = tvBody
                                advertiserView = tvInfo
                                logoView = ivLogo
                                ctaView = btnCTA
                            }
                        }

                        nativeAdView.apply {
                            ad.apply {
                                tvTitle.text = headLine
                                tvBody.text = body
                                tvInfo.text = advertiser
                                ivLogo.show()
                                ivLogo.load(logo)
                                btnCTA.text = callToAction
                            }
                        }

                        adView.nativeAd = ad

                        binding.main.apply {
                            removeAllViews()
                            addView(adView)
                        }
                        "success: native".log()
                    }

                    override fun onFailure(adError: AdError) {
                        // Handle failure here
                        "onFailure: ${adError.errorCode} ${adError.errorMessage}".log()
                    }
                }).withAdsEventsListener(object : AdEventsListener() {
                    override fun onAdClicked() {
                        //Handle ad click here
                    }

                    override fun onAdImpression() {
                        //Handle ad impression here
                    }
                }).build().loadAd(configuration.build())
            }
        })
    }
}