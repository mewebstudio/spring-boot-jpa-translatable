package com.mewebstudio.springboot.jpa.translatable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Repository interface for translatable entities.
 *
 * @param <T>  The type of the translatable entity.
 * @param <ID> The type of the entity's identifier.
 * @param <TR> The type of the translation entity.
 */
@NoRepositoryBean
public interface JpaTranslatableRepository<T extends ITranslatable<ID, TR>, ID, TR extends ITranslation<ID, ?>>
    extends JpaRepository<T, ID> {

    /**
     * Checks if a translatable entity exists by its ID and locale.
     *
     * @param id     The ID of the entity.
     * @param locale The locale of the entity.
     * @return True if the entity exists, false otherwise.
     */
    @Query("SELECT COUNT(e) > 0 FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id AND t.locale = :locale")
    boolean existsByIdAndLocale(ID id, String locale);

    /**
     * Finds a translatable entity by its ID and locale.
     *
     * @param id     The ID of the entity.
     * @param locale The locale of the entity.
     * @return The translatable entity, or null if not found.
     */
    @Query("SELECT e FROM #{#entityName} e JOIN e.translations t WHERE e.id = :id AND t.locale = :locale")
    T findByIdAndLocale(ID id, String locale);

    /**
     * Finds all translatable entities that have a translation with the given locale.
     *
     * @param locale The locale to filter by.
     * @return List of translatable entities.
     */
    @Query("SELECT DISTINCT e FROM #{#entityName} e JOIN e.translations t WHERE t.locale = :locale")
    List<T> findAllByLocale(String locale);

    /**
     * Finds all translatable entities that have a translation with the given locale, with pagination.
     *
     * @param locale   The locale to filter by.
     * @param pageable Pagination information.
     * @return Page of translatable entities.
     */
    @Query("SELECT DISTINCT e FROM #{#entityName} e JOIN e.translations t WHERE t.locale = :locale")
    Page<T> findAllByLocale(String locale, Pageable pageable);

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
     * Deletes all translatable entities that have a translation with the given locale.
     *
     * @param locale The locale of the translations to delete.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE EXISTS (SELECT 1 FROM e.translations t WHERE t.locale = :locale)")
    int deleteByLocale(String locale);

    /**
     * Deletes a translatable entity if it has a translation with the given ID and locale.
     *
     * @param id     The ID of the entity.
     * @param locale The locale of the translation.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id AND EXISTS (SELECT 1 FROM e.translations t WHERE t.locale = :locale)")
    int deleteByIdAndLocale(ID id, String locale);
}
