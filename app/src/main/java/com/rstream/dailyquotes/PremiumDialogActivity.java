package com.rstream.dailyquotes;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class PremiumDialogActivity extends Dialog {


    private BillingClient billingClient;


    public PremiumDialogActivity(Activity context, SwipeQuoteAdapter swipeQuoteAdapter) {
        super(context);
        this.swipeQuoteAdapter=swipeQuoteAdapter;
    }
    SwipeQuoteAdapter swipeQuoteAdapter;
    TextView priceTextView;

    SkuDetails skuDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_premium_dialog);

        priceTextView = findViewById(R.id.MoneyTextView);


        getPurchaseDetails();

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //purchase(skuDetails);
                swipeQuoteAdapter.launchIAP();
                PremiumDialogActivity.this.dismiss();
            }
        });

    }

    private void getPurchaseDetails(){


        if (billingClient.isReady()){

            List<String> skuList = new ArrayList<>();
            skuList.add(getContext().getString(R.string.premium_sku));
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (responseCode == BillingClient.BillingResponse.OK
                                    && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    priceTextView.setText(price);
                                    if (getContext().getString(R.string.premium_sku).equals(sku)) {


                                        PremiumDialogActivity.this.skuDetails = skuDetails;

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
        int responseCode1 = billingClient.launchBillingFlow(getOwnerActivity(), flowParams);
    }

    public void setBillingClient(BillingClient billingClient) {
        this.billingClient = billingClient;
    }
}
