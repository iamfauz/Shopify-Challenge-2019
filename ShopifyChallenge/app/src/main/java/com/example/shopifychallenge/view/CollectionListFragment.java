package com.example.shopifychallenge.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopifychallenge.R;
import com.example.shopifychallenge.model.CollectionsResponse;
import com.example.shopifychallenge.model.CustomCollection;
import com.example.shopifychallenge.retrofit.ApiService;
import com.example.shopifychallenge.retrofit.RetrofitClientInstance;
import com.example.shopifychallenge.utils.CollectionsAdapter;
import com.example.shopifychallenge.utils.Constants;
import com.example.shopifychallenge.utils.FragmentUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CollectionListFragment extends Fragment implements CollectionsAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view_collection)
    public RecyclerView mRecyclerView;

    @BindView(R.id.swipe_to_refresh)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private CollectionsAdapter mCollectionsAdapter;


    public CollectionListFragment() {
        // Required empty public constructor
    }


    public static CollectionListFragment newInstance() {
        CollectionListFragment fragment = new CollectionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("Collections");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection_list, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                loadDataToRecyclerView();
            }
        });
    }


    /**
     * Method to initialize recyclerView
     */
    public void initRecyclerView() {

        //RecyclerViewDefinition
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        //Improving performance
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mCollectionsAdapter = new CollectionsAdapter(this, getActivity());
        mRecyclerView.setAdapter(mCollectionsAdapter);

    }

    public void loadDataToRecyclerView() {

        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);

        Call<CollectionsResponse> call = service.getCollections(1, Constants.ACCESS_TOKEN);

        call.enqueue(new Callback<CollectionsResponse>() {
            @Override
            public void onResponse(Call<CollectionsResponse> call, Response<CollectionsResponse> response) {

                if (response.isSuccessful()) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    mCollectionsAdapter.setCustomCollectionData(response.body().getCustomCollections());

                } else {

                    Toast.makeText(getActivity(), "Failed to get collections", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }

            @Override
            public void onFailure(Call<CollectionsResponse> call, Throwable t) {
                Log.d("DEBUG", t.getLocalizedMessage() + "");
                Log.d("DEBUG", t.getMessage() + "");
                t.printStackTrace();

                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {

        // Fetching data from server
        loadDataToRecyclerView();
    }

    @Override
    public void onListItemClick(CustomCollection customCollection) {

        FragmentUtils.replaceFragment((AppCompatActivity) getActivity(),
                ProductsFragment.newInstance(customCollection.getId(), customCollection.getTitle(), customCollection.getBodyHtml(),
                        customCollection.getImage().getSrc()), R.id.fragment_container, true);


    }


}

