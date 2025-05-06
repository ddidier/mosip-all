import Foundation
@testable import VCIClient

class MockNetworkSession: NetworkSession {
    var data: Data?
    var response: URLResponse?
    var error: Error?
    
    func data(for request: URLRequest) async throws -> (Data, URLResponse) {
        if let error = error {
            throw error
        }
        guard let data = data, let response = response else {
            fatalError("MockNetworkSession not configured properly")
        }
        return (data, response)
    }
}
