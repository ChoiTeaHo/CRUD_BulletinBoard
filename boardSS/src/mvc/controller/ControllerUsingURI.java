package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.command.CommandHandler;
import member.command.NullHandler;

public class ControllerUsingURI extends HttpServlet {

	// <커맨드, 핸들러인스턴스> 매핑 정보 저장
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

	// ★★★★ 초기화 init() 메소드 ★★★
	public void init() throws ServletException {
		String configFile = getInitParameter("configFile"); // 값= /WEB-INF/commandHandlerURI.properties

		Properties prop = new Properties();

		String configFilePath = getServletContext().getRealPath(configFile); // 프로퍼티파일 절대경로생성

		try (FileReader fis = new FileReader(configFilePath)) { // 프로퍼티파일 스트림생성
			prop.load(fis);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		Iterator keyIter = prop.keySet().iterator();
		while (keyIter.hasNext()) {
			String command = (String) keyIter.next(); // 키 꺼내기 : /join.do
			String handlerClassName = prop.getProperty(command); // 키값 꺼내기: member.command.JoinHandler
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance(); // 인터페이스객체셍성
				commandHandlerMap.put(command, handlerInstance); // 키/값 ==> (join.do / JoinHandler 객체) Map객체에 삽입
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new ServletException(e);
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	/** ★★★★ process().. 웹브라우저 주소창에 입력한 내용이 request에 저장되어 전송된다. ★★★★ **/
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// .do로 입력하면 xml에 설정해놨기때문에 모든 *.do경로는 서블릿이 이곳코드를 실행한다.
		// 중요한점은 바로 join.do 로 입력한 것이 단순하게 이 서블릿을 실행하기 위함이라는 점이다. 그런데 연계된 부분이있다.
		// 연계된부분은 이러하다. 초기화단계에서 join.do 라는 키를 프로퍼티파일에 저장하고있었다.
		// (그리고 키는 그대로 키값 / 값을 클래스로변환하여 Map객체에 넣어놨었다.)
		// 이곳에서 요청한 URI 문자열을 잘라서 join.do 를 Key로 만든다.
		// 그리고 방금만든 Map객체에서 만든Key로 저장해둔 Value인 클래스객체를 리턴한다.
		// 이 클래스객체가 바로 JoinHandler.class 이다. 이녀석의 process() 메소드를 실행함으로써 뷰를 리턴한다.

		String command = request.getRequestURI();
		log("파워3" + command); // 출력= /board/join.do // join.do가 출력되는이유: 웹브라우저에 입력해서 실행했기때문
		log("파워3_1" + request.getContextPath()); // 출력= /board
		log("파워3_2" + command.indexOf(request.getContextPath())); // 출력= 0
		log("파워3_3" + command.substring(request.getContextPath().length())); // 출력= /join.do

		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length()); // board문자길이를 잘라서 join.do 만 추출하기
		}
		log("파워3" + command); // 출력= /join.do

		// 이렇게 분리한 이유의 핵심코드이다. if-else문을 사용하지 않아도 된다?. (new 객체 new 객체 new 객체..)
		CommandHandler handler = commandHandlerMap.get(command); // 초기화때 저장한 Map(join.do, JoinHandler객체)
		if (handler == null) { // 즉, 값인 JoinHandler 객체를 인터페이스에 대입해서 handler 생성.
			handler = new NullHandler();
		}

		String viewPage = null;
		try {
			viewPage = handler.process(request, response); // Joinhandler클래스 객체의 process 실행 (여기서 경로를리턴해준다)
			log("뷰페이지" + viewPage); // 출력= /WEB-INF/view/joinForm.jsp

		} catch (Throwable e) {
			throw new ServletException(e);
		}
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
}