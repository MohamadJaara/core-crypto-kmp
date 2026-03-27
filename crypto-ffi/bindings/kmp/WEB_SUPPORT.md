# Web Support Status (`core-crypto-kmp`)

This document defines the current web support level for `core-crypto-kmp`.

## Current Stage

- `js`: functional through an npm-backed adapter using `@wireapp/core-crypto`
- `wasmJs`: not implemented yet

The existing public Kotlin API `com.wire.crypto.*` remains the consumer contract.

## Kotlin/JS Status

Supported on `js`:

- Real implementation of `com.wire.crypto` backed by `@wireapp/core-crypto@9.3.3`
- Internal WASM bootstrap via `initWasmModule(...)`
- Compile support for consumers using the existing KMP API
- Published KMP JS artifacts
- Local browser smoke/contract tests in this repository covering:
  - `CoreCrypto.init(...)`
  - `clientPublicKey(...)`
  - `randomBytes(...)`
  - `transaction { ... }` data persistence and error propagation
  - `conversationExists(...)` and `createConversation(...)`
  - `version()`, `buildMetadata()`, `setLogger(...)`, `setMaxLogLevel(...)` after runtime init
- Verified consumer integration in Kalium:
  - `:core:cryptography:compileKotlinJs`
  - `:logic:compileKotlinJs`
- Initial web runtime bootstrap confirmed by the consumer side

Important note:

- The `js` backend is implemented by replacing UniFFI-generated web stubs with a repository-owned web adapter during the build.
- This is not Gobley/UniFFI native JS support. It is a Kotlin/JS adapter over the existing npm runtime.
- `build.gradle.kts` intentionally wires this adapter only into the `js` task flow. `wasmJs` is left out on purpose until a separate Kotlin/Wasm-compatible implementation exists.

## Kotlin/JS Known Limitations

- `exportDatabaseCopy(...)` is still unsupported on JS and throws a clear `CoreCryptoException.Other`
- Browser runtime depends on the local webpack override in:
  - `webpack.config.d/corecrypto-wasm.js`
  to resolve the npm package WASM asset in `jsBrowserTest`

This means:

- JS compile, local browser smoke, and consumer integration are unblocked
- The current JS setup is validated for the key adapter/runtime contract paths, not full feature parity across all browser scenarios

## Kotlin/Wasm Status

`wasmJs` is still unsupported.

Current blocker:

- The new web adapter is written against Kotlin/JS interop and does not compile under Kotlin/Wasm.

Concretely, the current implementation uses JS interop patterns that Kotlin/Wasm rejects, including:

- `dynamic`
- `js("...")` helpers in multiple places
- current `Promise` and callback interop shapes
- external declarations shaped for Kotlin/JS rather than Kotlin/Wasm
- JS module annotations and types that are not accepted by Kotlin/Wasm in the same form

## What Needs To Be Done For `wasmJs`

The recommended path is a separate `wasmJs` implementation rather than trying to force one shared adapter for both targets.

Required work:

- Split the current web implementation into:
  - `js` adapter
  - `wasmJs` adapter
- Rewrite externals to Kotlin/Wasm-compatible JS interop
- Remove `dynamic`
- Replace ad-hoc `js("...")` usage with Kotlin/Wasm-compatible wrappers
- Rework callback and `Promise` interop for Kotlin/Wasm
- Re-test WASM bootstrap and asset loading in browser runtime

## Verification Targets

Currently verified:

- `./gradlew :crypto-ffi:bindings:kmp:compileKotlinJs`
- `./gradlew :crypto-ffi:bindings:kmp:compileTestKotlinJs`
- `./gradlew :crypto-ffi:bindings:kmp:jsBrowserTest --tests "com.wire.crypto.CoreCryptoJsSmokeTest"`
- `./gradlew :crypto-ffi:bindings:kmp:jsJar`
- `./gradlew :crypto-ffi:bindings:kmp:publishKotlinMultiplatformPublicationToMavenLocal`
- `./gradlew :crypto-ffi:bindings:kmp:publishJsPublicationToMavenLocal`

Project-level wrappers:

- `make kmp-js`
