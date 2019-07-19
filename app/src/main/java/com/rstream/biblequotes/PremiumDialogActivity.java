package com.rstream.biblequotes;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PremiumDialogActivity extends Dialog implements PurchasesUpdatedListener {


    private BillingClient billingClient;


    public PremiumDialogActivity(Activity context) {
        super(context);
       // this.swipeQuoteAdapter=swipeQuoteAdapter;
        this.context=context;
        sharedPreferences = context.getSharedPreferences("prefs.xml",MODE_PRIVATE);
    }
   // SwipeQuoteAdapter swipeQuoteAdapter;
    TextView priceTextView;
    SharedPreferences sharedPreferences;
    Activity context;


    SkuDetails skuDetails;
    SwipeQuoteAdapter swipeQuoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_premium_dialog);

        priceTextView = findViewById(R.id.MoneyTextView);
        //swipeQuoteAdapter = new SwipeQuoteAdapter()

        getPurchaseDetails();

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //purchase(skuDetails);
                //swipeQuoteAdapter.launchIAP();
                launchIAP();
                PremiumDialogActivity.this.dismiss();
            }
        });

    }

    public void initializeBillingClient(){
        /*billingClient = BillingClient.newBuilder(getContext()).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();
                    setBillingClient(billingClient);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });*/

        billingClient = BillingClient.newBuilder(getContext()).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();
                    setBillingClient(billingClient);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void getPurchaseDetails(){
        if (billingClient.isReady()){
            Log.d("billingresponseis","ready");
            List<String> skuList = new ArrayList<>();
            skuList.add(getContext().getString(R.string.premium_sku));
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            Log.d("billingresponseis",billingResult.getResponseCode()+" , "+skuDetailsList);
                            // Process the result.
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    priceTextView.setText(price);
                                    Log.d("billingresponseisare",skuDetails+" , "+price+" , "+skuDetails.getPrice()+" , "+skuDetails.getSku());
                                    if (getContext().getString(R.string.premium_sku).equals(sku)) {


                                        PremiumDialogActivity.this.skuDetails = skuDetails;

                                    }
                                }
                            }
                        }
                       /* public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (responseCode == BillingClient.BillingResponse.OK
                                    && skuDetailsList != null) {
                                for (SkuDetails
                                        skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    priceTextView.setText(price);
                                    if (getContext().getString(R.string.premium_sku).equals(sku)) {


                                        PremiumDialogActivity.this.skuDetails = skuDetails;

                                    }
                                }

                            }
                        }*/
                    });
        }
    }

    public void settingBillingClient(final TextView textView){
        billingClient = BillingClient.newBuilder(getContext()).enablePendingPurchases().setListener(this).build();
       /* billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();
                    getPrice(textView);
                   // setBillingClient(billingClient);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });*/

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();
                    getPrice(textView);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    public void getPrice(final TextView textView){
        if (billingClient.isReady()){
            Log.d("thepricevalis","ready");
            List<String> skuList = new ArrayList<>();
            skuList.add(getContext().getString(R.string.premium_sku));
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override

                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    textView.setText(price);
                                    if (getContext().getString(R.string.premium_sku).equals(sku)) {


                                        PremiumDialogActivity.this.skuDetails = skuDetails;

                                    }
                                }
                            }
                        }

                       /* public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (responseCode == BillingClient.BillingResponse.OK
                                    && skuDetailsList != null) {
                                for (SkuDetails
                                        skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    textView.setText(price);
                                    Log.d("thepricevalis",price);
                                    if (getContext().getString(R.string.premium_sku).equals(sku)) {


                                        PremiumDialogActivity.this.skuDetails = skuDetails;

                                    }
                                }

                            }
                        }*/
                    });

        }
        else
            Log.d("thepricevalis","not ready");
    }

    public void callIap(){
        Log.d("getpremiumiscalled","after");
        billingClient = BillingClient.newBuilder(getContext()).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();

                    launchIAP();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

       /* billingClient = BillingClient.newBuilder(getContext()).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();

                    launchIAP();
                    // setBillingClient(billingClient);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });*/
    }

    public void launchIAP() {
        if (billingClient.isReady()){
            Log.d("getpremiumiscalled","launchiap");
            List<String> skuList = new ArrayList<> ();
            skuList.add(getContext().getString(R.string.premium_sku));
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                    && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    Log.d("getpremiumiscalled","sku price "+ skuDetails.getPrice() +" , "+skuDetails.getSku()+" , "+skuDetails);
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    if (getContext().getString(R.string.premium_sku).equals(sku)) {

                                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(skuDetails)
                                                .build();
                                         billingClient.launchBillingFlow(context,flowParams);
                                        //int responseCode1 = billingClient.launchBillingFlow(getOwnerActivity(), flowParams);

                                    }
                                }

                            }
                        }
                    });
        }
    }

    public void purchase(SkuDetails skuDetails) {
    Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
         billingClient.launchBillingFlow(context, flowParams);
    }

    private void refreshPurchaseList() {
        Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        List<Purchase> purchasedList = purchasesResult.getPurchasesList();
        Log.d("getpremiumiscalled","purchase result is :"+purchasedList);
        for(Purchase purchase: purchasedList){
            Log.d("getpremiumiscalled","purchase is :"+purchase);
            if (purchase.getSku().equals(getContext().getString(R.string.premium_sku)))
            {
                Boolean purchased=true;
                sharedPreferences.edit().putBoolean("purchased",purchased).apply();
            }

        }
    }

    public void setBillingClient(BillingClient billingClient) {
        this.billingClient = billingClient;
       // getPrice();
    }

  /*  @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        refreshPurchaseList();
    }*/

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        refreshPurchaseList();
    }
}
