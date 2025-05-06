# Java 21 Migration Guide

## Overview

This document is designed to be a comprehensive resource for users who have deployed the latest versions of the Modular Open Source Identity Platform (MOSIP) compatible with Java 11 and are preparing to upgrade their systems to Java 21. It provides a detailed, step-by-step migration process to facilitate a seamless and efficient transition. By adhering to the guidelines in this document, users can modernize their MOSIP environments to take full advantage of Java 21's improved performance, advanced security features, and enhanced functionality. The guide also emphasizes best practices to minimize disruptions, maintain system stability, and ensure compliance throughout the upgrade process.

## **1. Pre-requisites:**

1. **JDK 21**: Ensure Java Development Kit (JDK) 21 is installed and configured in your system's environment variables.
2. **Maven (Latest Version)**: To build and manage dependencies, use the latest version of Maven, such as 3.9.6.
3. **Optional:**

* A modern IDE (e.g., Eclipse, IntelliJ IDEA, or others) is compatible with Java 21 to streamline coding and debugging.
* Ensure the **Lombok library** version is compatible with your IDE. \
  For instance, **Lombok 1.18.30** works seamlessly with the latest IDE versions.

{% hint style="info" %}
**Note**: After adding Lombok to your project, ensure it is correctly set up in your IDE to avoid compilation issues. This typically involves running the Lombok installer or manually enabling it in the IDE settings.
{% endhint %}

## **2. Backward compatibility:**

1. Java applications compiled in older versions are compatible with Java 21 run time. To support running those application jars in Java 21, additional VM Arguments need to be added while running applications in Java 21.

```
--add-modules=ALL-SYSTEM --add-opens=java.base/java.lang=ALL-UNNAMED
```

2. Java applications compiled with older versions are compatible with the Java 21 runtime. However, to ensure these application JARs run correctly in Java 21, additional JVM arguments may need to be specified when running the applications.
   1. The libraries must be API-compatible and should not rely on deprecated or removed APIs.
   2. The libraries should not depend on older Spring Boot versions (before 3.x), as the newer Spring Boot versions introduce significant API changes. Failure to meet this requirement can lead to compile-time or runtime issues, such as errors during class loading, bean initialization, or method invocation.\

3. The dependent libraries of any module that have a dependency on any other MOSIP library (such as kernel-core) or older Spring Boot version (older than 3.x) need to be migrated before migrating the specific module. This applies not only to the static dependencies mentioned in the POM file, but also to the dynamic dependencies loaded from the classpath such as Kernel Auth Adaptor, BioSDK client, or any such libraries.

## **3. Migration Changes in a module/repository:**

### **I. POM file changes:**

1. All POM versions of the modules and their dependency modules should be updated to reference the Java 21 migrated version.
2.  Change the source and target compiler versions to 21:

    ```xml
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    ```
3.  Jacoco-plugin version needs to be updated to 0.8.11:

    ```xml
    <jacoco.maven.plugin.version>0.8.11</jacoco.maven.plugin.version>
    ```

{% hint style="info" %}
**Note:** A new kernel-bom file has been introduced as part of this release in the commons repo which contains all the latest version changes to the spring-boot and other dependencies. Here spring-boot:3.2.3 is used.
{% endhint %}

4. Please refer to this [file ](https://github.com/mosip/commons/blob/v1.3.0-beta.1/kernel/kernel-bom/pom.xml)to learn about the dependencies and versions. this file is created to remove the repetitiveness in defining the dependencies. If you have other repositories that use repeated definitions of dependencies, create a new bom file for that specific repository that includes the kernel-bom in the dependencyManagement section then add the extra dependencies with appropriate versions, and then use the same bom file in your respective modules pom files.\

5.  Any module that needs the predefined versions for any of its dependencies should import the kernel-bom file into the module pom file’s dependency management section. Remove all the versions from the properties and dependencies section for which kernel-bom has already defined the version.\
    \
    Please refer to the example in the [audit-service pom file](https://github.com/mosip/audit-manager/blob/e7b86dfa27da2a083cbe28e8ac950ac798a38bfe/kernel/kernel-auditmanager-api/pom.xml#L32). (You will need to include the latest link once tagging is done). If a module does not need any version from the kernel-bom, it's not needed to import it.

    ```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.mosip.kernel</groupId>
                <artifactId>kernel-bom</artifactId>
                <version>1.3.0-beta.1</version> <!-- Reconfirm the version during the release -->
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ```
6. A module pom or bom file can import one or more bom files, and if there is a dependency referred from more than one bom file, it will be referred from the last bom file. Please refer [here](https://docs.spring.io/dependency-management-plugin/docs/1.0.8.RELEASE/reference/html/#dependency-management-configuration-bom-import-multiple).
7. Unless there is a compelling reason for using a different version of the library than the version defined in kernel-bom, do not mention the version to that dependency, if done it will override the version with the specified one.
8. Remove any unused version properties from the pom.
9.  **Swagger UI update:**\
    To upgrade to **Swagger-UI version 3**, make the following changes:\
    \
    **1. Update the Dependency**\
    Add the `springdoc-openapi-starter-webmvc-ui` dependency to your [pom.xml](https://github.com/mosip/audit-manager/blob/release-1.3.x/kernel/kernel-auditmanager-service/pom.xml) file.\


    ```xml
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.5.0</version>
    </dependency>
    ```

&#x20;      **2. Remove Deprecated Dependency**\
&#x20;     Remove any reference to the `springdoc-openapi-ui` dependency to prevent conflicts.

{% hint style="info" %}
**Note**: If swagger-2 was used in the module already, change it to Swagger-3 and also make the above change.
{% endhint %}

10. **Bouncy Castle Version update:**\
    Remove reference to an older version of the bouncycastle library and use the below one. The same is applied in kernel-bom and kernel-core. Please refer to the [pom.xml](https://github.com/mosip/commons/blob/a075f146b04c95b09d19b3ad80f396a2faa95ee6/kernel/kernel-bom/pom.xml#L127) file.

```xml
<dependency>
	<groupId>org.bouncycastle</groupId>
	<artifactId>bcprov-jdk18on</artifactId>
	<version>1.78.1</version>
</dependency>
```

{% hint style="info" %}
#### **Note:**

* Any exclusions specified for a library in the POM can be retained. However, the version mentioned in that dependency can be removed, allowing it to inherit the version defined in the kernel-bom or another POM file.
* Always make it a practice to keep the versions in the properties instead of hardcoding.
* Even if the version is changed in the properties of pom.xml, make sure those properties are used in those dependencies/plugins instead of hardcoding the version.

**For example:**

`maven-javadoc-plugin dependency should refer to ${maven.javadoc.version} property in the POM file.`
{% endhint %}

11. Check and remove any unused version properties. With the use of kernel-bom, the version does not need to be mentioned to the dependencies mostly, unless it needs to be overridden or a different version is used.
12. POM files should not include duplicate version properties, as this can lead to errors. For example, even if the version property is updated correctly in its first occurrence, subsequent occurrences may override it with an outdated or incorrect version. This behavior can go unnoticed and may cause unexpected errors or functionality issues. To avoid such problems, carefully review POM files to identify and remove any repeated version properties. This ensures consistent version management and prevents overriding conflicts.

### **II. Java file changes:**&#x20;

1. Below are the package changes that need to be applied in Java files.

| Text to Replace                                       | Replacement                                                |
| ----------------------------------------------------- | ---------------------------------------------------------- |
| javax.servlet.\*                                      |  jakarta.servlet.\*,                                       |
| javax.annotation.\*                                   |  jakarta.annotation.\*,                                    |
| javax.activation.\*                                   |  jakarta.activation.\*,                                    |
| javax.persistence.\*                                  |  jakarta.persistence.\*,                                   |
| javax.validation.\*                                   |  jakarta.validation.\*,                                    |
| javax.mail.\*                                         |  jakarta.mail.\*,                                          |
| org.apache.http.impl.client.\*                        |  org.apache.hc.client5.http.impl.classic.\*,               |
| org.apache.http.conn.ssl.SSLConnectionSocketFactory   |  org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory |

{% hint style="info" %}
**Note:** While doing a text replacement for the above packages, it might be accidentally renaming the properties having the same naming for example javax.persistence.jdbc.driver. Please make sure this does not happen. Please refer [here](https://github.com/mosip/commons/pull/1505/files) to such fixes.
{% endhint %}

2. **Postgres Hibernate Dialect:** Instead of using specific version dialects for PostgreSQL, such as org.hibernate.dialect.PostgreSQL95Dialect or org.hibernate.dialect.PostgreSQL92Dialect, it should be org.hibernate.dialect.PostgreSQLDialect. This is applicable for properties referred to in Java code and any properties file as well.\

3. **The Sleuth configuration** has now been migrated to the Micrometer Tracer
   1. This required the addition of micrometer tracing dependencies and quartz scheduler dependency which is already included in [kernel-core pom file.](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-core/pom.xml)
   2. Code changes were also necessary for the migration.
      1. Import `BraveAutoConfiguration.class` instead of using a component scan of `org.springframework.cloud.sleuth.autoconfig.*`
      2. Use the `AccessLogValve` class as the base class instead of `ValveBase` for the `SleuthValve` class.
      3. Replace the following code:
         * `tracer.newTrace()` → `tracer.nextSpan()`
         * `span.context().traceIdString()` → `span.context().traceId()`
         * `span.context().spanIdString()` → `span.context().spanId()`
      4. Refer to the details below for the changes made.
         1. [Sleuth Auto Configuration](https://github.com/mosip/commons/blob/73aa49a9e943fcfe6f65b1158688429432c8397e/kernel/kernel-applicanttype-api/pom.xml#L8)
         2. [Sleuth Valve](https://github.com/mosip/commons/blob/73aa49a9e943fcfe6f65b1158688429432c8397e/kernel/kernel-applicanttype-api/pom.xml#L8)
4.  **HTTP Connection Manager**  changes:&#x20;

    The following changes have been introduced related to the HTTP Connection Manager:

    1. **Deprecation of Existing Configuration Methods**
       * The previously existing configuration methods have been deprecated and removed.
       * New configuration methods must be used, utilizing updated classes such as:
         * `ReactorLoadBalancerExchangeFilterFunction`
         * `PoolingHttpClientConnectionManagerBuilder`
    2. **Direct Auto-Wiring of `ReactorLoadBalancerExchangeFilterFunction`**
       * Instead of using `LoadBalancerClient` with `LoadBalancerExchangeFilterFunction`, directly auto-wire `ReactorLoadBalancerExchangeFilterFunction` in your implementation.
    3. **New Way to Set `setMaxConnPerRoute`**
       * The method for setting `setMaxConnPerRoute` in the HTTP connection manager now uses `PoolingHttpClientConnectionManagerBuilder`.

    For detailed implementation and examples, refer to this [pull request](https://github.com/mosip/mosip-openid-bridge/pull/248/files#diff-b1dfbafdc22fd490550a2dab462b251a1965114e980c886764f9d840b1368249).
5. **Spring Security Changes:** The Old way of extending WebSecurityConfigurationAdapter for SecurityConfig is deprecated, now it requires a new way of configuring the same using as mentioned below.
   1. Remove `extends WebSecurityConfigurerAdapter`
   2. Replace `@EnableGlobalMethodSecurity(prePostEnabled = true)` with `@EnableMethodSecurity`
   3. Replace the `.antMatchers()` method with `.requestMatchers()`
   4. Replace the `@Override` on the configure method with `@Bean` that returns the `SecurityFilterChain` instance as a result of `http.build()`
   5. Replace the existing deprecated way of HttpSecurity configurations with lambda based configuration. Please refer to this file present [here](https://github.com/mosip/mosip-openid-bridge/pull/248/files#diff-d00cf9604efefecccc5ca9614e93d3f1073875854aaf4edf3dfec8a576297491R154-R155).
   6. `HttpStatusCodeException.getStatusCode()` should be replaced with `HttpStatus.valueOf(statusCodeException.getStatusCode().value())`\
      Please refer to [this link](https://github.com/mosip/mosip-openid-bridge/pull/248/files#diff-b2d63f4dbe0db690f69eac56ce2e1bf741f9e8f145be59917b2c577624ecdf02) where all above stated changes have been made:
6. The existing `bootstrap.properties` file continues to function only if the spring-cloud-starter-bootstrap dependency is added. This dependency is currently included in the kernel-core mentioned in the [pom.xml](https://github.com/mosip/commons/blob/71f49497ba5dc5d3f8771c0656d442dcd071313b/kernel/kernel-core/pom.xml#L63) file. If the dependency is not added, you will need to use application.properties instead.
7. **Spring Batch Migration:** Spring Batch has been migrated to version 5.x. The following changes need to be applied:
8. **DB Changes**:
   1. The Map-based job repository is now deprecated. Therefore, any application running a batch job must have a database with Batch Job-related tables. If a database is not feasible, at least an in-memory database must be configured. Refer to the [DB script ](https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-postgresql.sql)for creating the tables.
   2. If a Spring datasource is already being used in the Spring Batch job application, the Batch Job-related tables must be created in that datasource (i.e., database) using the aforementioned DB script.
   3.  The existing Spring Batch Job tables have to be applied with the below upgrade script:\


       ```
       -- ------------------------------------------------------------------------------------------
       -- Upgrade script for Migrating Spring batch version to 5.0 as part of Java 21 Migration.
       -- References: 
       --  1. https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide#ms-sqlserver
       --  2. https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/migration/5.0/migration-postgresql.sql
       -- ------------------------------------------------------------------------------------------
       ALTER TABLE BATCH_STEP_EXECUTION ADD CREATE_TIME TIMESTAMP NOT NULL DEFAULT '1970-01-01 00:00:00';
       ALTER TABLE BATCH_STEP_EXECUTION ALTER COLUMN START_TIME DROP NOT NULL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS DROP COLUMN DATE_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS DROP COLUMN LONG_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS DROP COLUMN DOUBLE_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN TYPE_CD TYPE VARCHAR(100);
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME TYPE_CD TO PARAMETER_TYPE;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN KEY_NAME TYPE VARCHAR(100);
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME KEY_NAME TO PARAMETER_NAME;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN STRING_VAL TYPE VARCHAR(2500);
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME STRING_VAL TO PARAMETER_VALUE;
       ALTER TABLE BATCH_JOB_EXECUTION DROP COLUMN JOB_CONFIGURATION_LOCATION;

       CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
       CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
       CREATE SEQUENCE BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CYCLE;
       ```
   4.  The rollback script for the above is given below:\


       ```
       -- ------------------------------------------------------------------------------------------
       -- Revoke script for Migrating Spring batch version to 5.0 as part of Java 21 Migration.
       -- References: 
       --  1. https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide#ms-sqlserver
       --  2. https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/migration/5.0/migration-postgresql.sql
       -- ------------------------------------------------------------------------------------------
       ALTER TABLE BATCH_STEP_EXECUTION DROP CREATE_TIME TIMESTAMP NOT NULL DEFAULT '1970-01-01 00:00:00';
       ALTER TABLE BATCH_STEP_EXECUTION ALTER COLUMN START_TIME ADD NULL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ADD COLUMN DATE_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ADD COLUMN LONG_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ADD COLUMN DOUBLE_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN TYPE_CD TYPE VARCHAR(6);
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME PARAMETER_TYPE TO TYPE_CD;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN KEY_NAME TYPE VARCHAR(100);
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME PARAMETER_NAME TO KEY_NAME;
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS ALTER COLUMN STRING_VAL TYPE VARCHAR(250);
       ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME PARAMETER_VALUE TO STRING_VAL;
       ALTER TABLE BATCH_JOB_EXECUTION ADD COLUMN JOB_CONFIGURATION_LOCATION;

       DROP SEQUENCE BATCH_STEP_EXECUTION_SEQ;
       DROP SEQUENCE BATCH_JOB_EXECUTION_SEQ;
       DROP SEQUENCE BATCH_JOB_SEQ;
       ```
9.  **POM Changes:**

    1.  Add `spring-boot-starter-batch` if it does not already exist. The version will be 3.x if `kernel-bom` is used, as discussed in the previous sections.\


        ```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-batch</artifactId>
        </dependency>
        ```
    2.  Add hibernate-validate dependency if it does not exist. The version will be 8.x if kernel-bom is used as discussed in the previous sections.\


        ```xml
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
        </dependency>
        ```


10. **Java Code changes:**
    1. Remove `@EnableBatchProcessing` Annotation
    2. The `BatchConfigurer` and `DefaultBatchConfigurer` classes are deprecated, and any references to them must be removed. For example, in the `kernel-salt-generator`, these classes were used to implement a Map-based Job Repository. However, since the Map-based Job Repository is no longer supported and requires the use of database tables, the only option is to remove references to these classes and create the necessary batch job tables.
    3.  Create a bean definition for `PlatformTransactionManager`\
        Please refer to the[ SaltGeneratorConfig.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-salt-generator/src/main/java/io/mosip/kernel/saltgenerator/config/SaltGeneratorConfig.java#L113-#L116) file. Below is the relevant code snippet from the file:\


        ```java
        @Bean
        public PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }
        ```
    4.  `JobBuilderFactory` and `StepBuilderFactory` are deprecated. Instead, use `JobBuilder` and `StepBuilder`.

        For reference, please refer to the [SaltGeneratorJobConfig.java ](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-salt-generator/src/main/java/io/mosip/kernel/saltgenerator/config/SaltGeneratorJobConfig.java#L54-L62)file.
    5.  Since `JobExecutionListenerSupport` is deprecated in favor of the `JobExecutionListener` interface, update the Batch Job Listener class to implement `JobExecutionListener` instead of extending `JobExecutionListenerSupport`.

        For reference, see the relevant code in the [BatchJobListener.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-salt-generator/src/main/java/io/mosip/kernel/saltgenerator/listener/BatchJobListener.java#L26) file.
    6.  The `write` method parameter in the `org.springframework.batch.item.ItemWriter` interface has been changed from `List` to `Chunk`.

        For reference, please refer to the [SaltWriter.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-salt-generator/src/main/java/io/mosip/kernel/saltgenerator/step/SaltWriter.java#L35-L44) file.
    7. Key Changes:
       1.  To obtain a stream from a `Chunk`, use the `StreamSupport` utility class as follows:

           ```java
           javaCopyEditStreamSupport.stream(chunk.spliterator(), true)
           ```
       2. Similar to the `List.of()` the method used for creating a `List`, you can use the `Chunk.of()` method to create a `Chunk`.
11. **References:**
    1. **Spring Batch 5.0 Migration Guide**: [Spring Batch 5.0 Migration Guide](https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide#ms-sqlserver)
    2.  In `@RestControllerAdvice`, if the response content type is not explicitly set to `application/json`, it might default to returning an XML response. To prevent this, ensure the `contentType` is specified in the `ResponseEntity` as shown below:\


        ```java
        return ResponseEntity.status(<any code>)
                .contentType(MediaType.APPLICATION_JSON)
                .body(<any body>);
        ```
12. In `org.apache.commons.lang3.time.DateUtils`, null parameters will now throw a `NullPointerException` instead of an `IllegalArgumentException`. To prevent breaking functionality, handle the exception as shown below.

    The impact is observed in utility methods for the following classes:

    * [CalendarUtils.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-core/src/main/java/io/mosip/kernel/core/util/CalendarUtils.java)
    * [DateUtils.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-core/src/main/java/io/mosip/kernel/core/util/DateUtils.java)

    You can handle the `NullPointerException` appropriately by adding a null check or by using a try-catch block.
13. In the `org.apache.commons.lang3.StringUtils.join` method, invalid arguments now throw an `IllegalArgumentException` instead of an `ArrayIndexOutOfBoundsException`.To prevent breaking functionality, handle this exception as shown in the [StringUtils.java file](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-core/src/main/java/io/mosip/kernel/core/util/StringUtils.java#L624-L628).
14. Symmetric Algorithm AES/GCM/PKCS5Padding is no longer supported and it should be replaced with AES/GCM/NoPadding. Please refer to the CryptoUtil changes mentioned in the [CryptoUtil.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-core/src/main/java/io/mosip/kernel/core/util/CryptoUtil.java#L36) file.
15. In a JPA repository, for non-native query methods, the column names should be based on the entity’s field names rather than the actual column names. For example, if an entity has a column `lang_code` mapped to a field `private String langCode`, the non-native query should use `langCode` instead of `lang_code`.
16. Since Joda Time is deprecated, the related date formatting classes have been removed from `spring-context`, which may cause compatibility issues. Therefore, replace Joda's Time-based logic with `java.time`-based logic to ensure compatibility and maintainability.
17. **Hibernate** **CriteriaBuilder**: Hibernate’s CriteriaBuilder now cannot be reused for multiple CriteriaQuery instances using the createQuery method on the same builder instance. When we want to create multiple CriteriaQuery it should be associated with a new CriteriaBuilder instance, otherwise, it will throw an error saying java.lang.IllegalArgumentException: Already registered a copy: SqmBasicValuedSimplePath.
18. **JPA Naming Strategy:** The `SpringPhysicalNamingStrategy` class is no longer available in the latest Spring Boot jar. Therefore, any JPA configuration related to it should refer to the class `org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl`.\
    For example:

    ```properties
    #JPA Naming Strategy for Springboot 3
    spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
    ```
19. If component scanning does not work with the `scanBasePackages` attribute within the `@SpringBootApplication` annotation, move that definition inside the `@ComponentScan` annotation’s `basePackages` attribute. The same applies to any exclusions. When using the exclusion attribute in the `@ComponentScan` annotation, apply the AspectJ `filter-type` to exclude a list of packages for convenience.

### **III. JUnit Test Case related changes**

1.  **JUnit Test Dependency:** To run the JUnit tests, the following dependency is required and has now been added to `kernel-core`. For reference, see: [kernel-core pom.xml](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-core/pom.xml#L255-L258)\


    ```xml
    <dependency>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>    
    </dependency>
    ```
2. MVEL Dependency Update: If MVEL-related test cases are failing, update the MVEL dependency as per the [kernel-applicanttype-api pom.xml](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-applicanttype-api/pom.xml#L32-L36) file.
3.  **Spring Boot Test Error Fix:**

    If you encounter the following error:\


    ```vbnet
    Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to determine a suitable driver class
    ```

    \
    Add the exclusion in the Spring Boot Test application as shown below:\


    ```java
    @SpringBootApplication(scanBasePackages = { "any.packages.*" },
    exclude={DataSourceAutoConfiguration.class})
    ```

This will exclude the `DataSourceAutoConfiguration.class`, resolving the issue.

4.  **Mockito Related errors:** The `Mockito` dependencies in the `spring-boot-bom` were incorrect, but this has now been corrected in `kernel-bom`. The Mockito version is now correctly set to `3.4.3`.

    For reference, please refer to the [kernel-bom pom.xml](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-bom/pom.xml#L13) file.
5.  **Power-Mockito related issues:**

    1. **Mockito Core Version Issue:**
       *   If you encounter the following error:

           ```makefile
           java.lang.NoSuchMethodError: 'java.lang.AutoCloseable org.mockito.MockitoAnnotations.openMocks
           ```

    **Solution:** Ensure that the `mockito-core` version is set to `3.4.3`.



    &#x20; b. **Maven Build Access Issues:**

    * If the build fails due to access-related issues, add the following `argLine` configuration in the `maven-surefire-plugin` section of the `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${maven.surefire.plugin.version}</version>
    <configuration>
        <skipTests>false</skipTests>
        <skip>false</skip>
        <argLine>
            ${argLine}
            --add-opens java.xml/jdk.xml.internal=ALL-UNNAMED
            --add-opens java.base/java.lang=ALL-UNNAMED
            --illegal-access=debug
            --enable-preview
        </argLine>
    </configuration>
</plugin>
```

For example, if you are facing the following error:

```vbnet
module java.base does not "opens java.io" to the unnamed module
```

then add the below command to the argument line.

```csharp
--add-opens java.base/java.io=ALL-UNNAMED
```

*   If you encounter the `PBKDF2WithHmacSHA256 algorithm not supported` error, add `javax.crypto.*` to `@PowerMockitoIgnore` as shown below:\


    ```java
    @PowerMockIgnore({ "@PowerMockIgnore({
        "com.sun.org.apache.xerces.*",
        "javax.xml.*",
        "org.xml.*",
        "javax.management.*",
        "com.sun.org.apache.xalan.*",
        "javax.crypto.*"
    })
    ```
* If you get the error `PowerMockitoInjectingAnnotationEngine.injectMocks() is not accessible` because it is a private method, use `mockito-core version 3.11.2` in that module specifically.

6. **Test Security Config:** To configure test security, a `SecurityFilterChain` bean is required. The bean can be implemented as shown below:

```java
@Bean
protected SecurityFilterChain configureSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.build();
}
```

For reference, see the implementation in the file [TestSecurityConfig.java](https://github.com/mosip/commons/blob/release-1.3.x/kernel/kernel-notification-service/src/test/java/io/mosip/kernel/emailnotification/test/config/TestSecurityConfig.java#L34-L38) (lines 34-38)

If you encounter the following error:

```java
ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for
```

\
Use the `-e` flag with the `mvn clean install` command to enable stack trace printing:\


```java
mvn clean install -e
```

After running the command, scroll to the top of the errors to locate the **real root cause**, as this error is not the original issue.

7. **Dependency Version Issue:** In `kernel-pdfgenerator-itext`, downgrade the `itext-core` version from `7.2.0` to `7.1.0` to fix test case issues.
8.  **Error: MockMvc - 401 or 403 Status**\
    If using `mockMvc` results in errors like the below:\


    <pre class="language-makefile"><code class="lang-makefile"><strong>java.lang.AssertionError: Status expected:&#x3C;200> but was:&#x3C;401>
    </strong></code></pre>

    \
    Check the `TestSecurityConfig` to ensure that the URLs are permitted. Configure it as shown below to allow all requests during JUnit tests:\


    <pre class="language-java"><code class="lang-java"><strong>Fix: Modify the SecurityFilterChain bean definition:
    </strong>httpSecurity.authorizeHttpRequests(http -> http.anyRequest().permitAll());
    </code></pre>
9. **Component Scanning Fix:** If component scanning does not work with the `scanBasePackages` attribute within the `@SpringBootApplication` annotation, follow these steps:
   * Move the `scanBasePackages` definition to the `@ComponentScan` annotation’s `basePackages` attribute.
   * The same approach applies to any exclusions.
   *   When using the `exclusion` attribute in the `@ComponentScan` annotation, use the `ASPECTJ` filter type to conveniently exclude specific packages.\
       \
       **Example:**

       ```java
       @ComponentScan(
           basePackages = {"com.example.package1", "com.example.package2"},
           excludeFilters = @Filter(type = FilterType.ASPECTJ, pattern = "com.example.excluded.*")
       )
       ```
10. **Test Properties Not Loaded:** If JUnit tests are not loading the test properties, ensure that the following annotation is added to the test class:

```java
javaCopyEdit@TestPropertySource("classpath:application.properties")
```

Replace `application.properties` with the correct properties file name, if different.

{% hint style="info" %}
**Note:** \
**Avoid Mixing JUnit 4 and JUnit 5**

While this may not be directly related to Java migration, it is strongly recommended to use either JUnit 4 or JUnit 5 consistently throughout your test classes. Mixing the two versions can cause compatibility issues, leading to test failures or unexpected behavior.

**Key Differences and Equivalents:**

1. **Assertions**
   * JUnit 4: `org.junit.Assert`
   * JUnit 5: `org.junit.jupiter.api.Assertions`
2. **Lifecycle Annotations**
   * JUnit 4: `@Before`
   * JUnit 5: `@BeforeEach`
{% endhint %}

### **IV.** Configuration Changes

**1. Explicitly Configuring Ant Path Matcher**

In Spring MVC, the default path-matching strategy has changed to `PathPatternParser`. This can cause failures when processing Ant-style patterns. To avoid such issues, explicitly configure the application to use  `AntPathMatcher` by setting the appropriate property in your configuration file.

**For example:**

```properties
propertiesCopyEditspring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER
```

Refer to the implementation here: [application-default.properties (lines 469-470)](https://github.com/mosip/mosip-config/blob/release-1.3.x/application-default.properties#L469-L470)

**2. Updated HTTP Header Size Property**

The `server.max-http-header-size` property is now deprecated. Use `server.max-http-request-header-size` instead to configure the maximum HTTP request header size.

**For example:**

```properties
propertiesCopyEditserver.max-http-request-header-size=8192
```

Refer to the implementation here: [application-default.properties (lines 337-338)](https://github.com/mosip/mosip-config/blob/release-1.3.x/application-default.properties#L337-L338)

### **V. Artifactory Changes**

The following steps can be followed to configure Artifactory during the Java migration for the modules:

1. If a dynamic dependency is migrated for a module that needs to be loaded from the artifactory (either directly downloaded as a jar file or packaged in a zip file), it can be created as a new entry to the artifactory pom.xml file by mentioning the version and its path which is different from an existing entry.
2. The idea is to keep the existing artifacts unchanged to have the existing services unaffected and only add new entries that are differentiated by the new version in the jar file or the containing folder. Finally, when all modules migration is done we can remove the old version artifact entries.
3. While deploying a migrated service, it is essential to update the Docker run command or Helm chart configuration to include the appropriate argument for loading the newer version of the dynamic library from the Artifactory server.

### **VI. Docker file changes**

The following updates are done in the docker file.

| Text to Replace                                                                                  | Replacement                                                                                                                                   |
| ------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------- |
| FROM openjdk:11                                                                                  | FROM eclipse-temurin:21-jre-alpine                                                                                                            |
| apt-get -y update                                                                                | apk -q update                                                                                                                                 |
| apt-get update -y                                                                                | apk -q update                                                                                                                                 |
| apt-get install -y                                                                               | apk add -q                                                                                                                                    |
| apk add -q unzip \\                                                                              | apk add -q unzip wget \\                                                                                                                      |
| apt-get -y install                                                                               | apk add -q                                                                                                                                    |
| groupadd -g ${container\_user\_gid} ${container\_user\_group}                                    | addgroup -g ${container\_user\_gid} ${container\_user\_group}                                                                                 |
| useradd -u ${container\_user\_uid} -g ${container\_user\_group} -s /bin/sh -m ${container\_user} | adduser -s /bin/sh -u ${container\_user\_uid} -G ${container\_user\_group} -h /home/${container\_user} --disabled-password ${container\_user} |
| ARG container\_user\_uid=1001                                                                    | ARG container\_user\_uid=1002                                                                                                                 |

### **VII. Github Workflow changes:**

1.  If the new branch `develop-java21` is not included in the GitHub workflow push trigger, ensure that it is added.

    To see the changes made please refer to [this commit](https://github.com/mosip/commons/commit/9d2a5676666a9f1a4d6aec2dfbcfee900646b3b7).

**Key Updates:**

1. In the push trigger, the Java version was updated from:

* `java-version: 11` → `java-version: 21`

2. For PR checks, any references to the `kattu` repository in the GitHub workflow YAML files should now point to the `master-java21` branch.\
   Please refer to the related commit [here](https://github.com/mosip/commons/commit/a987dc0cf45b3925b8ea3f71bf8fa62eaf087d1a).

{% hint style="info" %}
#### **Note:**

* Please refer to the changes made for running the audit service in the following pull requests (PRs):
  * [PR 1499 - mosip/commons](https://github.com/mosip/commons/pull/1499)
  * [PR 248 - mosip/mosip-openid-bridge](https://github.com/mosip/mosip-openid-bridge/pull/248)
  * [PR 144 - mosip/audit-manager](https://github.com/mosip/audit-manager/pull/144)
* While these changes apply generally to most modules, specific dependencies or code used by other modules may require additional, module-specific modifications.
{% endhint %}

## 5. Additional Module-wise Migration Guides

### I. Resident Service:

Perform the following additional steps if you encounter the issue mentioned below during migration:

* **Interceptor Issue:** An empty interceptor is not working with Hibernate 6. To resolve this, you need to implement the `Interceptor` interface.

```java
public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
return super.onSave(entity, id, state, propertyNames, types);}
public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
return super.super.onLoad(entity, id, state, propertyNames, types);}
public boolean onFlushDirty(Object entity, Serializable id, Object[] state, Object[] previousState,
			String[] propertyNames, Type[] types) {
			return super.onFlushDirty(entity, id, state, previousState, propertyNames, types);}
```

As part of the Java upgrade from an older version, the above method is deprecated. Therefore, use the following method instead:

```java
public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
// Instead of super.onSave needs to return Interceptor.onSave
return Interceptor.onSave(entity, id, state, propertyNames, types);
}
public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) {
return Interceptor.super.onLoad(entity, id, state, propertyNames, types);}
public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
return Interceptor.super.onFlushDirty(entity, id, currentState, previousState, propertyNames, typ
```

### II. Registration Client:

* The `registration-client` internally uses Derby as the local database. As part of the migration to Java 21, the Derby dialect has been updated from `DerbyTenSevenDialect` to `DerbyDialect` to ensure compatibility.
* During the migration, issues were observed when converting request and response objects to `String` or `Map` with `registration-processor` APIs. To address this:
  * The `requestType` for some APIs was explicitly set to `String`.
  * The response is now received as an `Object` and converted to a `Map` to handle exceptions.
*   **Due to this change:**

    * A non-migrated `registration-client` will not work with a migrated `registration-processor`.
    * However, a migrated `registration-client` will work with a non-migrated `registration-processor`, or both modules must be migrated together.
    * As part of the Java migration, JavaFX has been upgraded to version 21.0.3 to support the latest features. When setting up the Java 21-migrated `registration-client` repository in your IDE, you will need to:
      1. Download the required JavaFX ZIP file.
    * While running or debugging the `Initialization.java` class to start the `registration-client` application, we pass certain VM arguments to ensure the application runs correctly. One of these arguments specifies the path to the OpenJFX ZIP file. You will need to update the path in the VM arguments to point to the latest JavaFX ZIP file that was downloaded in the previous step. Additionally, a few changes have been made to the existing VM arguments to support Java 21.&#x20;

    The updated VM arguments are listed below:\


    ```css
    --module-path <path-to-zip-file>\openjfx-21.0.3_windows-x64_bin-sdk\javafx-sdk-21.0.3\lib 
    --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.web,javafx.swing,javafx.graphics 
    --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED 
    --add-modules=ALL-SYSTEM 
    --add-opens=java.base/java.lang=ALL-UNNAMED
    ```

#### Running the Registration Client Downloader Docker image

* The base image in the Dockerfile has been updated to `mosipdev/openjdk-21-jdk:latest`    to support Java 21.
* The `registration-api-stub-impl` JAR dependency has been added to the Artifactory POM file. During the deployment of the registration client, this dependency is pulled from Artifactory and bundled with other JAR files in the `lib` folder. This approach avoids adding the dependency directly to the `registration-services` POM file. If custom implementations related to document scanning or geo-positioning are required, they can be pulled from Artifactory and bundled without modifying the `registration-client` codebase.
* Since JavaFX has been migrated to version 21.0.3, the JavaFX-related files, specifically `zulu21.34.19-ca-fx-jre21.0.3-win_x64.zip`, have been added to Artifactory. This ZIP file is used when preparing the registration-client downloadable ZIP file.
* Modifications have been made to the `configure.sh` script to support the above two changes.

### III. Pre-Registration:

Please refer to the below points for additional steps for the deployment of the pre-registration module:

* **Pre-registration Batch Job Upgrade Scripts for PostgreSQL DB:**
  * Run the scripts in the respective pre-registration batch job tables.
* **Pre-reg Service Migration:**
  * Migrate the pre-reg service from Java 11 to Java 21. For more details, refer to the [Pull Request #683](https://github.com/mosip/pre-registration/pull/683) in the MOSIP repository.
* **Java 21 Jars:**
  * Java 21 JARs can be taken from [here](https://oss.sonatype.org/content/repositories/snapshots/io/mosip/).

{% hint style="info" %}
**Note:**

**Keycloak Role Removal:** Please remove the **INDIVIDUAL** role from the **mosip\_prereg\_client Available Roles** in Keycloak.
{% endhint %}

### IV. Web-Sub Migration:

Please refer to the following points while migrating the Web-Sub Java version:

* The Web-Sub repository contains a Java-based module called `kafka-admin-client`.
* As part of the migration process, the `websub/kafka-admin-client` module has been updated to Java 17, the latest version supported by Ballerina.
* Additionally, Ballerina has been upgraded to the latest version, **2201.9.0 (Swan Lake Update 9)**, to ensure compatibility with Java 17. This upgrade enables the module to leverage the new features and improvements introduced in both Java 17 and the updated Ballerina version.
