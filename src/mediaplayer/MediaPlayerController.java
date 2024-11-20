package mediaplayer;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MediaPlayerController implements Initializable {
    
    private String path;
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private Button openFile;
    
    @FXML
    private Slider volumeSlider;
    
    @FXML
    private Slider progressBar;
    
    @FXML
    private Label label;
    
    @FXML
    private StackPane pane;
    
    @FXML
    private void OpenFileMethod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a .mp4 file", ".mp4");
//        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if(path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            
            //creating bindings
            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();
            
            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            
            //Setting up the volume
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });
            
            
            //Video starts playing at the selected time
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() {
                @Override
                public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                    progressBar.setValue(newValue.toSeconds());
                }
            }
            );
            //Selecting a time by clicking
            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
                }
            });
            
            //Selecting a time by dragging
            progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
                }
            });
            
            //When reached end of the duration video stops
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    javafx.util.Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
            });
            
            mediaPlayer.play();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    //Pause button
    public void pauseVideo(ActionEvent event){
        mediaPlayer.pause();
    }
    
    //Stop button
    public void stopVideo(ActionEvent event){
        mediaPlayer.stop();
    }
    
    //Play button
    public void playVideo(ActionEvent event){
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }
    
    //+5s button
    public void skip10(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(10)));
    }
    
    //>>> button
    public void furtherSpeedUpVideo(ActionEvent event){
        mediaPlayer.setRate(2);
    }
    
    //-5s button
    public void back5(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-5)));
    }
    
    //<<< button
    
    public void furtherSlowDownVideo(ActionEvent event){
        mediaPlayer.setRate(0.5);

    }

    
    
}
