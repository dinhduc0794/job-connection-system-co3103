import React from 'react';

const ChangePassword = () => {
  return (
    <div className='change-pass'>
        <h2>Đổi Mật Khẩu</h2>
        <label>Mật Khẩu Cũ</label>
        <input type="text" defaultValue="********" />

        <label>Mật Khẩu Mới</label>
        <input type="text" defaultValue="********" />

        <label>Xác Nhận Mật Khẩu</label>
        <input type="text" defaultValue="********" />

    </div>
  );
};

export default ChangePassword;