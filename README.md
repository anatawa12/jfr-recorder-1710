# JFR Recorder for minecraft 1.7.10

executes Java Flight Recorder ported to openjdk 8 in minecraft!

## Limitations

This mod uses Java Flight Recorder ported to openjdk 8, so you have to 
use an openjdk 8u262 or later for server-side.

The Minecraft's build-in Java is JRE 8u51, 
so we cannot use this mod without changing java if you want to record single play. 
Please install the latest some openjdk and configure to use it for booting Minecraft.

If want to record multi-play server, you have to use openjdk 8u262 or later to boot Minecraft Server.
You don't have to configure Java for booting Minecraft client.

## How-to-use

- ``/jfr config <file name>``
> set configuration to `<file name>`. 
> the `<file name>` is path to file in the client.
> special name `default` and `profile` are used to select JDK build-in configuration.

- ``/jfr start``
> starts jfr recording

- ``/jfr stop``
> stops jfr recording

# LICENSE 

MIT License
