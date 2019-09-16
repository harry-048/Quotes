package com.rstream.biblequotes


import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.*
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.chips.view.*
import kotlinx.android.synthetic.main.fragment_second.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Second : Fragment() {

    internal var count = 0
    internal var sharedPreferences: SharedPreferences? = null

    //var nextButton :Button? = null
    internal var catregoryCountText: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second, container, false)

        sharedPreferences = context?.getSharedPreferences("prefs.xml", Context.MODE_PRIVATE)
        catregoryCountText = view.findViewById(R.id.catregoryCountText)
        count = sharedPreferences?.getInt("categoryCount",0)!!
       val nextButton = view.findViewById<View>(R.id.nextButton)
         val categoryList = view.findViewById<FlexboxLayout>(R.id.categoryList)

        nextButton.setOnClickListener {
            if (count >= 3) {
                activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_second_to_thirdFragment)
            } else {
                Toast.makeText(context, "Please select " + (3 - count) + " more", Toast.LENGTH_SHORT).show()
            }
           // activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_second_to_thirdFragment)
        }
        val parseJsonData = ParseJsonData(context)
        val categoryNamesList = parseJsonData.parseData()
        val noOfChips = categoryNamesList.size

        val chipsTextViews = arrayOfNulls<TextView>(noOfChips)
        val onclicklistener = View.OnClickListener{
            for (i in 0 until noOfChips){
                if (it==chipsTextViews[i]){
                    saveAndRemoveCategory(chipsTextViews[i]?.text.toString(),chipsTextViews[i])
                }
            }

        }
        var cat = sharedPreferences?.getString("category","")
        for (i in 0 until noOfChips){
            val viewGroup = ViewGroup.inflate(context,R.layout.chips,null)
            val textView = viewGroup.textView
            textView.text= categoryNamesList[i]
           /* when (i) {
                0 -> textView.text = "BELIEVE"
                1 -> textView.text = "LOVE"
                2 -> textView.text = "MOTIVATIONAL"
                3 -> textView.text = "LONLINESS"
                4 -> textView.text = "HEALING PAIN"
            }
*/
            categoryList.addView(textView)
            chipsTextViews[i] = textView

            var c = sharedPreferences?.getInt("categoryCount",0)
            if (c != null) {
                if (c>0){
                    Log.d("categorylist", "selected: $cat c=$c")
                    @TargetApi(Build.VERSION_CODES.M)
                    if(cat?.trim()?.contains(chipsTextViews[i]?.text.toString())!!){
                        Log.d("categorylist", "selectedinside: $cat")
                        textView?.setTextColor(resources.getColor(R.color.Textselectcolor,context?.theme))
                        textView?.background = resources.getDrawable(R.drawable.rounded_textview_red,context?.theme)
                    }

                    when {
                        c >= 3 -> {
                            catregoryCountText?.visibility = View.INVISIBLE
                            nextButton?.background=resources.getDrawable(R.drawable.rounded_button,context?.theme)
                        }
                        c==0 -> {
                            catregoryCountText?.visibility = View.VISIBLE
                            catregoryCountText?.text = "Select atleast 3 collections from above"
                            nextButton?.background=resources.getDrawable(R.drawable.round_button,context?.theme)
                        }
                        else -> {
                            catregoryCountText?.visibility = View.VISIBLE
                            catregoryCountText?.text = "Please select " + (3 - c) + " more"
                            nextButton?.background=resources.getDrawable(R.drawable.round_button,context?.theme)
                        }
                    }
                }
            }
/*
            @TargetApi(Build.VERSION_CODES.M)
            if(cat?.trim()?.contains(chipsTextViews[i]?.text.toString())!!){
                chipsTextViews[i]?.setTextColor(resources.getColor(R.color.Textcolor,context?.theme))
                chipsTextViews[i]?.background = resources.getDrawable(R.drawable.rounded_textview,context?.theme)
            }*/
            chipsTextViews[i]?.setOnClickListener(onclicklistener)
        }
        return view
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun saveAndRemoveCategory(categoryName: String, textView: TextView?){
        var category = sharedPreferences?.getString("category","")
        //count = sharedPreferences?.getInt("categoryCount",0)
        Log.d("categorylist", "before $category")
        if(category?.trim()?.contains(categoryName)!!){
            count--
            textView?.setTextColor(resources.getColor(R.color.Textcolor,context?.theme))
            textView?.background = resources.getDrawable(R.drawable.rounded_textview,context?.theme)
            category=category.replace("$categoryName , ", "")
        }
        else{
            count++
            textView?.setTextColor(resources.getColor(R.color.Textselectcolor,context?.theme))
            textView?.background = resources.getDrawable(R.drawable.rounded_textview_red,context?.theme)
            category = "$category$categoryName , "
        }

        when {
            count >= 3 -> {
                catregoryCountText?.visibility = View.INVISIBLE
                nextButton?.background=resources.getDrawable(R.drawable.rounded_button,context?.theme)
            }
            count==0 -> {
                catregoryCountText?.visibility = View.VISIBLE
                catregoryCountText?.text = "Select atleast 3 collections from above"
                nextButton?.background=resources.getDrawable(R.drawable.round_button,context?.theme)
            }
            else -> {
                catregoryCountText?.visibility = View.VISIBLE
                catregoryCountText?.text = "Please select " + (3 - count) + " more"
                nextButton?.background=resources.getDrawable(R.drawable.round_button,context?.theme)
            }
        }
        sharedPreferences?.edit()?.putInt("categoryCount", count)?.apply()
        sharedPreferences?.edit()?.putString("category", category)?.apply()
        Log.d("categorylist", "after $category")

    }


}
