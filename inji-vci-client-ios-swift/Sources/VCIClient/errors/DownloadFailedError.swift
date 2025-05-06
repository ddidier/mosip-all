import Foundation

enum DownloadFailedError: Error {
    case invalidURL
    case invalidDataProvidedException(invalidFields: String)
    case requestGenerationFailed
    case httpError(statusCode: Int, description: String)
    case requestBodyEncodingFailed
    case noResponse
    case decodingResponseFailed
    case encodingResponseFailed
    case customError(description: String)
}
