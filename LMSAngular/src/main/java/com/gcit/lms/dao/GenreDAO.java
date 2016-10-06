package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;

public class GenreDAO extends BaseDAO implements ResultSetExtractor<List<Genre>>{

	private static final String SELECT_ID_BY_NAME = "select * from tbl_genre where genre_name = ?";
	private static final String SELECT_ALL = "SELECT * FROM tbl_genre";
	private static final String SELECT = "SELECT * FROM tbl_genre WHERE genre_id = ?";
	private static final String DELETE = "DELETE FROM tbl_genre WHERE genre_id = ? ";
	private static final String UPDATE = "UPDATE tbl_genre SET genre_name = ? WHERE genre_id = ?";
	private static final String INSERT = "INSERT INTO tbl_genre genre_name VALUES ?";
	private static final String INSERT_TO_BOOK_GENRES = "INSERT INTO tbl_book_genres (genre_id, bookId) VALUES (?, ?)";
	private static final String GET_COUNT = "select count(*) from tbl_genre";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_genre where genre_name like ?";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_genre where genre_name like ?";
	

	
	public void addGenre(Genre genre) throws SQLException {		
		template.update(INSERT, new Object[] {genre.getGenreName()});		
	}
	
	public Integer addGenreWithId(Genre genre) throws SQLException {
		final String genreName = genre.getGenreName();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(INSERT, new String[]{"genre_id"});
				pstmt.setString(1, genreName);
				return pstmt;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public void addBookGenre(Genre genre) throws SQLException{
		int generId = addGenreWithId(genre);
		for(Book book: genre.getBooks()){
			template.update(INSERT_TO_BOOK_GENRES, new Object[]{generId, book.getBookId()});
		}
	}
	
	public void updateGenre(Genre genre) throws SQLException {		
		template.update(UPDATE, new Object[] {genre.getGenreName(), genre.getGerneId()});	
	}

	
	public void deleteGenre(Genre genre) throws SQLException {
		template.update(DELETE, new Object[] {genre.getGerneId()});
	}

	
	public Genre readById(int genreId) throws SQLException{
		List<Genre> genres = template.query(SELECT, new Object[]{genreId}, this);
		if(genres != null){
			return genres.get(0);
		}
		return null;
	}
	
	public List<Genre> readall(Integer pageNo, Integer pageSize, String searchString) throws SQLException {
		
		setPageNo(pageNo);
		setPageSize(pageSize);
		
		if(searchString != null && !"".equals(searchString)){
			searchString = "%" + searchString + "%";
			return template.query(SELECT_ALL_SEARCH, new Object[]{searchString}, this);
		} 
		else {
			return template.query(SELECT_ALL, new Object[] {}, this);
		}		
	}
	
	public List<Genre> readallGenres() throws SQLException {
			return template.query(SELECT_ALL, new Object[] {}, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Genre> genres = new ArrayList<Genre>();
		while(rs.next()){
			Genre genre = new Genre();
			genre.setGerneId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
			genres.add(genre);
		}		
		return genres;
	}
	
	public Genre readbyName(String genreName) throws SQLException {
		List<Genre> genres = template.query(SELECT_ID_BY_NAME, new Object[] {genreName}, this); 
		if(genres != null){
			return genres.get(0);
		}
		return null;
	}
	
	public Integer getCount() throws SQLException {		
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
