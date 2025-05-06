import Foundation
import VCIClient

// Authorization constants
let clientID = ""
let redirectURI = URL(string: "")!
let authorizationEndpoint = URL(string: "")!
let tokenEndpoint = URL(string: "")!
let scopes = [""]
let additionalParameters = ["":""]


// IssuerMeta constant
let issuerCredentialAudience = ""
let issuerCredentialEndPoint = ""
let issuerDownloadTimeout = 30000
let issuerCredentialType = [""]

// issuer Metadata
let issuer = IssuerMeta(credentialAudience: issuerCredentialAudience, credentialEndpoint: issuerCredentialEndPoint, downloadTimeoutInMilliseconds: issuerDownloadTimeout, credentialType: issuerCredentialType , credentialFormat: CredentialFormat.ldp_vc)


