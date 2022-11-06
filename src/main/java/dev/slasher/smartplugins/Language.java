package dev.slasher.smartplugins;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import dev.slasher.smartplugins.plugin.config.SConfig;
import dev.slasher.smartplugins.plugin.config.SWriter;
import dev.slasher.smartplugins.plugin.config.SWriter.YamlEntry;
import dev.slasher.smartplugins.plugin.logger.SLogger;
import dev.slasher.smartplugins.utils.StringUtils;

@SuppressWarnings("rawtypes")
public class Language {
    public static String queue$actionbar$message = "§eYou are in the queue to join the server: §b{server} §7(Position #{id})";

    public static final SLogger LOGGER = ((SLogger) Core.getInstance().getLogger())
            .getModule("Language");
    private static final SConfig CONFIG = Core.getInstance().getConfig("Language");

    public static void setupLanguage() {
        boolean save = false;
        SWriter writer = Core.getInstance().getWriter(CONFIG.getFile(),
                "sCore - Developed by Smart Plugins Team\nVersion file: " + Core.getInstance()
                        .getDescription().getVersion());
        for (Field field : Language.class.getDeclaredFields()) {
            if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
                String nativeName = field.getName().replace("$", ".").replace("_", "-");

                try {
                    Object value;
                    SWriter.YamlEntryInfo entryInfo = field.getAnnotation(SWriter.YamlEntryInfo.class);

                    if (CONFIG.contains(nativeName)) {
                        value = CONFIG.get(nativeName);
                        if (value instanceof String) {
                            value = StringUtils.formatColors((String) value).replace("\\n", "\n");
                        } else if (value instanceof List) {
                            List l = (List) value;
                            List<Object> list = new ArrayList<>(l.size());
                            for (Object v : l) {
                                if (v instanceof String) {
                                    list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                                } else {
                                    list.add(v);
                                }
                            }

                            value = list;
                        }

                        field.set(null, value);
                        writer.set(nativeName, new YamlEntry(
                                new Object[]{entryInfo == null ? "" : entryInfo.annotation(),
                                        CONFIG.get(nativeName)}));
                    } else {
                        value = field.get(null);
                        if (value instanceof String) {
                            value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
                        } else if (value instanceof List) {
                            List l = (List) value;
                            List<Object> list = new ArrayList<>(l.size());
                            for (Object v : l) {
                                if (v instanceof String) {
                                    list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                                } else {
                                    list.add(v);
                                }
                            }

                            value = list;
                        }

                        save = true;
                        writer.set(nativeName, new YamlEntry(
                                new Object[]{entryInfo == null ? "" : entryInfo.annotation(), value}));
                    }
                } catch (ReflectiveOperationException e) {
                    LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
                }
            }
        }

        if (save) {
            writer.write();
            CONFIG.reload();
        }
    }
}