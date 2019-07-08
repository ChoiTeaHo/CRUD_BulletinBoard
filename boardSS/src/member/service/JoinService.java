package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class JoinService {
	private MemberDao memberDao = new MemberDao();

	public void join(JoinRequest joinReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection(); // DB커넥션을 구한다.
			conn.setAutoCommit(false); // 트랜잭션을 시작한다.

			// 만약 여기서 해당 ID가 검색이 TRUE되어 MEMBER에 저장된다면 ID가 있다는 것이다.
			Member member = memberDao.selectById(conn, joinReq.getId());
			if (member != null) {
				JdbcUtil.rollback(conn); // 가입하려는 ID에 해당하는 데이터존재시 Rollback 수행
				throw new DuplicateIdException(); // 듀플리케이션이셉션.class로 보냄
			}

			// member 객체에 아무것도 없다면 정상적으로 [데이터삽입] 시작.
			memberDao.insert(conn, new Member(joinReq.getId(), joinReq.getName(), joinReq.getPasswrod(), new Date()));
			conn.commit();

		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
