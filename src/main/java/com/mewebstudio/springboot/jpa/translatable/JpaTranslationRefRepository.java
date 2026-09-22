package com.mewebstudio.springboot.jpa.translatable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * {@link JpaTranslationRepository}'s counterpart for {@link ITranslationRef} — every query below
 * is the same shape, just keyed by {@code t.localeId} instead of {@code t.locale}.
 *
 * @param <T>         The type of the translation entity.
 * @param <ID>        The type of the identifier.
 * @param <OWNER>     The type of the owner entity.
 * @param <LOCALE_ID> The type of the referenced locale entity's own identifier.
 */
@NoRepositoryBean
public interface JpaTranslationRefRepository<T extends ITranslationRef<ID, OWNER, LOCALE_ID>, ID, OWNER, LOCALE_ID>
    extends JpaRepository<T, ID> {
    /**
     * Checks if a translation exists for a specific locale id.
     *
     * @param localeId The locale id to check for.
     * @return True if at least one translation with the given locale id exists, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM #{#entityName} t WHERE t.localeId = :localeId")
    boolean existsByLocaleId(LOCALE_ID localeId);

    /**
     * Checks if a translation exists for a specific owner ID.
     *
     * @param ownerId The ID of the owner entity.
     * @return True if a translation exists for the owner, false otherwise.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId")
    boolean existsByOwnerId(ID ownerId);

    /**
     * Checks if a translation exists for a specific owner and locale id.
     *
     * @param ownerId  The ID of the owner entity.
     * @param localeId The locale id to check for.
     * @return True if a translation exists for the owner and locale id, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.localeId = :localeId")
    boolean existsByOwnerIdAndLocaleId(ID ownerId, LOCALE_ID localeId);

    /**
     * Finds all translations for a specific owner by its ID.
     *
     * @param ownerId The ID of the owner entity.
     * @return List of translations for the owner.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId")
    List<T> findByOwnerId(ID ownerId);

    /**
     * Finds all translations for a specific owner by its ID, with pagination.
     *
     * @param ownerId  The ID of the owner entity.
     * @param pageable Pagination information.
     * @return Page of translations for the owner.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId")
    Page<T> findByOwnerId(ID ownerId, Pageable pageable);

    /**
     * Finds a translation for a specific owner and locale id.
     *
     * @param ownerId  The ID of the owner entity.
     * @param localeId The locale id of the translation.
     * @return The translation, or null if not found.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.localeId = :localeId")
    T findByOwnerIdAndLocaleId(ID ownerId, LOCALE_ID localeId);

    /**
     * Deletes all translations for a specific locale id.
     *
     * @param localeId The locale id of the translations to delete.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} t WHERE t.localeId = :localeId")
    int deleteByLocaleId(LOCALE_ID localeId);

    /**
     * Deletes a translation for a specific owner and locale id.
     *
     * @param ownerId  The ID of the owner entity.
     * @param localeId The locale id of the translation to delete.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.localeId = :localeId")
    int deleteByOwnerIdAndLocaleId(ID ownerId, LOCALE_ID localeId);
}
