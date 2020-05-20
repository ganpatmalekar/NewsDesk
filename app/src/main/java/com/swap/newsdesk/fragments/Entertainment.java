package com.swap.newsdesk.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.swap.newsdesk.Constants;
import com.swap.newsdesk.R;
import com.swap.newsdesk.adapters.NewsDataAdapter;
import com.swap.newsdesk.models.NewsDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Entertainment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private NewsDataAdapter headlineAdapter;
    private List<NewsDataModel> headlineModelList;
    private ShimmerFrameLayout shimmerFrameLayout;

    private static final String URL_GET_ENTERTAINMENT = Constants.URL_TOP_HEADLINES + "?country=in&category=entertainment&pageSize=100&apiKey=" + Constants.NEWS_API_KEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entertainment, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        recyclerView = view.findViewById(R.id.entertainment_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        refreshLayout = view.findViewById(R.id.entertainment_swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayEntertainment();
                refreshLayout.setRefreshing(false);
            }
        });

        headlineModelList = new ArrayList<>();
        headlineAdapter = new NewsDataAdapter(getContext(), headlineModelList);

        displayEntertainment();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    private void displayEntertainment() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_ENTERTAINMENT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                headlineAdapter.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("articles");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        NewsDataModel model = new NewsDataModel();
                        model.setImage_url(jsonObject.getString("urlToImage"));
                        model.setTitle(jsonObject.getString("title"));
                        model.setDescription(jsonObject.getString("description"));
                        model.setPublished_date(jsonObject.getString("publishedAt"));
                        model.setContent(jsonObject.getString("content"));
                        model.setUrl(jsonObject.getString("url"));

                        headlineModelList.add(model);
                    }
                    recyclerView.setAdapter(headlineAdapter);

                    shimmerFrameLayout.stopShimmer();;
                    shimmerFrameLayout.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}
