package com.kanjisoup.audio.player.external.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "com.kanjisoup.audio.player.external")
@Data
public class ExternalAudioPlayerConfig {

    /**
     * A base command to execute, preferably
     * E.g. "/usr/local/bin/mplayer"
     */
    private String baseCommand;

    /**
     * A list of arguments, one of which should be {0}, to be replaced by the target filename.
     * This will maintain its order, in case that matters.
     * E.g. ['-loop', '3', '{0}']
     *
     * Default is ['{0}'], i.e. just the target filename.
     */
    private List<String> arguments = List.of("{0}");

    /**
     * If specified, the command would be executed in this directory.
     * Could be relevant to certain executables if they read or produce files relative to the current directory.
     */
    private String workingDir;
}
