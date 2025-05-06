import XCTest
@testable import VCIClient

class UtilTests: XCTestCase {
    
    func testGetLogTag_WithTraceabilityId() {
        let className = "TestClass"
        let traceabilityId = "123456"
        
        let logTag = Util.getLogTag(className: className, traceabilityId: traceabilityId)
        
        XCTAssertEqual(logTag, "INJI-VCI-Client : \(className) | traceID \(traceabilityId)")
    }
    
    func testGetLogTag_WithMultipleCallsAndDifferentTraceabilityId() {
        let className = "TestClass"
        let traceabilityId1 = "123456"
        let traceabilityId2 = "789012"
        
        let logTag1 = Util.getLogTag(className: className, traceabilityId: traceabilityId1)
        let logTag2 = Util.getLogTag(className: className, traceabilityId: traceabilityId2)
        
        XCTAssertEqual(logTag1, "INJI-VCI-Client : \(className) | traceID \(traceabilityId1)")
        XCTAssertEqual(logTag2, "INJI-VCI-Client : \(className) | traceID \(traceabilityId2)")
    }
}
