# Marvel Characters Android

Just an example application to list, search and view the details of Marvel characters, written in Kotlin. 
This application has Use Cases to help separating the Presentation layer and the Domain layer, without using any dependency injection framework but having most of the architecture ready for it, hence all entity dependencies are passed (injected?) by constructor parameters.

# How can I compile this app?

Just clone this repo and add a properties file called "marvelApp.properties" to the project root with the following configurations:
```
marvelApiUrl="URL_TO_MARVEL_API"
marvelApiPublicKey="YOUR_MARVEL_API_PUBLIC_KEY"
marvelApiPrivateKey="YOUR_API_PRIVATE_KEY"

# values in bytes
# 1MB
debugCacheSize=1000000
# 10MB
releaseCacheSize=10000000
```

This will be your configuration for the app regarding the Marvel API base URL, your public API key and your private API key. This two keys are needed to do requests to the Marvel API. The cache size configurations will help you to setup the cache size for the API responses in your device. 

# Technologies used

 * Kotlin
 * RxJava
 * Retrofit
 * OkHttp
 * Gson
 * Glide
 * ButterKnife
 * Timber
 * Stetho
 * Spek
 * Junit
 * Mockito
 * PaperParcel

