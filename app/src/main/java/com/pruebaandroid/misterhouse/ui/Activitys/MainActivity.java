package com.pruebaandroid.misterhouse.ui.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.pruebaandroid.misterhouse.DataObjects.Producto;
import com.pruebaandroid.misterhouse.EventBus;
import com.pruebaandroid.misterhouse.Ormlite.DBHelperOrm;
import com.pruebaandroid.misterhouse.R;
import com.pruebaandroid.misterhouse.WebServices.Endpoints;
import com.pruebaandroid.misterhouse.ui.Fragments.ListAplicationsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    FrameLayout mFrameLayoutListas;
    Toolbar mToolbar;
    private RequestQueue mRequestQueue;
    private static DBHelperOrm dbHelperOrm;
    private static RuntimeExceptionDao<Producto, Integer> daoProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelperOrm = OpenHelperManager.getHelper(this, DBHelperOrm.class);
        daoProduct =dbHelperOrm.getRuntimeExceptionDao(Producto.class);
        mFrameLayoutListas = (FrameLayout)findViewById(R.id.container);
        EventBus.getInstance().register(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ListAplicationsFragment())
                    .commit();
        }
        initToolbar();
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.list_products));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }
    private  void removeToolberRowBack()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        mFrameLayoutListas.setVisibility(View.VISIBLE);
        removeToolberRowBack();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id ==16908332)
        {
            onBackPressed();
        }
        else if(id==R.id.sincro)
        {
            List<Producto> pendigsinco= daoProduct.queryForEq("updated",true);
            for (Producto producto : pendigsinco  ) {
                sincroRequest(producto);
            }
            EventBus.getInstance().post("Mensaje");
        }
        return super.onOptionsItemSelected(item);
    }
    // Menu icons are inflated just as they were with actionbar
    public void sincroRequest(final Producto pendigsinco) {

        mRequestQueue = Volley.newRequestQueue(this);
        String url = Endpoints.BASEURL+Endpoints.PRODUCTS+"/"+pendigsinco.getObjectId();
        JSONObject json = new JSONObject();
        try {
            json.put(Producto.DESCRIPTION_FIELD_NAME, pendigsinco.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,json,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        pendigsinco.setUpdated(false);
                        daoProduct.update(pendigsinco);
                        Log.d(String.format("successful to update endpoint %s",Endpoints.PRODUCTS), pendigsinco.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                            Log.d(String.format("ERROR response to update endpoint %s", Endpoints.PRODUCTS),"onErrorResponse");
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
    @Override
    public void onStop() {
        super.onStop();
        if(mRequestQueue!=null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }
}
