import React, { useState, useEffect } from "react";
import '../css/AppNavBar.css';
import { Link, useNavigate } from 'react-router-dom';
import TokenManager from '../utils/tokenManager';
import { useAuth } from "../Contexts/AuthContext";

function CompanyNavbar() {
    const { handleLogout, isAuthenticated } = useAuth();
    const navigate = useNavigate(); // Hook điều hướng
    const [role, setRole] = useState(null); // State để lưu role
    const [userId, setUserId] = useState(null); // State để lưu userId
    const [userInfo, setUserInfo] = useState(null);
    const token = TokenManager.getToken();
    const [companyInfo, setCompanyInfo] = useState(null); // State để lưu thông tin công ty
    useEffect(() => {
        if (token) {
            setRole(token.role?.toLowerCase()); // Lấy role từ token
            setUserId(token.id); // Lấy userId từ token
        }
    }, [token]);

    // Lấy thông tin công ty từ API
    useEffect(() => {
        const fetchCompanyInfo = async () => {
            try {
                const response = await fetch(`http://47.128.243.193:8080/public/companies/${userId}`);
                const data = await response.json();
                setCompanyInfo(data); // Lưu dữ liệu vào state
            } catch (error) {
                console.error("Error fetching company info:", error);
            }
        };

        fetchCompanyInfo();
    }, [userId]); // Chạy lại khi userId thay đổi

    const handleNavigate = (tab) => {
        navigate(`/CompanyProfile/${tab}`); // Điều hướng với tham số tab
    };

    const onLogoutClick = () => {
        const confirmLogout = window.confirm('Bạn có chắc chắn muốn đăng xuất?');
        if (confirmLogout) {
            handleLogout();
        }
    };

    return (
        <div className="headernav">
            <div className="nav">
                <div className='nav__logo'>
                    <a className="navbar-brand-custom" href="/">
                        <img
                            src="https://images.vexels.com/media/users/3/245747/isolated/preview/fc5e5179e126bb8b8878c65ed0639179-great-job-badge.png"
                            width="40"
                            height="40"
                            alt="logo"
                        />
                    </a>
                </div>

                <div className="nav__menu">
                    <div className="menu-home menu-effect">
                        <a href="/">Trang chủ</a>
                    </div>
                    <div className="menu-job menu-effect auth-only">
                        <a href="/alljob">
                            Việc Làm
                            <i className="fa-solid fa-angle-down"></i>
                        </a>
                        <ul className="subnav">
                            <li><Link to="/alljob">Toàn Bộ Việc Làm</Link></li>
                            <li><a href="./">Việc Làm IT</a></li>
                            <li><a href="./">Việc Làm Hot</a></li>
                        </ul>
                        <div className="underline"></div>
                    </div>
                    <div className="menu-company menu-effect student-only">
                        <a href="/allcompany">
                            Công Ty
                            <i className="fa-solid fa-angle-down"></i>
                        </a>
                        <ul className="subnav">
                            <li><Link to="/allcompany">Toàn Bộ Công Ty</Link></li>
                            <li><a href="./">Công Ty Hàng Đầu</a></li>
                            <li><a href="./">Công Ty IT</a></li>
                        </ul>
                        <div className="underline"></div>
                    </div>
                    <div className="menu-courses menu-effect student-only">
                        <a href="./">
                            Công Cụ
                            <i className="fa-solid fa-angle-down"></i>
                        </a>
                        <ul className="subnav">
                            <li><a href="./">Giới thiệu</a></li>
                            <li><a href="./">Đăng kí VIP</a></li>
                        </ul>
                        <div className="underline"></div>
                    </div>
                </div>

                <div className="nav__icons">
                    <div className="nav__notification">
                        <img
                            onClick={() => handleNavigate('Thông báo')}
                            src='https://png.pngtree.com/png-clipart/20200224/original/pngtree-bell-alarm-line-icon-vector-png-image_5247801.jpg'
                            className='bell-notifi'
                        />
                    </div>

                    <div className="nav__avatar">
                        <img
                            src="./images/default.png"
                            alt=""
                            className="avatar-icon"
                        />
                        <ul className="dropdown-menu">
                            <div className='infor-application'>
                                {/* Hiển thị thông tin công ty nếu đã có dữ liệu */}
                                {companyInfo ? (
                                    <>
                                        <h4>{companyInfo.name}</h4>
                                        <p>{companyInfo.taxCode}</p>
                                    </>
                                ) : (
                                    <p>Loading...</p>
                                )}
                            </div>
                            <li>
                                <i className="fa-solid fa-user-cog"></i>
                                <a onClick={() => handleNavigate('Thông tin công ty')}>Cài đặt thông tin cá nhân</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-crown"></i>
                                <a href="/">Nâng cấp tài khoản VIP</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-crown"></i>
                                <a onClick={() => handleNavigate('Quản lý việc làm')}>Quản lý việc làm</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-bell"></i>
                                <a onClick={() => handleNavigate('Thông báo')}>Thông Báo</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-bell"></i>
                                <a onClick={() => handleNavigate('Đăng tuyển việc làm')}>Đăng tuyển việc làm</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-key"></i>
                                <a onClick={() => handleNavigate('Đổi mật khẩu')}>Đổi mật khẩu</a>
                            </li>
                            <li className="logout">
                                <i className="fa-solid fa-sign-out-alt"></i>
                                <a onClick={onLogoutClick}>Đăng xuất</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CompanyNavbar;
