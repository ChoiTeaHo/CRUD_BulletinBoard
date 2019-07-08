package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	// 커넥션을 구할때 사용할 코드(web.xml에서 지정한 poolName 값인 board를 풀이름으로 사용)
	public static Connection getConnection() throws SQLException {
		// 커넥션풀에서 커넥션을 리턴하는 방법. (맨 끝에 board는 DBPInit클래스에서 등록한 커넥션풀이름)
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:board");
	}
}

// (원래사용하는 방식)
// PoolingDriver driver = (PoolingDriver)
// DriverManager.getDriver("jdbc:apache:commons:dbcp:");
// driver.registerPool("chap14", connectionPool);