package com.example.quotes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quotes.QuotesNames;
import com.example.quotes.QuotesTypes;
import com.example.quotes.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    boolean pass=true;
    String url="";
    String jsonurl="";
    int flag=0;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;

    ArrayList<QuotesNames> quotesNames;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);




        try{
            Log.d("in oncreate","yes");
              /*  recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                quotesNames = new ArrayList<>();

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            final QuotesTypes quotesTypes = new QuotesTypes(getActivity());
            recyclerView.setAdapter(quotesTypes);
*/
           /* SharedPreferences preferences = getActivity().getSharedPreferences("prefs.xml", MODE_PRIVATE);
            String js=preferences.getString("jsonval","abc");
            flag=preferences.getInt("flag",2);
            Log.d("before click",js);*/

           // Toast.makeText(getActivity(), "flag:"+flag, Toast.LENGTH_SHORT).show();
           /* if (flag==1){

                getQuotesImages(js);
            }
            else{
                url="http://52.91.243.194/RIA/set/setgrid.php?type=category&page=moving-light&country=us&lang=en";
                DownloadQuote getQuote = new DownloadQuote();
                getQuote.execute(url);
            }*/

        }
        catch (Exception ee){
            Log.d("recycle error","here, "+ee.getMessage());
            ee.printStackTrace();
        }



        return rootView;

    }



  /*  public class DownloadQuote extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            Log.d("somethings","here");
            try{
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs.xml",MODE_PRIVATE).edit();
                editor.putString("jsonval",s);
                editor.putInt("flag",1);
                editor.apply();

                getQuotesImages(s);

            }
            catch (Exception es){
                Log.d("adaptor error","here "+es.getMessage());
                es.printStackTrace();
            }

            Log.d("Data","here");
            jsonurl=s;
            // getQuotesImages(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data!= -1){
                    char current = (char) data;
                    result+=current;
                    data= reader.read();
                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();

                return null;

            }
        }
    }

    public void getQuotesImages(String s){
        try{

            JSONObject jsonObject = new JSONObject(s);
            String category = jsonObject.getString("category");
            if (category!=null){
                Log.d("json parsed","done");
                JSONArray arr = new JSONArray(category);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonpart = arr.getJSONObject(i);
                    //quotesNames.add(new QuotesNames(jsonpart.getString("author")+"",jsonpart.getString("url")) );
                    Log.d("Author "+i,jsonpart.getString("author"));
                    Log.d("Image "+i,jsonpart.getString("url"));
                    //homeFragment.setQuotesNames(quotesNames);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
*/
}
