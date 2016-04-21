package com.yamobil.ag.yamobilization.Adapters;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yamobil.ag.yamobilization.ArtistInfoActivity;
import com.yamobil.ag.yamobilization.External.CollectionUtils;
import com.yamobil.ag.yamobilization.External.ExternalMethods;
import com.yamobil.ag.yamobilization.MainActivity;
import com.yamobil.ag.yamobilization.Models.Artist;
import com.yamobil.ag.yamobilization.R;

import java.util.List;

public class ArtistsRVAdapter extends RecyclerView.Adapter<ArtistsRVAdapter.ViewHolder> {
    private List<Artist> artistList;

    public ArtistsRVAdapter(List<Artist> dataset) {
        artistList = dataset;
    }

    @Override
    public ArtistsRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArtistsRVAdapter.ViewHolder holder, int position) {
        holder.setItem(artistList.get(position));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "MY_LOG";

        private Artist mItem;

        private TextView tvTitle;
        private TextView tvGenres;
        private TextView tvExtraInfo;
        private ImageView ivCover;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvGenres = (TextView) v.findViewById(R.id.tv_genres);
            tvExtraInfo = (TextView) v.findViewById(R.id.tv_extra_info);
            ivCover = (ImageView) v.findViewById(R.id.iv_artist_cover);
        }

        public void setItem(Artist item) {
            mItem = item;
            Picasso.with(ivCover.getContext()).load(mItem.getCover().getSmall()).into(ivCover);
            tvTitle.setText(mItem.getName());
            tvGenres.setText(CollectionUtils.join(mItem.getGenres(), ", "));
            tvExtraInfo.setText(ExternalMethods.getAlbumsString(ivCover.getContext(), mItem.getAlbums()) + ", "
                    + ExternalMethods.getTracksString(ivCover.getContext(), mItem.getTracks()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ArtistInfoActivity.class);
            intent.putExtra("artistItem", mItem);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (MainActivity) v.getContext(),
                    new Pair<View, String>(v.findViewById(R.id.iv_artist_cover),
                            v.getContext().getString(R.string.transition_name_cover))
            );

            ActivityCompat.startActivity(((MainActivity) v.getContext()), intent, options.toBundle());
        }

    }
}
