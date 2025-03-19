import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService, userService } from '../services';
import { useNavigate , useLocation } from 'react-router-dom';
import TokenManager from '../utils/tokenManager';
import { jwtDecode } from 'jwt-decode';
const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [authState, setAuthState] = useState({
    isAuthenticated: false,
    id : null,
    loading: true,
    error: null,
    role:null
  });
  const navigate = useNavigate();
  const location = useLocation();
  useEffect(() => {
    const initializeAuth = () => {
        const tokenData = TokenManager.getToken();
        if (tokenData) {
            setAuthState({
                isAuthenticated: true,
                id: tokenData.id,
                role: tokenData.role,
                loading: false,
                error: null,
            });
        } else {
            setAuthState({
                isAuthenticated: false,
                id: null,
                role: null,
                loading: false,
                error: null,
            });
        }
    };

    initializeAuth();
}, []);
useEffect(() => {
  let warningTimeout;

  const setupExpirationWarning = () => {
      const tokenData = TokenManager.getToken();
      if (!tokenData?.value) return;
      
      // Không cần jwtDecode nữa vì tokenData đã có thông tin
      const timeLeft = tokenData.expires - Date.now();
      
      if (timeLeft > 0) {
          const warningTime = timeLeft - (60 * 1000); // Cảnh báo trước 1 phút
          if (warningTime > 0) {
              warningTimeout = setTimeout(() => {
                  alert('Phiên làm việc của bạn sắp hết hạn. Vui lòng đăng nhập lại.');
                  handleLogout();
              }, warningTime);
          }
      }
  };

  if (authState.isAuthenticated) {
      setupExpirationWarning();
  }

  // Cleanup function
  return () => {
      if (warningTimeout) {
          clearTimeout(warningTimeout);
      }
  };
}, [authState.isAuthenticated]);

  useEffect(() => {
    const publicRoutes = ['/login', '/register', '/alljob', '/alljob/:id', '/allcompany', '/allcompany/:id', '/JobDetail/:id'];
    
    // Chỉ kiểm tra khi chưa xác thực và không ở public route
    if (!authState.isAuthenticated && !publicRoutes.some(route => {
      if (route.includes(':')) {
        const baseRoute = route.split('/:')[0];
        return location.pathname.startsWith(baseRoute);
      }
      return location.pathname === route;
    })) {
      checkAuthStatus();
    } else {
      setAuthState(prev => ({
        ...prev,
        loading: false,
      }));
    }
  }, [location.pathname, authState.isAuthenticated]);
  
  // useEffect(() => {
    // let warningTimeout;

    // const setupExpirationWarning = () => {
    //   const tokenData = TokenManager.getToken();
    //   if (!tokenData) return;

    //   const timeLeft = tokenData.expires - Date.now();
    //   const warningTime = timeLeft - (60 * 1000); // Cảnh báo trước 1 phút

    //   if (warningTime > 0) {
    //     warningTimeout = setTimeout(() => {
    //       alert('Phiên làm việc của bạn sắp hết hạn. Vui lòng đăng nhập lại.');
    //     }, warningTime);
    //   }
    // };

    // if (authState.isAuthenticated) {
    //   setupExpirationWarning();
    // }

    // return () => {
    //   if (warningTimeout) {
    //     clearTimeout(warningTimeout);
    //   }
    // };
  // }, [authState.isAuthenticated]);

  // const updateAuthState = (updates) => {
  //   setAuthState((prev) => ({
  //     ...prev,
  //     id,
  //     role: updates.role,
  //     isAuthenticated: true
  //   }));
  // };

  const checkAuthStatus = async () => {
    try {
      const token = TokenManager.getToken();
      
      if (!token) {
        setAuthState(prev => ({
          ...prev,
          isAuthenticated: false,
          loading: false,
          id: null,
          role: null
        }));
        return;
      }
  
      const id = TokenManager.getUserId();
      const role = TokenManager.getUserRole();
      console.log('Retrieved user info and role:', { id, role });
  
      // updateAuthState({
      //   isAuthenticated: true,
      //   loading: false,
      //   user: userInfo,
      //   role: roleid
      // });
    } catch (error) {
      console.error('Auth status check error:', error);
      // ...
    }
  };

  const handleLogin = async (credentials) => {
    console.log('Login started with credentials:', credentials);
    
    // Set loading state
    setAuthState(prev => ({
      ...prev,
      loading: true,
      error: null
    }));
  
    try {
      const result = await authService.login(credentials);
      console.log('Login API response:', result);
  
      
      if (!result || !result.token || !result.role ) {
        throw new Error('Invalid response format');
      }
  
      
      
  
      // Lưu token và user info
      TokenManager.setToken(result.role, result.token, result.id);
      // TokenManager.setUserId(result.id);
      // TokenManager.setUserRole(result.role);
      
  
      // Update auth state
      setAuthState(prev => ({
        ...prev,
        isAuthenticated: true,
        id: result.id,
        role: result.role,
        loading: false,
        error: null
      }));
  
      // Redirect based on role
      if (result.role === 'applicant') {
        navigate('/ApplicantProfile');
      } else if (result.role === 'admin') {
        navigate('/dashboard');
      } else if (result.role === 'company') {
        navigate('/CompanyProfile');
      }
  
    } catch (error) {
      console.error('Login error:', error);
      
      setAuthState(prev => ({
        ...prev,
        isAuthenticated: false,
        id : null,
        role: null,
        loading: false,
        error: 'Đăng nhập thất bại. Vui lòng thử lại.'
      }));
    }
  };

  const handleLogout = async () => {
    try {
      // Xóa token và thông tin người dùng
      TokenManager.clearAuth();
      
      // Cập nhật state
      setAuthState({
        isAuthenticated: false,
        id: null,
        role: null,
        loading: false,
        error: null
      });

      // Chuyển hướng về trang login
      navigate('/login');
      
    } catch (error) {
      console.error('Lỗi đăng xuất:', error);
      setAuthState(prev => ({
        ...prev,
        error: 'Đăng xuất thất bại. Vui lòng thử lại.'
      }));
    }
};
  const hasRole = (requiredRole) => {
    return authState.role === requiredRole;
  };
  const canAccess = (path) => {
    if (!authState.isAuthenticated || !authState.role) return false;

    
    if (path.startsWith('/admin') && authState.role === 'admin') return true;
    if (path.startsWith('/applicants') && authState.role === 'applicant') return true;
    if (path.startsWith('/companies') && authState.role === 'company') return true;
    
    return false;
  };

  const value = {
    ...authState,
    handleLogin,
    handleLogout,
    hasRole,
    canAccess
  };

  if (authState.loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <p>Đang kiểm tra trạng thái đăng nhập...</p>
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900" />
      </div>
    );
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};