# Audio Player Application

## Purpose
Read from a queue, and play audio based on the messages received.

See https://github.com/sciurolocutus/event-audio for information on the event model.

## Configuration
### Audio player
Built-in, you have a choice of two audio players, `clip` or `external`, `clip` being the default.
```yaml
com.kanjisoup.audio.player.type: clip
# com.kanjisoup.audio.player.type: external
```

`clip` can only play audio file types supported by your JVM / extensions, which
by default likely only supports, `.wav`, `.aiff`, `.au`, and `.midi`.

`external` on the other hand is limited only by what is on your system, and how you configure it,
as it uses a configured external command to "play" the target file. Take care when configuring this one,
for security reasons which should be fairly obvious when executing an arbitrary external command. 

Both players take in a `baseDir` property, to limit the file paths that may be played,
and specify a directory to serve them from. 
By default this is the `audio-player` directory under the executing user's home directory.
You can change it
```yaml
com.kanjisoup.audio:
  player:
    baseDir: "${user.home}/audio-player"
    # baseDir: "/usr/local/share/audio"
```

#### External Audio Player
The external audio player is configured with a working dir, a base command, and arguments
```yaml
com.kanjisoup.audio:
  player:
    type: external
    external:
      baseCommand: '/usr/bin/afplay'
      # baseCommand: '/usr/local/bin/mplayer
      # {0} will be replaced by the full canonical path to the file to be played.
      arguments:
        - '{0}'
      workingDir: ${user.home}

```

`baseCommand` specifies the base command to run.

`arguments` are a list of arguments to the command, one of which should be `{0}`, substituting for the file to be played.
By default this argument list contains exactly one element `{0}` for the filename argument.
One thing to remember when specifying options is that `-option value` is in fact two arguments,
and would be used like so:
```yaml
com.kanjisoup.audio.player.external.arguments:
  - '-option'
  - 'value'
  - '{0}'
```

`workingDir` specifies the working directory that the command will be executed in.
This may be relevant only for a *very* few commands, which may produce or consume files
relative to its current working directory. This is even more limited given the fact
that the target audio file will be passed in as its canonical form.
Still, since the command itself is arbitrary, this is made available in case it is relevant.
When not populated, the command will run in the implicit directory, the directory of the parent process
(i.e. this application's)

### Rabbit Configuration
#### Connection
We leverage Spring rabbit connection properties "baked into" Spring Boot.
See class `org.springframework.boot.autoconfigure.amqp.RabbitProperties` in `spring-boot-autoconfigure-2.2.5.RELEASE.jar`
which loads from `@ConfigurationProperties(prefix = "spring.rabbitmq")`.

Defaults are sensible for most rabbitmq docker images / default installations, but are flexible enough to change via config.
```yaml
spring.rabbitmq:
  host: localhost
  port: 5672
  username: admin
  password: password
  # Other properties are available but not necessary for a simple local example.
```

#### Exchange / Queue Configuration
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
