package com.swap.newsdesk.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.android.material.button.MaterialButton;
import com.swap.newsdesk.Constants;
import com.swap.newsdesk.R;
import com.swap.newsdesk.adapters.NewsDataAdapter;
import com.swap.newsdesk.models.NewsDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllNews extends Fragment {

    private EditText edCharacter;
    private MaterialButton mbSearch;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private NewsDataAdapter headlineAdapter;
    private List<NewsDataModel> headlineModelList;
    private ProgressDialog progressDialog;
    private String character = "";
    private ShimmerFrameLayout shimmerFrameLayout;

    private static final String URL_GET_ALL_NEWS = Constants.URL_EVERYTHING + "?q=india&pageSize=100&sortBy=publishedAt&apiKey=" + Constants.NEWS_API_KEY;
    private static final String URL_GET_NEWS_BY_KEYWORD = Constants.URL_EVERYTHING + "?q=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_news, container, false);

        edCharacter = view.findViewById(R.id.search_character);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        recyclerView = view.findViewById(R.id.all_news_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // SwipeRefreshLayout
        refreshLayout = view.findViewById(R.id.all_news_swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayAllNews();
                refreshLayout.setRefreshing(false);
            }
        });

        headlineModelList = new ArrayList<>();
        headlineAdapter = new NewsDataAdapter(getContext(), headlineModelList);

        view.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewsByKeyword();
            }
        });

        displayAllNews();

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

    // display news by keyword
    private void displayNewsByKeyword() {
        character = edCharacter.getText().toString().trim();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching " + character);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_NEWS_BY_KEYWORD + character + "&pageSize=100&sortBy=publishedAt&apiKey=" + Constants.NEWS_API_KEY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                headlineAdapter.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    // display all news
    private void displayAllNews() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_ALL_NEWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                headlineAdapter.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++) {
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

                    shimmerFrameLayout.stopShimmer();
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
