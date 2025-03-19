import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../css/CompanyProfile.css'; // Sử dụng lại className và CSS từ CompanyProfile
import Navbar from './navbar';
import AppNavbar from './AppNavbar';
import Footer from './Footer';
import ApplicantInfo from './ApplicantInfo';
import Notifications from './Notifications';
import ChangePassword from './ChangePassword';
import AppSavedJob from './AppSavedJob';
import JobApplied from './JobApplied';
import { useAuth } from "../Contexts/AuthContext";
import TokenManager from "../utils/tokenManager";

const ApplicantProfile = () => {
  const { tab } = useParams();
  const [activeTab, setActiveTab] = useState(tab || 'Thông tin cá nhân');

  const [role, setRole] = useState(null); 
  const [userId, setUserId] = useState(null);

  const token = TokenManager.getToken();

  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase());
      setUserId(token.id);
    }
  }, [token]);

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const renderNavbar = () => {
    if (role === 'applicant') {
      return <AppNavbar />;
    } else {
      return <Navbar />;
    }
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'Thông tin cá nhân':
        return <ApplicantInfo userId={userId} />;
      case 'Thông báo':
        return <Notifications />;
      case 'Tin quan tâm':
        return <AppSavedJob />;
      case 'Bài đã ứng tuyển':
        return <JobApplied />;
      case 'Đổi mật khẩu':
        return <ChangePassword />;
      default:
        return null;
    }
  };

  return (
    <div>
      <AppNavbar/>
      <div id="company-profile-container">
        <div className="sidebar">
          <ul>
            {['Thông tin cá nhân', 'Thông báo', 'Tin quan tâm', 'Bài đã ứng tuyển', 'Đổi mật khẩu'].map((item) => (
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

export default ApplicantProfile;
