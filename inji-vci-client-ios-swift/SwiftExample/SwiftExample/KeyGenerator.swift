
import Security
import Foundation

func generateKeyPair() -> (publicKeyPEM: String, privateKeyPEM: String)? {
    var publicKey, privateKey: SecKey?
    
    let parameters: [String: Any] = [
        kSecAttrKeyType as String: kSecAttrKeyTypeRSA,
        kSecAttrKeySizeInBits as String: 2048
    ]
    guard SecKeyGeneratePair(parameters as CFDictionary, &publicKey, &privateKey) == errSecSuccess else {
        print("Failed to generate key pair")
        return nil
    }
    
    guard let publicKeyData = SecKeyCopyExternalRepresentation(publicKey!, nil) as Data?,
          let privateKeyData = SecKeyCopyExternalRepresentation(privateKey!, nil) as Data? else {
        print("Failed to retrieve key data")
        return nil
    }
    
    let publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" + publicKeyData.base64EncodedString() + "\n-----END PUBLIC KEY-----"
    let privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" + privateKeyData.base64EncodedString() + "\n-----END PRIVATE KEY-----"
    
    return (publicKeyPEM, privateKeyPEM)
}
