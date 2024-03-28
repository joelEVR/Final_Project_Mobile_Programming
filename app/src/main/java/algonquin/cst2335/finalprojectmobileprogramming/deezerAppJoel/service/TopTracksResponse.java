package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TopTracksResponse {
    @SerializedName("data")
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public static class Track {
        @SerializedName("id")
        private long id;

        @SerializedName("title")
        private String title;

        @SerializedName("duration")
        private int duration;

        @SerializedName("album")
        private Album album;

        @SerializedName("preview")
        private String preview;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public Album getAlbum() {
            return album;
        }

        public void setPreview(Album album) {
            this.album = album;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

    }

    public static class Album {
        @SerializedName("title")
        private String title;

        @SerializedName("cover")
        private String coverUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }
    }
}
