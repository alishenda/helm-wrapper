package tech.orbit

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HelmCommandBuilderTest {

    @Test
    fun `should build basic upgrade release command`() {
        val command = helm {
            upgradeRelease(
                releaseName = "my-release",
                chart = "my-chart",
                namespace = "my-namespace"
            )
        }.build()

        assertEquals(
            "helm upgrade --install my-release my-chart --namespace my-namespace",
            command
        )
    }

    @Test
    fun `should build command with --set values`() {
        val command = helm {
            upgradeRelease(
                releaseName = "my-release",
                chart = "my-chart",
                namespace = "my-namespace"
            )
            set(mapOf("key1" to "value1", "key2" to "value2"))
        }.build()

        assertEquals(
            "helm upgrade --install my-release my-chart --namespace my-namespace --set key1=value1,key2=value2",
            command
        )
    }

    @Test
    fun `should build command with multiple --values files`() {
        val command = helm {
            upgradeRelease(
                releaseName = "my-release",
                chart = "my-chart",
                namespace = "my-namespace"
            )
            values(listOf("file1.yaml", "file2.yaml"))
        }.build()

        assertEquals(
            "helm upgrade --install my-release my-chart --namespace my-namespace --values file1.yaml --values file2.yaml",
            command
        )
    }

    @Test
    fun `should build command with --timeout`() {
        val command = helm {
            upgradeRelease(
                releaseName = "my-release",
                chart = "my-chart",
                namespace = "my-namespace"
            )
            timeout("5m0s")
        }.build()

        assertEquals(
            "helm upgrade --install my-release my-chart --namespace my-namespace --timeout 5m0s",
            command
        )
    }

    @Test
    fun `should build command with combined parameters`() {
        val command = helm {
            upgradeRelease(
                releaseName = "my-release",
                chart = "my-chart",
                namespace = "my-namespace"
            )
            set(mapOf("key1" to "value1"))
            setString(mapOf("key2" to "stringValue"))
            values(listOf("file1.yaml"))
            timeout("10m")
        }.build()

        assertEquals(
            "helm upgrade --install my-release my-chart --namespace my-namespace --set key1=value1 --set-string key2=stringValue --values file1.yaml --timeout 10m",
            command
        )
    }
}