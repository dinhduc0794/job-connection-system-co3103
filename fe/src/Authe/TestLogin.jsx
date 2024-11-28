import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

function TestLogin() {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  
  return (
    <nav>
      {token ? (
        <>
          <span>Xin chào, {role}</span>
          <button onClick={handleLogout}>Đăng xuất</button>
        </>
      ) : (
        <Link to="/login">Đăng nhập</Link>
      )}
    </nav>
  );
}

export default TestLogin;
