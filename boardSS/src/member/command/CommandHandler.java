package member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//인터페이스 process 를 강제한다.
public interface CommandHandler {
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception;
}