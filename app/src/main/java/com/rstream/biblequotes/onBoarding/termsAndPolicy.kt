package com.rstream.biblequotes


import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import androidx.fragment.app.Fragment
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import com.google.android.material.snackbar.Snackbar






// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class termsAndPolicy : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_terms_and_policy, container, false)

        val webView = view.findViewById<WebView>(R.id.privacyandtermsWebview)

       // val link = "http://riafy.me/wellness/$termsAndPrivacy.php?appname=Walking workouts"

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        try{
            webView.loadUrl("http://cookbookrecipes.in/otherapps/videoFeeds/privacy_general/Privacy_Policy.php?appname=Bible%20Quotes&fbclid=IwAR0O-XIuzqz6qQ3I7cZoFLh0_eXJ-hTltGwMd32uQnM74e-wMa9tlucRKDo")

        }
        catch (e: Exception){
            Log.d("Exceptioncaught","exception is $e")
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                view: WebView, request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                // Do something
                Log.d("Exceptioncaught","there is an error $error")
                try{
                    Snackbar.make(view, "Check your internet connection", Snackbar.LENGTH_SHORT).show()
                }
                catch (e: Exception){
                    Log.d("Exceptioncaught","exception is $e")
                }


            }
        }

       // webView.loadUrl(link)

        return view
    }


}
