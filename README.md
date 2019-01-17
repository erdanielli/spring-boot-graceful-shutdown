# spring-boot-graceful-shutdown
Provides graceful shutdown support for Spring Boot.
The implementation is based on comments from issue #4657 (https://github.com/spring-projects/spring-boot/issues/4657).
I hope the Spring Boot team will come to an "official" solution soon, but until then this project might do the job. 

### Limitations

1. Supports only servlet based web applications;
2. Undertow is preferred, but Tomcat is also supported;
3. No Jetty support yet.

### Installation

This library has no transitive dependencies and is a complement to an already functional environment. 
It assumes that you added all the desidered spring-boot-starters previously.

- If you're on Spring Boot 2.1
```
<dependency>
  <groupId>com.github.erdanielli</groupId>
  <artifactId>spring-boot-graceful-shutdown</artifactId>
  <version>2.1.0</version>
</dependency>
```
- If you're on Spring Boot 2.0
```
<dependency>
  <groupId>com.github.erdanielli</groupId>
  <artifactId>spring-boot-graceful-shutdown</artifactId>
  <version>2.0.0</version>
</dependency>
```

This library is auto-configured. If you prefer importing configurations manually,
them you'll need to import `com.github.erdanielli.boot.shutdown.undertow.UndertowGracefulShutdownConfiguration` 
or `com.github.erdanielli.boot.shutdown.tomcat.TomcatGracefulShutdownConfiguration`

### Custom configurations

```
# Max duration to wait for pending requests to complete
# Duration values are only supported on Spring Boot 2.1.
# On Spring Boot 2.0 you must provide the value in milliseconds.
server.shutdown-timeout=30s    
```

To enable/disable logging, fine-tune the logger `com.github.erdanielli.boot.shutdown.GracefulShutdown`

### Further updates

Further (relevant) comments on issue #4657 (https://github.com/spring-projects/spring-boot/issues/4657) 
may be integrated here.