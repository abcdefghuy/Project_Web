package controller.staffManagementController;

import DAO.StaffDAO;
import business.Staff;
import config.UtilsEmail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebServlet("/deleteStaff")
public class DeleteStaffServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Long staffId = Long.parseLong(request.getParameter("emp-id"));
        Staff staff = StaffDAO.getStaffById(staffId);
        StaffDAO.delete(staff);
        HttpSession session = request.getSession();
        List<Staff> listStaff = (List<Staff>) session.getAttribute("listStaff");
        //listEmployee.remove(employee);
        for (Staff s : listStaff) {
            if (s.getPersonID() == staff.getPersonID()) {
                listStaff.remove(s);
                break;
            }
        }

        session.setAttribute("listStaff", listStaff);
        //gửi mail thông báo xóa tài khoản
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            String subject = "Tạo tài khoản thành công!";
            String content = "Xin chào " + staff.getName() + ",\n\n"
                    + "Tài khoản nhân viên của bạn đã bị xóa khỏi hệ thống.\n"
                    + "Cảm ơn bạn đã làm việc tại hệ thống trong thời gian qua." + "\n\n"
                    + "Mọi thắc mắc vui lòng liên hệ Đặng Bá Hiền (0xxx-xxx-xxx)!";
            UtilsEmail.sendEmail(staff.getEmail(), subject, content);
        });
        executorService.shutdown();

        response.sendRedirect("listStaff");
    }
}
