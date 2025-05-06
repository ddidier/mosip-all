import Foundation

public protocol CredentialRequestProtocol{
    init( accessToken: String,
           issuerMetaData: IssuerMeta,
          proof: JWTProof)
    
    func constructRequest() throws -> URLRequest
    
    func validateIssuerMetadata() -> ValidatorResult
}
