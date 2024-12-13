import React, { useState, useEffect } from 'react';
import '../css/ListCompany.css';
import loadinggif from '../images/Evitare loader.gif';
import logo from '../images/logo2.png'
import { useNavigate } from 'react-router-dom';


function ListCompany() {
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const itemsPerPage = 9; // Mỗi trang hiển thị 9 công ty
  const [currentPage, setCurrentPage] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    // Gọi API
    const fetchCompanies = async () => {
      try {
        const response = await fetch('http://47.128.243.193:8080/public/companies');
        const data = await response.json();
        // Lọc công ty có `recruitQuantity` > 20
        const filteredCompanies = data.filter((company) => company.recruitQuantity > 10);
        setCompanies(filteredCompanies);
      } catch (error) {
        console.error('Error fetching companies:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchCompanies();
  }, []);

  const totalPages = Math.ceil(companies.length / itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const currentItems = companies.slice(startIndex, startIndex + itemsPerPage);

  const handleNextPage = () => setCurrentPage((prevPage) => (prevPage + 1) % totalPages);
  const handlePreviousPage = () => setCurrentPage((prevPage) => (prevPage - 1 + totalPages) % totalPages);
  const handlePageClick = (pageIndex) => setCurrentPage(pageIndex);

  const handleAllCompany = () => {
    navigate(`/allcompany`);
  }
  // if (loading) {
  //   return (
  //     <div className="loading-container">
  //       <img src={loadinggif} alt="Loading..." />
  //     </div>
  //   );
  // }

  const handleCompany = (id) => {
    navigate(`/allcompany/${id}`);
  }

  return (
    <div>
      <div id="list-company-container">
        {/* Dòng đầu tiên */}
        <div className="header">
          <h2>Các Công Ty Lớn</h2>
          <button className="view-all-btn" onClick={handleAllCompany}>
            Xem tất cả
          </button>
        </div>

        {/* Flexbox chứa các công ty */}
        <div className="company-flexbox-container">
          {loading ? ( // Hiển thị khi đang tải
            <div className="loading-container">
              <img src={loadinggif} alt="Loading..." className="loading-company" />
            </div>
          ) : currentItems.length > 0 ? ( // Hiển thị danh sách công việc
            <>
              <button className="carousel-control left" onClick={handlePreviousPage}>
                &lt;
              </button>

              <div className="company-list">
                {currentItems.map((company) => (
                  <div
                    className="company-card"
                    key={company.id}
                    onClick={() => handleCompany(company.id)} // Gắn sự kiện onClick cho từng công ty
                    style={{ cursor: 'pointer' }} // Thêm con trỏ chuột chỉ vào công ty
                  >
                    {/* Phần trên: logo (hoặc hình đại diện) + thông tin */}
                    <div className="card-top">
                      <div className="company-logo">
                        <img src={logo} alt={company.name} />
                      </div>
                      <div className="company-info">
                        <h3 title={company.name}>
                          {company.name.length > 50 ? `${company.name.substring(0, 50)}...` : company.name}
                        </h3>
                        <p>{company.fields.join(', ')}</p>
                      </div>
                    </div>

                    {/* Phần dưới: số lượng tuyển dụng */}
                    <div className="card-bottom">
                      <p>Số lượng tuyển: {company.recruitQuantity}</p>
                    </div>
                  </div>
                ))}
              </div>

              <button className="carousel-control right" onClick={handleNextPage}>
                &gt;
              </button>
            </>
          ) : (
            <p>Không có công ty nào...</p>
          )}


        </div>

        {/* Dấu chấm biểu thị các trang */}
        <div className="pagination-dots">
          {Array.from({ length: totalPages }).map((_, pageIndex) => (
            <span
              key={pageIndex}
              className={`dot ${currentPage === pageIndex ? 'active' : ''}`}
              onClick={() => handlePageClick(pageIndex)}
            ></span>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ListCompany;
