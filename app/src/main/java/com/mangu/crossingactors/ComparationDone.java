package com.mangu.crossingactors;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mangu.crossingactors.Model.Cast;
import com.mangu.crossingactors.Views.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class ComparationDone extends AppCompatActivity {

    @BindView(R.id.tv_been_together)
    TextView tvBeenTogether;

    MovieAdapter mv;
    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparation_done);
        ButterKnife.bind(this);
        ArrayList<String> results = (ArrayList<String>) getIntent().getSerializableExtra(Cast.class.getName());
        mv = new MovieAdapter(this, results);
        listview.setAdapter(mv);
        if(results.size() == 0) {
            tvBeenTogether.setText("Sorry, these actors have no movies in common. \n");
        }
        listview.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Snackbar.make(adapterView.getRootView(), "TEST", Snackbar.LENGTH_SHORT).show();
            return true;
        });
    }
}
