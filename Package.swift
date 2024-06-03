// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "Shared",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "Shared",
            targets: ["Shared"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            url: "https://github.com/bevanchristian/multiplatformLibraryKMM/raw/main/Shared.xcframework.zip",
            checksum:"296d4aadd74b96b2bbdbf761cdedde4a4e2ffaacb32246eedb3ee71589342cd1"
            )

    ]
)
