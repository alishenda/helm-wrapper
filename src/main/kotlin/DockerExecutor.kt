package tech.orbit

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class DockerExecutor : Executor() {

    private fun execute(command: List<String>): Either<Throwable, String> {
        return try {
            val process = ProcessBuilder(command)
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                output.right()
            } else {
                RuntimeException("Docker command failed with exit code $exitCode. Output: $output").left()
            }
        } catch (e: Throwable) {
            e.left()
        }
    }

    override fun install(name: String, source: String, options: Map<String, String>?, files: List<String>?): Either<Throwable, String> {
        val command = mutableListOf("docker", "run", "--name", name, source)
        options?.forEach { (key, value) -> command.addAll(listOf("--$key", value)) }
        return execute(command)
    }

    override fun update(name: String, source: String, options: Map<String, String>?, files: List<String>?): Either<Throwable, String> {
        return Either.Left(UnsupportedOperationException("Update is not supported for DockerExecutor"))
    }

    override fun delete(name: String): Either<Throwable, String> {
        val command = listOf("docker", "rm", "-f", name)
        return execute(command)
    }
}