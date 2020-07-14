# Audio Player Application

## Purpose
Read from a queue, and play audio based on the messages received.

See https://github.com/sciurolocutus/event-audio for information on the event model.

## Configuration
### Base Directory

By default, this is configured to play audio files 
```yaml
com.kanjisoup.audio:
  player:
    baseDir: "${user.home}/audio-player"
```

## Security
Though you'll want to evaluate your own exposure/security levels
throughout your use of this, I did implement one type of "security" feature:

The application will *not* attempt to open and play any files that are not in the defined directory
or a subdirectory thereof. Play requests for `../../../../etc/passwd` are rejected,
as are requests for symlinked files whose "hard copy" is outside of
the parent directory.
