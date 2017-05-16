package com.mangu.crossingactors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.mangu.crossingactors.Model.ActorListResponse;
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

public class MainActivity extends AppCompatActivity {

    private static final Comparator<Result> actor_comparator = ComparatorFactory.getActorComparator();
    public static final int MESSAGE_QUERY_UPDATE = 5612;
    public static final int QUERY_UPDATE_DELAY_MILLIS = 100;

    ViewGroup.LayoutParams original_params;
    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_search)
    Button btnSearch;
    private ArrayList<Result> actors = new ArrayList<>();
    public static ActorService myActorFactory = makeActorService();
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        searchAdapter = new SearchAdapter(actors);
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
                        .subscribe(actorListResponse -> showActors(actorListResponse.results), throwable -> Log.e("ObservableError", throwable.getLocalizedMessage()));
            }
        }
    };

    public void showActors(List<Result> results) {
        btnSearch.setVisibility(View.INVISIBLE);

        floatingSearchView.swapSuggestions(results);
    }

    public void compareMovies(View view) {
        ArrayList<Result> actor_results = searchAdapter.getDataSet();

    }
}
