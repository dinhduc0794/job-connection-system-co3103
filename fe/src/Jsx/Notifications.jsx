import React, { useState, useEffect } from 'react';
import '../css/Notifications.css';
import TokenManager from '../utils/tokenManager';
import LoadInfo from './LoadingInfo';
import { FaSpinner } from 'react-icons/fa';

const Notifications = () => {
  const [notifications, setNotifications] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isDeleting, setIsDeleting] = useState(false); // Trạng thái đang xóa
  const [selectedNotificationId, setSelectedNotificationId] = useState(null); // ID thông báo cần xóa
  const [showConfirmDelete, setShowConfirmDelete] = useState(false); // Trạng thái hiển thị pop-up
  const token = TokenManager.getToken();
  const userId = token?.id; // Lấy userId từ token

  useEffect(() => {
    if (userId) {
      // Gọi API để lấy dữ liệu thông báo
      fetch(`http://47.128.243.193:8080/notifications/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      })
        .then(response => response.json())
        .then(data => {
          // Sắp xếp thông báo theo id giảm dần
          const sortedData = data.sort((a, b) => b.id - a.id);
          setNotifications(sortedData);
          setIsLoading(false);
        })
        .catch(error => {
          console.error('Error fetching notifications:', error);
          setIsLoading(false);
        });
    }
  }, [userId]);

  const handleDelete = () => {
    setIsDeleting(true); // Đặt trạng thái đang xóa
    // Gọi API DELETE để xóa thông báo
    fetch(`http://47.128.243.193:8080/notifications/${selectedNotificationId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token.value}`,
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to delete notification');
        }
        // Nếu xóa thành công, cập nhật lại state
        setNotifications(notifications.filter(notification => notification.id !== selectedNotificationId));
        alert('Đã xóa thông báo thành công');
        setShowConfirmDelete(false); // Ẩn pop-up sau khi xóa thành công
      })
      .catch(error => {
        console.error('Error deleting notification:', error);
      })
      .finally(() => {
        setIsDeleting(false); // Đặt lại trạng thái không còn xóa nữa
      });
  };

  const openDeletePopup = (id) => {
    setSelectedNotificationId(id);
    setShowConfirmDelete(true);
  };

  const closeDeletePopup = () => {
    setShowConfirmDelete(false);
    setSelectedNotificationId(null);
  };

  if (isLoading) {
    return <LoadInfo text="Đang tải thông báo" />;
  }

  return (
    <div className="notifications">
      <h2>Thông báo</h2>
      {notifications.length === 0 ? (
        <p>No notifications available.</p>
      ) : (
        <ul>
          {notifications.map(notification => (
            <li key={notification.id} className="notification-item">
              <div className="notification-content">
                <p>{notification.content}</p>
              </div>
              <button
                className="delete-button"
                onClick={() => openDeletePopup(notification.id)}
              >
                Xóa
              </button>
            </li>
          ))}
        </ul>
      )}

      {showConfirmDelete && (
        <div className="confirm-delete-popup">
          <div className="popup-content">
            <h1>Bạn có chắc chắn muốn xóa thông báo này?</h1>
            <div className="popup-actions">
              <button
                className="cancel-button"
                onClick={closeDeletePopup}
                disabled={isDeleting}
              >
                Hủy
              </button>
              <button
                className="confirm-button"
                onClick={handleDelete}
                disabled={isDeleting}
              >
                {isDeleting ? <FaSpinner className="spinner" /> : 'Xác nhận'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Notifications;
