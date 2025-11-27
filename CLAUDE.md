# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Wire CoreCrypto is a cross-platform cryptography library providing MLS (Messaging Layer Security) and Proteus protocol implementations. It wraps OpenMLS to provide an ergonomic API for WebAssembly and mobile platforms (iOS/Android) through FFI bindings.

## Build Commands

### Prerequisites
- Rust (via rustup)
- GNU Make 4.3+
- cargo-nextest: `cargo install --locked cargo-nextest`

### Common Build Targets
```bash
make jvm                    # Build JVM bindings (auto-selects darwin/linux)
make android                # Build Android bindings (requires ANDROID_NDK_HOME)
make ios-create-xcframework # Build iOS XCFramework (macOS only)
make ts                     # Build TypeScript/WASM bindings
make local                  # Build all bindings + format
```

Release builds: add `RELEASE=1` to any make command.

### Testing
```bash
cargo nextest run                          # Run all Rust tests
cargo nextest run --features test-all-cipher  # Test all ciphersuites (slow)
make jvm-test                              # Kotlin/JVM tests
make ts-test                               # TypeScript tests (Chrome via wdio)
make ts-test TEST=<name>                   # Run specific TS test
wasm-pack test --headless --chrome ./keystore  # Keystore WASM tests
```

### Formatting & Linting
```bash
make fmt           # Format all (Rust, Swift, Kotlin, TypeScript)
make check         # Lint all
make rust-fmt      # Rust only (uses nightly)
make rust-check    # Rust clippy + check (native + wasm32)
```

### Single Test
```bash
cargo nextest run <test_name>              # Run specific Rust test
cargo nextest run -p <crate_name>          # Run tests for specific crate
```

## Architecture

### Workspace Crates
- **crypto**: Core MLS/Proteus abstractions. Main entry point is `CoreCrypto` struct wrapping `Session` (MLS) and optionally `ProteusCentral`
- **keystore**: Encrypted persistent storage (SQLCipher on native, IndexedDB + AES256-GCM on WASM)
- **mls-provider**: Implements OpenMLS crypto provider trait, bridges RustCrypto and keystore
- **crypto-ffi**: FFI bindings via UniFFI (iOS/Android/JVM) and wasm-bindgen (WASM)
- **crypto-macros**: Procedural macros for the workspace

### Key Concepts
- **Session**: Entry point for MLS operations, owns the MLS client and conversations
- **Client**: Local device with ability to produce keying material
- **Conversation**: MLS group abstraction for messaging

### Platform Bindings
- iOS/Android: Generated via UniFFI from `crypto-ffi/uniffi.toml`
- TypeScript/WASM: Built with wasm-bindgen + uniffi-bindgen-react-native

### External Dependencies
- OpenMLS: Wire's fork at `github.com/wireapp/openmls`
- Proteus: Wire's implementation at `github.com/wireapp/proteus`
- rusty-jwt-tools: E2EI (end-to-end identity) support

## Git Workflow

- Use conventional commits (picked up by changelog generator)
- No merge commits - always rebase on main
- Sign commits and tags
- Include JIRA ticket IDs in format `[TICKET_ID]` when applicable