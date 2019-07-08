package article.service;

import java.util.List;

import article.model.Article;

public class ArticlePage {

	private int total; // ��, total��: 13��
	private int currentPage; // ��, currentPage�� : 1
	private List<Article> content; // ����.
	private int totalPages; // ��, totalPages��: 2
	private int startPage; // ��, startPage��: 1
	private int endPage; // ��, endPage��: 2

	public ArticlePage(int total, int currentPage, int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;

		if (total == 0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {

			// total��: 13��
			// currentPage��: 1��
			// size��: 10��

			totalPages = total / size;
			System.out.println("totalPages��: " + totalPages); // 1�� (10���̻��ִٴ°��� �����)

			if (total % size > 0) {
				totalPages++;
				System.out.println("totalPages��: " + totalPages); // 2�� (���������� 1��������Ŵ/ 10���� ���¾ƶ��������� �˻�)
			}

			int modVal = currentPage % 5; // 1 % 5
			System.out.println("modVal��: " + modVal); // �� mdVal��: 1

			startPage = currentPage / 5 * 5 + 1;
			System.out.println("currentPage/5��: " + currentPage / 5); // 0��
			System.out.println("startPage��: " + startPage); // ��, startPage�� 1

			if (modVal == 0) // 1==0 (���� madVal 0�̶�� ���ǽ���)
				startPage = startPage - 5; // startPage - 5 �ϱ� ==> ��: -4

			endPage = startPage + 4; // startPage + 4 �ϱ� ==> ��: 5 / ��, endPage��: 5
			System.out.println("endPage: " + endPage);
			if (endPage > totalPages) // if( 5>2 ) ��, true
				endPage = totalPages; // �ᱹ endPage 2����
			System.out.println("endPage������: " + endPage);

			System.out.println("-------------------------------");
			System.out.println("total��: " + total); //
			System.out.println("currentPage " + currentPage);
			System.out.println("contentũ��: " + content.size());
			System.out.println("totalPages��: " + totalPages);
			System.out.println("startPage��: " + startPage);
			System.out.println("endPage������: " + endPage);
			System.out.println("-------------------------------");
		}
	}

	public int getTotal() {
		return total;
	}

	public boolean hasNoArticles() {
		return total == 0;
	}

	public boolean hasArticles() {
		return total > 0;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<Article> getContent() {
		return content;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

}
