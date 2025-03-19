import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ConfirmModal from './ConfirmModal';
import TokenManager from '../utils/tokenManager';
import Loading from './Loading'
import '../css/ApplicantInfo.css';

const ApplicantInfo = ({ userId }) => {
  const [applicantInfo, setApplicantInfo] = useState(null);
  const [isEditing, setIsEditing] = useState(false);  // Trạng thái chỉnh sửa
  const [editableInfo, setEditableInfo] = useState({});  // Dữ liệu có thể chỉnh sửa
  const [skillsOptions, setSkillsOptions] = useState([]); // List of skill options
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(false);  // State loading
  const token = TokenManager.getToken();


  useEffect(() => {
    if (userId) {
      axios
        .get(`/applicants/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        })
        .then((response) => {
          const data = response.data;
          setApplicantInfo(data);
          setEditableInfo({
            ...data,
            skills: data.skills || [], // Đảm bảo có skills mặc dù có thể không có
            emails: data.emails || [{ email: '' }], // Đảm bảo có emails
            phoneNumbers: data.phoneNumbers || [{ phoneNumber: '' }], // Đảm bảo có phoneNumbers
            certifications: data.certifications || [] // Đảm bảo có certifications
          });
        })
        .catch((error) => console.error('Error fetching applicant info:', error));
    }
 // Fetch skill options
    axios
      .get('/public/skills')
      .then((response) => {
        setSkillsOptions(response.data); // Populate skill options
      })
      .catch((error) => console.error('Error fetching skills options:', error));
  }, [userId]);


  if (!applicantInfo) {
    return <div>Đang tải thông tin...</div>;
  }
  
  const handleSkillChange = (e, index) => {
    const updatedSkills = [...editableInfo.skills];
    const skillId = e.target.value;
    updatedSkills[index] = { ...updatedSkills[index], id: skillId };
    setEditableInfo((prevState) => ({
      ...prevState,
      skills: updatedSkills,
    }));
  };

  const addSkill = () => {
    const newSkill = { id: '' }; // New skill with empty id
    setEditableInfo((prevState) => ({
      ...prevState,
      skills: [...prevState.skills, newSkill],
    }));
  };

  const removeSkill = (index) => {
    const updatedSkills = [...editableInfo.skills];
    updatedSkills.splice(index, 1); // Xóa kỹ năng tại vị trí index
    setEditableInfo((prevState) => ({
      ...prevState,
      skills: updatedSkills,
    }));
  };

  const addEmail = () => {
    setEditableInfo((prevState) => ({
      ...prevState,
      emails: [...prevState.emails, { email: '' }],
    }));
  };

  const removeEmail = (index) => {
    const updatedEmails = [...editableInfo.emails];
    updatedEmails.splice(index, 1);
    setEditableInfo((prevState) => ({
      ...prevState,
      emails: updatedEmails,
    }));
  };

  const addPhone = () => {
    setEditableInfo((prevState) => ({
      ...prevState,
      phoneNumbers: [...prevState.phoneNumbers, { phoneNumber: '' }],
    }));
  };

  const removePhone = (index) => {
    const updatedPhones = [...editableInfo.phoneNumbers];
    updatedPhones.splice(index, 1);
    setEditableInfo((prevState) => ({
      ...prevState,
      phoneNumbers: updatedPhones,
    }));
  };

  const addCertification = () => {
    setEditableInfo((prevState) => ({
      ...prevState,
      certifications: [
        ...prevState.certifications,
        { 
          name: '', 
          level: '', 
          description: '', 
          proof: '', 
          startDate: '', 
          endDate: '' 
        }
      ],
    }));
  };

  const removeCertification = (index) => {
    const updatedCertifications = [...editableInfo.certifications];
    updatedCertifications.splice(index, 1);
    setEditableInfo((prevState) => ({
      ...prevState,
      certifications: updatedCertifications,
    }));
  };

  const handleCertificationChange = (e, index, field) => {
    const updatedCertifications = [...editableInfo.certifications];
    if (updatedCertifications[index]) { // Kiểm tra sự tồn tại của đối tượng tại vị trí index
      updatedCertifications[index][field] = e.target.value;
      setEditableInfo((prevState) => ({
        ...prevState,
        certifications: updatedCertifications,
      }));
    }
  };
  

  

  const {
    firstName,
    lastName,
    dob,
    emails,
    phoneNumbers,
    address,
    description,
    skills,
    applicantJobtypeEntities,
    certifications,
  } = editableInfo;

  const formatDate = (dateString) => {
    return new Date(dateString).toISOString().split('T')[0];  // Chuyển đổi về định dạng YYYY-MM-DD
  };

  // Hàm để thay đổi trạng thái của các trường thông tin khi nhấn Cập nhật
  const handleInputChange = (e) => {
  const { name, value } = e.target;
  setEditableInfo((prevState) => ({
    ...prevState,
    [name]: value || '', // Nếu value là undefined hoặc null, sẽ thay bằng chuỗi rỗng
  }));
};


  // Hàm gửi dữ liệu thay đổi lên server
  const handleUpdate = () => {
    setIsLoading(true); // Bật loading khi bắt đầu cập nhật
    const updatedData = {
      id: userId,
      username: editableInfo.username,
      password: editableInfo.password,
      firstName: editableInfo.firstName,
      lastName: editableInfo.lastName,
      description: editableInfo.description,
      isPublic: true,  // Trả về true
      address: editableInfo.address,
      image: editableInfo.image,  // Giữ nguyên hình ảnh
      phoneNumbers: editableInfo.phoneNumbers.map(phone => phone.phoneNumber),  // Đảm bảo chỉ lấy số điện thoại
      dob: editableInfo.dob,
      emails: editableInfo.emails.map(email => email.email),  // Đảm bảo chỉ lấy email
      notificationIds: applicantInfo.notificationIds,  // Giữ nguyên notificationIds
      blockedUserIds: applicantInfo.blockedUserIds,  // Giữ nguyên blockedUserIds
      skillIds: editableInfo.skills.map(skill => skill.id),  // Lấy ID kỹ năng từ dữ liệu đã chọn
      certifications: editableInfo.certifications.map(cert => ({
        name: cert.name,
        level: cert.level,
        description: cert.description,
        proof: cert.proof,
        startDate: cert.startDate,
        endDate: cert.endDate
      })),
    };
  
    // Gửi POST request đến API
    axios
      .post(`/applicants`, updatedData, {
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      })
      .then((response) => {
        setApplicantInfo(response.data);  // Cập nhật thông tin sau khi thay đổi
        setEditableInfo(response.data);  // Cập nhật editableInfo để UI phản ánh ngay
        setIsEditing(false);  // Tắt chế độ chỉnh sửa
        setIsLoading(false);  // Tắt loading khi cập nhật thành công
        alert('Cập nhật thành công!');
      })
      .catch((error) => {
        console.error('Lỗi khi cập nhật thông tin:', error);
        setIsLoading(false);  // Tắt loading khi cập nhật thành công
        alert('Cập nhật thất bại. Vui lòng thử lại.');
      });
  };

  const handleConfirmEdit = () => {
    handleUpdate();  // Gọi hàm handleUpdate khi xác nhận
    setIsModalVisible(false);  // Đóng pop-up
  };

  const handleCancelEdit = () => {
    setIsModalVisible(false);  // Đóng pop-up mà không cập nhật
  };

  // Hàm hủy thay đổi và thoát chế độ chỉnh sửa
  const handleCancel = () => {
    setEditableInfo(applicantInfo);  // Khôi phục lại dữ liệu ban đầu
    setIsEditing(false);  // Tắt chế độ chỉnh sửa
  };

  return (
    <div className="applicant-info">
      {isLoading && <Loading />}  {/* Hiển thị loading khi isLoading = true */}
      <h1>Thông tin cá nhân</h1>
      <div className="info-section">
        <div className="info-row">
          <p><strong>Họ:</strong></p>
          <input
            type="text"
            name="lastName"
            value={lastName || ''}
            disabled={!isEditing}
            onChange={handleInputChange}
          />
        </div>
        <div className="info-row">
          <p><strong>Tên:</strong></p>
          <input
            type="text"
            name="firstName"
            value={firstName || ''}
            disabled={!isEditing}
            onChange={handleInputChange}
          />
        </div>
        <div className="info-row">
          <p><strong>Ngày sinh:</strong></p>
          <input
            type="date"
            name="dob"
            value={dob ? formatDate(dob) : ''}  // Dùng formatDate để chuyển đổi định dạng ngày
            disabled={!isEditing}
            onChange={handleInputChange}
          />
        </div>
        
        {/* Email */}
        <div className="info-row">
          <p><strong>Email:</strong></p>
          <div className="email-container">
            {emails?.map((email, index) => (
              <div key={index} className="info-row">
                <input
                  type="text"
                  value={email.email || ''}
                  disabled={!isEditing}
                  onChange={(e) => {
                    const updatedEmails = [...editableInfo.emails];
                    updatedEmails[index].email = e.target.value;
                    setEditableInfo((prevState) => ({
                      ...prevState,
                      emails: updatedEmails,
                    }));
                  }}
                  className="input-field"
                />
                {isEditing && (
                  <button
                    onClick={() => removeEmail(index)}
                    className="remove-skill-btn"
                  >
                    -
                  </button>
                )}
              </div>
            ))}
            {isEditing && (
              <button onClick={addEmail} className="add-skill-btn">+</button>
            )}
          </div>
        </div>

        {/* Số điện thoại */}
        <div className="info-row">
          <p><strong>Số điện thoại:</strong></p>
          <div className="phone-container">
            {phoneNumbers?.map((phone, index) => (
              <div key={index} className="info-row">
                <input
                  type="text"
                  value={phone.phoneNumber || ''}
                  disabled={!isEditing}
                  onChange={(e) => {
                    const updatedPhones = [...editableInfo.phoneNumbers];
                    updatedPhones[index].phoneNumber = e.target.value;
                    setEditableInfo((prevState) => ({
                      ...prevState,
                      phoneNumbers: updatedPhones,
                    }));
                  }}
                  className="input-field"
                />
                {isEditing && (
                  <button
                    onClick={() => removePhone(index)}
                    className="remove-skill-btn"
                  >
                    -
                  </button>
                )}
              </div>
            ))}
            {isEditing && (
              <button onClick={addPhone} className="add-skill-btn">+</button>
            )}
          </div>
        </div>

        <div className="info-row">
          <p><strong>Địa chỉ:</strong></p>
          <input
            type="text"
            name="address"
            value={address || ''}
            disabled={!isEditing}
            onChange={handleInputChange}
          />
        </div>
        <div className="info-row">
          <p><strong>Mô tả:</strong></p>
          <textarea
            name="description"
            value={description || ''}
            disabled={!isEditing}
            onChange={handleInputChange}
          />
        </div>

        <div className="info-row">
          <p><strong>Kỹ năng:</strong></p>
          <div className="skills-container">
            {skills?.map((skill, index) => (
              <div key={index} className="info-row">
                <select
                  value={skill.id || ''}
                  disabled={!isEditing}
                  onChange={(e) => handleSkillChange(e, index)}
                  className="input-field-skill"
                >
                  <option value="">Chọn kỹ năng</option>
                  {skillsOptions.map((option) => (
                    <option key={option.id} value={option.id} >
                      {option.name}
                    </option>
                  ))}
                </select>
                {isEditing && (
                  <button
                    onClick={() => removeSkill(index)}
                    className="remove-skill-btn"
                  >
                    -
                  </button>
                )}
              </div>
            ))}
            {isEditing && (
              <button onClick={addSkill} className="add-skill-btn">+</button>
            )}
          </div>
        </div>

        <div className="info-row">
          <p><strong>Cấp độ:</strong></p>
          <input
            type="text"
            name="applicantJobtypeEntities"
            value={
              applicantJobtypeEntities?.length > 0
                ? applicantJobtypeEntities.map((jobType) => jobType.level).join(', ')
                : ''
            }
            disabled={!isEditing}
            onChange={handleInputChange}
          />
        </div>
      </div>

      <div className="certifications-section">
          <h2>Chứng nhận</h2>
          {certifications?.length > 0 ? (
            certifications.map((cert, index) => (
              <div key={index} className="certification-item">
                <p>
                  <strong>Tên chứng nhận:</strong>
                  <input
                    type="text"
                    value={cert.name || ''}
                    disabled={!isEditing}
                    onChange={(e) => handleCertificationChange(e, index, 'name')}
                  />
                </p>
                <p>
                  <strong>Cấp độ:</strong>
                  <input
                    type="text"
                    value={cert.level || ''}
                    disabled={!isEditing}
                    onChange={(e) => handleCertificationChange(e, index, 'level')}
                  />
                </p>
                <p>
                  <strong>Mô tả:</strong>
                  <input
                    type="text"
                    value={cert.description || ''}
                    disabled={!isEditing}
                    onChange={(e) => handleCertificationChange(e, index, 'description')}
                  />
                </p>
                <p>
                  <strong>Ngày bắt đầu:</strong>
                  <input
                    type="date"
                    value={cert.startDate? formatDate(cert.startDate) : '' }
                    disabled={!isEditing}
                    onChange={(e) => handleCertificationChange(e, index, 'startDate')}
                  />
                </p>
                <p>
                  <strong>Ngày kết thúc:</strong>
                  <input
                    type="date"
                    value={cert.endDate? formatDate(cert.endDate) : '' }
                    disabled={!isEditing}
                    onChange={(e) => handleCertificationChange(e, index, 'endDate')}
                  />
                </p>
                {isEditing && (
                  <button
                    onClick={() => removeCertification(index)}
                    className="remove-cert-btn"
                  >
                    -
                  </button>
                )}
              </div>
            ))
          ) : (
            <div>Chưa có chứng nhận nào.</div>
          )}
          {isEditing && (
            <button onClick={addCertification} className="add-cert-btn">+</button>
          )}
        </div>

      {isEditing ? (
        <div className="button-container">
          <button className="update-btn" onClick={() => setIsModalVisible(true)}>Lưu</button>
          <button className="cancel-btn" onClick={handleCancel}>Thoát</button>
        </div>
      ) : (
        <button className="update-btn" onClick={() => setIsEditing(true)}>Cập nhật</button>
      )}
      {/* Hộp xác nhận khi nhấn "Cập nhật" */}
      {isModalVisible && (
        <ConfirmModal
          isVisible={isModalVisible}  // Thêm isVisible vào đây
          message="Bạn chắc chắn muốn cập nhật thông tin?"
          onConfirm={handleConfirmEdit}
          onCancel={handleCancelEdit}
        />
      )}
    </div>
  );
};

export default ApplicantInfo;
