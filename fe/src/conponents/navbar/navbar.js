import React from 'react';
import './navbar.css';
import { Link } from 'react-router-dom';


function Navbar() {  
    return (
        <div id="header">
        <div id="nav">
        <div className='nav__logo'>
              <a className="navbar-brand-custom" href="#home">
                <img
                  src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXkI3X3ldqL1YTY2TZhVx-8QeRWlqvCiqtxg&s"
                  width="50"
                  height="50"
                  alt="logo"
                />
              </a>
            </div>

            <div className="nav__menu">
                <div className="menu-home menu-effect">
                    <a href="./">Trang chủ</a>
                </div>
                <div className="menu-job menu-effect auth-only">
                    <a href="./teacher-info.html">
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
                    <a href="./student-info.html">
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
                    <a href="./courses.html">
                        Công Cụ
                        <i className="fa-solid fa-angle-down"></i>
                    </a>
                    <ul className="subnav">
                        <li><a href="./">Danh sách khóa học</a></li>
                        <li><a href="./">Đăng kí mới</a></li>
                    </ul>
                    <div className="underline"></div>
                </div>
            </div>

            <div className="navbar-auth-custom">
              <p>Đăng nhập</p>
              <p>Đăng xuất</p>
            </div>
        </div>
    </div>
      );
}

export default Navbar;