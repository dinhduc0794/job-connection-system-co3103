
import React, { useState, useEffect } from 'react';
import AppNavbar from '../Jsx/AppNavbar';
import CompanyNavbar from '../Jsx/CompanyNavbar';
import Navbar from '../Jsx/navbar';
import Footer from '../Jsx/Footer';
import axios from 'axios';
import { useSearchParams, useNavigate } from 'react-router-dom';
import TokenManager from '../utils/tokenManager';
import placeholderImage from '../images/logo3.png';
import {
  Star,
  Search,
  X,
  ChevronUp,
  ChevronDown,
  MapPin,

  Filter as FilterIcon,
  ArrowRight,
  Loader2
} from 'lucide-react';
const token = TokenManager.getToken();
const role = token?.role;
const renderNavbar = () => {
  if (role === 'applicant') return <AppNavbar />;
  if (role === 'company') return <CompanyNavbar />;
  return <Navbar />;
};

const CompanySearch = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [companies, setCompanies] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  // Location state
  const [locations, setLocations] = useState([]);
  const [cities, setCities] = useState([]);
  const [wards, setWards] = useState([]);

  // Cascading location selection
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedWard, setSelectedWard] = useState('');

  // Other filter states
  const [searchTitle, setSearchTitle] = useState('');
  const [selectedField, setSelectedField] = useState('');
  const [minRating, setMinRating] = useState('');
  const [fields, setFields] = useState([]);

  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const companiesPerPage = 9;

  const navigate = useNavigate();
  const [expandedSections, setExpandedSections] = useState({
    location: false,
    requirements: false,
    rating: false
  });

  // Fetch locations and fields
  useEffect(() => {
    const fetchLocationsAndFields = async () => {
      try {
        const [locationsResponse, fieldsResponse] = await Promise.all([
          axios.get('http://47.128.243.193:8080/public/locations'),
          axios.get('http://47.128.243.193:8080/public/fields')
        ]);
        setLocations(locationsResponse.data);
        setFields(fieldsResponse.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchLocationsAndFields();
  }, []);
  // Fetch companies khi trang được tải lần đầu
  useEffect(() => {
    fetchCompanies();
  }, []);

  // Update cities when province changes
  useEffect(() => {
    const selectedProvinceObj = locations.find(loc => loc.name === selectedProvince);
    if (selectedProvinceObj) {
      setCities(selectedProvinceObj.cities || []);
      setSelectedCity('');
      setSelectedWard('');
      setWards([]);
    } else {
      setCities([]);
      setSelectedCity('');
      setSelectedWard('');
      setWards([]);
    }
  }, [selectedProvince, locations]);

  // Update wards when city changes
  useEffect(() => {
    const selectedCityObj = cities.find(city => city.name === selectedCity);
    if (selectedCityObj) {
      setWards(selectedCityObj.wards || []);
      setSelectedWard('');
    } else {
      setWards([]);
      setSelectedWard('');
    }
  }, [selectedCity, cities]);

  // Fetch companies with dynamic filtering
  useEffect(() => {

  })
  const fetchCompanies = async () => {
    setIsLoading(true);
    try {
      console.log('Đang fetch!');
      const params = {};
      if (searchTitle) params.name = searchTitle;
      if (selectedProvince) params.province = encodeURIComponent(selectedProvince);
      if (selectedCity) params.city = encodeURIComponent(selectedCity);
      if (selectedWard) params.ward = encodeURIComponent(selectedWard);
      if (selectedField) params.field = encodeURIComponent(selectedField);
      if (minRating) params.minRating = minRating;

      console.log('Request Parameters:', params);

      const response = await axios.get('http://47.128.243.193:8080/public/companies', { params });
      setCompanies(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      console.error('Error fetching companies:', error);
      setIsLoading(false);
    } finally {
      console.log('Đã tìm xong!');
      setIsLoading(false);
    }
  };

  // Update search params
  const updateSearchParams = () => {
    const params = new URLSearchParams();
    if (searchTitle) params.set('name', searchTitle);
    if (selectedProvince) params.set('province', selectedProvince);
    if (selectedCity) params.set('city', selectedCity);
    if (selectedWard) params.set('ward', selectedWard);
    if (selectedField) params.set('field', selectedField);
    if (minRating) params.set('minRating', minRating);
    setSearchParams(params);
  };

  // Handlers
  const handleSearch = () => {
    fetchCompanies();
    updateSearchParams();
  };

  const handleClearFilters = () => {
    setSearchTitle('');
    setSelectedProvince('');
    setSelectedCity('');
    setSelectedWard('');
    setSelectedField('');
    setMinRating('');
    setSearchParams({});
    fetchCompanies();
  };
  const CompanyCardSkeleton = () => (
    <div className="bg-white rounded-xl shadow-lg animate-pulse">
      <div className="w-full h-48 bg-gray-300 rounded-t-xl"></div>
      <div className="p-5">
        <div className="h-6 bg-gray-200 mb-2 w-3/4 rounded"></div>
        <div className="h-4 bg-gray-200 mb-2 w-1/2 rounded"></div>
        {[1, 2, 3, 4].map((_, index) => (
          <div key={index} className="h-4 bg-gray-200 mb-1 w-full rounded"></div>
        ))}
        <div className="h-10 bg-gray-200 mt-4 rounded"></div>
      </div>
    </div>
  );
  // Pagination logic
  const indexOfLastCompany = currentPage * companiesPerPage;
  const indexOfFirstCompany = indexOfLastCompany - companiesPerPage;
  const currentCompanies = Array.isArray(companies)
    ? companies.slice(indexOfFirstCompany, indexOfLastCompany)
    : [];
  const totalPages = Array.isArray(companies) ? Math.ceil(companies.length / companiesPerPage) : 0;
  return (
    <>
      {renderNavbar()}
      <div className="container mx-auto px-4 py-8 bg-gray-50 min-h-screen  mt-20">
        <div className="max-w-6xl mx-auto">
          {/* Large Search Bar */}
          <div className="mb-6">
            <div className="relative">
              <input
                type="text"
                placeholder="Tìm kiếm tên công ty"
                value={searchTitle}
                onChange={(e) => setSearchTitle(e.target.value)}
                className="w-full p-4 pl-12 border-2 border-blue-300 rounded-xl text-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <Search className="absolute left-4 top-4 text-blue-500 w-6 h-6" />
            </div>
          </div>

          {/* Filter Row */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            {/* Location Filters */}
            <div className="space-y-4">
              <select
                value={selectedProvince}
                onChange={(e) => setSelectedProvince(e.target.value)}
                className="w-full p-3 border rounded-lg bg-white focus:ring-2 focus:ring-blue-300"
              >
                <option value="">Chọn Tỉnh/Thành Phố</option>
                {locations.map(loc => (
                  <option key={loc.id} value={loc.name}>{loc.name}</option>
                ))}
              </select>

              <select
                value={selectedCity}
                onChange={(e) => setSelectedCity(e.target.value)}
                disabled={!selectedProvince}
                className="w-full p-3 border rounded-lg bg-white disabled:bg-gray-200 focus:ring-2 focus:ring-blue-300"
              >
                <option value="">Chọn Quận/Huyện</option>
                {cities.map(city => (
                  <option key={city.id} value={city.name}>{city.name}</option>
                ))}
              </select>

              <select
                value={selectedWard}
                onChange={(e) => setSelectedWard(e.target.value)}
                disabled={!selectedCity}
                className="w-full p-3 border rounded-lg bg-white disabled:bg-gray-200 focus:ring-2 focus:ring-blue-300"
              >
                <option value="">Chọn Phường/Xã</option>
                {wards.map(ward => (
                  <option key={ward.id} value={ward.name}>{ward.name}</option>
                ))}
              </select>
            </div>

            {/* Field Filter */}
            <div className="space-y-4">
              <select
                value={selectedField}
                onChange={(e) => setSelectedField(e.target.value)}
                className="w-full p-3 border rounded-lg bg-white focus:ring-2 focus:ring-blue-300"
              >
                <option value="">Chọn Lĩnh Vực</option>
                {fields.map(field => (
                  <option key={field.id} value={field.name}>{field.name}</option>
                ))}
              </select>

              {/* Rating Filter */}
              <div className="relative">
                <input
                  type="number"
                  placeholder="Đánh giá tối thiểu"
                  value={minRating}
                  onChange={(e) => setMinRating(e.target.value)}
                  min="0"
                  max="5"
                  className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-300"
                />
                <Star className="absolute right-4 top-4 text-yellow-500" />
              </div>






              {/* Search and Clear Buttons */}
              <div className="flex space-x-2">
                <button
                  onClick={() => {
                    fetchCompanies();
                    updateSearchParams();
                  }}
                  disabled={isLoading}
                  className="bg-blue-500 text-white p-3 rounded-lg hover:bg-blue-600 transition flex-1 flex items-center justify-center disabled:opacity-50"
                >
                  {isLoading ? (
                    <><Loader2 className="mr-2 animate-spin" /> Đang tìm...</>
                  ) : (
                    <><Search className="mr-2" /> Tìm kiếm</>
                  )}
                </button>
                <button
                  onClick={handleClearFilters}
                  className="bg-gray-200 text-gray-700 p-3 rounded-lg hover:bg-gray-300 transition"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>
            </div>
            {/* Loading or Results Count */}

            <div className="flex items-center justify-center bg-blue-50 rounded-lg p-4 text-center">
              <div>
                <p className="text-blue-700 font-semibold">
                  {isLoading
                    ? "Đang tìm kiếm..."
                    : `Tìm thấy ${companies.length} công ty`}
                </p>
              </div>
            </div>
          </div>



          {isLoading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[1, 2, 3, 4, 5, 6].map((_, index) => (
                <CompanyCardSkeleton key={index} />
              ))}
            </div>
          ) : currentCompanies.length > 0 ? (
            <>
              {/* Companies Grid */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {currentCompanies.map((company) => (
                  <div
                    key={company.id}
                    className="bg-white rounded-2xl shadow-lg hover:shadow-xl transition-transform transform hover:-translate-y-2 border border-gray-200 overflow-hidden"
                  >
                    {/* Company Image */}
                    <div className="relative">
                      <img
                        src={placeholderImage}
                        alt={company.name}
                        className="w-full h-48 object-cover"
                      />
                      <span className="absolute top-2 right-2 bg-blue-500 text-white text-xs font-semibold px-3 py-1 rounded-full shadow-sm">
                        {company.fields?.[0] || "Unknown Field"}
                      </span>
                    </div>

                    {/* Company Information */}
                    <div className="p-6 space-y-4">
                      {/* Company Name */}
                      <h2 className="text-lg font-bold text-gray-800 truncate">{company.name}</h2>

                      {/* Rating */}
                      <div className="flex items-center space-x-2">
                        <StarRating rating={company.rating || 0} />

                      </div>

                      {/* Address */}
                      <p className="text-sm text-gray-600 flex items-center">
                        <MapPin className="w-4 h-4 text-blue-500 mr-2" />
                        {company.fullAddress || "Địa chỉ không xác định"}
                      </p>

                      {/* Tax Code */}
                      <p className="text-sm text-gray-600">
                        <i className="mr-2">💼</i>
                        Mã số thuế: <span className="font-medium">{company.taxCode || "Không có"}</span>
                      </p>

                      {/* Contact Information */}
                      <div className="space-y-2 text-sm">
                        <p className="text-gray-600">
                          <i className="mr-2">📧</i>
                          Emails: <span className="font-medium">{company.emails?.join(", ") || "Không có"}</span>
                        </p>
                        <p className="text-gray-600">
                          <i className="mr-2">📞</i>
                          Số điện thoại: <span className="font-medium">{company.phoneNumbers?.join(", ") || "Không có"}</span>
                        </p>
                      </div>

                      {/* Followers */}
                      <p className="text-sm text-gray-600 flex items-center">
                        <i className="mr-2">👥</i>
                        Người theo dõi: <span className="font-medium">{company.numberOfFollowers || 0}</span>
                      </p>
                      <button
                        onClick={() => navigate(`http://47.128.243.193:8080/allcompany/${company.id}`)}
                        className="mt-2 w-full bg-blue-500 text-white p-2 rounded-lg hover:bg-blue-600 transition flex items-center justify-center"
                      >
                        Xem Chi Tiết <ArrowRight className="ml-2 w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </>
          ) : (
            <div className="text-center text-gray-600 py-8">
              Không tìm thấy công ty nào phù hợp
            </div>
          )}



          {/* Pagination */}
          {companies.length > companiesPerPage && (
            <div className="flex justify-center mt-8 space-x-4 items-center">
              <button
                onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
                disabled={currentPage === 1}
                className="px-4 py-2 bg-blue-500 text-white rounded-lg disabled:opacity-50 hover:bg-blue-600"
              >
                Trước
              </button>
              <span className="text-gray-700">
                Trang {currentPage} / {totalPages}
              </span>
              <button
                onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
                disabled={currentPage === totalPages}
                className="px-4 py-2 bg-blue-500 text-white rounded-lg disabled:opacity-50 hover:bg-blue-600"
              >
                Tiếp
              </button>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </>
  );

};

// StarRating component remains the same as in the original code



// Star Rating Component
const StarRating = ({ rating }) => {
  const fullStars = Math.floor(rating);
  const partialStarPercentage = (rating - fullStars) * 100;

  return (
    <>
      {[...Array(5)].map((_, index) => (
        <div key={index} className="relative">
          <Star
            className={`w-5 h-5 ${index < fullStars ? 'text-yellow-500' : 'text-gray-300'}`}
            fill={index < fullStars ? 'currentColor' : 'none'}
          />
          {index === fullStars && partialStarPercentage > 0 && (
            <div
              className="absolute top-0 left-0 overflow-hidden"
              style={{ width: `${partialStarPercentage}%` }}
            >
              <Star className="w-5 h-5 text-yellow-500" fill="currentColor" />
            </div>
          )}
        </div>
      ))}
      <span className="ml-2 text-sm text-gray-600">
        ({rating.toFixed(1)})
      </span>
    </>
  );
};

export default CompanySearch;
