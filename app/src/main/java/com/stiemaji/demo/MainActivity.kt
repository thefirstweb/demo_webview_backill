package com.stiemaji.demo

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.webkit.DownloadListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sitemaji.core.SitemajiAdFetchListener
import com.sitemaji.core.SitemajiBanner
import com.sitemaji.core.listener.BannerListener
import com.sitemaji.view.SitemajiAdView
import com.sitemaji.view.SitemajiAdViewStatusListener
import com.stiemaji.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    lateinit var mSitemajiAdView : SitemajiAdView
    lateinit var mSitemajiBanner : SitemajiBanner
    lateinit var mBind : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBind.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        mSitemajiAdView.destroy()
        mSitemajiBanner.onDestroy()
        super.onDestroy()
    }

    fun initView() {
        mSitemajiAdView = SitemajiAdView(this)
        mSitemajiAdView.setSitemajiAdViewStatusListener(object : SitemajiAdViewStatusListener {
            override fun onSuccess() {
                debug("AdView onSuccess")
                Log.i(TAG, "AdView onSuccess")
            }

            override fun onFail() {
                debug("AdView onFail")
                Log.i(TAG, "AdView onFail")
                runOnUiThread {
                    loadMobvista()
                }
            }

            override fun onClick() {
                debug("AdView onClick")
                Log.i(TAG, "AdView onClick")
            }
        })
        mSitemajiBanner = SitemajiBanner()
        mSitemajiBanner.setBannerListener(object : BannerListener {
            override fun onClick() {
                debug("Banner onClick")
                Log.i(TAG, "Banner onClick")
            }

            override fun onClose() {
                debug("Banner onClose")
                Log.i(TAG, "Banner onClose")
            }

            override fun onLoaded() {
                debug("Banner onLoaded")
                Log.i(TAG, "Banner onLoaded")
            }

            override fun onLoadFail() {
                debug("Banner onLoadFail")
                Log.i(TAG, "Banner onLoadFail")
            }

            override fun onFail(p0: Int, p1: String?) {
                val msg : String = String.format(
                    "Banner onFail errno: %s errmsg",
                    p0,
                    p1
                )
                debug(msg)
                Log.i(TAG, msg)
            }
        })
        mBind.buttomWebview.setOnClickListener {
            loadWebview()
        }
        mBind.buttomMobvista.setOnClickListener {
            loadMobvista()
        }
    }

    fun loadMobvista() {
        debug("Load mobvista")
        mBind.frameLayout.removeAllViews()
        mSitemajiBanner.fetch(this@MainActivity, "mobvista", object : SitemajiAdFetchListener {
            override fun onSuccess() {
                val layoutParams = ViewGroup.LayoutParams(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320F, resources.displayMetrics).toInt(),
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 480F, resources.displayMetrics).toInt()
                )
                mSitemajiBanner.display(
                    this@MainActivity,
                    mBind.frameLayout,
                    layoutParams
                )
            }

            override fun onFail(p0: Int, p1: String?) {
                val msg : String = String.format(
                    "Banner onFail errno: %s errmsg",
                    p0,
                    p1
                )
                debug(msg)
                Log.i(TAG, msg)
            }
        })
    }

    fun loadWebview() {
        debug("Load URL: ${Config.WEB_URL}")
        mBind.frameLayout.removeAllViews()
        val layoutParams = ViewGroup.LayoutParams(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320F, resources.displayMetrics).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 480F, resources.displayMetrics).toInt()
        )
        mBind.frameLayout.addView(
            mSitemajiAdView,
            layoutParams
        )
        mSitemajiAdView.loadUrl(Config.WEB_URL)
    }

    fun debug(message: String) {
        mBind.scrollView.post {
            mBind.textViewLog.append(message + "\n")
            mBind.scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }
}
