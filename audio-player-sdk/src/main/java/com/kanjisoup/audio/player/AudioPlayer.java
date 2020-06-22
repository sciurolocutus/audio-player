package com.kanjisoup.audio.player;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import javax.sound.sampled.*;

public class AudioPlayer {
  private String basePath;

  public AudioPlayer(String basePath) {
    this.basePath = basePath;
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
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
}
