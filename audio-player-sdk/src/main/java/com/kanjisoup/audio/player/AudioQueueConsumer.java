package com.kanjisoup.audio.player;

import com.google.gson.Gson;
import com.kanjisoup.audio.event.sdk.domain.PlayEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AudioQueueConsumer {

  private final Gson gson;
  private final AudioPlayer audioPlayer;

  /**
   * Constructs a new instance and records its association to the passed-in channel.
   *
   * @param audioPlayer
   */
  @Autowired
  public AudioQueueConsumer(AudioPlayer audioPlayer) {
    this.audioPlayer = audioPlayer;
    this.gson = new Gson();
  }

  public void receiveMessage(String message) {

    PlayEvent event = gson
        .fromJson(message, PlayEvent.class);

    log.debug("Received event {} to play {} for {}",
        event.getUuid(), event.getFilePath(), event.getDuration());
    audioPlayer.playAudioFile(event.getFilePath());
  }
}
