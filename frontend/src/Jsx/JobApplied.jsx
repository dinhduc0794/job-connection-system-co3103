import React, { useEffect, useState } from 'react';
import '../css/JobApplied.css'
import axios from 'axios';
import TokenManager from "../utils/tokenManager";
import { useHistory, useNavigate } from 'react-router-dom';


const JobApplied = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [userInfo, setUserInfo] = useState(null);
  const [role, setRole] = useState(null); // State để lưu role
  const [error, setError] = useState(null);

  const token = TokenManager.getToken();
  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase());
    }
  }, [token]);

  useEffect(() => {
    // Lấy dữ liệu từ API
    const fetchApplications = async () => {
      try {
        const response = await axios.get(`/applicants/${token.id}/applications`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token.value}`
              },
              credentials: 'include',
        });
        setApplications(response.data.data);
      } catch (error) {
        console.error('Error fetching applications:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchApplications();
  }, []);
  const navigate = useNavigate();

  const handleJobClick = (jobPostingId) => {
    // Điều hướng đến trang chi tiết công việc
    navigate(`/JobDetail/${jobPostingId}`);
  };

  if (loading) {
    return <div>Loading...</div>;
  }
  if (!applications || applications.length === 0) {
    return <div>Không có đơn ứng tuyển nào.</div>;
  }

  if (error) return <div>{error}</div>;

  return (
    <div className="job-applied-container">
      <h2>Danh sách công việc đã ứng tuyển</h2>
      <table>
        <thead>
          <tr>
            <th>Tiêu đề công việc: </th>
            <th>Trạng thái: </th>
            <th>Email: </th>
            <th>Số điện thoại: </th>
            <th>CV: </th>
          </tr>
        </thead>
        <tbody>
          {applications && applications.map((application) => (
            <tr
              key={application.id}
              onClick={() => handleJobClick(application.jobPostingId)}
              style={{ cursor: 'pointer' }}
            >
              <td>{application.title}</td>
              <td>{application.status}</td>
              <td>{application.email}</td>
              <td>{application.phoneNumber}</td>
              <td>{application.resume}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default JobApplied;
