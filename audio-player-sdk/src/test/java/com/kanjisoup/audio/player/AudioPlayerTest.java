package com.kanjisoup.audio.player;

import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import com.kanjisoup.audio.player.exception.InvalidFileRequestException;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class AudioPlayerTest {

    private AudioPlayer player;

    @Before
    public void setup() {
        AudioPlayerConfig config = new AudioPlayerConfig();
        config.setBaseDir(Paths.get(".").toAbsolutePath().toString());
        player = new AudioPlayer(config);
    }

    @Test(expected = InvalidFileRequestException.class)
    public void testBadFileRequestIsRejected() throws InvalidFileRequestException {
        player.playAudioFile("../../etc/passwd");
    }

    @Test
    public void testValidFileRequestIsSupported() throws InvalidFileRequestException {
        player.playAudioFile("random.wav");
    }
}