package com.app.newsapp.ui.book

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adster.sdk.mediation.AdError
import com.adster.sdk.mediation.AdEventsListener
import com.adster.sdk.mediation.AdRequestConfiguration
import com.adster.sdk.mediation.AdSterAdLoader
import com.adster.sdk.mediation.InterstitialAdEventsListener
import com.adster.sdk.mediation.MediationAdListener
import com.adster.sdk.mediation.MediationBannerAd
import com.adster.sdk.mediation.MediationInterstitialAd
import com.adster.sdk.mediation.MediationNativeAd
import com.adster.sdk.mediation.MediationNativeAdView
import com.adster.sdk.mediation.MediationRewardedAd
import com.adster.sdk.mediation.Reward
import com.adster.sdk.mediation.RewardedAdEventsListener
import com.app.newsapp.main.MainActivity
import com.app.newsapp.adapter.BookAdapter
import com.app.newsapp.main.applicationContext
import com.app.newsapp.base.BaseFragment
import com.app.newsapp.data.dataModals.Book
import com.app.newsapp.databinding.AdNativeLayoutBinding
import com.app.newsapp.databinding.FragmentBooksBinding
import com.app.newsapp.databinding.ItemPostAdBinding
import com.app.newsapp.utils.extensionFunctions.click
import com.app.newsapp.utils.extensionFunctions.gone
import com.app.newsapp.utils.extensionFunctions.log
import com.app.newsapp.utils.extensionFunctions.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BooksFragment : BaseFragment<FragmentBooksBinding>() {

    override fun setBinding() = FragmentBooksBinding.inflate(layoutInflater)

    private val viewModel: BooksViewModel by viewModels()

    @Inject
    lateinit var adapter : BookAdapter

    private var booksClickedCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            adapter.onClick = {
                booksClickedCount++
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.productUrl))
                startActivity(intent)
            }
            booksRecyclerView.adapter = adapter
            booksRecyclerView.addOnScrollListener(ScrollListener())
            adapter.loadAd = { binding -> fetchNativeAd(binding) }
            icNews.click { activity?.onBackPressed() }
        }

        viewModel.getBooks()
        viewModel.list.observe(viewLifecycleOwner) {
            adapter.updateList(it.toList())
        }

        viewModel.getInProgress.observe(viewLifecycleOwner){
            if(it) showProgressBar()
            else hideProgressBar()
        }

        if(booksClickedCount!=0){
            if(booksClickedCount%4==0) fetchRewardAd()
            else if(booksClickedCount%2==0) fetchInterstitialAd()
        }

    }

    private fun fetchNativeAd(binding: ItemPostAdBinding) {
        val configuration = AdRequestConfiguration.Companion.builder(
            applicationContext, "native_0"
        )
        configuration.addCustomTargetingValue("news_app","true").addCustomTargetingValue("ad_type","native")

        AdSterAdLoader.builder().withAdsListener(object : MediationAdListener() {

            override fun onNativeAdLoaded(ad: MediationNativeAd) {
                super.onNativeAdLoaded(ad)
                "onNativeAdLoaded: ${ad.headLine}".log()
                // Create AdSter MediationNativeAdView object
                val adView = MediationNativeAdView(applicationContext)
                adView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                // Add this layout as a parent to your native ad layout
                val nativeAdView = AdNativeLayoutBinding.inflate(
                    LayoutInflater.from(applicationContext), adView, true
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
                        headlineView = titleTextView
                        bodyView = bodyTextView
                        ctaView = ctaButton
                        advertiserView = infoTextView
                        logoView = iconLogoImageView
//                        iconLogoImageView.show()
                        iconLogoImageView.load(ad.logo)
                    }
                }

                nativeAdView.apply {
                    ad.apply {
                        titleTextView.text = headLine
                        bodyTextView.text = body
                        ctaButton.text = callToAction
                        infoTextView.text = advertiser
                    }
                }

                adView.nativeAd = ad

                binding.main.apply {
                    removeAllViews()
                    addView(adView)
                }
                "success: native".log()
            }

            override fun onBannerAdLoaded(ad: MediationBannerAd) {
                "on ${ad.adType} ad loaded ".log()
                binding.main.apply {
                    removeAllViews()
                    addView(ad.view)
                }
                super.onBannerAdLoaded(ad)
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

    private fun navigateToBookDetailsFragment(it: Book) {

    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        var dySum = 0
        override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(rv, dx, dy)

            val layoutManager = rv.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            if (lastVisibleItemPosition == totalItemCount - 1) viewModel.getBooks()

            dySum+=dy
            if(dySum>400) {
                binding.headerLayout.gone()
                dySum = 0
            }
            else if(firstVisibleItemPosition==0 || dySum<-400) {
                binding.headerLayout.show()
                dySum = 0
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.gone()
    }

    private fun fetchRewardAd() {
        val configuration = AdRequestConfiguration.Companion.builder(
            applicationContext, "rewarded_0"
        )
        configuration.addCustomTargetingValue("news_app","true").addCustomTargetingValue("ad_type","rewarded")

        AdSterAdLoader.builder().withAdsListener(object : MediationAdListener() {

            override fun onRewardedAdLoaded(ad: MediationRewardedAd) {
                super.onRewardedAdLoaded(ad)
                "${ad.adType} ad loaded".log()
                ad.showAd(MainActivity.activity)
            }

            override fun onFailure(adError: AdError) {
                // Handle failure here
                "onFailure: ${adError.errorCode} ${adError.errorMessage}".log()
            }
        }).withRewardedAdEventsListener(object : RewardedAdEventsListener() {
            override fun onAdClicked() {
                //Handle ad click here
            }

            override fun onAdImpression() {
                //Handle ad impression here
            }

            override fun onUserEarnedReward(reward: Reward) {

            }

            override fun onVideoClosed() {

            }

            override fun onVideoComplete() {

            }

            override fun onVideoStart() {

            }
        }).build().loadAd(configuration.build())
    }


    private fun fetchInterstitialAd() {
        val configuration = AdRequestConfiguration.Companion.builder(
            applicationContext, "interstitial_0"
        )
        configuration.addCustomTargetingValue("news_app","true").addCustomTargetingValue("ad_type","interstitial")

        AdSterAdLoader.builder().withAdsListener(object : MediationAdListener() {

            override fun onInterstitialAdLoaded(ad: MediationInterstitialAd) {
                super.onInterstitialAdLoaded(ad)
                "${ad.adType} ad loaded".log()
                ad.showAd(MainActivity.activity)
            }

            override fun onFailure(adError: AdError) {
                // Handle failure here
                "onFailure: ${adError.errorCode} ${adError.errorMessage}".log()
            }
        }).withInterstitialAdEventsListener(object : InterstitialAdEventsListener() {
            override fun onAdClicked() {
                //Handle ad click here
            }

            override fun onAdClosed() {

            }

            override fun onAdImpression() {
                //Handle ad impression here
            }

            override fun onAdOpened() {

            }
        }).build().loadAd(configuration.build())
    }

}