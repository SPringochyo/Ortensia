package dev.springochyo.ortensia.ruleset.infrastructure.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import dev.springochyo.ortensia.ruleset.application.RuleSetMapper;
import dev.springochyo.ortensia.ruleset.application.dto.RuleSetDto;
import dev.springochyo.ortensia.ruleset.application.port.RuleSetRepository;
import dev.springochyo.ortensia.ruleset.core.RuleSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class FileRuleSetRepository implements RuleSetRepository {
    private final Path storagePath;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileRuleSetRepository() {
        this(Path.of("storage/rulesets"));
    }

    public FileRuleSetRepository(Path storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public void save(RuleSet ruleSet) {
        String fileName = ruleSet.getRulesetId() + ".json";
        Path fullPath = storagePath.resolve(fileName);

        try {
            Files.createDirectories(storagePath);

            try (BufferedWriter writer = Files.newBufferedWriter(fullPath)) {
                gson.toJson(RuleSetMapper.toDto(ruleSet), writer);
            }

        } catch (IOException | JsonParseException e) {
            throw new StorageException("Failed to save ruleset: " + ruleSet.getRulesetId(), e);
        }
    }

    @Override
    public RuleSet load(String ruleSetId) {
        Path fullPath = storagePath.resolve(ruleSetId + ".json");

        if (!Files.exists(fullPath))
            throw new NoSuchElementException("Ruleset not found: " + ruleSetId);

        try (BufferedReader reader = Files.newBufferedReader(fullPath)) {
            RuleSetDto dto = gson.fromJson(reader, RuleSetDto.class);
            if (dto == null)
                throw new StorageException("Ruleset file is empty: " + ruleSetId);
            return RuleSetMapper.toObj(dto);
        } catch (IOException | JsonParseException e) {
            throw new StorageException("Failed to read ruleset: " + ruleSetId, e);
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
            throw new StorageException("Failed to list ruleset files", e);
        }
    }
}
