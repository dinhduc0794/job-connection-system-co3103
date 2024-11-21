import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { MapPin, Briefcase, Calendar, DollarSign, Phone, Mail } from "lucide-react";
import DefaultImage from '../images/logo1.png'; // Thay thế bằng hình ảnh mặc định của bạn
import Navbar from './navbar';
import Footer from './Footer';
import ApplicationForm from "./ApplicationForm";
import '../css/JobDetail.css'; // File CSS để style

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
        return <div>Đang tải thông tin công việc...</div>;
    }

    if (!job) {
        return <div>Không tìm thấy công việc!</div>;
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
                  <div className="info-item">Mức lương: {job.minSalary} - {job.maxSalary} triệu</div>
                  <div className="info-item">Địa điểm: {job.province}</div>
                  <div className="info-item">Kinh nghiệm: {job.level}</div>
                </div>
                <div className="button">
                  <button className="apply-btn" onClick={handleApplyClick}>Ứng tuyển ngay</button>
                  <button className="save-btn">Lưu tin</button>
                </div>
              </div>
              {isPopupOpen && <ApplicationForm onClose={handleClosePopup} />}
              <div className="job-details">
                <h3>Chi tiết tin tuyển dụng</h3>
                <div className="job-description">
                  <h4>Mô tả công việc</h4>
                  <p>- Tiếp cận khách hàng để nắm bắt yêu cầu từ khách hàng từ giai đoạn đầu chưa hợp đồng. Từ đó phân tích, đánh giá nhu cầu/mong muốn và tư vấn xây dựng giải pháp thực hiện tới khách hàng.</p>
                  <p>- Chủ trì tổ chức triển khai dự án.</p>
                  <p>- Kiểm soát đảm bảo tiến độ và chất lượng thực hiện dự án theo kế hoạch</p>
                  <p>- Kiểm soát thay đổi yêu cầu</p>
                  <p>- Tổ chức thực hiện công tác nghiệm thu, triển khai đưa sản phẩm vào sử dụng.</p>
                  <p>- Lập báo cáo về tình hình thực dự án theo quy định của công ty</p>
                  <p>- Thực hiện nhiệm vụ theo yêu cầu của trưởng bộ phận</p>
                </div>
                <div className="job-requirements">
                  <h4>Yêu cầu ứng viên</h4>
                  <p>- Tốt nghiệp đại học trở lên về CNTT, điện tử viễn thông, hệ thống thông tin và các ngành có liên quan</p>
                  <p>- Có kiến thức tốt về CNTT và hiểu được thiết kế kỹ thuật phân tích, thiết kế hệ thống CNTT</p>
                  <p>- Kinh nghiệm từ 2 năm trở lên ở các vị trí tương đương</p>
                  <p> - Có kỹ năng làm việc độc lập và đội nhóm tốt</p>
                  <p>- Tinh thần hợp tác và làm việc tốt</p>
                  <p>- Có khả năng chịu áp lực công việc tốt</p>
                  <p>- Có Tinh thần học hỏi và cầu tiến trong công việc</p>
                  <p>- Ứng viên có chứng chỉ quản lý dự án là lợi thế</p>
                  <p>- Kỹ năng giao tiếp tốt, làm việc với khách hàng hiệu quả</p>
                  <p>- Có khả năng thuyết trình, không nói ngọng khó nghe.</p>
                  <p>- Khả năng phân tích, tư duy logic tốt.</p>
                  <p>- Có kiến thức và kinh nghiệm phát triển phần mềm theo mô hình waterfall, Agile</p>
                  <p>- Sử dụng thành thạo các công cụ hỗ trợ phân tích như: UML, Microsoft Visio, ...</p>
                  <p>- Kỹ năng trình bày, giải quyết vấn đề và làm việc nhóm.</p>
                  <p>- Kỹ năng xử lý tình huống linh hoạt, khả năng làm việc độc lập, chủ động</p>
                </div>
                <div className="job-benefits">
                  <h4>Quyền lợi</h4>
                  <p>- Lương: Thỏa thuận theo năng lực</p>
                  <p>- Thưởng tháng 13 + Thưởng thi đua quý/năm + Thưởng KPI (2-3 tháng)</p>
                  <p>- Môi trường làm việc năng động, thân thiện, chuyên nghiệp với nhiều cơ hội phát triển</p>
                  <p>- Được tham gia các khóa đào tạo nội bộ và bên ngoài để nâng cao kiến thức, kỹ năng mềm và trình độ chuyên môn.</p>
                  <p>- Phúc lợi: Teambuilding, nghỉ mát, sinh nhật, thưởng lễ/tết,...</p>
                  <p>- Các chế độ bảo hiểm, nghỉ phép,... theo Luật lao động</p>
                  <p>- Thời gian làm việc: 8h00 - 12h00, 13h00 - 17h00</p>

                </div>
                <div className="job-location">
                  <h4>Địa điểm làm việc</h4>
                  <p>- {job.province}: {job.address}</p>
                </div>
                <div className="job-time">
                  <h4>Thời gian làm việc</h4>
                  <p>- Thứ 2 - Thứ 6 (từ 08:00 đến 17:00)</p>
                </div>
                <div className="job-way">
                  <h4>Cách thức ứng tuyển</h4>
                  <p>Ứng viên nộp hồ sơ trực tuyến bằng cách bấm Ứng tuyển ngay dưới đây.</p>
                </div>
                <p>Hạn nộp hồ sơ: 30/11/2024</p>


              </div>
            </div>

            {/* Right Column */}
            <div className="right-column">
              <div className="company-info">
                <h3>Thông tin công ty</h3>
                <p>{job.companyName}</p>
                {/* <p>Quy mô: 100-499 nhân viên</p> */}
                <p>Lĩnh vực: {job.companyField}</p>
                <p>Địa điểm: {job. address}</p>
                <p>Email: {job.emails}</p>
                <p>Số điện thoại: {job.phoneNumbers}</p>
              </div>

              <div className="general-info">
                <h3>Thông tin chung</h3>
                <p>Hình thức: {job.schedule}</p>
                <p>Level: {job.level}</p>
                <p>Số lượng tuyển: {job.numberOfApplicants}</p>
              </div>

              <div className="specialty-tags">
                <h3>Yêu cầu</h3>
                <p className="tag">Loại hình: {job.jobType} </p>
                <p className='tag'>Kỹ năng: {job.skills}</p>
                <p className="tag">Khu vực: {job.ward}, {job.city}, {job.province}</p>
              </div>

              <div className="recommended-jobs">
                <h3>Gợi ý việc làm phù hợp</h3>
              </div>
            </div>
          </div>
        </div>
        <Footer/>
      </div>
    );
}

export default JobDetail;
