package com.mangu.crossingactors;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mangu.crossingactors.Model.Cast;
import com.mangu.crossingactors.Utils.ImageFactory;
import com.mangu.crossingactors.Utils.UrlFactory;
import com.mangu.crossingactors.Utils.UtilsFactory;
import com.mangu.crossingactors.Views.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

import static com.mangu.crossingactors.Utils.ComparatorFactory.MOVIE_POSTER_KEY;

@SuppressWarnings("unchecked")
public class ComparationDone extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private static final int ID_POSTER = 2;
    private static final int ID_SEARCH = 1;
    @BindView(R.id.tv_been_together)
    TextView tvBeenTogether;
    MovieAdapter mv;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.comparation_done_layout)
    LinearLayout comparationDoneLayout;

    private QuickAction quickAction;
    private Intent destination_url;
    private String poster_path = "";
    private ArrayList<String> posters;
    private View anchor_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparation_done);
        ButterKnife.bind(this);
        QuickAction.setDefaultColor(ResourcesCompat.getColor(getResources(), R.color.teal, null));
        QuickAction.setDefaultTextColor(Color.BLACK);
        prepareQuickAction();
        ArrayList<String> results = (ArrayList<String>) getIntent().getSerializableExtra(Cast.class.getName());
        posters = (ArrayList<String>) getIntent().getSerializableExtra(MOVIE_POSTER_KEY);
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

    public void prepareQuickAction() {
        ActionItem eraseItem = new ActionItem(ID_SEARCH, "Look up", R.drawable.ic_search_black_24dp);
        ActionItem lookPoster = new ActionItem(ID_POSTER, "See poster", R.drawable.ic_search_black_24dp);
        quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
        quickAction.setColorRes(R.color.background);
        quickAction.setTextColorRes(R.color.black);
        quickAction.addActionItem(eraseItem);
        quickAction.addActionItem(lookPoster);
        quickAction.setOnActionItemClickListener(item -> {
            if (item.getActionId() == ID_SEARCH) {
                startActivity(destination_url);
            } else if (item.getActionId() == ID_POSTER) {
                showPopup();
            }
        });
    }

    private void showPopup() {
        View popUpView = getLayoutInflater().inflate(R.layout.popup_image, null);
        PopupWindow popupWindow = new PopupWindow(popUpView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView iv_poster = (ImageView) popUpView.findViewById(R.id.iv_poster);

        Glide.with(this.getApplicationContext())
                .load(poster_path)
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv_poster.setImageBitmap(resource);
                    }
                });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(anchor_view, Gravity.CENTER, 0, 0);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        destination_url = UrlFactory.generateBrowserIntent(this.mv.getItem(i));
        poster_path = ImageFactory.formUrlPoster(posters.get(i));
        anchor_view = view;
        quickAction.show(view);
        return true;
    }
}
