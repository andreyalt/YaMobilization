package com.yamobil.ag.yamobilization;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.yamobil.ag.yamobilization.Adapters.ArtistsRVAdapter;
import com.yamobil.ag.yamobilization.External.DividerItemDecoration;
import com.yamobil.ag.yamobilization.Interfaces.ArtistsAPI;
import com.yamobil.ag.yamobilization.Models.Artist;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ENDPOINT = "http://download.cdn.yandex.net/mobilization-2016/";

    private RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeLayout;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    ProgressBar progressBar;

    private RecyclerView.Adapter mAdapter;
    private ArtistsAPI artistsAPI;

    private List<Artist> artistList = new ArrayList<Artist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.s_artists);
        setSupportActionBar(toolbar);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.my_recycler_view_cards);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(layoutManager);

        //объявим адаптер для RecyclerView
        mAdapter = new ArtistsRVAdapter(artistList);

        mRecyclerView.setAdapter(mAdapter);

        //настроим цвета и сам swipe
        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.parseColor("#B00F3B"),
                Color.parseColor("#0E2642"),
                Color.parseColor("#2F2F2F"));
        swipeLayout.setOnRefreshListener(this);

        progressBar = (ProgressBar) this.findViewById(R.id.pb_loader);
        coordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.my_coordinatorlayout);

        // создаем подключение к АПИ
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        artistsAPI = retrofit.create(ArtistsAPI.class);

        onRefresh();

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                loadData();
            }
        }, 3000);
    }

    private void createSnackbar() {
        snackbar = Snackbar.make(coordinatorLayout, R.string.s_no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.s_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        loadData();
                    }
                });
    }

    private void loadData() {

        //перед загрузкой проверим есть ли снэкбар и отображен ли он,еслида, то прибъём его
        if (snackbar != null) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }

        //получим данные
        Call<List<Artist>> call = artistsAPI.getArtists();

        call.enqueue(new Callback<List<Artist>>() {

            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    artistList.clear();
                    artistList.addAll(response.body()); // обавили новые данные в наш массив
                    mAdapter.notifyDataSetChanged();

                    //данные закэшировали
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(MainActivity.this.getFilesDir().getPath().toString() + "/cache.out");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(artistList);
                        oos.flush();
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                // при ошибке покажем снэкбар и вытянемданные из кэша
                progressBar.setVisibility(View.GONE);
                createSnackbar();
                snackbar.show();

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(MainActivity.this.getFilesDir().getPath().toString() + "/cache.out");
                    ObjectInputStream oin = new ObjectInputStream(fis);
                    artistList.clear();
                    artistList.addAll((ArrayList<Artist>) oin.readObject());
                    mAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
