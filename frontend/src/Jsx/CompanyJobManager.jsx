import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/CompanyJobManager.css";
import TokenManager from "../utils/tokenManager";


const CompanyJobManagement = () => {
  const [companyData, setCompanyData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  
  const [userInfo, setUserInfo] = useState(null);
  const [role, setRole] = useState(null); // State để lưu role
  const [userId, setUserId] = useState(null); // State để lưu userId

  const token = TokenManager.getToken();
//   useEffect(() => {
//     if (token) {
//       setRole(token.role?.toLowerCase()); // Lấy role từ token
//       setUserId(token.id); // Lấy userId từ token
//     }
//   }, [token]);

useEffect(() => {
    const token = TokenManager.getToken();
    if (token) {
      const extractedUserId = token.id; // Lấy userId từ token
      setUserId(extractedUserId);
    } else {
      console.error("Token not found or invalid");
      setError("Không thể xác thực người dùng.");
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const fetchCompanyData = async () => {
      if (!userId) return; // Chờ userId sẵn sàng
      try {
        const response = await axios.get(`/public/companies/${userId}`);
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

  if (loading) return <div>Đang tải dữ liệu...</div>;
  if (error) return <div>{error}</div>;

  const handleJobClick = (jobId) => {
    navigate(`/JobDetail/${jobId}`);
  };

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
            </div>
          ))}
        </div>
      ) : (
        <p>Không có bài đăng nào.</p>
      )}
    </div>
  );
};

export default CompanyJobManagement;
