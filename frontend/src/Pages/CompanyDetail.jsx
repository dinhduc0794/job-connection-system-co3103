import { useState, useEffect } from 'react';
import { useParams, NavLink } from 'react-router-dom';
import { MapPin, Phone, Mail, Building2, Award, Users, Star } from 'lucide-react';
import Footer from '../Jsx/Footer';
import Navbar from '../Jsx/navbar';
import TokenManager from '../utils/tokenManager';
import AppNavbar from '../Jsx/AppNavbar';
import CompanyNavbar from '../Jsx/CompanyNavbar';
const token = TokenManager.getToken();
const CompanyDetail = () => {
  const { id } = useParams();
  const [company, setCompany] = useState(null);
  const [loading, setLoading] = useState(true);
  const [userId, setUserId] = useState(null);
  const [role, setRole] = useState(null);
  const [userRating, setUserRating] = useState(0);
const [showRatingModal, setShowRatingModal] = useState(false);
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
    const fetchCompanyDetail = async () => {
      try {
        const headers = {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Origin': '*'
        };
  
        
        if (token?.value) {
          headers['Authorization'] = `Bearer ${token.value}`;
        }
  
        const response = await fetch(`/public/companies/${id}`, {
          method: 'GET',
          headers,
          credentials: 'include'
        });
  
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
  
        const data = await response.json();
        setCompany(data);
      } catch (error) {
        console.error('Error fetching company details:', error);
        setCompany(null);
      } finally {
        setLoading(false);
      }
    };
  
    fetchCompanyDetail();
  }, [id]);
  const handleRateCompany = async (rating) => {
    try {
      const response = await fetch('/rate-company', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token.value}`
        },
        body: JSON.stringify({
          companyId: id,
          rating: rating
        })
      });
  
      if (response.ok) {
        // Cập nhật lại rating hiển thị
        setCompany(prev => ({
          ...prev,
          rating: rating
        }));
        setShowRatingModal(false);
      }
    } catch (error) {
      console.error('Error rating company:', error);
    }
  };
  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (!company) {
    return (
      <div className="flex items-center justify-center min-h-screen text-2xl font-bold text-red-500">
        Company not found
      </div>
    );
  }

  return (
    <>
      {renderNavbar()}
      <div className="w-full max-w-[2300px] mx-auto px-4 py-8">
        <div className="bg-white rounded-xl shadow-2xl overflow-hidden">
          {/* Hero Section */}
          <div className="relative h-80 bg-gradient-to-r from-blue-600 to-purple-600"> {/* Tăng height */}
            <img
              src={company.image}
              alt={company.name}
              className="w-full h-full object-cover mix-blend-overlay"
            />
            <div className="absolute bottom-0 left-0 right-0 p-8 bg-gradient-to-t from-black/90 via-black/60 to-transparent">
              <h1 className="text-4xl font-bold text-white mb-4">
                {company.name}
              </h1>
              <div className="flex items-center gap-3 mt-3">
                <Award className="w-6 h-6 text-yellow-400" />
                <span className="text-lg text-white font-medium">{company.fields}</span>
              </div>
            </div>
          </div>
  
          {/* Main Content */}
          <div className="p-8">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
              {/* Left Column */}
              <div className="space-y-10">
                {/* About Section */}
                <div className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-all duration-300">
                  <h2 className="text-3xl font-bold mb-6 text-gray-800 border-b-2 border-blue-500 pb-3 flex items-center gap-3">
                    <Building2 className="w-8 h-8 text-blue-500" />
                    Giới thiệu công ty
                  </h2>
                  <p className="text-xl text-gray-700 leading-relaxed">{company.description}</p>
                </div>
  
                {/* Stats Cards */}
                <div className="grid grid-cols-2 gap-8">
                
  
                  <div className="bg-gradient-to-br from-purple-50 to-pink-50 p-8 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300">
                    <div className="flex items-center gap-3 mb-4">
                      <Users className="w-8 h-8 text-purple-500" />
                      <h3 className="text-2xl font-semibold text-gray-700">Đang tuyển</h3>
                    </div>
                    <p className="text-4xl font-bold text-purple-600">{company.recruitQuantity}<span className="text-xl text-gray-600"> vị trí</span></p>
                  </div>
                </div>
              </div>
  
              {/* Right Column - Contact Information */}
              <div className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-all duration-300">
                <h2 className="text-3xl font-bold mb-8 text-gray-800 border-b-2 border-purple-500 pb-3 flex items-center gap-3">
                  <Mail className="w-8 h-8 text-purple-500" />
                  Thông tin liên hệ
                </h2>
  
                <div className="space-y-8">
                  {/* Address Section */}
                  <div className="space-y-4">
                    <h3 className="text-2xl font-semibold text-gray-700 flex items-center gap-2">
                      <MapPin className="w-6 h-6 text-blue-500" />
                      Địa chỉ văn phòng
                    </h3>
                    {company.addresses.map((address, index) => (
                      <div key={index} className="ml-8 p-4 bg-blue-50 rounded-lg hover:bg-blue-100 transition-all duration-300">
                        <p className="text-xl text-gray-700">{address}</p>
                      </div>
                    ))}
                  </div>
  
                  {/* Phone Numbers Section */}
                  <div className="space-y-4">
                    <h3 className="text-2xl font-semibold text-gray-700 flex items-center gap-2">
                      <Phone className="w-6 h-6 text-green-500" />
                      Số điện thoại liên hệ
                    </h3>
                    {company.phoneNumbers.map((phone, index) => (
                      <div key={index} className="ml-8 p-4 bg-green-50 rounded-lg hover:bg-green-100 transition-all duration-300">
                        <a href={`tel:${phone}`} className="text-xl text-gray-700 hover:text-green-600 flex items-center gap-2">
                          {phone}
                          <span className="text-sm text-green-600">(Bấm để gọi)</span>
                        </a>
                      </div>
                    ))}
                  </div>
  
                  {/* Email Section */}
                  <div className="space-y-4">
                    <h3 className="text-2xl font-semibold text-gray-700 flex items-center gap-2">
                      <Mail className="w-6 h-6 text-red-500" />
                      Email liên hệ
                    </h3>
                    {company.emails.map((email, index) => (
                      <div key={index} className="ml-8 p-4 bg-red-50 rounded-lg hover:bg-red-100 transition-all duration-300">
                        <a href={`mailto:${email}`} className="text-xl text-gray-700 hover:text-red-600 flex items-center gap-2">
                          {email}
                          <span className="text-sm text-red-600">(Bấm để gửi mail)</span>
                        </a>
                      </div>
                    ))}
                  </div>
  
                  {/* Tax Code Section */}
                  <div className="space-y-4">
                    <h3 className="text-2xl font-semibold text-gray-700 flex items-center gap-2">
                      <Building2 className="w-6 h-6 text-orange-500" />
                      Mã số thuế
                    </h3>
                    <div className="ml-8 p-4 bg-orange-50 rounded-lg">
                      <p className="text-xl text-gray-700">{company.taxCode}</p>
                    </div>
                  </div>
                  {/* Job Openings Section - Thêm vào sau phần main content */}
                  <div className="w-full bg-gray-50 mt-18">
                  <div className="max-w-[2300px] mx-auto px-8 py-12"> {/* Tăng padding-x từ 4 lên 8 */}
    <h2 className="text-4xl font-bold mb-12 text-gray-800 border-b-2 border-green-500 pb-4 flex items-center gap-3">
      <Users className="w-10 h-10 text-green-500" />
      Vị trí đang tuyển dụng ({company.recruitQuantity} vị trí)
    </h2>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      {company.jobPostings && company.jobPostings.map((job) => (
        <div key={job.id} className="bg-white rounded-2xl p-8 shadow-lg hover:shadow-2xl transition-all duration-300">
          <div className="flex justify-between items-start mb-6">
            <h3 className="text-2xl font-bold text-gray-800 flex-grow">{job.jobType}</h3>
            <span className="px-4 py-2 bg-green-100 text-green-700 rounded-full text-lg font-medium ml-4">
              {job.level}
            </span>
          </div>

          <div className="space-y-6 mt-6">
            <div className="flex items-center gap-4 text-gray-700">
              <MapPin className="w-7 h-7 text-blue-500 flex-shrink-0" />
              <span className="text-xl">{job.ward}, {job.city}, {job.province}</span>
            </div>

            <div className="flex items-center gap-4 text-gray-700">
              <Users className="w-7 h-7 text-purple-500 flex-shrink-0" />
              <span className="text-xl">{job.schedule}</span>
            </div>

            <div className="flex items-center gap-4 text-gray-700">
              <Award className="w-7 h-7 text-yellow-500 flex-shrink-0" />
              <span className="text-xl">${job.minSalary}k - ${job.maxSalary}k</span>
            </div>
          </div>

          <div className="mt-8 pt-6 border-t border-gray-100">
            <NavLink to={`/JobDetail/${job.id}`} className="block">
              <button className="w-full px-8 py-4 bg-gradient-to-r from-blue-500 to-purple-500 text-white rounded-xl 
                text-xl font-semibold transform transition-all duration-300 
                hover:scale-[1.02] hover:shadow-xl hover:from-blue-600 hover:to-purple-600 
                flex items-center justify-center gap-3">
                Xem chi tiết
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5l7 7-7 7" />
                </svg>
              </button>
            </NavLink>
          </div>
        </div>
      ))}
    </div>

    {(!company.jobPostings || company.jobPostings.length === 0) && (
      <div className="text-center py-16 bg-white rounded-2xl shadow-lg">
        <Users className="w-16 h-16 text-gray-400 mx-auto mb-4" />
        <p className="text-3xl text-gray-500 font-medium">
          Hiện tại chưa có vị trí nào đang tuyển dụng
        </p>
      </div>
    )}
  </div>
</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="min-h-screen flex flex-col">
        <div className="flex-grow"></div>
        <Footer />
      </div>
    </>
  );
};
export default CompanyDetail;
