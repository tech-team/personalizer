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
}
