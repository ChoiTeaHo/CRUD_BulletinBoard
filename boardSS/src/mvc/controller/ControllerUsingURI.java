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

	// <Ŀ�ǵ�, �ڵ鷯�ν��Ͻ�> ���� ���� ����
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

	// �ڡڡڡ� �ʱ�ȭ init() �޼ҵ� �ڡڡ�
	public void init() throws ServletException {
		String configFile = getInitParameter("configFile"); // ��= /WEB-INF/commandHandlerURI.properties

		Properties prop = new Properties();

		String configFilePath = getServletContext().getRealPath(configFile); // ������Ƽ���� �����λ���

		try (FileReader fis = new FileReader(configFilePath)) { // ������Ƽ���� ��Ʈ������
			prop.load(fis);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		Iterator keyIter = prop.keySet().iterator();
		while (keyIter.hasNext()) {
			String command = (String) keyIter.next(); // Ű ������ : /join.do
			String handlerClassName = prop.getProperty(command); // Ű�� ������: member.command.JoinHandler
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance(); // �������̽���ü�ļ�
				commandHandlerMap.put(command, handlerInstance); // Ű/�� ==> (join.do / JoinHandler ��ü) Map��ü�� ����
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

	/** �ڡڡڡ� process().. �������� �ּ�â�� �Է��� ������ request�� ����Ǿ� ���۵ȴ�. �ڡڡڡ� **/
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// .do�� �Է��ϸ� xml�� �����س��⶧���� ��� *.do��δ� ������ �̰��ڵ带 �����Ѵ�.
		// �߿������� �ٷ� join.do �� �Է��� ���� �ܼ��ϰ� �� ������ �����ϱ� �����̶�� ���̴�. �׷��� ����� �κ����ִ�.
		// ����Ⱥκ��� �̷��ϴ�. �ʱ�ȭ�ܰ迡�� join.do ��� Ű�� ������Ƽ���Ͽ� �����ϰ��־���.
		// (�׸��� Ű�� �״�� Ű�� / ���� Ŭ�����κ�ȯ�Ͽ� Map��ü�� �־������.)
		// �̰����� ��û�� URI ���ڿ��� �߶� join.do �� Key�� �����.
		// �׸��� ��ݸ��� Map��ü���� ����Key�� �����ص� Value�� Ŭ������ü�� �����Ѵ�.
		// �� Ŭ������ü�� �ٷ� JoinHandler.class �̴�. �̳༮�� process() �޼ҵ带 ���������ν� �並 �����Ѵ�.

		String command = request.getRequestURI();
		log("�Ŀ�3" + command); // ���= /board/join.do // join.do�� ��µǴ�����: ���������� �Է��ؼ� �����߱⶧��
		log("�Ŀ�3_1" + request.getContextPath()); // ���= /board
		log("�Ŀ�3_2" + command.indexOf(request.getContextPath())); // ���= 0
		log("�Ŀ�3_3" + command.substring(request.getContextPath().length())); // ���= /join.do

		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length()); // board���ڱ��̸� �߶� join.do �� �����ϱ�
		}
		log("�Ŀ�3" + command); // ���= /join.do

		// �̷��� �и��� ������ �ٽ��ڵ��̴�. if-else���� ������� �ʾƵ� �ȴ�?. (new ��ü new ��ü new ��ü..)
		CommandHandler handler = commandHandlerMap.get(command); // �ʱ�ȭ�� ������ Map(join.do, JoinHandler��ü)
		if (handler == null) { // ��, ���� JoinHandler ��ü�� �������̽��� �����ؼ� handler ����.
			handler = new NullHandler();
		}

		String viewPage = null;
		try {
			viewPage = handler.process(request, response); // JoinhandlerŬ���� ��ü�� process ���� (���⼭ ��θ��������ش�)
			log("��������" + viewPage); // ���= /WEB-INF/view/joinForm.jsp

		} catch (Throwable e) {
			throw new ServletException(e);
		}
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
}