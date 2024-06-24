public class Song {
    private int songID;
    private String songName;
    private String singer;
    private String genre;
    private String album;
    private int releaseYear;

    public Song(int songID, String songName, String singer, String genre, String album, int releaseYear) {
        this.songID = songID;
        this.songName = songName;
        this.singer = singer;
        this.genre = genre;
        this.album = album;
        this.releaseYear = releaseYear;
    }
    
    public Song() {
    }

    public int getSongID() {
        return songID;
    }

    public String getSongName() {
        return songName;
    }

    public String getSinger() {
        return singer;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

        
}
