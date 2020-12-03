package com.core.categoryservice;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class contains details about categories.
 */
@Entity
@Table(name = "category")
public class Category {

	private int id;
	private String name;

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, unique=true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return id == other.id && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}

}
