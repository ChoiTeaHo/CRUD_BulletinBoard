package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.JdbcUtil;

public class ArticleDao {

	public Article insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("insert into article "
					+ "(writer_id, writer_name, title, regdate, moddate, read_cnt) " + "values(?,?,?,?,?,0)");

			pstmt.setString(1, article.getWriter().getId()); // user id����
			pstmt.setString(2, article.getWriter().getName()); // user pw����
			pstmt.setString(3, article.getTitle()); // title(����) ����
			pstmt.setTimestamp(4, toTimestamp(article.getRegDate())); // date��������
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate())); // date��������

			int insertedCount = pstmt.executeUpdate(); // insert�Ͽ� ������ (�׸��� ���ٸ����ߴ��� ��������)

			if (insertedCount > 0) { // �����Ѵٸ� �ֱ� ����� �⺻Ű article_no�� int�� �߰��Ͽ� Article��ü ����
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select last_insert_id() from article"); // ���̺� �߰��� �ֱ� ���ڵ��� �⺻Ű����
				if (rs.next()) { // �� , insert�� �߰��� article_noĮ�������� ����
					Integer newNum = rs.getInt(1); // ������ 1��° �ʵ带 Integer���·� �޴´�. //�� �⺻Ű �ڵ��������޴´�.
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegDate(),
							article.getModifiedDate(), 0);
				}
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}

	// (2)�Խñ۸����ȸ���
	public int selectCount(Connection conn) throws SQLException {

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM article");
			if (rs.next())
				return rs.getInt(1);

			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}

	// (2-1)�����ѹ����� �Խñ��� �д� ���
	public List<Article> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			pstmt = conn.prepareStatement("SELECT * FROM article ORDER BY article_no DESC LIMIT ?, ?");
			pstmt.setInt(1, startRow); // 0����
			pstmt.setInt(2, size); // 10����
			rs = pstmt.executeQuery();

			List<Article> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertArticle(rs));
			}

			return result;
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
	}

	private Article convertArticle(ResultSet rs) throws SQLException {

		Article article = new Article(rs.getInt("article_no"), // �⺻Ű
				new Writer(rs.getString("writer_id"), // userID
						rs.getString("writer_name")), // userName
				rs.getString("title"), // title
				new Date(rs.getTimestamp("regdate").getTime()), // Date����
				new Date(rs.getTimestamp("moddate").getTime()), // Date����
				rs.getInt("read_cnt")); // read_cnt

		return article;
	}

	// 3. Ư����ȣ�� �ش��ϴ� �Խñ۵����� �б�
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			pstmt = conn.prepareStatement("SELECT * FROM article WHERE article_no=?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();

			Article article = null;
			if (rs.next()) {
				article = convertArticle(rs);
			}

			return article;
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
	}

	// 3-1 Ư���Խñ��� ��ȸ�� ī��Ʈ�ϱ�
	public void increaseReadCount(Connection conn, int no) throws SQLException {
		try (PreparedStatement pstmt = conn
				.prepareStatement("UPDATE article SET read_cnt = read_cnt + 1 WHERE article_no =?")) {
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}

	// 4. �Խù� ������� ...moddate Į���� ���� ����ð����� �����ؼ� �ֱټ������
	public int update(Connection conn, int no, String title) throws SQLException {
		try (PreparedStatement pstmt = conn
				.prepareStatement("UPDATE article SET title=?, moddate=NOW() WHERE article_no=?")) {
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}

	// 5. �Խù��������...
	public void delete(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("DELETE FROM article where article_no=?");
			pstmt.setInt(1, no);
			pstmt.executeUpdate();

		} catch (Exception e) {
		}
	}

	// ������
	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

}
