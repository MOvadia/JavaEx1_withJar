package Base;

import java.util.HashMap;
import java.util.Map;

public class StringToMap {

    public static Map<String, String> convert(String string) {
        Map<String, String> map = new HashMap<>();
        if (string != null) {
            String parts[] = string.split(",");
            for (String part : parts) {
                String Data[] = part.split("=");
                String key = Data[0].trim();
                String value = Data[1].trim();
                map.put(key, value);
            }
        }
        return map;
    }
}
