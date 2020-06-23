package com.kanjisoup.audio.player.exception;

import lombok.Getter;

/**
 * Thrown when an invalid file is requested, e.g. one that looks like a directory traversal attack,
 * requesting a file higher up in the directory structure with "../../somefile"
 */
@Getter
public class InvalidFileRequestException extends Exception{
    private String filename;

    public InvalidFileRequestException(String filename) {
        super("Invalid file request: " + filename);
        this.filename = filename;
    }
}
