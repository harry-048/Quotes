package com.rstream.biblequotes


import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.core.os.HandlerCompat.postDelayed




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
        val getPremiumLater = view.findViewById<TextView>(R.id.getPremiumLater)
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
        val PremiumText = view.findViewById<TextView>(R.id.premiumTextView)
        val sixMonthText = view.findViewById<TextView>(R.id.sixmonthTextView)
        val sevenDayText = view.findViewById<TextView>(R.id.sevendayTextView)
        val getPremium = GetPremium(context,activity)

        var iapName = "lifetime"
        var iapType = ""

        getPremium.getPrice(context,"monthly", getString(R.string.premium_sub_monthly)) {
            Log.d("mothlyprice",it)
            sevenDayPrice.text = it
        }

        getPremium.getPrice(context,"6month", getString(R.string.premium_sub_sixmonth)) {
            Log.d("mothlyprice",it)
            sixMonthPrice.text = it
        }

        getPremium.getPrice(context,"lifetime", getString(R.string.premium_sku)) {
            val d = it.split(it[1])[1].toDouble().times(2).toString()
            val doublePrice = "${it[0]}${it[1]}${d.split(".")[0]}.${if (d.split(".")[1].toInt() == 0) "00" else d.split(".")[1]}"
            Log.d("totalprice","$it new price is $doublePrice")
            doubletimePrice.text = doublePrice
            doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off_white, context?.theme)
            lifeTimePrice.text = it
        }

        //monthlyPrice.text = MainActivity.

        premiumDialogActivity.settingBillingClient(lifeTimePrice)
      //  premiumDialogActivity.getPrice(lifeTimePrice)
        purchaseCardView.setOnClickListener {
            Log.d("getpremiumiscalled","before")
            iapName = "lifetime"
            doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off_white, context?.theme)
          //  purchaseCardView.setCardBackgroundColor(Color.)
            if (Build.VERSION.SDK_INT >= M) {
                purchaseCardView.setCardBackgroundColor(resources.getColor(R.color.Buttoncolor, context?.theme))
                sixMonthsCardView.setCardBackgroundColor(resources.getColor(R.color.Textselectcolor, context?.theme))
                sevendayCardView.setCardBackgroundColor(resources.getColor(R.color.Textselectcolor, context?.theme))

                lifeTimePrice.setTextColor(resources.getColor(R.color.PremiumValuecolor, context?.theme))
                doubletimePrice.setTextColor(resources.getColor(R.color.PremiumValuecolor, context?.theme))
                sixMonthPrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                sevenDayPrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                PremiumText.setTextColor(resources.getColor(R.color.Textselectcolor, context?.theme))
                sixMonthText.setTextColor(resources.getColor(R.color.TextDarkcolor, context?.theme))
                sevenDayText.setTextColor(resources.getColor(R.color.TextDarkcolor, context?.theme))
            }



            //premiumDialogActivity.callIap()
            //premiumDialogActivity.show()
        }

        sixMonthsCardView.setOnClickListener {
            iapName = "6month"
            iapType = getString(R.string.premium_sub_sixmonth)
            doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off, context?.theme)
            if (Build.VERSION.SDK_INT >= M) {
                purchaseCardView.setCardBackgroundColor(resources.getColor(R.color.Textselectcolor, context?.theme))
                sixMonthsCardView.setCardBackgroundColor(resources.getColor(R.color.Buttoncolor, context?.theme))
                sevendayCardView.setCardBackgroundColor(resources.getColor(R.color.Textselectcolor, context?.theme))

                lifeTimePrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                doubletimePrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                sixMonthPrice.setTextColor(resources.getColor(R.color.PremiumValuecolor, context?.theme))
                sevenDayPrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                PremiumText.setTextColor(resources.getColor(R.color.TextDarkcolor, context?.theme))
                sixMonthText.setTextColor(resources.getColor(R.color.Textselectcolor, context?.theme))
                sevenDayText.setTextColor(resources.getColor(R.color.TextDarkcolor, context?.theme))
            }
            //getPremium.callIAP(context,getString(R.string.premium_sub_sixmonth),"6month")
        }

        sevendayCardView.setOnClickListener {
            iapName = "monthly"
            iapType = getString(R.string.premium_sub_monthly)
            doubletimePrice.background = context?.resources?.getDrawable(R.drawable.strike_off, context?.theme)
            if (Build.VERSION.SDK_INT >= M) {
                purchaseCardView.setCardBackgroundColor(resources.getColor(R.color.Textselectcolor, context?.theme))
                sixMonthsCardView.setCardBackgroundColor(resources.getColor(R.color.Textselectcolor, context?.theme))
                sevendayCardView.setCardBackgroundColor(resources.getColor(R.color.Buttoncolor, context?.theme))

                lifeTimePrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                doubletimePrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                sixMonthPrice.setTextColor(resources.getColor(R.color.Textcolor, context?.theme))
                sevenDayPrice.setTextColor(resources.getColor(R.color.PremiumValuecolor, context?.theme))
                PremiumText.setTextColor(resources.getColor(R.color.TextDarkcolor, context?.theme))
                sixMonthText.setTextColor(resources.getColor(R.color.TextDarkcolor, context?.theme))
                sevenDayText.setTextColor(resources.getColor(R.color.Textselectcolor, context?.theme))
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
