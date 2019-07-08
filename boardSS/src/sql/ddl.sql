create table board.article (
	article_no int auto_increment primary key,
	writer_id varchar(50) not null,
	writer_name varchar(50) not null,
	title varchar(255) not null,
	regdate datetime not null,
	moddate datetime not null,
	read_cnt int
)engin=InnoDB default character set = utf8;

CREATE TABLE board.article_content (
	article_no int primary key,
	content text
)engin=InnoDB default character set = utf8;