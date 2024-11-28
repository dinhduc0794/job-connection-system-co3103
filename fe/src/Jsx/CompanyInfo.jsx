import React from 'react';
import '../css/CompanyInfo.css'

const CompanyInfo = () => {

  const userID = localStorage.getItem('id');
  
  return (
    
    <div id='content-company-info'>
      <h2>Welcome Decathlon VN</h2>
      <h3>Thông tin cơ bản</h3>

      {/* Company Logo and Upload Button */}
      <div className="company-info">
        <img src="https://via.placeholder.com/150" alt="Company Logo" className="company-logo" />
        <button className="upload-btn">Tải lên</button>
        <p className="file-info">
          Max file size is 1MB, Minimum dimension: 300x300 And Suitable files are .jpg & .png
        </p>
      </div>

      {/* Company Details */}
      <div className="company-details">
        <label>Tên Công Ty</label>
        <input type="text" value="Decathlon VN" readOnly />

        <label>Description</label>
        <textarea rows="5" cols="50" defaultValue="Decathlon là một trong những nhà bán lẻ các mặt hàng về thể thao lớn nhất thế giới..." />
      </div>

      {/* Contact Information */}
      <div className="contact-info">
        <h3>Contact Information</h3>

        <label>Phone Number</label>
        <input type="text" defaultValue="028 3840 5336" />

        <label>Email</label>
        <input type="email" defaultValue="decathlon@gmail.com" />

        <label>Website</label>
        <input type="url" defaultValue="https://www.decathlon.vn/vi/" />

        <div className="location-info">
          <div>
            <label>City/Province</label>
            <select>
              <option>Ho Chi Minh</option>
            </select>
          </div>
          <div>
            <label>District/Town</label>
            <select>
              <option>Cu Chi District</option>
            </select>
          </div>
        </div>

        <label>Address</label>
        <textarea rows="2" defaultValue="Pearl Plaza, 561A Điện Biên Phủ, Phường 25, Bình Thạnh, Hồ Chí Minh"></textarea>
      </div>

      {/* Update Button */}
      <button className="update-btn">Cập nhật</button>
    </div>
  );
};

export default CompanyInfo;
