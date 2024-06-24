import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MusicAppGUI extends JFrame {
    // Components
    private JPanel mainPanel;
    private JTextField searchField;
    private JComboBox<String> searchCriteria;
    private JButton searchButton;
    private JButton addSongButton;
    private JButton deleteSongButton;
    private JComboBox<String> playlistDropdown;
    private JButton addToPlaylistButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    // Sample data (replace with actual data source)
    private ArrayList<Song> songs = new ArrayList<>();
    private HashMap<String, ArrayList<Song>> playlists = new HashMap<>();

    private String loggedInUser;

    private static final String SONGS_FILE = "songs_%s.txt";
    private static final String PLAYLISTS_FILE = "playlists_%s.txt";

    public MusicAppGUI(String loggedInUser) {
        this.loggedInUser = loggedInUser;

        setTitle("Music Listening App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadSongs();
        loadPlaylists();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(173, 216, 230)); 
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        String[] criteriaOptions = {"Song", "Album", "Genre", "Year of Release", "Singer"};
        searchCriteria = new JComboBox<>(criteriaOptions);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> search());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchCriteria);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Buttons panel (Add Song, Delete Song, Add to Playlist)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addSongButton = new JButton("Add Song");
        addSongButton.addActionListener(e -> addSongDialog());

        deleteSongButton = new JButton("Delete Song");
        deleteSongButton.addActionListener(e -> deleteSong());

        addToPlaylistButton = new JButton("Add to Playlist");
        addToPlaylistButton.addActionListener(e -> addToPlaylistDialog());

        buttonsPanel.add(addSongButton);
        buttonsPanel.add(deleteSongButton);
        buttonsPanel.add(new JLabel("Playlist:"));
        playlistDropdown = new JComboBox<>();
        updatePlaylistDropdown();
        buttonsPanel.add(playlistDropdown);
        buttonsPanel.add(addToPlaylistButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Results table
        String[] columns = {"ID", "Song Name", "Singer", "Genre", "Album", "Release Year"};
        tableModel = new DefaultTableModel(columns, 0);
        resultsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);

        //Add Songs on table
        for (Song song : songs) {
            addToTable(song);
        }
    }

    // Ham lay infor tu user
    private void search() {
        String searchText = searchField.getText().toLowerCase();
        String criteria = (String) searchCriteria.getSelectedItem();
        tableModel.setRowCount(0); // xoa Ket qua dang truoc

        for (Song song : songs) {
            switch (criteria) {
                case "Song":
                    if (song.getSongName().toLowerCase().contains(searchText)) {
                        addToTable(song);
                    }
                    break;
                case "Album":
                    if (song.getAlbum().toLowerCase().contains(searchText)) {
                        addToTable(song);
                    }
                    break;
                case "Genre":
                    if (song.getGenre().toLowerCase().contains(searchText)) {
                        addToTable(song);
                    }
                    break;
                case "Year of Release":
                    if (String.valueOf(song.getReleaseYear()).contains(searchText)) {
                        addToTable(song);
                    }
                    break;
                case "Singer":
                    if (song.getSinger().toLowerCase().contains(searchText)) {
                        addToTable(song);
                    }
                    break;
            }
        }
    }

    // Add a song to the table
    private void addToTable(Song song) {
        Object[] rowData = {
                song.getSongID(),
                song.getSongName(),
                song.getSinger(),
                song.getGenre(),
                song.getAlbum(),
                song.getReleaseYear()
        };
        tableModel.addRow(rowData);
    }

    // Add a new song
    private void addSongDialog() {
        JTextField songNameField = new JTextField(20);
        JTextField singerField = new JTextField(20);
        JTextField genreField = new JTextField(20);
        JTextField albumField = new JTextField(20);
        JTextField releaseYearField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 5));
        panel.add(new JLabel("Song Name:"));
        panel.add(songNameField);
        panel.add(new JLabel("Singer:"));
        panel.add(singerField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);
        panel.add(new JLabel("Album:"));
        panel.add(albumField);
        panel.add(new JLabel("Release Year:"));
        panel.add(releaseYearField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Song",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Retrieve input values
            String songName = songNameField.getText();
            String singer = singerField.getText();
            String genre = genreField.getText();
            String album = albumField.getText();
            int releaseYear = Integer.parseInt(releaseYearField.getText());
            int songID = songs.size() + 1;

            // Create new Song
            Song newSong = new Song(songID, songName, singer, genre, album, releaseYear);
            songs.add(newSong);

            // Add new song to the table
            addToTable(newSong);

            // Save the updated song list to file
            saveSongs();
        }
    }

    // Delete a song
    private void deleteSong() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a song to delete.", "Delete Song", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int songID = (int) resultsTable.getValueAt(selectedRow, 0);
        String songName = (String) resultsTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the song '" + songName + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove from songs list
            songs.removeIf(song -> song.getSongID() == songID);

            // Remove from table
            tableModel.removeRow(selectedRow);

            // Save the updated song list to file
            saveSongs();
        }
    }

    // Cap nhat Menu xuong Ds phat
    private void updatePlaylistDropdown() {
        playlistDropdown.removeAllItems();
        for (String playlistName : playlists.keySet()) {
            playlistDropdown.addItem(playlistName);
        }
    }

    //Add song to playlist or create new playlist
    private void addToPlaylistDialog() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a song to add to playlist.", "Add to Playlist", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String playlistName = JOptionPane.showInputDialog(this, "Enter playlist name:", "Create Playlist", JOptionPane.PLAIN_MESSAGE);
        if (playlistName == null || playlistName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Playlist name cannot be empty.", "Create Playlist", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int songID = (int) resultsTable.getValueAt(selectedRow, 0); // Assuming songID is in the first column

        // Find the song 
        Song songToAdd = null;
        for (Song song : songs) {
            if (song.getSongID() == songID) {
                songToAdd = song;
                break;
            }
        }

        // Check if playlist already exists
        if (playlists.containsKey(playlistName)) {
            // Playlist exists, add song to playlist
            ArrayList<Song> playlistSongs = playlists.get(playlistName);
            if (!playlistSongs.contains(songToAdd)) {
                playlistSongs.add(songToAdd);
                JOptionPane.showMessageDialog(this, "Song added to playlist '" + playlistName + "'.", "Add to Playlist", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Song is already in playlist '" + playlistName + "'.", "Add to Playlist", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            // Playlist doesn't exist, create new playlist
            ArrayList<Song> newPlaylist = new ArrayList<>();
            newPlaylist.add(songToAdd);
            playlists.put(playlistName, newPlaylist);
            updatePlaylistDropdown();
            JOptionPane.showMessageDialog(this, "Playlist '" + playlistName + "' created and song added.", "Add to Playlist", JOptionPane.INFORMATION_MESSAGE);
        }

        // Save the updated playlists to file
        savePlaylists();
    }

    // Save songs to file
    private void saveSongs() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(SONGS_FILE, loggedInUser)))) {
            for (Song song : songs) {
                writer.write(song.getSongID() + "," + song.getSongName() + "," + song.getSinger() + ","
                        + song.getGenre() + "," + song.getAlbum() + "," + song.getReleaseYear());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load songs from file
    private void loadSongs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format(SONGS_FILE, loggedInUser)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int songID = Integer.parseInt(parts[0]);
                String songName = parts[1];
                String singer = parts[2];
                String genre = parts[3];
                String album = parts[4];
                int releaseYear = Integer.parseInt(parts[5]);
                Song song = new Song(songID, songName, singer, genre, album, releaseYear);
                songs.add(song);
            }
        } catch (IOException e) {
            
        }
    }

    //Save playlists to file
    private void savePlaylists() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(PLAYLISTS_FILE, loggedInUser)))) {
            for (String playlistName : playlists.keySet()) {
                writer.write("PLAYLIST:" + playlistName);
                writer.newLine();
                for (Song song : playlists.get(playlistName)) {
                    writer.write(song.getSongID() + "," + song.getSongName() + "," + song.getSinger() + ","
                            + song.getGenre() + "," + song.getAlbum() + "," + song.getReleaseYear());
                    writer.newLine();
                }
                writer.write("END_PLAYLIST");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load playlists from file
    private void loadPlaylists() {
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format(PLAYLISTS_FILE, loggedInUser)))) {
            String line;
            String currentPlaylistName = null;
            ArrayList<Song> currentPlaylistSongs = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PLAYLIST:")) {
                    currentPlaylistName = line.substring(9);
                    currentPlaylistSongs = new ArrayList<>();
                } else if (line.equals("END_PLAYLIST")) {
                    if (currentPlaylistName != null && currentPlaylistSongs != null) {
                        playlists.put(currentPlaylistName, currentPlaylistSongs);
                    }
                    currentPlaylistName = null;
                    currentPlaylistSongs = null;
                } else {
                    String[] parts = line.split(",");
                    int songID = Integer.parseInt(parts[0]);
                    String songName = parts[1];
                    String singer = parts[2];
                    String genre = parts[3];
                    String album = parts[4];
                    int releaseYear = Integer.parseInt(parts[5]);
                    Song song = new Song(songID, songName, singer, genre, album, releaseYear);
                    if (currentPlaylistSongs != null) {
                        currentPlaylistSongs.add(song);
                    }
                }
            }
        } catch (IOException e) {
            
        }
    }

    public static void main(String[] args) {
    
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and display the GUI 
        SwingUtilities.invokeLater(() -> {
            LoginAndSignUpGUI loginAndSignUpGUI = new LoginAndSignUpGUI();
            if (loginAndSignUpGUI.showLoginScreen()) {
                String loggedInUser = loginAndSignUpGUI.getLoggedInUser(); 
                MusicAppGUI app = new MusicAppGUI(loggedInUser);
                app.setVisible(true);
            } else {
                System.exit(0); // Exit
            }
        });
    }
}
