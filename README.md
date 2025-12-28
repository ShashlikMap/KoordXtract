# KoordXtract
A small KMP utility library that helps to resolve shared links from map apps(with focus on GoogleMaps and Apple Maps) 
to latitude/longitude coordinates without relying on any Google API(e.g. Places) or any other APIs.

Initial goals of the library is to use it in [shashlik-map](https://github.com/ShashlikMap/shashlik-map) project.

Android demo application can receive a shared the link from GoogleMaps or it can launch an internal test that resolves some random links:
```
D  link: https://maps.app.goo.gl/wPn6i3inrCDNFD7r8, result: Either.Right((108.0465532, 12.7109931))
D  link: https://maps.app.goo.gl/SrjpRwuVfhqyKc5HA, result: Either.Right((139.6412587, 35.831881))
D  link: https://maps.app.goo.gl/HSUQp6K9L8z5efAY7, result: Either.Right((139.6641148, 35.791951600000004))
D  link: https://maps.app.goo.gl/nWx4HL35gqPhHrif8, result: Either.Right((-118.13840819999999, 33.8114365))
D  link: https://maps.app.goo.gl/tkQ8rmvUKTxnstZM8, result: Either.Right((72.8920559, 19.0483874))
D  link: https://maps.app.goo.gl/VWAUD4NbM45PDze36, result: Either.Right((-106.668247, 35.097595))
D  link: https://maps.app.goo.gl/7C7nDGQ15kKD3vCv5, result: Either.Right((-3.6945569, 40.4079123))
D  link: https://maps.app.goo.gl/r6AfdfewurGHRiLe7, result: Either.Right((42.2736908, 18.2124262))
```
## How to use
1. Add dependency to the version catalog
```gradle
[versions]
koordxtract = "0.4.0"

[libraries]
koordxtract = { group = "io.github.shashlikmap", name = "koordxtract", version.ref = "koordxtract" }

implementation(libs.koordxtract)
```
2. KMP usage example
```kotlin
val xTractor = LatLonExtractor()
lifecycleScope.launch {
    val result = xTractor.extractFromStringData("https://maps.app.goo.gl/SrjpRwuVfhqyKc5HA")
    // use result
}
```
Or use a convienient extension for `Intent` in case of pure Android
```kotlin
xTractor.extractFromIntent(...)
```

## Roadmap
- [ ] Configure CI + more unit tests

