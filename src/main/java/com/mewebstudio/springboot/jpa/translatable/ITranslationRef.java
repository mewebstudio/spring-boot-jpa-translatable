package com.mewebstudio.springboot.jpa.translatable;

/**
 * Variant of {@link ITranslation} for translation rows whose locale reference is the owning
 * locale entity's own (typically immutable) primary key, rather than a human-readable locale
 * string (e.g. {@code "en"}, {@code "tr-TR"}). Use this when the locale a translation belongs to
 * is modeled as a real FK to a {@code Locale}-like entity whose business code (name, ISO code,
 * ...) can change after creation — keying by that entity's id means such a rename never requires
 * touching any translation row, unlike {@link ITranslation#getLocale()}, which IS the FK value
 * itself.
 * <p>
 * Fully independent of {@link ITranslation} — implement this one instead, not both, for a given
 * translation entity. Existing code using {@link ITranslation} is entirely unaffected by this
 * addition.
 *
 * @param <ID>        The type of the identifier for the translation entity.
 * @param <T>         The type of the owner entity.
 * @param <LOCALE_ID> The type of the referenced locale entity's own identifier.
 */
public interface ITranslationRef<ID, T, LOCALE_ID> {
    /**
     * Returns the ID of the translation entity.
     *
     * @return the ID
     */
    ID getId();

    /**
     * Returns the owner of the translation entity.
     *
     * @return the owner entity
     */
    T getOwner();

    /**
     * Returns the id of the locale entity this translation belongs to.
     *
     * @return the locale entity's id
     */
    LOCALE_ID getLocaleId();
}
