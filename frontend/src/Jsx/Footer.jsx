import React from 'react';
import '../css/Footer.css';

function Footer() {
  return (
    <footer id="footer">
      <div className="footer-container">
        {/* Cột 1 - Thông tin công ty */}
        <div className="footer-column">
          <h3>Về Chúng Tôi</h3>
          <p>
            Công ty tuyển dụng hàng đầu, cung cấp hàng nghìn cơ hội việc làm từ các doanh nghiệp uy tín. 
            Sứ mệnh của chúng tôi là kết nối người tìm việc với các công ty hàng đầu.
          </p>
        </div>

        {/* Cột 2 - Liên kết quan trọng */}
        <div className="footer-column">
          <h3>Liên Kết Quan Trọng</h3>
          <ul>
            <li><a href="#">Tìm Việc</a></li>
            <li><a href="#">Về Chúng Tôi</a></li>
            <li><a href="#">Liên Hệ</a></li>
            <li><a href="#">Chính Sách Bảo Mật</a></li>
          </ul>
        </div>

        {/* Cột 3 - Liên hệ và Địa chỉ */}
        <div className="footer-column">
          <h3>Liên Hệ</h3>
          <p><i className="fas fa-map-marker-alt"></i> 123 Đường ABC, Quận 1, TP.HCM</p>
          <p><i className="fas fa-phone"></i> +84 123 456 789</p>
          <p><i className="fas fa-envelope"></i> support@company.com</p>
        </div>

        {/* Cột 4 - Mạng xã hội và Đăng ký */}
        <div className="footer-column">
          <h3>Kết Nối Với Chúng Tôi</h3>
          <div className="social-icons">
            <a href="#"><i className="fab fa-facebook-f"></i></a>
            <a href="#"><i className="fab fa-twitter"></i></a>
            <a href="#"><i className="fab fa-linkedin"></i></a>
            <a href="#"><i className="fab fa-instagram"></i></a>
          </div>
          {/* <h3>Đăng Ký Nhận Thông Báo</h3>
          <form className="newsletter-form">
            <input type="email" placeholder="Nhập email của bạn" />
            <button type="submit">Đăng Ký</button>
          </form> */}
        </div>
      </div>

      <div className="footer-bottom">
        <p>&copy; 2024 Công Ty Tuyển Dụng. All Rights Reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;
