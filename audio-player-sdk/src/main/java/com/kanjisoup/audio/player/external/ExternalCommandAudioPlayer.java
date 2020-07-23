package com.kanjisoup.audio.player.external;

import com.kanjisoup.audio.player.AbstractAudioPlayer;
import com.kanjisoup.audio.player.config.AudioPlayerConfig;
import com.kanjisoup.audio.player.external.config.ExternalAudioPlayerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@ConditionalOnProperty(value = "com.kanjisoup.audio.player.type", havingValue = "external")
@Slf4j
public class ExternalCommandAudioPlayer extends AbstractAudioPlayer {
    private ProcessBuilder processBuilder;
    private ExternalAudioPlayerConfig externalPlayerConfig;

    public ExternalCommandAudioPlayer(AudioPlayerConfig config, ExternalAudioPlayerConfig externalPlayerConfig) {
        this.basePath = Paths.get(config.getBaseDir());
        processBuilder = new ProcessBuilder();

        Objects.requireNonNull(externalPlayerConfig.getBaseCommand());
        if (externalPlayerConfig.getWorkingDir() != null) {
            processBuilder.directory(new File(externalPlayerConfig.getWorkingDir()));
        }
        this.externalPlayerConfig = externalPlayerConfig;
    }

    @Override
    public void playAudioFile(File audioFile) {
        try {
            String pathToFile = audioFile.getCanonicalPath();
            String command = externalPlayerConfig.getBaseCommand();

            List<String> commandAndArgs = new ArrayList<>();
            commandAndArgs.add(command);
            for (String arg : externalPlayerConfig.getArguments()) {
                commandAndArgs.add(MessageFormat.format(arg, pathToFile));
            }

            log.debug("Executing external command {}", String.join(" ", commandAndArgs));

            processBuilder
                .command(commandAndArgs)
                .start();
        } catch (IOException e) {
            log.error("IOException opening file " + audioFile.getName(), e);
        }
    }
}
