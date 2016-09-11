package com.pruebaandroid.misterhouse.ui.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pruebaandroid.misterhouse.Adapter.ProducAdapter;
import com.pruebaandroid.misterhouse.DataObjects.Producto;
import com.pruebaandroid.misterhouse.EventBus;
import com.pruebaandroid.misterhouse.Ormlite.DBHelperOrm;
import com.pruebaandroid.misterhouse.R;
import com.pruebaandroid.misterhouse.Utils.Utils;
import com.pruebaandroid.misterhouse.WebServices.Endpoints;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListAplicationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = ListAplicationsFragment.class.getName();
    private AbsListView mListResults;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProducAdapter mProducAdapter;
    private ProgressBar mprogressBar;
    private LinearLayout mmessage_network;
    private ImageView mimage_status_network;
    private TextView mtext_status_network;
    private RequestQueue mRequestQueue;

    private static DBHelperOrm dbHelperOrm;
    private static RuntimeExceptionDao<Producto, Integer> daoProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_list_products, container, false);
        dbHelperOrm = OpenHelperManager.getHelper(getContext(), DBHelperOrm.class);
        daoProduct = dbHelperOrm.getRuntimeExceptionDao(Producto.class);
        mmessage_network = (LinearLayout) rootView.findViewById(R.id.mesagge_network);
        mimage_status_network = (ImageView) rootView.findViewById(R.id.image_status_network);
        mtext_status_network = (TextView) rootView.findViewById(R.id.text_status_network);
        mprogressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mListResults = (AbsListView) rootView.findViewById(R.id.listapps);
        mProducAdapter = new ProducAdapter(getContext(), new ArrayList<Producto>());
        mListResults.setAdapter(mProducAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        EventBus.getInstance().register(this);
        return rootView;
    }

    @Subscribe
    public void onEntryChanged(String mensa) {
        //Toast.makeText(getContext(),mensa,Toast.LENGTH_LONG).show();
        mProducAdapter.clear();
        showdata();
    }

    public void requestGetProducts() {
        mRequestQueue = Volley.newRequestQueue(getContext());
        String url = Endpoints.BASEURL + Endpoints.PRODUCTS;
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        ArrayList<Producto> listResponseProducts = new ArrayList<Producto>();
                        Log.d(String.format("successful response call endpoint %s", Endpoints.PRODUCTS), response.toString());
                        JSONArray ProductArray = null;
                        try {
                            ProductArray = response.getJSONArray("results");
                            listResponseProducts = new Gson().fromJson(String.valueOf(ProductArray), new TypeToken<ArrayList<Producto>>() {
                            }.getType());
                            updateProducts(listResponseProducts);
                            loadListViewData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mprogressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(String.format("ERROR response call endpoint %s", Endpoints.PRODUCTS), error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = Endpoints.getHeaders();
                return params;
            }
        };
        mRequestQueue.add(postRequest);

    }

    private void loadListViewData() {
        mProducAdapter.clear();
        List<Producto> localProducts = new ArrayList<>(daoProduct.queryForAll());
        if (localProducts != null && !localProducts.isEmpty()) {
            mProducAdapter.addAll(localProducts);
        } else {
            Fragment noDataFragment = new NoDataNoConectionFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, noDataFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void updateProducts(ArrayList<Producto> listResponseProducts) {
        for (Producto newproduct : listResponseProducts) {

            try {
                QueryBuilder<Producto, Integer> queryBuilder = daoProduct.queryBuilder();
                queryBuilder.where().eq(Producto.OBJETCID_FIELD_NAME, newproduct.getObjectId());
                Producto oldProduct = daoProduct.queryForFirst(queryBuilder.prepare());
                if (oldProduct == null) {
                    daoProduct.create(newproduct);
                } else if (!oldProduct.getUpdatedAt().equals(newproduct.getUpdatedAt())) {
                    newproduct.setUpdated(false);
                    newproduct.setIdproducto(oldProduct.getIdproducto());
                    daoProduct.update(newproduct);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showdata() {
        if (Utils.isConnected(getContext())) {
            requestGetProducts();
            Log.d(LOG_TAG, "internet Conection done");
            showMessageNetWork(true);
        } else {
            Log.e(LOG_TAG, "No internet Conection");

            loadListViewData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showdata();

    }

    private void showMessageNetWork(boolean conectect) {
        if (conectect) {
            mmessage_network.setBackgroundColor(getResources().getColor(R.color.onlinecolor));
            mtext_status_network.setText(R.string.status_on_line);
            mimage_status_network.setBackgroundResource(R.drawable.ic_file_cloud_done);
            AnimationSet set = new AnimationSet(true);
            Animation animationItems = null;
            animationItems = new TranslateAnimation(0, 0, 0, 100);
            animationItems.setDuration(5000);
            set.addAnimation(animationItems);
            mmessage_network.startAnimation(animationItems);

            animationItems.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                    mmessage_network.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    mmessage_network.setVisibility(View.GONE);
                }
            });
        } else {
            mmessage_network.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mimage_status_network.setBackgroundResource(R.drawable.ic_file_cloud_off_line);
            mtext_status_network.setText(R.string.status_off_line);
            mmessage_network.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            requestGetProducts();
            Log.d(LOG_TAG, "internet Conection done");
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Log.e(LOG_TAG, "No internet Conection");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
