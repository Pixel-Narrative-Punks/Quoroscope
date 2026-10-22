# Quoroscope

Quoroscope is a cross-platform application for launching text-based games created for the [QSP platform](https://qsp.org/)

> ⚠️ **Status:** Work in Progress (WIP). In active development.

## Project Structure

The project is split into multiplatform Gradle modules by feature and layer:

- `composeApp` — platform entry points (Android Application & Desktop JVM launcher)
- `core:*` — shared database (Room), network (Ktor), navigation components, common helpers and design system
- `features:<name>` — feature modules with isolated domain and UI logic (presentation via Decompose + MVIKotlin)

**Stack:** Kotlin Multiplatform, Compose Multiplatform (Android + Desktop), Decompose, MVIKotlin, Koin, Ktor, Room, Coroutines & Flow.

## Links to official communities

[Official Telegram chat](https://t.me/QuestopiaChat)
[Official Telegram channel](https://t.me/PixNPunk)

## Similar launch tools

### Desktop-only

[QSP from seedHarta](https://github.com/seedhartha/qsp)

[QSP from Sonnix](https://gitlab.com/Sonnix1/Qqsp)

[QSP from Byte](https://github.com/QSPFoundation/qsp)
