import Foundation

extension String? {
    func isBlank() -> Bool {
        return self == nil || self!.replacingOccurrences(of: " ", with: "").count == 0
    }
}
