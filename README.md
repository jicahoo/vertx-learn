# vertx-learn
As title.

## Examples
* http://escoffier.me/vertx-hol/ 
  * My fork: https://github.com/jicahoo/vertx-microservices-workshop.git
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
  * RestQuoteAPIVertilce会从EventBus中消费地址为`market`的消息。消息源是三个MarketDataVerticle， MarketDataVerticle各自每隔一段时间会发布新的数据。
  * 主Verticle是在pom.xml中配置的。相应信息会生成到JAR中META-INF/MANIFEST.MF中.
  * 执行入口也在MANIFEST.MF中指定`Main-Class: io.vertx.workshop.common.Launcher`
* trader-dashboard是主要使用了vertx web扩展的功能，webroot文件夹就是vertx web规定的。要注意的是vert是core加一堆扩展。http://vertx.io/docs/vertx-web/java/
* audit-service.
* portfolio: （个人或机构的)投资组合，有价证券组合
  * 提供一个PortfolioService,  其他组件，可以发现和使用这个Service, 得到这个Service之后，就可以调用buy,sell，进行股票交易。进行股票交易的时候，PortfolioService也会将交易信息以消息的方式发布在EventBus上，消息地址是PortforlioServier.EVENT\_ADDRESS="portfolio".
  * TraderUtils类会调用PortfolioService进行股票交易
  * 在实现PortfolioServiceImpl的时候，实现computeEvaluation和getValueForCompany的时候，遇到一些问题。
    * 一是异常处理，每次塞正常逻辑的Handler了时，你也要随后塞一个异常处理的Handler。这也是异步环境下，异常处理的基本方法。开始的时候，我没有给bodyHandler加对应的exceptionHandler.
    * 二是你在拿Future结果的时候，不仅要检测是否完成，也要检测是否成功；不成功，你会得到Throable，你可以打印它，或则用其他方式处理。
    * 三是一定要注意异步执行的顺序。在下面的代码中，client.get的Handler肯定先执行，拿到HTTP状态码，bodyHandler的代码是随后在某个时间点执行的。所以，你在client.get的Handler里面拿Future的值，肯定是未完成的，结果也是null.
    ```java
        client.get("/name="+encode(company), resp -> {
      //Handle the exception when reading response.
      resp.exceptionHandler(future::fail);
      if (resp.statusCode() == 200) {
        System.out.println("200 OK: " + resp.statusCode());
        resp.bodyHandler(buff -> {
          JsonObject jsonObject = buff.toJsonObject();
          System.out.println(jsonObject);
          double bidVal = jsonObject.getDouble("bid");
          System.out.println("bid: " + bidVal);
          System.out.println(jsonObject);
          future.complete(numberOfShares * jsonObject.getDouble("bid"));
        }).exceptionHandler(future::fail);
      } else {
        System.out.println("Status Code: " + resp.statusCode());
        future.complete(0.0);
      }
      //Good question: should future.result() return null or correct double value?
      System.out.println("Jichao: Got bid for company: " + company + " - " + future.isComplete() +  " - " + future.result());
    }).exceptionHandler(future::fail).end(); //In call-back style, you have to register the exception handler.
    ```
* compulsive-traders: MainVerticle会部署另外三个Verticle实例，两个是Java版的交易者，一个是groovy版的交易者。
  * Java版的交易者, 会拿到两个依赖的服务：一个是PortfolioService, 一个是market-data服务对应的MessageConsumer,是为了接收MakketDataVertilce在地址"market"上发布的消息。都会通过异步的方式去拿到这两个依赖。如果任何一个依赖失败，就会通知该Verticle部署失败。如果依赖都成功获得，就会监听"market"地址上的消息，消息来了就尝试进行一次交易。

* Others:
  * publish有两个意义，有时候是发布消息到EventBus, 有的时候，是发布一个Service (MicroService, Service Discovery)
* 启动与测试
  * 如果只启动protfolio和trader-dashboard, 访问页面的时候，trader-dashboard会报错。要和quote-generator一起起来，可以比较正常的工作
