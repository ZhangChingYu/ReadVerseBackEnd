# ReadVerse Server
首先介紹一下文件夾的結構
## 1. build.gradle: 
比較需要注意的部分只有 **dependencies** 的內容。
我們在創建一個 Spring Boot 項目時有一個步驟是勾選需要的 dependencies，這些 dependencies (ex. Spring Web, Lombok, Spring Data JPA 等等)在項目創建後便會在 build.gradle 這個文件中的 Dependencies 部分展現。

當我們需要拓展項目依賴時，比如發現需要將 Object 類型的數據轉換成 Json 格式，我們知道可以通過添加 Gson 的 dependency 來調用函數進行轉換，可以上網查詢 dependency 的格式:
~~~ 
implementation group: 'com.google.code.gson', name: 'gson', version: '2.7'
~~~
然後將其添加到 build.gradle 即可：
~~~
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	# 添加在這裡，其實不用管順序，隨便貼在某一行都可以
	implementation group: 'com.google.code.gson', name: 'gson', version: '2.7'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
~~~
## 2. application.properties
這個文件就是我們說的配置文件了，它位於 java > resources > application.properties。在這裡我們會編輯數據庫相關的配置：
~~~
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/ReadVerse
spring.datasource.username=root
spring.datasource.password=Sunny.1218
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
~~~
也會設置服務器的端口：
~~~
server.port=8080
~~~
以上就是我們需要注意的文件，是不是很少啊？哈哈哈～～
# 接下來我們來看項目真正的結構
### 1. models
這裡主要是對應數據庫存放的 Table 結構，以 Customer 為例
### 2. repositories
### 3. services
### 4. controllers
### 5. dto

