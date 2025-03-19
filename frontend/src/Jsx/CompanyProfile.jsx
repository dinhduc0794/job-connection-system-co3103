import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom'; // Import useParams
import '../css/CompanyProfile.css';
import Navbar from './navbar';
import AppNavbar from './AppNavbar';
import CompanyNavbar from './CompanyNavbar';
import Footer from './Footer';
import CompanyInfo from './CompanyInfo';
import CompanyJobManagement from './CompanyJobManager';
import Notifications from './Notifications';
import CompanyPostJob from './CompanyPostJob';
import ChangePassword from './ChangePassword';
import { useAuth } from "../Contexts/AuthContext";
import TokenManager from "../utils/tokenManager";

const CompanyProfile = () => {
  const { tab } = useParams(); // Lấy tham số "tab" từ URL
  const [activeTab, setActiveTab] = useState(tab || 'Thông tin công ty'); // Active tab mặc định
  
  const [userInfo, setUserInfo] = useState(null);
  const [role, setRole] = useState(null); // State để lưu role
  const [userId, setUserId] = useState(null); // State để lưu userId

  const token = TokenManager.getToken();
  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase()); // Lấy role từ token
      setUserId(token.id); // Lấy userId từ token
    }
  }, [token]);

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

    const renderNavbar = () => {
        if (role === 'applicant') {
          return <AppNavbar />;
        } else if (role === 'company') {
          return <CompanyNavbar />;
        } else {
          return <Navbar />;
        }
      };

  const renderContent = () => {
    switch (activeTab) {
      case 'Thông tin công ty':
        return <CompanyInfo />;
      case 'Quản lý việc làm':
        return <CompanyJobManagement />;
      case 'Thông báo':
        return <Notifications />;
      case 'Đăng tuyển việc làm':
        return <CompanyPostJob />;
      case 'Đổi mật khẩu':
        return <ChangePassword />;
      default:
        return null;
    }
  };

  return (
    <div>
      {renderNavbar()}
      <div id="company-profile-container">
        <div className="sidebar">
          <ul>
            {['Thông tin công ty', 'Quản lý việc làm', 'Thông báo', 'Đăng tuyển việc làm', 'Đổi mật khẩu'].map((item) => (
              <li
                key={item}
                className={activeTab === item ? 'active' : ''}
                onClick={() => handleTabClick(item)}
              >
                {item}
              </li>
            ))}
            <li onClick={() => alert('Đăng xuất')} className="logout">Đăng xuất</li>
          </ul>
        </div>
        <div className="content">{renderContent()}</div>
      </div>
      <Footer />
    </div>
  );
};

export default CompanyProfile;
