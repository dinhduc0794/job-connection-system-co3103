import React, { useState, useEffect } from "react";
import axios from 'axios';
import '../css/AppNavBar.css';
import { Link } from 'react-router-dom';
import { useAuth } from "../Contexts/AuthContext";
import TokenManager from "../utils/tokenManager";

function AppNavbar() {
    const { handleLogout, isAuthenticated } = useAuth();
    const [role, setRole] = useState(null); // State để lưu role
    const [userId, setUserId] = useState(null); // State để lưu userId
    const [userInfo, setUserInfo] = useState({ firstName: '', lastName: '', email: '' }); // Lưu name và email
    const token = TokenManager.getToken();

    useEffect(() => {
        if (token) {
            setRole(token.role?.toLowerCase()); // Lấy role từ token
            setUserId(token.id); // Lấy userId từ token
        }
    }, [token]);

    useEffect(() => {
        // Fetch thông tin người dùng từ API
        if (userId) {
            axios
                .get(`/public/applicants/${userId}`)
                .then(response => {
                    const { firstName, lastName, emails } = response.data;
                    setUserInfo({
                        firstName,
                        lastName,
                        email: emails.length > 0 ? emails[0].email : '',
                    });
                })
                .catch(error => {
                    console.error('Lỗi khi lấy dữ liệu người dùng:', error);
                });
        }
    }, [userId]);

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
                            src='https://png.pngtree.com/png-clipart/20200224/original/pngtree-bell-alarm-line-icon-vector-png-image_5247801.jpg'
                            className='bell-notifi'
                        />
                    </div>

                    <div className="nav__avatar">
                        <img 
                            src="https://meatworld.info/wp-content/uploads/avatar-trang-16.jpg" 
                            alt={`${userInfo.firstName} ${userInfo.lastName}`}
                            className="avatar-icon"
                        />
                        <ul className="dropdown-menu">
                            <div className='infor-application'>
                                <h4>{`${userInfo.firstName} ${userInfo.lastName}`}</h4>
                                <p>{userInfo.email}</p>
                            </div>
                            <li>
                                <i className="fa-solid fa-user-cog"></i>
                                <Link to="/ApplicantProfile">Cài đặt thông tin cá nhân</Link>
                            </li>
                            <li>
                                <i className="fa-solid fa-crown"></i>
                                <a href="/">Nâng cấp tài khoản VIP</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-bell"></i>
                                <a href="/">Thông Báo</a>
                            </li>
                            <li>
                                <i className="fa-solid fa-key"></i>
                                <a href="/">Đổi mật khẩu</a>
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

export default AppNavbar;
