package tech.orbit

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.BufferedReader
import java.io.InputStreamReader

class HelmExecutor {

    fun execute(command: List<String>): Either<Throwable, String> {
        return try {
            // Démarre le processus
            val process = ProcessBuilder(command)
                .redirectErrorStream(true)
                .start()

            // Capture la sortie
            val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }

            // Vérifie le code de sortie
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                output.right()
            } else {
                RuntimeException("Helm command failed with exit code $exitCode. Output: $output").left()
            }
        } catch (e: Throwable) {
            e.left()
        }
    }
}