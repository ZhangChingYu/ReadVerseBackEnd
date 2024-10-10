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
~~~ properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/ReadVerse
spring.datasource.username=root
spring.datasource.password=Sunny.1218
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
~~~
也會設置服務器的端口：
~~~ properties
server.port=8080
~~~
以上就是我們需要注意的文件，是不是很少啊？哈哈哈～～
# 接下來我們來看項目真正的結構
### 1. models
這裡主要是對應數據庫存放的 Table 結構，以 Customer 為例：
~~~ sql
Create Table customer (
    customer_id bigint primary key,
    password varchar(225) not null,
    email varchar(225) not null unique
);
~~~
我們創建 Customer.java 來做映射，其中標有 **@** 符號的在Spring Boot 中被稱作 Annotation，時間有限我們不用詳細理解裡面的原理，只要會用就可以（當然好奇可以ChatGPT問，但你問我，我必答不出來^_^）

只要記得在每個放在 model 中的類都必須是數據庫中有的 Table，attributes 需要相互對應，並且這些 Annotation 都要加上即可：
~~~ java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    // 添加這個 annotation customerId 就是自增的了
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String password;
    private String email;
}
~~~
### 2. repositories
在講 repositories 之前我需要先說明一下 Spring Data JPA 的用途，Spring Data JPA 是用來簡化我們的數據庫操作，可以根據方法名自動生成相應的SQL查詢，可以大大減輕我們的開發負擔。

我們說的 repository 其實就是用來封裝數據庫操作的，它只提供訪問數據庫的方法不直接暴露底層的數據庫實現細節，使業務邏輯不需要直接處理數據庫操作，而是通過定義好的接口進行交互。話不多說直接上代碼：
~~~ java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 通過 email 查詢 Customer
    Customer findCustomerByEmail(String email);
    // 通過 customer_id 查詢 Customer
    Customer findCustomerByCustomerId(Long customerId);
    // 通過 email 和 password 查詢 Customer
    Customer findCustomersByEmailAndPassword(String email, String password);
    // 通過 customer_id 更新 Customer
    Boolean updateCustomerByCustomerId(Customer customer);
    // 根據 customer_id 刪除 Customer
    Boolean removeCustomerByCustomerId(Long customerId);
}
~~~
首先要注意，在 repositories 目錄下只能放 interface，並且一定要加上 **@Repository** 的 Annotation 否則後續運行上會報錯。可以看到我們通過 extend 來使用 Spring Data JPA 的接口，其中 JpaRepository<Customer, Long> 中 Customer 是 model 中對應 Table 的 class，而 Long 則是主鍵的數據類型（注意！這裡指的是Java裡面定義的數據類型）。除了上述提提供的 function 還有很多其他的，可以根據需要自己摸索。
### 3. services
總算來到我們的業務邏輯層了，這一層好像也被叫做服務層，位於 repository 和 controller 之間，
### 4. controllers
### 5. dto

