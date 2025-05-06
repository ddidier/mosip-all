import UIKit
import AppAuth

class AuthHandler {
    static func authenticateWithIssuer(viewController: UIViewController) {
        
        let configuration = OIDServiceConfiguration(authorizationEndpoint: authorizationEndpoint,
                                                    tokenEndpoint: tokenEndpoint)
        
        let request = OIDAuthorizationRequest(configuration: configuration,
                                              clientId: clientID,
                                              scopes: scopes,
                                              redirectURL: redirectURI,
                                              responseType: OIDResponseTypeCode,
                                              additionalParameters: additionalParameters)
    
        
        print("Initiating authorization request with scope: \(request.scope ?? "nil")")
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        
        appDelegate.currentAuthorizationFlow = OIDAuthState.authState(byPresenting: request, presenting: viewController) { authState, error in
            if let authState = authState {
                (viewController as? ViewController)?.setAuthState(authState)
                print("Got authorization tokens. Access token: " +
                        "\(authState.lastTokenResponse?.accessToken ?? "nil")")
            } else {
                print("Authorization error: \(error?.localizedDescription ?? "Unknown error")")
                (viewController as? ViewController)?.setAuthState(nil)
            }
        }
    }
}


