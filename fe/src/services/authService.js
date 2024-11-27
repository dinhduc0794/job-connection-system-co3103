import api from '../api/axios';
import { handleApiError } from '../utils/errorHandle';
import TokenManager from '../utils/tokenManager';

class AuthService {
     constructor()
     {
       this.tokenManager =  TokenManager;
     }
  

  

  // Auth API calls
  async login(credentials) {
    try {
        console.log('Login credentials:', credentials); // Log credentials gửi đi

        const response = await api.post('/login', credentials);
        console.log('Server response:', response.data); // Log response từ server

        const { token, user, expiresIn } = response.data; // Không cần nhận refreshToken từ server
        
        if (!token || !user || !user.role) {
            console.error('Invalid server response format:', response.data);
            throw new Error('Invalid response from server');
        }

        // Lưu access token và thông tin người dùng
        this.tokenManager.setToken(token, user.role, expiresIn || 3600); // expiresIn mặc định là 1h nếu server không trả về
        this.tokenManager.setUserInfo(user);

        return { 
            success: true, 
            token,
            user,
            message: 'Đăng nhập thành công'
        };
    } catch (error) {
        console.error('Login error:', error);
        if (error.response) {
            const { status, data } = error.response;
            
            switch (status) {
                case 401:
                    return {
                        success: false,
                        error: 'Tên đăng nhập hoặc mật khẩu không đúng'
                    };
                case 403:
                    return {
                        success: false,
                        error: 'Tài khoản của bạn đã bị khóa'
                    };
                case 404:
                    return {
                        success: false,
                        error: 'Không tìm thấy tài khoản'
                    };
                default:
                    return {
                        success: false,
                        error: data.message || 'Đăng nhập thất bại'
                    };
            }
        }
        return {
            success: false,
            error: error.message || 'Không thể kết nối đến server'
        };
    }
}

  async registerApplicant(userData) {
    try {
      const response = await api.post('/register/applicant', userData);
      return {
        success: true,
        data: response.data,
        message: 'Đăng ký ứng viên thành công'
      };
    } catch (error) {
      return handleApiError(error, 'Đăng ký ứng viên thất bại');
    }
  }
  isAuthenticated() {
    const token = TokenManager.getToken();
    const user = TokenManager.getUserInfo();
    return !!(token && user);
  }
  
  // Đăng ký company
  async registerCompany(userData) {
    try {
      const response = await api.post('/register/company', userData);
      return {
        success: true,
        data: response.data,
        message: 'Đăng ký công ty thành công'
      };
    } catch (error) {
      return handleApiError(error, 'Đăng ký công ty thất bại');
    }
  }

  async refreshToken() {
      try {
        
        const response = await api.post('/refresh');/// chưa viết nè 
        const { token } = response.data;

        if (!token) {
            throw new Error('Invalid response from server');
        }

        this.tokenManager.setToken(token, 'user_role_placeholder', 3600); 
        return { success: true, token };
    } catch (error) {
        return handleApiError(error, 'Làm mới token thất bại');
    }
  }
  
   logout() {
    this.tokenManager.clearAuth();
  }

  async verifyToken() {
    try {
      const response = await api.get('/api/auth/verify');/////////////// chưa viết nè 
      return { success: true, data: response.data };
    } catch (error) {
      return handleApiError(error, 'Token không hợp lệ');
    }
  }
}

const authService = new AuthService();
export default authService;