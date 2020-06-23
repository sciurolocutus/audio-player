package com.kanjisoup.audio.player;

import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import com.kanjisoup.audio.player.exception.InvalidFileRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

@Component
@Slf4j
public class AudioPlayer {
    private Path basePath;

    public AudioPlayer(AudioPlayerConfig config) {
        this.basePath = Paths.get(config.getBaseDir());
    }

    public void playAudioFile(String basename) throws InvalidFileRequestException {
        try {
            // Open an audio input stream.
            File inFile = basePath.resolve(basename).toFile();
            if (!inFile.getCanonicalPath().startsWith(basePath.normalize().toString())) {
                throw new InvalidFileRequestException(basename);
            }
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
