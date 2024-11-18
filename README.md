![제목을 입력해주세요_-003 (2)](https://github.com/user-attachments/assets/4f52570f-03ea-4160-b037-3d4538f83662)

## 프로젝트 소개
- 개발 기간 | 2024.11.06 - 2024.11.19
- 사이트 URL | 
- 배달 및 포장 음식 주문 관리 플랫폼입니다. 
</br>

## 팀원 역할분담

  | 역할    | 팀원 이름 | 주요 업무                  |
  |------------|--------| -------------------------- |
  | 팀장, 백엔드 개발 | 이건우 | 회원/북마크 API 설계 및 구현, 서버 배포   |
  | 백엔드 개발 | 전혜리 | 가게/카테고리/메뉴 API 설계 및 구현, AI API 연동|
  | 백엔드 개발 | 윤영식 | 리뷰/결제/북마크 API 설계 및 구현 |
  | 백엔드 개발 | 양예린 | 장바구니/주문 API 설계 및 구현    |
  </br>

## 기술 스택
  ![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white) ![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=flat-square&logo=amazonaws&logoColor=white) ![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=flat-square&logo=amazons3&logoColor=white) ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white) ![Google AI API](https://img.shields.io/badge/Google%20AI%20API-4285F4?style=flat-square&logo=google&logoColor=white) ![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)
  
</br>

## ERD
![스크린샷 2024-11-18 162430](https://github.com/user-attachments/assets/d8f8bd36-6baa-4495-b973-9665d8a940fd)
</br>

## System Architecture
![image](https://github.com/user-attachments/assets/6566503b-ae83-4b95-a7b6-b55fe53a8ecd)
</br>

## API docs
- API 명세서 Notion 문서
- https://teamsparta.notion.site/API-18d16d2d6c9e46a59361e21bc286b560

</br>

## 서비스 기능

### 회원 기능
- [X] 회원가입
  - 이메일 및 비밀번호로 회원가입 진행
  - 
- [X] 로그인

### 카테고리 기능
> 가게에 등록되는 카테고리를 관리하는 기능
- [X] 카테고리 등록
  - 로그인 한 User 중 MANAGER, MASTER 권한만 카테고리 등록이 가능
- [X] 카테고리 수정
  - 로그인 한 User 중 MANAGER, MASTER 권한만 카테고리 수정이 가능
- [X] 카테고리 삭제
  - 로그인 한 User 중 MANAGER, MASTER 권한만 카테고리 삭제가 가능
  - category 에 stores 가 비어있지 않다면 삭제 불가

### 가게 기능
> 가게를 관리하는 기능
- [X] 가게 등록
  - 로그인 한 User 중 MANAGER, MASTER 권한만 가게 등록이 가능
  - 가게 등록 시 가게에 들어가는 userId는 권한이 OWNER인 userId 만 허용
- [X] 가게 조회
  - 반환값 - 가게이름, 카테고리이름, 평균 별점, 리뷰 개수, 메뉴5개(메뉴이름, 메뉴가격, 메뉴이미지)
  - keyword 또는 categoryName 이 존재하지 않으면 검색이 불가능
  - keyword 검색 시 가게이름, 메뉴이름, 메뉴설명, 카테고리이름에 keyword가 포함된 가게 반환
  - category 검색 시 해당 카테고리 가게 반환
  - category & keword 검색 시 category가 일치하고 가게이름, 메뉴이름, 메뉴설명, 카테고리이름에 keyword가 포함된 가게 반환
- [X] 가게 상세 조회
  - 반환값 - 가게 이름, 사업자 이름, 가게 위치, 영업 여부, 카테고리 이름, 평균 별점, 리뷰 개수, 메뉴 목록, 리뷰 목록
  - 가게주인 OR MANAGER, MASTER(숨김 처리 메뉴 포함)
    - 조회 시 해당 가게에 포함된 메뉴 목록 반환
    - keyword 검색 시 메뉴설명, 메뉴옵션이름에 keyword가 해당 가게에 포함된 메뉴 목록 반환
  - 일반 유저 (숨김 처리 메뉴 불포함)
    - 조회 시 해당 가게에 포함된 메뉴 목록 반환
    - keyword 검색 시 메뉴설명, 메뉴옵션이름에 keyword가 해당 가게에 포함된 메뉴 목록 반환
- [X] 가게 수정
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 가게 수정이 가능
- [X] 가게 삭제
  - 로그인 한 User 중 MANAGER, MASTER 권한만 가게 삭제가 가능
  
      
### 메뉴 기능
> 가게의 메뉴를 관리하는 기능
- [X] 메뉴 등록
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 메뉴 등록 가능
- [X] 메뉴 상세 페이지 조회
  - 일반유저 - 메뉴 정보, 메뉴 옵션 목록 반환
  - MANAGER,MASTER or 가게소유자 - 메뉴 정보, 메뉴 옵션, AI 질문&답변 반환
- [X] 메뉴 수정
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 메뉴 수정 가능
- [X] 메뉴 삭제
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 메뉴 삭제 가능
- [X] 메뉴 옵션 추가
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 메뉴 옵션 등록 가능
- [X] 메뉴 옵션 수정
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 메뉴 옵션 수정 가능
- [X] 메뉴 옵션 삭제
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 메뉴 옵션 삭제 가능
- [X] 메뉴 설명 AI 요청
  - 로그인 한 User 중 MANAGER, MASTER 권한 또는 가게 소유자만 AI 요청 가능

### 장바구니 기능
> 원하는 가게의 메뉴와 옵션들을 선택해 주문 전 미리 담아두는 기능
- [X] 장바구니 생성 및 조회
  - 로그인 한 모든 권한의 유저는 생성 가능
    - 점주라면 자신의 가게 주문만 생성 가능
    - 메뉴 활성화 여부를 확인하여 주문가능 한 메뉴만 담을 수 있음.
    - 먼저 담긴 장바구니가 있다면 가게를 비교하여 동일 가게 메뉴만 담을 수 있음.
  - 장바구니 목록 조회
    - 관리자는 장바구니 상태에 관계없이 모든 장바구니 목록 조회 
    - 고객과 점주는 자신의 장바구니만 조회가 가능하며 대기 상태의 주문이나 삭제처리가 되지않은 장바구니만 조회 
- [X] 장바구니 수정 및 삭제
  - 메뉴 수량과 선택 옵션 변경 가능
    - 대기 상태의 장바구니만 수정 가능
    - 고객과 점주는 자신의 장바구니만 수정 가능
  - 권한에 따라 장바구니 삭제 가능
    - 관리자는 모든 장바구니의 삭제가 가능
    - 고객과 점주는 자신의 대기상태의 장바구니만 삭제 가능
    - 삭제처리 된 장바구니는 삭제 상태값으로 변경
   
### 주문 기능
> 미리 담아 둔 장바구니 리스트를 토대로 주문하는 기능
- [X] 주문 등록 및 조회
  - 로그인 한 모든 권한의 유저는 주문 접수 가능
    - 장바구니 목록을 확인하여 대기 상태의 장바구니가 있을 때만 주문 가능
    - 점주는 자신의 가게의 주문만 가능
    - 장바구니 주문과 연결 및 상태 완료로 변경 
  - 권한에 따라 주문 목록 및 세부내역 조회, 검색
    - 관리자는 모든 주문 조회
    - 점주는 점주의 모든 가게 주문 조회
    - 고객은 자신의 주문만 조회
    - 원하는 키워드를 입력하여 가게명, 메뉴명에 키워드를 포함한 주문 목록 조회 
- [X] 주문 상태 변경 및 취소
  - 권한을 확인하여 고객을 제외한 점주와 관리자만 주문 상태 변경 가능
  - 주문 취소 
    - 시간을 체크하여 5분 이내의 주문만 취소 가능
    - 고객과 점주는 자신이 접수한 주문만 취소가 가능
    - 취소된 주문의 상태 변경, 관련 장바구니 상태 복귀 연결해제
      
### 리뷰 기능
- [X] 리뷰 등록
  - 로그인 한 점주, 고객만 생성 가능
  - 주문이 배달 완료 상태일때 리뷰 등록 가능
- [X] 리뷰 수정
  - 로그인 한 점주, 고객만 수정가능
- [X] 리뷰 전체 조회
  - 모든 권한 조회 가능
  - 삭제된 리뷰 제외한 전체 리뷰 조회
- [X] 내 리뷰 조회
  - 점주, 고객만 생성 가능
  - 삭제된 리뷰 제외한 내가 작성한 리뷰 조회
      
### 결제 기능
- [X] 결제 등록
  - 고객만 등록 가능
  - 주문이 준비중 상태일때 결제 가능
- [X] 결제 내역
  - 점주 및 고객일때 자신의 결제 내역 조회
  - 관리자일때 전체 결제 내역 조회
- [X] 결제 상세 내역
  - 결제 코드 및 가게 정보 조회

      
### 북마크 기능
- [X] 북마크 등록 및 취소
  - 모든 권한
  - 해당 가게에 대한 북마크 등록
  - 가게에 대한 북마크가 이미 있으면 삭제
- [X] 북마크 조회
  - 모든 권한
     
</br>
