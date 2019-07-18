package com.rstream.biblequotes


import android.content.Intent
import android.os.Bundle
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
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController


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

        val nextButton = view.findViewById<Button>(R.id.nextButton)
        val sevenDayTrial = view.findViewById<Button>(R.id.nextButton)
        val monthly = view.findViewById<Button>(R.id.nextButton)
        val lifetime = view.findViewById<Button>(R.id.nextButton)


        // Inflate the layout for this fragment
        return view
    }


}
