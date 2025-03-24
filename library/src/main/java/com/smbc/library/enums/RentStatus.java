package com.smbc.library.enums;

public enum RentStatus {
   DIPINJAM("Dipinjam"),
   DIKEMBALIKAN("Dikembalikan"),
   TERLAMBAT("Terlambat");

   private final String status;

   RentStatus(String status) {
       this.status = status;
   }

   public String getStatus() {
       return status;
   }
}
