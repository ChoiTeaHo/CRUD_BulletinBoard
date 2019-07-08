package member.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.JoinRequest;
import member.service.JoinService;

public class JoinHandler implements CommandHandler { // CommandHandler 구현 process()를 강제한다.

	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	private JoinService joinService = new JoinService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (req.getMethod().equalsIgnoreCase("GET")) { // 요청방식이 GET방식이면 processForm()실행
			System.out.println("####조인핸들러");
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) { // POST방식이면 processSubmit()실행
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 둘다아니면 405응답코드를 전송
			return null;
		}
	}

	// GET방식
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW; // 단순하게 폼을 보여주는 뷰경로를 리턴한다.
	}

	// Post방식
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		JoinRequest joinReq = new JoinRequest(); // 폼에 입력한데이터들을 받아서 JoinRequest 객체를생성하여 저장해놓는다.
		joinReq.setId(req.getParameter("id"));
		joinReq.setName(req.getParameter("name"));
		joinReq.setPasswrod(req.getParameter("password"));
		joinReq.setConfirmPassword(req.getParameter("confirmPassword"));

		Map<String, Boolean> errors = new HashMap<>(); // 에러정보를 담을 맵객체를 생성하고 errors 속성에 저장한다.
		req.setAttribute("errors", errors);

		joinReq.validate(errors); // JoinRequest에서 객체의 값이 올바른지 검사하고 올바르지않으면 error맵객체에 키를추가함.
		System.out.println("에러사이즈" + errors.size());

		// -----------------------------------
		Set<String> keySet = errors.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();

			System.out.println(key);
		}
		// -----------------------------------

		// 실패시 (에러맵객체 1개라도 존재시)
		if (!errors.isEmpty()) { // 맵객체에 데이터존재시 폼뷰경로를 리턴한다. errors에 데이터 가 존재한다는것은 joinReq객체의
									// 데이터가 바르지않다는것이다. 즉, errors에 에러와관련된 키를 추가했다는것을 의미한다.
			return FORM_VIEW;
		}

		// 성공시 (석세스jsp 경로리턴)
		try {
			joinService.join(joinReq); // JoinSrvice(서비스클래스)의 join실행 select insert 등을 수행한다.
			return "/WEB-INF/view/joinSuccess.jsp";
		} catch (Exception e) {
			errors.put("duplicateId", Boolean.TRUE); // JoinSrvice(서비스객체)에서 ID가존재하면
			return FORM_VIEW; // DuplicateIdException.class를 실행한다. 즉, 이후 여기블록이로 돌아와
		} // erros맵객체에 duplicatedId 라는 키를 추가하고 폼이있는 뷰로 경로를 리턴한다.

	}
}
