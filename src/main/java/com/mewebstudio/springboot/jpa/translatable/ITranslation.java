package com.mewebstudio.springboot.jpa.translatable;

/**
 * Interface representing a translation entity.
 *
 * @param <ID> The type of the identifier for the translation entity.
 * @param <T>  The type of the owner entity.
 */
public interface ITranslation<ID, T> {
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
     * Returns the locale of the translation entity.
     *
     * @return the locale string (e.g., "en", "tr")
     */
    String getLocale();
}
