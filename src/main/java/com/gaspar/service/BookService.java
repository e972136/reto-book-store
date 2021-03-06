package com.gaspar.service;

import com.gaspar.dto.BookDto;
import com.gaspar.models.Book;
import com.gaspar.repository.BookRepository;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;

    public Book save(BookDto bookDto) {
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .description(bookDto.getDescription())
                .stock(bookDto.getStock())
                .salePrice(bookDto.getSalePrice())
                .available(true)
                .build();
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Integer id, Book book) {
        Book bookObtain = bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Id no existe"));
        bookObtain.set(book);
        return bookObtain;
    }

    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> getBook(Integer id) {
        return bookRepository.findById(id);
    }

    /*
    *   String title;
    String description;
    Integer stock;
    Double salePrice;
    Boolean available;
    * */
    @Transactional
    public Book patch(Integer id, Map<Object, Object> fields) {        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Book bookObtain = bookRepository.findById(id).orElseThrow(() -> new IllegalStateException("Id no existe"));
        //System.err.println(" encontrado: "+bookObtain);
        if (bookObtain != null) {
            if(fields.containsKey("title")){
                bookObtain.setTitle((String)fields.get("title"));
            }
            if(fields.containsKey("description")){
                bookObtain.setDescription((String)fields.get("description"));
            }
            if(fields.containsKey("stock")){
                try{
                    bookObtain.setStock(Integer.valueOf(fields.get("stock")+""));
                }catch (Exception e){
                    log.error(e+"");
                }
            }
            if(fields.containsKey("salePrice")){
                bookObtain.setSalePrice(Double.valueOf(fields.get("salePrice")+""));
            }
            if(fields.containsKey("available")){
                bookObtain.setAvailable(Boolean.valueOf(fields.get("available")+""));
            }
//            fields.forEach((key, value) -> {
//                if(key.equals("id")) return;
//                Field findField = ReflectionUtils.findField(Book.class, key.toString());
//                findField.setAccessible(true);
//                ReflectionUtils.setField(findField, bookObtain, value);
//            });
            System.err.println(" actualizado: " + bookObtain);

        }
        return bookObtain;
    }

    public Map<Object, Object> allBooksPage(boolean unavailable,
                                            Integer page,
                                            Integer size,
                                            String sortStr
    ) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        if (page != null) {
            page = page - 1;
        }
        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 12;
        }

        Field findField = ReflectionUtils.findField(Book.class, sortStr);
        if (findField == null) {
             sortStr = "title";
        } else {
            System.err.println("Columna " + sortStr + " no existe");
        }

        Sort sortBy = Sort.by(sortStr);
        PageRequest pageRequest = PageRequest.of(page, size, sortBy);
        Page<Book> findAll = bookRepository.findAll(pageRequest);


        Map<Object, Object> respuesta = new LinkedHashMap<>();

        Boolean showUnavaible = unavailable;

        if (showUnavaible) {
            System.err.println("muestra todo");
            respuesta.put("content", findAll.getContent());
        } else {
            System.err.println("muestra restringido");
            respuesta.put("content", findAll.getContent().stream().filter(f -> f.getAvailable()).collect(Collectors.toList()));
        }


        respuesta.put("size", size);
        respuesta.put("numberOfElements", findAll.getNumberOfElements());
        respuesta.put("totalElements", findAll.getTotalElements());
        respuesta.put("totalPages", findAll.getTotalPages());
        respuesta.put("number", findAll.getNumber()+1);

        return respuesta;
    }


}
