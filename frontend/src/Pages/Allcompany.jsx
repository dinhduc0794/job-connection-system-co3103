import React, { useState, useEffect, useRef } from 'react';
//import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import {
  Search,
  X,
  Star,
  Building2,
  Mail,
  Phone,
  MapPin,
  Globe,
  Share2,
  Bookmark,
  Info,
  ChevronLeft,
  ChevronRight,
  Filter
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import Footer from '../Jsx/Footer';
import Navbar from '../Jsx/navbar';
import AppNavbar from '../Jsx/AppNavbar';
import CompanyNavbar from '../Jsx/CompanyNavbar';
import TokenManager from '../utils/tokenManager';

const AllCompany = () => {
  // State for companies and search
  const [companies, setCompanies] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [locationSearch, setLocationSearch] = useState('');
  const [minRating, setMinRating] = useState(0);
  const [suggestions, setSuggestions] = useState([]);
  const [locationSuggestions, setLocationSuggestions] = useState([]);
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const [isLocationFocused, setIsLocationFocused] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [bookmarkedCompanies, setBookmarkedCompanies] = useState(new Set());
  const [hoveredCard, setHoveredCard] = useState(null);
  const [role, setRole] = useState(null); // State để lưu role
  const [userId, setUserId] = useState(null); // State để lưu userId
  const [userInfo, setUserInfo] = useState(null);
  const token = TokenManager.getToken();
  const searchRef = useRef(null);
  const locationRef = useRef(null);
  const itemsPerPage = 6;
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  // Fetch companies data
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
    const fetchCompanies = async () => {
      setIsLoading(true);
      try {
        const headers = {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Origin': '*'
        };
  
        const token = TokenManager.getToken();
        if (token?.value) {
          headers['Authorization'] = `Bearer ${token.value}`;
        }
  
        const response = await fetch('/public/companies', {
          method: 'GET',
          headers,
          credentials: 'include'
        });
  
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
  
        const data = await response.json();
        setCompanies(Array.isArray(data) ? data : []);
      } catch (error) {
        console.error('Error fetching companies:', error);
        setCompanies([]);
      }
    };
  
    fetchCompanies();
  }, []);

  // Handle company name search
  const handleSearch = (term) => {
    setSearchTerm(term);
    setCurrentPage(1);

    if (term.trim()) {
      const searchTermLower = term.toLowerCase();
      const filtered = companies.filter(company => 
        company.name?.toLowerCase().includes(searchTermLower)
      );
      setSuggestions(filtered.slice(0, 5));
    } else {
      setSuggestions([]);
    }
  };

  // Handle location search
  const handleLocationSearch = (term) => {
    setLocationSearch(term);
    setCurrentPage(1);

    if (term.trim()) {
      const locationTermLower = term.toLowerCase();
      const locationSuggestions = companies
        .filter(company => company.addresses)
        .flatMap(company => company.addresses)
        .filter(address => address?.toLowerCase().includes(locationTermLower))
        .slice(0, 5);
      setLocationSuggestions(locationSuggestions);
    } else {
      setLocationSuggestions([]);
    }
  };

  // Clear search fields
  const clearSearch = () => {
    setSearchTerm('');
    setSuggestions([]);
    searchRef.current?.focus();
  };

  const clearLocationSearch = () => {
    setLocationSearch('');
    setLocationSuggestions([]);
    locationRef.current?.focus();
  };

  // Filter companies based on all criteria
  const filteredCompanies = companies.filter(company => {
    const nameMatch = company.name?.toLowerCase().includes(searchTerm.toLowerCase());
    const locationMatch = !locationSearch || 
      company.addresses?.some(address => 
        address?.toLowerCase().includes(locationSearch.toLowerCase())
      );
    const ratingMatch = company.rating >= minRating;
    
    return nameMatch && locationMatch && ratingMatch;
  });

  // Pagination
  const totalPages = Math.ceil(filteredCompanies.length / itemsPerPage);
  const paginatedCompanies = filteredCompanies.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <>
    {renderNavbar()}
    <div className="w-full max-w-[2200px] mx-auto px-4 py-8  bg-gradient-to-br from-slate-100 to-blue-100">
      <div className="w-full max-w-[2300px] mx-auto">
        <motion.div 
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8 space-y-4"
        >
          <h1 className="text-5xl font-bold text-center bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-purple-600">
            Danh Mục Công Ty
          </h1>
          
          {/* Search Filters */}
          <div className="bg-white/80 backdrop-blur-sm p-8 rounded-xl shadow-lg space-y-6 border border-blue-100 max-w-[2300px] mx-auto"> {/* Tăng padding và space-y */}
  {/* Company Name Search */}
  <div className="flex flex-col gap-6"> {/* Tăng gap */}
    <div className="flex gap-4"> {/* Tăng gap */}
      <div className="relative flex-1">
        <input
          ref={searchRef}
          type="text"
          value={searchTerm}
          onChange={(e) => handleSearch(e.target.value)}
          onFocus={() => setIsSearchFocused(true)}
          onBlur={() => setTimeout(() => setIsSearchFocused(false), 200)}
          placeholder="Tìm kiếm theo tên công ty..."
          className="w-full pl-14 pr-12 py-4 text-lg border-2 border-blue-200 rounded-xl focus:border-blue-400 focus:ring-2 focus:ring-blue-200 transition-all duration-200"
        /> {/* Tăng padding, font-size và border-radius */}
        <Search className="absolute left-5 top-1/2 transform -translate-y-1/2 text-blue-400 w-6 h-6" /> {/* Tăng size icon và điều chỉnh vị trí */}
        {searchTerm && (
          <button 
            onClick={clearSearch} 
            className="absolute right-5 top-1/2 transform -translate-y-1/2"
          >
            <X className="w-6 h-6 text-gray-400 hover:text-gray-600" /> {/* Tăng size icon */}
          </button>
        )}
      </div>
      
      <button 
        onClick={() => handleSearch(searchTerm)}
        className="px-8 py-4 bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition-colors flex items-center gap-3 text-lg font-medium"
      > {/* Tăng padding, font-size và border-radius */}
        <Search size={24} /> {/* Tăng size icon */}
        Tìm kiếm
      </button>

      <button 
        onClick={() => {
          setSearchTerm('');
          setLocationSearch('');
          setMinRating(0);
          setSuggestions([]);
          setLocationSuggestions([]);
        }} 
        className="px-8 py-4 bg-red-500 text-white rounded-xl hover:bg-red-600 transition-colors flex items-center gap-3 text-lg font-medium"
      > {/* Tăng padding, font-size và border-radius */}
        <X size={24} /> {/* Tăng size icon */}
        Xóa tất cả
      </button>
    </div>
 


    {/* Company Suggestions */}
    <AnimatePresence>
      {isSearchFocused && suggestions.length > 0 && (
        <motion.div 
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          className="absolute z-20 w-full mt-2 bg-white rounded-lg shadow-lg border border-blue-100"
        >
          {suggestions.map((company) => (
            <button
              key={company.id}
              onClick={() => {
                setSearchTerm(company.name);
                setSuggestions([]);
              }}
              className="w-full px-4 py-3 text-left hover:bg-blue-50 flex items-center gap-2"
            >
              <Building2 className="text-blue-500" />
              <div>
                <p className="font-medium">{company.name}</p>
                <p className="text-sm text-gray-500">{company.fields}</p>
              </div>
            </button>
          ))}
        </motion.div>
      )}
    </AnimatePresence>
  </div>

  {/* Location Search */}
  <div className="relative">
    <input
      ref={locationRef}
      type="text"
      value={locationSearch}
      onChange={(e) => handleLocationSearch(e.target.value)}
      onFocus={() => setIsLocationFocused(true)}
      onBlur={() => setTimeout(() => setIsLocationFocused(false), 200)}
      placeholder="Tìm kiếm theo địa điểm..."
      className="w-full pl-12 pr-10 py-3 border-2 border-blue-200 rounded-lg focus:border-blue-400 focus:ring-2 focus:ring-blue-200 transition-all duration-200"
    />
    <MapPin className="absolute left-4 top-1/2 transform -translate-y-1/2 text-blue-400" />
    {locationSearch && (
      <button onClick={clearLocationSearch} className="absolute right-4 top-1/2 transform -translate-y-1/2">
        <X className="text-gray-400 hover:text-gray-600" />
      </button>
    )}
    
    {/* Location Suggestions */}
    <AnimatePresence>
      {isLocationFocused && locationSuggestions.length > 0 && (
        <motion.div 
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          className="absolute z-20 w-full mt-2 bg-white rounded-lg shadow-lg border border-blue-100"
        >
          {locationSuggestions.map((address, index) => (
            <button
              key={index}
              onClick={() => {
                setLocationSearch(address);
                setLocationSuggestions([]);
              }}
              className="w-full px-4 py-3 text-left hover:bg-blue-50 flex items-center gap-2"
            >
              <MapPin className="text-blue-500" />
              <span>{address}</span>
            </button>
          ))}
        </motion.div>
      )}
    </AnimatePresence>
  </div>

  {/* Rating Filter */}
  <div className="flex items-center gap-6 bg-white/90 p-6 rounded-xl shadow-md">
  <div className="flex items-center gap-4">
    <Star className="w-8 h-8 text-yellow-400" /> {/* Tăng kích thước icon sao */}
    <span className="text-xl font-medium whitespace-nowrap">Đánh giá tối thiểu:</span>
    <input
      type="range"
      min="0"
      max="5"
      step="0.5"
      value={minRating}
      onChange={(e) => setMinRating(parseFloat(e.target.value))}
      className="w-48 h-3 bg-blue-200 rounded-lg appearance-none cursor-pointer"
    />
    <span className="text-2xl font-bold text-blue-600 min-w-[3ch]">
      {minRating.toFixed(1)}
    </span>
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map((num) => (
        <Star
          key={num}
          className={`w-6 h-6 ${
            num <= minRating
              ? 'text-yellow-400 fill-yellow-400'
              : 'text-gray-300'
          }`}
        />
      ))}
    </div>
  </div>
  </div>
</div>
</motion.div>
        
        {/* Company Cards */}
        <div className="grid grid-cols-1 md:grid-cols-1 lg:grid-cols-2 xl:grid-cols-2 2xl:grid-cols-3 gap-8"> {/* Giảm số cột và tăng gap */}
  <AnimatePresence>
    {paginatedCompanies.map((company) => (
      <motion.div
        key={company.id}
        layout
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 0.9 }}
        whileHover={{ scale: 1.02 }}
        className="bg-white rounded-xl shadow-lg overflow-hidden border border-blue-100 group hover:shadow-xl transition-all duration-300"
      >
        <div className="relative h-64"> {/* Tăng chiều cao ảnh từ h-48 lên h-64 */}
          <img
            src={company.image || "/api/placeholder/400/320"}
            alt={company.name}
            className="w-full h-full object-cover opacity-75 transition-transform duration-300 group-hover:scale-105"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/50 to-transparent">
            <div className="absolute bottom-6 left-6 right-6"> {/* Tăng padding bottom và left/right */}
              <h2 className="text-2xl font-bold text-white mb-3 line-clamp-2"> {/* Tăng font-size và margin-bottom */}
                {company.name}
              </h2>
              <div className="flex items-center gap-2">
                <Star className="w-6 h-6 text-yellow-400 fill-current" /> {/* Tăng kích thước icon */}
                <span className="text-lg text-white font-medium"> {/* Tăng font-size */}
                  {company.rating.toFixed(1)}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="p-6 space-y-6"> {/* Tăng padding và space between */}
          <p className="text-lg text-gray-600 line-clamp-2"> {/* Tăng font-size */}
            {company.description}
          </p>

          <div className="grid grid-cols-2 gap-6"> {/* Tăng gap */}
            {company.taxCode && (
              <div className="flex items-center gap-3"> {/* Tăng gap */}
                <Building2 className="w-6 h-6 text-blue-500" /> {/* Tăng kích thước icon */}
                <span className="text-base">{company.taxCode}</span> {/* Tăng font-size */}
              </div>
            )}
            
            {company.fields && (
              <div className="flex items-center gap-3">
                <Globe className="w-6 h-6 text-blue-500" />
                <span className="text-base">{company.fields}</span>
              </div>
            )}

            {company.addresses?.[0] && (
              <div className="col-span-2 flex items-start gap-3">
                <MapPin className="w-6 h-6 text-blue-500 mt-1" />
                <span className="text-base line-clamp-2">
                  {company.addresses[0]}
                </span>
              </div>
            )}
          </div>
          <motion.button
  whileHover={{ scale: 1.02 }}
  whileTap={{ scale: 0.98 }}
  onClick={() => navigate(`/allcompany/${company.id}`)}
  className="w-full bg-gradient-to-r from-blue-500 to-blue-600 text-white font-medium py-4 px-8 rounded-lg text-xl hover:shadow-lg transition-all duration-300"
>
  Xem chi tiết
</motion.button>
        </div>
      </motion.div>
    ))}
  </AnimatePresence>
</div>
        {/* Pagination */}
        {totalPages > 1 && (
          <div className="mt-8 flex justify-center gap-2">
            <motion.button
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
              onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
              disabled={currentPage === 1}
              className="p-2 rounded-lg bg-white shadow-md disabled:opacity-50"
            >
              <ChevronLeft className="text-blue-500" />
            </motion.button>
            
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
              <motion.button
                key={page}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
                onClick={() => setCurrentPage(page)}
                className={`w-10 h-10 rounded-lg font-medium shadow-md
                  ${currentPage === page 
                    ? 'bg-blue-500 text-white' 
                    : 'bg-white text-blue-500'
                  }`}
              >
                {page}
              </motion.button>
            ))}
            
            <motion.button
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
              onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
              disabled={currentPage === totalPages}
              className="p-2 rounded-lg bg-white shadow-md disabled:opacity-50"
            >
              <ChevronRight className="text-blue-500" />
            </motion.button>
            </div>
          )}
  
          {/* No Results Message */}
          {!isLoading && filteredCompanies.length === 0  && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="text-center py-12 px-4"
            >
              <div className="max-w-md mx-auto bg-white rounded-xl shadow-lg p-8">
                <Info className="w-16 h-16 text-blue-500 mx-auto mb-4" />
                <h3 className="text-xl font-semibold text-gray-800 mb-2">
                  Không tìm thấy kết quả
                </h3>
                <p className="text-gray-600">
                  Không có công ty nào phù hợp với tiêu chí tìm kiếm của bạn. 
                  Vui lòng thử lại với các bộ lọc khác.
                </p>
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  onClick={() => {
                    clearSearch();
                    clearLocationSearch();
                    setMinRating(0);
                  }}
                  className="mt-6 px-6 py-2 bg-gradient-to-r from-blue-500 to-blue-600 
                    text-white rounded-lg font-medium hover:shadow-lg transition-all"
                >
                  Xóa bộ lọc
                </motion.button>
              </div>
            </motion.div>
          )}
  
          {/* Loading State */}
          {!companies.length && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="text-center py-12"
            >
              <div className="inline-block p-4 bg-white rounded-lg shadow-lg">
                <div className="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto" />
                <p className="mt-4 text-gray-600 font-medium">Đang tải dữ liệu...</p>
              </div>
            </motion.div>
          )}
  
          {/* Back to Top Button */}
          <motion.button
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
            className="fixed bottom-8 right-8 p-4 bg-blue-500 text-white rounded-full shadow-lg 
              hover:bg-blue-600 transition-colors z-50"
          >
            <ChevronLeft className="w-6 h-6 transform rotate-90" />
          </motion.button>
        </div>
      </div>
      <div className="min-h-screen flex flex-col">
        <div className="flex-grow"></div>
        <Footer />
      </div>
      </>
    );
  };
  
  export default AllCompany;