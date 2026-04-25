# ☠ God Gun Mod — Forge 1.20.1 / 47.4.10

## What it does
- Hold **right-click** with the God Gun to fire full-auto
- Instantly kills **everything in your FOV** (53° cone, 150 block range)
- Custom death message: **"PlayerName was obliterated off of the face of the earth by YourName"**
- Works on your own server even if your friend doesn't have the mod installed (see below)

---

## Setup (compiling from source)

### Requirements
- JDK 17 (https://adoptium.net)
- The Forge 1.20.1 MDK (https://files.minecraftforge.net → 1.20.1 → 47.4.10 → MDK)

### Steps
1. Download and unzip the Forge MDK
2. Copy the `src/` folder and `build.gradle`, `gradle.properties`, `settings.gradle`
   from this project INTO the MDK folder (replace the existing ones)
3. Open a terminal in the MDK folder and run:
   ```
   ./gradlew build        (Mac/Linux)
   gradlew.bat build      (Windows)
   ```
4. The compiled `.jar` will be in `build/libs/godgun-1.0.0.jar`
5. Drop that `.jar` into your server's `mods/` folder and your own client `mods/` folder

---

## Getting the gun in-game

Once the mod is on the server, run this command (needs op):
```
/godgun give
```

Or the vanilla give command:
```
/give @s godgun:god_gun
```

---

## How it works on your friend's unmodded client

- **Server has the mod** → Float.MAX_VALUE damage per tick, kills anything including OP withers instantly
- **Your friend has no mod** → They don't need it! The killing happens server-side when YOU use the gun
  - Your friend just plays normally — the wither dies, they don't need to install anything

The client-side fallback also lets the gun work even if you somehow join a server
without the mod installed — it sends vanilla attack packets rapidly to everything in your FOV.
Damage won't be Float.MAX_VALUE in that case, but it still rapid-attacks.

---

## Troubleshooting
- **"Unknown command /godgun"** → Mod isn't loaded on the server. Drop the jar in `mods/` and restart.
- **Gun does nothing** → Make sure you're holding it in your MAIN hand and right-clicking.
- **Wither survives** → Make sure the server has the mod. Client-only mode uses vanilla damage which won't kill an OP wither.
