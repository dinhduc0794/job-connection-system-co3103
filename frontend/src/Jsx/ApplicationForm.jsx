import React, { useState, useEffect } from 'react';
import '../css/ApplicationForm.css';
import TokenManager from '../utils/tokenManager';

const ApplicationForm = ({ onClose, jobPostingId }) => {
  const [role, setRole] = useState(null); 
  const [userId, setUserId] = useState(null);
  const [formData, setFormData] = useState({
    email: '',
    phoneNumber: '',
    introduction: '',
    resume: null,
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false); // State quản lý loading
  const token = TokenManager.getToken();

  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase());
      setUserId(token.id);
    }
  }, [token]);

  useEffect(() => {
    if (Object.keys(errors).length > 0) {
      const timer = setTimeout(() => {
        setErrors({});
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [errors]);

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    setFormData({
      ...formData,
      [name]: files ? files[0] : value,
    });
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.email) newErrors.email = "Email là bắt buộc.";
    if (!formData.phoneNumber) newErrors.phoneNumber = "Số điện thoại là bắt buộc.";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      setIsLoading(true); // Bắt đầu loading
      try {
        const requestBody = {
          applicantId: userId,
          jobPostingId: jobPostingId,
          email: formData.email,
          phoneNumber: formData.phoneNumber,
          resume: formData.resume ? formData.resume.name : '',
          description: formData.introduction,
        };

        const response = await fetch('/applications', {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token.value}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        });

        if (!response.ok) {
          throw new Error(`Failed to apply: ${response.status}`);
        }

        const data = await response.json();
        alert('Ứng tuyển thành công!');
        onClose();
      } catch (error) {
        alert('Đã xảy ra lỗi, vui lòng thử lại!');
      } finally {
        setIsLoading(false); // Kết thúc loading
      }
    }
  };

  return (
    <div className="popup-apply-overlay">
      <div className="popup-content">
        <button className="close-btn" onClick={onClose} disabled={isLoading}>X</button>
        <h3>Ứng Tuyển</h3>
        {isLoading && <p className="loading-text">Đang xử lý...</p>} {/* Hiển thị trạng thái loading */}
        <form onSubmit={handleSubmit}>
          <label>
            Tải lên CV từ máy tính, chọn hoặc kéo thả
            <input
              type="file"
              name="resume"
              accept="application/pdf"
              onChange={handleChange}
              disabled={isLoading} // Chặn chỉnh sửa khi đang loading
            />
          </label>

          <label>
            Email<span className="required">*</span>:
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={formData.email}
              onChange={handleChange}
              disabled={isLoading} // Chặn chỉnh sửa khi đang loading
            />
            {errors.email && <span className="error">{errors.email}</span>}
          </label>

          <label>
            Số Điện Thoại<span className="required">*</span>:
            <input
              type="tel"
              name="phoneNumber"
              placeholder="Số điện thoại"
              value={formData.phoneNumber}
              onChange={handleChange}
              disabled={isLoading} // Chặn chỉnh sửa khi đang loading
            />
            {errors.phoneNumber && <span className="error">{errors.phoneNumber}</span>}
          </label>

          <label>
            <p>Thư Giới Thiệu</p>
            <textarea
              name="introduction"
              placeholder="Giới thiệu ngắn gọn về bản thân..."
              value={formData.introduction}
              onChange={handleChange}
              disabled={isLoading} // Chặn chỉnh sửa khi đang loading
            ></textarea>
          </label>

          <div className="button-group">
            <button type="button" onClick={onClose} className="cancel-btn" disabled={isLoading}>
              Cancel
            </button>
            <button type="submit" className="submit-btn" disabled={isLoading}>
              {isLoading ? 'Đang gửi...' : 'Submit Application'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ApplicationForm;
