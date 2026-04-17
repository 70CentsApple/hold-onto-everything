package net.apple70cents.holdontoeverything.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.*;

import java.util.Map;
import java.util.regex.Pattern;

//? if >=1.21.6 {
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
//?} elif >=1.20.5 {
/*import net.minecraft.data.registries.VanillaRegistries;
*///?}

/**
 * @author 70CentsApple
 */
public class TextUtils {
    public static final String PREFIX = "key.hold_onto_everything.";

    public static Component literal(String str) {
        //? if >=1.19 {
        return Component.literal(str);
        //? } else {
        /*return new TextComponent(str);
        *///? }
    }

    public static Component transWithPrefix(String str, String prefix) {
        //? if >=1.19 {
        return Component.translatable(prefix + str);
        //? } else {
        /*return new TranslatableComponent(prefix + str);
        *///? }
    }

    public static Component transWithPrefix(String str, String prefix, Object... args) {
        //? if >=1.19 {
        return Component.translatable(prefix + str, args);
        //? } else {
        /*return new TranslatableComponent(prefix + str, args);
        *///? }
    }

    public static Component trans(String str, Object... args) {
        return of(transWithPrefix(str, PREFIX, args).getString().strip());
    }

    public static Component trans(String str) {
        return of(transWithPrefix(str, PREFIX).getString().strip());
    }

    public static Component of(String str) {
        return Component.nullToEmpty(str);
    }

    public static Component empty() {
        //? if >=1.19 {
        return Component.empty();
        //? } else {
        /*return of("");
        *///? }
    }

    /**
     * removes color codes in the string
     *
     * @param str the string
     * @return string with no color codes
     */
    public static String wash(String str) {
        return Pattern.compile("§.").matcher(str).replaceAll("");
    }

    public static String escapeColorCodes(String str) {
        return str.replace('&', '§').replace("\\§", "&");
    }

    public static String backEscapeColorCodes(String str) {
        return str.replace('§', '&');
    }

    private static void replaceFieldValue(JsonElement jsonElement, String oldValue, String newValue) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> ele : jsonObject.entrySet()) {
                String key = ele.getKey();
                JsonElement value = ele.getValue();
                if (value.isJsonPrimitive() && value.getAsString().contains(oldValue)) {
                    jsonObject.addProperty(key, value.getAsString().replace(oldValue, newValue));
                } else {
                    replaceFieldValue(value, oldValue, newValue);
                }
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                replaceFieldValue(element, oldValue, newValue);
            }
        }
    }
}
