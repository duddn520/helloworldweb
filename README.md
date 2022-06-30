# Hello World - 개발자 커뮤니티
<img src="https://img.shields.io/badge/Intellij-2021.1-orange"/> <img src="https://img.shields.io/badge/react-18.0.0-green"/>


<!-- [![NPM Version][npm-image]][npm-url]
[![Build Status][travis-image]][travis-url]
[![Downloads Stats][npm-downloads]][npm-url] -->

http://3.37.52.109

Stack Overflow와 같은 IT관련 커뮤니티를 구축하는 Team Project입니다.

IT관련 질문들을 작성하고 서로의 아이디어와 지식을 공유하고 자신만의 공간에 기록하고 저장합니다. 

![](../header.png)

## 설치 방법

Front-end:

```sh
./frontend
npm install
npm start
```

<!-- Back-end:

```sh
edit autoexec.bat
```
 -->
## 사용 예제

<img width="1430" alt="스크린샷 2022-05-13 오후 3 00 01" src="https://user-images.githubusercontent.com/73530944/168220660-3df13b66-54d9-49b3-a6ae-98d9e18bd767.png">

<!-- 
스크린 샷과 코드 예제를 통해 사용 방법을 자세히 설명합니다.

_더 많은 예제와 사용법은 [Wiki][wiki]를 참고하세요._ -->

## 개발 환경 설정

모든 개발 의존성 설치 방법과 자동 테스트 슈트 실행 방법을 운영체제 별로 작성합니다.
* IntelliJ 
```sh
Sync Gradle Settings in ./build.gradle
```

* DB
1. MySQL Workbench (Connenctions to AWS Remote DB)
```sh
You need an endPoint from admin
```
2. h2 Database (Test)
```sh
You can use your own local db server
```


## 업데이트 내역

* 0.0.1
    * 프로젝트 진행 중
    * [@DB_Scheme](https://round-nose-fe5.notion.site/DB-Domain-Scheme-31cbe25367224dfc8ae7871f23a4c9ff)

## 정보

김영우 – [@깃허브 주소](https://github.com/duddn520) – duddn520@naver.com

공진우 - [@깃허브 주소](https://github.com/beanzinu) - wlsdn1372@naver.com

허지훈 - [@깃허브 주소](https://github.com/ys05143) - ys05143@naver.com



## 기여 방법

1. (<https://github.com/yourname/yourproject/fork>)을 포크합니다.
2. (`git checkout -b yourname`) 명령어로 새 브랜치를 만드세요.
3. (`git commit -am 'Add some yourcodes'`) 명령어로 커밋하세요.
4. (`git push origin yourname`) 명령어로 브랜치에 푸시하세요. 
5. 풀리퀘스트를 보내주세요.

### commit convention
add : 새로운 기능 혹은 코드를 추가할 경우  
style : 스타일 추가 혹은 변경  
fix: 기존코드에 버그수정 및 수정 된 기능이 있을경우  
rename : 파일이름 및 명시된 이름이 변경 되었을 경우  
remove : 코드가 삭제 된 경우사용  
delete : 파일이 삭제된 경우  
refactor : 코드를 전면수정한 경우  
move : 파일이 다른 디렉토리로 이동했을 경우 



<!-- Markdown link & img dfn's -->
[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki
