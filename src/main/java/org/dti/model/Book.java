package org.dti.model;


import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private Integer id;
    private String title;
    private String author;
    private Integer pages;
    private Double price;
    private LocalDate publishedDate;
    private String description;

    public Book() {}

    public Book(Integer id, String title, String author, Integer pages, Double price, LocalDate publishedDate, String description) {
        this.id = id; this.title = title; this.author = author;
        this.pages = pages; this.price = price; this.publishedDate = publishedDate; this.description = description;
    }

    public Integer getId(){return id;}
    public void setId(Integer id){this.id=id;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getAuthor(){return author;}
    public void setAuthor(String author){this.author=author;}
    public Integer getPages(){return pages;}
    public void setPages(Integer pages){this.pages=pages;}
    public Double getPrice(){return price;}
    public void setPrice(Double price){this.price=price;}
    public LocalDate getPublishedDate(){return publishedDate;}
    public void setPublishedDate(LocalDate publishedDate){this.publishedDate=publishedDate;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    @Override
    public String toString(){
        return String.format("Book{id=%d, title='%s', author='%s', pages=%d, price=%s, publishedDate=%s}",
                id, title, author, pages, price==null?"-":price.toString(), publishedDate==null?"-":publishedDate.toString());
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof Book)) return false;
        Book b=(Book)o;
        return Objects.equals(id, b.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
