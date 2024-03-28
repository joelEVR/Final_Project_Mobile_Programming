package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeezerService {
    @GET("search/artist")
    Call<SearchResponse> searchArtists(@Query("q") String query);

    @GET("artist/{artistId}/top")
    Call<TopTracksResponse> getTopTracks(@Path("artistId") int artistId, @Query("limit") int limit);
}