import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Heart,
  MapPin,
  Calendar,
  DollarSign,
  Globe,
  Layers, //cap bac
  Mail,   
  Phone,
  BookText, //hình thuc
  ReceiptText , //so luong tuyen
  ClipboardType,
  Tag,

} from "lucide-react";
import Navbar from './navbar';
import Footer from './Footer';
import ApplicationForm from "./ApplicationForm";
import Loading from "./Loading";
import ErrorBoundary from 'antd/es/alert/ErrorBoundary';
import '../css/JobDetail.css'; // File CSS để style
import DefaultImage from '../images/logo3.png'

function JobDetail() {
    const { id } = useParams(); // Lấy `id` từ URL
    const navigate = useNavigate(); // Điều hướng trở lại danh sách công việc
    const [job, setJob] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isPopupOpen, setIsPopupOpen] = useState(false);

    const handleApplyClick = () => {
      setIsPopupOpen(true);
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
              const response = await fetch(`/jobpostings/${id}`);
              if (!response.ok) {
                  throw new Error(`Failed to fetch job details: ${response.status}`);
              }
              const data = await response.json();
              setJob(data);
          } catch (error) {
              console.error('Error fetching job details:', error);
          } finally {
            setLoading(false); // ket thuc tai du lieu
          }
      };
  
      fetchJobDetails();
  }, [id]);

    if (loading) {
        return <Loading/>;
    }

    if (!job) {
        return <ErrorBoundary/>;
    }

    return (
         <div>
        <Navbar/>
        <div id="job-detail-container">
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
                      <div className="top">Cấp bậc</div>
                      <div className="bottom">{job.level}</div>
                    </div>
                  </div>
                </div>
                <div className="button">
                  <button className="apply-btn" onClick={handleApplyClick}>Ứng tuyển ngay</button>
                  <button className="save-btn">Lưu tin</button>
                </div>
              </div>
              {isPopupOpen && <ApplicationForm onClose={handleClosePopup} />}
              <div className="job-details">
                <h3>Chi tiết tin tuyển dụng</h3>
                <p>{job.description && job.description.split('\n').map((line, index) => (
                    <span key={index}>{line}<br /></span>
                ))}</p>


              </div>
            </div>

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
        <Footer/>
      </div>
    );
}

export default JobDetail;
