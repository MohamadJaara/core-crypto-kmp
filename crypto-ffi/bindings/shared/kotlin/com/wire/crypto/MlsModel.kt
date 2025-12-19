package com.wire.crypto

/** The default ciphersuite: `MLS_128_DHKEMX25519_AES128GCM_SHA256_ED25519` */
public val CIPHERSUITE_DEFAULT: Ciphersuite = Ciphersuite.MLS_128_DHKEMX25519_AES128GCM_SHA256_ED25519

/** The default set of ciphersuites: `[MLS_128_DHKEMX25519_AES128GCM_SHA256_ED25519]` */
public val CIPHERSUITES_DEFAULT: List<Ciphersuite> = listOf(CIPHERSUITE_DEFAULT)

/** Default credential type */
public val CREDENTIAL_TYPE_DEFAULT: CredentialType = CredentialType.BASIC

/** Default Custom Configuration */
public val CUSTOM_CONFIGURATION_DEFAULT: CustomConfiguration = CustomConfiguration(null, null)

/** Default Conversation Configuration */
public val CONVERSATION_CONFIGURATION_DEFAULT: ConversationConfiguration =
    ConversationConfiguration(null, listOf(), CUSTOM_CONFIGURATION_DEFAULT)

/** Construct a client ID from a string */
public fun String.toClientId(): ClientId = ClientId(this.encodeToByteArray())

/** Construct an external sender key from bytes */
public fun ByteArray.toExternalSenderKey(): ExternalSenderKey = ExternalSenderKey(this)

/** Construct a Welcome from bytes */
public fun ByteArray.toWelcome(): Welcome = Welcome(this)

/** Construct a KeyPackage from bytes */
public fun ByteArray.toMLSKeyPackage(): KeyPackage = KeyPackage(this)

/** Construct an AVS secret from bytes */
public fun ByteArray.toAvsSecret(): SecretKey = SecretKey(this)

/** Construct a GroupInfo from bytes */
public fun ByteArray.toGroupInfo(): GroupInfo = GroupInfo(this)

/** Construct a ConversationId from bytes */
public fun ByteArray.toConversationId(): ConversationId = ConversationId(this)
