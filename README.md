# 로그프레소 센트리 SDK

## 개요

로그프레소 센트리는 원격 호스트에 설치되어 로그프레소 엔터프라이즈 서버로 데이터를 전송하거나 실시간 조회를 수행하는 에이전트입니다. 로그프레소 센트리는 자바 6 버전 이상의 환경에서 동작합니다.

서드파티 개발사에서는 araqne-log-api 인터페이스를 이용하여 커스텀 수집기를 개발하여 설치하거나, 센트리 확장 모듈을 개발하여 서버에서 센트리 호스트로 원격 메소드 호출을 실행할 수 있습니다.

이 저장소는 아래와 같이 구성됩니다.
* **logpresso-sentry-api**: 확장 모듈의 RPC 서비스를 정의하는데 필요한 인터페이스 API를 포함합니다.
* **logpresso-sentry-example**: hello RPC 구현 예제를 통해 기본적인 확장 기능 구현을 설명합니다.

## 예제 코드
로그프레소는 OSGi 애플리케이션 서버 환경에서 구동되며, iPOJO 기반의 컴포넌트 개발 방법론을 사용합니다. 확장 RPC 서비스를 구현하려면 SentryCommandHandler 서비스 인터페이스를 구현하고, @Component 및 @Provides 어노테이션을 이용하여 OSGi 서비스를 등록해야 합니다.

```
package org.logpresso.sentry.example;

@Component(name = "sentry-hello-plugin")
@Provides
public class HelloCommandHandler implements SentryCommandHandler {
	@Override
	public Collection<String> getFeatures() {
		return Arrays.asList("hello");
	}

	@SentryMethod
	public String hello(String name) {
		return "hello, " + name;
	}
}

```

SentryCommandHandler.getFeatures() 메소드는 이 서비스에서 제공하는 기능 (feature) 식별자 집합을 반환합니다. 로그프레소 엔터프라이즈 서버는 이 메소드 호출을 통해 센트리에서 제공하는 기능을 자동으로 식별합니다.

원격 호출을 허용하는 메소드는 명시적으로 @SentryMethod 어노테이션을 지정해야 합니다. 서버에서 센트리 RPC 메소드 호출 시 매개변수의 갯수 및 타입이 일치해야 합니다. 매개변수 및 반환값은 다음의 타입을 사용할 수 있습니다: null, Boolean, Short, Integer, Long, Float, Double, String, Date, Inet4Address, Inet6Address, Map, List, byte[]

## 빌드
메이븐 및 JDK 6 이상의 버전이 설치되어 있다면, 프로젝트 최상위 디렉터리에서 아래와 같이 명령을 실행합니다.
```
$ mvn clean install
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO]
[INFO] Logpresso Sentry SDK
[INFO] Logpresso Sentry API
[INFO] Logpresso Sentry Example
...
```

## 설치 및 테스트

센트리 쉘에 텔넷 혹은 SSH로 접속 후, 아래와 같이 확장 번들을 설치합니다.
```
araqne> bundle.install file:///logpresso-sdk-sentry/logpresso-sentry-example/target/logpresso-sentry-example-1.0.0.jar
bundle [20] loaded

araqne> bundle.start 20
bundle 20 started.
```

이제 로그프레소 엔터프라이즈 서버 쉘에 텔넷 혹은 SSH로 접속 후, 아래와 같이 테스트합니다. base.call 명령을 통해 간단한 원격 메소드 호출을 테스트할 수 있습니다.
```
araqne> base.list
Connected Sentry List
-------------------------
guid=demo, remote=/127.0.0.1:55192

araqne> base.call
Description

        call sentry method

Arguments

        1. guid: the guid of sentry  (required)
        2. method: the rpc method name (required)
        3. arguments: string arguments (optional)

araqne> base.call demo hello logpresso
hello, logpresso
```
