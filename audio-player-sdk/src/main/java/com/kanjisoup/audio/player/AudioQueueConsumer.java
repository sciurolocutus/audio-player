package com.kanjisoup.audio.player;

import com.google.gson.Gson;
import com.kanjisoup.audio.event.sdk.domain.PlayEvent;
import com.kanjisoup.audio.player.exception.InvalidEventException;
import com.kanjisoup.audio.player.exception.InvalidFileRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Slf4j
@Component
public class AudioQueueConsumer implements MessageListener {

    private final Gson gson;
    private final AbstractAudioPlayer audioPlayer;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     */
    @Autowired
    public AudioQueueConsumer(AbstractAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.gson = new Gson();
    }

    @Override
    public void onMessage(Message message) {
        String encoding = Optional.ofNullable(message.getMessageProperties().getContentEncoding())
            .orElse("UTF-8");
        try {
            String json = new String(message.getBody(),
                encoding);

            PlayEvent event = gson
                .fromJson(json, PlayEvent.class);

            audioPlayer.playAudioFile(event);
        } catch (UnsupportedEncodingException e) {
            log.error("Error decoding message in encoding " + encoding, e);
        } catch (InvalidFileRequestException e) {
            log.error("Invalid file play request: ", e);
        } catch (InvalidEventException e) {
            log.error("Invalid event: ", e);
        }
    }
}
