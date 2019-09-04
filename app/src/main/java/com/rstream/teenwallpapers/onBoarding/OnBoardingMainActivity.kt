package com.rstream.teenwallpapers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.view.WindowManager



class OnBoardingMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContentView(R.layout.onboarding_activity_main)



        val premiumDialogActivity = PremiumDialogActivity(this)
        premiumDialogActivity.initializeBillingClient()

        val sharedPreferences = getSharedPreferences("prefs.xml", Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean("appOpened", false)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val getPremium = GetPremium(this,this)

        getPremium.getPrice(this,"monthly", getString(R.string.premium_sub_monthly)) {
            Log.d("mothlyprice",it)
        }
        getPremium.getPrice(this,"6month", getString(R.string.premium_sub_sixmonth)) {
            Log.d("mothlyprice",it)
        }




    }


}
