package com.rstream.teenwallpapers


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val premiumDialogActivity = PremiumDialogActivity(activity)
        val getPremiumLater = view.findViewById<TextView>(R.id.getPremiumLater)
        val ss = SpannableString("Get premium later")
        val cancelPremium = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Log.d("linkitemclick", "$ss is clicked")


                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                context?.run {
                    ds.color = ContextCompat.getColor(this, R.color.PremiumTextColor)
                }
            }
        }

        ss.setSpan(cancelPremium, 0, ss.length, 0)

        getPremiumLater.text = ss
        getPremiumLater.movementMethod = LinkMovementMethod.getInstance()

        val handler = Handler()
        handler.postDelayed({
            getPremiumLater.visibility = View.VISIBLE
        }, 5000)

        val nextButton = view.findViewById<Button>(R.id.nextButton)
        val purchaseCardView = view.findViewById<CardView>(R.id.purchaseCardView)
        val sixMonthsCardView = view.findViewById<CardView>(R.id.sixMonthsCardView)
        val sevendayCardView = view.findViewById<CardView>(R.id.sevendayCardView)
        val lifeTimePrice = view.findViewById<TextView>(R.id.lifeTimePrice)
        val doubletimePrice = view.findViewById<TextView>(R.id.doublelifeTimePrice)
        val sixMonthPrice = view.findViewById<TextView>(R.id.sixMonthPrice)
        val sevenDayPrice = view.findViewById<TextView>(R.id.sevenDayPrice)
        val premiumText = view.findViewById<TextView>(R.id.premiumTextView)
        val sixMonthText = view.findViewById<TextView>(R.id.sixmonthTextView)
        val sevenDayText = view.findViewById<TextView>(R.id.sevendayTextView)
        val getPremium = GetPremium(context,activity)





        val animation = AnimationUtils.loadAnimation(context, R.anim.button_animation)
        nextButton.startAnimation(animation)

        var iapName = "monthly"
        var iapType = getString(R.string.premium_sub_monthly)

        getPremium.getPrice(context,"monthly", getString(R.string.premium_sub_monthly)) {
            Log.d("mothlyprice",it)
            sevenDayPrice.text = "Cancel any time. $it / 6 month"
        }

        getPremium.getPrice(context,"6month", getString(R.string.premium_sub_sixmonth)) {
            Log.d("mothlyprice",it)
            sixMonthPrice.text = "$it /month"
        }

        getPremium.getSubsPeriod(context,"monthly", getString(R.string.premium_sub_monthly)) {
            Log.d("thetimegot","subscription period : $it")
            if (it=="P1M") {
              //  document.querySelector("#opt-6month .subscDetails").innerHTML= document.querySelector("#opt-6month .subscDetails").innerHTML.replace("6 month","1 month");
                getPremium.getPrice(context,"monthly", getString(R.string.premium_sub_monthly)) {
                    sevenDayPrice.text = "Cancel any time. $it / 1 month"
                }
            }
            else if(it=="P1W")
            {
               // document.querySelector("#opt-6month .subscDetails").innerHTML= document.querySelector("#opt-6month .subscDetails").innerHTML.replace("6 month","1 week");
                getPremium.getPrice(context,"monthly", getString(R.string.premium_sub_monthly)) {
                    sevenDayPrice.text = "Cancel any time. $it / 1 week"
                }
            }

        }

        getPremium.getSubsPeriod(context,"6month", getString(R.string.premium_sub_sixmonth)) {
            if (it=="P1M") {
                sixMonthText.text= "MONTHLY"
            }
            else if(it=="P1W")
            {
                sixMonthText.text= "WEEKLY"
            }
            Log.d("thetimegot","subs period : $it")
        }

        getPremium.getTrialPeriod(context,"monthly", getString(R.string.premium_sub_monthly)) {
            Log.d("thetimegot","trial period : $it")
            if (it=="P1W") {
                sevenDayText.text= "7 DAY FREE TRIAL"

            }
            else if(it=="P3D")
            {
                sevenDayText.text= "3 DAY FREE TRIAL"
            }
            else{
                sevenDayText.text= "GET PREMIUM"
            }
        }




        getPremium.getPrice(context,"lifetime", getString(R.string.premium_sku)) {
            val d = it.split(it[1])[1].toDouble().times(2).toString()
            val doublePrice = "${it[0]}${it[1]}${d.split(".")[0]}.${if (d.split(".")[1].toInt() == 0) "00" else d.split(".")[1]}"
            Log.d("totalprice","$it new price is $doublePrice")
            doubletimePrice.text = doublePrice
            doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off, context?.theme)
            lifeTimePrice.text = it
        }

        //monthlyPrice.text = MainActivity.

        premiumDialogActivity.settingBillingClient(lifeTimePrice)
      //  premiumDialogActivity.getPrice(lifeTimePrice)
        purchaseCardView.setOnClickListener {
            Log.d("getpremiumiscalled","before")
            iapName = "lifetime"
          //  doubletimePrice.foreground = context?.resources?.getDrawable(R.drawable.strike_off, context?.theme)
            context?.run{
                purchaseCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Buttoncolor))
                sixMonthsCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Textselectcolor))
                sevendayCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Textselectcolor))

                lifeTimePrice.setTextColor(ContextCompat.getColor(this,R.color.PremiumValuecolor))
                doubletimePrice.setTextColor(ContextCompat.getColor(this,R.color.PremiumValuecolor))
                sixMonthPrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                sevenDayPrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                premiumText.setTextColor(ContextCompat.getColor(this,R.color.Textselectcolor))
                sixMonthText.setTextColor(ContextCompat.getColor(this,R.color.TextDarkcolor))
                sevenDayText.setTextColor(ContextCompat.getColor(this,R.color.TextDarkcolor))
            }

            //premiumDialogActivity.callIap()
            //premiumDialogActivity.show()
        }

        sixMonthsCardView.setOnClickListener {
            iapName = "6month"
            iapType = getString(R.string.premium_sub_sixmonth)
           // doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off, context?.theme)
            context?.run{
                purchaseCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Textselectcolor))
                sixMonthsCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Buttoncolor))
                sevendayCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Textselectcolor))

                lifeTimePrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                doubletimePrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                sixMonthPrice.setTextColor(ContextCompat.getColor(this,R.color.PremiumValuecolor))
                sevenDayPrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                premiumText.setTextColor(ContextCompat.getColor(this,R.color.TextDarkcolor))
                sixMonthText.setTextColor(ContextCompat.getColor(this,R.color.Textselectcolor))
                sevenDayText.setTextColor(ContextCompat.getColor(this,R.color.TextDarkcolor))
            }

            //getPremium.callIAP(context,getString(R.string.premium_sub_sixmonth),"6month")
        }

        sevendayCardView.setOnClickListener {
            iapName = "monthly"
            iapType = getString(R.string.premium_sub_monthly)
          //  doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off, context?.theme)

            context?.run{
                purchaseCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Textselectcolor))
                sixMonthsCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Textselectcolor))
                sevendayCardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.Buttoncolor))

                lifeTimePrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                doubletimePrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                sixMonthPrice.setTextColor(ContextCompat.getColor(this,R.color.Textcolor))
                sevenDayPrice.setTextColor(ContextCompat.getColor(this,R.color.PremiumValuecolor))
                premiumText.setTextColor(ContextCompat.getColor(this,R.color.TextDarkcolor))
                sixMonthText.setTextColor(ContextCompat.getColor(this,R.color.TextDarkcolor))
                sevenDayText.setTextColor(ContextCompat.getColor(this,R.color.Textselectcolor))
            }


           // getPremium.callIAP(context,getString(R.string.premium_sub_monthly),"monthly")
        }

        nextButton.setOnClickListener {
            if (iapName == "lifetime")
                premiumDialogActivity.callIap()
            else
                getPremium.callIAP(context,iapType,iapName)
        }

        // Inflate the layout for this fragment
        return view
    }


}
