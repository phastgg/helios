Preparing better readme, this library contains currently scheduling api and more api is currently being developed. 
To use scheduling api, just type HeliosTask and then use any of the builders by what you need.

# Implementing in gradle
Use shadow plugin to shade.
```
maven {
    name = "phastReleases"
    url = uri("https://repo.phast.gg/releases")
}
```
```
implementation("gg.phast:helios:1.0-SNAPSHOT")
```

# Implementing in maven
Change scope to shade.
```
<repository>
  <id>phast-releases</id>
  <name>Phast Repository</name>
  <url>https://repo.phast.gg/releases</url>
</repository>
```
```
ependency>
  <groupId>gg.phast</groupId>
  <artifactId>helios</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
