package com.kanjisoup.audio.player.clip;

import com.kanjisoup.audio.player.AbstractAudioPlayer;
import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(value = "com.kanjisoup.audio.player.type", havingValue = "clip")
@Slf4j
public class ClipAudioPlayer extends AbstractAudioPlayer {

    public ClipAudioPlayer(AudioPlayerConfig config) {
        basePath = Paths.get(config.getBaseDir());
    }

    @Override
    public void playAudioFile(File audioFile) {
        AudioInputStream audioIn;
        try {
            audioIn = AudioSystem.getAudioInputStream(audioFile);

            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            log.error("Unsupported audio file " + audioFile.getName(), e);
        } catch (IOException e) {
            log.error("IOException opening file " + audioFile, e);
        } catch (LineUnavailableException e) {
            log.error("Line unavailable for playing audio", e);
        }
    }
}
