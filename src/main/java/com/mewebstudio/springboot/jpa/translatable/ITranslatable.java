package com.mewebstudio.springboot.jpa.translatable;

import java.util.List;

/**
 * Interface representing a translatable entity.
 *
 * @param <ID> The type of the identifier for the translatable entity.
 * @param <T>  The type of the translation entity.
 */
public interface ITranslatable<ID, T extends ITranslation<ID, ?>> {
    /**
     * Returns the ID of the translatable entity.
     *
     * @return the ID
     */
    ID getId();

    /**
     * Returns the list of translations for the entity.
     *
     * @return the list of translation entities
     */
    List<T> getTranslations();
}
