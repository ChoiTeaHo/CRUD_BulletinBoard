package jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class DBCPInitListener implements ServletContextListener {

	@Override // 웹어플리케이션이 시작시 (초기화) 호출된다.
	public void contextInitialized(ServletContextEvent sce) { // 웹어플컨텍스트를 구할수 있는 getServletContext() 메소드를 제공한다.
		String poolConfig = sce.getServletContext().getInitParameter("poolConfig"); // 컨텍스트 초기화파라미터를 구할수있다.
																					// 지정한 이름을갖는 컨텍스트초기화파라미터를 리턴한다.
		Properties prop = new Properties(); // 키=값형태의 [프로퍼티 객체] 생성
		try {
			prop.load(new StringReader(poolConfig)); // 키=값 형식으로 구성된 문자열로부터 프로퍼티를 로딩한다.(dbUser는 jspexam 프로퍼티값을 줌)
		} catch (IOException e) {
			throw new RuntimeException("config load fail", e);
		}
		loadJDBCDriver(prop); // 컨텍스트초기화 파라미터를 이용해서 필요값을 로딩한다.
		initConnectionPool(prop); // 컨텍스트초기화 파라미터를 이용해서 필요값을 로딩한다.
	}

	// loadJDBCDriver 메소드
	private void loadJDBCDriver(Properties prop) { // 프로퍼티를 매개값으로 받는다.
		String driverClass = prop.getProperty("jdbcdriver"); // 프로퍼트의 Key에 대한 Value값을 가져온다.
		try {
			Class.forName(driverClass); // JDBC드라이버를 로딩한다. (com.mysql.cj.jdbc.Driver)
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBC Driver", ex);
		}
	}

	private void initConnectionPool(Properties prop) {
		try {
			String jdbcUrl = prop.getProperty("jdbcUrl"); // 프로퍼티를 매개값으로 받는다. (jdbcUrl)
			jdbcUrl += "&useUnicode=true&useSSL=false&serverTimezone=UTC";
			String username = prop.getProperty("dbUser"); // 프로퍼티를 매개값으로 받는다. (username)
			String pw = prop.getProperty("dbPass"); // 프로퍼티를 매개값으로 받는다. (pw)

			// 커넥션풀이 새로운 커넥션을 생성할때 사용한다. --> 커넥션팩토리 생성
			ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcUrl, username, pw);

			// DBCP는 커넥션풀에 커넥션을 보관할떄 이녀석 PoolableConnectionFactory를 사용한다.
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
			String validationQuery = prop.getProperty("validationQuery");// 프로퍼티를 매개값으로 받는다(validateQuery)
			if (validationQuery != null && !validationQuery.isEmpty()) {
				poolableConnFactory.setValidationQuery(validationQuery); // 커넥션 유효여부검사할떄 사용할 쿼리지정
			}

			// 커넥션풀의 설정정보를 생성할떄 사용한다 --> GenericObjectPoolConfig를 사용한다.
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L); // 유효커넥션검사주기
			poolConfig.setTestWhileIdle(true); // 풀에 보관중인 커넥션이 유효한지를 검사할지 여부선택

			int minIdle = getIntProperty(prop, "minIdle", 5); // 프로퍼티로딩 커스텀함수(프로퍼티값없으면 5를 설정)
			poolConfig.setMinIdle(minIdle);

			int maxTotal = getIntProperty(prop, "maxTotal", 50); // 프로퍼티로딩 커스텀함수(프로퍼티값없으면 50을 설정)
			poolConfig.setMaxTotal(maxTotal);

			// 커넥션풀을 생성할떄 사용한다. --> GenericObjectPool<PoolableConnection>
			GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnFactory,
					poolConfig); // 매개변수 ( 사용할팩토리 , 설정정보 ) 를 사용한다.
			poolableConnFactory.setPool(connectionPool); // 생성한 커넥션풀을 연결한다.

			Class.forName("org.apache.commons.dbcp2.PoolingDriver"); // 커넥션풀을 제공하는 JDBC드라이버를 등록
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			String poolName = prop.getProperty("poolName"); // 프로퍼티를 매개값으로 받는다 (poolName) 이값은 board이다.
			driver.registerPool(poolName, connectionPool); // 생성한 커넥션풀을 등록한다.
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private int getIntProperty(Properties prop, String propName, int defaultValue) {
		String value = prop.getProperty(propName);
		if (value == null)
			return defaultValue;
		return Integer.parseInt(value);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}