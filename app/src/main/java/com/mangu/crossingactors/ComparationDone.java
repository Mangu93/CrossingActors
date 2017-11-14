package com.mangu.crossingactors;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mangu.crossingactors.models.Cast;
import com.mangu.crossingactors.utils.ImageFactory;
import com.mangu.crossingactors.utils.UrlFactory;
import com.mangu.crossingactors.utils.UtilsFactory;
import com.mangu.crossingactors.views.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

import static com.mangu.crossingactors.utils.ComparatorFactory.MOVIE_POSTER_KEY;
import static com.mangu.crossingactors.utils.UtilsFactory.createZoomInToast;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class ComparationDone extends AppCompatActivity
        implements AdapterView.OnItemLongClickListener {

    private static final int ID_POSTER = 2;
    private static final int ID_SEARCH = 1;
    @SuppressWarnings("CanBeFinal")
    @BindView(R.id.tv_been_together)
    TextView tvBeenTogether;
    MovieAdapter mv;
    @SuppressWarnings("CanBeFinal")
    @BindView(R.id.listview)
    ListView listview;
    @SuppressWarnings("CanBeFinal")
    @BindView(R.id.comparation_done_layout)
    LinearLayout comparationDoneLayout;

    private QuickAction mQuickAction;
    private Intent mDestinationUrl;
    private String mPosterPath = "";
    private ArrayList<String> mPosters;
    private View mAnchorView;
    boolean isImageFitToScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparation_done);
        ButterKnife.bind(this);
        QuickAction.setDefaultColor(ResourcesCompat.getColor(getResources(), R.color.teal, null));
        QuickAction.setDefaultTextColor(Color.BLACK);
        prepareQuickAction();
        ArrayList<String> results =
                (ArrayList<String>) getIntent().getSerializableExtra(Cast.class.getName());
        mPosters = (ArrayList<String>) getIntent().getSerializableExtra(MOVIE_POSTER_KEY);
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
        Toast.makeText(this.getApplicationContext(), getString(R.string.long_tap),
                Toast.LENGTH_SHORT).show();
    }

    public void prepareQuickAction() {
        ActionItem eraseItem = new ActionItem(ID_SEARCH,
                "Look up", R.drawable.ic_search_black_24dp);
        ActionItem lookPoster = new ActionItem(ID_POSTER,
                "See poster", R.drawable.ic_search_black_24dp);
        mQuickAction = new QuickAction(this, QuickAction.HORIZONTAL);
        mQuickAction.setColorRes(R.color.background);
        mQuickAction.setTextColorRes(R.color.black);
        mQuickAction.addActionItem(eraseItem);
        mQuickAction.addActionItem(lookPoster);
        mQuickAction.setOnActionItemClickListener(item -> {
            if (item.getActionId() == ID_SEARCH) {
                startActivity(mDestinationUrl);
            } else if (item.getActionId() == ID_POSTER) {
                showPopup();
            }
        });
    }

    private void showPopup() {
        @SuppressLint("InflateParams") View popUpView = getLayoutInflater().
                inflate(R.layout.popup_image, null);
        PopupWindow popupWindow = new PopupWindow(popUpView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView ivPoster = (ImageView) popUpView.findViewById(R.id.iv_poster);

        Glide.with(this.getApplicationContext())
                .load(mPosterPath)
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        ivPoster.setImageBitmap(resource);
                    }
                });
        createZoomInToast(this.getApplicationContext(), true).show();
        ivPoster.setOnClickListener(view -> {
            if (isImageFitToScreen) {
                isImageFitToScreen = false;
                ivPoster.setLayoutParams(
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT));
                ivPoster.setAdjustViewBounds(true);
            } else {
                isImageFitToScreen = true;
                ivPoster.setLayoutParams(
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT));
                ivPoster.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            createZoomInToast(this.getApplicationContext(), !isImageFitToScreen).show();
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(mAnchorView, Gravity.CENTER, 0, 0);

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        mDestinationUrl = UrlFactory.generateBrowserIntent(this.mv.getItem(i));
        mPosterPath = ImageFactory.formUrlPoster(mPosters.get(i));
        mAnchorView = view;
        mQuickAction.show(view);
        return true;
    }
}
