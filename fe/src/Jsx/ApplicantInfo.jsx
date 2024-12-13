import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ConfirmModal from './ConfirmModal';
import TokenManager from '../utils/tokenManager';
import Loading from './Loading'
import '../css/ApplicantInfo.css';
import LoadInfo from './LoadingInfo';

const ApplicantInfo = ({ userId }) => {
  const [applicantInfo, setApplicantInfo] = useState(null);
  const [isEditing, setIsEditing] = useState(false);  // Trạng thái chỉnh sửa
  const [editableInfo, setEditableInfo] = useState({});  // Dữ liệu có thể chỉnh sửa
  const [skillsOptions, setSkillsOptions] = useState([]); // List of skill options
  const [jobTypesOptions, setJobTypesOptions] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(false);  // State loading
  const token = TokenManager.getToken();


  const [locations, setLocations] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [isAddressProcessed, setIsAddressProcessed] = useState(false);
  const [initialProvince, setInitialProvince] = useState('');
  const [initialCity, setInitialCity] = useState('');
  const [initialDistrict, setInitialDistrict] = useState('');


  useEffect(() => {
    if (userId) {
      axios
        .get(`http://47.128.243.193:8080/applicants/${userId}`, {
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
            emails: data.emails || [],//[{ email: '' }], // Đảm bảo có emails
            phoneNumbers: data.phoneNumbers || [{ phoneNumber: '' }], // Đảm bảo có phoneNumbers
            certifications: data.certifications || [] // Đảm bảo có certifications
          });
        })
        .catch((error) => console.error('Error fetching applicant info:', error));
    }
    //fetich jobtype options
    axios
      .get('http://47.128.243.193:8080/public/jobtypes')
      .then((response) => {
        setJobTypesOptions(response.data);
      })
      .catch((error) => console.error('Error fetching job types options:', error));
    // Fetch skill options
    axios
      .get('http://47.128.243.193:8080/public/skills')
      .then((response) => {
        setSkillsOptions(response.data); // Populate skill options
      })
      .catch((error) => console.error('Error fetching skills options:', error));
  }, [userId]);


  useEffect(() => {
    const fetchLocations = async () => {
      try {
        const response = await fetch('http://47.128.243.193:8080/public/locations');
        const data = await response.json();
        setLocations(data);
      } catch (error) {
        console.error("Error fetching locations:", error);
      }
    };

    fetchLocations();
  }, []);
  useEffect(() => {
    if (applicantInfo?.fullAddress && !isAddressProcessed) { // Kiểm tra nếu companyInfo có fullAddress và chưa xử lý
      const addressParts = applicantInfo.fullAddress.split(',').map(part => part.trim());
      if (addressParts.length === 4) {
        setSelectedDistrict(addressParts[1]);
        setSelectedCity(addressParts[2]);
        setSelectedProvince(addressParts[3]);
        setInitialProvince(addressParts[3]); // Lưu lại tỉnh ban đầu
        setInitialCity(addressParts[2]);     // Lưu lại thành phố ban đầu
        setInitialDistrict(addressParts[1]); // Lưu lại huyện ban đầu
      }
      setIsAddressProcessed(true); // Đánh dấu đã xử lý địa chỉ
    }
  }, [applicantInfo?.fullAddress, isAddressProcessed]); // Chỉ chạy khi fullAddress thay đổi và chưa xử lý


  if (!applicantInfo) {
    return <LoadInfo text="Đang tải thông tin" />;
  }
  const handleJobTypeChange = (index, field, value) => {
    const updatedJobTypes = [...editableInfo.jobTypes];
    updatedJobTypes[index] = {
      ...updatedJobTypes[index],
      [field]: value,
    };
    setEditableInfo((prevState) => ({
      ...prevState,
      jobTypes: updatedJobTypes,
    }));
  };
  // Hàm thêm một job type mới
  const handleAddJobType = () => {
    setEditableInfo((prevState) => ({
      ...prevState,
      jobTypes: [...prevState.jobTypes, { name: '', level: '' }],
    }));
  };

  // Hàm xóa một job type
  const handleRemoveJobType = (index) => {
    setEditableInfo((prevState) => ({
      ...prevState,
      jobTypes: prevState.jobTypes.filter((_, i) => i !== index),
    }));
  };


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
      emails: [...prevState.emails, ''], // Thêm chuỗi rỗng vào mảng emails
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
      phoneNumbers: [...prevState.phoneNumbers, ''], // Thêm chuỗi rỗng vào mảng phoneNumbers
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
    specificAddress,
    description,
    skills,
    jobTypes,
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

    const selectedWard = locations
      .find((province) => province.name === selectedProvince)
      ?.cities.find((city) => city.name === selectedCity)
      ?.wards.find((ward) => ward.name === selectedDistrict);

    const updatedData = {
      id: userId,
      username: editableInfo.username,
      password: editableInfo.password,
      firstName: editableInfo.firstName,
      lastName: editableInfo.lastName,
      description: editableInfo.description,
      image: editableInfo.image,  // Giữ nguyên hình ảnh
      phoneNumbers: editableInfo.phoneNumbers.map((phone) => phone), // Trả về mảng chuỗi
      emails: editableInfo.emails.map((email) => email), // Trả về mảng chuỗi
      specificAddress: editableInfo.specificAddress,
      ward: selectedWard
        ? { id: selectedWard.id, name: selectedWard.name }
        : null, // Đảm bảo ward là object
      dob: editableInfo.dob,
      certifications: editableInfo.certifications.map((cert) => ({
        name: cert.name,
        level: cert.level,
        description: cert.description,
        proof: cert.proof,
        startDate: cert.startDate,
        endDate: cert.endDate,
        applicantId: userId,
      })),
      jobTypes: editableInfo.jobTypes.map((jobType) => ({
        id: jobType.id,
        name: jobType.name,
        level: jobType.level,
      })),
      skills: editableInfo.skills.map((skill) => ({
        id: skill.id,
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
        setApplicantInfo(response.data); // Cập nhật thông tin sau khi thay đổi
        setEditableInfo(response.data); // Cập nhật editableInfo để UI phản ánh ngay
        setIsEditing(false); // Tắt chế độ chỉnh sửa

        setInitialProvince(applicantInfo.province); // Khôi phục tỉnh ban đầu
        setInitialCity(applicantInfo.city); // Khôi phục thành phố ban đầu
        setInitialDistrict(applicantInfo.ward); // Khôi phục huyện ban đầu
        alert('Cập nhật thành công!');
        setIsLoading(false); // Tắt loading khi cập nhật thành công
        window.location.reload();
      })
      .catch((error) => {
        console.error('Lỗi khi cập nhật thông tin:', error);
        setIsLoading(false); // Tắt loading khi cập nhật thất bại
        alert('Cập nhật thất bại. Vui lòng thử lại.');
      }
      );
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
    setSelectedProvince(initialProvince); // Khôi phục tỉnh ban đầu
    setSelectedCity(initialCity);         // Khôi phục thành phố ban đầu
    setSelectedDistrict(initialDistrict); // Khôi phục huyện ban đầu
    setIsEditing(false);  // Tắt chế độ chỉnh sửa
  };

  const handleProvinceChange = (e) => {
    const newProvince = e.target.value;
    console.log("Selected Province:", newProvince); // Debug
    setSelectedProvince(newProvince); // Cập nhật tỉnh
    setSelectedCity(''); // Reset thành phố
    setSelectedDistrict(''); // Reset huyện
  };


  const handleCityChange = (e) => {
    const newCity = e.target.value;
    console.log("Selected City:", newCity); // Debug
    setSelectedCity(newCity); // Cập nhật thành phố
    setSelectedDistrict(''); // Reset huyện
  };


  const handleDistrictChange = (e) => {
    const newDistrict = e.target.value;
    console.log("Selected District:", newDistrict); // Debug
    setSelectedDistrict(newDistrict); // Cập nhật huyện
  };

  return (
    <div className="applicant-info">
      {isLoading ? (
        <Loading /> // Hiển thị Loading khi isLoading = true
      ) : (
        <>
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
                      value={email || ''}
                      disabled={!isEditing}
                      onChange={(e) => {
                        const updatedEmails = [...editableInfo.emails];
                        updatedEmails[index] = e.target.value;
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
                      value={phone || ''}
                      disabled={!isEditing}
                      onChange={(e) => {
                        const updatedPhones = [...editableInfo.phoneNumbers];
                        updatedPhones[index] = e.target.value;
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

            <div className='info-row'>
              <p><strong>Tỉnh:</strong></p>
              <div className="skills-container">
                <select
                  className="input-field-skill"
                  value={selectedProvince || ''}
                  onChange={handleProvinceChange}
                  disabled={!isEditing}
                >
                  <option value=''>Chọn Tỉnh</option>
                  {locations.map((province) => (
                    <option key={province.id} value={province.name}>
                      {province.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className='info-row'>
              <p><strong>Quận:</strong></p>
              <div className="skills-container">
                <select
                  className="input-field-skill"
                  value={selectedCity || ''}
                  onChange={handleCityChange}
                  disabled={!isEditing || !selectedProvince}
                >
                  <option value=''>Chọn Quận</option>
                  {locations
                    .find((province) => province.name === selectedProvince)?.cities.map((city) => (
                      <option key={city.id} value={city.name}>
                        {city.name}
                      </option>
                    ))}
                </select>
              </div>
            </div>

            <div className='info-row'>
              <p><strong>Phường:</strong></p>
              <div className="skills-container">
                <select
                  className="input-field-skill"
                  value={selectedDistrict || ''}
                  onChange={handleDistrictChange}
                  disabled={!isEditing}
                >
                  <option value=''>Chọn Phường</option>
                  {locations
                    .find((province) => province.name === selectedProvince)?.cities
                    .find((city) => city.name === selectedCity)?.wards.map((ward) => (
                      <option key={ward.id} value={ward.name}>
                        {ward.name}
                      </option>
                    ))}
                </select>
              </div>
            </div>



            <div className="info-row">
              <p><strong>Địa chỉ:</strong></p>
              <input
                type="text"
                name="address"
                value={specificAddress || ''}
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

            {editableInfo.jobTypes && (
              <div className="info-row">
                <p><strong>Cấp độ công việc:</strong></p>
                <div className="jobtype-container">
                  {editableInfo.jobTypes.map((jobType, index) => (
                    <div key={index} className="jobtype-row">
                      <select
                        value={jobType.name || ''}
                        disabled={!isEditing}
                        onChange={(e) => handleJobTypeChange(index, 'name', e.target.value)}
                      >
                        <option value="">Chọn loại công việc</option>
                        {jobTypesOptions.map((option) => (
                          <option key={option.id} value={option.name}>{option.name}</option>
                        ))}
                      </select>

                      <select
                        value={jobType.level || ''}
                        disabled={!isEditing}
                        onChange={(e) => handleJobTypeChange(index, 'level', e.target.value)}
                      >
                        <option value="">Chọn cấp bậc</option>
                        <option value="INTERN">INTERN</option>
                        <option value="FRESHER">FRESHER</option>
                        <option value="JUNIOR">JUNIOR</option>
                        <option value="SENIOR">SENIOR</option>
                        <option value="LEAD">LEAD</option>
                        <option value="MANAGER">MANAGER</option>
                        <option value="DIRECTOR">DIRECTOR</option>
                      </select>

                      {isEditing && (
                        <>
                          <button
                            className="remove-jobtype-btn"
                            onClick={() => handleRemoveJobType(index)}
                          >-
                          </button>
                        </>
                      )}
                    </div>
                  ))}
                  {isEditing && (
                    <button
                      className="add-jobtype-btn"
                      onClick={() => handleAddJobType()}
                    >+
                    </button>
                  )}
                </div>
              </div>
            )}
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
                      value={cert.startDate ? formatDate(cert.startDate) : ''}
                      disabled={!isEditing}
                      onChange={(e) => handleCertificationChange(e, index, 'startDate')}
                    />
                  </p>
                  <p>
                    <strong>Ngày kết thúc:</strong>
                    <input
                      type="date"
                      value={cert.endDate ? formatDate(cert.endDate) : ''}
                      disabled={!isEditing}
                      onChange={(e) => handleCertificationChange(e, index, 'endDate')}
                    />
                  </p>
                  {isEditing && (
                    <button
                      onClick={() => removeCertification(index)}
                      className="remove-skill-btn"
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
              <button onClick={addCertification} className="add-skill-btn">+</button>
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
        </>
      )}
    </div>
  );
};

export default ApplicantInfo;
