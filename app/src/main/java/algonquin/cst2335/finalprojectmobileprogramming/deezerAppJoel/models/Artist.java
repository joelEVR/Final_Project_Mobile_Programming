package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models;


public class Artist {
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
    }

