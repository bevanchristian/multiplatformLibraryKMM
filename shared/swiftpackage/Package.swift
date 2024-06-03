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
            url: "https://github.com/bevanchristian/multiplatformLibraryKMM",
            checksum:"296d4aadd74b96b2bbdbf761cdedde4a4e2ffaacb32246eedb3ee71589342cd1"
            )
        ),
    ]
)
