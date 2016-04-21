package com.yamobil.ag.yamobilization.External;

import android.content.Context;

import com.yamobil.ag.yamobilization.R;

public class ExternalMethods {

    public static String getAlbumsString(Context context, int albumsCount) {
        String albumsString = null;

        int cnt = albumsCount%100;

        if ((cnt >= 11) && (cnt <= 19)) {
            albumsString = context.getString(R.string.s_albums_3);
        } else {
            cnt = cnt%10;
            switch (cnt) {
                case 1: albumsString = context.getString(R.string.s_albums_1);
                    break;
                case 2:
                case 3:
                case 4: albumsString = context.getString(R.string.s_albums_2);
                    break;
                default: albumsString = context.getString(R.string.s_albums_3);
                    break;
            }
        }

        return Integer.toString(albumsCount) + " " + albumsString;
    }

    public static String getTracksString(Context context, int tracksCount) {
        String tracksString = null;

        int cnt = tracksCount%100;

        if ((cnt >= 11) && (cnt <= 19)) {
            tracksString = context.getString(R.string.s_tracks_3);
        } else {
            cnt = cnt%10;
            switch (cnt) {
                case 1: tracksString = context.getString(R.string.s_tracks_1);
                    break;
                case 2:
                case 3:
                case 4: tracksString = context.getString(R.string.s_tracks_2);
                    break;
                default: tracksString = context.getString(R.string.s_tracks_3);
                    break;
            }
        }

        return Integer.toString(tracksCount) + " " + tracksString;
    }

}
