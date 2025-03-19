import CryptoJS from 'crypto-js';
import { jwtDecode } from 'jwt-decode';  
let memoryStorage = {};
const ENCRYPTION_KEY = process.env.REACT_APP_ENCRYPTION_SECRET || 'default-secret-key';

const STORAGE_KEYS = {
    AUTH_TOKEN: 'auth_token',
    USER_ROLE: 'user_role',
    USER_ID: 'user_id'
};


////////////////////////// đúng không thể sai 
export const TokenManager = {
   
   encrypt: (data) => {
        return CryptoJS.AES.encrypt(JSON.stringify(data), ENCRYPTION_KEY).toString();
    },

    decrypt: (encryptedData) => {
        try {
            const bytes = CryptoJS.AES.decrypt(encryptedData, ENCRYPTION_KEY);
            return JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
        } catch (error) {
            console.error('Decryption error:', error);
            return null;
        }
    },
    hasRole: (role) => TokenManager.getUserRole()?.toLowerCase() === role.toLowerCase(),
    canAccess: (path) => {
        const userRole = TokenManager.getUserRole();
        if (!userRole) return false;
    
      
        switch(userRole.toLowerCase()) {
            case 'admin':
                return path.startsWith('/dashboard');
                
            case 'applicant':
                return path.startsWith('/ApplicantProfile') || 
                       path.startsWith('/saved-jobs');
                
            case 'company':
                return path.startsWith('/CompanyProfile');
                
            default:
                return path.startsWith('/');
        }
    },
    // Cập nhật các phương thức để sử dụng memoryStorage
    // 
    setToken: (role,token,id ) => {
        console.log('TokenManager: Setting token with role:', role);
        try {
            if (!token || !role) {
                throw new Error('Token và role không được để trống');
            }
            const validRoles = ['admin', 'applicant', 'company'];
            if (!validRoles.includes(role.toLowerCase())) {
                throw new Error('Role không hợp lệ');
            }
            const decoded = jwtDecode(token);
            const tokenData = {               
                role: role,
                value: token,
                id: id,
                expires: decoded.exp * 1000
            };
            const encryptedData = TokenManager.encrypt(tokenData);
            memoryStorage.token = encryptedData;////// token sẽ lưu caid cần mã hóa 
            TokenManager.setUserRole(role);
            TokenManager.setUserId(id);
            localStorage.setItem('auth_token', encryptedData);
            // Lưu role
        } catch (error) {
            console.error('Error setting token:', error);
            throw new Error('Invalid token format');
        }
    },

    setUserRole: (role) => {
        //memoryStorage.userRole = TokenManager.encrypt(role);
        memoryStorage.role = role;/// user role để lưu role 
    },

    getUserRole: () => memoryStorage.role || TokenManager.getToken()?.role,

    

    clearAuth: () => {
        memoryStorage = {};
        localStorage.removeItem('auth_token');
    },

    getToken: () => {
        // Ưu tiên lấy từ memoryStorage
        try {
            // Lấy token từ memory hoặc localStorage
            const encryptedTokenData = memoryStorage.token || localStorage.getItem('auth_token');
            if (!encryptedTokenData) return null;

            const tokenData = TokenManager.decrypt(encryptedTokenData);
            if (!tokenData) {
                TokenManager.clearAuth();
                return null;
            }

            // Kiểm tra token hết hạn
            const decoded = jwtDecode(tokenData.value);
            const currentTime = Date.now() / 1000;
            
            if (decoded.exp < currentTime) {
                // Token đã hết hạn
                TokenManager.clearAuth();
                return null;
            }

            // Đồng bộ lại memoryStorage nếu cần
            if (!memoryStorage.token) {
                memoryStorage.token = encryptedTokenData;
                memoryStorage.role = tokenData.role;
                memoryStorage.id = tokenData.id;
                memoryStorage.expires = decoded.exp * 1000;
            }

            return tokenData;
        } catch (error) {
            console.error('Error in getToken:', error);
            TokenManager.clearAuth();
            return null;
        }
    },

    removeToken: () => {
        delete memoryStorage.token; 
        delete memoryStorage.id;
        delete memoryStorage.role;
    },

    setUserId: (id) => {
        // memoryStorage.userInfo = TokenManager.encrypt(userInfo);
        memoryStorage.id = id;// gồm role và id 
    },

    getUserId: () => memoryStorage.id || TokenManager.getToken()?.id,

    isTokenExpired: () => {
        try {
            const tokenData = TokenManager.getToken();
            if (!tokenData?.value) return true;
            
            const decodedToken = jwtDecode(tokenData.value);
            const currentTime = Date.now() / 1000; // Chuyển về giây để so sánh với exp
            
            // So sánh thời gian hiện tại với thời gian hết hạn từ token
            return decodedToken.exp < currentTime;
        } catch (error) {
            console.error("Error checking token expiration:", error);
            return true;
        }
    }
   
};

export default TokenManager;