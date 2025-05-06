import Foundation

class MsoMdocVcCredentialRequest:CredentialRequestProtocol {
    
    
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
        if(self.issuerMetaData.docType.isBlank()){
            validatorResult.addInvalidField("docType")
        }
        if(self.issuerMetaData.claims==nil || self.issuerMetaData.claims?.values.count == 0){
            validatorResult.addInvalidField("claims")
        }
        return validatorResult
    }
    
    func constructRequest() throws -> URLRequest {
        
        var request = URLRequest(url: URL(string: self.issuerMetaData.credentialEndpoint)!)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
        request.timeoutInterval = TimeInterval(self.issuerMetaData.downloadTimeoutInMilliseconds / 1000)
        
        guard let requestBody = try generateRequestBody() else {
            throw DownloadFailedError.requestGenerationFailed
        }
        request.httpBody = requestBody
        
        return request
    }
    
    private func generateRequestBody() throws -> Data? {
        let logTag = Util.getLogTag(className: String(describing: type(of: self)))
        let credentialRequestBody = MsoMdocCredentialRequestBody(format: self.issuerMetaData.credentialFormat, doctype: self.issuerMetaData.docType!, claims: Util.convertToAnyCodable(dict: self.issuerMetaData.claims!), proof: self.proof as JWTProof
        )
        
        do {
            let jsonData = try JSONEncoder().encode(credentialRequestBody)
            return jsonData
        } catch let error {
            print(logTag,"Error occured while constructing request body \(error.localizedDescription)")
            throw DownloadFailedError.requestBodyEncodingFailed
        }
    }
    }

struct MsoMdocCredentialRequestBody: Encodable {
    let format: CredentialFormat
    let proof: JWTProof
    let doctype: String
    let claims: [String:AnyCodable]
    
    init(format: CredentialFormat, doctype: String,claims:[String:AnyCodable], proof: JWTProof) {
        self.format = format
        self.doctype = doctype
        self.proof = proof
        self.claims = claims
    }
}
