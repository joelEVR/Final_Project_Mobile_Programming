package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "artist")
    private String artist;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "duration")
    private int duration;
    @ColumnInfo(name = "album_name")
    private String albumName;
    @ColumnInfo(name = "album_cover_url")
    private String albumCoverUrl;

    @ColumnInfo(name = "preview_song_url")
    private  String previewSongUrl;

    public Song(long id,String artist,String title, int duration, String albumName, String albumCoverUrl,String previewSongUrl) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
        this.previewSongUrl = previewSongUrl;

    }

    public long getId() {
        return id;
    }
    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public String getPreviewSongUrl() {
        return previewSongUrl;
    }

}