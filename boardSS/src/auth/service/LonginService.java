package auth.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class LonginService {

	private MemberDao memberDao = new MemberDao();

	public User login(String id, String password) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Member member = memberDao.selectById(conn, id); // DB에 저장된 내용을 Member객체로 가져오기
			if (member == null) {
				throw new LoginFailException();
			}
			if (!member.matchPassword(password)) { // Member객체에 저장된 PW와 입력이들어온PW 비교
				throw new LoginFailException();
			}
			return new User(member.getId(), member.getName());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
