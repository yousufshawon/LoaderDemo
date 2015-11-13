package com.yousuf.shawon.loaderdemo.app.model;


import java.util.Date;

/**
 * Created by user on 7/19/2015.
 */
public class Book {

    public Long _id;
    public String title;
    public String authorName;
    public Date publishDate;


    public Book() {
    }

    public Book(String title, String authorName, Date publishDate) {
        this.title = title;
        this.authorName = authorName;
        this.publishDate = publishDate;
    }

    public Book(Long _id, String title, String authorName, Date publishDate) {
        this._id = _id;
        this.title = title;
        this.authorName = authorName;
        this.publishDate = publishDate;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return  title + "\n" + authorName ;
    }
}

