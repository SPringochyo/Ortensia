# Ortensia — Менеджер настольных ролевых игр

Десктопное приложение для управления правилами и персонажами настольных RPG (D&D и подобных).  
Написано на **Kotlin + Compose Multiplatform (Desktop)** с бизнес-ядром на **Java**.

---

## Структура проекта

```
src/main/
├── java/dev/springochyo/ortensia/
│   ├── app/
│   │   └── Main.java                     — точка входа Java (тестовый запуск ядра)
│   ├── ruleset/
│   │   ├── core/
│   │   │   ├── RuleSet.java              — сущность «Система правил»
│   │   │   └── StatType.java             — тип характеристики (имя, описание, min/max)
│   │   ├── application/
│   │   │   ├── RuleSetCreator.java       — Builder для создания RuleSet
│   │   │   ├── RuleSetMapper.java        — маппинг RuleSet ↔ DTO
│   │   │   ├── StatTypeMapper.java       — маппинг StatType ↔ DTO
│   │   │   ├── dto/RuleSetDto.java       — DTO для сериализации
│   │   │   ├── dto/StatTypeDto.java      — DTO для StatType
│   │   │   └── port/RuleSetRepository.java — интерфейс репозитория
│   │   └── infrastructure/io/
│   │       ├── FileRuleSetRepository.java — JSON-хранилище правил (gson)
│   │       └── StorageException.java      — исключение IO
│   └── characteer/
│       ├── core/
│       │   ├── Characteer.java            — сущность «Персонаж»
│       │   └── Stat.java                  — текущее значение характеристики
│       ├── application/
│       │   ├── CharacteerCreator.java     — Builder для создания персонажа
│       │   ├── CharacteerMapper.java      — маппинг Characteer ↔ DTO
│       │   ├── InvalidRuleSetException.java
│       │   ├── dto/CharacteerDto.java     — DTO персонажа
│       │   └── port/CharacteerRepository.java — интерфейс репозитория
│       └── infrastructure/io/
│           └── FileCharacteerRepository.java — JSON-хранилище персонажей
│
└── kotlin/dev/springochyo/ortensia/ui/
    ├── Main.kt                            — точка входа GUI (fun main)
    ├── AppViewModel.kt                    — навигация + бизнес-логика GUI
    ├── theme/
    │   ├── OrtensiaTheme.kt              — дизайн-система Caelestia: 4 темы M3
    │   └── AppStrings.kt                 — локализация (RU / EN), LocalStrings
    ├── components/
    │   └── Components.kt                 — переиспользуемые Compose-компоненты:
    │                                        CaelestiaCard, CaelestiaBackButton,
    │                                        CaelestiaHeader, CaelestiaTextField,
    │                                        CaelestiaButton, StatControlRow, SectionLabel
    └── screens/
        ├── MainMenuScreen.kt             — главное меню
        ├── SettingsScreen.kt             — настройки (тема / масштаб / язык)
        ├── RuleSetScreens.kt             — список и просмотр систем правил
        ├── RuleSetCreateScreen.kt        — создание системы правил
        ├── CharacterScreens.kt           — список персонажей, выбор правил
        └── CharacterCreateAndSheetScreens.kt — создание и лист персонажа
```

---

## Архитектурные решения

### Масштабирование UI
Масштаб интерфейса реализован **правильно** — через переопределение `LocalDensity` на верхнем уровне:
```kotlin
CompositionLocalProvider(LocalDensity provides Density(base.density * scale)) { ... }
```
Размер окна не меняется. Все `dp`/`sp` пересчитываются автоматически.

### Цветовая система (Caelestia)
4 полноценные Material3 цветовые схемы в `OrtensiaTheme.kt`:
- **Caelestia Cyan** — тёмный, акцент #89DDFF
- **Nebula Violet** — тёмный, акцент #C792EA
- **Deep Emerald** — тёмный, акцент #80CBC4
- **Light Dawn** — светлая тема

Все компоненты используют только `MaterialTheme.colorScheme.*` — никаких хардкоженных цветов.

### Локализация
Типобезопасный i18n через `CompositionLocalProvider(LocalStrings provides strings)`.  
Переключение языка RU/EN мгновенное, без перезапуска.

---

## Команды сборки и запуска

### Linux

```bash
# Запуск в режиме разработки
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./gradlew run

# Только компиляция
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./gradlew compileKotlin

# Сборка .deb пакета
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./gradlew packageDeb

# Сборка нативного исполняемого файла
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./gradlew packageDistributionForCurrentOS

# Очистка сборки
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./gradlew clean
```

> **Важно:** В `gradle.properties` прописан `org.gradle.java.home=/usr/lib/jvm/java-21-openjdk`.  
> Если путь к JDK 21 отличается, обновите этот файл.

### Windows

```cmd
# Запуск в режиме разработки
gradlew.bat run

# Только компиляция
gradlew.bat compileKotlin

# Сборка .msi установщика
gradlew.bat packageMsi

# Очистка
gradlew.bat clean
```

> На Windows убедитесь, что `JAVA_HOME` указывает на JDK 21.

---

## Требования

| Компонент | Версия |
|-----------|--------|
| JDK | 21 |
| Kotlin | 2.1.0 |
| Compose Multiplatform | 1.7.3 |
| Gradle | 9.x |
| Gson | 2.14.0 |

---

## Хранение данных

Данные сохраняются в JSON-файлы в директории запуска:
- `rulesets/<id>.json` — системы правил
- `characters/<id>.json` — персонажи

---

## Навигация (экраны)

```
MainMenu
├── RuleSetList
│   ├── RuleSetCreate
│   └── RuleSetView
├── CharacterList
│   ├── CharacterSelectRuleSet
│   │   └── CharacterCreate
│   └── CharacterSheet
└── Settings
```
