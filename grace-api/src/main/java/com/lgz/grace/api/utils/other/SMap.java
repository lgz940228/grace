package com.lgz.grace.api.utils.other;

import com.lgz.grace.api.utils.collection.ListUtil;
import com.lgz.grace.api.utils.num.NumUtil;
import com.lgz.grace.api.utils.time.TimeUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;

public class SMap<K, V> implements Map<K, V>, Serializable {
    private final Map<K, V> map;

    public static <K, V> SMap<K, V> map(Object... objects) {
        return getSMap(ListUtil.createMapNoCheck(objects));
    }

    public static <K, V> SMap<K, V> getSMap(Map<K, V> map) {
        if (map instanceof SMap) {
            return (SMap) map;
        } else if (map != null) {
            return new SMap<K, V>(map);
        } else {
            return new SMap<K, V>();
        }
    }

    public SMap() {
        this(new HashMap<K, V>(), true);
    }

    public SMap(int size) {
        this(new HashMap<K, V>(size), true);
    }

    public SMap(int size, float loadFactor) {
        this(new HashMap<K, V>(size, loadFactor), true);
    }

    public SMap(Map<K, V> map) {
        this(map, true);
    }

    public SMap(Map<K, V> map, boolean proxy) {
        if (map == null) {
            this.map = new HashMap<K, V>();
        } else if (proxy) {
            this.map = map;
        } else {
            this.map = new HashMap<K, V>(map);
        }
    }

    public SMap getSMap(Object o) {
        Object map = get(o);
        if (map instanceof SMap) {
            return (SMap) map;
        } else if (map instanceof Map) {
            return new SMap((Map) map);
        }
        return null;
    }

    public V getFirstValue() {
        Iterator<V> it = map.values().iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public String getString(Object o) {
        Object obj = get(o);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    public V putNotNull(K key, V v) {
        if (v == null) {
            return null;
        }
        return put(key, v);
    }

    public V putNotEmpty(K key, V v) {
        if (v == null) {
            return null;
        } else if (v instanceof String && StringUtils.isEmpty(v.toString())) {
            return null;
        }
        return put(key, v);
    }

    public Map getMap(Object o) {
        return get(o, Map.class);
    }

    public Boolean getBoolean(Object o) {
        Object v = get(o);
        if (v instanceof Boolean) {
            return (Boolean) v;
        } else if (v instanceof String) {
            return Boolean.parseBoolean(v.toString());
        }
        return null;
    }

    public Integer getInt(Object o) {
        return NumUtil.parseInt(get(o));
    }

    public Long getLong(Object o) {
        return NumUtil.parseLong(get(o));
    }

    public Double getDouble(Object o) {
        return NumUtil.parseDouble(get(o));
    }

    public Date getDate(Object o) {
        return TimeUtil.parseDate(get(o));
    }

    public <P> P get(Object o, Class<P> clazz) {
        Object obj = get(o);
        if (obj != null && clazz.isAssignableFrom(obj.getClass())) {
            return (P) obj;
        }
        return null;
    }

    public <T> T getV(Object key) {
        return (T) map.get(key);
    }

    /**
     * 根据json格式转化对象为指定类对象
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> T transformByJSON(Class<T> t) {
        return null;
        //return JSONUtil.transform(this, t);
    }

    @Deprecated
    public static <K, V> SMap<K, V> getSuperMap(Map<K, V> map) {
        return getSMap(map);
    }

    @Deprecated
    public SMap getSupperMap(Object o) {
        return getSMap(o);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
