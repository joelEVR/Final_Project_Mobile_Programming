package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


    @Entity(tableName = "artists")
    public class ArtistModel {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        public long id;

        @ColumnInfo(name = "name")
        private final String name;

        @ColumnInfo(name = "link")
        private final String link;

        @ColumnInfo(name = "picture_small")
        private final String pictureSmall;

        @ColumnInfo(name = "picture_medium")
        private final String pictureMedium;

        @ColumnInfo(name = "picture_big")
        private final String pictureBig;

        @ColumnInfo(name = "nb_album")
        private final int nbAlbum;

        @ColumnInfo(name = "nb_fan")
        private final int nbFan;

        @ColumnInfo(name = "radio")
        private final boolean radio;

        @ColumnInfo(name = "tracklist")
        private final String tracklist;

        public ArtistModel(String name, String link, String pictureSmall, String pictureMedium, String pictureBig, int nbAlbum, int nbFan, boolean radio, String tracklist) {
            this.name = name;
            this.link = link;
            this.pictureSmall = pictureSmall;
            this.pictureMedium = pictureMedium;
            this.pictureBig = pictureBig;
            this.nbAlbum = nbAlbum;
            this.nbFan = nbFan;
            this.radio = radio;
            this.tracklist = tracklist;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public String getPictureSmall() {
            return pictureSmall;
        }

        public String getPictureMedium() {
            return pictureMedium;
        }

        public String getPictureBig() {
            return pictureBig;
        }

        public int getNbAlbum() {
            return nbAlbum;
        }

        public int getNbFan() {
            return nbFan;
        }

        public boolean isRadio() {
            return radio;
        }

        public String getTracklist() {
            return tracklist;
        }
    }

