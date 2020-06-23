package com.kanjisoup.audio.player;

import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

@Component
@Slf4j
public class AudioPlayer {
    private String basePath;

    public AudioPlayer(AudioPlayerConfig config) {
        this.basePath = config.getBaseDir();
    }

    public void playAudioFile(String basename) {
        try {
            // Open an audio input stream.
            File inFile = Paths.get(basePath, basename).toFile();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(inFile);

            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            log.error("Unsupported audio file " + basename, e);
        } catch (IOException e) {
            log.error("IOException opening file " + basename, e);
        } catch (LineUnavailableException e) {
            log.error("Line unavailable for playing audio", e);
        }
    }
}
