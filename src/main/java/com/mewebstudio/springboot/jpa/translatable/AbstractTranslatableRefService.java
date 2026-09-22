package com.mewebstudio.springboot.jpa.translatable;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * {@link AbstractTranslatableService}'s counterpart for {@link ITranslatableRef}/
 * {@link JpaTranslatableRefRepository}.
 *
 * @param <T>         The type of the translatable entity.
 * @param <ID>        The type of the ID of the translatable entity.
 * @param <TR>        The type of the translation entity.
 * @param <LOCALE_ID> The type of the referenced locale entity's own identifier.
 */
public abstract class AbstractTranslatableRefService<
    T extends ITranslatableRef<ID, TR>,
    ID,
    TR extends ITranslationRef<ID, ?, LOCALE_ID>,
    LOCALE_ID> {
    /**
     * The repository for managing translatable entities.
     *
     * @see JpaTranslatableRefRepository
     */
    protected final JpaTranslatableRefRepository<T, ID, TR, LOCALE_ID> repository;

    /**
     * Constructor for the abstract translatable service.
     *
     * @param repository The repository for managing translatable entities.
     */
    public AbstractTranslatableRefService(JpaTranslatableRefRepository<T, ID, TR, LOCALE_ID> repository) {
        this.repository = repository;
    }

    /**
     * Checks if a translatable entity exists by its ID and locale id.
     *
     * @param id       The ID of the entity.
     * @param localeId The locale id to check for.
     * @return True if the entity exists with the given locale id, false otherwise.
     */
    public boolean existsByIdAndLocaleId(ID id, LOCALE_ID localeId) {
        return repository.existsByIdAndLocaleId(id, localeId);
    }

    /**
     * Finds a translatable entity by its ID and locale id.
     *
     * @param id       The ID of the entity.
     * @param localeId The locale id of the translation.
     * @return The translatable entity, or null if not found.
     */
    public T findByIdAndLocaleId(ID id, LOCALE_ID localeId) {
        return repository.findByIdAndLocaleId(id, localeId);
    }

    /**
     * Finds all translatable entities that have a translation with the given locale id.
     *
     * @param localeId The locale id to filter by.
     * @return List of translatable entities.
     */
    public List<T> findAllByLocaleId(LOCALE_ID localeId) {
        return repository.findAllByLocaleId(localeId);
    }

    /**
     * Finds all translatable entities that have a translation with the given locale id, with pagination.
     *
     * @param localeId The locale id to filter by.
     * @param pageable Pagination information.
     * @return Page of translatable entities.
     */
    public Page<T> findAllByLocaleId(LOCALE_ID localeId, Pageable pageable) {
        return repository.findAllByLocaleId(localeId, pageable);
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
     * Deletes all translatable entities that have a translation with the given locale id.
     *
     * @param localeId The locale id of the translations to delete.
     * @return The number of deleted entities.
     */
    @Transactional
    public int deleteByLocaleId(LOCALE_ID localeId) {
        return repository.deleteByLocaleId(localeId);
    }

    /**
     * Deletes a translatable entity if it has a translation with the given ID and locale id.
     *
     * @param id       The ID of the entity.
     * @param localeId The locale id of the translation.
     * @return The number of deleted entities (usually 0 or 1).
     */
    @Transactional
    public int deleteByIdAndLocaleId(ID id, LOCALE_ID localeId) {
        return repository.deleteByIdAndLocaleId(id, localeId);
    }
}
