package com.app.adstertimes.ui.postDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import coil.load
import com.adster.sdk.mediation.AdError
import com.adster.sdk.mediation.AdEventsListener
import com.adster.sdk.mediation.AdRequestConfiguration
import com.adster.sdk.mediation.AdSterAdLoader
import com.adster.sdk.mediation.MediationAdListener
import com.adster.sdk.mediation.MediationBannerAd
import com.app.adstertimes.main.applicationContext
import com.app.adstertimes.base.BaseFragment
import com.app.adstertimes.utils.extensionFunctions.log
import com.app.adtertimes.databinding.FragmentPostDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding>() {

    override fun setBinding() = FragmentPostDetailsBinding.inflate(layoutInflater)

    private val viewModel: PostDetailsViewModel by viewModels()
    private lateinit var args: PostDetailsFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        args = PostDetailsFragmentArgs.fromBundle(requireArguments())
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchBannerAd()
        binding.apply {
            val item = args.postItem
            item.media?.firstOrNull()?.let {
                imageView.load("https://www.nytimes.com/${it.url}")
            }
            headline.text = item.headline?.main
            snippet.text = item.snippet
            paragraph.text = item.paragraph
            time.text = item.getDate()
        }

    }

    fun fetchBannerAd(){
        val configuration = AdRequestConfiguration.Companion.builder(
            applicationContext, "banner_0"
        )

        configuration.addCustomTargetingValue("news_app","true").addCustomTargetingValue("ad_type","banner")

        AdSterAdLoader.builder().withAdsListener(object : MediationAdListener() {

            override fun onBannerAdLoaded(ad: MediationBannerAd) {
                super.onBannerAdLoaded(ad)
                binding.bannerAdView.apply {
                    removeAllViews()
                    addView(ad.view)
                }
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

}