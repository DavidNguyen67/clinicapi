#!/usr/bin/env pwsh
# Script to generate RSA key pair for JWT signing

Write-Host "Generating RSA Key Pair for JWT..." -ForegroundColor Green

# Create keys directory if not exists
$keysDir = "keys"
if (-not (Test-Path $keysDir)) {
    New-Item -ItemType Directory -Path $keysDir | Out-Null
    Write-Host "Created 'keys' directory" -ForegroundColor Yellow
}

# Generate private key (2048 bits)
Write-Host "Generating private key..." -ForegroundColor Cyan
openssl genrsa -out "$keysDir/private_key.pem" 2048

# Generate public key from private key
Write-Host "Generating public key..." -ForegroundColor Cyan
openssl rsa -in "$keysDir/private_key.pem" -pubout -out "$keysDir/public_key.pem"

# Convert to PKCS8 format (recommended for Java)
Write-Host "Converting to PKCS8 format..." -ForegroundColor Cyan
openssl pkcs8 -topk8 -inform PEM -outform PEM -in "$keysDir/private_key.pem" -out "$keysDir/private_key_pkcs8.pem" -nocrypt

Write-Host "`nKeys generated successfully!" -ForegroundColor Green
Write-Host "Private Key: $keysDir/private_key_pkcs8.pem" -ForegroundColor Yellow
Write-Host "Public Key:  $keysDir/public_key.pem" -ForegroundColor Yellow

Write-Host "`nIMPORTANT: Add 'keys/' to .gitignore to prevent committing private keys!" -ForegroundColor Red
Write-Host "`nUpdating .gitignore..." -ForegroundColor Cyan

# Add keys/ to .gitignore if not already there
$gitignorePath = ".gitignore"
if (Test-Path $gitignorePath) {
    $content = Get-Content $gitignorePath -Raw
    if ($content -notmatch "keys/") {
        Add-Content $gitignorePath "`n### RSA Keys ###`nkeys/`n*.pem`n"
        Write-Host ".gitignore updated" -ForegroundColor Green
    } else {
        Write-Host "keys/ already in .gitignore" -ForegroundColor Yellow
    }
}

Write-Host "`nDone! Update your .env file:" -ForegroundColor Green
Write-Host "JWT_PRIVATE_KEY_PATH=keys/private_key_pkcs8.pem" -ForegroundColor Cyan
Write-Host "JWT_PUBLIC_KEY_PATH=keys/public_key.pem" -ForegroundColor Cyan
