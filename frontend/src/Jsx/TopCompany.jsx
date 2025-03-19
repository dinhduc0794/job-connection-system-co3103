import React, { useState, useEffect } from 'react';
import '../css/TopCompany.css';



function TopCompany() {
    const newsList = [
        { title: 'Vitex hợp tác FUNiX tìm kiếm nguồn cung ứng nhân lực IT chất lượng', 
            description: 'Vitex Vietnam Software kỳ vọng tìm được nguồn cung ứng nhân lực IT chất lượng qua chương trình hợp tác đồng thời đóng góp hiệu quả vào đào tạo CNTT tại Việt Nam', 
            url: 'https://vnexpress.net/vitex-hop-tac-funix-tim-kiem-nguon-cung-ung-nhan-luc-it-chat-luong-4816280.html',
            imageUrl: 'https://i1-vnexpress.vnecdn.net/2024/11/15/2-1263-1731644383.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=H8ClFOkADFdR9Ak9bt3ARQ' },
        { title: 'Trò yêu công nghệ giúp mentor được tiếp thêm lửa nghề', 
            description: 'Trở thành mentor giảng dạy online Khoa học máy tính với Python tại FUNiX từ năm 2023, chị Phan Vũ Hà Thiên (27 tuổi, Quảng Trị) có nhiều kỷ niệm khi đồng hành cùng học viên nhỏ tuổi.', 
            url: 'https://vnexpress.net/tro-yeu-cong-nghe-giup-mentor-duoc-tiep-them-lua-nghe-4813784.html',
            imageUrl: 'https://i1-vnexpress.vnecdn.net/2024/11/15/Image-323810943-ExtractWord-1-9076-5600-1731636375.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=uhbA8D98zSWhDk83L0i3NA' },
        { title: 'Cậu bé 13 tuổi thay đổi tích cực sau ba năm học IT trực tuyến', 
            description: 'Cao Gia Bảo, 13 tuổi, Hà Nội theo học IT trực tuyến để làm quen với máy tính, lập trình. Hiện cậu có nhiều thay đổi tích cực sau ba năm gắn bó cùng FUNiX.', 
            url: 'https://vnexpress.net/cau-be-13-tuoi-thay-doi-tich-cuc-sau-ba-nam-hoc-it-truc-tuyen-4815705.html' ,
            imageUrl: 'https://i1-vnexpress.vnecdn.net/2024/11/14/Image-ExtractWord-0-Out-9755-1731554458.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=dpxyZLaVP9cqHIRI4zNHIg'},
        { title: 'Nam sinh 15 tuổi học FUNiX Wings theo đuổi ngành công nghệ thông tin', 
            description: 'Nguyễn Hoàng Minh (15 tuổi, Thanh Hóa) được bố mẹ ủng hộ theo học khóa FUNiX Wings để theo đuổi ngành công nghệ thông tin khi vào đại học.', 
            url: 'https://vnexpress.net/nam-sinh-15-tuoi-hoc-funix-wings-theo-duoi-nganh-cong-nghe-thong-tin-4812903.html',
            imageUrl: 'https://i1-vnexpress.vnecdn.net/2024/11/07/Image-986679571-ExtractWord-0-6858-1897-1730948621.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=Wn7JHzLqXKfQ4vPsR3d3Rg' },
        { title: 'Công ty Elite là nhà phân phối của VMware by Broadcom', 
            description: 'Hợp tác đánh dấu việc đưa các giải pháp thế giới của VMware by Broadcom đến gần hơn với doanh nghiệp Việt Nam.', 
            url: 'https://vnexpress.net/cong-ty-elite-la-nha-phan-phoi-cua-vmware-by-broadcom-4808365.html',
            imageUrl: 'https://i1-sohoa.vnecdn.net/2024/10/25/edit-JPG-9894-1729844398.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=Clz5SI1KXcBaIBsVpJMC2Q' },
      ];
    const [currentNewsIndex, setCurrentNewsIndex] = useState(0);
    // Tự động chuyển trang
    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentNewsIndex((prevIndex) => (prevIndex + 1) % newsList.length);
        }, 3000); // 3 giây
        return () => clearInterval(interval); // Xóa interval khi component unmount
    }, [newsList.length]);

    const handleDotClick = (index) => {
        setCurrentNewsIndex(index);
    };

    const handleNewsClick = (url) => {
        window.location.href = url;
    };

    const companies = [
        { name: 'FSI', logo: 'https://fsivietnam.com.vn/wp-content/uploads/2020/08/logo.svg', url: '/company-a' },
        { name: 'FOXCONN INDUSTRIAL INTERNET', logo: 'https://www.vietnamworks.com/_next/image?url=https%3A%2F%2Fimages.vietnamworks.com%2Flogo%2Ffuyu_vip_128291.png&w=128&q=70', url: '/company-b' },
        { name: 'V-GREEN', logo: 'https://www.vietnamworks.com/_next/image?url=https%3A%2F%2Fimages.vietnamworks.com%2Flogo%2Fvgreen_vip_129144.jpg&w=128&q=70', url: '/company-c' },
        { name: 'LG', logo: 'https://www.vietnamworks.com/_next/image?url=https%3A%2F%2Fimages.vietnamworks.com%2Flogo%2Flghn_vip1_129127.png&w=128&q=70', url: '/company-d' },
        { name: 'MUFG BANK', logo: 'https://www.vietnamworks.com/_next/image?url=https%3A%2F%2Fimages.vietnamworks.com%2Flogo%2FmufgN_vip_129059.jpg&w=128&q=70', url: '/company-e' }
      ];
      const handleCompanyClick = (url) => {
        // Điều hướng đến trang giới thiệu công ty
        window.location.href = url;
      };
    return (
        <div className='container-wrapper'>
            <div id="hometopcompany-container">
            <div className="news-carousel-container">
            <div
                className="news-card"
                style={{
                    backgroundImage: `url(${newsList[currentNewsIndex].imageUrl})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    backgroundRepeat: 'no-repeat',
                }}
                onClick={() => handleNewsClick(newsList[currentNewsIndex].url)}
            >
                <div className="background-overlay"></div>
                <h2>{newsList[currentNewsIndex].title}</h2>
                <p>{newsList[currentNewsIndex].description}</p>
            </div>
            {/* Pagination dots */}
            <div className="pagination-dots">
                {newsList.map((_, index) => (
                    <div
                        key={index}
                        className={`dot ${currentNewsIndex === index ? 'active' : ''}`}
                        onClick={() => handleDotClick(index)}
                    ></div>
                ))}
            </div>
        </div>


                
                <div className='top-company-home'>
                    <h2 className="section-title">Các Công Ty Hàng Đầu</h2>
                    <div className="topcompany-list">
                        {companies.map((company, index) => (
                        <div
                            key={index}
                            className="topcompany-card"
                            onClick={() => handleCompanyClick(company.url)}
                        >
                            <div className="logo-container">
                                <img src={company.logo} alt={company.name} className="topcompany-logo" />
                            </div>
                            <div className='name-container'>
                            <p className="topcompany-name" title={company.name}>
                                {company.name.length > 30 ? company.name.slice(0, 30) + '...' : company.name}
                            </p>
                            </div>
                        </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default TopCompany;