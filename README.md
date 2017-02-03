# libosu
osu! libraries for the JVM.

## Usage

### Replays Example
See `com.zerocooldown.libosu.replay.io.ReplayIO`.

```
Replay play = ReplayIO.read(
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("replays/cookiezi_freedomdive_hdhr.osr"));

/* do interesting thing */

ByteArrayOutputStream out = new ByteArrayOutputStream();
ReplayIO.write(play, out);
```

### Beatmaps example
See `com.zerocooldown.libosu.beatmap.io.BeatmapReader`

```
Beatmap beatmap = BeatmapReader.read(
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("beatmaps/Halozy - Genryuu Kaiko (Hollow Wings) [Higan Torrent].osu");

/* do interesting thing */
```

## Built With
* Java 1.8
* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
