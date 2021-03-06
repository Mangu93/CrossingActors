package com.mangu.crossingactors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.mangu.crossingactors.models.ActorListResponse;
import com.mangu.crossingactors.models.Cast;
import com.mangu.crossingactors.models.Credits;
import com.mangu.crossingactors.models.Result;
import com.mangu.crossingactors.networking.DataManager;
import com.mangu.crossingactors.networking.services.ActorService;
import com.mangu.crossingactors.utils.ComparatorFactory;
import com.mangu.crossingactors.utils.scheduler.SchedulerUtils;
import com.mangu.crossingactors.views.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static com.mangu.crossingactors.networking.services.ActorServiceFactory.makeActorService;
import static com.mangu.crossingactors.utils.ComparatorFactory.MOVIE_POSTER_KEY;
import static com.mangu.crossingactors.utils.ModelFactory.joinCredits;
import static com.mangu.crossingactors.utils.UtilsFactory.START_MAIN_ACTIVITY_FROM_COMPARATION;

public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_QUERY_UPDATE = 5612;
    private static final int QUERY_UPDATE_DELAY_MILLIS = 100;
    private static final int DELAY_MILLIS = 5000;
    private static final ActorService myActorFactory = makeActorService();
    private static final ComparatorFactory.CoincidenceMap coincidenceMap
            = new ComparatorFactory.CoincidenceMap();
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @BindView(R.id.btn_search)
    Button btnSearch;
    private SearchAdapter mSearchAdapter;
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @BindView(R.id.pb_list_loading)
    ProgressBar pbListLoading;
    private final ArrayList<Result> mActors = new ArrayList<>();
    @SuppressWarnings("CanBeFinal")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_QUERY_UPDATE) {
                String query = (String) msg.obj;
                DataManager dm = new DataManager(myActorFactory);
                Observable<ActorListResponse> listActors = dm.getActorList(query);
                listActors.compose(SchedulerUtils.ioToMain())
                        .subscribe(actorListResponse -> showActors(actorListResponse.results),
                                throwable ->
                                        Log.e("ObservableError", throwable.getLocalizedMessage()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(
                getApplicationContext(), LinearLayout.VERTICAL, false);
        mSearchAdapter = new SearchAdapter(getApplicationContext(), mActors);
        recyclerView.setAdapter(mSearchAdapter);
        recyclerView.setLayoutManager(linearLayout);

        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (newQuery.length() > 2) {
                Message message = Message.obtain(mHandler, MESSAGE_QUERY_UPDATE, newQuery);
                mHandler.postDelayed(() -> mHandler.dispatchMessage(message)
                        , QUERY_UPDATE_DELAY_MILLIS);
            }
            mHandler.removeMessages(MESSAGE_QUERY_UPDATE);
        });
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                floatingSearchView.setSearchText("");
                floatingSearchView.clearQuery();
                ArrayList<Result> results = mSearchAdapter.getDataSet();
                results.add((Result) searchSuggestion);
                mSearchAdapter.swapResults(results);
                mSearchAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mSearchAdapter);
                btnSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                floatingSearchView.clearQuery();

            }
        });

    }

    private void showActors(List<Result> results) {
        btnSearch.setVisibility(View.INVISIBLE);
        floatingSearchView.swapSuggestions(results);
    }

    @SuppressWarnings({"SameParameterValue", "UnusedParameters", "Convert2MethodRef", "WeakerAccess"})
    public void compareMovies(View view) {
        pbListLoading.setVisibility(View.VISIBLE);
        ArrayList<Result> actorResults = mSearchAdapter.getDataSet();
        if (actorResults.size() >= 2) {
            DataManager dm = new DataManager(myActorFactory);
            for (int index = 0; index < actorResults.size(); index++) {
                Result e = actorResults.get(index);
                boolean last = false;
                if (index + 1 == actorResults.size()) {
                    last = true;
                }
                final boolean l = last;
                Observable<Credits> castObservable = dm.getActor(Integer.toString(e.getId()));
                Observable<com.mangu.crossingactors.models.tv.Credits> tvObservable =
                        dm.getActorTv(Integer.toString(e.getId()));
                Observable.zip(castObservable, tvObservable,
                        (credits, credits2) -> joinCredits(credits, credits2)).
                        compose(SchedulerUtils.ioToMain())
                        .subscribe(act -> processActor(act, l),
                                throwable ->
                                        Log.e("ObservableError", throwable.getLocalizedMessage()));
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content),
                    "You need to add at least two mActors", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_MAIN_ACTIVITY_FROM_COMPARATION
                && resultCode == Activity.RESULT_FIRST_USER) {
            compareMovies(null);
        }
    }

    private void processCoincidences(List<String> movieCoincidences) {
        coincidenceMap.restartCoincidences();
        Intent intent = new Intent(this, ComparationDone.class);
        List<String> posters = coincidenceMap.getPosters();
        intent.putExtra(Cast.class.getName(), (ArrayList) movieCoincidences);
        intent.putExtra(MOVIE_POSTER_KEY, (ArrayList) posters);
        coincidenceMap.restartPosters();
        pbListLoading.setVisibility(View.INVISIBLE);
        startActivityForResult(intent, START_MAIN_ACTIVITY_FROM_COMPARATION);
    }

    private void processActor(Credits response, boolean lastToPass) {
        for (Cast cast : response.getCast()) {
            coincidenceMap.add(cast.getTitle(), (String) cast.getPosterPath());
        }
        coincidenceMap.restartProcessed();
        if (lastToPass) {
            mHandler.postDelayed(() -> processCoincidences(
                    coincidenceMap.getCoincidences(mSearchAdapter.getItemCount()))
                    , DELAY_MILLIS);
        }
    }
}
