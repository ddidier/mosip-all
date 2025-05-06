import UIKit
import VCIClient
import AppAuth

class ViewController: UIViewController {
    var myAccessToken: String = ""
    
    @IBAction func getKeys(_ sender: UIButton) {
                
        if let keyPair = generateKeyPair() {
            print("Public Key:\n\(keyPair.publicKeyPEM)")
            print("\nPrivate Key:\n\(keyPair.privateKeyPEM)")
        }
    }
    
    @IBAction func getAccessTokenFromIssuer(_ sender: UIButton) {
        authenticateWithIssuer()
    }
    
    @IBAction func getCredentialFromLib(_ sender: UIButton) {
        Task {
            do {
                
                let proof = JWTProof(jwt: "eyJh.fghjk.poiuyt")

                if let requestCredential = try await VCIClient(traceabilityId: "SwiftApp").requestCredential(issuerMeta: issuer, proof: proof, accessToken: accessToken){
                        showAlertMessage(title: "Success..!!", message: "Credentials have been downloaded")
                        print("VC downloaded ", requestCredential)
                }
            } catch {
                showAlertMessage(title: "Error", message: error.localizedDescription)
                print("An error occurred:", error.localizedDescription)
            }
        }
    }


    @objc func authenticateWithIssuer() {
        AuthHandler.authenticateWithIssuer(viewController: self)
    }

    func setAuthState(_ authState: OIDAuthState?) {
        myAccessToken = authState?.lastTokenResponse?.accessToken ?? "nil"
    }
}

