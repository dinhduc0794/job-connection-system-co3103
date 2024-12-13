import React, { useEffect, useState } from 'react';
import '../css/ApplicationsPopup.css';
import TokenManager from "../utils/tokenManager";
import SpinLoad from '../images/spin-load.gif'

const ApplicationsPopup = ({ jobId, onClose }) => {
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchQuery, setSearchQuery] = useState("");
    const [filterStatus, setFilterStatus] = useState("");
    const itemsPerPage = 10;
    const [saving, setSaving] = useState(false);
    const [showConfirmDelete, setShowConfirmDelete] = useState(false);  // Trạng thái hiển thị pop-up xác nhận xóa
    const [deleteAppId, setDeleteAppId] = useState(null);  // ID của ứng viên đang muốn xóa
    const [isDeleting, setIsDeleting] = useState(false); // Thêm state này

    const token = TokenManager.getToken();

    useEffect(() => {
        const fetchApplications = async () => {
            try {
                const response = await fetch(`http://47.128.243.193:8080/companies/jobpostings/${jobId}/applications`, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token.value}`
                    }
                });

                if (!response.ok) {
                    throw new Error(`Failed to fetch applications: ${response.status}`);
                }

                const data = await response.json();
                setApplications(data.data.map(app => ({ ...app, isEditing: false })));
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchApplications();
    }, [jobId]);

    const handleEditStatus = (id) => {
        setApplications(prev =>
            prev.map(app => app.applicationId === id ? { ...app, isEditing: true } : app)
        );
    };

    const handleSaveStatus = async (id, newStatus) => {
        const application = applications.find(app => app.applicationId === id);

        if (!application) {
            alert("Không tìm thấy đơn ứng tuyển!");
            return;
        }

        try {
            setSaving(true);
            const payload = {
                id: application.applicationId,
                status: newStatus,
                applicantId: application.applicantId,
                jobPostingId: jobId,
                email: application.email,
                phoneNumber: application.phoneNumber,
                description: application.description,
                resume: application.resume
            };

            const response = await fetch(`http://47.128.243.193:8080/companies/jobpostings/applications`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token.value}`
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error(`Failed to update status: ${response.status}`);
            }

            setApplications(prev =>
                prev.map(app => app.applicationId === id ? { ...app, status: newStatus, isEditing: false } : app)
            );

            alert("Cập nhật trạng thái thành công!");
        } catch (err) {
            console.error(err.message);
            alert("Cập nhật trạng thái thất bại.");
        } finally {
            setSaving(false);
        }
    };

    const handleCancelEdit = (id) => {
        setApplications(prev =>
            prev.map(app => app.applicationId === id ? { ...app, isEditing: false } : app)
        );
    };

    const filteredApplications = applications.filter(app => {
        const matchesSearch =
            app.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            app.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
            app.phoneNumber.includes(searchQuery);
        const matchesStatus = filterStatus ? app.status === filterStatus : true;

        return matchesSearch && matchesStatus;
    });

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredApplications.slice(indexOfFirstItem, indexOfLastItem);
    const totalPages = Math.ceil(filteredApplications.length / itemsPerPage);

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleStatusChange = (id, newStatus) => {
        setApplications(prev =>
            prev.map(app =>
                app.applicationId === id ? { ...app, status: newStatus } : app
            )
        );
    };

    // Hàm xử lý xóa ứng tuyển
    const handleDeleteApplication = async (id) => {
        const application = applications.find(app => app.applicationId === id);

        if (!application) {
            alert("Không tìm thấy đơn ứng tuyển!");
            return;
        }

        if (application.status !== "REJECTED") {
            alert("Chỉ có thể xóa đơn ứng tuyển có trạng thái 'REJECTED'");
            return;
        }

        try {
            setIsDeleting(true);
            const response = await fetch(`http://47.128.243.193:8080/companies/jobpostings/applications/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token.value}`
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to delete application: ${response.status}`);
            }

            setApplications(prev => prev.filter(app => app.applicationId !== id));
            alert("Đã xóa đơn ứng tuyển thành công!");
        } catch (err) {
            console.error(err.message);
            alert("Xóa đơn ứng tuyển thất bại.");
        } finally {
            setShowConfirmDelete(false);  // Đóng pop-up xác nhận xóa
            setIsDeleting(false);
            setDeleteAppId(null);
        }
    };

    // Mở pop-up xác nhận xóa
    const handleShowConfirmDelete = (id) => {
        setShowConfirmDelete(true);
        setDeleteAppId(id);
    };

    // Đóng pop-up xác nhận xóa
    const handleCloseConfirmDelete = () => {
        setShowConfirmDelete(false);
        setDeleteAppId(null);
    };

    if (loading) {
        return (
            <div className="applications-popup">
                <div className="popup-content">
                    <h2>Danh sách các đơn ứng tuyển</h2>
                    <div className="loading-spinner">
                        <img src={SpinLoad} size={200} />

                        <p className='loading-p'>Đang tải các đơn ứng tuyển...</p>
                    </div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="applications-popup">
                <div className="popup-content">
                    <h2>Danh sách các đơn ứng tuyển</h2>
                    <p className="error">Hiện tại chưa có đơn ứng tuyển nào</p>
                    <button onClick={onClose} className="close-btn">x</button>
                </div>
            </div>
        );
    }

    return (
        <div className="applications-popup">
            <div className="popup-content">
                <h2>Danh sách các đơn ứng tuyển</h2>
                <button onClick={onClose} className="close-btn">x</button>

                <div className="filter-container">
                    <input
                        type="text"
                        placeholder="Tìm kiếm theo tên, email, số điện thoại..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="search-bar"
                    />
                    <select
                        value={filterStatus}
                        onChange={(e) => setFilterStatus(e.target.value)}
                        className="status-filter"
                    >
                        <option value="">Tất cả trạng thái</option>
                        <option value="WAITING">WAITING</option>
                        <option value="ACCEPTED">ACCEPTED</option>
                        <option value="REJECTED">REJECTED</option>
                    </select>
                </div>

                {filteredApplications.length === 0 ? (
                    <p>Không có đơn ứng tuyển nào phù hợp.</p>
                ) : (
                    <>
                        <table className="applications-table">
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>
                                    <th>Mô tả</th>
                                    <th>CV</th>
                                    <th>Trạng thái</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                {currentItems.map((app) => (
                                    <tr key={app.applicationId}>
                                        <td>{app.name}</td>
                                        <td>{app.email}</td>
                                        <td>{app.phoneNumber}</td>
                                        <td>{app.description}</td>
                                        <td>{app.resume}</td>
                                        <td>
                                            {app.isEditing ? (
                                                <select
                                                    value={app.status}
                                                    onChange={(e) => handleStatusChange(app.applicationId, e.target.value)}
                                                >
                                                    <option value="WAITING">WAITING</option>
                                                    <option value="ACCEPTED">ACCEPTED</option>
                                                    <option value="REJECTED">REJECTED</option>
                                                </select>
                                            ) : (
                                                app.status
                                            )}
                                        </td>
                                        <td>
                                            {app.isEditing ? (
                                                <>
                                                    <button className='sa-btn' onClick={() => handleSaveStatus(app.applicationId, app.status)} disabled={saving}>
                                                        {!saving ? "Lưu" : "Đang lưu"}
                                                    </button>
                                                    <button className='del-btn' onClick={() => handleCancelEdit(app.applicationId)} disabled={saving}>Hủy</button>
                                                </>
                                            ) : (
                                                <>
                                                    <button className='sa-btn' onClick={() => handleEditStatus(app.applicationId)}>Chỉnh sửa</button>
                                                    <button className='del-btn' onClick={() => handleShowConfirmDelete(app.applicationId)}>Xóa</button>
                                                </>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>

                        {showConfirmDelete && (
                            <div className="confirm-delete-popup">
                                <div className="confirm-delete-content">
                                    <p>Bạn có chắc chắn muốn xóa ứng tuyển này không?</p>
                                    <button onClick={() => handleDeleteApplication(deleteAppId)}>Xác nhận</button>
                                    <button onClick={handleCloseConfirmDelete}>Hủy</button>
                                </div>
                            </div>
                        )}

                        {/* Pop-up xác nhận xóa */}
                        {showConfirmDelete && (
                            <div className="confirm-delete-popup">
                                <div className="popup-delete-content">
                                    <h1>Bạn có chắc chắn muốn xóa đơn ứng tuyển này không?</h1>
                                    <div className="popup-actions">
                                        <button
                                            className="cancel-button"
                                            onClick={() => handleDeleteApplication(deleteAppId)}
                                            disabled={isDeleting}
                                        >
                                            Hủy
                                        </button>
                                        <button
                                            className="confirm-button"
                                            onClick={handleCloseConfirmDelete}
                                            disabled={isDeleting}
                                        >
                                            {isDeleting ? "Đang xóa..." : "Xác nhận"}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </>
                )}

                <div className="pagination">
                    <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>Trước</button>
                    <span>{currentPage}/{totalPages}</span>
                    <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>Sau</button>
                </div>
            </div>
        </div>
    );
};

export default ApplicationsPopup;
