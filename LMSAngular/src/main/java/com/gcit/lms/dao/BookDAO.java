package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;


public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{

	@Autowired
	AuthorDAO authorDao;
	
	@Autowired
	PublisherDAO publisherDao;
	
	@Autowired
	GenreDAO genreDao;
	
	
	private static final String SELECT_ALL = "select * from tbl_book";
	private static final String SELECT = "select * from tbl_book where bookId = ?";
	private static final String SELECT_ID_BY_TITLE = "select bookId from tbl_book where title = ?";
	private static final String SELECT_BOOKLIST = "SELECT tbl_book.bookId, tbl_book.title, tbl_author.authorName FROM tbl_book inner join tbl_author on tbl_book.authId = tbl_author.authorId";
	private static final String SELECT_BY_PUBLISHER = "select * from tbl_book where pubId =? ";
	private static final String SELECT_BOOK_WITH_AUTHOR = "select * from tbl_book where bookId IN (select bookId from tbl_book_authors where authorId = ?)";
//	private static final String SELECT_BOOK_WITH_BRANCH ="select * from tbl_book where bookId IN (select bookId from tbl_book_copies where branchId = ?)";
	private static final String SELECT_BOOK_WITH_BRANCH = "SELECT * FROM tbl_book b join tbl_book_copies bc on b.bookId = bc.bookId join tbl_library_branch br on bc.branchId = br.branchId where br.branchId = ?";
	private static final String INSERT = "insert into tbl_book (title, pubId) values (?,?)";
	private static final String INSERT_IN_TO_BOOK_AUTHORS = "insert into tbl_book_authors (bookId, authorId) values (?,?)";
	private static final String INSERT_IN_TO_BOOK_GENRE = "insert into tbl_book_genres (genre_id, bookId) values (?,?)";
	private static final String DELETE = "delete from tbl_book where bookId = ?";
	private static final String DELETE_FROM_BOOK_GENRE = "delete from tbl_book_genres where bookId = ?";
	private static final String DELETE_FROM_BOOK_AUTHOR = "delete from tbl_book_authors where bookId = ?";
	private static final String UPDATE = "update tbl_book set title = ?, pubId = ? where bookId = ?";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_book where title like ?";
	private static final String SELECT_ID_BY_NAME = "select * from tbl_book where title = ?";
	private static final String GET_COUNT = "select count(*) from tbl_book";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_book where title like ?";
	private static final String GET_PUBLISHER_NAME ="SELECT publisherName FROM tbl_publisher tp "
			+ "join tbl_book tb on tp.publisherId = tb.pubId where tb.bookId = ?";
	private static final String UPDATE_NO_OF_COPIES_CHECKOUT = "update tbl_book_copies set noOfCopies = noOfCopies-1 where bookId = ? and branchId = ?";
	

	public void addBook(Book book) throws SQLException {	
		int bookId = addBookWithID(book);
		
		if(book.getAuthors() != null){
			for(Author author: book.getAuthors()){
				template.update(INSERT_IN_TO_BOOK_AUTHORS, new Object[]{bookId, author.getAuthorId()});
			}			
		}
		if(book.getGenres() != null){
			for(Genre genre: book.getGenres()){
				template.update(INSERT_IN_TO_BOOK_GENRE, new Object[]{genre.getGerneId(), bookId});
			}
			
		}	
	}
	
	public Integer addBookWithID(Book book) {
		final String bookName = book.getTitle();
		final Integer pubId = book.getPublisher().getPublisherId();

		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT, new String[] { "bookId" });
				ps.setString(1, bookName);
				ps.setInt(2, pubId);
				return ps;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();

	}
	
	public void updateBook(Book book) throws SQLException {

		template.update(UPDATE,	new Object[] { book.getTitle(), book.getPublisher().getPublisherId(),
						book.getBookId() });
		template.update(DELETE_FROM_BOOK_GENRE,	new Object[] { book.getBookId() });
		template.update(DELETE_FROM_BOOK_AUTHOR, new Object[] { book.getBookId() });	
		
		if(book.getAuthors() != null){
			for(Author author: book.getAuthors()){
				template.update(INSERT_IN_TO_BOOK_AUTHORS, new Object[]{book.getBookId(), author.getAuthorId()});
			}			
		}

		if(book.getGenres() != null){
			for(Genre genre: book.getGenres()){
				template.update(INSERT_IN_TO_BOOK_GENRE, new Object[]{genre.getGerneId(), book.getBookId()});
			}			
		}
	}
	
	public void deleteBook(Book book) throws SQLException {
		template.update(DELETE, new Object[] {book.getBookId()});
	}
	
	public Book readBookById(Integer bookId) throws SQLException {
		List<Book> books = template.query(SELECT , new Object[] {bookId}, this);
		if(books != null){
			return books.get(0);
		} 
		return null;
	}
	
	public List<Book> readAuthorBook(int authorId) {
		return template.query(SELECT_BOOK_WITH_AUTHOR, new Object[] {authorId}, this);
	}
	
	public List<Book> readBranchBook(int branchId) {
		return template.query(SELECT_BOOK_WITH_BRANCH, new Object[] { branchId }, this);
	}
	
	public List<Book> readBookByPublisher(int pubId) {
		return template.query(SELECT_BY_PUBLISHER, new Object[] { pubId }, this);
	}
	
	public List<Book> readallBooksByName(Integer pageNo, Integer pageSize, String searchString) throws SQLException {
		setPageNo(pageNo);
		setPageSize(pageSize);
		
		if(searchString != null && !"".equals(searchString)){
			
			searchString = "%" + searchString + "%";
			
			return template.query(SELECT_ALL_SEARCH, new Object[]{searchString}, this);
		}	else{
				return template.query(SELECT_ALL, new Object[]{}, this);
		}
	}
	
	public List<Book> readAllBooks() throws SQLException{
		return template.query(SELECT_ALL, new Object[]{}, this);
	}
	
	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {		
		List<Book> books = new ArrayList<Book>();
		
		while(rs.next()){
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			
			try {
				book.setPublisher(publisherDao.readPublisherById(rs.getInt("pubId")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<Author> authors= template.query("select * from tbl_author where authorId In"
					+ "(select authorId from tbl_book_authors where bookId=?)", new Object[] {rs.getInt("bookId")}, authorDao);
			book.setAuthors(authors);
			
			List<Genre> genres= template.query("select * from tbl_genre where genre_id In"
					+ "(select genre_id from tbl_book_genres where bookId=?)", new Object[]{rs.getInt("bookId")}, genreDao);
			book.setGenres(genres);
			try{
				book.setNoOfCopies(rs.getInt("noOfCopies"));
			}catch(Exception e){
				book.setNoOfCopies(0);
			}
			
			
//			Integer temp = rs.getInt("noOfCopies");
//			if( temp!=null){
//				book.setNoOfCopies(rs.getInt("noOfCopies"));
//			}
			
			
			books.add(book);
		}
		return books;
	}
	
	public Book readbyName(String title) throws SQLException {
		List<Book> books = template.query(SELECT_ID_BY_NAME, new Object[] {title}, this);
		if(books != null){
			return books.get(0);
		}
		return null;
	}
	
	public Integer getCount() throws SQLException {
		return template.queryForObject(GET_COUNT, Integer.class);
	}

	
	public Integer getCountByName(String searchString) throws SQLException {
		if(searchString != null && !"".equals(searchString)){
			
			searchString = "%" + searchString + "%";
			
			return template.queryForObject(GET_COUNT_BY_NAME,new Object[]{searchString}, Integer.class);
		}	
		else{
			return template.queryForObject(GET_COUNT, Integer.class);
		}
	}
}
