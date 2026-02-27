#!/bin/bash
# Script to generate RSA key pair for JWT signing

echo "Generating RSA Key Pair for JWT..."

# Create keys directory if not exists
if [ ! -d "keys" ]; then
    mkdir keys
    echo "Created 'keys' directory"
fi

# Generate private key (2048 bits)
echo "Generating private key..."
openssl genrsa -out keys/private_key.pem 2048

# Generate public key from private key
echo "Generating public key..."
openssl rsa -in keys/private_key.pem -pubout -out keys/public_key.pem

# Convert to PKCS8 format (recommended for Java)
echo "Converting to PKCS8 format..."
openssl pkcs8 -topk8 -inform PEM -outform PEM -in keys/private_key.pem -out keys/private_key_pkcs8.pem -nocrypt

echo ""
echo "Keys generated successfully!"
echo "Private Key: keys/private_key_pkcs8.pem"
echo "Public Key:  keys/public_key.pem"

echo ""
echo "IMPORTANT: Add 'keys/' to .gitignore to prevent committing private keys!"
echo ""
echo "Updating .gitignore..."

# Add keys/ to .gitignore if not already there
if [ -f ".gitignore" ]; then
    if ! grep -q "keys/" .gitignore; then
        echo "" >> .gitignore
        echo "### RSA Keys ###" >> .gitignore
        echo "keys/" >> .gitignore
        echo "*.pem" >> .gitignore
        echo ".gitignore updated"
    else
        echo "keys/ already in .gitignore"
    fi
fi

echo ""
echo "Done! Update your .env file:"
echo "JWT_PRIVATE_KEY_PATH=keys/private_key_pkcs8.pem"
echo "JWT_PUBLIC_KEY_PATH=keys/public_key.pem"
