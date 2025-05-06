import XCTest
@testable import VCIClient

class LdpVcCredentialRequestTests: XCTestCase {
    
    var credentialRequest: LdpVcCredentialRequest!
    let url = URL(string: "https://domain.net/credential")!
    let accessToken = "AccessToken"
    let issuer = IssuerMeta(credentialAudience: "https://domain.net",
                            credentialEndpoint: "https://domain.net/credential",
                            downloadTimeoutInMilliseconds: 20000,
                            credentialType: ["VerifiableCredential"],
                            credentialFormat: .ldp_vc)
    let proofJWT = JWTProof(jwt: "ProofJWT")
    
    override func setUp() {
        super.setUp()
        credentialRequest = LdpVcCredentialRequest(accessToken: accessToken, issuerMetaData: issuer, proof: proofJWT)
    }
    
    override func tearDown() {
        credentialRequest = nil
        super.tearDown()
    }
    
    func testConstructRequestSuccess() {
        
        
        do {
            let request = try credentialRequest.constructRequest()
            XCTAssertEqual(request.url, url)
            XCTAssertEqual(request.httpMethod, "POST")
            XCTAssertEqual(request.allHTTPHeaderFields?["Content-Type"], "application/json")
            XCTAssertEqual(request.allHTTPHeaderFields?["Authorization"], "Bearer \(accessToken)")
            XCTAssertEqual(request.timeoutInterval, TimeInterval(issuer.downloadTimeoutInMilliseconds / 1000))
            XCTAssertNotNil(request.httpBody)
        } catch {
            XCTFail("Error: \(error.localizedDescription)")
        }
    }
    
    func testGenerateRequestBodySuccess() {
        let proofJWT = JWTProof(jwt: "xxxx.yyyy.zzzz")
        let issuer = IssuerMeta(credentialAudience: "https://domain.net",
                                credentialEndpoint: "https://domain.net/credential",
                                downloadTimeoutInMilliseconds: 20000,
                                credentialType: ["VerifiableCredential"],
                                credentialFormat: .ldp_vc)
        
        do {
            let jsonData = try credentialRequest.generateRequestBody(proofJWT: proofJWT, issuer: issuer)
            XCTAssertNotNil(jsonData)
        } catch {
            XCTFail("Error: \(error.localizedDescription)")
        }
    }

    func testCredentialDefinitionInitializationSuccess() {
        let credentialDefinition = CredentialDefinition(type: ["Type"])
        XCTAssertEqual(credentialDefinition.context, ["https://www.w3.org/2018/credentials/v1"])
        XCTAssertEqual(credentialDefinition.type, ["Type"])
    }
    
    func testshouldReturnValidatorResultWithIsValidAsTrueWhenRequiredIssuerMetadataDetailsOfLdpVcAreAvailable() {
        let validationResult = credentialRequest.validateIssuerMetadata()
        
        XCTAssertTrue(validationResult.isValid)
        XCTAssert(validationResult.invalidFields.count==0)
    }
    
    func testshouldReturnValidatorResultWithIsValidAsFalseWithInvalidFieldsWhenRequiredCredentialTypeIsNotAvailableInIssuerMetadata() {
        let issuerMetadataWithoutCredentialType = IssuerMeta(credentialAudience: "https://domain.net",
                                credentialEndpoint: "https://domain.net/credential",
                                downloadTimeoutInMilliseconds: 20000,
                                credentialFormat: .mso_mdoc)
        credentialRequest = LdpVcCredentialRequest(accessToken: accessToken, issuerMetaData: issuerMetadataWithoutCredentialType, proof: proofJWT)
        
        let validationResult = credentialRequest.validateIssuerMetadata()
        
        XCTAssertFalse(validationResult.isValid)
        XCTAssert(validationResult.invalidFields.elementsEqual(["credentialType"]))
    }
}
