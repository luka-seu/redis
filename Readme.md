## 遇到的问题：
1. Could not resolve placeholder：写清楚文件名，不要用*代替
2. 无法读取properties文件值：用<context:property-placeholder location="classpath:redis.properties"/>
3. spring-test: 
- @RunWith(SpringJUnit4ClassRunner.class)
- @ContextConfiguration("classpath:spring.xml")