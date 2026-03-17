package com.wire.crypto

import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CoreCryptoJsSmokeTest {
    private suspend fun newCoreCrypto(): CoreCrypto =
        coreCryptoNew(
            path = "cc-js-smoke-${Random.nextInt()}",
            key = DatabaseKey(ByteArray(32) { (it + 1).toByte() }),
            clientId = "smoke-client-${Random.nextInt()}".toClientId(),
            ciphersuites = listOf(CIPHERSUITE_DEFAULT),
            entropySeed = ByteArray(32) { 7 },
            nbKeyPackage = 1u,
        )

    private suspend fun newCoreCryptoClient(
        dbName: String = "cc-js-smoke-${Random.nextInt()}",
        clientName: String = "smoke-client-${Random.nextInt()}",
    ): CoreCryptoClient {
        return coreCryptoNew(
            path = dbName,
            key = DatabaseKey(ByteArray(32) { (it + 1).toByte() }),
            clientId = clientName.toClientId(),
            ciphersuites = listOf(CIPHERSUITE_DEFAULT),
            entropySeed = ByteArray(32) { 7 },
            nbKeyPackage = 1u,
        ).lift()
    }

    @Test
    fun versionAndBuildMetadataSmokeTest() = runTest {
        val coreCrypto = newCoreCrypto()
        try {
            val version = version()
            val metadata = buildMetadata()

            assertTrue(version.isNotBlank(), "version should not be blank")
            assertNotNull(metadata)
            assertTrue(metadata.gitDescribe.isNotBlank(), "gitDescribe should not be blank")
        } finally {
            coreCrypto.close()
        }
    }

    @Test
    fun loggerConfigurationSmokeTest() = runTest {
        val coreCrypto = newCoreCrypto()
        val logger = object : CoreCryptoLogger {
            override fun log(level: CoreCryptoLogLevel, message: String, context: String?) = Unit
        }

        try {
            setLogger(logger, CoreCryptoLogLevel.DEBUG)
            setLoggerOnly(logger)
            setMaxLogLevel(CoreCryptoLogLevel.INFO)
        } finally {
            coreCrypto.close()
        }
    }

    @Test
    fun initAndClientPublicKeySmokeTest() = runTest {
        val coreCrypto = newCoreCrypto()
        try {
            val publicKey = coreCrypto.clientPublicKey(CIPHERSUITE_DEFAULT, CREDENTIAL_TYPE_DEFAULT)
            assertTrue(publicKey.isNotEmpty(), "clientPublicKey should return bytes")
        } finally {
            coreCrypto.close()
        }
    }

    @Test
    fun randomBytesSmokeTest() = runTest {
        val coreCrypto = newCoreCryptoClient()
        try {
            val randomBytes = coreCrypto.randomBytes(32u)
            assertEquals(32, randomBytes.size, "randomBytes should return the requested length")
            assertFalse(randomBytes.all { it == 0.toByte() }, "randomBytes should not be all zeros")
        } finally {
            coreCrypto.close()
        }
    }

    @Test
    fun transactionPersistsDataAndPropagatesErrors() = runTest {
        val coreCrypto = newCoreCryptoClient()
        val payload = "js-transaction-payload".encodeToByteArray()

        try {
            coreCrypto.transaction { ctx ->
                assertEquals(null, ctx.getData())
                ctx.setData(payload)
            }

            coreCrypto.transaction { ctx ->
                assertContentEquals(payload, ctx.getData())
            }

            val exception = assertFailsWith<IllegalStateException> {
                coreCrypto.transaction<Unit> {
                    throw IllegalStateException("expected-js-transaction-error")
                }
            }
            assertEquals("expected-js-transaction-error", exception.message)
        } finally {
            coreCrypto.close()
        }
    }

    @Test
    fun conversationExistsFlowTest() = runTest {
        val coreCrypto = newCoreCryptoClient()
        val conversationId = "js-conversation-${Random.nextInt()}".encodeToByteArray().toConversationId()

        try {
            coreCrypto.transaction { ctx ->
                assertFalse(ctx.conversationExists(conversationId), "conversation should not exist before creation")
                ctx.createConversation(conversationId, CREDENTIAL_TYPE_DEFAULT, CONVERSATION_CONFIGURATION_DEFAULT)
                assertTrue(ctx.conversationExists(conversationId), "conversation should exist after creation")
                assertEquals(0uL, ctx.conversationEpoch(conversationId), "new conversation should start at epoch 0")
            }
        } finally {
            coreCrypto.close()
        }
    }
}
