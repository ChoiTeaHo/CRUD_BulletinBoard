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
			conn = ConnectionProvider.getConnection(); // DBĿ�ؼ��� ���Ѵ�.
			conn.setAutoCommit(false); // Ʈ������� �����Ѵ�.

			// ���� ���⼭ �ش� ID�� �˻��� TRUE�Ǿ� MEMBER�� ����ȴٸ� ID�� �ִٴ� ���̴�.
			Member member = memberDao.selectById(conn, joinReq.getId());
			if (member != null) {
				JdbcUtil.rollback(conn); // �����Ϸ��� ID�� �ش��ϴ� ����������� Rollback ����
				throw new DuplicateIdException(); // ���ø����̼��̼���.class�� ����
			}

			// member ��ü�� �ƹ��͵� ���ٸ� ���������� [�����ͻ���] ����.
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
