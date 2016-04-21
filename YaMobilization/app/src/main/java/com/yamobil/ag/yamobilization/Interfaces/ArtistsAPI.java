package com.yamobil.ag.yamobilization.Interfaces;

import com.yamobil.ag.yamobilization.Models.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArtistsAPI {

    @GET("artists.json")
    public Call<List<Artist>> getArtists();

}
