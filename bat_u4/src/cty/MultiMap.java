import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;

public class MultiMap<K, V> implements Map {
    private HashMap<K, LinkedList<V>> map;
    private HashMap<V, Integer> elements;
    private final int defaultSize;

    public MultiMap() {
        this(16);
    }

    public MultiMap(int defaultSize) {
        this.defaultSize = defaultSize;
        map = new HashMap<>(this.defaultSize);
        elements = new HashMap<>(this.defaultSize);
    }

    public boolean keyContainsValue(Object key, Object value) {
        LinkedList<V> values = map.get(key);
        if (values == null) { return false; }
        return values.contains(value);
    }

    private class MapIterator implements Iterator {
        private Iterator<V> iterator = null;
        private V next = null;

        private MapIterator(Iterator<V> i) {
            this.iterator = i;
        }

        private MapIterator() {}

        @Override
        public boolean hasNext() {
            if (iterator == null) { return false; }
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            if (iterator == null) { throw new NoSuchElementException(); }
            next = iterator.next();
            return next;
        }

        @Override
        public void remove() {
            if (next == null) {
                throw new IllegalStateException();
            }
            iterator.remove();
            int times = elements.get(next) - 1;
            if (times <= 0) {
                elements.remove(next);
            } else {
                elements.put(next, times);
            }
        }
    }

    public Iterator<V> keyIterator(Object key) {
        LinkedList<V> values = map.get(key);
        if (values == null) { return new MapIterator(); }
        return new MapIterator(values.iterator());
    }

    public int keySize(Object key) {
        LinkedList<V> values = map.get(key);
        if (values == null) { return 0; }
        return values.size();
    }

    public int keySize() {
        return map.size();
    }

    public boolean keyIsUnique(Object key) {
        LinkedList<V> values = map.get(key);
        if (values == null) { return true; }
        return values.size() <= 1;
    }

    public int realSize() { return elements.size(); }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return elements.containsKey(value);
    }

    @Override
    public V get(Object key) {
        LinkedList<V> values = map.get(key);
        if (values == null || values.isEmpty()) { return null; }
        return values.peekFirst();
    }

    @Override
    public V put(Object key, Object value) {
        LinkedList<V> values = map.get(key);
        V res = null;
        if (values == null) {
            values = new LinkedList<>();
            map.put((K) key, values);
        }
        if (!values.isEmpty()) {
            res = values.peekLast();
        }
        values.addLast((V) value);
        elements.put((V) value, elements.getOrDefault(value, 0) + 1);
        return res;
    }

    @Override
    public LinkedList<V> remove(Object key) {
        LinkedList<V> res = map.remove(key);
        for (V v: res) {
            elements.put(v, elements.get(v) - 1);
        }
        return res;
    }

    @Override
    public void putAll(Map m) {
        if (m instanceof MultiMap) {
            for (Map.Entry<K, LinkedList<V>> e:
                    ((MultiMap<K, V>) m).entrySet()) {
                K key = e.getKey();
                for (V value: e.getValue()) {
                    put(key, value);
                }
            }
        } else {
            for (Map.Entry<K, V> e: ((Map<K, V>) m).entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public void clear() {
        map.clear();
        elements.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return elements.keySet();
    }

    public V firstValue() {
        if (elements.isEmpty()) { return null; }
        Iterator<Map.Entry<V, Integer>> ei = elements.entrySet().iterator();
        return ei.next().getKey();
    }

    @Override
    public Set<Entry<K, LinkedList<V>>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        Object res = get(key);
        if (res == null) { return defaultValue; }
        return res;
    }
}
