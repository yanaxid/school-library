package com.smbc.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto{
    private String judul;
    private String penulis;
    private String penerbit;
    private Integer tahunTerbit;
    private String isbn;
    private String kategori;
    private Integer stok;
}