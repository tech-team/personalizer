package util.net;

import java.util.*;

public class Headers implements Iterable<Headers.Header> {

    private Map<String, Header> headers = new HashMap<>();

    public Headers() {
    }

    public Headers add(String name, String value) {
        Header header = headers.get(name);
        if (header == null) {
            header = new Header(name, value);
            headers.put(name, header);
        } else {
            header.set(name, value);
        }
        return this;
    }

    public Headers add(Header h) {
        return add(h.getName(), h.getValue());
    }

    public Header getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Iterator<Header> iterator() {
        return headers.values().iterator();
    }


    public class Header {
        private String name;
        private List<String> value = new LinkedList<>();

        public Header(String name, String value) {
            this.name = name;
            this.value.add(value);
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value.get(0);
        }

        public void set(String name, String value) {
            if (!name.equals(this.name)) {
                throw new RuntimeException("Name is not valid");
            }
            this.value.add(value);
        }

        public List<String> getValues() {
            return value;
        }

        @Override
        public String toString() {
            return name + ": " + value;
        }
    }
}
