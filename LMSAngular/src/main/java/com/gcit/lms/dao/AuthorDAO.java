package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

public class AuthorDAO extends BaseDAO implements ResultSetExtractor<List<Author>>{

	private static final String INSERT_TO_BOOK_AUTHORS = "insert into tbl_book_authors (bookId, authorId) values(?, ?)";
	private static final String SELECT_ID_BY_NAME = "select * from tbl_author where authorName = ?";
	private static final String SELECT_ALL = "select * from tbl_author order by lastUpdatedDate desc";
	private static final String SELECT = "select * from tbl_author where authorId = ?";
	private static final String SELECT_AUTHOR_BY_BOOK_ID = "select * from tbl_author where authorId IN (select authorId from tbl_book_authors where bookId = ?)";
	private static final String DELETE = "delete from tbl_author where authorId = ?";
	private static final String UPDATE = "update tbl_author set authorName = ?, lastUpdatedDate = ? where authorId = ?";
	private static final String INSERT = "insert into tbl_author (authorName) values (?)";
	private static final String INSERT_BOOK_AUTHOR = "insert into tbl_book_authors (bookId, authorId) values (?,?)";
	private static final String GET_COUNT = "select count(*) from tbl_author";
	private static final String SELECT_BY_NAME = "select * from tbl_author where authorName like ?";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_author where authorName like ?"; 
	private static final String DELETE_FROM_BOOK_AUTHOR_TABLE = "delete from tbl_book_authors where authorId = ?"
			+ "using tbl_book_authors inner join tbl_author on tbl_book_authors.authorId = tbl_author.authorId "
			+ "where (tbl_author.authorId = 1)";

	
	public void addAuthor(Author author) throws SQLException {		
		template.update(INSERT, new Object[] { author.getAuthorName() });		
	}
	
	public Integer addAuthorWithId(Author author) throws SQLException{
		final String authorName = author.getAuthorName();
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(INSERT, new String[]{"authorId"});
				pstmt.setString(1, authorName);
				return pstmt;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void addBookAuthor(Author author) throws SQLException{
		for(Book book: author.getBooks()){
			template.update(INSERT_TO_BOOK_AUTHORS, new Object[]{author.getAuthorId(), book.getBookId()});
		}
	}
	
//	public void updateAuthor(Author author) throws SQLException {
//		template.update(UPDATE, new Object[] { author.getAuthorName(), author.getAuthorId() });
//		template.update(DELETE_FROM_BOOK_AUTHOR_TABLE, new Object[] { author.getAuthorId() });
//		
//		if (author.getBooks() != null) {
//			for (Book book : author.getBooks()) {
//				template.update(INSERT_BOOK_AUTHOR,	new Object[] { book.getBookId(), 
//						author.getAuthorId() });
//			}
//		}
//	}
	
	public void updateAuthor(Author author) throws SQLException {
		template.update(UPDATE, new Object[] { author.getAuthorName(), new Date(), author.getAuthorId() });
	}
	
	public void deleteAuthor(Author author) throws SQLException {		
		template.update(DELETE, new Object[] { author.getAuthorId() });		
	}

	public Author readAuthor(int pk) throws SQLException {
		List<Author> authors = template.query(SELECT, new Object[] {pk}, this);
		if(authors != null){
			return authors.get(0);
		}
		return null;
	}

	public Author readAuthorByID(Integer authorId) throws SQLException {
		List<Author> authors = template.query(SELECT, new Object[] { authorId }, this);
		if (authors != null) {
			return authors.get(0);
		}
		return null;
	}

	public List<Author> readAuthorsByName(Integer pageNo, Integer pageSize, String searchString) throws SQLException {
		
		setPageNo(pageNo);
		setPageSize(pageSize);
		String sql="";
		if(searchString != null && !"".equals(searchString)){
			searchString = "%" + searchString + "%";
			sql = addLimitToQuery(pageNo,pageSize,SELECT_BY_NAME);
			return template.query(sql, new Object[]{searchString}, this);
		}	else{
			sql = addLimitToQuery(pageNo,pageSize,SELECT_ALL);
				return template.query(sql, new Object[]{}, this);
		}
	}
	
	public List<Author> readAllAuthors() throws SQLException {
				return template.query(SELECT_ALL, new Object[]{}, this);
	}
	
	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Author> authors = new ArrayList<Author>();
		
			while(rs.next()){
				Author a = new Author();
				a.setAuthorId(rs.getInt("authorId"));
				a.setAuthorName(rs.getString("authorName"));
				a.setLastUpdatedDate(rs.getDate("lastUpdatedDate"));
				authors.add(a);	
			}
		
		return authors;
	}

	public Author readbyName(String authorName) throws SQLException {
		List<Author> authors = template.query(SELECT_ID_BY_NAME, new Object[] {authorName}, this);
		if(authors != null){
			return authors.get(0);
		}
		return null; 
	}
	
	public List<Author> readBookAuthor(int bookId) {
		return template.query(SELECT_AUTHOR_BY_BOOK_ID, new Object[] { bookId }, this);
	}
	
	public Integer getCount() throws SQLException{
		return template.queryForObject(GET_COUNT, Integer.class);
	}
	
	public Integer getCountByName(String searchString) throws SQLException {
		
		if(searchString != null && !"".equals(searchString)){
			
			searchString = "%" + searchString + "%";
			
			return template.queryForObject(GET_COUNT_BY_NAME, new Object[]{searchString}, Integer.class);
		}	
		else{
			return template.queryForObject(GET_COUNT, Integer.class);
		}		
	}
}
