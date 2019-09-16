package com.rstream.biblequotes

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener

import java.util.ArrayList

import android.content.Context.MODE_PRIVATE

class SubsPremium
// String price;

(internal var mContext: Context, internal var activity: Activity) : PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null
    internal var sharedPreferences: SharedPreferences = mContext.getSharedPreferences("prefs.xml", MODE_PRIVATE)

    private fun handlePurchase(purchase: Purchase, IAPtype: String?) {

        Log.d("purchased", "success" + purchase.purchaseState + IAPtype)
        var openPurchase = true

        if (sharedPreferences.getBoolean("purchased", false) || sharedPreferences.getBoolean("monthlySubscribed", false) || sharedPreferences.getBoolean("sixMonthSubscribed", false)) {
            openPurchase = false
        }

        var flag = true
        if (openPurchase)
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

                if (IAPtype != null) {
                    if (IAPtype.isNullOrEmpty() && IAPtype.trim { it <= ' ' } == "premiumIAP")
                        sharedPreferences.edit().putBoolean("purchased", true).apply()
                    else {
                        Log.d("purchased", "one")
                        sharedPreferences.edit().putBoolean("monthlySubscribed", false).apply()
                        sharedPreferences.edit().putBoolean("sixMonthSubscribed", false).apply()
                    }
                }
                if (IAPtype != null) {
                    if (IAPtype.isNullOrEmpty() && IAPtype.trim { it <= ' ' } == "monthly") {
                        flag = false
                        sharedPreferences.edit().putBoolean("monthlySubscribed", true).apply()
                    }
                }
                if (IAPtype.isNullOrEmpty() && IAPtype!!.trim { it <= ' ' } == "sixmonth") {
                    flag = false
                    sharedPreferences.edit().putBoolean("sixMonthSubscribed", true).apply()
                }
                if (flag) {
                    Log.d("purchased", "two")
                    sharedPreferences.edit().putBoolean("sixMonthSubscribed", false).apply()
                    sharedPreferences.edit().putBoolean("monthlySubscribed", false).apply()
                }


                // select which activity to go after puchasing

                /*Intent intent = new Intent(MainActivity.mContext,SplashScreen.class);
                mContext.startActivity(intent);*/

                Log.d("purchased", "success")
                // Acknowledge purchase and grant the item to the user
            } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Log.d("purchased", "pending")
                sharedPreferences.edit().putBoolean("purchased", false).apply()
                sharedPreferences.edit().putBoolean("monthlySubscribed", false).apply()
                sharedPreferences.edit().putBoolean("sixMonthSubscribed", false).apply()
                // Here you can confirm to the user that they've started the pending
                // purchase, and to complete it, they should follow instructions that
                // are given to them. You can also choose to remind the user in the
                // future to complete the purchase if you detect that it is still
                // pending.
            } else {
                Log.d("purchased", "not")
                sharedPreferences.edit().putBoolean("monthlySubscribed", false).apply()
                sharedPreferences.edit().putBoolean("sixMonthSubscribed", false).apply()
            }


    }

    fun getPrice(context: Context, iapName: String?, premiumSku: String, priceListener: PriceListener) {
        // final String[] price = {""};

        billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()

        billingClient!!.startConnection(object : BillingClientStateListener {


            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.d("billingresoponse", (billingResult.getResponseCode()).toString() + "" + BillingClient.BillingResponseCode.OK)
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //    Log.d("pricesofvals","here");
                    val purchasesResult: Purchase.PurchasesResult
                    if (iapName != null && iapName!!.trim { it <= ' ' } == "lifetime")
                        purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                    else
                        purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.SUBS)
                    //price[0] =getPurchaseDetails(premiumSku);
                    try {
                        getPurchaseDetails(premiumSku, priceListener, iapName)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    //  Log.d("pricesofvals",price[0]);
                    try {
                        refreshPurchaseList(context, iapName)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    // Log.d("billingclient","price = "+ price);
                    //launchIAP();
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

    }

    fun callIAP(context: Context, iapType: String, iapName: String?) {
        billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()

        billingClient!!.startConnection(object : BillingClientStateListener {


            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.d("billingresoponse", (billingResult.getResponseCode()).toString() + "" + BillingClient.BillingResponseCode.OK)
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    val purchasesResult: Purchase.PurchasesResult
                    if (iapName != null && iapName!!.trim { it <= ' ' } == "lifetime") {
                        purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                        Log.d("queryPurchases", (billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)).toString() + "   , mmm " + purchasesResult.purchasesList + " , " + purchasesResult.responseCode)
                    } else
                        purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.SUBS)

                    try {
                        refreshPurchaseList(context, iapName)
                        launchIAP(iapType, iapName)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

    }

    fun refreshPurchaseList(context: Context, iapName: String?) {
        val purchasesResult: Purchase.PurchasesResult
        Log.d("iappurchased", "one " + iapName!!)
        if (iapName!!.trim { it <= ' ' } == "lifetime") {
            purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
            val purchasedList = purchasesResult.purchasesList
            Log.d("iappurchased", "two " + iapName!!)
            Log.d("billingresposnseokayref", "purchase list : " + purchasedList + " purchase result : " + purchasesResult.responseCode + " ,sku : ")

            if (purchasedList.isEmpty()) {
                Log.d("purchasedListepmty", "one " + iapName!!)
                sharedPreferences.edit().putBoolean("purchased", false).apply()
            }

            for (purchase in purchasedList) {
                if (purchase.sku == context.getString(R.string.premium_sku)) {
                    handlePurchase(purchase, "premiumIAP")

                    //isPurchased=true;
                    // handlePurchase(purchase);
                    /* purchased=true;
                     sharedPreferences.edit().putBoolean("purchased",purchased).apply();*/
                }
            }

        } else if (iapName!!.trim { it <= ' ' } == "6month") {
            purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.SUBS)
            val purchasedList = purchasesResult.purchasesList
            Log.d("iappurchased", "three " + iapName!!)
            Log.d("billingresposnseokayref", "purchase list : " + purchasedList + " purchase result : " + purchasesResult.responseCode + " ,sku : ")

            if (purchasedList.isEmpty()) {
                Log.d("purchasedListepmty", "sixmonth ")
                sharedPreferences.edit().putBoolean("sixMonthSubscribed", false).apply()
            }

            for (purchase in purchasedList) {
                if (purchase.sku == context.getString(R.string.premium_sub_sixmonth)) {
                    handlePurchase(purchase, "sixmonth")

                    //isPurchased=true;
                    // handlePurchase(purchase);
                    /* purchased=true;
                     sharedPreferences.edit().putBoolean("purchased",purchased).apply();*/
                } else {
                    Log.d("nomoresubscirption", "sixmonth")
                    sharedPreferences.edit().putBoolean("sixMonthSubscribed", false).apply()
                }
            }

        } else {
            purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.SUBS)
            val purchasedList = purchasesResult.purchasesList
            Log.d("iappurchased", "four " + iapName!!)
            Log.d("billingresposnseokayref", "purchase list : " + purchasedList + " purchase result : " + purchasesResult.responseCode + " ,sku : ")

            if (purchasedList.isEmpty()) {
                Log.d("purchasedListepmty", "monthly ")
                sharedPreferences.edit().putBoolean("monthlySubscribed", false).apply()
            }

            for (purchase in purchasedList) {
                if (purchase.sku == context.getString(R.string.premium_sub_monthly)) {
                    handlePurchase(purchase, "monthly")


                    //isPurchased=true;
                    // handlePurchase(purchase);
                    /* purchased=true;
                     sharedPreferences.edit().putBoolean("purchased",purchased).apply();*/
                } else {
                    Log.d("nomoresubscirption", "monthly")
                    sharedPreferences.edit().putBoolean("monthlySubscribed", false).apply()
                }
            }

        }


    }

    fun launchIAP(iapType: String, iapName: String?) {
        Log.d("billingclient", "not ready")
        if (billingClient!!.isReady) {
            Log.d("billingclient", "ready")
            val skuList = ArrayList<String>()
            skuList.add(iapType)
            val params = SkuDetailsParams.newBuilder()
            if (iapName != null && iapName!!.trim { it <= ' ' } == "lifetime")
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            else
                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

            billingClient!!.querySkuDetailsAsync(params.build()
            ) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    for (skuDetails in skuDetailsList!!) {
                        val sku = skuDetails.sku
                        val price = skuDetails.price
                        Log.d("billingclient", iapType + " values " + skuDetails.sku)
                        /*if (mContext.getString(R.string.premium_sku).equals(sku)) {
                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(skuDetails)
                                                    .build();

                                            Log.d("responseCode1"," response code : "+ billingClient.launchBillingFlow(activity,flowParams));
                                        }*/
                        if (iapType == sku) {
                            val flowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()

                            Log.d("responseCode1", " response code : " + billingClient!!.launchBillingFlow(activity, flowParams))
                        }
                    }
                }
            }
        }

    }

    interface PriceListener {
        fun gotPrice(price: String)
    }

    fun getPurchaseDetails(premiumSku: String, priceListener: PriceListener, iapName: String?): String {
        val price = arrayOf("")

        Log.d("billingclient", "not ready")
        if (billingClient!!.isReady) {
            Log.d("billingclient", "ready")
            val skuList = ArrayList<String>()
            skuList.add(premiumSku)
            val params = SkuDetailsParams.newBuilder()
            if (iapName != null && iapName!!.trim { it <= ' ' } == "lifetime")
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            else
                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

            billingClient!!.querySkuDetailsAsync(params.build(),
                    object : SkuDetailsResponseListener {
                        override fun onSkuDetailsResponse(billingResult: BillingResult, skuDetailsList: List<SkuDetails>?) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                for (skuDetails in skuDetailsList!!) {
                                    val sku = skuDetails.sku

                                    price[0] = skuDetails.price
                                    val p = price[0]
                                    priceListener.gotPrice(p)
                                    Log.d("billingclient", p + " price : " + price[0])
                                    if (premiumSku == sku) {

                                    }

                                }

                            }
                        }
                    })
        }
        Log.d("billingclient", " price == " + price[0])
        return price[0]
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (purchases != null)
            for (purchase in purchases) {

                if (purchase.sku == mContext.getString(R.string.premium_sku)) {
                    handlePurchase(purchase, "premiumIAP")
                    //isPurchased=true;
                    // handlePurchase(purchase);
                    /* purchased=true;
                     sharedPreferences.edit().putBoolean("purchased",purchased).apply();*/
                }
                if (purchase.sku == mContext.getString(R.string.premium_sub_monthly)) {
                    handlePurchase(purchase, "monthly")
                    //isPurchased=true;
                    // handlePurchase(purchase);
                    /* purchased=true;
                     sharedPreferences.edit().putBoolean("purchased",purchased).apply();*/
                }
                if (purchase.sku == mContext.getString(R.string.premium_sub_sixmonth)) {
                    handlePurchase(purchase, "sixmonth")
                    //isPurchased=true;
                    // handlePurchase(purchase);
                    /* purchased=true;
                     sharedPreferences.edit().putBoolean("purchased",purchased).apply();*/
                }
            }
        Log.d("billingclient", "purchase updated ")
    }
}
