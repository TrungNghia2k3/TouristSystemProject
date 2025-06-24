package com.ntn.tourism.dto.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class TourBookingDTO {

    @NotBlank(message = "Phone number cannot be blank")
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email không hợp lệ") // Kiểm tra định dạng email
    private String email;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Date from cannot be blank")
    @FutureOrPresent(message = "Date from cannot be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @NotNull(message = "Date to cannot be blank")
    @FutureOrPresent(message = "Date to cannot be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @NotNull(message = "Guest cannot be blank")
    @Min(value = 1, message = "Must have at least 1 guest")
    private Integer guest;

    @Min(value = 0, message = "The number of children cannot be negative")
    private Integer children;

    private String notes; // Không bắt buộc nhập

    @NotNull
    private Integer tourId;
}