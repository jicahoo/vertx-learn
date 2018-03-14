# vertx-learn
As title.

## Examples
* http://escoffier.me/vertx-hol/ 
* https://github.com/vert-x3/vertx-examples

## vertx-hol
* 每个模块(除了common)打出的fat jar都是对等的，没有任何从属关系。都是可执行文件，都包含cluster.xml, Mail-Class都是common.Laucher, Main-Verticle各不相同。
* quote-generator:
  * GeneratorConfigVertile是主Verticle. 在启动的时候，负责部署另外两个Verticle: MarketDataVerticle 和 RestQuoteAPIVerticle.
  * 主Verticle是在pom.xml中配置的。相应信息会生成到JAR中META-INF/MANIFEST.MF中.
  * 执行入口也在MANIFEST.MF中指定`Main-Class: io.vertx.workshop.common.Launcher`
* trader-dashboard是主要使用了vertx web扩展的功能，webroot文件夹就是vertx web规定的。要注意的是vert是core加一堆扩展。http://vertx.io/docs/vertx-web/java/
