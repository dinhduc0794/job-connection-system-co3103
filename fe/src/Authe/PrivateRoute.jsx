import React from 'react';
import { Navigate } from 'react-router-dom';
import Loading from '../Jsx/Loading';
function PrivateRoute({ children, role }) {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('role');

  if (!token || userRole !== role) {
    return <Navigate to="/loading" />;
  }

  return children;
}

export default PrivateRoute;
