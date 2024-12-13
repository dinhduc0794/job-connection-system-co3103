import React, { useState, useEffect } from "react";
import TokenManager from "../utils/tokenManager";
import "../css/ChangePassword.css";

const ChangePassword = () => {
  const [role, setRole] = useState(null);
  const [userId, setUserId] = useState(null);
  const [userInfo, setUserInfo] = useState(null);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [showPasswords, setShowPasswords] = useState(false); // Trạng thái hiển thị mật khẩu

  const token = TokenManager.getToken();

  // Lấy role và userId từ token
  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase());
      setUserId(token.id);
    }
  }, [token]);

  // Lấy thông tin user từ API
  useEffect(() => {
    if (role && userId) {
      const url = role === "applicant"
        ? `http://47.128.243.193:8080/applicants/${userId}`
        : `http://47.128.243.193:8080/companies/${userId}`;

      fetch(url, {
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json'
        }
      })
        .then((response) => response.json())
        .then((data) => setUserInfo(data))
        .catch((error) => console.error("Error fetching user info:", error));
    }
  }, [role, userId]);

  // Xử lý khi nhấn nút "Đổi Mật Khẩu"
  const handleChangePassword = () => {
    if (!userInfo) return;

    if (oldPassword !== userInfo.password) {
      setErrorMessage("Mật khẩu cũ không chính xác.");
      setTimeout(() => setErrorMessage(null), 3000);
      setSuccessMessage("");
      return;
    }

    if (newPassword !== confirmPassword) {
      setErrorMessage("Mật khẩu mới và xác nhận mật khẩu không khớp.");
      setTimeout(() => setErrorMessage(null), 3000);
      setSuccessMessage("");
      return;
    }

    const payload = {
      id: userId,
      username: userInfo.username,
      password: oldPassword,
      newPassword: newPassword,
    };

    fetch("http://47.128.243.193:8080/accounts/reset-password", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': `Bearer ${token.value}`
      },
      body: JSON.stringify(payload),
    })
      .then((response) => {
        if (response.ok) {
          setSuccessMessage("Đổi mật khẩu thành công!");
          setErrorMessage("");
        } else {
          throw new Error("Đổi mật khẩu thất bại.");
        }
      })
      .catch((error) => {
        setErrorMessage(error.message);
        setSuccessMessage("");
      });
  };

  // Đổi trạng thái hiển thị mật khẩu
  const toggleShowPasswords = () => setShowPasswords(!showPasswords);

  return (
    <div className="change-pass">
      <h2>Đổi Mật Khẩu</h2>

      {errorMessage && <p className="error">{errorMessage}</p>}
      {successMessage && <p className="success">{successMessage}</p>}

      <label>Mật Khẩu Cũ</label>
      <div className="input-container">
        <input
          type={showPasswords ? "text" : "password"}
          value={oldPassword}
          onChange={(e) => setOldPassword(e.target.value)}
        />
      </div>

      <label>Mật Khẩu Mới</label>
      <div className="input-container">
        <input
          type={showPasswords ? "text" : "password"}
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />
      </div>

      <label>Xác Nhận Mật Khẩu</label>
      <div className="input-container">
        <input
          type={showPasswords ? "text" : "password"}
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
      </div>

      <button className="toggle-password" onClick={toggleShowPasswords}>
        {showPasswords ? "Ẩn mật khẩu" : "Hiển thị mật khẩu"}
      </button>

      <button className="change-password-btn" onClick={handleChangePassword}>
        Đổi Mật Khẩu
      </button>
    </div>
  );
};

export default ChangePassword;
