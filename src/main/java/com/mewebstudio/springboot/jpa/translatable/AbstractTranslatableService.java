package com.mewebstudio.springboot.jpa.translatable;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Abstract service class for managing translatable entities.
 *
 * @param <T>  The type of the translatable entity.
 * @param <ID> The type of the ID of the translatable entity.
 * @param <TR> The type of the translation entity.
 */
public abstract class AbstractTranslatableService<T extends ITranslatable<ID, TR>, ID, TR extends ITranslation<ID, ?>> {
    /**
     * The repository for managing translatable entities.
     *
     * @see JpaTranslatableRepository
     */
    protected final JpaTranslatableRepository<T, ID, TR> repository;

    /**
     * Constructor for the abstract translatable service.
     *
     * @param repository The repository for managing translatable entities.
     */
    public AbstractTranslatableService(JpaTranslatableRepository<T, ID, TR> repository) {
        this.repository = repository;
    }

    /**
     * Checks if a translatable entity exists by its ID and locale.
     *
     * @param id     The ID of the entity.
     * @param locale The locale to check for.
     * @return True if the entity exists with the given locale, false otherwise.
     */
    public boolean existsByIdAndLocale(ID id, String locale) {
        return repository.existsByIdAndLocale(id, locale);
    }

    /**
     * Finds a translatable entity by its ID and locale.
     *
     * @param id     The ID of the entity.
     * @param locale The locale of the translation.
     * @return The translatable entity, or null if not found.
     */
    public T findByIdAndLocale(ID id, String locale) {
        return repository.findByIdAndLocale(id, locale);
    }

    /**
     * Finds all translatable entities that have a translation with the given locale.
     *
     * @param locale The locale to filter by.
     * @return List of translatable entities.
     */
    public List<T> findAllByLocale(String locale) {
        return repository.findAllByLocale(locale);
    }

    /**
     * Finds all translatable entities that have a translation with the given locale, with pagination.
     *
     * @param locale   The locale to filter by.
     * @param pageable Pagination information.
     * @return Page of translatable entities.
     */
    public Page<T> findAllByLocale(String locale, Pageable pageable) {
        return repository.findAllByLocale(locale, pageable);
    }

    /**
     * Finds all translations for a specific translatable entity by its ID.
     *
     * @param id The ID of the entity.
     * @return List of translations for the entity.
     */
    public List<TR> findTranslationsById(ID id) {
        return repository.findTranslationsById(id);
    }

    /**
     * Finds all translations for a specific translatable entity by its ID, with pagination.
     *
     * @param id       The ID of the entity.
     * @param pageable Pagination information.
     * @return Page of translations for the entity.
     */
    public Page<TR> findTranslationsById(ID id, Pageable pageable) {
        return repository.findTranslationsById(id, pageable);
    }

    /**
     * Saves a translatable entity.
     *
     * @param entity The entity to save.
     * @return The saved entity.
     */
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Deletes all translatable entities that have a translation with the given locale.
     *
     * @param locale The locale of the translations to delete.
     * @return The number of deleted entities.
     */
    @Transactional
    public int deleteByLocale(String locale) {
        return repository.deleteByLocale(locale);
    }

    /**
     * Deletes a translatable entity if it has a translation with the given ID and locale.
     *
     * @param id     The ID of the entity.
     * @param locale The locale of the translation.
     * @return The number of deleted entities (usually 0 or 1).
     */
    @Transactional
    public int deleteByIdAndLocale(ID id, String locale) {
        return repository.deleteByIdAndLocale(id, locale);
    }
}
