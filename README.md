# Spring Boot graceful shutdown support
A feature still missing (since ever), this "draft" implementation is based on comments from 
[issue #4657](https://github.com/spring-projects/spring-boot/issues/4657). Until the "official" solution arrives, 
feel free to test-drive and contribute.  

### What is graceful shutdown?
Is a way to don't upset your clients when you need to update your cluster. When the shutdown signal comes, you expect 
the instance to:
1. reject any new requests attempts (status 503), and
2. complete all pending requests (all requests that came before the shutdown signal).
  
### Installation

This library has no transitive dependencies and assumes that you've already added all the desired spring-boot-starters 
dependencies.

If you're using Spring Boot **2.1**, add
```
<dependency>
  <groupId>com.github.erdanielli</groupId>
  <artifactId>spring-boot-graceful-shutdown</artifactId>
  <version>2.1.1</version>
</dependency>
```
**OR**

for Spring Boot **2.0** projects, add
```
<dependency>
  <groupId>com.github.erdanielli</groupId>
  <artifactId>spring-boot-graceful-shutdown</artifactId>
  <version>2.0.0</version>
</dependency>
```
**OR**

for the ancient Spring Boot **1.5**, add
```
<dependency>
  <groupId>com.github.erdanielli</groupId>
  <artifactId>spring-boot-graceful-shutdown</artifactId>
  <version>1.5.0</version>
</dependency>
```

**AND YOU'RE DONE!**

<sub>... unless you don't enjoy auto-configuration and prefer importing configurations by yourself, 
import `com.github.erdanielli.boot.shutdown.undertow.UndertowGracefulShutdownConfiguration` **or** 
`com.github.erdanielli.boot.shutdown.tomcat.TomcatGracefulShutdownConfiguration`</sub>

### Custom configurations

```
# Max duration to wait for pending requests to complete
# Duration values are only supported by Spring Boot 2.1.
# Previous versions must provide the value in milliseconds (30000)
server.shutdown-timeout=30s    
```

To enable/disable logging, fine-tune the logger `com.github.erdanielli.boot.shutdown.GracefulShutdown`

### Limitations

1. Only servlet-based runtime is supported;
2. Undertow is preferred, Tomcat is also supported (see _Caveats_ below);
3. No Jetty support yet.

### Caveats

- Tomcat doesn't reject new requests with 503 yet. Instead, a 'connection reset by peer' error is returned only after 
the shutdown completes.

### Further updates

Depends on the progress of [issue #4657](https://github.com/spring-projects/spring-boot/issues/4657).  

### Acknowledgements

- All contributions on [issue #4657](https://github.com/spring-projects/spring-boot/issues/4657)
- Mohammad Nadeem's for his [great DZone article](https://dzone.com/articles/publish-your-artifacts-to-maven-central) that
helped me putting this on Maven Central.
