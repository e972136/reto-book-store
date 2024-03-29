/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gaspar.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author ds010102
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="likes")
public class Likes implements Serializable {

    @Id
    @GeneratedValue
    Integer id;

    String customerEmail;

    public Likes(Integer id, String customerEmail) {
        this.id = id;
        this.customerEmail = customerEmail;
    }


    //    @OneToOne
//    @MapsId
//    @JoinColumn(name = "book_id")
//    Book book;


    @ManyToOne(optional = false)
    @JoinColumn(name = "bookId",nullable = false)
    @JsonBackReference
    private Book book;




}
