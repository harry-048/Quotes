package com.rstream.dailyquotes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rstream.dailyquotes.MainActivity;
import com.rstream.dailyquotes.R;

import java.util.ArrayList;


public class SearchFragment extends Fragment {



    public void setListItems(ArrayList<String> listItems) {
        this.listItems = listItems;
    }

    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        listItems=getArguments().getStringArrayList("quoteitems");
        listView = rootView.findViewById(R.id.quoteslistView);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).clickListView(position);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }




}
