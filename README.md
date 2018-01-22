# Marvel Characters Android

This will be the readme file for this Android Application

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

This will be your configuration for the app regarding the Marvel API base url, your public API key and your private API key. This two keys are needed to do requests to the Marvel API. The cache size configurations will help you to setup the cache size for the API responses in your device. 
