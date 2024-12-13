import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import TokenManager from "../utils/tokenManager";
import LoadInfo from "./LoadingInfo";

const AppSavedJob = () => {
    const [savedJobs, setSavedJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [role, setRole] = useState(null);
    const [userId, setUserId] = useState(null);
    const [isDeleting, setIsDeleting] = useState(false); // Thêm state này    
    const [showConfirmDelete, setShowConfirmDelete] = useState(false);
    const [selectedJobId, setSelectedJobId] = useState(null);
    const [notification, setNotification] = useState(null); // Trạng thái thông báo

    const token = TokenManager.getToken();

    // useEffect(() => {
    //     if (token) {
    //         setRole(token.role?.toLowerCase());
    //         setUserId(token.id);
    //     }
    // }, [token]);


    useEffect(() => {
        // Gọi API với fetch
        fetch(`http://47.128.243.193:8080/applicants/${token.id}/interested-posts`, {
            headers: {
                Authorization: `Bearer ${token.value}`,
                "Content-Type": "application/json",
            }
        })
            .then((response) => response.json())
            .then((data) => {
                setSavedJobs(data.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching data: ", error);
                setLoading(false);
            });
    }, [token.id, token]);

    const closeConfirmDelete = () => {
        if (!isDeleting) { // Chỉ cho phép đóng khi không đang xóa
            setShowConfirmDelete(false);
            setSelectedJobId(null);
        }
    };

    const handleJobClick = (jobId) => {
        navigate(`/JobDetail/${jobId}`);
    };

    const handleSave = async () => {

        if (!token.id) {
            setNotification('Vui lòng đăng nhập để lưu bài đăng!');
            setTimeout(() => setNotification(null), 3000);
            return;
        }
        else if (token.role === 'company') {
            setNotification('Bạn phải là ứng viên mới có thể lưu bài đăng!');
            setTimeout(() => setNotification(null), 3000);
            return;
        }

        setIsDeleting(true); // Bắt đầu lưu, thay đổi biểu tượng thành GIF loading
        try {
            const response = await fetch('http://47.128.243.193:8080/applicants/interested-posts', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token.value}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    applicantId: token.id,
                    jobPostingId: selectedJobId
                })
            });

            const result = await response.json();

            if (response.ok) {
                setNotification(result.message); // Hiển thị thông báo từ API
                setTimeout(() => setNotification(null), 3000);
            } else {
                setNotification('Đã xảy ra lỗi. Vui lòng thử lại!');
                setTimeout(() => setNotification(null), 3000);
            }
        } catch (error) {
            console.error('Error saving post:', error);
            setNotification('Đã xảy ra lỗi. Vui lòng thử lại!');
            console('Đã xảy ra lỗi!')
            setTimeout(() => setNotification(null), 3000);
        } finally {
            setIsDeleting(false); // Kết thúc quá trình lưu, trở lại biểu tượng tim
            setTimeout(() => setNotification(null), 3000);
            setSelectedJobId(null);
            setShowConfirmDelete(false);

        }
    };

    const openConfirmDelete = (event, jobId) => {
        event.stopPropagation(); // Ngăn điều hướng
        setSelectedJobId(jobId);
        setShowConfirmDelete(true);
    };

    if (loading) {
        return <LoadInfo text="Đang tải việc làm ..." />;
    }

    return (
        <div className="company-job-management-container">
            {notification && (
                <div className="notification-popup">
                    {notification}
                </div>
            )}
            <h2>Tin đã lưu</h2>
            {savedJobs && savedJobs.length > 0 ? (
                savedJobs.map((job) => (
                    <div
                        key={job.id}
                        className="job-item"
                        onClick={() => handleJobClick(job.id)}
                    >
                        <div className="job-header">
                            <h3 className="job-title">{job.title}</h3>
                            <span className="job-schedule">{job.schedule}</span>
                        </div>
                        <p className="job-location">
                            {job.ward}, {job.city}, {job.province}
                        </p>
                        <p className="job-salary">
                            Mức lương: {job.minSalary} - {job.maxSalary} triệu VND
                        </p>
                        <p className="job-skills">Kỹ năng: {job.skills}</p>
                        <button
                            className="delete-job-button"
                            onClick={(event) => openConfirmDelete(event, job.id)}
                        >
                            Hủy
                        </button>
                    </div>
                ))
            ) : (
                <p>Chưa lưu tin nào</p>
            )}
            {/* Pop-up xác nhận xóa */}
            {showConfirmDelete && (
                <div className="confirm-delete-popup">
                    <div className="popup-content">
                        <h1>Bạn có chắc chắn muốn hủy lưu bài đăng này không?</h1>
                        <div className="popup-actions">
                            <button
                                className="cancel-button"
                                onClick={closeConfirmDelete}
                                disabled={isDeleting}
                            >
                                Thoát
                            </button>
                            <button
                                className="confirm-button"
                                onClick={handleSave}
                                disabled={isDeleting}
                            >
                                {isDeleting ? "Đang hủy..." : "Xác nhận"}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AppSavedJob;
