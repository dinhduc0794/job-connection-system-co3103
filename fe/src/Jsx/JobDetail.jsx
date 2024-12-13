import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  MapPin,
  DollarSign,
  Globe,
  Layers, //cap bac
  Mail,
  Phone,
  BookText, //hình thuc
  ReceiptText, //so luong tuyen
  ClipboardType,
  Tag,
  Loader,
} from "lucide-react";
import SpinLoad from '../images/spin-load.gif'
import Navbar from './navbar';
import Footer from './Footer';
import ApplicationForm from "./ApplicationForm";
import Loading from "./Loading";
import ErrorBoundary from 'antd/es/alert/ErrorBoundary';
import '../css/JobDetail.css'; // File CSS để style
import DefaultImage from '../images/logo3.png'
import { useAuth } from '../Contexts/AuthContext';
import AppNavbar from './AppNavbar';
import CompanyNavbar from './CompanyNavbar';
import TokenManager from "../utils/tokenManager";
import Loadingedit from '../images/Load_info.gif'
import ApplicationsPopup from './ApplicationsPopup';


function JobDetail() {
  const { id } = useParams(); // Lấy `id` từ URL
  const navigate = useNavigate(); // Điều hướng trở lại danh sách công việc
  const [job, setJob] = useState(null);
  const [editedJob, setEditedJob] = useState({});
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [role, setRole] = useState(null); // State để lưu role
  const [userId, setUserId] = useState(null); // State để lưu userId
  const [isWarningPopupOpen, setIsWarningPopupOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const [notification, setNotification] = useState(null); // Trạng thái thông báo
  const [interestedPosts, setInterestedPosts] = useState([]);
  const [isSaving, setIsSaving] = useState(false);



  const token = TokenManager.getToken();
  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase()); // Lấy role từ token
      setUserId(token.id); // Lấy userId từ token
    }
  }, [token]);


  const renderNavbar = () => {
    if (role === 'applicant') {
      return <AppNavbar />;
    } else if (role === 'company') {
      return <CompanyNavbar />;
    } else {
      return <Navbar />;
    }
  };
  useEffect(() => {
    const fetchInterestedPosts = async () => {
      if (!userId) return; // Không gọi API nếu chưa có userId

      try {
        const response = await fetch(`http://47.128.243.193:8080/applicants/${userId}/interested-posts`, {
          headers: {
            'Authorization': `Bearer ${token?.value}`,
          },
        });

        if (response.ok) {
          const result = await response.json();
          setInterestedPosts(result.data?.map(post => post.id) || []);
        } else {
          console.error("Failed to fetch interested posts");
        }
      } catch (error) {
        console.error("Error fetching interested posts:", error);
      }
    };

    fetchInterestedPosts();
  }, [userId, token]);

  const handleApplyClick = () => {
    if (userId === job.companyId) {
      setIsPopupOpen(true);
    } else if (role !== 'applicant') {
      setIsWarningPopupOpen(true);
    } else {
      setIsPopupOpen(true);
    }
  };

  const handleWarningPopupClose = () => {
    setIsWarningPopupOpen(false);
  };

  const handleClosePopup = () => {
    setIsPopupOpen(false);
  };

  useEffect(() => {
    if (!id) {
      console.error('ID is undefined!');
      return;
    }

    const fetchJobDetails = async () => {
      try {
        const response = await fetch(`http://47.128.243.193:8080/public/jobpostings/${id}`);
        if (!response.ok) {
          throw new Error(`Failed to fetch job details: ${response.status}`);
        }
        const data = await response.json();
        setJob(data);
        setEditedJob(data); // Initialize editedJob with current job details
      } catch (error) {
        console.error('Error fetching job details:', error);
      } finally {
        setLoading(false); // ket thuc tai du lieu
      }
    };

    fetchJobDetails();
  }, [id]);
  const handleEditClick = () => {
    setIsEditModalOpen(true);
  };

  const handleModalClose = () => {
    setIsEditModalOpen(false);
  };
  const handleChange = (e) => {
    setEditedJob({
      ...editedJob,
      [e.target.name]: e.target.value,
    });
  };



  const handleSubmit = async () => {
    const jobData = {
      id: id, // Giữ nguyên id của công việc
      title: editedJob.title || "",
      description: (editedJob.description || ""), // Đảm bảo mô tả công việc sử dụng ký tự \\n thay vì \n
      schedule: (editedJob.schedule || "").toUpperCase().replace(/[^A-Z]/g, ""),
      level: (editedJob.level || "").toUpperCase(),
      minSalary: editedJob.minSalary || 0,
      maxSalary: editedJob.maxSalary || 0,
      image: editedJob.image || "", // Đảm bảo có ảnh, nếu không thì để rỗng hoặc giá trị mặc định
      numberOfApplicants: editedJob.numberOfApplicants || 0,
      allowance: editedJob.allowance || 0,
      //wardId: editedJob.wardId || 0, // Giữ wardId
      companyId: userId || 0,
      //jobTypeId: editedJob.jobTypeId || 0, // Giữ jobTypeId
      skills: [
        "AngularJS",
        "ReactJS",
        "NodeJS"
      ] || [], // Mảng kỹ năng
      status: editedJob.status,
    };

    setIsLoading(true); // Bắt đầu quá trình loading

    try {
      const response = await fetch('http://47.128.243.193:8080/companies/jobpostings', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(jobData), // Gửi jobData thay vì editedJob
      });

      if (response.ok) {
        alert('Cập nhật công việc thành công!');
        setIsEditModalOpen(false);
        // Optionally redirect or refresh job details
        window.location.reload();
      } else {
        alert('Có lỗi xảy ra khi cập nhật công việc.');
      }
    } catch (error) {
      console.error('Error updating job:', error);
    } finally {
      setIsLoading(false); // Kết thúc quá trình loading
    }
  };
  const handleSave = async (id) => {

    if (!userId) {
      setNotification('Vui lòng đăng nhập để lưu bài đăng!');
      setTimeout(() => setNotification(null), 3000);
      return;
    }
    else if (role === 'company') {
      setNotification('Bạn phải là ứng viên mới có thể lưu bài đăng!');
      setTimeout(() => setNotification(null), 3000);
      return;
    }

    setIsSaving(true); // Bắt đầu lưu, thay đổi biểu tượng thành GIF loading
    try {
      const response = await fetch('http://47.128.243.193:8080/applicants/interested-posts', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token.value}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          applicantId: userId,
          jobPostingId: id
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
      setTimeout(() => setNotification(null), 3000);
    } finally {
      setIsSaving(false); // Kết thúc quá trình lưu, trở lại biểu tượng tim
      setTimeout(() => setNotification(null), 3000);
    }
  };
  const handleSaveOrEdit = (id) => {
    if (userId === job.companyId) {
      // Nếu userId trùng với companyId, chuyển sang chế độ "Sửa"
      setIsEditModalOpen(true);
    } else {
      // Nếu không, lưu tin
      handleSave(id);
    }
  };
  if (loading) {
    return <Loading />;
  }
  if (!job) {
    return <ErrorBoundary />;
  }
  return (
    <div>
      {renderNavbar()}
      <div id="job-detail-container">
        {notification && (
          <div className="notification-popup">
            {notification}
          </div>
        )}
        <div className="content-container">

          {/* Main Content (Left Column) */}
          <div className="left-column">
            <div className="job-overview">
              <h2>{job.title || "Chưa có tiêu đề công việc"}</h2>
              <div className="job-info">
                <div className="info-item">
                  <span className="icon-info">
                    <DollarSign size={16} />
                  </span>
                  <div className="info-text">
                    <div className="top">Mức lương</div>
                    <div className="bottom">{job.minSalary} - {job.maxSalary} triệu</div>
                  </div>
                </div>

                <div className="info-item">
                  <span className="icon-info">
                    <MapPin size={16} />
                  </span>
                  <div className="info-text">
                    <div className="top">Địa điểm</div>
                    <div className="bottom">{job.province}</div>
                  </div>
                </div>

                <div className="info-item">
                  <span className="icon-info">
                    <Layers size={16} />
                  </span>
                  <div className="info-text">
                    <div className="top">Kinh nghiệm</div>
                    <div className="bottom">{!job.yoe ? "Không yêu cầu" : `${job.yoe} năm`}</div>
                  </div>
                </div>
              </div>
              <div className="button">
                <button className="apply-btn" onClick={handleApplyClick}>
                  {userId === job.companyId ? "Xem các đơn ứng tuyển" : "Ứng tuyển ngay"}
                </button>
                <button disabled={isSaving} className="save-btn" onClick={() => handleSaveOrEdit(job.id)}>
                  {userId === job.companyId ? "Sửa" :
                    !isSaving && interestedPosts.includes(job.id) ? "Đã lưu" :
                      isSaving && !interestedPosts.includes(job.id) ? "Đang lưu" :
                        isSaving && interestedPosts.includes(job.id) ? "Đang hủy lưu " :
                          "Lưu tin"}
                </button>
              </div>
            </div>
            {isPopupOpen && userId === job.companyId && (
              <ApplicationsPopup jobId={id} onClose={handleClosePopup} />
            )}
            {isPopupOpen && userId !== job.companyId && <ApplicationForm onClose={handleClosePopup} jobPostingId={id} />}
            <div className="job-details">
              <h3>Chi tiết tin tuyển dụng</h3>
              <p>{job.description && job.description.split('\n').map((line, index) => (
                <span key={index}>{line}<br /></span>
              ))}</p>


            </div>
          </div>
          {isWarningPopupOpen && (
            <div className="popup-overlay">
              <div className="ppopup-content">
                <h3>Thông báo</h3>
                <p>Bạn cần là Ứng viên mới có thể ứng tuyển công việc này. Vui lòng đăng nhập!</p>
                <div className="popup-buttons">
                  <button
                    className="popup-close"
                    onClick={handleWarningPopupClose}
                  >
                    Hủy
                  </button>
                  <button
                    className="popup-login"
                    onClick={() => navigate('/login', {
                      state: { returnUrl: `/JobDetail/${id}` },
                    })}
                  >
                    Đăng nhập
                  </button>
                </div>
              </div>
            </div>
          )}


          {/* Right Column */}
          <div className="right-column">
            <div className="company-info">
              <div className='right-wrap'>
                <div className="company-info-container">
                  <div className="company-logo">
                    <img src={DefaultImage} alt="Default Logo" />
                  </div>
                  <div className="company-name">
                    <p>{job.companyName}</p>
                  </div>
                </div>
                <div className="info-company-item">
                  <div className="icon-text">
                    <Globe />
                    Lĩnh vực
                  </div>
                  <div className="content">{job.companyField}</div>
                </div>
                <div className="info-company-item">
                  <div className="icon-text">
                    <MapPin />
                    Địa điểm
                  </div>
                  <div className="content">{job.address}</div>
                </div>

                <div className="info-company-item">
                  <div className="icon-text">
                    <Mail />
                    Email
                  </div>
                  <div className="content">{job.emails}</div>
                </div>
                <div className="info-company-item">
                  <div className="icon-text">
                    <Phone />
                    Số điện thoại
                  </div>
                  <div className="content">{job.phoneNumbers}</div>
                </div>
              </div>
            </div>




            <div className="general-info">
              <div className='right-wrap'>
                <h3>Thông tin chung</h3>
                <div className="info-item">
                  <div className="icon-container">
                    <BookText />
                  </div>
                  <div className="info-content">
                    <div className="info-title">Hình thức</div>
                    <div className="info-value">{job.schedule}</div>
                  </div>
                </div>
                <div className="info-item">
                  <div className="icon-container">
                    <Layers />
                  </div>
                  <div className="info-content">
                    <div className="info-title">Cấp bậc</div>
                    <div className="info-value">{job.level}</div>
                  </div>
                </div>
                <div className="info-item">
                  <div className="icon-container">
                    <ReceiptText />
                  </div>
                  <div className="info-content">
                    <div className="info-title">Số lượng tuyển</div>
                    <div className="info-value">{job.numberOfApplicants}</div>
                  </div>
                </div>
              </div>
            </div>


            <div className="specialty-tags">
              <div className='right-wrap'>
                <h3>Yêu cầu</h3>

                <div className="info-company-item">
                  <div className="icon-text">
                    <ClipboardType />
                    Loại hình
                  </div>
                  <div className="content">{job.jobType}</div>
                </div>

                <div className="info-company-item">
                  <div className="icon-text">
                    <Tag />
                    Kỹ năng
                  </div>
                  <div className="content">{job.skills}</div>
                </div>

                <div className="info-company-item">
                  <div className="icon-text">
                    <MapPin />
                    Khu vực
                  </div>
                  <div className="content">{job.ward}, {job.city}, {job.province}</div>
                </div>
              </div>
            </div>

            <div className="recommended-jobs">
              <div className='right-wrap'>
                <h3>Gợi ý việc làm phù hợp</h3>
              </div>
            </div>
          </div>
        </div>
      </div>
      {/* Edit Modal */}
      {isEditModalOpen && (
        <div className="detail-modal-overlay">
          <div className="detail-modal-content">
            <h3>Sửa thông tin công việc</h3>
            {isLoading ? (
              <div className="loading-spinner">
                <img src={SpinLoad} size={200} />

                <p className='loading-p'>Đang sửa thông tin công việc...</p>
              </div> // Hoặc thay thế bằng biểu tượng quay từ thư viện như lucide-react
            ) : (
              <>
                <label>
                  <strong>Tiêu đề:</strong>
                  <input
                    type="text"
                    name="title"
                    value={editedJob.title || ''}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  <strong>Mức lương tối thiểu:</strong>
                  <input
                    type="number"
                    name="minSalary"
                    value={editedJob.minSalary || ''}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  <strong>Mức lương tối đa:</strong>
                  <input
                    type="number"
                    name="maxSalary"
                    value={editedJob.maxSalary || ''}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  <strong>Trợ cấp:</strong>
                  <input
                    type="text"
                    name="allowance"
                    value={editedJob.allowance || ''}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  <strong>Số lượng tuyển:</strong>
                  <input
                    type="number"
                    name="numberOfApplicants"
                    value={editedJob.numberOfApplicants || ''}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  <strong>Mô tả công việc:</strong>
                  <textarea
                    name="description"
                    value={editedJob.description || ''}
                    onChange={handleChange}
                    rows="12"
                  />
                </label>
              </>
            )}
            <div className="detail-modal-buttons">
              <button className="detail-close-btn" disabled={isLoading} onClick={handleModalClose}>Hủy</button>
              <button className="detail-save-btn" disabled={isLoading} onClick={handleSubmit}>Lưu</button>
            </div>
          </div>
        </div>
      )}
      <Footer />
    </div>
  );
}

export default JobDetail;
