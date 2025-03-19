import axios from 'axios';
import { useAuth } from '../Contexts/AuthContext';
import TokenManager from '../utils/tokenManager';
import AuthService from '../services/authService';
//const baseURL = 'http://your-api';
const baseURL = 'http://localhost:8080';
//const REFRESH_TIMEOUT = 10000;
const axiosInstance = axios.create({
  baseURL,
  timeout: 30000,
  withCredentials: true,//cookkieeeeeeeee
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
}
});

axiosInstance.interceptors.request.use(
  async (config) => {
    const token = TokenManager.getToken()?.value;
    // Kiểm tra token hết hạn trước mỗi request
    if (token &&TokenManager.isTokenExpired()) {
      const shouldRelogin = window.confirm('Phiên làm việc đã hết hạn. Bạn có muốn đăng nhập lại không?');
      if (shouldRelogin) {
        window.location.href = '/login'; // Chuyển tới trang đăng nhập
      } else {
        TokenManager.clearAuth();
        window.location.href = '/';  // Về trang chủ
      }
      return Promise.reject('Phiên làm việc đã hết hạn');
    }
    
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 403) {
      console.error('Access denied: You do not have the necessary permissions.');
      alert('Bạn không có quyền truy cập vào tài nguyên này.');
      TokenManager.clearAuth();
      window.location.href = '/login';
      return Promise.reject(error);
    }

    if (error.response?.status === 401) {
      // Token hết hạn hoặc không hợp lệ
      alert('Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.');
      TokenManager.clearAuth();
      window.location.href = '/login';
      return Promise.reject(error);
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;