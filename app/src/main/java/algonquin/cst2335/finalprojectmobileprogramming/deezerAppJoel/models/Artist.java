package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
    private int id;
    private String name;
    private String picture_small;
    private String tracklist;

    public Artist(int id, String name, String pictureSmall, String tracklist) {
        this.id = id;
        this.name = name;
        this.picture_small = pictureSmall;
        this.tracklist = tracklist;
    }

    protected Artist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        picture_small = in.readString();
        tracklist = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPictureSmall() {
        return picture_small;
    }

    public String getTracklist() {
        return tracklist;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(picture_small);
        dest.writeString(tracklist);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}

