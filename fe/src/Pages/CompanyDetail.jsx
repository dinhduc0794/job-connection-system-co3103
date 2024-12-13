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
  const [companyRatings, setCompanyRatings] = useState([]);
  const [showRatingsModal, setShowRatingsModal] = useState(false);
  const [ratingData, setRatingData] = useState({
    ratingId: null,
    feedback: ''
  });

  useEffect(() => {
    if (token) {
      setRole(token.role?.toLowerCase());
      setUserId(token.id);
    }
  }, [token]);
  const fetchCompanyRatings = async () => {
    try {
      const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token.value}`
      };

      const response = await fetch(`http://47.128.243.193:8080/companies/${id}/rates`, {
        method: 'GET',
        headers,
      });

      if (response.ok) {
        const data = await response.json();
        setCompanyRatings(data.data || []);
      } else {
        console.error('Failed to fetch company ratings');
      }
    } catch (error) {
      console.error('Error fetching company ratings:', error);
    }
  };
  const renderNavbar = () => {
    if (role === 'applicant') {
      return <AppNavbar />;
    } else if (role === 'company') {
      return <CompanyNavbar />;
    } else {
      return <Navbar />;
    }
  };
  const renderCompanyRatingsModal = () => {
    if (!showRatingsModal || role !== 'company') return null;

    return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div className="bg-white rounded-xl p-8 max-w-4xl w-full max-h-[90vh] overflow-y-auto">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Đánh giá của ứng viên</h2>
            <button
              onClick={() => setShowRatingsModal(false)}
              className="text-gray-600 hover:text-gray-900"
            >
              <svg
                className="w-6 h-6"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>

          {companyRatings.length === 0 ? (
            <div className="text-center py-12">
              <Star className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-xl text-gray-500">Chưa có đánh giá nào</p>
            </div>
          ) : (
            <div className="space-y-6">
              {companyRatings.map((rating) => (
                <div
                  key={rating.id}
                  className="bg-gray-50 p-6 rounded-xl shadow-md hover:shadow-lg transition-all duration-300"
                >
                  <div className="flex justify-between items-center mb-4">
                    <div className="flex">
                      {[1, 2, 3, 4, 5].map((star) => (
                        <Star
                          key={star}
                          className={`w-6 h-6 ${star <= rating.rate
                              ? 'text-yellow-500 fill-current'
                              : 'text-gray-300'
                            }`}
                        />
                      ))}
                    </div>
                    <span className="text-sm text-gray-500">
                      Applicant ID: {rating.applicantId}
                    </span>
                  </div>

                  {rating.feedback && (
                    <p className="text-gray-700 italic">"{rating.feedback}"</p>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    );
  };

  // Modify the existing rendering logic to add ratings button for companies
  const renderRatingSection = () => {
    if (role === 'company') {
      return (
        <div className="bg-gradient-to-br from-yellow-50 to-amber-50 p-8 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300">
          <div className="flex items-center gap-3 mb-4">
            <Star className="w-8 h-8 text-yellow-500" />
            <h3 className="text-xl font-semibold text-gray-700">Đánh giá từ ứng viên</h3>


          </div>
          <div className="flex flex-col items-center space-y-4">
            <div className="flex items-center">
              <div className="flex">
                {[1, 2, 3, 4, 5].map((star) => (
                  <Star
                    key={star}
                    className={`w-8 h-8 ${star <= Math.round(company.rating)
                        ? 'text-yellow-500 fill-current'
                        : 'text-gray-300'
                      }`}
                  />
                ))}
              </div>
              <span className="ml-2 text-xl text-gray-700 font-bold">
                {company.rating ? company.rating.toFixed(1) : 'Chưa có'}
              </span>
            </div>

            <button
              className="w-full px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-colors"
              onClick={() => {
                fetchCompanyRatings();
                setShowRatingsModal(true);
              }}
            >
              Xem đánh giá
            </button>
          </div>
        </div>
      );
    }

    // Existing applicant rating section remains unchanged
    return role === 'applicant' ? (
      <div className="bg-gradient-to-br from-yellow-50 to-amber-50 p-8 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300">
        {/* Existing applicant rating section */}
      </div>
    ) : null;
  };
  const renderRatingModal = () => {
    if (!showRatingModal || role !== 'applicant') return null;

    return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div className="bg-white rounded-xl p-8 max-w-md w-full">
          <h2 className="text-2xl font-bold mb-4 text-center">Đánh giá công ty</h2>

          <div className="flex justify-center mb-6">
            {[1, 2, 3, 4, 5].map((star) => (
              <Star
                key={star}
                className={`w-10 h-10 cursor-pointer ${star <= userRating
                    ? 'text-yellow-500 fill-current'
                    : 'text-gray-300'
                  }`}
                onClick={() => setUserRating(star)}
              />
            ))}
          </div>

          <textarea
            className="w-full p-2 border rounded-lg mb-4"
            placeholder="Nhận xét về công ty (tuỳ chọn)"
            value={ratingData.feedback}
            onChange={(e) => setRatingData(prev => ({
              ...prev,
              feedback: e.target.value
            }))}
          />

          <div className="flex justify-between">
            <button
              className="px-4 py-2 bg-gray-200 rounded-lg"
              onClick={() => setShowRatingModal(false)}
            >
              Hủy
            </button>
            <button
              className="px-4 py-2 bg-blue-500 text-white rounded-lg"
              onClick={() => handleRateCompany(userRating)}
            >
              Gửi đánh giá
            </button>
          </div>
        </div>
      </div>
    );
  };
  const [isLoading, setIsLoading] = useState(false);
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
        setCompany({
          ...data,
          fields: data.fields || [], // Add fields array
          numberOfFollowers: data.numberOfFollowers || 0, // Add numberOfFollowers
          jobPostings: data.jobPostings || [],
          addresses: data.addresses || [],
          phoneNumbers: data.phoneNumbers || [],
          emails: data.emails || [],
          recruitQuantity: data.recruitQuantity || 0
        });
      } catch (error) {
        console.error('Error fetching company details:', error);
        setCompany(null);
      } finally {
        setLoading(false);
      }
    };

    fetchCompanyDetail();
  }, [id]);
  const [notification, setNotification] = useState({
    show: false,
    message: '',
    type: ''
  });
  const handleRateCompany = async (rating) => {
    // Only allow rating if user is an applicant
    if (role !== 'applicant') {
      setNotification({
        show: true,
        message: 'Chỉ applicant mới có thể đánh giá công ty',
        type: 'error'
      });
      return;
    }

    try {
      const response = await fetch('http://47.128.243.193:8080/applicants/rate-company', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token.value}`
        },
        body: JSON.stringify({
          applicantId: userId,
          companyId: id,
          rate: rating,
          feedback: ratingData.feedback || '' // Optional feedback
        })
      });

      if (response.ok) {
        const responseData = await response.json();

        // Update company rating and store rating ID if returned
        setCompany(prev => ({
          ...prev,
          rating: rating
        }));

        // Store the rating ID if returned
        setRatingData(prev => ({
          ...prev,
          ratingId: responseData.ratingId || null
        }));

        setShowRatingModal(false);
        setShowRatingModal(false);
        setNotification({
          show: true,
          message: 'Đánh giá công ty thành công!',
          type: 'success'
        });
        setTimeout(() => {
          setNotification({ show: false, message: '', type: '' });
        }, 6000);
      } else {
        // Xử lý lỗi từ server
        const errorData = await response.json();
        setNotification({
          show: true,
          message: errorData.message || 'Có lỗi xảy ra khi đánh giá công ty',
          type: 'error'
        });
      }
    } catch (error) {
      console.error('Error rating company:', error);
      setNotification({
        show: true,
        message: 'Đã có lỗi xảy ra. Vui lòng thử lại.',
        type: 'error'
      });
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
  const renderNotification = () => {
    if (!notification.show) return null;

    const bgColor = notification.type === 'success'
      ? 'bg-green-500'
      : 'bg-red-500';

    return (
      <div className={`fixed top-4 right-4 ${bgColor} text-white px-6 py-4 rounded-lg shadow-lg z-50 transition-all duration-300`}>
        {notification.message}
      </div>
    );
  };
  return (
    <>
      {renderNavbar()}
      {renderNotification()}
      {renderCompanyRatingsModal()}
      <div className="w-full max-w-[1200px] mx-auto px-3 py-6 bg-gradient-to-b from-blue-50 via-white to-blue-50 mt-20">
        <div className="bg-white rounded-xl shadow-2xl overflow-hidden">
          {/* Hero Section */}
          <div className="relative h-80 bg-gradient-to-b from-blue-300 via-cyan-200 to-blue-500">
            <img
              src={company.image}
              className="w-full h-full object-cover mix-blend-overlay"
            />
            <div className="absolute bottom-0 left-0 right-0 p-8 bg-gradient-to-t from-black/90 via-black/60 to-transparent">
              <h1 className="text-2xl font-bold text-white mb-4">{company.name}</h1>
              <div className="flex items-center gap-3 mt-3">
                <Award className="w-6 h-6 text-yellow-400" />
                <span className="text-lg text-white font-medium">
                  {company.fields.join(', ')}
                </span>
              </div>
            </div>
          </div>





          {/* Main Content */}
          <div className="p-8">

            <div className="grid grid-cols-3 gap-12">
              {/* Left Column - About Section */}
              <div className="col-span-1 space-y-10">
                {/* About Section */}
                <div className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-all duration-300">
                  <h2 className="text-1xl font-bold mb-6 text-gray-800 border-b-2 border-blue-500 pb-3 flex items-center gap-3">
                    <Building2 className="w-8 h-8 text-blue-500" />
                    Giới thiệu công ty
                  </h2>
                  <p className="text-xl text-gray-700 leading-relaxed">{company.description}</p>
                </div>
                <div className="grid grid-cols gap-4">
                  {/* Thay thế phần này bằng renderRatingSection() */}
                  {renderRatingSection()}
                </div>



                {/* Stats Cards */}
                <div className="grid grid-cols gap-4">
                  <div className="bg-gradient-to-br from-purple-50 to-pink-50 p-8 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300">
                    <div className="flex items-center gap-3 mb-4">
                      <Users className="w-8 h-8 text-purple-500" />
                      <h3 className="text-xl font-semibold text-gray-700">Đang tuyển: {company.recruitQuantity} vị trí </h3>
                    </div>

                  </div>

                  {/* Rating Card */}
                  {role === 'applicant' && (
                    <div className="bg-gradient-to-br from-yellow-50 to-amber-50 p-8 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300">
                      <div className="flex items-center gap-3 mb-4">
                        <Star className="w-8 h-8 text-yellow-500" />
                        <h3 className="text-xl font-semibold text-gray-700">Đánh giá</h3>
                      </div>
                      <div className="flex flex-col items-center space-y-4">
                        {/* Current Rating Display */}
                        <div className="flex items-center">
                          <div className="flex">
                            {[1, 2, 3, 4, 5].map((star) => (
                              <Star
                                key={star}
                                className={`w-8 h-8 ${star <= Math.round(company.rating)
                                    ? 'text-yellow-500 fill-current'
                                    : 'text-gray-300'
                                  }`}
                              />
                            ))}
                          </div>
                          <span className="ml-2 text-xl text-gray-700 font-bold">
                            {company.rating ? company.rating.toFixed(1) : 'Chưa có'}
                          </span>
                        </div>

                        {/* Rating Button */}
                        <button
                          className="w-full px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-colors"
                          onClick={() => {

                            setShowRatingModal(true);
                            setUserRating(0);
                          }}
                        >
                          Đánh giá công ty
                        </button>
                      </div>
                    </div>
                  )}
                </div>

                {renderRatingModal()}
              </div>
              {/* Right Column - Contact Information */}
              <div className="col-span-2 bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-all duration-300">
                <h2 className="text-1xl font-bold mb-8 text-gray-800 border-b-2 border-purple-500 pb-3 flex items-center gap-3">
                  <Mail className="w-8 h-8 text-purple-500" />
                  Thông tin liên hệ
                </h2>

                <div className="space-y-8">
                  {/* Address Section */}
                  <div className="space-y-4">
                    <h3 className="text-xl font-semibold text-gray-700 flex items-center gap-2">
                      <MapPin className="w-5 h-5 text-blue-500" />
                      Địa chỉ văn phòng
                    </h3>
                    <div className="ml-8 p-4 bg-blue-50 rounded-lg hover:bg-blue-100 transition-all duration-300">
                      <p className="text-base text-gray-700">{company.fullAddress}</p>
                    </div>
                  </div>



                  {/* Phone Numbers Section */}
                  <div className="space-y-4">
                    <h3 className="text-1xl font-semibold text-gray-700 flex items-center gap-2">
                      <Phone className="w-6 h-6 text-green-500" />
                      Số điện thoại liên hệ
                    </h3>
                    {company.phoneNumbers.map((phone, index) => (
                      <div key={index} className="ml-8 p-4 bg-green-50 rounded-lg hover:bg-green-100 transition-all duration-300">
                        <a href={`tel:${phone}`} className="text-1xl text-gray-700 hover:text-green-600 flex items-center gap-2">
                          {phone}
                          <span className="text-sm text-green-600">(Bấm để gọi)</span>
                        </a>
                      </div>
                    ))}
                  </div>

                  {/* Email Section */}
                  <div className="space-y-4">
                    <h3 className="text-1xl font-semibold text-gray-700 flex items-center gap-2">
                      <Mail className="w-6 h-6 text-red-500" />
                      Email liên hệ
                    </h3>
                    {company.emails.map((email, index) => (
                      <div key={index} className="ml-8 p-4 bg-red-50 rounded-lg hover:bg-red-100 transition-all duration-300">
                        <a href={`mailto:${email}`} className="text-1xl text-gray-700 hover:text-red-600 flex items-center gap-2">
                          {email}
                          <span className="text-sm text-red-600">(Bấm để gửi mail)</span>
                        </a>
                      </div>
                    ))}
                  </div>

                  {/* Tax Code Section */}
                  <div className="space-y-4">
                    <h3 className="text-1xl font-semibold text-gray-700 flex items-center gap-2">
                      <Building2 className="w-6 h-6 text-orange-500" />
                      Mã số thuế
                    </h3>
                    <div className="ml-8 p-4 bg-orange-50 rounded-lg">
                      <p className="text-1xl text-gray-700">{company.taxCode}</p>
                    </div>
                  </div>

                </div>
                <div>
                  {/* Job Openings Section - Thêm vào sau phần main content */}
                  <div className="w-full bg-gray-50 mt-4"> {/* Giảm mt-8 xuống mt-4 */}
                    <div className="max-w-4xl mx-auto px-8 py-6"> {/* Giảm py-12 xuống py-6 */}
                      <h2 className="text-1xl font-bold mb-6 text-gray-800 border-b-2 border-green-500 pb-2 flex items-center gap-3"> {/* Giảm mb-12 xuống mb-6, pb-4 xuống pb-2 */}
                        <Users className="w-10 h-10 text-green-500" />
                        Vị trí đang tuyển dụng ({company?.recruitQuantity || 0} vị trí)
                      </h2>
                      <div className="grid grid-cols-1 gap-6"> {/* Giảm gap-8 xuống gap-6 */}
                        {company.jobPostings && company.jobPostings.map((job) => (
                          <div key={job.id} className="bg-white rounded-2xl p-6 shadow-lg hover:shadow-2xl transition-all duration-300 w-full"> {/* Giảm p-8 xuống p-6 */}
                            <div className="flex justify-between items-start mb-4"> {/* Giảm mb-6 xuống mb-4 */}
                              <h3 className="text-xl font-bold text-gray-800 flex-grow">{job.jobType}</h3>
                              <span className="px-4 py-2 bg-green-100 text-green-700 rounded-full text-lg font-medium ml-4">
                                {job.level}
                              </span>
                            </div>

                            <div className="space-y-4 mt-4"> {/* Giảm space-y-6 xuống space-y-4, mt-6 xuống mt-4 */}
                              <div className="flex items-center gap-4 text-gray-700">
                                <MapPin className="w-7 h-7 text-blue-500 flex-shrink-0" />
                                <span className="text-1xl">{job.ward}, {job.city}, {job.province}</span>
                              </div>

                              <div className="flex items-center gap-4 text-gray-700">
                                <Users className="w-7 h-7 text-purple-500 flex-shrink-0" />
                                <span className="text-1xl">{job.schedule}</span>
                              </div>

                              <div className="flex items-center gap-4 text-gray-700">
                                <Award className="w-7 h-7 text-yellow-500 flex-shrink-0" />
                                <span className="text-1xl">${job.minSalary}k - ${job.maxSalary}k</span>
                              </div>
                            </div>

                            <div className="mt-6 pt-4 border-t border-gray-100"> {/* Giảm mt-8 xuống mt-6, pt-6 xuống pt-4 */}
                              <NavLink to={`http://47.128.243.193:8080/JobDetail/${job.id}`} className="block">
                                <button className="w-full px-8 py-3 bg-gradient-to-r from-blue-500 to-purple-500 text-white rounded-xl 
                text-1xl font-semibold transform transition-all duration-300 
                hover:scale-[1.02] hover:shadow-xl hover:from-blue-600 hover:to-purple-600 
                flex items-center justify-center gap-3"> {/* Giảm py-4 xuống py-3 */}
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
                          <p className="text-1xl text-gray-500 font-medium">
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
