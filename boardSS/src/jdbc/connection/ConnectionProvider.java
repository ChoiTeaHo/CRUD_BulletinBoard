package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	// Ŀ�ؼ��� ���Ҷ� ����� �ڵ�(web.xml���� ������ poolName ���� board�� Ǯ�̸����� ���)
	public static Connection getConnection() throws SQLException {
		// Ŀ�ؼ�Ǯ���� Ŀ�ؼ��� �����ϴ� ���. (�� ���� board�� DBPInitŬ�������� ����� Ŀ�ؼ�Ǯ�̸�)
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:board");
	}
}

// (��������ϴ� ���)
// PoolingDriver driver = (PoolingDriver)
// DriverManager.getDriver("jdbc:apache:commons:dbcp:");
// driver.registerPool("chap14", connectionPool);