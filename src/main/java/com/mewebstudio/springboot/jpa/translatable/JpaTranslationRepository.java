package com.mewebstudio.springboot.jpa.translatable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Repository interface for translation entities.
 *
 * @param <T>     The type of the translation entity.
 * @param <ID>    The type of the identifier.
 * @param <OWNER> The type of the owner entity.
 */
@NoRepositoryBean
public interface JpaTranslationRepository<T extends ITranslation<ID, OWNER>, ID, OWNER> extends JpaRepository<T, ID> {
    /**
     * Checks if a translation exists for a specific locale.
     *
     * @param locale The locale to check for.
     * @return True if at least one translation with the given locale exists, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM #{#entityName} t WHERE t.locale = :locale")
    boolean existsByLocale(String locale);

    /**
     * Checks if a translation exists for a specific owner ID.
     *
     * @param ownerId The ID of the owner entity.
     * @return True if a translation exists for the owner, false otherwise.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.locale = :locale")
    boolean existsByOwnerId(ID ownerId);

    /**
     * Checks if a translation exists for a specific owner and locale.
     *
     * @param ownerId The ID of the owner entity.
     * @param locale  The locale to check for.
     * @return True if a translation exists for the owner and locale, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.locale = :locale")
    boolean existsByOwnerIdAndLocale(ID ownerId, String locale);

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
     * Finds a translation for a specific owner and locale.
     *
     * @param ownerId The ID of the owner entity.
     * @param locale  The locale of the translation.
     * @return The translation, or null if not found.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.locale = :locale")
    T findByOwnerIdAndLocale(ID ownerId, String locale);

    /**
     * Deletes all translations for a specific locale.
     *
     * @param locale The locale of the translations to delete.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} t WHERE t.locale = :locale")
    int deleteByLocale(String locale);

    /**
     * Deletes a translation for a specific owner and locale.
     *
     * @param ownerId The ID of the owner entity.
     * @param locale  The locale of the translation to delete.
     * @return The number of deleted entities.
     */
    @Modifying
    @Query("DELETE FROM #{#entityName} t WHERE t.owner.id = :ownerId AND t.locale = :locale")
    int deleteByOwnerIdAndLocale(ID ownerId, String locale);
}
