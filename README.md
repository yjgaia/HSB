HSB - Han Sol Board (high scalability bulletin board system)
============================================================

HSB 프로젝트는 SW 마에스트로 3기 연수생인 심영재, 이한솔, 김민환 군이 팀을 이루어 진행하는 NoSQL 솔루션을 활용한 대용량 커뮤니티 시스템 개발 프로젝트 입니다.

개발 목적
---------
대표적인 NoSQL 솔루션인 Redis와 Couchbase를 활용하여 트위터와 같은 대용량 커뮤니티(SNS) 시스템을 개발하고, Open API 형태로 서비스를 제공합니다. 추가적으로, 개발된 API를 가지고 하나의 예시로써 하이브리드 앱을 제작해 봅니다.

기반 시스템
-----------
- CentOS 6.2
- jdk1.7.0_04
- Spring ROO 1.1.5.RELEASE
- 서버는 j2ee 기반의 모든 서버에서 작동합니다.
- RDBMS는 MySql, Oracle Database 등 Hibernate가 제공하는 대다수의 시스템에서 작동합니다.
- Redis 2.4.17
- Couchbase 1.8.1

기반 프레임워크
---------------
- Spring Framework 3.1.1.RELEASE
- Spring MVC 3.1.1.RELEASE
- Spring Roo 1.1.5.RELEASE
- Hibernate 3.6.4.Final
- Jackson 1.9.9
- XStream 1.4.2

라이센스
--------
- MPL

> MPL은 넷스케이프(Netscape)사가 개발한 모질라(Mozilla) 브라우즈의 소스코드를 공개하는데 사용한 라이센스로서 "Mozilla Public License"의 약어이다. MPL라이센스는 소스코드와 실행파일을 불리하여 이 둘을 보완하여 만든 것이다. 먼저 소스코드 측면에서는 소스코드는 반드시 공개되어야 하며 소스코드를 수정하였을 경우에는 최초의 저작자에게 수정한 내용을 통지해야 한다. 실행파일 측면에서는 실행파일 자체를 독점라이센스로 배포할 수 있다. 즉, 저작자의 이익을 보호할 뿐 아니라, 수정, 보완된 소프트웨어의 배포를 통한 상업적인 이익을 보호할 수 있으며 또한 적정한 가격을 요구할 수 있고, 불법복제에 대해 제재를 가할 수도 있다. 결국 이 소프트웨어를 더욱 보완, 발전시키려는 개발자들의 이익을 보호할 수 있게 된다. 즉, 기술적으로 개선을 할 경우, 코드를 보고 수정한 후, 컴파일하여 새로운 독창적인 버전으로 재배포할 수 있다.

설치방법
--------
1. github 주소(https://github.com/Hanul/HSB.git)를 이용하여 이클립스에서 프로젝트를 checkout 합니다.
* src/main/resources/META-INF/spring/applicationContext.xml의 Couchbase server connector 세팅을 본인의 설정에 맞게 변경합니다.
* src/main/resources/META-INF/spring/applicationContext.xml의Redis Server 세팅을 본인의 설정에 맞게 변경합니다.
* src/main/resources/META-INF/spring/database.properties를 본인의 설정에 맞게 변경합니다.
* 기반 시스템을 갖추고 실행합니다.

사용법
------
- 아래 REST URL을 사용하여 Method와 필요한 파라미터를 전송한 뒤 JSON 형식의 데이터를 반환받아 사용합니다.

REST URL
--------
- /user/auth (POST) 로그인

> 필요한 파라미터 목록
> - username
> - password
> 반환되는 JSON (예제)
> "`
{
    "success": true,
    "single": true,
    "data": {
        "username": "test",
        "generatedSecureKey": "1f507f23c0d7c4c5dac3ad8b74f66d43c33084a6"
    }
}
"`

- /user/auth (DELETE, 인증필요) 로그아웃
- /user/account (POST) 회원가입
- /user/account (PUT, 인증필요) 회원 정보 수정
- /user/account (DELET, 인증필요) 회원 정보 삭제 (탈퇴)
- /user/timeline (GET, 인증필요) 타임라인
- /{username} (GET) 유저의 글 목록 보기
- /{username}/info (GET) 유저 정보 보기
- /{username} (POST, 인증필요) 글쓰기
- /{username}/follow (POST, 인증필요) 팔로우하기
- ‎/{username}/follow (DELETE, 인증필요) 언팔로우
- /{username}/following (GET) 팔로잉 목록 보기
- /{username}/followers (GET) 팔로우 하는 사람 목록 보기
- /article/{id} (DELETE, 인증필요) 글삭제
- /article/{id}/comments (GET) 댓글 목록
- /article/{id}/comment (POST, 인증필요) 댓글달기
- /comment/{id} (DELETE, 인증필요) 댓글삭제

관련 링크
---------
- https://github.com/Hanul/HSB
- http://redis.io
- http://www.couchbase.com
- http://swmaestro.kr