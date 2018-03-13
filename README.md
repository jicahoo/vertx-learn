# vertx-learn
As title.

## Examples
* http://escoffier.me/vertx-hol/ 
* https://github.com/vert-x3/vertx-examples

## vertx-hol
* quote-generator:
  * GeneratorConfigVertile是主Verticle. 在启动的时候，负责部署另外两个Verticle: MarketDataVerticle 和 RestQuoteAPIVerticle.
  * 主Verticle是在pom.xml中配置的。相应信息会生成到JAR中META-INF/MANIFEST.MF中.
