@file:Suppress(
    "ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT",
    "OPT_IN_USAGE",
    "UNCHECKED_CAST_TO_EXTERNAL_INTERFACE",
    "UnsafeCastFromDynamic",
    "unused",
    "ClassName",
    "FunctionName"
)
@file:OptIn(kotlinx.coroutines.DelicateCoroutinesApi::class)

package com.wire.crypto

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import kotlinx.datetime.Instant
import org.khronos.webgl.Uint8Array
import kotlin.js.Promise

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external object JsCoreCryptoModule {
    fun initWasmModule(location: String = definedExternally): Promise<Unit>
    fun buildMetadata(): JsBuildMetadata
    fun ciphersuiteDefault(): Short
    fun ciphersuiteFromU16(discriminant: Short): Short
    fun openDatabase(name: String, key: JsDatabaseKey): Promise<JsDatabase>
    fun migrateDatabaseKeyTypeToBytes(path: String, old_key: String, new_key: JsDatabaseKey): Promise<Unit>
    fun updateDatabaseKey(name: String, old_key: JsDatabaseKey, new_key: JsDatabaseKey): Promise<Unit>
    fun setLogger(logger: JsCoreCryptoLogger)
    fun setMaxLogLevel(level: Int)
    fun version(): String
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsClientId(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsConversationId(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsDatabaseKey(buf: Uint8Array) {
    fun free()
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsDatabase {
    fun free()
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsExternalSenderKey(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsGroupInfo(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsKeyPackage(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsSecretKey(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsWelcome(bytes: Uint8Array) {
    fun free()
    fun copyBytes(): Uint8Array
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsMlsTransportData(buf: Uint8Array) {
    val data: Uint8Array
    fun free()
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsCoreCrypto {
    fun inner(): Any?
    fun close(): Promise<Unit>
    fun provideTransport(transportProvider: JsMlsTransport, ctx: Any? = definedExternally): Promise<Unit>
    fun conversationExists(conversationId: JsConversationId): Promise<Boolean>
    fun conversationEpoch(conversationId: JsConversationId): Promise<dynamic>
    fun conversationCiphersuite(conversationId: JsConversationId): Promise<Short>
    fun e2eiConversationState(conversationId: JsConversationId): Promise<Short>
    fun e2eiIsEnabled(ciphersuite: Int): Promise<Boolean>
    fun e2eiIsPKIEnvSetup(): Promise<Boolean>
    fun exportSecretKey(conversationId: JsConversationId, keyLength: Int): Promise<JsSecretKey>
    fun getClientIds(conversationId: JsConversationId): Promise<Array<JsClientId>>
    fun getDeviceIdentities(conversationId: JsConversationId, deviceIds: Array<JsClientId>): Promise<Array<JsWireIdentity>>
    fun getExternalSender(conversationId: JsConversationId): Promise<JsExternalSenderKey>
    fun getUserIdentities(conversationId: JsConversationId, userIds: Array<String>): Promise<dynamic>
    fun isHistorySharingEnabled(conversationId: JsConversationId): Promise<Boolean>
    fun clientPublicKey(ciphersuite: Int, credentialType: Int): Promise<Uint8Array>
    fun proteusFingerprint(): Promise<String>
    fun proteusFingerprintLocal(sessionId: String): Promise<String>
    fun proteusFingerprintRemote(sessionId: String): Promise<String>
    fun proteusSessionExists(sessionId: String): Promise<Boolean>
    fun randomBytes(length: Int): Promise<Uint8Array>
    fun reseedRng(seed: Uint8Array): Promise<Unit>
    fun registerEpochObserver(observer: JsEpochObserver): Promise<Unit>
    fun registerHistoryObserver(observer: JsHistoryObserver): Promise<Unit>
    fun transaction(callback: (JsCoreCryptoContext) -> Promise<Any?>): Promise<Any?>

    companion object {
        fun init(params: JsCoreCryptoParams): Promise<JsCoreCrypto>
        fun deferredInit(params: JsCoreCryptoDeferredParams): Promise<JsCoreCrypto>
        fun historyClient(historySecret: JsHistorySecret): Promise<JsCoreCrypto>
        fun setLogger(logger: JsCoreCryptoLogger)
        fun setMaxLogLevel(level: Int)
        fun proteusLastResortPrekeyId(): Int
        fun proteusFingerprintPrekeybundle(prekey: Uint8Array): String
    }
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsCoreCryptoContext {
    fun setData(data: Uint8Array): Promise<Unit>
    fun getData(): Promise<Uint8Array?>
    fun randomBytes(length: Int): Promise<Uint8Array>
    fun proteusInit(): Promise<Unit>
    fun proteusSessionFromPrekey(sessionId: String, prekey: Uint8Array): Promise<Unit>
    fun proteusSessionFromMessage(sessionId: String, envelope: Uint8Array): Promise<Uint8Array>
    fun proteusSessionSave(sessionId: String): Promise<Unit>
    fun proteusSessionDelete(sessionId: String): Promise<Unit>
    fun proteusSessionExists(sessionId: String): Promise<Boolean>
    fun proteusDecrypt(sessionId: String, ciphertext: Uint8Array): Promise<Uint8Array>
    fun proteusDecryptSafe(sessionId: String, ciphertext: Uint8Array): Promise<Uint8Array>
    fun proteusEncrypt(sessionId: String, plaintext: Uint8Array): Promise<Uint8Array>
    fun proteusEncryptBatched(sessions: Array<String>, plaintext: Uint8Array): Promise<dynamic>
    fun proteusNewPrekey(prekeyId: Int): Promise<Uint8Array>
    fun proteusNewPrekeyAuto(): Promise<JsProteusAutoPrekeyBundle>
    fun proteusLastResortPrekey(): Promise<Uint8Array>
    fun proteusFingerprint(): Promise<String>
    fun proteusFingerprintLocal(sessionId: String): Promise<String>
    fun proteusFingerprintRemote(sessionId: String): Promise<String>
    fun proteusReloadSessions(): Promise<Unit>
    fun mlsInit(clientId: JsClientId, ciphersuites: Array<Int>, nbKeyPackage: Int? = definedExternally): Promise<Unit>
    fun clientPublicKey(ciphersuite: Int, credentialType: Int): Promise<Uint8Array>
    fun conversationEpoch(conversationId: JsConversationId): Promise<dynamic>
    fun conversationCiphersuite(conversationId: JsConversationId): Promise<Short>
    fun conversationExists(conversationId: JsConversationId): Promise<Boolean>
    fun getClientIds(conversationId: JsConversationId): Promise<Array<JsClientId>>
    fun exportSecretKey(conversationId: JsConversationId, keyLength: Int): Promise<JsSecretKey>
    fun getExternalSender(conversationId: JsConversationId): Promise<JsExternalSenderKey>
    fun clientKeypackages(ciphersuite: Int, credentialType: Int, amountRequested: Int): Promise<Array<Uint8Array>>
    fun clientValidKeypackagesCount(ciphersuite: Int, credentialType: Int): Promise<dynamic>
    fun createConversation(conversationId: JsConversationId, creatorCredentialType: Int, configuration: JsConversationConfiguration? = definedExternally): Promise<Unit>
    fun processWelcomeMessage(welcomeMessage: JsWelcome, configuration: JsCustomConfiguration? = definedExternally): Promise<JsWelcomeBundle>
    fun addClientsToConversation(conversationId: JsConversationId, keyPackages: Array<Uint8Array>): Promise<Array<String>?>
    fun removeClientsFromConversation(conversationId: JsConversationId, clientIds: Array<JsClientId>): Promise<Unit>
    fun markConversationAsChildOf(childId: JsConversationId, parentId: JsConversationId): Promise<Unit>
    fun updateKeyingMaterial(conversationId: JsConversationId): Promise<Unit>
    fun commitPendingProposals(conversationId: JsConversationId): Promise<Unit>
    fun wipeConversation(conversationId: JsConversationId): Promise<Unit>
    fun decryptMessage(conversationId: JsConversationId, payload: Uint8Array): Promise<JsDecryptedMessage>
    fun encryptMessage(conversationId: JsConversationId, message: Uint8Array): Promise<Uint8Array>
    fun joinByExternalCommit(groupInfo: JsGroupInfo, credentialType: Int, configuration: JsCustomConfiguration? = definedExternally): Promise<JsWelcomeBundle>
    fun enableHistorySharing(conversationId: JsConversationId): Promise<Unit>
    fun disableHistorySharing(conversationId: JsConversationId): Promise<Unit>
    fun e2eiNewEnrollment(clientId: String, displayName: String, handle: String, expirySec: Int, ciphersuite: Int, team: String? = definedExternally): Promise<JsE2eiEnrollment>
    fun e2eiNewActivationEnrollment(displayName: String, handle: String, expirySec: Int, ciphersuite: Int, team: String? = definedExternally): Promise<JsE2eiEnrollment>
    fun e2eiNewRotateEnrollment(expirySec: Int, ciphersuite: Int, displayName: String? = definedExternally, handle: String? = definedExternally, team: String? = definedExternally): Promise<JsE2eiEnrollment>
    fun e2eiMlsInitOnly(enrollment: JsE2eiEnrollment, certificateChain: String, nbKeyPackage: Int? = definedExternally): Promise<Array<String>?>
    fun e2eiIsPKIEnvSetup(): Promise<Boolean>
    fun e2eiRegisterAcmeCA(trustAnchorPEM: String): Promise<Unit>
    fun e2eiRegisterIntermediateCA(certPEM: String): Promise<Array<String>?>
    fun e2eiRegisterCRL(crlDP: String, crlDER: Uint8Array): Promise<JsCrlRegistration>
    fun e2eiRotate(conversationId: JsConversationId): Promise<Unit>
    fun saveX509Credential(enrollment: JsE2eiEnrollment, certificateChain: String): Promise<Array<String>?>
    fun deleteStaleKeyPackages(ciphersuite: Int): Promise<Unit>
    fun e2eiEnrollmentStash(enrollment: JsE2eiEnrollment): Promise<Uint8Array>
    fun e2eiEnrollmentStashPop(handle: Uint8Array): Promise<JsE2eiEnrollment>
    fun e2eiConversationState(conversationId: JsConversationId): Promise<Short>
    fun e2eiIsEnabled(ciphersuite: Int): Promise<Boolean>
    fun getDeviceIdentities(conversationId: JsConversationId, deviceIds: Array<JsClientId>): Promise<Array<JsWireIdentity>>
    fun getUserIdentities(conversationId: JsConversationId, userIds: Array<String>): Promise<dynamic>

    companion object {
        fun proteusLastResortPrekeyId(): Int
        fun proteusFingerprintPrekeybundle(prekey: Uint8Array): String
    }
}

@JsModule("@wireapp/core-crypto")
@JsNonModule
internal external class JsE2eiEnrollment {
    fun free()
    fun directoryResponse(directory: Uint8Array): Promise<JsAcmeDirectory>
    fun newAccountRequest(previousNonce: String): Promise<Uint8Array>
    fun newAccountResponse(account: Uint8Array): Promise<Unit>
    fun newOrderRequest(previousNonce: String): Promise<Uint8Array>
    fun newOrderResponse(order: Uint8Array): Promise<JsNewAcmeOrder>
    fun newAuthzRequest(url: String, previousNonce: String): Promise<Uint8Array>
    fun newAuthzResponse(authz: Uint8Array): Promise<JsNewAcmeAuthz>
    fun createDpopToken(expirySecs: Int, backendNonce: String): Promise<Uint8Array>
    fun newDpopChallengeRequest(accessToken: String, previousNonce: String): Promise<Uint8Array>
    fun newDpopChallengeResponse(challenge: Uint8Array): Promise<Unit>
    fun checkOrderRequest(orderUrl: String, previousNonce: String): Promise<Uint8Array>
    fun checkOrderResponse(order: Uint8Array): Promise<String>
    fun finalizeRequest(previousNonce: String): Promise<Uint8Array>
    fun finalizeResponse(finalize: Uint8Array): Promise<String>
    fun certificateRequest(previousNonce: String): Promise<Uint8Array>
    fun newOidcChallengeRequest(idToken: String, previousNonce: String): Promise<Uint8Array>
    fun newOidcChallengeResponse(challenge: Uint8Array): Promise<Unit>
}

internal external interface JsCoreCryptoDeferredParams {
    var databaseName: String
    var key: JsDatabaseKey
    var entropySeed: Uint8Array?
}

internal external interface JsCoreCryptoParams : JsCoreCryptoDeferredParams {
    var clientId: JsClientId
    var ciphersuites: Array<Int>
    var nbKeyPackage: Int?
}

internal external interface JsCoreCryptoLogger {
    fun log(level: Int, message: String, context: String?)
}

internal external interface JsEpochObserver {
    fun epochChanged(conversationId: JsConversationId, epoch: dynamic): Promise<Unit>
}

internal external interface JsHistoryObserver {
    fun historyClientCreated(conversationId: JsConversationId, secret: JsHistorySecret): Promise<Unit>
}

internal external interface JsMlsTransport {
    fun sendCommitBundle(commitBundle: JsCommitBundle): Promise<dynamic>
    fun sendMessage(message: Uint8Array): Promise<dynamic>
    fun prepareForTransport(secret: JsHistorySecret): Promise<JsMlsTransportData>
}

internal external interface JsConversationConfiguration {
    var ciphersuite: Int?
    var externalSenders: Array<JsExternalSenderKey>?
    var keyRotationSpan: Int?
    var wirePolicy: Int?
}

internal external interface JsCustomConfiguration {
    var keyRotationSpan: Int?
    var wirePolicy: Int?
}

internal external interface JsBuildMetadata {
    val timestamp: String
    val cargoDebug: String
    val cargoFeatures: String
    val optLevel: String
    val targetTriple: String
    val gitBranch: String
    val gitDescribe: String
    val gitSha: String
    val gitDirty: String
}

internal external interface JsBufferedDecryptedMessage {
    val message: Uint8Array?
    val isActive: Boolean
    val commitDelay: dynamic
    val senderClientId: JsClientId?
    val hasEpochChanged: Boolean
    val identity: JsWireIdentity?
    val crlNewDistributionPoints: Array<String>?
}

internal external interface JsDecryptedMessage : JsBufferedDecryptedMessage {
    val bufferedMessages: Array<JsBufferedDecryptedMessage>?
}

internal external interface JsHistorySecret {
    var clientId: JsClientId
    var data: Uint8Array
}

internal external interface JsCommitBundle {
    val welcome: JsWelcome?
    val commit: Uint8Array
    val groupInfo: JsGroupInfoBundle
    val encryptedMessage: Uint8Array?
}

internal external interface JsGroupInfoBundle {
    val encryptionType: Int
    val ratchetTreeType: Int
    val payload: JsGroupInfo
}

internal external interface JsWelcomeBundle {
    val id: JsConversationId
    val crlNewDistributionPoints: Array<String>?
}

internal external interface JsWireIdentity {
    val clientId: String
    val status: Int
    val thumbprint: String
    val credentialType: Int
    val x509Identity: JsX509Identity?
}

internal external interface JsX509Identity {
    val handle: String
    val displayName: String
    val domain: String
    val certificate: String
    val serialNumber: String
    val notBefore: dynamic
    val notAfter: dynamic
}

internal external interface JsAcmeChallenge {
    val delegate: Uint8Array
    val url: String
    val target: String
}

internal external interface JsAcmeDirectory {
    val newNonce: String
    val newAccount: String
    val newOrder: String
    val revokeCert: String
}

internal external interface JsNewAcmeAuthz {
    val identifier: String
    val keyauth: String?
    val challenge: JsAcmeChallenge
}

internal external interface JsNewAcmeOrder {
    val delegate: Uint8Array
    val authorizations: Array<String>
}

internal external interface JsProteusAutoPrekeyBundle {
    val id: Int
    val pkb: Uint8Array
}

internal external interface JsCrlRegistration {
    val dirty: Boolean
    val expiration: dynamic
}

internal external interface JsErrorContext {
    val type: String?
    val msg: String?
    val reason: String?
    val conversationId: Array<Int>?
    val errorCode: Int?
    val error: String?
    val e2eiError: String?
}

internal external interface JsCoreCryptoError {
    val message: String?
    val type: String?
    val context: JsErrorContext?
}

private var wasmInitPromise: Promise<Unit>? = null

private suspend fun ensureWasmInitialized() {
    val promise = wasmInitPromise ?: run {
        val initPromise = GlobalScope.promise {
            val candidates = listOf<String?>(
                null,
                "autogenerated/",
                "/base/node_modules/@wireapp/core-crypto/src/autogenerated/",
                "/base/build/js/node_modules/@wireapp/core-crypto/src/autogenerated/",
            )
            var lastError: Throwable? = null
            for (candidate in candidates) {
                try {
                    if (candidate == null) {
                        JsCoreCryptoModule.initWasmModule().await()
                    } else {
                        JsCoreCryptoModule.initWasmModule(candidate).await()
                    }
                    return@promise
                } catch (throwable: Throwable) {
                    lastError = throwable
                }
            }
            throw lastError ?: CoreCryptoException.Other("Unable to initialize @wireapp/core-crypto WASM module")
        }
        wasmInitPromise = initPromise
        initPromise
    }
    promise.await()
}

private suspend fun <T> webCall(block: () -> Promise<T>): T {
    ensureWasmInitialized()
    return try {
        block().await()
    } catch (throwable: Throwable) {
        throw mapCoreCryptoError(throwable)
    }
}

private fun mapCoreCryptoError(throwable: Throwable): Throwable {
    val error = throwable.asDynamic().unsafeCast<JsCoreCryptoError?>() ?: return throwable
    val type = error.type ?: return throwable
    val context = error.context
    return when (type) {
        "Mls" -> CoreCryptoException.Mls(mapMlsException(context, error.message))
        "Proteus" -> CoreCryptoException.Proteus(mapProteusException(context))
        "E2ei" -> CoreCryptoException.E2ei(context?.e2eiError ?: error.message ?: "Unknown E2EI error")
        "TransactionFailed" -> CoreCryptoException.TransactionFailed(context?.error ?: error.message ?: "Transaction failed")
        "Other" -> CoreCryptoException.Other(context?.msg ?: error.message ?: "Unknown CoreCrypto error")
        else -> throwable
    }
}

private fun mapMlsException(context: JsErrorContext?, fallbackMessage: String?): MlsException = when (context?.type) {
    "ConversationAlreadyExists" -> MlsException.ConversationAlreadyExists((context.conversationId ?: emptyArray()).map { it.toByte() }.toByteArray())
    "DuplicateMessage" -> MlsException.DuplicateMessage()
    "BufferedFutureMessage" -> MlsException.BufferedFutureMessage()
    "WrongEpoch" -> MlsException.WrongEpoch()
    "BufferedCommit" -> MlsException.BufferedCommit()
    "MessageEpochTooOld" -> MlsException.MessageEpochTooOld()
    "SelfCommitIgnored" -> MlsException.SelfCommitIgnored()
    "UnmergedPendingGroup" -> MlsException.UnmergedPendingGroup()
    "StaleProposal" -> MlsException.StaleProposal()
    "StaleCommit" -> MlsException.StaleCommit()
    "OrphanWelcome" -> MlsException.OrphanWelcome()
    "MessageRejected" -> MlsException.MessageRejected(context.reason ?: fallbackMessage ?: "Message rejected")
    else -> MlsException.Other(context?.msg ?: fallbackMessage ?: "Unknown MLS error")
}

private fun mapProteusException(context: JsErrorContext?): ProteusException = when (context?.type) {
    "SessionNotFound" -> ProteusException.SessionNotFound()
    "DuplicateMessage" -> ProteusException.DuplicateMessage()
    "RemoteIdentityChanged" -> ProteusException.RemoteIdentityChanged()
    else -> ProteusException.Other((context?.errorCode ?: 0).toUShort())
}

private fun ByteArray.toUint8Array(): Uint8Array = Uint8Array(toTypedArray())
private fun Uint8Array.toByteArray(): ByteArray = ByteArray(length) { (this.asDynamic()[it] as Number).toByte() }
private fun dynamicToULong(value: dynamic): ULong = value?.toString()?.toULong() ?: 0u
private fun dynamicToTimestamp(value: dynamic): Instant = Instant.fromEpochSeconds(value.toString().toLong())
private val jsCoreCryptoExport: dynamic get() = JsCoreCryptoModule.asDynamic().CoreCrypto
private val jsCoreCryptoContextExport: dynamic get() = JsCoreCryptoModule.asDynamic().CoreCryptoContext
private fun constructJsBytesType(typeName: String, bytes: ByteArray): dynamic {
    val ctor = JsCoreCryptoModule.asDynamic()[typeName]
    return js("Reflect.construct")(ctor, arrayOf(bytes.toUint8Array()))
}
private fun jsCoreCryptoInit(params: JsCoreCryptoParams): Promise<JsCoreCrypto> =
    jsCoreCryptoExport.init(params).unsafeCast<Promise<JsCoreCrypto>>()
private fun jsCoreCryptoDeferredInit(params: JsCoreCryptoDeferredParams): Promise<JsCoreCrypto> =
    jsCoreCryptoExport.deferredInit(params).unsafeCast<Promise<JsCoreCrypto>>()
private fun jsCoreCryptoHistoryClient(secret: JsHistorySecret): Promise<JsCoreCrypto> =
    jsCoreCryptoExport.historyClient(secret).unsafeCast<Promise<JsCoreCrypto>>()
private fun jsCoreCryptoProteusLastResortPrekeyId(): Int =
    jsCoreCryptoExport.proteusLastResortPrekeyId().unsafeCast<Int>()
private fun jsCoreCryptoProteusFingerprintPrekeybundle(prekey: Uint8Array): String =
    jsCoreCryptoExport.proteusFingerprintPrekeybundle(prekey).unsafeCast<String>()
private fun jsCoreCryptoContextProteusLastResortPrekeyId(): Int =
    jsCoreCryptoContextExport.proteusLastResortPrekeyId().unsafeCast<Int>()
private fun jsCoreCryptoContextProteusFingerprintPrekeybundle(prekey: Uint8Array): String =
    jsCoreCryptoContextExport.proteusFingerprintPrekeybundle(prekey).unsafeCast<String>()
private fun jsCoreCryptoContextFromRaw(rawContext: dynamic): JsCoreCryptoContext =
    jsCoreCryptoContextExport.fromFfiContext(rawContext).unsafeCast<JsCoreCryptoContext>()
private fun newJsClientId(bytes: ByteArray): JsClientId = constructJsBytesType("ClientId", bytes).unsafeCast<JsClientId>()
private fun newJsConversationId(bytes: ByteArray): JsConversationId = constructJsBytesType("ConversationId", bytes).unsafeCast<JsConversationId>()
private fun newJsDatabaseKey(bytes: ByteArray): JsDatabaseKey = constructJsBytesType("DatabaseKey", bytes).unsafeCast<JsDatabaseKey>()
private fun newJsExternalSenderKey(bytes: ByteArray): JsExternalSenderKey = constructJsBytesType("ExternalSenderKey", bytes).unsafeCast<JsExternalSenderKey>()
private fun newJsGroupInfo(bytes: ByteArray): JsGroupInfo = constructJsBytesType("GroupInfo", bytes).unsafeCast<JsGroupInfo>()
private fun newJsWelcome(bytes: ByteArray): JsWelcome = constructJsBytesType("Welcome", bytes).unsafeCast<JsWelcome>()
private fun newJsMlsTransportData(bytes: ByteArray): JsMlsTransportData = constructJsBytesType("MlsTransportData", bytes).unsafeCast<JsMlsTransportData>()
private fun ciphersuiteToJs(value: Ciphersuite): Int = value.value.toInt()
private fun credentialTypeToJs(value: CredentialType): Int = value.value.toInt()
private fun logLevelToJs(value: CoreCryptoLogLevel): Int = value.value.toInt()
private fun wirePolicyToJs(value: WirePolicy?): Int? = value?.value?.toInt()
private fun encryptionTypeFromJs(value: Int): MlsGroupInfoEncryptionType = MlsGroupInfoEncryptionType.entries.first { it.value.toInt() == value }
private fun ratchetTreeTypeFromJs(value: Int): MlsRatchetTreeType = MlsRatchetTreeType.entries.first { it.value.toInt() == value }
private fun ciphersuiteFromJs(value: Int): Ciphersuite = Ciphersuite.entries.first { it.value.toInt() == value }
private fun credentialTypeFromJs(value: Int): CredentialType = CredentialType.entries.first { it.value.toInt() == value }
private fun deviceStatusFromJs(value: Int): DeviceStatus = DeviceStatus.entries.first { it.value.toInt() == value }
private fun e2eiConversationStateFromJs(value: Int): E2eiConversationState = E2eiConversationState.entries.first { it.value.toInt() == value }

private fun customConfigurationToJs(customConfiguration: CustomConfiguration): JsCustomConfiguration =
    (js("({})") as JsCustomConfiguration).also {
        it.keyRotationSpan = customConfiguration.keyRotationSpan?.inWholeSeconds?.toInt()
        it.wirePolicy = wirePolicyToJs(customConfiguration.wirePolicy)
    }

private fun conversationConfigurationToJs(configuration: ConversationConfiguration): JsConversationConfiguration =
    (js("({})") as JsConversationConfiguration).also {
        it.ciphersuite = configuration.ciphersuite?.let(::ciphersuiteToJs)
        it.externalSenders = configuration.externalSenders.map { key -> key.requireJs() }.toTypedArray()
        it.keyRotationSpan = configuration.custom.keyRotationSpan?.inWholeSeconds?.toInt()
        it.wirePolicy = wirePolicyToJs(configuration.custom.wirePolicy)
    }

private fun historySecretToJs(secret: HistorySecret): JsHistorySecret =
    (js("({})") as JsHistorySecret).also {
        it.clientId = secret.clientId.requireJs()
        it.data = secret.data.toUint8Array()
    }

private fun mapWireIdentity(identity: JsWireIdentity?): WireIdentity = WireIdentity(
    clientId = identity?.clientId ?: "",
    status = deviceStatusFromJs(identity?.status ?: DeviceStatus.VALID.value.toInt()),
    thumbprint = identity?.thumbprint ?: "",
    credentialType = credentialTypeFromJs(identity?.credentialType ?: CredentialType.BASIC.value.toInt()),
    x509Identity = identity?.x509Identity?.let(::mapX509Identity),
)

private fun mapX509Identity(identity: JsX509Identity): X509Identity = X509Identity(
    handle = identity.handle,
    displayName = identity.displayName,
    domain = identity.domain,
    certificate = identity.certificate,
    serialNumber = identity.serialNumber,
    notBefore = dynamicToTimestamp(identity.notBefore),
    notAfter = dynamicToTimestamp(identity.notAfter),
)

private fun mapBufferedDecryptedMessage(message: JsBufferedDecryptedMessage): BufferedDecryptedMessage = BufferedDecryptedMessage(
    message = message.message?.toByteArray(),
    isActive = message.isActive,
    commitDelay = if (message.commitDelay == null) null else dynamicToULong(message.commitDelay),
    senderClientId = message.senderClientId?.let(::ClientId),
    hasEpochChanged = message.hasEpochChanged,
    identity = mapWireIdentity(message.identity),
    crlNewDistributionPoints = message.crlNewDistributionPoints?.toList(),
)

private fun mapDecryptedMessage(message: JsDecryptedMessage): DecryptedMessage = DecryptedMessage(
    message = message.message?.toByteArray(),
    isActive = message.isActive,
    commitDelay = if (message.commitDelay == null) null else dynamicToULong(message.commitDelay),
    senderClientId = message.senderClientId?.let(::ClientId),
    hasEpochChanged = message.hasEpochChanged,
    identity = mapWireIdentity(message.identity),
    bufferedMessages = message.bufferedMessages?.map(::mapBufferedDecryptedMessage),
    crlNewDistributionPoints = message.crlNewDistributionPoints?.toList(),
)

private fun mapGroupInfoBundle(bundle: JsGroupInfoBundle): GroupInfoBundle = GroupInfoBundle(
    encryptionType = encryptionTypeFromJs(bundle.encryptionType),
    ratchetTreeType = ratchetTreeTypeFromJs(bundle.ratchetTreeType),
    payload = GroupInfo(bundle.payload),
)

private fun mapCommitBundle(bundle: JsCommitBundle): CommitBundle = CommitBundle(
    welcome = bundle.welcome?.let(::Welcome),
    commit = bundle.commit.toByteArray(),
    groupInfo = mapGroupInfoBundle(bundle.groupInfo),
    encryptedMessage = bundle.encryptedMessage?.toByteArray(),
)

private fun mapWelcomeBundle(bundle: JsWelcomeBundle): WelcomeBundle = WelcomeBundle(
    id = ConversationId(bundle.id),
    crlNewDistributionPoints = bundle.crlNewDistributionPoints?.toList(),
)

private fun mapCrlRegistration(registration: JsCrlRegistration): CrlRegistration = CrlRegistration(
    dirty = registration.dirty,
    expiration = registration.expiration?.let(::dynamicToULong),
)

private fun mapProteusAutoPrekeyBundle(bundle: JsProteusAutoPrekeyBundle): ProteusAutoPrekeyBundle = ProteusAutoPrekeyBundle(
    id = bundle.id.toUShort(),
    pkb = bundle.pkb.toByteArray(),
)

private fun mapAcmeChallenge(challenge: JsAcmeChallenge): AcmeChallenge = AcmeChallenge(
    delegate = challenge.delegate.toByteArray(),
    url = challenge.url,
    target = challenge.target,
)

private fun mapAcmeDirectory(directory: JsAcmeDirectory): AcmeDirectory = AcmeDirectory(
    newNonce = directory.newNonce,
    newAccount = directory.newAccount,
    newOrder = directory.newOrder,
    revokeCert = directory.revokeCert,
)

private fun mapNewAcmeAuthz(authz: JsNewAcmeAuthz): NewAcmeAuthz = NewAcmeAuthz(
    identifier = authz.identifier,
    keyauth = authz.keyauth,
    challenge = mapAcmeChallenge(authz.challenge),
)

private fun mapNewAcmeOrder(order: JsNewAcmeOrder): NewAcmeOrder = NewAcmeOrder(
    delegate = order.delegate.toByteArray(),
    authorizations = order.authorizations.toList(),
)

private fun mapUserIdentities(map: dynamic): Map<String, List<WireIdentity>> {
    val keys = js("Object.keys(map)") as Array<String>
    return keys.associateWith { key ->
        (map.asDynamic()[key] as Array<JsWireIdentity>).map(::mapWireIdentity)
    }
}

private fun mapTransportResponse(response: dynamic): MlsTransportResponse = when {
    response == null -> MlsTransportResponse.Success
    response == "success" -> MlsTransportResponse.Success
    response == "retry" -> MlsTransportResponse.Retry
    response.abort != undefined && response.abort != null -> MlsTransportResponse.Abort(response.abort.reason as String)
    else -> CoreCryptoException.Other("Unknown MLS transport response: ${response.toString()}").let { throw it }
}

private fun dynamicBytesToByteArray(value: dynamic): ByteArray {
    if (value == null) {
        return byteArrayOf()
    }
    return when {
        value.buffer != undefined && value.length != undefined -> (value as Uint8Array).toByteArray()
        value.length != undefined -> {
            val array = value.unsafeCast<Array<dynamic>>()
            ByteArray(array.size) { index -> (array[index] as Number).toByte() }
        }
        else -> throw CoreCryptoException.Other("Unsupported byte container returned from JS Proteus runtime")
    }
}

private fun mapProteusEncryptBatchedResult(jsValue: dynamic): Map<String, ByteArray> {
    if (jsValue == null) {
        return emptyMap()
    }

    val arrayFrom = js("Array.from")
    val objectKeys = js("Object.keys")
    val mapKeys = jsValue.keys
    val mapGet = jsValue.get

    return when {
        mapKeys != undefined && mapGet != undefined -> {
            val keys = arrayFrom(mapKeys.call(jsValue)).unsafeCast<Array<String>>()
            keys.associateWith { key -> dynamicBytesToByteArray(mapGet.call(jsValue, key)) }
        }

        js("Array.isArray")(jsValue).unsafeCast<Boolean>() -> {
            val entries = jsValue.unsafeCast<Array<Array<dynamic>>>()
            entries.associate { entry ->
                val key = entry[0] as String
                key to dynamicBytesToByteArray(entry[1])
            }
        }

        else -> {
            val keys = objectKeys(jsValue).unsafeCast<Array<String>>()
            keys.associateWith { key -> dynamicBytesToByteArray(jsValue[key]) }
        }
    }
}

private fun jsProteusDecryptSafeOrFallback(context: JsCoreCryptoContext, sessionId: String, ciphertext: Uint8Array): Promise<Uint8Array> {
    val jsContext = context.asDynamic()
    val decryptSafe = jsContext.proteusDecryptSafe
    return if (decryptSafe == undefined || decryptSafe == null) {
        jsContext.proteusDecrypt(sessionId, ciphertext).unsafeCast<Promise<Uint8Array>>()
    } else {
        decryptSafe.call(jsContext, sessionId, ciphertext).unsafeCast<Promise<Uint8Array>>()
    }
}

private fun patchJsCoreCryptoContext(rawContext: dynamic): JsCoreCryptoContext {
    val context = jsCoreCryptoContextFromRaw(rawContext)
    val dynamicContext = context.asDynamic()

    if (dynamicContext.proteusDecryptSafe == undefined || dynamicContext.proteusDecryptSafe == null) {
        dynamicContext.proteusDecryptSafe = { sessionId: String, ciphertext: Uint8Array ->
            rawContext.proteus_decrypt_safe(sessionId, ciphertext)
        }
    }

    return context
}

private fun jsCoreCryptoTransaction(coreCrypto: JsCoreCrypto, command: CoreCryptoCommand): Promise<Any?> {
    val rawCoreCrypto = coreCrypto.inner().asDynamic()
    val jsCommand = js("({})")
    jsCommand.execute = { rawContext: dynamic ->
        GlobalScope.promise<Any?> {
            command.execute(CoreCryptoContext(patchJsCoreCryptoContext(rawContext)))
            null
        }
    }
    return rawCoreCrypto.transaction(jsCommand).unsafeCast<Promise<Any?>>()
}

private fun mlsTransportToJs(transport: MlsTransport): JsMlsTransport {
    val jsTransport = js("({})")
    jsTransport.sendCommitBundle = { commitBundle: JsCommitBundle ->
        GlobalScope.promise<dynamic> {
            val result = transport.sendCommitBundle(mapCommitBundle(commitBundle))
            when (result) {
                is MlsTransportResponse.Success -> "success"
                is MlsTransportResponse.Retry -> "retry"
                is MlsTransportResponse.Abort -> js("({ abort: { reason: result.reason } })")
            }
        }
    }
    jsTransport.sendMessage = { message: Uint8Array ->
        GlobalScope.promise<dynamic> {
            val result = transport.sendMessage(message.toByteArray())
            when (result) {
                is MlsTransportResponse.Success -> "success"
                is MlsTransportResponse.Retry -> "retry"
                is MlsTransportResponse.Abort -> js("({ abort: { reason: result.reason } })")
            }
        }
    }
    jsTransport.prepareForTransport = { secret: JsHistorySecret ->
        GlobalScope.promise<JsMlsTransportData> {
            newJsMlsTransportData(
                transport.prepareForTransport(
                    HistorySecret(ClientId(secret.clientId), secret.data.toByteArray())
                )
            )
        }
    }
    return jsTransport.unsafeCast<JsMlsTransport>()
}

private fun coreCryptoLoggerToJs(logger: CoreCryptoLogger): JsCoreCryptoLogger {
    val jsLogger = js("({})")
    jsLogger.log = { level: Int, message: String, context: String? ->
        logger.log(CoreCryptoLogLevel.entries.first { entry -> entry.value.toInt() == level }, message, context)
    }
    return jsLogger.unsafeCast<JsCoreCryptoLogger>()
}

private fun epochObserverToJs(observer: EpochObserver): JsEpochObserver {
    val jsObserver = js("({})")
    jsObserver.epochChanged = { conversationId: JsConversationId, epoch: dynamic ->
        GlobalScope.promise<Unit> { observer.epochChanged(ConversationId(conversationId), dynamicToULong(epoch)) }
    }
    return jsObserver.unsafeCast<JsEpochObserver>()
}

private fun historyObserverToJs(observer: HistoryObserver): JsHistoryObserver {
    val jsObserver = js("({})")
    jsObserver.historyClientCreated = { conversationId: JsConversationId, secret: JsHistorySecret ->
        GlobalScope.promise<Unit> {
            observer.historyClientCreated(
                ConversationId(conversationId),
                HistorySecret(ClientId(secret.clientId), secret.data.toByteArray())
            )
        }
    }
    return jsObserver.unsafeCast<JsHistoryObserver>()
}

private abstract class WebHandle {
    open fun free() {}
}

private open class ByteArrayHandle(private val data: ByteArray) : WebHandle() {
    fun bytes(): ByteArray = data.copyOf()
    override fun equals(other: Any?): Boolean = other is ByteArrayHandle && data.contentEquals(other.data)
    override fun hashCode(): Int = data.contentHashCode()
    override fun toString(): String = data.decodeToString()
}

public actual open class ClientId : Disposable, ClientIdInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal constructor(js: JsClientId) {
        bytesHandle = ByteArrayHandle(js.copyBytes().toByteArray())
    }

    internal fun requireJs(): JsClientId = newJsClientId(copyBytes())

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    public actual companion object
}

public actual open class ConversationId : Disposable, ConversationIdInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal constructor(js: JsConversationId) {
        bytesHandle = ByteArrayHandle(js.copyBytes().toByteArray())
    }

    internal fun requireJs(): JsConversationId = newJsConversationId(copyBytes())

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    actual override fun toString(): String = bytesHandle?.toString() ?: ""
    actual override fun equals(other: Any?): Boolean = other is ConversationId && copyBytes().contentEquals(other.copyBytes())
    actual override fun hashCode(): Int = copyBytes().contentHashCode()
    public actual companion object
}

public actual open class Database : Disposable, DatabaseInterface {
    internal val js: JsDatabase?

    public actual constructor(noPointer: NoPointer) {
        js = null
    }

    internal constructor(database: JsDatabase) {
        js = database
    }

    actual override fun destroy(): Unit {
        js?.free()
    }

    actual override fun close(): Unit = destroy()
    public actual companion object
}

public actual open class DatabaseKey : Disposable, DatabaseKeyInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(key: ByteArray) {
        bytesHandle = ByteArrayHandle(key)
    }

    internal fun requireJs(): JsDatabaseKey = newJsDatabaseKey(bytesHandle?.bytes() ?: byteArrayOf())

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    actual override fun equals(other: Any?): Boolean = other is DatabaseKey && (bytesHandle?.bytes() ?: byteArrayOf()).contentEquals(other.bytesHandle?.bytes() ?: byteArrayOf())
    public actual companion object
}

public actual open class ExternalSenderKey : Disposable, ExternalSenderKeyInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal constructor(js: JsExternalSenderKey) {
        bytesHandle = ByteArrayHandle(js.copyBytes().toByteArray())
    }

    internal fun requireJs(): JsExternalSenderKey = newJsExternalSenderKey(copyBytes())

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    actual override fun toString(): String = bytesHandle?.toString() ?: ""
    actual override fun equals(other: Any?): Boolean = other is ExternalSenderKey && copyBytes().contentEquals(other.copyBytes())
    actual override fun hashCode(): Int = copyBytes().contentHashCode()
    public actual companion object
}

public actual open class GroupInfo : Disposable, GroupInfoInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal constructor(js: JsGroupInfo) {
        bytesHandle = ByteArrayHandle(js.copyBytes().toByteArray())
    }

    internal fun requireJs(): JsGroupInfo = newJsGroupInfo(copyBytes())

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    actual override fun toString(): String = bytesHandle?.toString() ?: ""
    actual override fun equals(other: Any?): Boolean = other is GroupInfo && copyBytes().contentEquals(other.copyBytes())
    actual override fun hashCode(): Int = copyBytes().contentHashCode()
    public actual companion object
}

public actual open class KeyPackage : Disposable, KeyPackageInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal fun requireJs(): Uint8Array = copyBytes().toUint8Array()

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    actual override fun toString(): String = bytesHandle?.toString() ?: ""
    actual override fun equals(other: Any?): Boolean = other is KeyPackage && copyBytes().contentEquals(other.copyBytes())
    actual override fun hashCode(): Int = copyBytes().contentHashCode()
    public actual companion object
}

public actual open class SecretKey : Disposable, SecretKeyInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal constructor(js: JsSecretKey) {
        bytesHandle = ByteArrayHandle(js.copyBytes().toByteArray())
    }

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    actual override fun toString(): String = bytesHandle?.toString() ?: ""
    actual override fun equals(other: Any?): Boolean = other is SecretKey && copyBytes().contentEquals(other.copyBytes())
    actual override fun hashCode(): Int = copyBytes().contentHashCode()
    public actual companion object
}

public actual open class Welcome : Disposable, WelcomeInterface {
    private val bytesHandle: ByteArrayHandle?

    public actual constructor(noPointer: NoPointer) {
        bytesHandle = null
    }

    public actual constructor(bytes: ByteArray) {
        bytesHandle = ByteArrayHandle(bytes)
    }

    internal constructor(js: JsWelcome) {
        bytesHandle = ByteArrayHandle(js.copyBytes().toByteArray())
    }

    internal fun requireJs(): JsWelcome = newJsWelcome(copyBytes())

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun copyBytes(): ByteArray = bytesHandle?.bytes() ?: byteArrayOf()
    actual override fun toString(): String = bytesHandle?.toString() ?: ""
    actual override fun equals(other: Any?): Boolean = other is Welcome && copyBytes().contentEquals(other.copyBytes())
    actual override fun hashCode(): Int = copyBytes().contentHashCode()
    public actual companion object
}

public actual open class E2eiEnrollment : Disposable, E2eiEnrollmentInterface {
    internal val js: JsE2eiEnrollment?

    public actual constructor(noPointer: NoPointer) {
        js = null
    }

    internal constructor(enrollment: JsE2eiEnrollment) {
        js = enrollment
    }

    private fun requireJs(): JsE2eiEnrollment = js ?: throw CoreCryptoException.Other("E2eiEnrollment is not initialized")

    actual override fun destroy(): Unit {
        js?.free()
    }

    actual override fun close(): Unit = destroy()
    public actual override suspend fun certificateRequest(previousNonce: String): ByteArray = webCall { requireJs().certificateRequest(previousNonce) }.toByteArray()
    public actual override suspend fun checkOrderRequest(orderUrl: String, previousNonce: String): ByteArray = webCall { requireJs().checkOrderRequest(orderUrl, previousNonce) }.toByteArray()
    public actual override suspend fun checkOrderResponse(order: ByteArray): String = webCall { requireJs().checkOrderResponse(order.toUint8Array()) }
    public actual override suspend fun createDpopToken(expirySecs: UInt, backendNonce: String): String = webCall { requireJs().createDpopToken(expirySecs.toInt(), backendNonce) }.toByteArray().decodeToString()
    public actual override suspend fun directoryResponse(directory: ByteArray): AcmeDirectory = mapAcmeDirectory(webCall { requireJs().directoryResponse(directory.toUint8Array()) })
    public actual override suspend fun finalizeRequest(previousNonce: String): ByteArray = webCall { requireJs().finalizeRequest(previousNonce) }.toByteArray()
    public actual override suspend fun finalizeResponse(finalize: ByteArray): String = webCall { requireJs().finalizeResponse(finalize.toUint8Array()) }
    public actual override suspend fun newAccountRequest(previousNonce: String): ByteArray = webCall { requireJs().newAccountRequest(previousNonce) }.toByteArray()
    public actual override suspend fun newAccountResponse(account: ByteArray) { webCall { requireJs().newAccountResponse(account.toUint8Array()) } }
    public actual override suspend fun newAuthzRequest(url: String, previousNonce: String): ByteArray = webCall { requireJs().newAuthzRequest(url, previousNonce) }.toByteArray()
    public actual override suspend fun newAuthzResponse(authz: ByteArray): NewAcmeAuthz = mapNewAcmeAuthz(webCall { requireJs().newAuthzResponse(authz.toUint8Array()) })
    public actual override suspend fun newDpopChallengeRequest(accessToken: String, previousNonce: String): ByteArray = webCall { requireJs().newDpopChallengeRequest(accessToken, previousNonce) }.toByteArray()
    public actual override suspend fun newDpopChallengeResponse(challenge: ByteArray) { webCall { requireJs().newDpopChallengeResponse(challenge.toUint8Array()) } }
    public actual override suspend fun newOidcChallengeRequest(idToken: String, previousNonce: String): ByteArray = webCall { requireJs().newOidcChallengeRequest(idToken, previousNonce) }.toByteArray()
    public actual override suspend fun newOidcChallengeResponse(challenge: ByteArray) { webCall { requireJs().newOidcChallengeResponse(challenge.toUint8Array()) } }
    public actual override suspend fun newOrderRequest(previousNonce: String): ByteArray = webCall { requireJs().newOrderRequest(previousNonce) }.toByteArray()
    public actual override suspend fun newOrderResponse(order: ByteArray): NewAcmeOrder = mapNewAcmeOrder(webCall { requireJs().newOrderResponse(order.toUint8Array()) })
    public actual companion object
}

public actual open class CoreCrypto : Disposable, CoreCryptoInterface {
    internal val js: JsCoreCrypto?
    private var destroyed = false

    public actual constructor(noPointer: NoPointer) {
        js = null
    }

    internal constructor(coreCrypto: JsCoreCrypto) {
        js = coreCrypto
    }

    private fun requireJs(): JsCoreCrypto {
        val instance = js ?: throw CoreCryptoException.Other("CoreCrypto is not initialized")
        if (destroyed) {
            throw IllegalStateException("CoreCrypto client is destroyed")
        }
        return instance
    }

    actual override fun destroy(): Unit = close()
    actual override fun close(): Unit {
        val instance = js ?: return
        if (destroyed) {
            return
        }
        destroyed = true
        GlobalScope.promise {
            try {
                ensureWasmInitialized()
                instance.close().await()
            } catch (_: Throwable) {
                Unit
            }
        }
    }

    public actual override suspend fun clientPublicKey(ciphersuite: Ciphersuite, credentialType: CredentialType): ByteArray = webCall {
        requireJs().clientPublicKey(ciphersuiteToJs(ciphersuite), credentialTypeToJs(credentialType))
    }.toByteArray()

    public actual override suspend fun conversationCiphersuite(conversationId: ConversationId): Ciphersuite = ciphersuiteFromJs(
        webCall { requireJs().conversationCiphersuite(conversationId.requireJs()) }.toInt()
    )

    public actual override suspend fun conversationEpoch(conversationId: ConversationId): ULong = dynamicToULong(
        webCall { requireJs().conversationEpoch(conversationId.requireJs()) }
    )

    public actual override suspend fun conversationExists(conversationId: ConversationId): Boolean = webCall {
        requireJs().conversationExists(conversationId.requireJs())
    }

    public actual override suspend fun e2eiConversationState(conversationId: ConversationId): E2eiConversationState = e2eiConversationStateFromJs(
        webCall { requireJs().e2eiConversationState(conversationId.requireJs()) }.toInt()
    )

    public actual override suspend fun e2eiIsEnabled(ciphersuite: Ciphersuite): Boolean = webCall {
        requireJs().e2eiIsEnabled(ciphersuiteToJs(ciphersuite))
    }

    public actual override suspend fun e2eiIsPkiEnvSetup(): Boolean = webCall { requireJs().e2eiIsPKIEnvSetup() }
    public actual override suspend fun exportSecretKey(conversationId: ConversationId, keyLength: UInt): ByteArray = webCall {
        requireJs().exportSecretKey(conversationId.requireJs(), keyLength.toInt())
    }.copyBytes().toByteArray()

    public actual override suspend fun getClientIds(conversationId: ConversationId): List<ClientId> = webCall {
        requireJs().getClientIds(conversationId.requireJs())
    }.map(::ClientId)

    public actual override suspend fun getDeviceIdentities(conversationId: ConversationId, deviceIds: List<ClientId>): List<WireIdentity> = webCall {
        requireJs().getDeviceIdentities(conversationId.requireJs(), deviceIds.map { it.requireJs() }.toTypedArray())
    }.map(::mapWireIdentity)

    public actual override suspend fun getExternalSender(conversationId: ConversationId): ByteArray = webCall {
        requireJs().getExternalSender(conversationId.requireJs())
    }.copyBytes().toByteArray()

    public actual override suspend fun getUserIdentities(conversationId: ConversationId, userIds: List<String>): Map<String, List<WireIdentity>> = mapUserIdentities(
        webCall { requireJs().getUserIdentities(conversationId.requireJs(), userIds.toTypedArray()) }
    )

    public actual override suspend fun isHistorySharingEnabled(conversationId: ConversationId): Boolean = webCall {
        requireJs().isHistorySharingEnabled(conversationId.requireJs())
    }

    public actual override suspend fun proteusFingerprint(): String = webCall { requireJs().proteusFingerprint() }
    public actual override fun proteusFingerprintPrekeybundle(prekey: ByteArray): String = jsCoreCryptoProteusFingerprintPrekeybundle(prekey.toUint8Array())
    public actual override suspend fun proteusFingerprintLocal(sessionId: String): String = webCall { requireJs().proteusFingerprintLocal(sessionId) }
    public actual override suspend fun proteusFingerprintRemote(sessionId: String): String = webCall { requireJs().proteusFingerprintRemote(sessionId) }
    public actual override fun proteusLastResortPrekeyId(): UShort = jsCoreCryptoProteusLastResortPrekeyId().toUShort()
    public actual override suspend fun proteusSessionExists(sessionId: String): Boolean = webCall { requireJs().proteusSessionExists(sessionId) }
    public actual override suspend fun provideTransport(callbacks: MlsTransport) { webCall { requireJs().provideTransport(mlsTransportToJs(callbacks)) } }
    public actual override suspend fun randomBytes(len: UInt): ByteArray = webCall { requireJs().randomBytes(len.toInt()) }.toByteArray()
    public actual override suspend fun registerEpochObserver(epochObserver: EpochObserver) { webCall { requireJs().registerEpochObserver(epochObserverToJs(epochObserver)) } }
    public actual override suspend fun registerHistoryObserver(historyObserver: HistoryObserver) { webCall { requireJs().registerHistoryObserver(historyObserverToJs(historyObserver)) } }
    public actual override suspend fun reseedRng(seed: ByteArray) { webCall { requireJs().reseedRng(seed.toUint8Array()) } }
    public actual override suspend fun transaction(command: CoreCryptoCommand) {
        webCall {
            jsCoreCryptoTransaction(requireJs(), command)
        }
    }

    public actual companion object
}

public actual open class CoreCryptoContext : Disposable, CoreCryptoContextInterface {
    internal val js: JsCoreCryptoContext?

    public actual constructor(noPointer: NoPointer) {
        js = null
    }

    internal constructor(context: JsCoreCryptoContext) {
        js = context
    }

    private fun requireJs(): JsCoreCryptoContext = js ?: throw CoreCryptoException.Other("CoreCryptoContext is not initialized")

    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override suspend fun addClientsToConversation(conversationId: ConversationId, keyPackages: List<KeyPackage>): List<String>? = webCall {
        requireJs().addClientsToConversation(conversationId.requireJs(), keyPackages.map { it.requireJs() }.toTypedArray())
    }?.toList()
    public actual override suspend fun clientKeypackages(ciphersuite: Ciphersuite, credentialType: CredentialType, amountRequested: UInt): List<KeyPackage> = webCall {
        requireJs().clientKeypackages(ciphersuiteToJs(ciphersuite), credentialTypeToJs(credentialType), amountRequested.toInt())
    }.map { bytes -> KeyPackage(bytes.toByteArray()) }
    public actual override suspend fun clientPublicKey(ciphersuite: Ciphersuite, credentialType: CredentialType): ByteArray = webCall {
        requireJs().clientPublicKey(ciphersuiteToJs(ciphersuite), credentialTypeToJs(credentialType))
    }.toByteArray()
    public actual override suspend fun clientValidKeypackagesCount(ciphersuite: Ciphersuite, credentialType: CredentialType): ULong = dynamicToULong(webCall {
        requireJs().clientValidKeypackagesCount(ciphersuiteToJs(ciphersuite), credentialTypeToJs(credentialType))
    })
    public actual override suspend fun commitPendingProposals(conversationId: ConversationId) { webCall { requireJs().commitPendingProposals(conversationId.requireJs()) } }
    public actual override suspend fun conversationCiphersuite(conversationId: ConversationId): Ciphersuite = ciphersuiteFromJs(webCall { requireJs().conversationCiphersuite(conversationId.requireJs()) }.toInt())
    public actual override suspend fun conversationEpoch(conversationId: ConversationId): ULong = dynamicToULong(webCall { requireJs().conversationEpoch(conversationId.requireJs()) })
    public actual override suspend fun conversationExists(conversationId: ConversationId): Boolean = webCall { requireJs().conversationExists(conversationId.requireJs()) }
    public actual override suspend fun createConversation(conversationId: ConversationId, creatorCredentialType: CredentialType, config: ConversationConfiguration) { webCall { requireJs().createConversation(conversationId.requireJs(), credentialTypeToJs(creatorCredentialType), conversationConfigurationToJs(config)) } }
    public actual override suspend fun decryptMessage(conversationId: ConversationId, payload: ByteArray): DecryptedMessage = mapDecryptedMessage(webCall { requireJs().decryptMessage(conversationId.requireJs(), payload.toUint8Array()) })
    public actual override suspend fun deleteStaleKeyPackages(ciphersuite: Ciphersuite) { webCall { requireJs().deleteStaleKeyPackages(ciphersuiteToJs(ciphersuite)) } }
    public actual override suspend fun disableHistorySharing(conversationId: ConversationId) { webCall { requireJs().disableHistorySharing(conversationId.requireJs()) } }
    public actual override suspend fun e2eiConversationState(conversationId: ConversationId): E2eiConversationState = e2eiConversationStateFromJs(
        webCall { requireJs().e2eiConversationState(conversationId.requireJs()) }.toInt()
    )
    public actual override suspend fun e2eiEnrollmentStash(enrollment: E2eiEnrollment): ByteArray = webCall { requireJs().e2eiEnrollmentStash(enrollment.js ?: throw CoreCryptoException.Other("E2eiEnrollment is not initialized")) }.toByteArray()
    public actual override suspend fun e2eiEnrollmentStashPop(handle: ByteArray): E2eiEnrollment = E2eiEnrollment(webCall { requireJs().e2eiEnrollmentStashPop(handle.toUint8Array()) })
    public actual override suspend fun e2eiIsEnabled(ciphersuite: Ciphersuite): Boolean = webCall { requireJs().e2eiIsEnabled(ciphersuiteToJs(ciphersuite)) }
    public actual override suspend fun e2eiIsPkiEnvSetup(): Boolean = webCall { requireJs().e2eiIsPKIEnvSetup() }
    public actual override suspend fun e2eiMlsInitOnly(enrollment: E2eiEnrollment, certificateChain: String, nbKeyPackage: UInt?): List<String>? = webCall { requireJs().e2eiMlsInitOnly(enrollment.js ?: throw CoreCryptoException.Other("E2eiEnrollment is not initialized"), certificateChain, nbKeyPackage?.toInt()) }?.toList()
    public actual override suspend fun e2eiNewActivationEnrollment(displayName: String, handle: String, team: String?, expirySec: UInt, ciphersuite: Ciphersuite): E2eiEnrollment = E2eiEnrollment(webCall { requireJs().e2eiNewActivationEnrollment(displayName, handle, expirySec.toInt(), ciphersuiteToJs(ciphersuite), team) })
    public actual override suspend fun e2eiNewEnrollment(clientId: String, displayName: String, handle: String, team: String?, expirySec: UInt, ciphersuite: Ciphersuite): E2eiEnrollment = E2eiEnrollment(webCall { requireJs().e2eiNewEnrollment(clientId, displayName, handle, expirySec.toInt(), ciphersuiteToJs(ciphersuite), team) })
    public actual override suspend fun e2eiNewRotateEnrollment(displayName: String?, handle: String?, team: String?, expirySec: UInt, ciphersuite: Ciphersuite): E2eiEnrollment = E2eiEnrollment(webCall { requireJs().e2eiNewRotateEnrollment(expirySec.toInt(), ciphersuiteToJs(ciphersuite), displayName, handle, team) })
    public actual override suspend fun e2eiRegisterAcmeCa(trustAnchorPem: String) { webCall { requireJs().e2eiRegisterAcmeCA(trustAnchorPem) } }
    public actual override suspend fun e2eiRegisterCrl(crlDp: String, crlDer: ByteArray): CrlRegistration = mapCrlRegistration(webCall { requireJs().e2eiRegisterCRL(crlDp, crlDer.toUint8Array()) })
    public actual override suspend fun e2eiRegisterIntermediateCa(certPem: String): List<String>? = webCall { requireJs().e2eiRegisterIntermediateCA(certPem) }?.toList()
    public actual override suspend fun e2eiRotate(conversationId: ConversationId) { webCall { requireJs().e2eiRotate(conversationId.requireJs()) } }
    public actual override suspend fun enableHistorySharing(conversationId: ConversationId) { webCall { requireJs().enableHistorySharing(conversationId.requireJs()) } }
    public actual override suspend fun encryptMessage(conversationId: ConversationId, message: ByteArray): ByteArray = webCall { requireJs().encryptMessage(conversationId.requireJs(), message.toUint8Array()) }.toByteArray()
    public actual override suspend fun exportSecretKey(conversationId: ConversationId, keyLength: UInt): SecretKey = SecretKey(webCall { requireJs().exportSecretKey(conversationId.requireJs(), keyLength.toInt()) })
    public actual override suspend fun getClientIds(conversationId: ConversationId): List<ClientId> = webCall { requireJs().getClientIds(conversationId.requireJs()) }.map(::ClientId)
    public actual override suspend fun getData(): ByteArray? = webCall { requireJs().getData() }?.toByteArray()
    public actual override suspend fun getDeviceIdentities(conversationId: ConversationId, deviceIds: List<ClientId>): List<WireIdentity> = webCall { requireJs().getDeviceIdentities(conversationId.requireJs(), deviceIds.map { it.requireJs() }.toTypedArray()) }.map(::mapWireIdentity)
    public actual override suspend fun getExternalSender(conversationId: ConversationId): ExternalSenderKey = ExternalSenderKey(webCall { requireJs().getExternalSender(conversationId.requireJs()) })
    public actual override suspend fun getUserIdentities(conversationId: ConversationId, userIds: List<String>): Map<String, List<WireIdentity>> = mapUserIdentities(webCall { requireJs().getUserIdentities(conversationId.requireJs(), userIds.toTypedArray()) })
    public actual override suspend fun joinByExternalCommit(groupInfo: GroupInfo, customConfiguration: CustomConfiguration, credentialType: CredentialType): WelcomeBundle = mapWelcomeBundle(webCall { requireJs().joinByExternalCommit(groupInfo.requireJs(), credentialTypeToJs(credentialType), customConfigurationToJs(customConfiguration)) })
    public actual override suspend fun markConversationAsChildOf(childId: ConversationId, parentId: ConversationId) { webCall { requireJs().markConversationAsChildOf(childId.requireJs(), parentId.requireJs()) } }
    public actual override suspend fun mlsInit(clientId: ClientId, ciphersuites: List<Ciphersuite>, nbKeyPackage: UInt?) { webCall { requireJs().mlsInit(clientId.requireJs(), ciphersuites.map(::ciphersuiteToJs).toTypedArray(), nbKeyPackage?.toInt()) } }
    public actual override suspend fun processWelcomeMessage(welcomeMessage: Welcome, customConfiguration: CustomConfiguration): WelcomeBundle = mapWelcomeBundle(webCall { requireJs().processWelcomeMessage(welcomeMessage.requireJs(), customConfigurationToJs(customConfiguration)) })
    public actual override suspend fun proteusDecrypt(sessionId: String, ciphertext: ByteArray): ByteArray = webCall { requireJs().proteusDecrypt(sessionId, ciphertext.toUint8Array()) }.toByteArray()
    public actual override suspend fun proteusDecryptSafe(sessionId: String, ciphertext: ByteArray): ByteArray = webCall {
        jsProteusDecryptSafeOrFallback(requireJs(), sessionId, ciphertext.toUint8Array())
    }.toByteArray()
    public actual override suspend fun proteusEncrypt(sessionId: String, plaintext: ByteArray): ByteArray = webCall { requireJs().proteusEncrypt(sessionId, plaintext.toUint8Array()) }.toByteArray()
    public actual override suspend fun proteusEncryptBatched(sessions: List<String>, plaintext: ByteArray): Map<String, ByteArray> =
        mapProteusEncryptBatchedResult(webCall { requireJs().proteusEncryptBatched(sessions.toTypedArray(), plaintext.toUint8Array()) })
    public actual override suspend fun proteusFingerprint(): String = webCall { requireJs().proteusFingerprint() }
    public actual override fun proteusFingerprintPrekeybundle(prekey: ByteArray): String = jsCoreCryptoContextProteusFingerprintPrekeybundle(prekey.toUint8Array())
    public actual override suspend fun proteusFingerprintLocal(sessionId: String): String = webCall { requireJs().proteusFingerprintLocal(sessionId) }
    public actual override suspend fun proteusFingerprintRemote(sessionId: String): String = webCall { requireJs().proteusFingerprintRemote(sessionId) }
    public actual override fun proteusLastResortPrekeyId(): UShort = jsCoreCryptoContextProteusLastResortPrekeyId().toUShort()
    public actual override suspend fun proteusInit() { webCall { requireJs().proteusInit() } }
    public actual override suspend fun proteusLastResortPrekey(): ByteArray = webCall { requireJs().proteusLastResortPrekey() }.toByteArray()
    public actual override suspend fun proteusNewPrekey(prekeyId: UShort): ByteArray = webCall { requireJs().proteusNewPrekey(prekeyId.toInt()) }.toByteArray()
    public actual override suspend fun proteusNewPrekeyAuto(): ProteusAutoPrekeyBundle = mapProteusAutoPrekeyBundle(webCall { requireJs().proteusNewPrekeyAuto() })
    public actual override suspend fun proteusReloadSessions() { webCall { requireJs().proteusReloadSessions() } }
    public actual override suspend fun proteusSessionDelete(sessionId: String) { webCall { requireJs().proteusSessionDelete(sessionId) } }
    public actual override suspend fun proteusSessionExists(sessionId: String): Boolean = webCall { requireJs().proteusSessionExists(sessionId) }
    public actual override suspend fun proteusSessionFromMessage(sessionId: String, envelope: ByteArray): ByteArray = webCall { requireJs().proteusSessionFromMessage(sessionId, envelope.toUint8Array()) }.toByteArray()
    public actual override suspend fun proteusSessionFromPrekey(sessionId: String, prekey: ByteArray) { webCall { requireJs().proteusSessionFromPrekey(sessionId, prekey.toUint8Array()) } }
    public actual override suspend fun proteusSessionSave(sessionId: String) { webCall { requireJs().proteusSessionSave(sessionId) } }
    public actual override suspend fun randomBytes(len: UInt): ByteArray = webCall { requireJs().randomBytes(len.toInt()) }.toByteArray()
    public actual override suspend fun removeClientsFromConversation(conversationId: ConversationId, clients: List<ClientId>) { webCall { requireJs().removeClientsFromConversation(conversationId.requireJs(), clients.map { it.requireJs() }.toTypedArray()) } }
    public actual override suspend fun saveX509Credential(enrollment: E2eiEnrollment, certificateChain: String): List<String>? = webCall { requireJs().saveX509Credential(enrollment.js ?: throw CoreCryptoException.Other("E2eiEnrollment is not initialized"), certificateChain) }?.toList()
    public actual override suspend fun setData(data: ByteArray) { webCall { requireJs().setData(data.toUint8Array()) } }
    public actual override suspend fun updateKeyingMaterial(conversationId: ConversationId) { webCall { requireJs().updateKeyingMaterial(conversationId.requireJs()) } }
    public actual override suspend fun wipeConversation(conversationId: ConversationId) { webCall { requireJs().wipeConversation(conversationId.requireJs()) } }
    public actual companion object
}

public actual open class CoreCryptoCommandImpl : Disposable, CoreCryptoCommand {
    public actual constructor(noPointer: NoPointer)
    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override suspend fun execute(context: CoreCryptoContext) {
        throw CoreCryptoException.Other("CoreCryptoCommandImpl is not used on Kotlin/JS; use CoreCryptoCommand instead")
    }
    public actual companion object
}

public actual open class CoreCryptoLoggerImpl : Disposable, CoreCryptoLogger {
    public actual constructor(noPointer: NoPointer)
    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override fun log(level: CoreCryptoLogLevel, message: String, context: String?): Unit {
        throw LoggingException.Ffi("CoreCryptoLoggerImpl is not used on Kotlin/JS; pass CoreCryptoLogger directly")
    }
    public actual companion object
}

public actual open class EpochObserverImpl : Disposable, EpochObserver {
    public actual constructor(noPointer: NoPointer)
    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override suspend fun epochChanged(conversationId: ConversationId, epoch: ULong) {
        throw EpochChangedReportingException.Ffi("EpochObserverImpl is not used on Kotlin/JS; pass EpochObserver directly")
    }
    public actual companion object
}

public actual open class HistoryObserverImpl : Disposable, HistoryObserver {
    public actual constructor(noPointer: NoPointer)
    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override suspend fun historyClientCreated(conversationId: ConversationId, secret: HistorySecret) {
        throw NewHistoryClientReportingException.Ffi("HistoryObserverImpl is not used on Kotlin/JS; pass HistoryObserver directly")
    }
    public actual companion object
}

public actual open class MlsTransportImpl : Disposable, MlsTransport {
    public actual constructor(noPointer: NoPointer)
    actual override fun destroy(): Unit = Unit
    actual override fun close(): Unit = Unit
    public actual override suspend fun sendCommitBundle(commitBundle: CommitBundle): MlsTransportResponse {
        throw CoreCryptoException.Other("MlsTransportImpl is not used on Kotlin/JS; pass MlsTransport directly")
    }
    public actual override suspend fun sendMessage(mlsMessage: ByteArray): MlsTransportResponse {
        throw CoreCryptoException.Other("MlsTransportImpl is not used on Kotlin/JS; pass MlsTransport directly")
    }
    public actual override suspend fun prepareForTransport(historySecret: HistorySecret): MlsTransportData {
        throw CoreCryptoException.Other("MlsTransportImpl is not used on Kotlin/JS; pass MlsTransport directly")
    }
    public actual companion object
}

public actual fun buildMetadata(): BuildMetadata {
    val metadata = JsCoreCryptoModule.buildMetadata()
    return BuildMetadata(
        timestamp = metadata.timestamp,
        cargoDebug = metadata.cargoDebug,
        cargoFeatures = metadata.cargoFeatures,
        optLevel = metadata.optLevel,
        targetTriple = metadata.targetTriple,
        gitBranch = metadata.gitBranch,
        gitDescribe = metadata.gitDescribe,
        gitSha = metadata.gitSha,
        gitDirty = metadata.gitDirty,
    )
}

public actual fun ciphersuiteDefault(): Ciphersuite = ciphersuiteFromJs(JsCoreCryptoModule.ciphersuiteDefault().toInt())
public actual fun ciphersuiteFromU16(discriminant: UShort): Ciphersuite = ciphersuiteFromJs(JsCoreCryptoModule.ciphersuiteFromU16(discriminant.toShort()).toInt())
public actual suspend fun coreCryptoDeferredInit(path: String, key: DatabaseKey, entropySeed: ByteArray?): CoreCrypto {
    ensureWasmInitialized()
    val params = (js("({})") as JsCoreCryptoDeferredParams).also {
        it.databaseName = path
        it.key = key.requireJs()
        it.entropySeed = entropySeed?.toUint8Array()
    }
    return CoreCrypto(webCall { jsCoreCryptoDeferredInit(params) })
}

public actual suspend fun coreCryptoHistoryClient(historySecret: HistorySecret): CoreCrypto {
    ensureWasmInitialized()
    return CoreCrypto(webCall { jsCoreCryptoHistoryClient(historySecretToJs(historySecret)) })
}

public actual suspend fun coreCryptoNew(path: String, key: DatabaseKey, clientId: ClientId, ciphersuites: List<Ciphersuite>, entropySeed: ByteArray?, nbKeyPackage: UInt?): CoreCrypto {
    ensureWasmInitialized()
    val params = (js("({})") as JsCoreCryptoParams).also {
        it.databaseName = path
        it.key = key.requireJs()
        it.clientId = clientId.requireJs()
        it.ciphersuites = ciphersuites.map(::ciphersuiteToJs).toTypedArray()
        it.entropySeed = entropySeed?.toUint8Array()
        it.nbKeyPackage = nbKeyPackage?.toInt()
    }
    return CoreCrypto(webCall { jsCoreCryptoInit(params) })
}

public actual suspend fun exportDatabaseCopy(database: Database, destinationPath: String) {
    throw CoreCryptoException.Other("exportDatabaseCopy is not available in the JS runtime")
}

public actual suspend fun migrateDatabaseKeyTypeToBytes(path: String, oldKey: String, newKey: DatabaseKey) {
    webCall { JsCoreCryptoModule.migrateDatabaseKeyTypeToBytes(path, oldKey, newKey.requireJs()) }
}

public actual suspend fun openDatabase(name: String, key: DatabaseKey): Database = Database(webCall { JsCoreCryptoModule.openDatabase(name, key.requireJs()) })
public actual fun setLogger(logger: CoreCryptoLogger, level: CoreCryptoLogLevel) {
    JsCoreCryptoModule.setLogger(coreCryptoLoggerToJs(logger))
    JsCoreCryptoModule.setMaxLogLevel(logLevelToJs(level))
}

public actual fun setLoggerOnly(logger: CoreCryptoLogger) {
    JsCoreCryptoModule.setLogger(coreCryptoLoggerToJs(logger))
}

public actual fun setMaxLogLevel(level: CoreCryptoLogLevel) {
    JsCoreCryptoModule.setMaxLogLevel(logLevelToJs(level))
}

public actual suspend fun updateDatabaseKey(name: String, oldKey: DatabaseKey, newKey: DatabaseKey) {
    webCall { JsCoreCryptoModule.updateDatabaseKey(name, oldKey.requireJs(), newKey.requireJs()) }
}

public actual fun version(): String = JsCoreCryptoModule.version()
