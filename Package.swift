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
            checksum:"e3f08384ca91e7e61c7b5e142be380c3000b1d3fe930009c69d9b88630fa3fc3"
            )

    ]
)
