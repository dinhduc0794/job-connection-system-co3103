import React from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion'; // Cần cài đặt: npm install framer-motion
import unauthImage from '../images/unadmin.svg';
import { FaEnvelope } from 'react-icons/fa';
const Unauthorized = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-gray-100 to-gray-200">
      <div className="max-w-2xl mx-auto p-8 text-center">
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ duration: 0.5 }}
        >
          {/* Icon hoặc hình ảnh */}
          <img 
            src= {unauthImage}
            alt="Unauthorized" 
            className="w-64 h-64 mx-auto mb-8"
          />
        </motion.div>

        <motion.h1 
          className="text-6xl font-bold mb-6 bg-gradient-to-r from-red-600 to-pink-600 text-transparent bg-clip-text"
          initial={{ y: -50, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.2 }}
        >
          403
        </motion.h1>

        <motion.h2
          className="text-4xl font-bold mb-4 text-gray-800"
          initial={{ y: -30, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.3 }}
        >
          Không có quyền truy cập
        </motion.h2>

        <motion.p 
          className="text-xl text-gray-600 mb-4" // Đổi mb-8 thành mb-4
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.4 }}
        >
          Bạn không có quyền truy cập vào trang này. Vui lòng liên hệ quản trị viên để được hỗ trợ.
        </motion.p>

        {/* Thêm phần email */}
        <motion.div
          className="flex items-center justify-center gap-2 mb-8 text-gray-600"
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.45 }}
        >
          <FaEnvelope className="text-blue-500" />
          <a 
            href="mailto:alextisgona@gmail.com?subject=Yêu cầu quyền truy cập&body=Xin chào, tôi cần được cấp quyền truy cập cho..."
            className="text-xl  text-red-500 hover:text-blue-700 transition-colors duration-300 underline"
            target="_blank"
            rel="noopener noreferrer"
          >
            alextisgona@gmail.com
          </a>
        </motion.div>
        
        <motion.div
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.5 }}
        >
          <button
            onClick={() => navigate('/')}
            className="bg-gradient-to-r from-blue-500 to-blue-700 text-white px-8 py-3 rounded-lg
                     text-lg font-semibold transition-all duration-300 transform hover:scale-105
                     hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
          >
            Về trang chủ
          </button>
        </motion.div>
      </div>
    </div>
  );
};

export default Unauthorized;