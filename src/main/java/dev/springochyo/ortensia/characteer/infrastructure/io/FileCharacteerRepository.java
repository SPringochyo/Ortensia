package dev.springochyo.ortensia.characteer.infrastructure.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import dev.springochyo.ortensia.characteer.application.CharacteerMapper;
import dev.springochyo.ortensia.characteer.application.dto.CharacteerDto;
import dev.springochyo.ortensia.characteer.application.port.CharacteerRepository;
import dev.springochyo.ortensia.characteer.core.Characteer;
import dev.springochyo.ortensia.ruleset.core.RuleSet;
import dev.springochyo.ortensia.ruleset.infrastructure.io.StorageException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class FileCharacteerRepository implements CharacteerRepository {
    private final Path storagePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileCharacteerRepository() {
        this(Path.of("storage/characters"));
    }

    public FileCharacteerRepository(Path storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public void save(Characteer characteer) {
        String fileName = characteer.getName() + "_" + characteer.getRulesetId() + ".json";
        Path fullPath = storagePath.resolve(fileName);

        try {
            Files.createDirectories(storagePath);

            try (BufferedWriter writer = Files.newBufferedWriter(fullPath)) {
                gson.toJson(CharacteerMapper.toDto(characteer), writer);
            }

        } catch (IOException | JsonParseException e) {
            throw new StorageException("Failed to save ruleset: " + characteer.getName(), e);
        }
    }

    @Override
    public Characteer load(RuleSet ruleSet, String characterName) {
        Path fullPath = storagePath.resolve(characterName + ".json");

        if (!Files.exists(fullPath))
            throw new NoSuchElementException("Character not found: " + characterName);

        try (BufferedReader reader = Files.newBufferedReader(fullPath)) {
            CharacteerDto dto = gson.fromJson(reader, CharacteerDto.class);
            if (dto == null)
                throw new StorageException("Character file is empty: " + characterName);
            return CharacteerMapper.toObj(ruleSet, dto);
        } catch (IOException | JsonParseException e) {
            throw new StorageException("Failed to read character: " + characterName, e);
        }
    }

    @Override
    public List<String> list() {
        if (!Files.exists(storagePath))
            return Collections.emptyList();

        try (Stream<Path> stream = Files.list(storagePath)) {
            return stream
                    .filter(path -> !Files.isDirectory(path) && path.toString().endsWith(".json"))
                    .map(path -> path.getFileName().toString().replace(".json", ""))
                    .toList();
        } catch (IOException e) {
            throw new StorageException("Failed to list character files", e);
        }
    }
}
