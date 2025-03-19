import { useState, useEffect } from 'react';
import TokenManager from '../utils/tokenManager';
import { XCircleIcon } from '@heroicons/react/24/outline';

const LocationFilter = ({ onProvinceChange, onCityChange, onWardChange, onReset }) => {
  const [locations, setLocations] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedWard, setSelectedWard] = useState('');
  const token = TokenManager.getToken();
  useEffect(() => {
    const fetchLocations = async () => {
      try {
        const response = await fetch(`/public/locations`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token.value}`, // Thêm token vào header
              // Thêm các headers khác nếu cần
              'Accept': 'application/json',
              'Access-Control-Allow-Origin': '*'
            },
            credentials: 'include' // Nếu bạn cần gửi cookies
          });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        setLocations(data);
      } catch (error) {
        console.error('Error fetching locations:', error);
      }
    };
    fetchLocations();
  }, []);

  const handleProvinceChange = (e) => {
    const provinceId = parseInt(e.target.value);
    setSelectedProvince(provinceId);
    setSelectedCity('');
    setSelectedWard('');
    onProvinceChange(provinceId);
  };

  const handleCityChange = (e) => {
    const cityId = parseInt(e.target.value);
    setSelectedCity(cityId);
    setSelectedWard('');
    onCityChange(cityId);
  };

  const handleWardChange = (e) => {
    const wardId = parseInt(e.target.value);
    setSelectedWard(wardId);
    onWardChange(wardId);
  };
  const handleReset = () => {
    setSelectedProvince('');
    setSelectedCity('');
    setSelectedWard('');
    onReset(); // Gọi callback để reset ở component cha
  };
  const selectedProvinceData = locations.find(p => p.id === selectedProvince);
  // Tìm city được chọn
  const selectedCityData = selectedProvinceData?.cities.find(c => c.id === selectedCity);
  return (
    <div className="space-y-4">
      <h3 className="font-semibold text-lg text-blue-900">Vị trí</h3>
      {(selectedProvince || selectedCity || selectedWard) && (
          <button
            onClick={handleReset}
            className="flex items-center text-gray-500 hover:text-red-500"
          >
            <XCircleIcon className="h-5 w-5 mr-1" />
            <span>Xóa bộ lọc</span>
          </button>
        )}
      {/* Select Tỉnh/Thành phố */}
      <select
        value={selectedProvince}
        onChange={handleProvinceChange}
        className="w-full p-2 border rounded-md"
      >
        <option value="">Chọn tỉnh/thành phố</option>
        {locations.map(province => (
          <option key={province.id} value={province.id}>
            {province.name}
          </option>
        ))}
      </select>

      {/* Select Quận/Huyện - Chỉ hiện khi đã chọn Tỉnh/Thành phố */}
      {selectedProvinceData && (
        <select
          value={selectedCity}
          onChange={handleCityChange}
          className="w-full p-2 border rounded-md"
        >
          <option value="">Chọn quận/huyện</option>
          {selectedProvinceData.cities.map(city => (
            <option key={city.id} value={city.id}>
              {city.name}
            </option>
          ))}
        </select>
      )}

      {/* Select Phường/Xã - Chỉ hiện khi đã chọn Quận/Huyện */}
      {selectedCityData && (
        <select
          value={selectedWard}
          onChange={handleWardChange}
          className="w-full p-2 border rounded-md"
        >
          <option value="">Chọn phường/xã</option>
          {selectedCityData.wards.map(ward => (
            <option key={ward.id} value={ward.id}>
              {ward.name}
            </option>
          ))}
        </select>
      )}
    </div>
  );
};

export default LocationFilter;