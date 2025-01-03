/*
작업자 : 홍제기
 */

package org.kosa.bookmanagement.util;

import org.kosa.bookmanagement.model.dao.AuthorDAOImpl;
import org.kosa.bookmanagement.model.dao.BookDAOImpl;
import org.kosa.bookmanagement.model.dao.CopyDAOImpl;
import org.kosa.bookmanagement.model.dto.BookDTO;
import org.kosa.bookmanagement.model.service.AuthorService;
import org.kosa.bookmanagement.model.service.BookService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

// 책을 넣기 위한 클래스, 사전 데이터를 넣기 위해 존재
public class BookInsertion {
    public static void main(String[] args) {
        // 경로를 로컬에 맞게 설정할 것
        File file = new File("C:\\dev\\project1st\\book-management\\src\\main\\resources\\org\\kosa\\bookmanagement\\서울특별시교육청고척도서관 장서 대출목록 (2024년 05월).csv");

        AuthorService authorService = new AuthorService(new AuthorDAOImpl());
        BookService bookService = new BookService(new BookDAOImpl(), new CopyDAOImpl());

        // 파일 읽기 인코딩 설정 출처 : https://mytory.net/archives/307
        try(FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "EUC-KR");
            BufferedReader br = new BufferedReader(isr)){

            String line = "";
            int index = 1;
            int end = index + 3000 - 1;
            Set<Integer> authorSet = new HashSet<>();

            while((line = br.readLine())!= null){
                String[] bookInfo = line.split(",");

                if(bookInfo.length > 13){
                    continue;
                }

                bookInfo[2] = bookInfo[2].replace("\"", "");
                String categoryStr = bookInfo[9].replace("\"", "");
                String publisher = bookInfo[3].replace("\"", "");
                String yearStr = bookInfo[4].replace("\"", "");
                String isbn = bookInfo[5].replace("\"", "");

                // 각종 예외에 continue 처리
                if(isbn.length() > 13){
                    continue;
                }

                if(bookInfo[2].length() != 6 || !bookInfo[2].contains(" 지음")){
                    continue;
                }

                if(categoryStr.isEmpty()){
                    continue;
                }

                if(publisher.isEmpty()){
                    continue;
                }

                int point = categoryStr.indexOf(".");
                if(point != -1){
                    categoryStr = categoryStr.substring(0, point);
                }
                int category = 0;
                try {
                    category = Integer.parseInt(categoryStr)/100*100;
                } catch (Exception e) {
                    continue;
                }

                try {
                    int year = Integer.parseInt(yearStr);
                    if(year >= 10000){
                        continue;
                    }
                } catch (Exception e){
                    continue;
                }

                // 저자 먼저 insert
                String authorName = bookInfo[2].replace(" 지음", "");
                authorService.insertAuthor(authorName);

                // isbn insert
                BookDTO book = new BookDTO();
                book.setTitle(bookInfo[1].replace("\"", ""));
                book.setPublisher(publisher);
                book.setPublishYear(yearStr);
                book.setIsbn(isbn);

                book.setCategory(category == 0 ? "000" : ""+category);

                authorSet.clear();
                authorSet.add(index);

                // 저자-isbn 연결
                bookService.updateISBN(book, authorSet);

                for(int i=0;i<(int)(Math.random() * 3)+1;i++){
                    bookService.updateCopy(book.getIsbn(), 0, (int)(Math.random() * 4)+1);
                }

                // 터지거나 오래 걸리면 여기를 조정
                if(index++ % 20 == 0){
                    Thread.sleep(200);
                }

                if(index++ > end){
                    break;
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
