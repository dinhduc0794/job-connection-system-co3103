import api from '../../api/axios';
import { handleApiError } from '../../utils/errorHandle';
import TokenManager from '../../utils/tokenManager';
 class UserService {
  
  async getUsers() {
    try {
      const token = TokenManager.getToken();
      const response = await fetch('/users', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return {
        success: true,
        data: data
      };
    } catch (error) {
      return handleApiError(error, 'Không thể lấy danh sách người dùng');
    }
  }

  // Lấy thông tin người dùng theo ID
  async getUserById(id) {
    try {
      const token = TokenManager.getToken();
      const response = await fetch(`/users/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return {
        success: true,
        data: data
      };
    } catch (error) {
      return handleApiError(error, 'Không thể lấy thông tin người dùng');
    }
  }
  // Thêm người dùng mới
//   async addUser(userData) {
//     try {
//       const { phone, email, ...otherData } = userData;
//       const response = await api.post('/users', otherData, {
//         params: {
//           phone,
//           email
//         }
//       });
//       return {
//         success: true,
//         data: response.data,
//         message: 'Thêm người dùng thành công'
//       };
//     } catch (error) {
//       return handleApiError(error, 'Thêm người dùng thất bại');
//     }
//   }

//   // Cập nhật thông tin người dùng
//   async updateUser(id, userData) {
//     try {
//       const response = await api.put(`/users/${id}`, userData);
//       return {
//         success: true,
//         data: response.data,
//         message: 'Cập nhật thông tin thành công'
//       };
//     } catch (error) {
//       return handleApiError(error, 'Cập nhật thông tin thất bại');
//     }
//   }

//   // Xóa người dùng
//   async deleteUser(id) {
//     try {
//       const response = await api.delete(`/users/${id}`);
//       return {
//         success: true,
//         message: response.data.message || 'Xóa người dùng thành công',
//         detail: response.data.detail
//       };
//     } catch (error) {
//       return handleApiError(error, 'Xóa người dùng thất bại');
//     }
//   }

// //============================================== bổ sung 
// async updateUsedToWork(userId, workData) {
//   try {
//     const response = await api.post(`/users/${userId}/used-to-work`, workData);
//     return {
//       success: true,
//       data: response.data,
//       message: 'Cập nhật thông tin công việc thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Cập nhật thông tin công việc thất bại');
//   }
// }

// // Quản lý địa chỉ và ward
// async updateUserAddress(userId, addressData) {
//   try {
//     const response = await api.put(`/users/${userId}/address`, addressData);
//     return {
//       success: true,
//       data: response.data,
//       message: 'Cập nhật địa chỉ thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Cập nhật địa chỉ thất bại');
//   }
// }

// // Quản lý số điện thoại
// async addPhoneNumber(userId, phoneData) {
//   try {
//     const response = await api.post(`/users/${userId}/phones`, phoneData);
//     return {
//       success: true,
//       data: response.data,
//       message: 'Thêm số điện thoại thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Thêm số điện thoại thất bại');
//   }
// }

// async deletePhoneNumber(userId, phoneId) {
//   try {
//     await api.delete(`/users/${userId}/phones/${phoneId}`);
//     return {
//       success: true,
//       message: 'Xóa số điện thoại thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Xóa số điện thoại thất bại');
//   }
// }

// // Quản lý email
// async addEmail(userId, emailData) {
//   try {
//     const response = await api.post(`/users/${userId}/emails`, emailData);
//     return {
//       success: true,
//       data: response.data,
//       message: 'Thêm email thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Thêm email thất bại');
//   }
// }

// async deleteEmail(userId, emailId) {
//   try {
//     await api.delete(`/users/${userId}/emails/${emailId}`);
//     return {
//       success: true,
//       message: 'Xóa email thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Xóa email thất bại');
//   }
// }

// // Quản lý chặn người dùng
// async blockUser(userId, blockedUserId) {
//   try {
//     const response = await api.post(`/users/${userId}/block`, { blockedUserId });
//     return {
//       success: true,
//       data: response.data,
//       message: 'Chặn người dùng thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Chặn người dùng thất bại');
//   }
// }

// async unblockUser(userId, blockedUserId) {
//   try {
//     await api.delete(`/users/${userId}/block/${blockedUserId}`);
//     return {
//       success: true,
//       message: 'Bỏ chặn người dùng thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Bỏ chặn người dùng thất bại');
//   }
// }

// // Báo cáo người dùng
// async reportUser(userId, reportData) {
//   try {
//     const response = await api.post(`/users/${userId}/report`, reportData);
//     return {
//       success: true,
//       data: response.data,
//       message: 'Báo cáo người dùng thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Báo cáo người dùng thất bại');
//   }
// }

// // Cập nhật trạng thái public/private
// async updatePublicStatus(userId, isPublic) {
//   try {
//     const response = await api.put(`/users/${userId}/public-status`, { isPublic });
//     return {
//       success: true,
//       data: response.data,
//       message: 'Cập nhật trạng thái thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Cập nhật trạng thái thất bại');
//   }
// }

// // Cập nhật ảnh đại diện
// async updateUserImage(userId, imageFile) {
//   try {
//     const formData = new FormData();
//     formData.append('image', imageFile);
    
//     const response = await api.put(`/users/${userId}/image`, formData, {
//       headers: {
//         'Content-Type': 'multipart/form-data'
//       }
//     });
    
//     return {
//       success: true,
//       data: response.data,
//       message: 'Cập nhật ảnh đại diện thành công'
//     };
//   } catch (error) {
//     return handleApiError(error, 'Cập nhật ảnh đại diện thất bại');
//   }
// }
}

const userService = new UserService();
export default userService;