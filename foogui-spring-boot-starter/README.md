# Alibaba-code
## 自定义一个starter

1. 将自动配置类DemoStarterAutoConfiguration利用SPI注入
2. @EnableConfigurationProperties和@ConfigurationProperties配合使用，相当于从yaml中读取了信息并创建了配置类的Bean
3. 利用条件装配和自定义注解对start进行开关
