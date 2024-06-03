// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "sharedBevanKmm",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "sharedBevanKmm",
            targets: ["sharedBevanKmm"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "sharedBevanKmm",
            path: "./sharedBevanKmm.xcframework"
        ),
    ]
)
