package member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//�������̽� process �� �����Ѵ�.
public interface CommandHandler {
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception;
}