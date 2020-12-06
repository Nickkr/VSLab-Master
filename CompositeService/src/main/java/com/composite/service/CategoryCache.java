package com.composite.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryCache implements Map<Integer, Category>, Serializable {

	private static final long serialVersionUID = -2060332784605672379L;

	private Map<Integer, Category> cache = new TreeMap<Integer, Category>();

	@Bean
	@Deprecated
	public CategoryService defaultDataServiceDummy() {
		return new CategoryService() {
			public Category getCategory(Integer id) {
				return new Category(id , "Test");
			}
		};
	}
	
	/** Adds all elements from the categories collection and replace existing elements. */
	public void putAll(Category[] categories) {
		cache.putAll(Arrays.stream(categories).collect(Collectors.toMap(Category::getId, Function.identity())));
	}

	/** Removes all elements and adds all from the categories collection. */
	public void initialize(Category[] categories) {
		clear();
		putAll(categories);
	}

	public Category putIfAbsent(Category value) {
		return cache.putIfAbsent(value.getId(), value);
	}
	
	public Category put(Category value) {
		return cache.put(value.getId(), value);
	}

	public Category remove(Integer key) {
		return cache.remove(key);
	}

	public boolean remove(Category value) {
		return cache.remove(value.getId(), value);
	}

	public boolean replace(Category oldValue, Category newValue) {
		return cache.replace(oldValue.getId(), oldValue, newValue);
	}

	public Category replace(Category value) {
		return cache.replace(value.getId(), value);
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	@Override
	public Category get(Object key) {
		return cache.get(key);
	}

	@Override
	public Category put(Integer key, Category value) {
		return cache.put(key, value);
	}

	@Override
	public Category remove(Object key) {
		return cache.remove(key);
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends Category> m) {
		cache.putAll(m);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Set<Integer> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<Category> values() {
		return cache.values();
	}

	@Override
	public Set<Entry<Integer, Category>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return cache.equals(o);
	}

	@Override
	public int hashCode() {
		return cache.hashCode();
	}

	@Override
	public Category getOrDefault(Object key, Category defaultValue) {
		return cache.getOrDefault(key, defaultValue);
	}

	@Override
	public void forEach(BiConsumer<? super Integer, ? super Category> action) {
		cache.forEach(action);
	}

	@Override
	public void replaceAll(BiFunction<? super Integer, ? super Category, ? extends Category> function) {
		cache.replaceAll(function);
	}

	@Override
	public Category putIfAbsent(Integer key, Category value) {
		return cache.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return cache.remove(key, value);
	}

	@Override
	public boolean replace(Integer key, Category oldValue, Category newValue) {
		return cache.replace(key, oldValue, newValue);
	}

	@Override
	public Category replace(Integer key, Category value) {
		return cache.replace(key, value);
	}

	@Override
	public Category computeIfAbsent(Integer key, Function<? super Integer, ? extends Category> mappingFunction) {
		return cache.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public Category computeIfPresent(Integer key, BiFunction<? super Integer, ? super Category, ? extends Category> remappingFunction) {
		return cache.computeIfPresent(key, remappingFunction);
	}

	@Override
	public Category compute(Integer key, BiFunction<? super Integer, ? super Category, ? extends Category> remappingFunction) {
		return cache.compute(key, remappingFunction);
	}

	@Override
	public Category merge(Integer key, Category value, BiFunction<? super Category, ? super Category, ? extends Category> remappingFunction) {
		return cache.merge(key, value, remappingFunction);
	}

}
