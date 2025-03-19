import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../Contexts/AuthContext';
import PropTypes from 'prop-types';

const RoleBasedRoute = ({ allowedRoles }) => {
  const { isAuthenticated, role, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!role || !allowedRoles?.some((allowedRole) => 
    allowedRole?.toLowerCase() === role?.toLowerCase())
  ) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
};

RoleBasedRoute.propTypes = {
  allowedRoles: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default RoleBasedRoute;