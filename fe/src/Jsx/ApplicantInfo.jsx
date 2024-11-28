import React, { useState, useEffect } from 'react';
import Navbar from "./navbar";
import AppNavbar from "./AppNavbar";
import CompanyNavbar from "./CompanyNavbar";
import '../css/ApplicantInfo.css'

const ApplicantInfo = () => {

    return (
        <div className='content-info'>
              <h3>Cài Đặt Thông Tin Cá Nhân</h3>

                <div>
                    <label>Họ Và Tên</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                </div>

                <div>
                    <label>Số Điện Thoạt</label>
                    <input
                        type="text"
                        placeholder="Nhập số điện thoại"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                    />
                </div>

                <div>
                    <label>Email</label>
                    <input type="text" value="abc@gmail.com" disabled  />
                </div>

            {/* Update Button */}
            <button className="update-btn" onClick={handleUpdate}>Cập nhật</button>
            </div>
    )
}
export default ApplicantInfo;