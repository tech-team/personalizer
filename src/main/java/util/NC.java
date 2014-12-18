package util;

/**
 * Null coalescing operations
 */
public class NC {
    public static String toString(Object obj, String _default) {
        if (obj != null)
            return obj.toString();
        else
            return _default;
    }

    public static String toString(Object obj) {
        return toString(obj, "");
    }

    public static Integer parseInt(String str) {
        Integer result = null;

        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException ignored) {}

        return result;
    }
}
