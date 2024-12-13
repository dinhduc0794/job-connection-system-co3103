import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/CompanyJobManager.css";
import TokenManager from "../utils/tokenManager";
import LoadInfo from "./LoadingInfo";

const CompanyJobManagement = () => {
  const [companyData, setCompanyData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [selectedJobId, setSelectedJobId] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false); // Thêm state này
  const navigate = useNavigate();
  const [userId, setUserId] = useState(null);

  const token = TokenManager.getToken();

  useEffect(() => {
    const token = TokenManager.getToken();
    if (token) {
      const extractedUserId = token.id;
      setUserId(extractedUserId);
    } else {
      setError("Không thể xác thực người dùng.");
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const fetchCompanyData = async () => {
      if (!userId) return;
      try {
        const response = await axios.get(`http://47.128.243.193:8080/public/companies/${userId}`);
        setCompanyData(response.data);
        setLoading(false);
      } catch (err) {
        console.error("Error fetching company data:", err);
        setError("Không thể tải dữ liệu công ty.");
        setLoading(false);
      }
    };

    fetchCompanyData();
  }, [userId]);

  const handleDeleteJob = async () => {
    if (!token || !selectedJobId) {
      console.error("Token hoặc Job ID không hợp lệ.");
      return;
    }
    setIsDeleting(true); // Bắt đầu trạng thái đang xóa
    try {
      const response = await axios.delete(`http://47.128.243.193:8080/companies/jobpostings/${selectedJobId}`, {
        headers: {
          Authorization: `Bearer ${token.value}`,
          "Content-Type": "application/json",
        },
      });

      setCompanyData((prev) => ({
        ...prev,
        jobPostings: prev.jobPostings.filter((job) => job.id !== selectedJobId),
      }));

      setShowConfirmDelete(false);
      console.log("Job deleted successfully:", response.data);
    } catch (err) {
      console.error("Error deleting job:", err);
      alert("Không thể xóa bài.");
    } finally {
      setIsDeleting(false); // Kết thúc trạng thái đang xóa
    }
  };

  const handleJobClick = (jobId) => {
    navigate(`/JobDetail/${jobId}`);
  };

  const openConfirmDelete = (event, jobId) => {
    event.stopPropagation(); // Ngăn điều hướng
    setSelectedJobId(jobId);
    setShowConfirmDelete(true);
  };

  const closeConfirmDelete = () => {
    if (!isDeleting) { // Chỉ cho phép đóng khi không đang xóa
      setShowConfirmDelete(false);
      setSelectedJobId(null);
    }
  };

  if (loading) return <LoadInfo text="Đang tải việc làm ..." />;
  if (error) return <div>{error}</div>;

  return (
    <div className="company-job-management-container">
      <h2>Các Bài Đã Đăng</h2>
      {companyData?.jobPostings && companyData.jobPostings.length > 0 ? (
        <div className="job-list">
          {companyData.jobPostings.map((job) => (
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
                Xóa
              </button>
            </div>
          ))}
        </div>
      ) : (
        <p>Không có bài đăng nào.</p>
      )}

      {/* Pop-up xác nhận xóa */}
      {showConfirmDelete && (
        <div className="confirm-delete-popup">
          <div className="popup-content">
            <h1>Bạn có chắc chắn muốn xóa bài đăng này không?</h1>
            <div className="popup-actions">
              <button
                className="cancel-button"
                onClick={closeConfirmDelete}
                disabled={isDeleting}
              >
                Hủy
              </button>
              <button
                className="confirm-button"
                onClick={handleDeleteJob}
                disabled={isDeleting}
              >
                {isDeleting ? "Đang xóa..." : "Xác nhận"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CompanyJobManagement;
