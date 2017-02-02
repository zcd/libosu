# libosu
osu! libraries for the JVM.

## Usage
`com.zerocooldown.libosu.replay.io.ReplayIO` contains all the interesting bits.

Example usage:

```
Replay play = ReplayIO.read(
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("replays/cookiezi_freedomdive_hdhr.osr"));

/* do interesting thing */

ByteArrayOutputStream out = new ByteArrayOutputStream();
ReplayIO.write(play, out);
```

## Built With
* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details