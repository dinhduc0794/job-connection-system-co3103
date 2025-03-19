import React, { useState, useCallback ,useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { FiUser, FiMail, FiLock, FiEye, FiEyeOff, FiPhone, FiMapPin, FiFileText, FiImage } from 'react-icons/fi';
import { useDropzone } from 'react-dropzone';
import Select from 'react-select';

const Register = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [previewImage, setPreviewImage] = useState(null);
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    fullName: '',
    phone: '',
    bio: '',
    avatar: null,
    role: 'applicant',
    province: null,
    district: null,
    ward: null
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
   
  // Dropzone configuration
  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];
    setFormData(prev => ({ ...prev, avatar: file }));
    setPreviewImage(URL.createObjectURL(file));
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: 'image/*',
    maxFiles: 1
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    // Tạo FormData để gửi file
    const submitData = new FormData();
    Object.keys(formData).forEach(key => {
      submitData.append(key, formData[key]);
    });

    try {
      const response = await fetch('/api/register', {
        method: 'POST',
        body: submitData
      });

      if (response.ok) {
        navigate('/login');
      } else {
        const error = await response.json();
        setErrors({ submit: error.message });
      }
    } catch (error) {
      setErrors({ submit: 'Đăng ký thất bại' });
    } finally {
      setLoading(false);
    }
  };
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear error khi user bắt đầu gõ
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  // Hàm xử lý thay đổi tỉnh/thành phố
  const handleProvinceChange = async (selectedOption) => {
    setFormData(prev => ({
      ...prev,
      province: selectedOption,
      district: null,
      ward: null
    }));
    
    // Reset quận/huyện và phường/xã
    setDistricts([]);
    setWards([]);

    if (selectedOption) {
      try {
        // Gọi API lấy danh sách quận/huyện
        const response = await fetch(`/api/districts/${selectedOption.value}`);
        const data = await response.json();
        setDistricts(data.map(district => ({
          value: district.id,
          label: district.name
        })));
      } catch (error) {
        console.error('Error fetching districts:', error);
      }
    }
  };

  // Hàm xử lý thay đổi quận/huyện
  const handleDistrictChange = async (selectedOption) => {
    setFormData(prev => ({
      ...prev,
      district: selectedOption,
      ward: null
    }));
    
    // Reset phường/xã
    setWards([]);

    if (selectedOption) {
      try {
        // Gọi API lấy danh sách phường/xã
        const response = await fetch(`/api/wards/${selectedOption.value}`);
        const data = await response.json();
        setWards(data.map(ward => ({
          value: ward.id,
          label: ward.name
        })));
      } catch (error) {
        console.error('Error fetching wards:', error);
      }
    }
  };

  // Hàm xử lý thay đổi phường/xã
  const handleWardChange = (selectedOption) => {
    setFormData(prev => ({
      ...prev,
      ward: selectedOption
    }));
  };

  // Fetch danh sách tỉnh/thành phố khi component mount
  useEffect(() => {
    const fetchProvinces = async () => {
      try {
        const response = await fetch('/api/provinces');
        const data = await response.json();
        setProvinces(data.map(province => ({
          value: province.id,
          label: province.name
        })));
      } catch (error) {
        console.error('Error fetching provinces:', error);
      }
    };

    fetchProvinces();
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-purple-50 to-pink-100 py-12 px-4 sm:px-6 lg:px-8">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="max-w-4xl mx-auto bg-white rounded-2xl shadow-2xl overflow-hidden"
      >
        <div className="grid grid-cols-1 lg:grid-cols-2">
          {/* Left Side - Form */}
          <div className="p-8">
            <div className="text-center mb-8">
              <h2 className="text-3xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                Tạo tài khoản mới
              </h2>
              <p className="mt-2 text-gray-500">Bắt đầu hành trình của bạn với chúng tôi</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Avatar Upload */}
              <div className="text-center">
                <div
                  {...getRootProps()}
                  className={`mt-2 flex justify-center rounded-lg border border-dashed border-gray-900/25 px-6 py-10 cursor-pointer
                    ${isDragActive ? 'border-indigo-500 bg-indigo-50' : ''}`}
                >
                  <div className="text-center">
                    {previewImage ? (
                      <img
                        src={previewImage}
                        alt="Preview"
                        className="mx-auto h-32 w-32 rounded-full object-cover"
                      />
                    ) : (
                      <FiImage className="mx-auto h-12 w-12 text-gray-300" />
                    )}
                    <div className="mt-4 flex text-sm leading-6 text-gray-600">
                      <input {...getInputProps()} />
                      <span>Kéo thả ảnh hoặc click để chọn</span>
                    </div>
                  </div>
                </div>
              </div>

              {/* Basic Info */}
              <div className="grid grid-cols-1 gap-6">
                <InputField
                  icon={<FiUser />}
                  name="fullName"
                  label="Họ và tên"
                  value={formData.fullName}
                  onChange={handleChange}
                  error={errors.fullName}
                />

                <InputField
                  icon={<FiMail />}
                  name="email"
                  type="email"
                  label="Email"
                  value={formData.email}
                  onChange={handleChange}
                  error={errors.email}
                />

                <InputField
                  icon={<FiPhone />}
                  name="phone"
                  label="Số điện thoại"
                  value={formData.phone}
                  onChange={handleChange}
                  error={errors.phone}
                />

                {/* Password Fields */}
                <PasswordField
                  value={formData.password}
                  onChange={handleChange}
                  showPassword={showPassword}
                  setShowPassword={setShowPassword}
                  error={errors.password}
                />

                <PasswordField
                  name="confirmPassword"
                  label="Xác nhận mật khẩu"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  showPassword={showConfirmPassword}
                  setShowPassword={setShowConfirmPassword}
                  error={errors.confirmPassword}
                />

                {/* Bio */}
                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Giới thiệu bản thân
                  </label>
                  <textarea
                    name="bio"
                    rows={4}
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                    value={formData.bio}
                    onChange={handleChange}
                  />
                </div>

                {/* Address Selection */}
                <div className="grid grid-cols-1 gap-4">
                  <Select
                    placeholder="Chọn Tỉnh/Thành phố"
                    options={provinces}
                    onChange={(option) => handleProvinceChange(option)}
                    className="react-select"
                  />
                  <Select
                    placeholder="Chọn Quận/Huyện"
                    options={districts}
                    onChange={(option) => handleDistrictChange(option)}
                    className="react-select"
                  />
                  <Select
                    placeholder="Chọn Phường/Xã"
                    options={wards}
                    onChange={(option) => handleWardChange(option)}
                    className="react-select"
                  />
                </div>
              </div>

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
                ) : 'Đăng ký'}
              </motion.button>
            </form>

            <div className="mt-6 text-center">
              <button
                onClick={() => navigate('/login')}
                className="text-sm font-medium text-indigo-600 hover:text-indigo-500"
              >
                Đã có tài khoản? Đăng nhập ngay
              </button>
            </div>
          </div>

          {/* Right Side - Image/Info */}
          <div className="hidden lg:block relative">
            <div className="absolute inset-0 bg-gradient-to-br from-indigo-600 to-purple-600">
              <div className="absolute inset-0 bg-white/10 backdrop-blur-sm">
                <div className="flex flex-col justify-center items-center h-full text-white p-12">
                  <h3 className="text-2xl font-bold mb-4">Chào mừng bạn đến với chúng tôi!</h3>
                  <p className="text-center mb-6">
                    Hãy tạo tài khoản để khám phá những cơ hội việc làm tuyệt vời
                  </p>
                  {/* Add more content or images here */}
                </div>
              </div>
            </div>
          </div>
        </div>
      </motion.div>
    </div>
  );
};

// Input Field Component
const InputField = ({ icon, name, label, type = "text", value, onChange, error }) => (
  <div>
    <label className="block text-sm font-medium text-gray-700">
      {label}
    </label>
    <div className="mt-1 relative rounded-md shadow-sm">
      <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        {icon}
      </div>
      <input
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        className={`block w-full pl-10 pr-3 py-2 border ${
          error ? 'border-red-500' : 'border-gray-300'
        } rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500`}
      />
    </div>
    {error && <p className="mt-1 text-sm text-red-600">{error}</p>}
  </div>
);

// Password Field Component
const PasswordField = ({ name = "password", label = "Mật khẩu", value, onChange, showPassword, setShowPassword, error }) => (
  <div>
    <label className="block text-sm font-medium text-gray-700">
      {label}
    </label>
    <div className="mt-1 relative rounded-md shadow-sm">
      <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
        <FiLock className="text-gray-400" />
      </div>
      <input
        type={showPassword ? "text" : "password"}
        name={name}
        value={value}
        onChange={onChange}
        className={`block w-full pl-10 pr-10 py-2 border ${
          error ? 'border-red-500' : 'border-gray-300'
        } rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500`}
      />
      <button
        type="button"
        className="absolute inset-y-0 right-0 pr-3 flex items-center"
        onClick={() => setShowPassword(!showPassword)}
      >
        {showPassword ? (
          <FiEyeOff className="text-gray-400 hover:text-gray-600" />
        ) : (
          <FiEye className="text-gray-400 hover:text-gray-600" />
        )}
      </button>
    </div>
    {error && <p className="mt-1 text-sm text-red-600">{error}</p>}
  </div>
);

export default Register;