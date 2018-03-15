# vertx-learn
As title.

## Examples
* http://escoffier.me/vertx-hol/ 
* https://github.com/vert-x3/vertx-examples


## vertx-hol

* 每个模块(除了common)打出的fat jar都是对等的，没有任何从属关系。都是可执行文件，都包含cluster.xml, Main-Class都是common.Laucher, 只不过Main-Verticle各不相同。
* End to end case
  * GET /operations 会到trader-dashboard，trader-dashboard会转发给audit-service, trader-dashboard REST call to audit-service. audit-servive的职责就是访问audit数据库
  * /eventbus/\*不知道trader-dashboard如何处理的？不知道前端JS代码是如何和trader-dashboard通信的?
  * GET /discovery. 不清楚如何处理，vertx的microservice自带?
  * GET /\* 其他的都会识别为静态文件(HTML,JS)，在trader-dashboard中，由vertx-web提供的StaticHandler来处理。也是在trader-dashboard中注册的这个Handler.
* quote-generator:
  * GeneratorConfigVertile是主Verticle. 在启动的时候，负责部署另外两个类型的Verticle: MarketDataVerticle 和 RestQuoteAPIVerticle。MarketDataVerticle是和公司对应的，配置文件config.json中，共有3个公司，所以，会有三个MarketDataVerticle实例。
  * 127.0.0.1:35000访问的是RestQuoteAPIVerticle, 这个Verticle具有HTTP Endpoint.
  * 主Verticle是在pom.xml中配置的。相应信息会生成到JAR中META-INF/MANIFEST.MF中.
  * 执行入口也在MANIFEST.MF中指定`Main-Class: io.vertx.workshop.common.Launcher`
* trader-dashboard是主要使用了vertx web扩展的功能，webroot文件夹就是vertx web规定的。要注意的是vert是core加一堆扩展。http://vertx.io/docs/vertx-web/java/
* audit-service.
