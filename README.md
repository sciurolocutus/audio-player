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

### Rabbit connection
Listen for events from the given exchange, expecting the given exchange type (creating it if it doesn't already exist)
and create a binding to a queue (where the events are really listened from) with the given routing key and queue name.

Different settings may be appropriate depending on the behavior you want,
in a multi-player environment. Broadcast (fanout), but listening on different queues is one.

Defaults:
```yaml
com.kanjisoup.audio:
  queue:
    exchange: event-audio
    exchange-type: fanout
    routing-key: event-audio
    queue-name: ${com.kanjisoup.audio.queue.routing-key}
```

## Security
Though you'll want to evaluate your own exposure/security levels
throughout your use of this, I did implement one type of "security" feature:

The application will *not* attempt to open and play any files that are not in the defined directory
or a subdirectory thereof. Play requests for `../../../../etc/passwd` are rejected,
as are requests for symlinked files whose "hard copy" is outside of
the parent directory.
