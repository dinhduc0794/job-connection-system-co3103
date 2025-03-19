import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../Contexts/AuthContext';
import { useForm } from '../../hooks/useForm';
import { FiEye, FiEyeOff, FiUser, FiLock } from 'react-icons/fi';
import { motion } from 'framer-motion';
import { Link } from 'lucide-react';

const Login = () => {
  const navigate = useNavigate();
  const { handleLogin } = useAuth();
  const [showPassword, setShowPassword] = useState(false);

  const { formData, errors, loading, handleChange, handleSubmit } = useForm(
    {
      username: '',
      password: ''
    },
    async (formData) => {
      try {
        const result = await handleLogin(formData);
        if (!result.success) {
          throw new Error(result.error || 'Đăng nhập thất bại');
        }
      } catch (error) {
        throw new Error(error.message || 'Có lỗi xảy ra khi đăng nhập');
      }
    }
  );

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 via-purple-50 to-pink-100">
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="max-w-md w-full m-4"
      >
        <div className="bg-white p-8 rounded-2xl shadow-2xl space-y-8">
          {/* Logo và Tiêu đề */}
          <div className="text-center">
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ duration: 0.5 }}
              className="mx-auto h-16 w-16 bg-gradient-to-r from-indigo-500 to-purple-600 rounded-full flex items-center justify-center mb-4"
            >
              <FiUser className="h-8 w-8 text-white" />
            </motion.div>
            <h2 className="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
              Chào mừng trở lại
            </h2>
            <p className="mt-2 text-gray-500">Đăng nhập để tiếp tục</p>
          </div>

          <form className="space-y-6" onSubmit={handleSubmit}>
            {/* Input Username */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700 block">
                Tên đăng nhập
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiUser className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  id="username"
                  name="username"
                  type="text"
                  required
                  className={`block w-full pl-10 pr-3 py-2 border ${
                    errors.username ? 'border-red-500' : 'border-gray-300'
                  } rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 text-gray-900`}
                  placeholder="Nhập tên đăng nhập"
                  value={formData.username}
                  onChange={handleChange}
                />
              </div>
              {errors.username && (
                <motion.p 
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="text-sm text-red-600"
                >
                  {errors.username}
                </motion.p>
              )}
            </div>

            {/* Input Password */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700 block">
                Mật khẩu
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <FiLock className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  id="password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  required
                  className={`block w-full pl-10 pr-10 py-2 border ${
                    errors.password ? 'border-red-500' : 'border-gray-300'
                  } rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 text-gray-900`}
                  placeholder="Nhập mật khẩu"
                  value={formData.password}
                  onChange={handleChange}
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? (
                    <FiEyeOff className="h-5 w-5 text-gray-400 hover:text-gray-600" />
                  ) : (
                    <FiEye className="h-5 w-5 text-gray-400 hover:text-gray-600" />
                  )}
                </button>
              </div>
              {errors.password && (
                <motion.p 
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="text-sm text-red-600"
                >
                  {errors.password}
                </motion.p>
              )}
            </div>

            {/* Error Message */}
            {errors.submit && (
              <motion.div 
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                className="rounded-lg bg-red-50 p-4"
              >
                <p className="text-sm text-red-700">{errors.submit}</p>
              </motion.div>
            )}

            {/* Submit Button */}
            <motion.button
              whileHover={{ scale: 1.01 }}
              whileTap={{ scale: 0.99 }}
              type="submit"
              disabled={loading}
              className={`w-full flex justify-center py-3 px-4 rounded-lg text-white text-sm font-semibold
                ${loading 
                  ? 'bg-gray-400 cursor-not-allowed' 
                  : 'bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700'}
                transition-all duration-200 transform`}
            >
              {loading ? (
                <div className="flex items-center">
                  <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Đang xử lý...
                </div>
              ) : (
                'Đăng nhập'
              )}
            </motion.button>

            {/* Additional Links */}
            <div className="flex items-center justify-between text-sm">
            <button 
        onClick={() => navigate('/forgot-password')}
        className="font-medium text-indigo-600 hover:text-indigo-700 transition-colors"
      >
        Quên mật khẩu?
      </button>
      <button
        onClick={() => navigate('/register')}
        className="font-medium text-indigo-600 hover:text-indigo-700 transition-colors hover:scale-105 transform duration-200"
      >
        Đăng ký tài khoản
      </button>
            </div>
          </form>
        </div>
      </motion.div>
    </div>
  );
};

export default Login;