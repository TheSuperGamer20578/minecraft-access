package org.mcaccess.minecraftaccess.utils;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcaccess.minecraftaccess.MainClass;
import org.mcaccess.minecraftaccess.mixin.I18NAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public class Translation {
    private static final @NotNull Pattern PLACEHOLDER_REGEX = Pattern.compile("\\{(?<placeholder>\\w+)}");

    private final @NotNull String key;
    private @Nullable String variant;
    private @Nullable String fallback;
    private final @NotNull Map<@NotNull String, @NotNull String> variables = new HashMap<>();

    @Contract(pure = true)
    public Translation(@NotNull String key) {
        this.key = key;
        if (key.contains("#")) {
            log.warn("Translation#varient(String) should be used for variants. Key: {}", key);
        }
    }

    @Contract(pure = true)
    private @NotNull String getKey() {
        return variant != null ? String.format("%s#%s", key, variant) : key;
    }

    @Contract(pure = true)
    private @NotNull String getRaw() {
        return I18NAccessor.getLanguage().getOrDefault(getKey(), fallback != null ? fallback : getKey());
    }

    @Contract(pure = true)
    public boolean exists() {
        return I18NAccessor.getLanguage().has(getKey());
    }

    @Contract(pure = true)
    public @NotNull String getString() {
        if (!exists() && fallback == null) {
            log.warn("Untranslated key: {}", getKey());
        }
        return PLACEHOLDER_REGEX.matcher(getRaw()).replaceAll(match -> {
            String placeholder = match.group("placeholder");
            if (!variables.containsKey(placeholder)) {
                log.warn("Missing placeholder {} for string {}", placeholder, getKey());
            }
            return variables.getOrDefault(placeholder, "");
        });
    }

    public void narrate(boolean interrupt) {
        MainClass.speakWithNarrator(getString(), interrupt);
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull Translation variant(@Nullable String variant) {
        this.variant = variant;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull Translation fallback(@Nullable String fallback) {
        this.fallback = fallback;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, @NotNull String value) {
        variables.put(placeholder, value);
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, byte value) {
        return set(placeholder, String.valueOf(value));
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, short value) {
        return set(placeholder, String.valueOf(value));
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, int value) {
        return set(placeholder, String.valueOf(value));
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, long value) {
        return set(placeholder, String.valueOf(value));
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, float value) {
        return set(placeholder, NarrationUtils.narrateNumber(value));
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, double value) {
        return set(placeholder, NarrationUtils.narrateNumber(value));
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, @NotNull Component value) {
        return set(placeholder, value.getString());
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull Translation set(@NotNull String placeholder, @NotNull FormattedCharSequence value) {
        return set(placeholder, StringUtils.formattedCharSequenceToString(value));
    }
}
