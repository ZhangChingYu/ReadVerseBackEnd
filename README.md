# ReadVerse Server
這個文件會介紹我們的後端項目結構以及 RESTFul API 的相關說明。這裡也會詳細列出各種狀態碼所代表的涵意。 
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
在講 repositories 之前我需要先說明一下 Spring Data JPA 的用途，Spring Data JPA 是用來簡化我們的數據庫操作，可以根據方法名自動生成相應的SQL查詢，大大減輕我們的開發負擔。

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
總算來到我們的業務邏輯層了，這一層好像也被叫做服務層，位於 repository 和 controller 之間。Controller 調用 Service 層來處理複雜的業務邏輯，而 Service 層通過調用 Repository 層來進行數據庫操作。
其他層都很簡單就這一層最麻煩，因為這是所有功能真正實現的地方。馬上來看代碼吧：
~~~ java
@Service
public class AuthService {
    private CustomerRepository customerRepository;
    @Autowired
    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UserDto login(LoginDto data){
        if (data.getRole().equals("Customer")) {
            Optional<Customer> customer = customerRepository.findCustomerByEmail(data.getEmail());
            // 如果該 email 已被註冊過我們對密碼進行匹配
            if (customer.isPresent()) {
                // 如果密碼正確，我們返回登入成功的信息給前端
                if (data.getPassword().equals(customer.get().getPassword())) {
                    UserDto user = UserDto.builder()
                            .status("200")
                            .message("Login Success")
                            .data(UserData.builder()
                                    .email(customer.get().getEmail())
                                    .role(data.getRole())
                                    .build())
                            .build();
                    return user;
                }
                // 如果密碼錯誤，我們返回密碼錯誤的信息給前端
                else {
                    UserDto user = UserDto.builder()
                            .status("400")
                            .message("Wrong Password")
                            .data(null)
                            .build();
                    return user;
                }
            }
            // 如果該用戶不存在，我們返回用戶不存在的信息給前端
            else {
                UserDto user = UserDto.builder()
                        .status("404")
                        .message("User Not Found")
                        .data(null)
                        .build();
                return user;
            }
        }
        return null;
    }
}
~~~
這個例子中我們實現了 Customer 登入的功能，可以看到我們在 class 名前也加上了 Annotation，表示這個類是一個 Service 層的類。不用懷疑，不加 Annotation 就是會報錯。你們現在一定很好奇 **LoginDto** 和 **UserDto** 是什麼吧？不用及，現在只要知道是兩個自定義的數據結構就可以了。
### 4. controllers
controller 顧名思義就是控制層啦～呼～快說完了總算可以鬆一口氣。負責處理使用者輸入的請求，協調 View 和 Model 之間的互動。 Controller 從 View 接收使用者的輸入，呼叫 Service 層的業務邏輯，取得資料後再傳遞給 View 進行展示。來看看代碼吧：
~~~ java
@RestController
public class AuthController {
    // Gson 是用來將數據轉換為 Json 格式的工具
    private Gson gson = new Gson();
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // 我們將 login 操作定義為前端的[Get]請求，所以使用 @GetMapping() 註釋
    @GetMapping("login")
    public String Login(@RequestBody LoginDto data){
        UserDto user = authService.login(data);
        return gson.toJson(user);
    }
}
~~~
**@GetMapping("login")** 註釋代表的就是當前端發送請求到 "http://localhost:8080/login"
時會後端會調用這個 Login() 方法。**@RequestBody** 代表前端數據是放在 Body 裡以 Json 的形式傳送給後端的。我們通過 LoginDto 來接收前端發送來的數據並作為查詢的輸入，最終返回登入結果轉成 Json 格式返回給前端。

當然前端傳送數據的方式不只一種，這裡只是舉了一個例子，具體可以參考 https://blog.csdn.net/qq_25305833/article/details/115394226 裡面羅列了一些根據前端傳送數據的方式不同，後端對應的接收方式。
### 5. dto
現在來到了神秘的 dto。dto 是用於在不同層之間傳遞資料的對象。通常用於將資料從 Server 傳輸到客戶端或從客戶端到 Server。它是一個純資料對象，通常不包含任何業務邏輯或複雜行為，只是簡單地封裝資料。它主要作用是為了簡化傳輸的資料結構，將資料庫或業務層的複雜對象轉換為適合傳輸的對象。

在上述的例子中曾出現的 **LoginDto** 和 **UserDto** 一個是客戶端傳送到 Server 的數據結構，另一個是 Server 傳送到客戶端的數據結構。他們兩個的代碼如下：
~~~ java
@Data
@Builder
public class LoginDto {
    private String email;
    private String password;
    private String role;
}
~~~
~~~ java
@Data
@Builder
public class UserDto {
    private String status;
    private String message;
    private UserData data;
    
    class UserData {
        private String email;
        private String role;
    }
}
~~~

## HTTP 狀態碼說明
HTTP 狀態碼是服務器用來表示請求處理結果的數字代碼。通常分為五個類別，在這個項目中我們只需要關注 2XX 和 4XX 即可：

- **1xx**：信息性狀態碼
- **2xx**：成功狀態碼
- **3xx**：重定向狀態碼
- **4xx**：客户端錯誤狀態碼
- **5xx**：服務器錯誤狀態碼

## 1xx - 信息性狀態碼

| 狀態碼 | 狀態信息           | 描述   |
|--------|----------------|------|
| 100    | Continue       | 繼續請求 |
| 101    | Switching Protocols | 切換協議 |

## 2xx - 成功狀態碼

| 狀態碼 | 狀態信息           | 描述            |
|--------|----------------|---------------|
| 200    | OK             | 請求成功          |
| 201    | Created        | 請求成功並創建了新的資源  |
| 202    | Accepted       | 請求已接受，但尚未處理   |
| 204    | No Content     | 請求成功，但不返回任何内容 |

## 3xx - 重定向狀態碼

| 狀態碼 | 狀態信息           | 描述              |
|--------|----------------|-----------------|
| 301    | Moved Permanently | 永久移動，資源的新位置     |
| 302    | Found          | 臨時移動，資源的新位置     |
| 304    | Not Modified   | 資源未修改，可以使用緩存的版本 |

## 4xx - 客户端錯誤狀態碼

| 狀態碼 | 狀態信息           | 描述           |
|--------|----------------|--------------|
| 400    | Bad Request    | 請求無效或格式錯誤    |
| 401    | Unauthorized   | 需要身份驗證       |
| 403    | Forbidden      | 服務器拒絕請求      |
| 404    | Not Found      | 資源未找到        |
| 409    | Conflict       | 請求與當前資源的狀態衝突 |

## 5xx - 服務器錯誤狀態碼

| 狀態碼 | 狀態信息           | 描述      |
|--------|----------------|---------|
| 500    | Internal Server Error | 服務器内部錯誤 |
| 502    | Bad Gateway    | 錯誤的網關   |
| 503    | Service Unavailable | 服務不可用   |
| 504    | Gateway Timeout | 網關超時    |

