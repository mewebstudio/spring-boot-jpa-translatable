package com.mewebstudio.springboot.jpa.translatable;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * {@link AbstractTranslationService}'s counterpart for {@link ITranslationRef}/
 * {@link JpaTranslationRefRepository}.
 *
 * @param <T>         The type of the translation entity.
 * @param <ID>        The type of the entity's identifier.
 * @param <OWNER>     The type of the translatable entity that owns the translation.
 * @param <LOCALE_ID> The type of the referenced locale entity's own identifier.
 */
public abstract class AbstractTranslationRefService<T extends ITranslationRef<ID, OWNER, LOCALE_ID>, ID, OWNER, LOCALE_ID> {
    /**
     * The repository for managing translation entities.
     *
     * @see JpaTranslationRefRepository
     */
    protected final JpaTranslationRefRepository<T, ID, OWNER, LOCALE_ID> repository;

    /**
     * Constructor for the abstract translation service.
     *
     * @param repository The repository for managing translation entities.
     */
    public AbstractTranslationRefService(JpaTranslationRefRepository<T, ID, OWNER, LOCALE_ID> repository) {
        this.repository = repository;
    }

    /**
     * Finds a translation for a specific owner.
     *
     * @param ownerId ID The ID of the translation entity.
     * @return Boolean indicating if the translation exists.
     */
    public boolean existsByOwnerId(ID ownerId) {
        return repository.existsByOwnerId(ownerId);
    }

    /**
     * Finds all translations for a specific owner by its ID.
     *
     * @param ownerId The ID of the owner entity.
     * @return List of translations for the owner.
     */
    public List<T> findByOwnerId(ID ownerId) {
        return repository.findByOwnerId(ownerId);
    }

    /**
     * Checks if a translation exists for a specific locale id.
     *
     * @param localeId The locale id to check for.
     * @return True if at least one translation with the given locale id exists, false otherwise.
     */
    public boolean existsByLocaleId(LOCALE_ID localeId) {
        return repository.existsByLocaleId(localeId);
    }

    /**
     * Checks if a translation exists for a specific owner and locale id.
     *
     * @param ownerId  The ID of the owner entity.
     * @param localeId The locale id to check for.
     * @return True if a translation exists for the owner and locale id, false otherwise.
     */
    public boolean existsByOwnerIdAndLocaleId(ID ownerId, LOCALE_ID localeId) {
        return repository.existsByOwnerIdAndLocaleId(ownerId, localeId);
    }

    /**
     * Finds all translations for a specific owner by its ID, with pagination.
     *
     * @param ownerId  The ID of the owner entity.
     * @param pageable Pagination information.
     * @return Page of translations for the owner.
     */
    public Page<T> findByOwnerId(ID ownerId, Pageable pageable) {
        return repository.findByOwnerId(ownerId, pageable);
    }

    /**
     * Finds a translation for a specific owner and locale id.
     *
     * @param ownerId  The ID of the owner entity.
     * @param localeId The locale id of the translation.
     * @return The translation, or null if not found.
     */
    public T findByOwnerIdAndLocaleId(ID ownerId, LOCALE_ID localeId) {
        return repository.findByOwnerIdAndLocaleId(ownerId, localeId);
    }

    /**
     * Saves a translation entity.
     *
     * @param translation The translation entity to save.
     * @return The saved translation entity.
     */
    @Transactional
    public T save(T translation) {
        return repository.save(translation);
    }

    /**
     * Deletes a translation for a specific owner and locale id.
     *
     * @param ownerId  The ID of the owner entity.
     * @param localeId The locale id of the translation to delete.
     * @return The number of deleted translations (usually 0 or 1).
     */
    @Transactional
    public int deleteByOwnerIdAndLocaleId(ID ownerId, LOCALE_ID localeId) {
        T existing = repository.findByOwnerIdAndLocaleId(ownerId, localeId);
        if (existing == null) {
            throw new EntityNotFoundException("Translation for owner id " +
                ownerId + " and locale id " + localeId + " not found");
        }
        return repository.deleteByOwnerIdAndLocaleId(ownerId, localeId);
    }

    /**
     * Deletes all translations for a specific locale id.
     *
     * @param localeId The locale id of the translations to delete.
     * @return The number of deleted translations.
     */
    @Transactional
    public int deleteByLocaleId(LOCALE_ID localeId) {
        if (!existsByLocaleId(localeId)) {
            throw new IllegalArgumentException("No translations found for locale id " + localeId);
        }
        return repository.deleteByLocaleId(localeId);
    }
}
