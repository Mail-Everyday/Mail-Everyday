## [팀 프로젝트] MailEveryday
메일 모니터링 웹서비스로  
특정 키워드를 포함한 메일을 수신하였을 때, 사용자에게 알림 메시지를 발송해주는 서비스
## 프로젝트 인원 (3명)  
* 고윤제
* 김민관
* 김성수

## 참여 영역
**기간: 2022.07.12 - 2022.08.22**  
**작업 영역:**
* OAuth 2.0을 이용한 로그인 관리 Api
  * 기본 프론트엔드 페이지 구현
  * UserEmail, UserKeyword 엔티티 설계
  * 토큰 발급 (갱신 포함) 및 유저 정보 획득을 위한 Google OPEN API 콜
  * 테스트 코드 작성
* 사용자 Keyword에 대한 CRUD 구현 
  * 프론트 페이지 구현 및 ajax를 이용하여 백엔드로 요청 전송
  * 페이지 접속권한, 보안관련 이슈, 예외사항 처리
  * 테스트 코드 작성
* 메일 모니터링 서비스 구현
  * MonitoringService와 EmailHandler로 구현
* 메시지 발송 서비스 구현
  * DB에 핸드폰번호 컬럼 추가 및 CRUD 구현 
  * 네이버 클라우드 API를 이용하여 사용자에게 알림메시지 발송 (진행 중) 

## 기술 스택
* 스프링부트 2.7.2 (Gradle Project) 
* Openjdk8
* AWS RDS Database (MariaDB)

## 개발 환경
* IntelliJ Ultimate Edition 2022.1.2
* Postman
* Sequel Pro
