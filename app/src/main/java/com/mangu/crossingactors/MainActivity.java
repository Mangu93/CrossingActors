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

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.mangu.crossingactors.Model.ActorListResponse;
import com.mangu.crossingactors.Model.Cast;
import com.mangu.crossingactors.Model.Credits;
import com.mangu.crossingactors.Model.Result;
import com.mangu.crossingactors.Networking.DataManager;
import com.mangu.crossingactors.Networking.Services.ActorService;
import com.mangu.crossingactors.Utils.ComparatorFactory;
import com.mangu.crossingactors.Utils.scheduler.SchedulerUtils;
import com.mangu.crossingactors.Views.SearchAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static com.mangu.crossingactors.Networking.Services.ActorServiceFactory.makeActorService;
import static com.mangu.crossingactors.Utils.UtilsFactory.START_MAIN_ACTIVITY_FROM_COMPARATION;

public class MainActivity extends AppCompatActivity {

    private static final Comparator<Result> actor_comparator = ComparatorFactory.getActorComparator();
    public static final int MESSAGE_QUERY_UPDATE = 5612;
    public static final int QUERY_UPDATE_DELAY_MILLIS = 100;

    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_search)
    Button btnSearch;
    private ArrayList<Result> actors = new ArrayList<>();
    public static ActorService myActorFactory = makeActorService();
    SearchAdapter searchAdapter;

    public static ComparatorFactory.CoincidenceMap coincidenceMap = new ComparatorFactory.CoincidenceMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        searchAdapter = new SearchAdapter(getApplicationContext(), actors);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(linearLayout);

        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (newQuery.length() > 2) {
                Message message = Message.obtain(mHandler, MESSAGE_QUERY_UPDATE, newQuery);
                mHandler.postDelayed(() -> mHandler.dispatchMessage(message), QUERY_UPDATE_DELAY_MILLIS);
            }
            mHandler.removeMessages(MESSAGE_QUERY_UPDATE);
        });
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                floatingSearchView.setSearchText("");
                floatingSearchView.clearQuery();
                ArrayList<Result> results = searchAdapter.getDataSet();
                results.add((Result) searchSuggestion);
                searchAdapter.swapResults(results);
                searchAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(searchAdapter);
                btnSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                floatingSearchView.clearQuery();

            }
        });

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_QUERY_UPDATE) {
                String query = (String) msg.obj;
                DataManager dm = new DataManager(myActorFactory);
                Observable<ActorListResponse> listActors = dm.getActorList(query);
                listActors.compose(SchedulerUtils.ioToMain())
                        .subscribe(actorListResponse -> showActors(actorListResponse.results),
                                throwable -> Log.e("ObservableError", throwable.getLocalizedMessage()));
            }
        }
    };

    public void showActors(List<Result> results) {
        btnSearch.setVisibility(View.INVISIBLE);
        floatingSearchView.swapSuggestions(results);
    }

    public void compareMovies(View view) {
        ArrayList<Result> actor_results = searchAdapter.getDataSet();
        if (actor_results.size() >= 2) {
            DataManager dm = new DataManager(myActorFactory);
            for (int index = 0; index < actor_results.size(); index++) {
                Result e = actor_results.get(index);
                boolean last = false;
                if (index + 1 == actor_results.size()) {
                    last = true;
                }
                final boolean l = last;
                Observable<Credits> castObservable = dm.getActor(Integer.toString(e.getId()));
                castObservable.compose(SchedulerUtils.ioToMain())
                        .subscribe(act -> processActor(act, l),
                                throwable -> Log.e("ObservableError", throwable.getLocalizedMessage()));
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content), "You need to add at least two actors", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_MAIN_ACTIVITY_FROM_COMPARATION) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                compareMovies(null);
            }
        }
    }

    private void processCoincidences(List<String> movieCoincidences) {
        coincidenceMap.restartCoincidences();
        Intent intent = new Intent(this, ComparationDone.class);
        intent.putExtra(Cast.class.getName(), (ArrayList) movieCoincidences);
        startActivityForResult(intent, START_MAIN_ACTIVITY_FROM_COMPARATION);
    }

    private void processActor(Credits response, boolean last_to_pass) {
        for (Cast cast : response.getCast()) {
            coincidenceMap.add(cast.getTitle());
        }
        coincidenceMap.restartProcessed();
        if (last_to_pass) {
            processCoincidences(coincidenceMap.getCoincidences(searchAdapter.getItemCount()));
        }
    }
}
