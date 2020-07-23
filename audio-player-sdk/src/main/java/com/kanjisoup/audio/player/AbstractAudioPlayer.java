package com.kanjisoup.audio.player;

import com.kanjisoup.audio.event.sdk.domain.PlayEvent;
import com.kanjisoup.audio.player.exception.InvalidEventException;
import com.kanjisoup.audio.player.exception.InvalidFileRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j
public abstract class AbstractAudioPlayer {
    protected Path basePath;

    public void playAudioFile(PlayEvent event) throws InvalidFileRequestException, InvalidEventException {
        log.debug("Received event {} to play {} for {}",
            event.getUuid(), event.getFilePath(), event.getDuration());

        validateEvent(event);

        String basename = event.getFilePath();
        try {
            // Open an audio input stream.
            File inFile = validateFileLocation(basename);
            playAudioFile(inFile);
        } catch (IOException e) {
            log.error("IOException opening file " + basename, e);
        }
    }

    public abstract void playAudioFile(File audioFile);

    protected void validateEvent(PlayEvent event) throws InvalidEventException {
        if (StringUtils.isEmpty(event.getFilePath())) {
            throw new InvalidEventException("Event's file path is empty.");
        }
    }

    protected File validateFileLocation(String filePath) throws InvalidFileRequestException, IOException {
        File inFile = basePath.resolve(filePath).toFile();
        if (!inFile.getCanonicalPath().startsWith(basePath.toFile().getCanonicalPath())) {
            throw new InvalidFileRequestException(filePath);
        }

        return inFile;
    }
}
