# SolarisMessageFramework

A small message library for the Solaris server network, built on top of [Adventure](https://docs.advntr.dev/) and [MiniMessage](https://docs.advntr.dev/minimessage/index.html). It gives plugins a centralized place to define server messages in code, with placeholders, formatting, and clickable components supported out of the box, while letting server admins override the wording through a config file without recompiling.

## Features

- **Centralized message registry** — define messages once in code as `MessageKey`s, send them from anywhere.
- **Full MiniMessage support** — placeholders, colors, formatting, click/hover events, all through standard MiniMessage syntax.
- **Config-backed overrides** — every registered message is written to `messages.yml` on first run and can be edited there; edits take priority over the code default.
- **Hot reload** — re-read `messages.yml` at runtime with `reload()`, no restart required.
- **Simple static entry point** — `SolarisMessageFramework.init()` once in `onEnable`, then `registerMessage()` per message.

## Installation

Add [JitPack](https://jitpack.io) as a repository:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Al0ris:SolarisMessageFramework:v0.1.0'
}
```

Replace `v0.1.0` with the latest tagged release.

## Usage

### 1. Define messages

```java
public static final MessageKey ISLAND_CREATE_SUCCESS = MessageKey.of(
    "island.create.success",
    "<green>Created island <yellow><name></yellow>!"
);

public static final MessageKey NO_PERMISSION = MessageKey.of(
    "error.no_permission",
    "<red>You don't have permission to do that. <click:run_command:'/help'><underlined>Need help?</underlined></click>"
);
```

The `id` is the key used in `messages.yml`; the `defaultTemplate` is the MiniMessage string used if no override exists.

### 2. Initialize and register in `onEnable`

```java
@Override
public void onEnable() {
    SolarisMessageFramework.init(this);
    SolarisMessageFramework.registerMessage(MyMessages.ISLAND_CREATE_SUCCESS);
    SolarisMessageFramework.registerMessage(MyMessages.NO_PERMISSION);
}
```

Each registered message is written into `messages.yml` the first time the plugin runs, if it isn't already present.

### 3. Send messages

```java
SolarisMessageFramework.send(player, MyMessages.ISLAND_CREATE_SUCCESS,
    Placeholder.parsed("name", islandName));
```

Or render a bare `Component` (for GUI titles, item lore, etc.) without sending it:

```java
Component message = SolarisMessageFramework.render(MyMessages.NO_PERMISSION);
```

### 4. Reload at runtime

```java
SolarisMessageFramework.reload();
```

Re-reads `messages.yml` from disk. Any edits made to existing keys take effect immediately; any keys missing from the file (e.g. deleted by an admin) are re-populated with their code default.

## Config file

`messages.yml` is auto-generated in the plugin's data folder on first registration:

```yaml
island.create.success: "<green>Created island <yellow><name></yellow>!"
error.no_permission: "<red>You don't have permission to do that. <click:run_command:'/help'><underlined>Need help?</underlined></click>"
```

Edit the values to change wording, formatting, or add click/hover behavior, then reload (or restart) to apply.

## Requirements

- Java 25
- Paper API 26.2+
- Adventure API (bundled with Paper)

## License

[MIT](LICENSE)
