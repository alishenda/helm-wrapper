package tech.orbit

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.BufferedReader
import java.io.InputStreamReader

class HelmExecutor : Executor() {

    private fun execute(command: String): Either<Throwable, String> {
        return try {
            val process = ProcessBuilder(command.split(" "))
                .redirectErrorStream(true)
                .start()

            val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }

            val exitCode = process.waitFor()
            if (exitCode == 0) {
                output.right()
            } else {
                RuntimeException("Command failed with exit code $exitCode. Output: $output").left()
            }
        } catch (e: Throwable) {
            e.left()
        }
    }

    override fun install(
        name: String,
        source: String,
        options: Map<String, String>?,
        files: List<String>?
    ): Either<Throwable, String> {
        val command = helm {
            command("install")
            flag(name)
            flag(source)
            options?.let { set(it) }
            files?.let { values(it) }
        }.build()

        return execute(command)
    }

    override fun update(
        name: String,
        source: String,
        options: Map<String, String>?,
        files: List<String>?
    ): Either<Throwable, String> {
        val command = helm {
            command("upgrade")
            flag("--install")
            flag(name)
            flag(source)
            options?.let { set(it) }
            files?.let { values(it) }
        }.build()

        return execute(command)
    }

    override fun delete(name: String): Either<Throwable, String> {
        val command = helm {
            command("uninstall")
            flag(name)
        }.build()

        return execute(command)
    }
}