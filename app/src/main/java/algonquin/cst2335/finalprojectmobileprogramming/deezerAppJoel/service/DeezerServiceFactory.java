package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

    public class DeezerServiceFactory {
        private static final String BASE_URL = "https://api.deezer.com/";

        public static DeezerService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit.create(DeezerService.class);
        }
    }
