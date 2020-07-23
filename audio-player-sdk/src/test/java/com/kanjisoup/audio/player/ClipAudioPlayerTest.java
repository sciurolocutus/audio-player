package com.kanjisoup.audio.player;

import com.kanjisoup.audio.event.sdk.domain.PlayEvent;
import com.kanjisoup.audio.player.clip.ClipAudioPlayer;
import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import com.kanjisoup.audio.player.exception.InvalidEventException;
import com.kanjisoup.audio.player.exception.InvalidFileRequestException;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.UUID;

public class ClipAudioPlayerTest {

    private ClipAudioPlayer player;

    private PlayEvent event;

    @Before
    public void setup() {
        AudioPlayerConfig config = new AudioPlayerConfig();
        config.setBaseDir(Paths.get(".").toAbsolutePath().toString());
        player = new ClipAudioPlayer(config);

        event = PlayEvent.builder()
            .uuid(UUID.randomUUID().toString())
            .duration("1s")
            .filePath("random.wav")
            .build();
    }

    @Test(expected = InvalidFileRequestException.class)
    public void testBadFileRequestIsRejected() throws InvalidFileRequestException, InvalidEventException {
        event.setFilePath("../../etc/passwd");
        player.playAudioFile(event);
    }

    @Test
    public void testValidFileRequestIsSupported() throws InvalidFileRequestException, InvalidEventException {
        player.playAudioFile(event);
    }

    @Test(expected = InvalidEventException.class)
    public void testInvalidEventIsRejected() throws InvalidFileRequestException, InvalidEventException {
        event.setFilePath(null);
        event.setUuid(null);
        player.playAudioFile(event);
    }
}