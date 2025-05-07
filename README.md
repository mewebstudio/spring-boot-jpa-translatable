# Translatable for Spring Boot JPA

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Maven badge](https://maven-badges.herokuapp.com/maven-central/com.mewebstudio/spring-boot-jpa-translatable/badge.svg?style=flat)](https://central.sonatype.com/artifact/com.mewebstudio/spring-boot-jpa-translatable)
[![javadoc](https://javadoc.io/badge2/com.mewebstudio/spring-boot-jpa-translatable/javadoc.svg)](https://javadoc.io/doc/com.mewebstudio/spring-boot-jpa-translatable)

This module provides an abstract and reusable foundation for supporting **translatable (multi-language) entities** using Spring Data JPA.  
It defines core interfaces, abstract repositories, and a base service class to handle translations with locale-specific logic.

---

## 📦 Package Structure

```
com.mewebstudio.springboot.jpa.translatable
├── ITranslatable.java
├── ITranslation.java
├── JpaTranslatableRepository.java
├── JpaTranslationRepository.java
└── AbstractTranslatableService.java
```

---

## 🧩 Interfaces

### `ITranslatable<ID, T extends ITranslation<ID, ?>>`

Represents an entity that supports translations.

```java
public interface ITranslatable<ID, T extends ITranslation<ID, ?>> {
    ID getId();
    List<T> getTranslations();
}
```

---

### `ITranslation<ID, T>`

Represents a translation of an entity in a specific locale.

```java
public interface ITranslation<ID, T> {
    ID getId();
    T getOwner();
    String getLocale();
}
```

---

## 🗃 Repositories

### `JpaTranslatableRepository<T, ID, TR>`

Generic JPA repository for translatable entities.

```java
@NoRepositoryBean
public interface JpaTranslatableRepository<T extends ITranslatable<ID, TR>, ID, TR extends ITranslation<ID, ?>>
        extends JpaRepository<T, ID> {

    @Query("SELECT COUNT(e) > 0 FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id AND t.locale = :locale")
    boolean existsByIdAndLocale(ID id, String locale);

    @Query("SELECT e FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id AND t.locale = :locale")
    T findByIdAndLocale(ID id, String locale);

    @Query("SELECT DISTINCT e FROM #{#entityName} e JOIN e.translations t WHERE t.locale = :locale")
    List<T> findAllByLocale(String locale);

    Page<T> findAllByLocale(String locale, Pageable pageable);

    @Query("SELECT t FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id")
    List<TR> findTranslationsById(ID id);

    Page<TR> findTranslationsById(ID id, Pageable pageable);

    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE EXISTS (SELECT 1 FROM e.translations t WHERE t.locale = :locale)")
    int deleteByLocale(String locale);

    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id AND EXISTS (SELECT 1 FROM e.translations t WHERE t.locale = :locale)")
    int deleteByIdAndLocale(ID id, String locale);
}
```

---

### `JpaTranslationRepository<T, ID, OWNER>`

Generic JPA repository for translation entities.

```java
@NoRepositoryBean
public interface JpaTranslationRepository<T extends ITranslation<ID, OWNER>, ID, OWNER>
        extends JpaRepository<T, ID> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM #{#entityName} t WHERE t.locale = :locale")
    boolean existsByLocale(String locale);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.locale = :locale")
    boolean existsByOwnerIdAndLocale(ID ownerId, String locale);

    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId")
    List<T> findByOwnerId(ID ownerId);

    Page<T> findByOwnerId(ID ownerId, Pageable pageable);

    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.locale = :locale")
    T findByOwnerIdAndLocale(ID ownerId, String locale);

    @Query("SELECT t FROM #{#entityName} t WHERE t.name = :name AND t.locale = :locale")
    List<T> findByNameAndLocale(String name, String locale);

    @Modifying
    @Query("DELETE FROM #{#entityName} t WHERE t.locale = :locale")
    int deleteByLocale(String locale);

    @Modifying
    @Query("DELETE FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.locale = :locale")
    int deleteByOwnerIdAndLocale(ID ownerId, String locale);
}
```

---

## 🧠 Abstract Service

### `AbstractTranslatableService<T, ID, TR>`

Provides a base service class for business logic operations.

```java
public abstract class AbstractTranslatableService<T extends ITranslatable<ID, TR>, ID, TR extends ITranslation<ID, ?>> {
    protected final JpaTranslatableRepository<T, ID, TR> repository;

    public AbstractTranslatableService(JpaTranslatableRepository<T, ID, TR> repository) {
        this.repository = repository;
    }

    public boolean existsByIdAndLocale(ID id, String locale) {
        return repository.existsByIdAndLocale(id, locale);
    }

    public T findByIdAndLocale(ID id, String locale) {
        return repository.findByIdAndLocale(id, locale);
    }

    public List<T> findAllByLocale(String locale) {
        return repository.findAllByLocale(locale);
    }

    public Page<T> findAllByLocale(String locale, Pageable pageable) {
        return repository.findAllByLocale(locale, pageable);
    }

    public List<TR> findTranslationsById(ID id) {
        return repository.findTranslationsById(id);
    }

    public Page<TR> findTranslationsById(ID id, Pageable pageable) {
        return repository.findTranslationsById(id, pageable);
    }

    @Transactional
    public int deleteByLocale(String locale) {
        return repository.deleteByLocale(locale);
    }

    @Transactional
    public int deleteByIdAndLocale(ID id, String locale) {
        return repository.deleteByIdAndLocale(id, locale);
    }
}
```

---

## 📥 Installation

#### for maven users
Add the following dependency to your `pom.xml` file:
```xml
<dependency>
  <groupId>com.mewebstudio</groupId>
  <artifactId>spring-boot-jpa-translatable</artifactId>
  <version>0.1.0</version>
</dependency>
```
#### for gradle users
Add the following dependency to your `build.gradle` file:
```groovy
implementation 'com.mewebstudio:spring-boot-jpa-translatable:0.1.0'
```

---

## 📌 Usage

You can extend these interfaces and abstract class to implement your own translatable entities and services:

```java
@Entity
public class Product implements ITranslatable<Long, ProductTranslation> {
    @Id private Long id;

    @OneToMany(mappedBy = "owner")
    private List<ProductTranslation> translations;

    // getters...
}
```

```java
@Entity
public class ProductTranslation implements ITranslation<Long, Product> {
    @Id private Long id;

    @ManyToOne
    private Product owner;

    private String locale;
    private String name;

    // getters...
}
```

---

## 🛠 Requirements

- Java 17+
- Spring Boot 3.x
- Spring Data JPA

---

## 🔁 Other Implementations

[Spring Boot JPA Translatable (Kotlin Maven Package)](https://github.com/mewebstudio/spring-boot-jpa-translatable-kotlin)

## 💡 Example Implementations

[Spring Boot JPA Translatable - Java Implementation](https://github.com/mewebstudio/spring-boot-jpa-translatable-java-impl)

[Spring Boot JPA Translatable - Kotlin Implementation](https://github.com/mewebstudio/spring-boot-jpa-translatable-kotlin-impl)

## 📃 License

MIT © [mewebstudio](https://github.com/mewebstudio)