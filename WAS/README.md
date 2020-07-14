빌드환경 
- jdk 1.8, maven , eclipse 

구현여부 
1. Http/1.1의 Host 헤더를 해석하세요. 
	- localhost, 127.0.0.1 이 2개 host로 확인 
	- Request의 Host 프로퍼티를 해석

2. 다음 사항을 설정 파일로 관리하세요.
	- Json Format으로 구현했습니다. (server.properties)
 
3. 403, 404, 500 오류를 처리합니다.
	- 요청한 url에 해당하는 서블릿이 없을때 404
	- .exe 확장자를 실행하려 할때 403
	- 그 외 서버에러 500
	- server.properties 에 정의한 VirtualHost별로 다른 에러 파일을 반환합니다.

4. 다음과 같은 보안 규칙을 둡니다.
	- HTTP_ROOT 디렉터리의 상위 디렉터리에 접근할 때, 확장자가 .exe 파일을 요청했을때 403 에러 반환하도록 규칙 추가했습니다.

5. logback
    - 하루 단위 분리 및 에러시 logger.error 사용하도록 추가했습니다. (logback.xml)


6. 간단한 WAS 를 구현합니다.
	- 예제코드와 동일하게 Thread Pool을 활용한 WAS를 구현했습니다.
	- Socket은 RequestProcessor 별로 (1:1) 처리됩니다.
	- 요청, 응답 처리 순서는 RequestProcessor-> RequestHandler -> ServletHandler -> ResponseHandler -> SimpleServlet 입니다.
	- RequestHandler에서 HttpRequest를 생성하고, 헤더 및 요청 값을 분석합니다.
	- ServletHandler에서는 HttpResponse를 생성하고, 사용할 Servlet을 결정합니다.
		+ 서버작업 간 내부 에러 발생시 ErrorCode 500으로 ErrorServlet을 사용합니다. 
		+ 404, 403도 해당 ErrorCode 로 ErrorServlet을 사용합니다.
	- ResponseHandler 는 위에서 결정된 Servlet의 service()를 호출합니다.

7. 현재 시각을 출력하는 SimpleServlet 구현체 작성
	- 추가 했습니다. http://localhost/NowTime 으로 확인가능 합니다.

감사합니다.