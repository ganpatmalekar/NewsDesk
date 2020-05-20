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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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

public class TopHeadelines extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private NewsDataAdapter headlineAdapter;
    private List<NewsDataModel> headlineModelList;
    private ProgressDialog progressDialog;
    private ShimmerFrameLayout shimmerFrameLayout;

    private static final String URL_GET_HEADLINES = Constants.URL_TOP_HEADLINES + "?country=in&pageSize=100&apiKey=" + Constants.NEWS_API_KEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_headlines, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        recyclerView = view.findViewById(R.id.top_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        refreshLayout = view.findViewById(R.id.top_swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayHeadlines();
                refreshLayout.setRefreshing(false);
            }
        });

        headlineModelList = new ArrayList<>();
        headlineAdapter = new NewsDataAdapter(getContext(), headlineModelList);

        displayHeadlines();

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

    private void displayHeadlines() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Top News");
//        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET_HEADLINES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
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
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    // Is thrown if there's no network connection or server is down
                    Toast.makeText(getContext(), getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                } else {
                    // Is thrown if there's no network connection or server is down
                    Toast.makeText(getContext(), getString(R.string.error_network),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                }

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}
