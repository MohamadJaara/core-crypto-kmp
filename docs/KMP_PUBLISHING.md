# KMP Publishing Guide

This guide explains how to set up Maven Central publishing for the KMP (Kotlin Multiplatform) bindings in this fork.

## Overview

The KMP module publishes to Maven Central with the following coordinates:
- **Group**: `io.github.mohamadjaara`
- **Artifact**: `core-crypto-kmp`
- **Version**: Defined in `crypto-ffi/bindings/kmp/gradle.properties`

## Prerequisites

- GitHub account (owner of this fork)
- GPG key for signing artifacts
- Sonatype account for Maven Central

---

## 1. Create Sonatype Account & Namespace

### Register on Maven Central

1. Go to https://central.sonatype.com/
2. Click **Sign In** and choose **Sign in with GitHub** (recommended for `io.github.*` namespaces)
3. Your `io.github.mohamadjaara` namespace should be **automatically verified** when you sign in with your GitHub account

### Verify Namespace (if not auto-verified)

If the namespace is not automatically verified:

1. Go to **Namespaces** in the Sonatype portal
2. Click **Add Namespace**
3. Enter `io.github.mohamadjaara`
4. Follow the verification instructions (usually involves creating a temporary public repository)

---

## 2. Generate Sonatype Token

The publishing workflow uses a token (not your password) for authentication.

1. Go to https://central.sonatype.com/account
2. Click **Generate User Token**
3. **Save both values** - you will need them for GitHub secrets:
   - **Username**: Token username (not your login username)
   - **Password**: Token password

> **Important**: You can only see the token once. If you lose it, generate a new one.

---

## 3. Generate GPG Signing Key

Maven Central requires all artifacts to be signed with GPG.

### Generate a New Key

```bash
gpg --full-generate-key
```

When prompted, select:
- **Kind of key**: `(1) RSA and RSA`
- **Keysize**: `4096`
- **Expiration**: `0` (does not expire) or your preference
- **Real name**: `Mohamad Jaara`
- **Email**: Your email address
- **Passphrase**: Create a strong passphrase and **save it securely**

### Get Your Key ID

```bash
gpg --list-secret-keys --keyid-format=long
```

Output example:
```
sec   rsa4096/ABCD1234EFGH5678 2024-01-01 [SC]
      FULL40CHARACTERFINGERPRINT1234567890ABCDEF
uid                 [ultimate] Mohamad Jaara <email@example.com>
ssb   rsa4096/WXYZ9876STUV5432 2024-01-01 [E]
```

Your **Key ID** is the part after `rsa4096/`: `ABCD1234EFGH5678`

### Export the Private Key

```bash
gpg --armor --export-secret-keys ABCD1234EFGH5678
```

This outputs the full private key block:
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

lQdGBF5...
...
-----END PGP PRIVATE KEY BLOCK-----
```

**Copy the entire output** including the `-----BEGIN` and `-----END` lines.

### Publish Public Key to Keyservers

Maven Central verifies signatures against public keyservers. Upload your public key:

```bash
# Upload to multiple keyservers for redundancy
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234EFGH5678
gpg --keyserver keys.openpgp.org --send-keys ABCD1234EFGH5678
```

> **Note**: It may take 10-15 minutes for keys to propagate across keyservers.

### Verify Key Upload

```bash
gpg --keyserver keyserver.ubuntu.com --search-keys your-email@example.com
```

---

## 4. Add Secrets to GitHub

1. Go to your fork's settings: https://github.com/MohamadJaara/core-crypto/settings/secrets/actions
2. Click **New repository secret**
3. Add each of the following secrets:

| Secret Name | Value | Description |
|-------------|-------|-------------|
| `SONATYPE_USERNAME` | Token username from step 2 | Sonatype token username |
| `SONATYPE_PASSWORD` | Token password from step 2 | Sonatype token password |
| `PGP_KEY_ID` | `ABCD1234EFGH5678` | Your GPG key ID (16 characters) |
| `PGP_SIGNING_KEY` | Full private key block | Include `-----BEGIN...` and `-----END...` |
| `PGP_PASSPHRASE` | Your GPG passphrase | The passphrase you set during key generation |

### Adding the PGP Signing Key

When adding `PGP_SIGNING_KEY`:
1. Click **New repository secret**
2. Name: `PGP_SIGNING_KEY`
3. Value: Paste the **entire** output from `gpg --armor --export-secret-keys`, including:
   ```
   -----BEGIN PGP PRIVATE KEY BLOCK-----

   [... key content ...]

   -----END PGP PRIVATE KEY BLOCK-----
   ```
4. Click **Add secret**

---

## 5. Publishing

### Automatic Publishing (on tag)

Create and push a signed tag:

```bash
# Create a signed tag
git tag -s v9.1.3 -m "Release 9.1.3"

# Push the tag
git push origin v9.1.3
```

The workflow will automatically trigger and publish to Maven Central.

### Manual Publishing

1. Go to **Actions** tab in your repository
2. Select **publish kmp** workflow
3. Click **Run workflow**
4. Optionally check **Dry run** to test without publishing
5. Click **Run workflow**

### Dry Run

To test the build without publishing:
1. Trigger the workflow manually
2. Check the **Dry run** checkbox
3. This will build all artifacts but skip the publish step

---

## 6. Verify Publication

After publishing:

1. Go to https://central.sonatype.com/
2. Search for `io.github.mohamadjaara`
3. Your artifact should appear within a few minutes

### Using the Published Artifact

Add to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.mohamadjaara:core-crypto-kmp:9.1.3")
}
```

---

## Troubleshooting

### "Namespace not verified"

- Sign out and sign back in to Sonatype with GitHub
- Or manually verify by creating a temporary repository named after the verification code

### "Invalid signature" or "Signature verification failed"

- Ensure the **entire** private key block is in `PGP_SIGNING_KEY` (including BEGIN/END lines)
- Verify the key ID matches: `gpg --list-secret-keys --keyid-format=long`
- Check that the passphrase is correct

### "Key not found on keyserver"

- Wait 10-15 minutes after uploading
- Try uploading to additional keyservers:
  ```bash
  gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
  ```

### Build timeout on GitHub Actions

- The `macos-14` runner has limited free minutes
- Consider using a self-hosted runner for frequent builds
- Reduce targets if needed (e.g., skip iOS simulator)

### "401 Unauthorized" during publish

- Regenerate your Sonatype token
- Update `SONATYPE_USERNAME` and `SONATYPE_PASSWORD` secrets
- Ensure the token has publish permissions

---

## Quick Reference

### Useful GPG Commands

```bash
# List all secret keys
gpg --list-secret-keys --keyid-format=long

# Export public key (for sharing)
gpg --armor --export YOUR_KEY_ID

# Export private key (for backup)
gpg --armor --export-secret-keys YOUR_KEY_ID > private-key-backup.asc

# Delete a key (if needed)
gpg --delete-secret-keys YOUR_KEY_ID
gpg --delete-keys YOUR_KEY_ID

# Import a key from backup
gpg --import private-key-backup.asc
```

### Workflow File Location

The publishing workflow is defined in:
```
.github/workflows/publish-kmp.yml
```

### Version Configuration

Update the version in:
```
crypto-ffi/bindings/kmp/gradle.properties
```

Change `VERSION_NAME`:
```properties
VERSION_NAME=9.1.4
```

---

## Security Best Practices

1. **Never commit secrets** - Use GitHub Secrets only
2. **Backup your GPG key** - Store `private-key-backup.asc` securely offline
3. **Use a strong passphrase** - For your GPG key
4. **Rotate tokens periodically** - Regenerate Sonatype tokens every few months
5. **Limit secret access** - Only repository admins should manage secrets
