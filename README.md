Overview
========
Introduces annotations that weaves log entering and/or log exiting statements into your code.  
Example:  
```java
    @LogAround(logResult = true)
    @GetMapping(path="products/test", produces = "application/json")
    @ApiOperation(value = "assdfasdf")
    public String test(@Lp String statement){
    	return statement;
    }
```
Having added the `@LogAround` annotation to the method (and given that the proper configuration is in place, see the class `LoggerConfig` further down in this readme) the following log statements will be created when calling above endpoint and passing the string `helloworld`: 

When the method has been entered:  
`c.g.h.l.Logger - <<< PDSController.test(String statement=helloworld)`  

Immediately before exiting the method:  
`c.g.h.l.Logger - >>> PDSController.test(String statement=helloworld): helloworld`

This repository is a modified clone of https://github.com/nickvl/aop-logging
============================================================================

Due to a fundamental API change I decided to to fork https://github.com/nickvl/aop-logging instead of issuing a pull request against tje original project. In addition to aforementioned API change this version of the logger project also contains a number of additional featues. Elaboration of the changes:
1. **API change**: In both the original project and in this repository the annotation @Lp is used in front of method parameters to signify that the given method parameter should be logged. In the original project, however, there was no way to *only* log the fact that you are entering and/or exiting the method, i.e. to not log any method parameters. This is because the logic interpreted the lack of any @Lp annotations as meaning: "Log all method parameters". This project interprets the lack of any @Lp annotations as "Log no method parameters.". To log all method parameters you must annotate all method parameters.
2. The original project defined the log level through the annotation type (@LogInfo, @LogError etc) and the scope (log entering, log exiting or both) through annotation members (`@LogInfo(LogPoint.IN)`). This project turns this around: Now the scope is given by the annotation type (`@LogAround`, `@LogBefore`, `@LogAfter`) and the log leven is defined through the annotation type members (`@LogAround(LogLevel.WARNING)`) with log level INFO being the default.
3. This project makes it possible to log the value of "this" (both when entering and exiting the method). See fx the annotation type member `LogAround.logThis` which defaults to false.
4. This project makes it possible to customize the log string in various ways. You can now customize the log entering/exiting string  and you can decide whether or not to include the fully qualified class name of the method being logged. See the constructor of UniversalLogAdapter.

aop-logging
===========

Declarative annotation based logging for Java Spring applications.
It is a small logging utility. Spring AOP is used for implementation.
Apache commons-logging is used to log messages (the same component used in Spring framework itself).

The Spring AOPlogging utility provides an ability to log:
 * entering in spring service methods,
 * method parameter names and values,
 * returning from spring service methods,
 * method execution results,
 * exceptions thrown from spring service methods.
 * an ability to avoid logging sensitive information (e.g. passwords)

It allows to flexible configure log message levels, provides exception handling taking into account 
exception classes hierarchy (alike try-catch). Log annotations could be applied for both methods and classes.
This logger is capable to log method parameters and result using reflection if the corresponding classes
do not provide toString() method. There is an ability to log a limited amount of elements of standard collections and arrays.

Quick start
-----------

### Add the dependency to your maven pom.xml
**The utility is uploaded to The Maven Central Repository**


    <dependencies>
    ...
      <dependency>
        <groupId>com.github.nickvl</groupId>
        <artifactId>xspring-aop-logging</artifactId>
        <version>0.3.2</version>
      </dependency>
    ...
    </dependencies>

### Apply the logging utility in your project

1.Activates the logger in spring's context

1.1.Xml based configuration style

    <?xml version="1.0" encoding="UTF-8"?>
    <beans
            xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:aop-logger="urn:nickvl/xspring/aop-logger"
            xsi:schemaLocation="
            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            urn:nickvl/xspring/aop-logger urn:nickvl/xspring/aop-logger/aop-logger.xsd">

        <!-- Activates the logger and @AspectJ style of Spring AOP. There are additional configuration options. -->
        <aop-logger:annotation-logger/>
        ...
    </beans>

1.2.Java-based configuration style


    package com.me.shop.config;
    import com.github.nickvl.xspring.core.log.aop.AOPLogger;
    import com.github.nickvl.xspring.core.log.aop.UniversalLogAdapter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.EnableAspectJAutoProxy;
    
    import java.util.Collections;
    import java.util.Set;
    
    @Configuration
    @EnableAspectJAutoProxy
    public class LoggerConfig {
    
        private static final boolean SKIP_NULL_FIELDS = true;
        private static final int CROP_THRESHOLD = 7;
        private static final Set<String> EXCLUDE_SECURE_FIELD_NAMES = Collections.<String>emptySet();
        private static final String ENTERING = ">>>";
        private static final String RETURNING = "<<<";
    
        @Bean
        public AOPLogger getLoggerBean() {
            AOPLogger aopLogger = new AOPLogger();
            UniversalLogAdapter adapter = UniversalLogAdapter
        		.builder()
        		.skipNullFields(SKIP_NULL_FIELDS)
        		.cropThreshold(CROP_THRESHOLD)
        		.excludeFieldNames(EXCLUDE_SECURE_FIELD_NAMES)
        		.entryString(ENTERING)
        		.exitString(RETURNING)
        		.methodNamePrefix(MethodNamePrefix.SIMPLE_CLASS_NAME)
        		.build();
            aopLogger.setLogAdapter(adapter);
            return aopLogger;
        }
    }

2.Add log annotation on required methods


    package com.me.shop;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.ws.server.endpoint.annotation.Endpoint;
    import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
    import org.springframework.ws.server.endpoint.annotation.RequestPayload;
    import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

    import com.me.shop.oxm.PaymentContract;
    import com.me.shop.shop.oxm.PaymentContractResponse;
    import com.me.shop.shop.NotEnoughMoneyException;

    import com.github.nickvl.xspring.core.log.aop.annotation.LogAround;
    import com.github.nickvl.xspring.core.log.aop.annotation.LogIn;

    /**
     * Billing shop endpoint.
     */
    @LogAround
    @Endpoint
    public class BillingShopEndpoint {

        private static final String NS = "urn:PaycashShopService";

        @Autowired
        private ShopService shop;

        @LogIn
        @LogException(value = {@Exc(value = Exception.class, stacktrace = true)}, warn = {@Exc({IllegalArgumentException.class, NotEnoughMoneyException.class})})
        @ResponsePayload
        @PayloadRoot(localPart = "PaymentContract", namespace = NS)
        public PaymentContractResponse processPaymentContract(@RequestPayload PaymentContract request) {
            return shop.checkPayment(request);
        }

        // other methods
    }

3.Configure logging in your application

### Example

Commons logging configured to log using log4j framework:

    2014-05-21 23:22:31,073 TRACE [benchmark.LoggableServiceImpl] (main) - calling: aopLogMethod(2 arguments: b=33)
    2014-05-21 23:22:31,074 TRACE [benchmark.LoggableServiceImpl] (main) - returning: aopLogMethod(2 arguments):34

### Performance measuring

A simple test shows the following results:

    Running net.ng.xspring.core.log.aop.benchmark.AOPLoggerPerformanceITCase
    Service invocation benchmark (AOPLoggerPerformanceITCase):
        600 ns takes a method when no logging is used
        1053 ns takes a method when direct logging is used
        5263 ns takes a method when aop logging is used

    Running net.ng.xspring.core.log.aop.benchmark.UniversalLogAdapterPerformanceITCase
    Building toString value benchmark (UniversalLogAdapterPerformanceITCase):
        2979 ns takes a method when reflection is used
        98 ns takes a method when overridden toString is used
