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

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO implements ResultSetExtractor<List<Publisher>>{
	
	private static final String GET_COUNT = "select count(*) from tbl_publisher";
	private static final String SELECT_ID_BY_NAME = "SELECT * FROM tbl_publisher WHERE publisherName = ?";
	private static final String SELECT_ALL = "SELECT * FROM tbl_publisher";
	private static final String SELECT = "SELECT * FROM tbl_publisher WHERE publisherId = ?";
	private static final String DELETE = "DELETE FROM tbl_publisher WHERE publisherId = ? ";
	private static final String UPDATE = "UPDATE tbl_publisher SET publisherName=?, publisherAddress=?, publisherPhone=? WHERE publisherId=?";
	private static final String INSERT = "INSERT INTO tbl_publisher (publisherName, "
			+ "publisherAddress, publisherPhone) VALUES (?, ?, ?)";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_publisher where publisherName like ?";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_publisher where publisherName like ?";
	private static final String READ_PUBLISHER_FROM_BOOK = "select * from tbl_publisher where publisherId IN (select pubId from tbl_book where bookId = ?)";

	
	public void addPublisher(Publisher publisher) throws SQLException {		
		template.update(INSERT, new Object[] {publisher.getPublisherName(), 
				publisher.getPublisherAddress(), publisher.getPublisherPhone()});		
	}

	
	public Integer addPublisherWithId(Publisher publisher) throws SQLException {
		final String publisherName = publisher.getPublisherName();
		final String publisherAddress = publisher.getPublisherAddress();
		final String publisherPhone = publisher.getPublisherPhone();
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(INSERT, new String[]{"publisherId"});
				pstmt.setString(1, publisherName);
				pstmt.setString(2, publisherAddress);
				pstmt.setString(3, publisherPhone);
				return pstmt;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	
	public void updatePublisher(Publisher publisher) throws SQLException {		
		template.update(UPDATE, new Object[] {publisher.getPublisherName(), 
						publisher.getPublisherAddress(), publisher.getPublisherPhone()},
						publisher.getPublisherId());		
	}

	
	public void deletePublisher(Publisher publisher) throws SQLException {		
		template.update(DELETE, new Object[] {publisher.getPublisherId()});		
	}

	public Publisher readPublisherById(int publisherId) throws SQLException {
		List<Publisher> publishers = template.query(SELECT, new Object[] {publisherId}, this);
		if(publishers != null){
			if(!publishers.isEmpty()){
				return publishers.get(0);
			}
			else{
				return null;
			}
		}
		return null;
	}
	
	public Publisher readbyName(String publisherName) throws SQLException {
		List<Publisher> publishers = template.query(SELECT_ID_BY_NAME, new Object[]{publisherName}, this);
		if(publishers != null){
			return publishers.get(0);
		}
		return null;
	}
	
	public List<Publisher> readBookPublishers(int bookId) {
		return template
				.query(READ_PUBLISHER_FROM_BOOK, new Object[] { bookId }, this);
	}
	
	public List<Publisher> readAllPublishers() throws SQLException {
		return template.query(SELECT_ALL, new Object[]{}, this);
}
	
	public List<Publisher> readallPublishersByName(Integer pageNo, Integer pageSize, String searchString) throws SQLException {
		
		setPageNo(pageNo);
		setPageSize(pageSize);
		
		if(searchString != null && !"".equals(searchString)){
			searchString = "%" + searchString + "%";			
			return template.query(SELECT_ALL_SEARCH, new Object[]{searchString}, this);
		} else {
			return template.query(SELECT_ALL, new Object[] {}, this);
		}		
		
	}

	@Override
	public List<Publisher> extractData(ResultSet rs)  throws SQLException, DataAccessException  {
		
		List<Publisher> publishers = new ArrayList<Publisher>();
		while(rs.next()){
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("publisherId"));
			publisher.setPublisherName(rs.getString("publisherName"));
			publisher.setPublisherAddress(rs.getString("publisherAddress"));
			publisher.setPublisherPhone(rs.getString("publisherPhone"));
			publishers.add(publisher);
		}

		return publishers;
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
