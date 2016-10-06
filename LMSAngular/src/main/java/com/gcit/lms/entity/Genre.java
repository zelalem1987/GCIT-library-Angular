package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Genre implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9059123163326796821L;
	
	private Integer gerneId;
	private String genreName;
	
	private List<Book> books;
	
	public Genre(){
		super();
	}
	
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}	

	public boolean addBook(Book e) {
		if(books == null) books = new ArrayList<Book>();
		return books.add(e);
	}

	public Integer getGerneId() {
		return gerneId;
	}
	
	public void setGerneId(Integer gerneId) {
		this.gerneId = gerneId;
	}
	
	public String getGenreName() {
		return genreName;
	}
	
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((books == null) ? 0 : books.hashCode());
		result = prime * result + ((genreName == null) ? 0 : genreName.hashCode());
		result = prime * result + ((gerneId == null) ? 0 : gerneId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Genre))
			return false;
		Genre other = (Genre) obj;
		if (books == null) {
			if (other.books != null)
				return false;
		} else if (!books.equals(other.books))
			return false;
		if (genreName == null) {
			if (other.genreName != null)
				return false;
		} else if (!genreName.equals(other.genreName))
			return false;
		if (gerneId == null) {
			if (other.gerneId != null)
				return false;
		} else if (!gerneId.equals(other.gerneId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Genre [gerneId=" + gerneId + ", genreName=" + genreName + ", books=" + books + "]";
	}
	
	
}
