package tech.orbit

import arrow.core.Either

/**
 * Abstract executor for managing resources.
 */
abstract class Executor {

    /**
     * Installs or creates a resource.
     *
     * @param name the name of the resource.
     * @param source the source or definition of the resource (e.g., chart, image).
     * @param options key-value pairs for configuration.
     * @param files list of file paths for external configurations.
     * @return the result of the operation.
     */
    abstract fun install(
        name: String,
        source: String,
        options: Map<String, String>? = null,
        files: List<String>? = null
    ): Either<Throwable, String>

    /**
     * Updates an existing resource.
     *
     * @param name the name of the resource.
     * @param source the source or definition of the updated resource.
     * @param options key-value pairs for configuration.
     * @param files list of file paths for external configurations.
     * @return the result of the operation.
     */
    abstract fun update(
        name: String,
        source: String,
        options: Map<String, String>? = null,
        files: List<String>? = null
    ): Either<Throwable, String>

    /**
     * Deletes a resource.
     *
     * @param name the name of the resource.
     * @return the result of the operation.
     */
    abstract fun delete(name: String): Either<Throwable, String>
}