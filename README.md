# 템플릿 엔진 만들기

난이도는 상을 선택하여 개발했습니다.

## 프로젝트 개요

사용자 데이터를 JSON 파일에서 읽어와 요구사항에 제시된 템플릿 문법을 기반으로 정보를 출력하는 템플릿 엔진입니다. 
템플릿과 데이터 파일을 입력으로 받아 output.txt 파일에 결과를 출력합니다.

## 주요 기능

- **템플릿 파일과 데이터 파일 읽기**: 템플릿과 데이터는 각각 파일로 제공되며, 이를 읽어들여 처리합니다.
- **템플릿 데이터 바인딩**: 템플릿 내의 문법을 분석해 값을 채워넣습니다.
- **결과 파일 출력**: 처리된 템플릿을 `output.txt` 파일에 출력합니다.

## 사용 기술 스택

- **언어**: Java 21
- **빌드 도구**: Gradle 8.5
- **JSON 파싱 라이브러리**: Jackson 2.13.4.2

## 설치 및 실행 방법

**프로그램 실행**

```sh
java -jar build/libs/<jar-file-name>.jar <template-file> <data-file>
```
템플릿 파일과 데이터 파일의 순서는 상관 없습니다.

예시:

```sh
java -jar build/libs/template-engine.jar template1.txt data.json
```

- 템플릿 파일과 데이터 파일 경로를 제공하지 않으면, 기본적으로 JAR 파일이 실행된 경로에서 `data.json`과 `template.txt` 파일을 찾습니다.
- 만약 해당 경로에도 파일들이 존재하지 않는다면, `resources` 디렉토리에 있는 기본 `data.json`과 `template.txt` 파일을 사용합니다.

## 프로젝트 구조
| 파일 경로                                                            | 설명                       |
|------------------------------------------------------------------|--------------------------|
| src/main/java/suhyeon/Main.java                                  | Main 클래스                 |
| src/main/java/suhyeon/exception/TemplateEngineException.java     | 템플릿 엔진 커스텀 Exception     |
| src/main/java/suhyeon/exception/TemplateEngineExceptionType.java | 템플릿 엔진 Exception 유형 Enum |
| src/main/java/suhyeon/token/EndForToken.java                     | "for" 구분의 종료 토큰          |
| src/main/java/suhyeon/token/ForToken.java                        | "for" 구문을 토큰             |
| src/main/java/suhyeon/token/TextToken.java                       | 템플릿 문법이 사용되지 않은 텍스트 토큰   |
| src/main/java/suhyeon/token/Token.java                           | 템플릿 문자열 토큰의 추상 클래스       |
| src/main/java/suhyeon/token/TokenType.java                       | 템플릿 문자열 토큰 유형 Enum       |
| src/main/java/suhyeon/token/Tokenizer.java                       | 템플릿 문자열 분석하여 토큰 목록으로 변환  |
| src/main/java/suhyeon/token/VariableToken.java                   | 변수 토큰                    |
| src/main/java/suhyeon/utils/FileUtils.java                       | 파일 읽기 및 쓰기 유틸리티 클래스      |
| src/main/resources/data.json                                     | 과제에 제공된 data.json 파일     |
| src/main/resources/template.txt                                  | 과제에 제공된 template.txt 파일  |
