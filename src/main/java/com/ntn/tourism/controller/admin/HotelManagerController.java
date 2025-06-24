package com.ntn.tourism.controller.admin;

import com.ntn.tourism.model.Booking;
import com.ntn.tourism.model.Hotel;
import com.ntn.tourism.repository.BookingRepository;
import com.ntn.tourism.service.UserService;
import com.ntn.tourism.dto.UserRegisteredDTO;
import com.ntn.tourism.model.User;
import com.ntn.tourism.repository.HotelRepository;
import com.ntn.tourism.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/hotel-manager")
public class HotelManagerController {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String displayDashboard() {
        return "hotel-manager/index";
    }

    @GetMapping("/displayTables")
    public String displayTables(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; // Chuyển hướng về trang login nếu chưa đăng nhập
        }

        Hotel hotel = hotelRepository.findByUser(user); // Tìm khách sạn mà user sở hữu
        if (hotel == null) {
            model.addAttribute("error", "You are not an owner of any hotel!");
            return "hotel-manager/tables"; // Trả về trang với thông báo lỗi
        }

        List<Booking> bookings = bookingRepository.findByHotelId(hotel.getId());// Lấy danh sách booking

        model.addAttribute("bookings", bookings); // Đưa vào model để hiển thị trong Thymeleaf

        return "hotel-manager/tables"; // Trả về template Thymeleaf
    }

    @GetMapping("/displayCharts")
    public String displayCharts() {
        return "hotel-manager/charts";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserRegisteredDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "hotel-manager/tables"; // Quay lại nếu có lỗi
        }
        // Nếu id == null, tạo user mới; nếu không, cập nhật user cũ
        if (userDTO.getId() == null) {
            userService.save(userDTO);
        } else {
            userService.update(userDTO);
        }
        return "redirect:/hotel-manager/displayTables";
    }
}
