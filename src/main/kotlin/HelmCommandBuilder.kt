package tech.orbit

@DslMarker
annotation class HelmDsl

@HelmDsl
class HelmCommandBuilder {
    private val commandParts = mutableListOf("helm")

    fun command(subcommand: String) {
        commandParts.add(subcommand)
    }

    fun flag(flag: String, value: String? = null) {
        commandParts.add(flag)
        if (value != null) {
            commandParts.add(value)
        }
    }

    fun set(values: Map<String, String>) {
        addKeyValueFlag("--set", values)
    }

    fun setFile(values: Map<String, String>) {
        addKeyValueFlag("--set-file", values)
    }

    fun setJson(values: Map<String, String>) {
        addKeyValueFlag("--set-json", values)
    }

    fun setLiteral(values: Map<String, String>) {
        addKeyValueFlag("--set-literal", values)
    }

    fun setString(values: Map<String, String>) {
        addKeyValueFlag("--set-string", values)
    }

    fun values(filePaths: List<String>) {
        filePaths.forEach { flag("--values", it) }
    }

    fun timeout(duration: String) {
        flag("--timeout", duration)
    }

    private fun addKeyValueFlag(flag: String, values: Map<String, String>) {
        if (values.isNotEmpty()) {
            val valueString = values.entries.joinToString(",") { "${it.key}=${it.value}" }
            flag(flag, valueString)
        }
    }

    override fun toString(): String = commandParts.joinToString(" ")

    fun build(): String = toString()
}

fun helm(init: HelmCommandBuilder.() -> Unit): HelmCommandBuilder {
    return HelmCommandBuilder().apply(init)
}

// Specific DSL for `upgrade --install`
fun HelmCommandBuilder.upgradeRelease(
    releaseName: String,
    chart: String,
    namespace: String
) {
    command("upgrade")
    flag("--install")
    flag(releaseName)
    flag(chart)
    flag("--namespace", namespace)
}
