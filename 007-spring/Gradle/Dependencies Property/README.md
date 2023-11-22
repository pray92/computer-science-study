# Dependencies Property

## Dependencies Block
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```
- 프로젝트 구성을 위해 받아와야 할 라이브러리를 정의하는 공간

### 각 키워드의 차이

- 라이브러리 앞에 붙은 `implementation`, `testImplmentation`, `debugImplementation`은 각 라이브러리가 적용될 범위를 의미
  - `implementation`: 전 범위
  - `testimplementation`: 테스트

| Scope | Keyword |
| --- | --- |
| compile | `implementation`, `api` |
| compile only | `compileOnly` |
| debug | `debugImplementation` |
| runtime | `runtimeOnly` |
| test | `testImplementation`, `testCompileOnly`, `testRunTimeOnly` |

# Reference

[[Gradle] build.gradle의 dependencies 블록 한 번에 정리하기. implementation, testImplementaion의 차이와 라이브러리 구성](https://kotlinworld.com/316)
