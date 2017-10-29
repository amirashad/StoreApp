package az.store.configs;

import java.util.*;

/**
 * @author Rashad Amirjanov
 */
public class Config {

    private static final List<Config> allconfigs = new ArrayList<>();
    private ResourceBundle resourceBundle = null;
    private TreeMap<String, String> configs = new TreeMap<>();

    public Config(String fileName) throws MissingResourceException {
        try {
            resourceBundle = ResourceBundle.getBundle(fileName);
        } catch (MissingResourceException ex) {
            System.err.format("Missing %s.properties!\n" + ex.getMessage(), fileName);
            throw ex;
        }

        Enumeration<String> v = resourceBundle.getKeys();
        while (v.hasMoreElements()) {
            String key = v.nextElement();
            String val = resourceBundle.getString(key);
            configs.put(key, val);
        }
    }

    public static Config instance(String configFileName) {
        Config config = null;
        int idx = allconfigs.indexOf(configFileName);
        if (idx != -1) {
            config = allconfigs.get(idx);
        }

        if (config == null) {
            config = new Config(configFileName);
        }

        return config;
    }

    public String getFileName() {
        return resourceBundle.getBaseBundleName();
    }

    public String value(String key) {
        return configs.get(key);
    }

    public String getString(String key) {
        return configs.get(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Config) {
                Config c = (Config) obj;
                if (Objects.equals(c.getFileName(), this.getFileName())) {
                    return true;
                }
            } else if (obj instanceof String) {
                String s = (String) obj;
                if (Objects.equals(s, this.getFileName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
