package com.gcit.lms;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;

@EnableTransactionManagement
@Configuration
public class LMSConfig {

	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost/library2?useSSL=false";
	private static String user = "root";
	private static String pass = "mysql";

	@Bean
	public BasicDataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(pass);

		return ds;
	}

	@Bean
	public JdbcTemplate template(){
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource());
		return template;
	}

	@Bean
	public AuthorDAO adao(){
		return new AuthorDAO();
	}

	@Bean
	public BookDAO bdao(){
		return new BookDAO();
	}
	
	@Bean
	public BranchDAO branchDao(){
		return new BranchDAO();
	}
	
	@Bean
	public BorrowerDAO borrowerDao(){
		return new BorrowerDAO();
	}
	
	@Bean
	public PublisherDAO publisherDao(){
		return new PublisherDAO();
	}
	
	@Bean
	public GenreDAO genreDao(){
		return new GenreDAO();
	}
	
	@Bean
	public BookLoanDAO bookLoanDao(){
		return new BookLoanDAO();
	}
	
	@Bean
	public BookCopiesDAO bookCopiesDao(){
		return new BookCopiesDAO();
	}
	
	@Bean
	public PlatformTransactionManager txManager(){
		DataSourceTransactionManager tx = new DataSourceTransactionManager();
		tx.setDataSource(dataSource());		
		return tx;
	}
}
