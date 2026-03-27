package com.wire.crypto

import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

private suspend fun CoreCryptoContext.proteusNewPreKeys(from: Int, count: Int): List<ByteArray> =
    from.until(from + count).map { proteusNewPrekey(it.toUShort()) }

private suspend fun CoreCryptoContext.proteusEncryptWithPreKey(
    message: ByteArray,
    preKey: ByteArray,
    sessionId: String,
): ByteArray {
    proteusSessionFromPrekey(sessionId, preKey)
    val encryptedMessage = proteusEncrypt(sessionId, message)
    proteusSessionSave(sessionId)
    return encryptedMessage
}

class ProteusJsRuntimeTest {
    private suspend fun newProteusClient(name: String): CoreCryptoClient {
        val client = CoreCrypto(
            "proteus-js-$name-${Random.nextInt()}",
            DatabaseKey(ByteArray(32) { 0 }),
        )
        client.transaction { ctx -> ctx.proteusInit() }
        return client
    }

    @Test
    fun newLastResortPreKeyReturnsExpectedId() = runTest {
        val aliceClient = newProteusClient("alice")
        try {
            val prekeyId = aliceClient.transaction { ctx -> ctx.proteusLastResortPrekeyId() }
            assertEquals(65535u, prekeyId)
        } finally {
            aliceClient.close()
        }
    }

    @Test
    fun newPreKeysReturnsRequestedCount() = runTest {
        val aliceClient = newProteusClient("alice")
        try {
            val preKeys = aliceClient.transaction { ctx -> ctx.proteusNewPreKeys(0, 10) }
            assertEquals(10, preKeys.size)
        } finally {
            aliceClient.close()
        }
    }

    @Test
    fun incomingPreKeyMessageCanBeDecrypted() = runTest {
        val aliceClient = newProteusClient("alice")
        val bobClient = newProteusClient("bob")
        val aliceSessionId = "alice-session-${Random.nextInt()}"
        val bobSessionId = "bob-session-${Random.nextInt()}"
        val message = "Hi Alice!"

        try {
            val aliceKey = aliceClient.transaction { ctx -> ctx.proteusNewPreKeys(0, 1).first() }
            val encryptedMessage = bobClient.transaction { ctx ->
                ctx.proteusEncryptWithPreKey(message.encodeToByteArray(), aliceKey, aliceSessionId)
            }
            val decryptedMessage = aliceClient.transaction { ctx ->
                ctx.proteusDecryptSafe(bobSessionId, encryptedMessage)
            }

            assertEquals(message, decryptedMessage.decodeToString())
        } finally {
            aliceClient.close()
            bobClient.close()
        }
    }

    @Test
    fun decryptingTheSameMessageTwiceReturnsDuplicateMessage() = runTest {
        val aliceClient = newProteusClient("alice")
        val bobClient = newProteusClient("bob")
        val aliceSessionId = "alice-session-${Random.nextInt()}"
        val bobSessionId = "bob-session-${Random.nextInt()}"
        val message = "Hi Alice!"

        try {
            val aliceKey = aliceClient.transaction { ctx -> ctx.proteusNewPreKeys(0, 1).first() }
            val encryptedMessage = bobClient.transaction { ctx ->
                ctx.proteusEncryptWithPreKey(message.encodeToByteArray(), aliceKey, aliceSessionId)
            }

            aliceClient.transaction { ctx -> ctx.proteusDecryptSafe(bobSessionId, encryptedMessage) }

            val exception = kotlin.test.assertFailsWith<CoreCryptoException.Proteus> {
                aliceClient.transaction { ctx -> ctx.proteusDecryptSafe(bobSessionId, encryptedMessage) }
            }
            assertIs<ProteusException.DuplicateMessage>(exception.exception)
        } finally {
            aliceClient.close()
            bobClient.close()
        }
    }

    @Test
    fun encryptBatchedIgnoresMissingSessions() = runTest {
        val aliceClient = newProteusClient("alice")
        val bobClient = newProteusClient("bob")
        val aliceSessionId = "alice-session-${Random.nextInt()}"
        val missingSessionId = "missing-session-${Random.nextInt()}"

        try {
            val aliceKey = aliceClient.transaction { ctx -> ctx.proteusNewPreKeys(0, 1).first() }
            bobClient.transaction { ctx -> ctx.proteusSessionFromPrekey(aliceSessionId, aliceKey) }

            val encryptedMessages = bobClient.transaction { ctx ->
                ctx.proteusEncryptBatched(
                    listOf(aliceSessionId, missingSessionId),
                    "Hi Alice!".encodeToByteArray(),
                )
            }

            assertEquals(1, encryptedMessages.size)
            assertTrue(encryptedMessages.containsKey(aliceSessionId))
        } finally {
            aliceClient.close()
            bobClient.close()
        }
    }

    @Test
    fun destroyedClientTransactionThrowsIllegalStateException() = runTest {
        val client = newProteusClient("alice")
        client.close()

        val exception = assertFailsWith<IllegalStateException> {
            client.transaction { ctx -> ctx.proteusFingerprint() }
        }

        assertTrue(exception.message?.contains("destroyed", ignoreCase = true) == true)
    }
}
