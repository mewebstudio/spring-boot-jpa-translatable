package com.mewebstudio.springboot.jpa.translatable;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Abstract service class for translation entities.
 *
 * @param <T>     The type of the translation entity.
 * @param <ID>    The type of the entity's identifier.
 * @param <OWNER> The type of the translatable entity that owns the translation.
 */
public abstract class AbstractTranslationService<T extends ITranslation<ID, OWNER>, ID, OWNER> {
    /**
     * The repository for managing translation entities.
     *
     * @see JpaTranslationRepository
     */
    protected final JpaTranslationRepository<T, ID, OWNER> repository;

    /**
     * Constructor for the abstract translation service.
     *
     * @param repository The repository for managing translation entities.
     */
    public AbstractTranslationService(JpaTranslationRepository<T, ID, OWNER> repository) {
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
     * Checks if a translation exists for a specific locale.
     *
     * @param locale The locale to check for.
     * @return True if at least one translation with the given locale exists, false otherwise.
     */
    public boolean existsByLocale(String locale) {
        return repository.existsByLocale(locale);
    }

    /**
     * Checks if a translation exists for a specific owner and locale.
     *
     * @param ownerId The ID of the owner entity.
     * @param locale  The locale to check for.
     * @return True if a translation exists for the owner and locale, false otherwise.
     */
    public boolean existsByOwnerIdAndLocale(ID ownerId, String locale) {
        return repository.existsByOwnerIdAndLocale(ownerId, locale);
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
     * Finds a translation for a specific owner and locale.
     *
     * @param ownerId The ID of the owner entity.
     * @param locale  The locale of the translation.
     * @return The translation, or null if not found.
     */
    public T findByOwnerIdAndLocale(ID ownerId, String locale) {
        return repository.findByOwnerIdAndLocale(ownerId, locale);
    }

    /**
     * Finds all translations with a specific name and locale.
     *
     * @param name   The name to search for in translations.
     * @param locale The locale of the translation.
     * @return List of translations.
     */
    public List<T> findByNameAndLocale(String name, String locale) {
        return repository.findByNameAndLocale(name, locale);
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
     * Deletes a translation for a specific owner and locale.
     *
     * @param ownerId The ID of the owner entity.
     * @param locale  The locale of the translation to delete.
     * @return The number of deleted translations (usually 0 or 1).
     */
    @Transactional
    public int deleteByOwnerIdAndLocale(ID ownerId, String locale) {
        T existing = repository.findByOwnerIdAndLocale(ownerId, locale);
        if (existing == null) {
            throw new EntityNotFoundException("Translation for owner id " +
                ownerId + " and locale " + locale + " not found");
        }
        return repository.deleteByOwnerIdAndLocale(ownerId, locale);
    }

    /**
     * Deletes all translations for a specific locale.
     *
     * @param locale The locale of the translations to delete.
     * @return The number of deleted translations.
     */
    @Transactional
    public int deleteByLocale(String locale) {
        if (!existsByLocale(locale)) {
            throw new IllegalArgumentException("No translations found for locale " + locale);
        }
        return repository.deleteByLocale(locale);
    }
}
