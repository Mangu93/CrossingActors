package com.mangu.crossingactors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mangu.crossingactors.Model.Cast;
import com.mangu.crossingactors.Utils.UrlFactory;
import com.mangu.crossingactors.Utils.UtilsFactory;
import com.mangu.crossingactors.Views.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressWarnings("unchecked")
public class ComparationDone extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    @BindView(R.id.tv_been_together)
    TextView tvBeenTogether;

    MovieAdapter mv;
    @BindView(R.id.listview)
    ListView listview;
    private static final int ID_ERASE = 1;
    @BindView(R.id.comparation_done_layout)
    LinearLayout comparationDoneLayout;
    private QuickAction quickAction;
    private Intent destination_url;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparation_done);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        QuickAction.setDefaultColor(ResourcesCompat.getColor(getResources(), R.color.teal, null));
        QuickAction.setDefaultTextColor(Color.BLACK);
        ActionItem eraseItem = new ActionItem(ID_ERASE, "Look up", R.drawable.ic_search_black_24dp);
        quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
        quickAction.setColorRes(R.color.background);
        quickAction.setTextColorRes(R.color.black);
        quickAction.addActionItem(eraseItem);
        quickAction.setOnActionItemClickListener(item -> startActivity(destination_url));
        ArrayList<String> results = (ArrayList<String>) getIntent().getSerializableExtra(Cast.class.getName());
        mv = new MovieAdapter(this, results);
        listview.setAdapter(mv);
        if (results.size() == 0) {
            tvBeenTogether.setText(getString(R.string.retry));
            listview.setVisibility(View.INVISIBLE);
            Button retryBtn = UtilsFactory.createRetryButton(this);
            retryBtn.setOnClickListener(view -> {
                Intent goBack = new Intent();
                goBack.putExtra("reload", "reload");
                setResult(Activity.RESULT_FIRST_USER, goBack);
                finish();
            });
            comparationDoneLayout.addView(retryBtn);
        }
        listview.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        destination_url = UrlFactory.generateBrowserIntent(this.mv.getItem(i));
        quickAction.show(view);
        return true;
    }
}
