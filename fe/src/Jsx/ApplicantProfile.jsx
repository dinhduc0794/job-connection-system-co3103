import React, { useState, useEffect } from 'react';
import '../css/ApplicantProfile.css'
import Navbar from './navbar';
import AppNavbar from './AppNavbar';
import CompanyNavbar from './CompanyNavbar';
import Footer from './Footer';

const ApplicantProfile = () => {
  const [activeTab, setActiveTab] = useState('Thông tin');
  const [name, setName] = useState('Học Đăng');
  const [phone, setPhone] = useState('');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const handleUpdate = () => {
    // Xử lý cập nhật thông tin hoặc hiển thị thông báo thành công
    alert("Thông tin đã được cập nhật!");
  };
  const role = localStorage.getItem('role');
  const renderNavbar = () => {
    if (role === 'applicant') {
      return <AppNavbar />;
    } else if (role === 'company') {
      return <CompanyNavbar />;
    } else {
      return <Navbar />;
    }
  };

  return (
    <div>
      {renderNavbar()}
      <div id="applicant-profile-container">
        {/* Sidebar */}
        <div className="sidebar">
          <ul>
            {['Thông tin', 'Thông báo', 'Đăng tuyển việc làm', 'Đổi mật khẩu', 'Đăng xuất'].map((item) => (
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
          {activeTab === 'Thông tin' && ( 
            <div className='content-info'>
              <h3>Cài Đặt Thông Tin Cá Nhân</h3>

                <div>
                    <label>Họ Và Tên</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                </div>

                <div>
                    <label>Số Điện Thoạt</label>
                    <input
                        type="text"
                        placeholder="Nhập số điện thoại"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                    />
                </div>

                <div>
                    <label>Email</label>
                    <input type="text" value="abc@gmail.com" disabled  />
                </div>

            {/* Update Button */}
            <button className="update-btn" onClick={handleUpdate}>Cập nhật</button>
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

export default ApplicantProfile;
