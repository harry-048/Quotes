package com.rstream.biblequotes


import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

import androidx.navigation.findNavController
import android.text.SpannableStringBuilder
import android.text.Spannable
import android.text.style.StyleSpan
import androidx.core.content.res.ResourcesCompat
import android.text.SpannableString

import android.content.Context


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class First_Fragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_, container, false)





        val continueButton = view.findViewById<Button>(R.id.continueButton)
        val imageView = view.findViewById<ImageView>(R.id.bibleImageView)

       /* val link = "http://riafyme.com/app/quotes/happy/youre-prettiest-when-youre-happy-quote-1.jpg"

        context?.resources?.getDrawable(R.drawable.loadinganimation, context?.theme)?.let {
            Picasso.get().load(link).placeholder(
                it
            ).into(imageView)
        }*/

       /* val layoutParams = container?.width?.let { RelativeLayout.LayoutParams(it, it) }
        imageView.layoutParams = layoutParams*/


        val ss = SpannableString(context?.resources?.getString(R.string.privacyPolicyText))
        val privacypolicy = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Log.d("linkclick","$ss is clicked")
                //termsAndPrivacy = "privacy"
                activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_first_Fragment_to_termsAndPolicy)
                //startActivity(Intent(context, NextActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                context?.run {
                    ds.color = ContextCompat.getColor(this, R.color.PremiumTextColor)
                }
            }
        }

        ss.setSpan(privacypolicy, 29, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = view.findViewById(R.id.termaAndprivacyTextView) as TextView
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()

     /*   val quoteText = SpannableString(context?.resources?.getString(R.string.firstPageQuote))
        val quote = object : ClickableSpan() {
            override fun onClick(widget: View) {
            }


            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                context?.run {
                    ds.color = ContextCompat.getColor(this, R.color.PremiumTextColor)
                }
            }
        }*/

        val font = Typeface.createFromAsset(context?.assets, "roboto_medium.ttf")
        val quoteText = SpannableStringBuilder(context?.resources?.getString(R.string.firstPageQuote))
        quoteText.setSpan( StyleSpan(Typeface.BOLD), 10, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)  //  bible quotes
       // quoteText.setSpan( StyleSpan(Typeface.BOLD), 5, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // teen wallpaper
       //  quoteText.setSpan( StyleSpan(R.font.roboto_black), 10, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
       // val string = setCustomFontTypeSpan(context,"Walk with Jesus Christ everyday by reading his words",10,22,R.font.roboto_medium)



        val quoteTextView = view.findViewById(R.id.bibleTextView) as TextView
        quoteTextView.text=quoteText
            /*.bold { append(id) }
            .append(name)*/


        continueButton.setOnClickListener {
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_first_Fragment_to_second)
        }

        return view


    }

    fun setCustomFontTypeSpan(context: Context, source: String, startIndex: Int, endIndex: Int, font: Int): SpannableString {
        val spannableString = SpannableString(source)
        val typeface = ResourcesCompat.getFont(context, font)
        spannableString.setSpan(StyleSpan(typeface!!.style),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

}
