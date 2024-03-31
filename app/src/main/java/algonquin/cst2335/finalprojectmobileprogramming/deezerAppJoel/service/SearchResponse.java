package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Artist;

public class SearchResponse {
    @SerializedName("data")
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }
}
