  import React, { useState, useEffect } from 'react';
  import '../css/CompanyInfo.css';
  import TokenManager from '../utils/tokenManager';

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
    

    useEffect(() => {
      if (token) {
        setRole(token.role?.toLowerCase());
        setUserId(token.id);
      }
    }, [token]);

    useEffect(() => {
      const fetchCompanyInfo = async () => {
        try {
          const response = await fetch(`/companies/${userId}` , {
            headers: {
              'Authorization': `Bearer ${token.value}`,
              'Content-Type': 'application/json'
            },
            credentials: 'include',
          });
          const data = await response.json();
          // Check if fields is a string, and convert to array if needed
          if (typeof data.fields === 'string') {
            data.fields = data.fields.split(',').map((field) => field.trim()); // Convert to array
          }
          setCompanyInfo(data);
          setInitialCompanyInfo(data); // Save initial data for restoration on cancel
        } catch (error) {
          console.error("Error fetching company info:", error);
        }
      };
    
      fetchCompanyInfo();
    }, [userId]);

    useEffect(() => {
      const fetchLocations = async () => {
        try {
          const response = await fetch('/public/locations');
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
          const response = await fetch('/public/fields');
          const data = await response.json();
          setFields(data); // Set the fetched fields
        } catch (error) {
          console.error("Error fetching fields:", error);
        }
      };

      fetchFields();
    }, []); // Fetch fields when component mounts

    
    useEffect(() => {
      if (companyInfo && companyInfo.addresses?.length > 0) {
        const addressParts = companyInfo.addresses[0]?.split(',').map(part => part.trim());
        if (addressParts.length === 3) {
          setSelectedDistrict(addressParts[0]); // Phường
          setSelectedCity(addressParts[1]);    // Quận
          setSelectedProvince(addressParts[2]); // Thành phố/Tỉnh
        }
      }
    }, [companyInfo]);

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
      const selectedWard = locations
        .find((province) => province.name === selectedProvince)
        ?.cities.find((city) => city.name === selectedCity)
        ?.wards.find((ward) => ward.name === selectedDistrict);
      const wardId = selectedWard ? selectedWard.id : null;
    
      // Lấy fieldIds từ danh sách ngành nghề đã chọn
      const fieldIds = companyInfo.fields.map((fieldName) => {
        const field = fields.find((field) => field.name === fieldName);
        return field ? field.id : null; // Lấy id của ngành nghề đã chọn
      }).filter(id => id !== null); // Lọc những id hợp lệ
    
      const updatedCompanyInfo = {
        id: userId, // Sử dụng userId
        username: 'username30', // Lấy username từ token nếu có
        password: 'password30', // Lấy password từ token nếu có
        name: companyInfo.name,
        taxCode: companyInfo.taxCode,
        image: 'new-image.jpg', // Đặt tên hình ảnh cố định, hoặc bạn có thể thay bằng ảnh tải lên
        addressWardIds: [
          {
            address: "1/2, đường Nguyễn Trãi", // Địa chỉ cố định
            wardId: wardId, // wardId lấy từ lựa chọn
          }
        ],
        rating: companyInfo.rating || "4.5", // Đặt rating mặc định nếu không có
        fieldIds: fieldIds,
        remainingPost: 2, // Ví dụ cho remainingPost
        description: companyInfo.description,
        isActive: true, // Luôn luôn là true
        emails: companyInfo.emails,
        phoneNumbers: companyInfo.phoneNumbers,
      };
      console.log(updatedCompanyInfo);

      try {
        const response = await fetch('/companies', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token.value}`, // Nếu token được yêu cầu
          },
          body: JSON.stringify(updatedCompanyInfo),
        });
    
        if (response.ok) {
          console.log('Company info updated successfully!');
          setIsEditing(false);
          setInitialCompanyInfo(updatedCompanyInfo); // Cập nhật dữ liệu ban đầu
        } else {
          console.error('Error updating company info:', response.statusText);
        }
      } catch (error) {
        console.error('Error during API request:', error);
      }
    };
    

    const handleExitClick = () => {
      setCompanyInfo(initialCompanyInfo); // Khôi phục dữ liệu ban đầu
      setIsEditing(false);
    };

    const handleProvinceChange = (e) => {
      console.log("Selected Province:", e.target.value);
      setSelectedProvince(e.target.value); // Cập nhật province
      setSelectedCity('');  // Reset city
      setSelectedDistrict('');  // Reset district
    };
    

    const handleCityChange = (e) => {
      console.log("Selected City:", e.target.value);
      setSelectedCity(e.target.value); // Cập nhật city
      setSelectedDistrict('');  // Reset district
    };

    const handleDistrictChange = (e) => {
      console.log("Selected District:", e.target.value);
      setSelectedDistrict(e.target.value); // Cập nhật district
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
      return <div>Loading...</div>;
    }

    const { name, taxCode, description, emails, phoneNumbers } = companyInfo;
    

    return (
      <div id='content-company-info'>
        <h2>Welcome {name}</h2>
        <h3>Thông tin cơ bản</h3>

        <div className="company-info">
          <img src={`https://via.placeholder.com/150`} alt="Company Logo" className="company-logo" />
          <button className="upload-btn">Tải lên</button>
          <p className="file-info">
            Max file size is 1MB, Minimum dimension: 300x300 And Suitable files are .jpg & .png
          </p>
        </div>

        <div className="company-details">
          <label>Tên Công Ty</label>
          <input
            type="text"
            value={name}
            readOnly={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, name: e.target.value })}
          />

          <label>Mã số thuế</label>
          <input
            type="text"
            value={taxCode}
            readOnly={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, taxCode: e.target.value })}
          />

          <label>Mô tả</label>
          <textarea
            rows="5"
            cols="50"
            value={description}
            readOnly={!isEditing}
            onChange={(e) => setCompanyInfo({ ...companyInfo, description: e.target.value })}
          />
        </div>

        <div className="contact-info">
          <h3>Contact Information</h3>

          <label>Số điện thoại</label>
          {phoneNumbers.map((phone, index) => (
          <div key={index} className="input-with-buttons">
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
              <button type="button" onClick={() => handleDeletePhone(index)}>-</button>
            )}
          </div>
        ))}
        {isEditing && (
          <button type="button" onClick={handleAddPhone}>+ Thêm số điện thoại</button>
        )}

          <label>Email</label>
          {emails.map((email, index) => (
          <div key={index} className="input-with-buttons">
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
              <button type="button" onClick={() => handleDeleteEmail(index)}>-</button>
            )}
          </div>
        ))}
        {isEditing && (
          <button type="button" onClick={handleAddEmail}>+ Thêm email</button>
        )}


          <label>Ngành</label>
          {Array.isArray(companyInfo.fields) && companyInfo.fields.map((field, index) => (
          <div key={index} className="input-with-buttons">
            <select
              value={field || ''}
              onChange={(e) => handleFieldChange(index, e.target.value)}
              disabled={!isEditing}
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
                className="delete-btn"
              >
                -
              </button>
            )}
          </div>
        ))}

          {isEditing && (
            <button
              type="button"
              onClick={() =>
                setCompanyInfo({
                  ...companyInfo,
                  fields: [...(companyInfo.fields || []), ''], // Thêm ngành mới
                })
              }
            >
              + Thêm ngành
            </button>
          )}

          <div className="location-info">
            <div>
              <label>Tỉnh</label>
              <select
                value={selectedProvince || ''} // Giá trị mặc định nếu chưa có
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

            <div>
              <label>Thành phố</label>
              <select
                value={selectedCity || ''} // Giá trị mặc định nếu chưa có
                onChange={handleCityChange}
                disabled={!isEditing || !selectedProvince}
              >
                <option value=''>Chọn Thành Phố</option>
                {locations
                  .find((province) => province.name === selectedProvince)?.cities.map((city) => (
                    <option key={city.id} value={city.name}>
                      {city.name}
                    </option>
                  ))}
              </select>
            </div>

            <div>
              <label>Huyện</label>
              <select
                value={selectedDistrict || ''} // Giá trị mặc định nếu chưa có
                onChange={handleDistrictChange}
                disabled={!isEditing || !selectedCity}
              >
                <option value=''>Chọn Huyện</option>
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

        </div>

        <div className="button-group">
          <button className="update-btn" onClick={isEditing ? handleSaveClick : handleEditClick}>
            {isEditing ? "Lưu" : "Chỉnh sửa"}
          </button>
          {isEditing && (
            <button className="exit-btn" onClick={handleExitClick}>
              Thoát
            </button>
          )}
        </div>
      </div>
    );
  };

  export default CompanyInfo;
