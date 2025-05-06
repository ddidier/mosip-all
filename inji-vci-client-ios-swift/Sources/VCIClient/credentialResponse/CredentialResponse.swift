import Foundation

public protocol CredentialResponse{
    func toJSONString() throws -> String
}
