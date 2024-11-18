import React, { useState } from 'react';
import '../css/CompanyProfile.css';
import Navbar from './navbar';
import Footer from './Footer';

const CompanyProfile = () => {
  const [activeTab, setActiveTab] = useState('Thông tin công ty');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <div>
      <Navbar/>
      <div id="company-profile-container">
        {/* Sidebar */}
        <div className="sidebar">
          <ul>
            {['Thông tin công ty', 'Quản lý việc làm', 'Thông báo', 'Hồ sơ', 'Đăng tuyển việc làm', 'Đổi mật khẩu', 'Xem thông tin', 'Đăng xuất'].map((item) => (
              <li
                key={item}
                className={activeTab === item ? 'active' : ''}
                onClick={() => handleTabClick(item)}
              >
                {item}
              </li>
            ))}
          </ul>
        </div>

        {/* Main Content */}
        <div className="content">
          {activeTab === 'Thông tin công ty' && ( 
            <div className='content-info'>
              <h2>Welcome Decathlon VN</h2>
              <h3>Thông tin cơ bản</h3>

              {/* Company Logo and Upload Button */}
                <div className="company-info">
                  <img src="https://via.placeholder.com/150" alt="Company Logo" className="company-logo" />
                  <button className="upload-btn">Tải lên</button>
                  <p className="file-info">
                    Max file size is 1MB, Minimum dimension: 300x300 And Suitable files are .jpg & .png
                  </p>
                </div>

                {/* Company Details */}
                <div className="company-details">
                  <label>Tên Công Ty</label>
                  <input type="text" value="Decathlon VN" readOnly />

                  <label>Description</label>
                  <textarea rows="5" cols="50" defaultValue="Decathlon là một trong những nhà bán lẻ các mặt hàng về thể thao lớn nhất thế giới..." />
                </div>

                {/* Contact Information */}
                <div className="contact-info">
                  <h3>Contact Information</h3>

                  <label>Phone Number</label>
                  <input type="text" defaultValue="028 3840 5336" />

                  <label>Email</label>
                  <input type="email" defaultValue="decathlon@gmail.com" />

                  <label>Website</label>
                  <input type="url" defaultValue="https://www.decathlon.vn/vi/" />

                  <div className="location-info">
                    <div>
                      <label>City/Province</label>
                      <select>
                        <option>Ho Chi Minh</option>
                        {/* Add more options as needed */}
                      </select>
                    </div>
                    <div>
                      <label>District/Town</label>
                      <select>
                        <option>Cu Chi District</option>
                        {/* Add more options as needed */}
                      </select>
                    </div>
                  </div>

                  <label>Address</label>
                  <textarea rows="2" defaultValue="Pearl Plaza, 561A Điện Biên Phủ, Phường 25, Bình Thạnh, Hồ Chí Minh"></textarea>
                </div>

            {/* Update Button */}
            <button className="update-btn">Cập nhật</button>
            </div>
          )}

          {activeTab === 'Quản lý việc làm' &&(
            <div>
              <h2>Các Bài Đã Đăng</h2>
            </div>
             
          )}

          {activeTab === 'Thông báo' && (
            <div>
              <h2>Thông báo</h2>
              <p>There is no record in transaction list</p>
            </div>
          )}

          {activeTab === 'Hồ sơ' &&(
            <div>
              <h2>Hồ Sơ</h2>
            </div>
             
          )}

          {activeTab === 'Đăng tuyển việc làm' &&(
            <div>
              <h2>Đăng Bài Tuyển Dụng</h2>
            </div>
             
          )}

          {activeTab === 'Đổi mật khẩu' &&(
            <div className='change-pass'>
              <h2>Đổi Mật Khẩu</h2>
              <label>Mật Khẩu Cũ</label>
              <input type="text" defaultValue="********" />

              <label>Mật Khẩu Mới</label>
              <input type="text" defaultValue="********" />

              <label>Xác Nhận Mật Khẩu</label>
              <input type="text" defaultValue="********" />

            </div>
             
          )}

          {activeTab === 'Xem thông tin' &&(
            <div>
              <h2>Xem Thông Tin</h2>
            </div>
             
          )}

          {activeTab === 'Đăng xuất' && <h2>Logout Content</h2>}
        </div>
      </div>
      <Footer/>
    </div>
  );
};

export default CompanyProfile;
