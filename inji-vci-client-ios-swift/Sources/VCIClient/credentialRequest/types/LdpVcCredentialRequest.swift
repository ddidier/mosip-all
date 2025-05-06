import Foundation

class LdpVcCredentialRequest: CredentialRequestProtocol{
let accessToken: String
let issuerMetaData: IssuerMeta
let proof: JWTProof
    required init(accessToken: String, issuerMetaData: IssuerMeta, proof: JWTProof) {
        self.accessToken = accessToken
        self.issuerMetaData = issuerMetaData
        self.proof = proof
    }
    
    func validateIssuerMetadata() -> ValidatorResult {
        let validatorResult =  ValidatorResult()
        if(self.issuerMetaData.credentialType == nil || self.issuerMetaData.credentialType?.count == 0){
            validatorResult.addInvalidField("credentialType")
            
        }
        return validatorResult
        
    }
    
    
    func constructRequest() throws -> URLRequest{
        var request = URLRequest(url: URL(string: self.issuerMetaData.credentialEndpoint)!)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
        request.timeoutInterval = TimeInterval(self.issuerMetaData.downloadTimeoutInMilliseconds / 1000)
        
        guard let requestBody = try generateRequestBody(proofJWT: self.proof, issuer: self.issuerMetaData) else {
            throw DownloadFailedError.requestGenerationFailed
        }

        request.httpBody = requestBody

        return request
    }
    
    func generateRequestBody(proofJWT: JWTProof, issuer: IssuerMeta) throws -> Data? {
        let credentialDefinition = CredentialDefinition(type: issuer.credentialType!)

        let credentialRequestBody = CredentialRequestBody(
            format: issuer.credentialFormat,
            credential_definition: credentialDefinition,
            proof: proofJWT
        )
        
        do {
            let jsonData = try JSONEncoder().encode(credentialRequestBody)
            return jsonData
        } catch {
            throw DownloadFailedError.requestBodyEncodingFailed
        }
    }
}
