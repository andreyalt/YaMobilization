package com.yamobil.ag.yamobilization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamobil.ag.yamobilization.External.CollectionUtils;
import com.yamobil.ag.yamobilization.External.ExternalMethods;
import com.yamobil.ag.yamobilization.Models.Artist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArtistInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Artist artist = (Artist) getIntent().getSerializableExtra("artistItem");

        ImageView ivBigCover = (ImageView) this.findViewById(R.id.iv_big_cover);
        TextView tvGenres = (TextView) this.findViewById(R.id.tv_genres);
        TextView tvAlbums = (TextView) this.findViewById(R.id.tv_albums);
        TextView tvDescription = (TextView) this.findViewById(R.id.tv_description);

        toolbar.setTitle(artist.getName());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bitmap bitmapCover = null;

        try {
            bitmapCover = bitmapFromUrl(artist.getCover().getSmall());
            Picasso.with(this.getApplication().getApplicationContext())
                    .load(artist.getCover().getBig())
                    .placeholder(new BitmapDrawable(getResources(), bitmapCover))
                    .into(ivBigCover);

            if (bitmapCover != null) {
                Palette palette = Palette.from(bitmapCover).generate();
                int vibrantColor = palette.getVibrantColor(Color.GRAY);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrantColor));

                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.setStatusBarColor(getDarkColor(vibrantColor));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        tvGenres.setText(CollectionUtils.join(artist.getGenres(), ", "));
        tvAlbums.setText(ExternalMethods.getAlbumsString(this, artist.getAlbums()) + "  ·  "
                        + ExternalMethods.getTracksString(this, artist.getTracks()));
        tvDescription.setText(artist.getDescription());

    }

    public Bitmap bitmapFromUrl(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (NetworkOnMainThreadException nomte) {
            nomte.printStackTrace();
        }

        return null;
    }

    public int getDarkColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
