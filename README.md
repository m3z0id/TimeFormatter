# TimeFormatter

[![MIT License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A simple client-side Minecraft mod that automatically finds and formats Discord-style timestamps in chat, converting them to your local time.

## Features

-   **Client-Side Only**: No server-side installation is required. It works on any server you join. The formatting works only with the mod installed.
-   **Automatic Formatting**: Detects timestamps like `<t:1672531200>` in chat messages.
-   **Literal Sending** (not timezone aware): If a timestamp like `<<t:1672531200>>` is sent in the chat, it will get replaced by a matching literal assuming it won't overflow the 256 character limit, thus visible to everyone.
-   **Local Time Conversion**: All timestamps are converted and displayed in your computer's local time zone.
-   **Multiple Formats**: Supports all of Discord's standard timestamp formatting styles.
-   **Relative Time**: Can display time relatively, such as "5 minutes ago" or "in 2 hours".

## How It Works

The mod checks incoming chat messages for timestamps like `<t:TIMESTAMP:STYLE>` (or `<<t:TIMESTAMP:STYLE>>`).

-   `TIMESTAMP`: A Unix timestamp (in seconds). You can also use the special values `yesterday`, `now` and `tomorrow` to represent the respective time.
-   `STYLE`: An optional single character that controls the output format. If you don't provide a style, it defaults to `f` (Short Date/Time).

### Supported Styles

| Style | Example Output                       | Description               |
|:-----:|:-------------------------------------|:--------------------------|
|  `t`  | `14:30`                              | Short Time                |
|  `T`  | `14:30:00`                           | Long Time                 |
|  `d`  | `31/12/2023`                         | Short Date                |
|  `D`  | `December 31, 2023`                  | Long Date                 |
|  `f`  | `December 31, 2023 at 14:30`         | Short Date/Time (Default) |
|  `F`  | `Sunday, December 31, 2023 at 14:30` | Long Date/Time            |
|  `R`  | `5 minutes ago`                      | Relative Time             |

### Example

If another player let's say in the UTC timezone sends the following message in chat:
> `The server event will start at <t:1704067200:F>.`

And you're in the GMT+1 timezone, you'll see:
> `The server event will start at Monday, January 1, 2024 at 01:00.`


However, if the player in UTC sends this message in chat:
> `My date and time is <<t:1704067200:F>>.`

Everyone (even players without this mod) will see this:
> `The server event will start at Monday, January 1, 2024 at 01:00.`

## Installation

This is a Fabric mod and requires the [Fabric API](https://modrinth.com/mod/fabric-api).

1.  Download the latest version.
2.  Place the downloaded `.jar` file into your `.minecraft/mods` folder.
3.  Launch the game using the Fabric Loader.

## Building from Source

1.  Clone the repository: `git clone https://github.com/m3z0id/TimeFormatter.git`
2.  Navigate into the project directory: `cd TimeFormatter`
3.  Build the project using Gradle: `./gradlew build -Pmc=<Minecraft Version> -Pyarn=<Yarn Version> -Papi=<Fabric API Version> -Padventure=<Koryo Adventure Fabric Version>`
4.  The compiled `.jar` file will be located in the `build/libs/` directory.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.