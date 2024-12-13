import React, { useState, useEffect } from 'react';
import '../css/CompanyInfo.css';
import TokenManager from '../utils/tokenManager';
import LoadInfo from './LoadingInfo';
import ConfirmModal from './ConfirmModal';

const CompanyInfo = () => {
  const [role, setRole] = useState(null);
  const [userId, setUserId] = useState(null);
  const [userInfo, setUserInfo] = useState(null);
  const token = TokenManager.getToken();
  const [companyInfo, setCompanyInfo] = useState(null);
  const [initialCompanyInfo, setInitialCompanyInfo] = useState(null); // Dữ liệu ban đầu
  const [isEditing, setIsEditing] = useState(false);
  const [locations, setLocations] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [fields, setFields] = useState([]); // State for fields
  const [isAddressProcessed, setIsAddressProcessed] = useState(false);
  const [initialProvince, setInitialProvince] = useState('');
  const [initialCity, setInitialCity] = useState('');
  const [initialDistrict, setInitialDistrict] = useState('');
  const [isSaving, setIsSaving] = useState(false); // Trạng thái đang lưu
  const [isModalVisible, setIsModalVisible] = useState(false);

  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase());
      setUserId(token.id);
    }
  }, [token]);

  useEffect(() => {
    const fetchCompanyInfo = async () => {
      try {
        const response = await fetch(`http://47.128.243.193:8080/companies/${userId}`, {
          headers: {
            'Authorization': `Bearer ${token.value}`,
            'Content-Type': 'application/json'
          },
          credentials: 'include',
        });

        const data = await response.json();
        // Chuyển đổi fields từ array object sang array string
        if (data) {

          if (Array.isArray(data.fields)) {
            data.fields = data.fields.map((field) => field.name); // Lấy name của từng field
          }
          setCompanyInfo(data);
          setInitialCompanyInfo(data); // Save initial data for restoration on cancel
        }
        else {
          console.error('API returned null data');
        }
      } catch (error) {
        console.error("Error fetching company info:", error);
      }
    };

    fetchCompanyInfo();
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
    const fetchFields = async () => {
      try {
        const response = await fetch('http://47.128.243.193:8080/public/fields');
        const data = await response.json();
        setFields(data); // Set the fetched fields
      } catch (error) {
        console.error("Error fetching fields:", error);
      }
    };

    fetchFields();
  }, []); // Fetch fields when component mounts

  useEffect(() => {
    if (companyInfo?.fullAddress && !isAddressProcessed) { // Kiểm tra nếu companyInfo có fullAddress và chưa xử lý
      const addressParts = companyInfo.fullAddress.split(',').map(part => part.trim());
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
  }, [companyInfo?.fullAddress, isAddressProcessed]); // Chỉ chạy khi fullAddress thay đổi và chưa xử lý




  useEffect(() => {
    if (companyInfo && Array.isArray(companyInfo.fields)) {
      setCompanyInfo({ ...companyInfo, fields: companyInfo.fields.map(field => field.toString()) });
    }
  }, [companyInfo]);

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleSaveClick = async () => {
    // Lấy wardId từ địa chỉ đã chọn
    console.log("Saving company info...");
    setIsSaving(true); // Đặt trạng thái "đang lưu"
    const selectedWard = locations
      .find((province) => province.name === selectedProvince)
      ?.cities.find((city) => city.name === selectedCity)
      ?.wards.find((ward) => ward.name === selectedDistrict);
    const wardId = selectedWard ? selectedWard.id : null;

    // Lấy fieldIds từ danh sách ngành nghề đã chọn
    // Chuyển fields từ danh sách tên ngành nghề sang danh sách ID
    // const fieldIds = companyInfo.fields.map((fieldName) => {
    //   const field = fields.find((field) => field.name === fieldName);
    //   return field ? field.id : null;
    // }).filter((id) => id !== null); // Loại bỏ các giá trị null
    // Chuyển fields từ danh sách tên ngành nghề sang danh sách ID (fields sẽ là một array chứa objects với key 'id')
    const updatedFields = companyInfo.fields.map((fieldName) => {
      const field = fields.find((field) => field.name === fieldName);
      return field ? { id: field.id, name: "sssss" } : null; // Tạo object { id: field.id }
    }).filter((field) => field !== null); // Loại bỏ null

    const updatedCompanyInfo = {
      id: userId, // Sử dụng userId
      username: companyInfo.username,
      password: companyInfo.password, // Lấy password từ token nếu có
      isActive: companyInfo.isActive, // Luôn luôn là true
      isPublic: companyInfo.isPublic,
      isBanned: companyInfo.isBanned,
      name: companyInfo.name,
      taxCode: companyInfo.taxCode,
      image: 'bg.jpg', // Đặt tên hình ảnh cố định, hoặc bạn có thể thay bằng ảnh tải lên
      ward: { id: wardId, name: "ssss" }, // wardId lấy từ lựa chọn
      specificAddress: companyInfo.specificAddress,

      //rating: companyInfo.rating || "4.5", // Đặt rating mặc định nếu không có
      fields: updatedFields,
      remainingPost: companyInfo.remainingPost, // Ví dụ cho remainingPost
      description: companyInfo.description,
      emails: companyInfo.emails,
      phoneNumbers: companyInfo.phoneNumbers,
    };
    console.log(updatedCompanyInfo);

    try {
      const response = await fetch('http://47.128.243.193:8080/companies', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token.value}`, // Nếu token được yêu cầu
        },
        body: JSON.stringify(updatedCompanyInfo),
      });

      if (response.ok) {
        console.log('Company info updated successfully!');
        alert('Cập nhật thông tin thành công!')
        setIsEditing(false);
        setInitialCompanyInfo(updatedCompanyInfo); // Cập nhật dữ liệu ban đầu
        setInitialProvince(companyInfo.province); // Khôi phục tỉnh ban đầu
        setInitialCity(companyInfo.city);         // Khôi phục thành phố ban đầu
        setInitialDistrict(companyInfo.ward); // Khôi phục huyện ban đầu
      } else {
        console.error('Error updating company info:', response.statusText);
        alert('Cập nhật thông tin thất bại!')
      }
    } catch (error) {
      console.error('Error during API request:', error);
    } finally {
      setIsSaving(false); // Hoàn thành cập nhật
    }
  };

  const handleConfirmEdit = () => {
    handleSaveClick();  // Gọi hàm handlesave khi xác nhận
    setIsModalVisible(false);  // Đóng pop-up
  };

  const handleCancelEdit = () => {
    setIsModalVisible(false);  // Đóng pop-up mà không cập nhật
  };

  const handleExitClick = () => {
    setCompanyInfo(initialCompanyInfo); // Khôi phục dữ liệu ban đầu
    setSelectedProvince(initialProvince); // Khôi phục tỉnh ban đầu
    setSelectedCity(initialCity);         // Khôi phục thành phố ban đầu
    setSelectedDistrict(initialDistrict); // Khôi phục huyện ban đầu
    setIsEditing(false);
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


  // Xử lý thay đổi ngành nghề
  const handleFieldChange = (index, value) => {
    const updatedFields = [...companyInfo.fields];
    updatedFields[index] = value; // Update field at the correct index
    setCompanyInfo({ ...companyInfo, fields: updatedFields });
  };

  // Thêm ngành mới
  const handleAddField = () => {
    const updatedFields = [...companyInfo.fields, '']; // Thêm ngành trống
    setCompanyInfo({ ...companyInfo, fields: updatedFields });
  };

  // Xóa ngành
  const handleDeleteField = (index) => {
    const updatedFields = companyInfo.fields.filter((_, idx) => idx !== index); // Xóa ngành tại index
    setCompanyInfo({ ...companyInfo, fields: updatedFields });
  };

  // Thêm email mới
  const handleAddEmail = () => {
    const updatedEmails = [...companyInfo.emails, ''];
    setCompanyInfo({ ...companyInfo, emails: updatedEmails });
  };

  // Xóa email
  const handleDeleteEmail = (index) => {
    const updatedEmails = companyInfo.emails.filter((_, idx) => idx !== index);
    setCompanyInfo({ ...companyInfo, emails: updatedEmails });
  };

  // Thêm số điện thoại mới
  const handleAddPhone = () => {
    const updatedPhones = [...companyInfo.phoneNumbers, ''];
    setCompanyInfo({ ...companyInfo, phoneNumbers: updatedPhones });
  };

  // Xóa số điện thoại
  const handleDeletePhone = (index) => {
    const updatedPhones = companyInfo.phoneNumbers.filter((_, idx) => idx !== index);
    setCompanyInfo({ ...companyInfo, phoneNumbers: updatedPhones });
  };

  if (!companyInfo) {
    return <LoadInfo text="Đang tải thông tin công ty" />;
  }

  const { name, taxCode, description, emails, phoneNumbers } = companyInfo;


  return (
    <div className='applicant-info'>
      <h1>Welcome {name}</h1>
      {/* <h3>Thông tin cơ bản</h3> */}
      <div className='info-section'>
        <div id='content-company-info'>

          <div className="company-info">
            <img src={`https://via.placeholder.com/150`} alt="Company Logo" className="company-logo" />
            <button className="upload-btn">Tải lên</button>
            <p className="file-info">
              Max file size is 1MB, Minimum dimension: 300x300 And Suitable files are .jpg & .png
            </p>
          </div>
        </div>

        {/* <div className="company-details"> */}
        <div className="info-row">
          <p><strong>Tên Công Ty</strong></p>
          <input
            type="text"
            value={name}
            readOnly={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, name: e.target.value })}
          />
        </div>

        <div className='info-row'>
          <p><strong>Mã số thuế</strong></p>
          <input
            type="text"
            value={taxCode}
            readOnly={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, taxCode: e.target.value })}
          />
        </div>

        <div className='info-row'>
          <p><strong>Mô tả</strong></p>
          <textarea
            rows="5"
            cols="50"
            value={description}
            readOnly={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, description: e.target.value })}
          />
        </div>

        <div className='info-row'>
          <p><strong>Số điện thoại</strong></p>
          <div className="phone-container">
            {phoneNumbers.map((phone, index) => (
              <div key={index} className="info-row">
                <input
                  type="text"
                  value={phone}
                  readOnly={!isEditing}
                  onChange={(e) => {
                    const newPhoneNumbers = [...phoneNumbers];
                    newPhoneNumbers[index] = e.target.value;
                    setCompanyInfo({ ...companyInfo, phoneNumbers: newPhoneNumbers });
                  }}
                />
                {isEditing && (
                  <button className="remove-skill-btn" type="button" onClick={() => handleDeletePhone(index)}>-</button>
                )}
              </div>
            ))}
            {isEditing && (
              <button className="add-skill-btn" type="button" onClick={handleAddPhone}>+ </button>
            )}
          </div>
        </div>

        <div className='info-row'>
          <p><strong>Email</strong></p>
          <div className='email-container'>
            {emails.map((email, index) => (
              <div key={index} className="info-row">
                <input
                  type="email"
                  value={email}
                  readOnly={!isEditing}
                  onChange={(e) => {
                    const newEmails = [...emails];
                    newEmails[index] = e.target.value;
                    setCompanyInfo({ ...companyInfo, emails: newEmails });
                  }}
                />
                {isEditing && (
                  <button className="remove-skill-btn" type="button" onClick={() => handleDeleteEmail(index)}>-</button>
                )}
              </div>
            ))}
            {isEditing && (
              <button className="add-skill-btn" type="button" onClick={handleAddEmail}>+</button>
            )}
          </div>
        </div>

        <div className='info-row'>
          <p><strong>Ngành</strong></p>
          <div className="phone-container">
            {Array.isArray(companyInfo?.fields) && companyInfo.fields.map((field, index) => (
              <div key={index} className="info-row">
                <select
                  value={field || ''}
                  onChange={(e) => handleFieldChange(index, e.target.value)}
                  disabled={!isEditing}
                  className='input-field-skill'
                >
                  <option value=''>Chọn ngành</option>
                  {fields.map((fieldOption, idx) => (
                    <option key={idx} value={fieldOption.name}>
                      {fieldOption.name} {/* Render tên ngành */}
                    </option>
                  ))}
                </select>
                {isEditing && (
                  <button
                    type="button"
                    onClick={() => handleDeleteField(index)}
                    className="remove-skill-btn"
                  >
                    -
                  </button>
                )}
              </div>
            ))}

            {isEditing && (
              <button
                className="add-skill-btn"
                type="button"
                onClick={() =>
                  setCompanyInfo({
                    ...companyInfo,
                    fields: [...(companyInfo.fields || []), ''], // Thêm ngành mới
                  })
                }
              >
                +
              </button>
            )}
          </div>
        </div>



        <div className='info-row'>
          <p><strong>Tỉnh</strong></p>
          <div className='skills-container'>
            <select
              className="input-field-skill"
              value={selectedProvince || ''}
              onChange={handleProvinceChange}
              disabled={!isEditing}
            >
              <option value=''>Chọn Tỉnh</option>
              {Array.isArray(locations) && locations.map((province) => (
                <option key={province.id} value={province.name}>
                  {province.name}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className='info-row'>
          <p><strong>Quận</strong></p>
          <div className='skills-container'>
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
          <p><strong>Phường</strong></p>
          <div className='skills-container'>
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
          <p><strong>Địa chỉ cụ thể</strong></p>
          <input
            type="text"
            value={companyInfo.specificAddress || ''}
            disabled={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, specificAddress: e.target.value })}
          />
        </div>


        {isEditing ? (
          <div className="button-container">
            <button className="update-btn" onClick={() => setIsModalVisible(true)}>Lưu</button>
            <button className="cancel-btn" onClick={handleExitClick}>Thoát</button>
          </div>
        ) : (
          <button className="update-btn" onClick={() => setIsEditing(true)}>Cập nhật</button>
        )}
        {/* Hộp xác nhận khi nhấn "Cập nhật" */}
        {isModalVisible && (
          <ConfirmModal
            isLoading={isSaving}
            isVisible={isModalVisible}  // Thêm isVisible vào đây
            message="Bạn chắc chắn muốn cập nhật thông tin?"
            onConfirm={handleConfirmEdit}
            onCancel={handleCancelEdit}
          />
        )}

        {/* <div className="button-group">
          <button className="update-btn" onClick={isEditing ? handleSaveClick : handleEditClick} 
            disabled={isSaving} // Vô hiệu hóa nút khi đang lưu
          >
             {isSaving ? "Đang lưu..." : isEditing ? "Lưu" : "Chỉnh sửa"}
          </button>

          {isEditing && (
            <button className="exit-btn" onClick={handleExitClick}>
              Thoát
            </button>
          )}
        </div> */}
      </div>
    </div>
  );
};

export default CompanyInfo;
