# CORS(Cross-origin Resource Sharing)

## Cross-origin
- 다음 중 한가지라도 다른 경우를 말함
  1. `프로토콜`: `http`와 `https` 프로토콜은 다름
  2. `도메인`: `domain.com`과 `other-domain.com`은 다름
  3. `포트 번호`

## CORS 정의
- 브라우저에서는 보안적인 이유로 `Cross-origin HTTP 요청`들을 제한함
- 그래서 `Cross-origin` 요청을 하려면 서버의 동의가 필요
- 만약 서버가 동의하면 브라우저에서는 요청을 허락하고, 그렇지 않으면 브라우저에서 거절함
- 이러한 허락을 구하고 거절하는 매커니즘을 HTTP Header를 이용해서 가능함 => `CORS`

## CORS 필요성
- `CORS`가 없이 모든 곳에서 데이터를 요청할 수 있게 되면 다른 사이트에서 원래 사이트 흉내가 가능
  - 기존 사이트와 동일하게 동작하도록하여 사용자가 로그인하도록 유도
  - 로그인한 세션을 탈취하여 악의적으로 정보를 추출하거나 다른 사람의 정보를 입력하는 등 공격 가능
- 이러한 공격을 브라우저에서 보호하고, 필요한 경우에만 서버와 협의하여 요청할 수 있도록 하기 위해 필요

(추후 추가 내용 작성)

# Reference

[CORS란 무엇인가? – 한윤석 개발 블로그](https://hannut91.github.io/blogs/infra/cors)