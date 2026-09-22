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
├── ITranslatableRef.java
├── ITranslation.java
├── ITranslationRef.java
├── JpaTranslatableRepository.java
├── JpaTranslatableRefRepository.java
├── JpaTranslationRepository.java
├── JpaTranslationRefRepository.java
├── AbstractTranslatableService.java
├── AbstractTranslatableRefService.java
├── AbstractTranslationService.java
└── AbstractTranslationRefService.java
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

## 🔗 Ref variant

Every interface/repository/abstract-service above keys a translation row by `locale: String` — the
FK value *is* the human-readable locale code (`"en"`, `"tr-TR"`, ...) itself. That's fine as long
as the code never changes after creation. If your `Locale`-like entity's code/name IS editable
after creation, keying translation rows directly by that mutable string means every rename has to
cascade across every translation table referencing it.

`ITranslationRef`/`ITranslatableRef`/`JpaTranslationRefRepository`/`JpaTranslatableRefRepository`/
`AbstractTranslationRefService`/`AbstractTranslatableRefService` are a **fully independent,
additive** parallel API — identical shape, but the locale is referenced by id (`localeId:
LOCALE_ID`, typically your locale entity's own, immutable primary key) instead of stored by value
(`locale: String`) — hence "Ref". Renaming the locale entity's business code then touches nothing
downstream, since no translation row's FK depends on that value. Pick ONE family per translation
entity — the two are not meant to be mixed on the same entity. Existing code using the
`locale: String` family above is completely unaffected by this addition.

```java
public interface ITranslationRef<ID, T, LOCALE_ID> {
    ID getId();
    T getOwner();
    LOCALE_ID getLocaleId();
}
```

```java
@Entity
public class CategoryTranslation implements ITranslationRef<Long, Category, Long> {
    @Id private Long id;

    @ManyToOne
    private Category owner;

    // The real FK — points at Locale.id, never changes even if Locale.code does.
    private Long localeId;

    // Optional: a denormalized, non-FK-constrained display copy of the locale's business code,
    // refreshed explicitly whenever that code changes — read Locale.code via a join instead if
    // you don't need it queryable/sortable on the translation row itself.
    private String localeCode;

    private String name;

    // getters...
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
  <version>0.1.2</version>
</dependency>
```
#### for gradle users
Add the following dependency to your `build.gradle` file:
```groovy
implementation 'com.mewebstudio:spring-boot-jpa-translatable:0.1.2'
```

---

## 📌 Usage

You can extend these interfaces and abstract class to implement your own translatable entities and services:

### Translatable Entity Example
```java
@Entity
public class Category implements ITranslatable<Long, CategoryTranslation> {
    @Id private Long id;

    @OneToMany(mappedBy = "owner")
    private List<CategoryTranslation> translations;

    // getters...
}
```

### Translation Entity Example
```java
@Entity
public class CategoryTranslation implements ITranslation<Long, Category> {
    @Id private Long id;

    @ManyToOne
    private Category owner;

    private String locale;
    private String name;

    // getters...
}
```

### Translatable Repository Example
```java
public interface CategoryRepository extends JpaTranslatableRepository<Category, String, CategoryTranslation> {
    // Custom query methods can be added here
}
```

### Translation Repository Example
```java
public interface CategoryTranslationRepository extends JpaTranslationRepository<CategoryTranslation, String, Category> {
    // Custom query methods can be added here
}
```

### Translatable Service Example
```java
@Service
@Slf4j
public class CategoryService extends AbstractTranslatableService<Category, String, CategoryTranslation> {
    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;

    /**
     * Constructs a new CategoryService.
     *
     * @param categoryRepository            the category repository
     * @param categoryTranslationRepository the category translation repository
     */
    public CategoryService(CategoryRepository categoryRepository, CategoryTranslationRepository categoryTranslationRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;

        log.debug("CategoryService initialized with repository: {}", categoryRepository);
        Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
    }

    // Custom business logic methods can be added here
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