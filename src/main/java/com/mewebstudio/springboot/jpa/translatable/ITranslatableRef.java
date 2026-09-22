package com.mewebstudio.springboot.jpa.translatable;

import java.util.List;

/**
 * Variant of {@link ITranslatable} for translatable entities whose translations implement
 * {@link ITranslationRef} instead of {@link ITranslation}. See {@link ITranslationRef} for why
 * you'd pick this pairing over the string-locale one.
 *
 * @param <ID> The type of the identifier for the translatable entity.
 * @param <T>  The type of the translation entity.
 */
public interface ITranslatableRef<ID, T extends ITranslationRef<ID, ?, ?>> {
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
