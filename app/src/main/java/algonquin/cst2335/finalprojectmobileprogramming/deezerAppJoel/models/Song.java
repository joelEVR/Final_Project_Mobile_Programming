package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "songs")
public class Song implements Parcelable {
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

    public Song(long id,String artist,String title, int duration, String albumName, String albumCoverUrl) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;

    }

    protected Song(Parcel in) {
        id = in.readLong();
        artist = in.readString();
        title = in.readString();
        duration = in.readInt();
        albumName = in.readString();
        albumCoverUrl = in.readString();
    }

    public static final Parcelable.Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeString(albumName);
        dest.writeString(albumCoverUrl);
    }
}