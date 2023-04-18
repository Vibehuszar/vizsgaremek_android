package hu.petrik.gorillago_android.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import hu.petrik.gorillago_android.R;


public class SearchFragment extends Fragment {

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform a search with the query string here
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update the search results as the user types here
                return true;
            }
        });
        return view;
    }
    public void init(View view){
        searchView = view.findViewById(R.id.searchView);


    }
}