import React from 'react';
import '../css/ConfirmModal.css'

const ConfirmModal = ({ isVisible, onConfirm, onCancel }) => {
  if (!isVisible) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Xác nhận chỉnh sửa</h2>
        <p>Bạn có chắc chắn muốn chỉnh sửa thông tin này?</p>
        <div className="button-container">
          <button className="cancel-btn" onClick={onCancel}>Hủy</button>
          <button className="confirm-btn" onClick={onConfirm}>Xác nhận</button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;
