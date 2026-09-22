package com.mewebstudio.springboot.jpa.translatable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * {@link JpaTranslatableRepository}'s counterpart for {@link ITranslatableRef}/
 * {@link ITranslationRef} — every query below is the same shape, just keyed by
 * {@code t.localeId} instead of {@code t.locale}.
 *
 * @param <T>         The type of the translatable entity.
 * @param <ID>        The type of the entity's identifier.
 * @param <TR>        The type of the translation entity.
 * @param <LOCALE_ID> The type of the referenced locale entity's own identifier.
 */
@NoRepositoryBean
public interface JpaTranslatableRefRepository<
    T extends ITranslatableRef<ID, TR>,
    ID,
    TR extends ITranslationRef<ID, ?, LOCALE_ID>,
    LOCALE_ID>
    extends JpaRepository<T, ID> {

    /**
     * Checks if a translatable entity exists by its ID and locale id.
     *
     * @param id       The ID of the entity.
     * @param localeId The locale id of the entity.
     * @return True if the entity exists, false otherwise.
     */
    @Query("SELECT COUNT(e) > 0 FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id AND t.localeId = :localeId")
    boolean existsByIdAndLocaleId(ID id, LOCALE_ID localeId);

    /**
     * Finds a translatable entity by its ID and locale id.
     *
     * @param id       The ID of the entity.
     * @param localeId The locale id of the entity.
     * @return The translatable entity, or null if not found.
     */
    @Query("SELECT e FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id AND t.localeId = :localeId")
    T findByIdAndLocaleId(ID id, LOCALE_ID localeId);

    /**
     * Finds all translatable entities that have a translation with the given locale id.
     *
     * @param localeId The locale id to filter by.
     * @return List of translatable entities.
     */
    @Query("SELECT DISTINCT e FROM #{#entityName} e JOIN e.translations t WHERE t.localeId = :localeId")
    List<T> findAllByLocaleId(LOCALE_ID localeId);

    /**
     * Finds all translatable entities that have a translation with the given locale id, with pagination.
     *
     * @param localeId The locale id to filter by.
     * @param pageable Pagination information.
     * @return Page of translatable entities.
     */
    @Query("SELECT DISTINCT e FROM #{#entityName} e JOIN e.translations t WHERE t.localeId = :localeId")
    Page<T> findAllByLocaleId(LOCALE_ID localeId, Pageable pageable);

    /**
     * Finds all translations for a specific translatable entity by its ID.
     *
     * @param id The ID of the entity.
     * @return List of translations for the entity.
     */
    @Query("SELECT t FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id")
    List<TR> findTranslationsById(ID id);

    /**
     * Finds all translations for a specific translatable entity by its ID, with pagination.
     *
     * @param id       The ID of the entity.
     * @param pageable Pagination information.
     * @return Page of translations for the entity.
     */
    @Query("SELECT t FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id")
    Page<TR> findTranslationsById(ID id, Pageable pageable);

    /**
     * Deletes all translatable entities that have a translation with the given locale id.
     *
     * @param localeId The locale id of the translations to delete.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE EXISTS (SELECT 1 FROM e.translations t WHERE t.localeId = :localeId)")
    int deleteByLocaleId(LOCALE_ID localeId);

    /**
     * Deletes a translatable entity if it has a translation with the given ID and locale id.
     *
     * @param id       The ID of the entity.
     * @param localeId The locale id of the translation.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id AND EXISTS (SELECT 1 FROM e.translations t WHERE t.localeId = :localeId)")
    int deleteByIdAndLocaleId(ID id, LOCALE_ID localeId);
}
