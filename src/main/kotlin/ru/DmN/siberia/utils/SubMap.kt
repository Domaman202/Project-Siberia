package ru.DmN.siberia.utils

class SubMap<K, V>(val parent: Map<K, V>, val map: MutableMap<K, V> = HashMap()) : MutableMap<K, V> {
    override val size: Int
        get() = map.size + parent.size
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = (map.entries + parent.entries).toMutableSet() as MutableSet<MutableMap.MutableEntry<K, V>>
    override val keys: MutableSet<K>
        get() = (map.keys + parent.keys).toMutableSet()
    override val values: MutableCollection<V>
        get() = (map.values + parent.values).toMutableSet()

    override fun clear() =
        map.clear()

    override fun isEmpty(): Boolean =
        map.isEmpty() && parent.isEmpty()

    override fun remove(key: K): V? =
        map.remove(key)

    override fun putAll(from: Map<out K, V>) =
        map.putAll(from)

    override fun put(key: K, value: V): V? =
        map.put(key, value)

    override fun get(key: K): V? =
        if (map.containsKey(key))
            map[key]
        else parent[key]

    override fun containsValue(value: V): Boolean =
        map.containsValue(value) || parent.containsValue(value)

    override fun containsKey(key: K): Boolean =
        map.containsKey(key) || parent.containsKey(key)
}