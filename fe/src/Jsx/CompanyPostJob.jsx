import React, { useState, useEffect } from "react";
import "../css/CompanyPostJob.css";

const CompanyPostJob = () => {
  const [fields, setFields] = useState([]); // Dữ liệu từ API
  const [selectedField, setSelectedField] = useState(null); // Lĩnh vực đã chọn
  const [selectedJobType, setSelectedJobType] = useState(null); // Công việc đã chọn
  const [selectedSkills, setSelectedSkills] = useState([""]); // Danh sách kỹ năng đã chọn (mặc định 1)
  const [jobTypes, setJobTypes] = useState([]); // Danh sách công việc theo lĩnh vực
  const [skills, setSkills] = useState([]); // Danh sách kỹ năng theo công việc


   // Dữ liệu cho địa điểm
   const [provinces, setProvinces] = useState([]); // Tỉnh
   const [selectedProvince, setSelectedProvince] = useState(null); // Tỉnh đã chọn
   const [selectedDistrict, setSelectedDistrict] = useState(null); // Huyện đã chọn
   const [selectedCity, setSelectedCity] = useState(null); // Dùng cho huyện
   const [districts, setDistricts] = useState([]); // Quận/Huyện
   const [wards, setWards] = useState([]); // Xã/Phường
   const [selectedWard, setSelectedWard] = useState(null); // Xã/Phường đã chọn

   // Trường state để lưu giá trị nhập vào
  const [jobTitle, setJobTitle] = useState("");
  const [minSalary, setMinSalary] = useState("");
  const [maxSalary, setMaxSalary] = useState("");
  const [allowance, setAllowance] = useState("");
  const [level, setLevel] = useState("");
  const [schedule, setSchedule] = useState("");
  const [description, setDescription] = useState("");
  const [numOfAppli, setNumOfAppli] = useState("");

  // Gọi API để lấy dữ liệu
  useEffect(() => {
    fetch("/fields")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        setFields(data); // Lưu toàn bộ dữ liệu lĩnh vực
      })
      .catch((error) => console.error("Error fetching fields:", error));

    // Gọi API lấy danh sách tỉnh
    fetch("/provinces")
      .then((response) => response.json())
      .then((data) => {
        setProvinces(data);
        console.log("Provinces loaded:", data); // Debug: Kiểm tra dữ liệu tỉnh
      })
      .catch((error) => console.error("Error fetching provinces:", error));
  }, []);


  // JobTyoe:
   // Xử lý khi chọn lĩnh vực
  const handleFieldChange = (fieldId) => {
    setSelectedField(fieldId); // Cập nhật lĩnh vực được chọn
    setSelectedJobType(null); // Reset công việc
    setSkills([]); // Xóa danh sách kỹ năng
    setSelectedSkills([""]); // Reset kỹ năng đã chọn

    const selectedField = fields.find((field) => field.id === parseInt(fieldId));
    setJobTypes(selectedField?.jobTypes || []); // Cập nhật danh sách công việc theo lĩnh vực
  };
  // Xử lý khi chọn công việc
  const handleJobTypeChange = (jobTypeId) => {
    setSelectedJobType(jobTypeId); // Cập nhật công việc được chọn
    setSelectedSkills([""]); // Reset kỹ năng đã chọn

    const selectedJobType = jobTypes.find((job) => job.id === parseInt(jobTypeId));
    setSkills(selectedJobType?.skills || []); // Cập nhật danh sách kỹ năng theo công việc
  };

  // Xử lý thêm kỹ năng mới
  const addSkillSelect = () => {
    setSelectedSkills([...selectedSkills, ""]);
  };

  
  // Xử lý khi chọn kỹ năng từ dropdown
  const handleSkillChange = (value, index) => {
    const updatedSkills = [...selectedSkills];
    updatedSkills[index] = value;
    setSelectedSkills(updatedSkills);
  };

  // Xử lý xóa kỹ năng
  const removeSkill = (index) => {
    const updatedSkills = [...selectedSkills];
    updatedSkills.splice(index, 1);
    setSelectedSkills(updatedSkills);
  };

  // Lọc kỹ năng chưa được chọn để tránh trùng lặp
  const getAvailableSkills = () => {
    return skills.filter((skill) => !selectedSkills.includes(skill.id));
  };

  // Xử lý khi chọn tỉnh
  const handleProvinceChange = (provinceId) => {
    setSelectedProvince(provinceId);
    setSelectedCity(null); // Reset huyện
    setDistricts([]); // Reset huyện
    setSelectedDistrict(null); // Reset xã
    setWards([]); // Reset xã

    const selectedProvince = provinces.find((province) => province.id === parseInt(provinceId));
    if (selectedProvince) {
      setDistricts(selectedProvince.cities || []); // Lấy huyện từ cities
      console.log("Districts (cities) for province:", selectedProvince.cities); // Debug: Kiểm tra huyện của tỉnh
    }
  };

  // Xử lý khi chọn huyện (tỉnh thành)
  const handleCityChange = (cityId) => {
    setSelectedCity(cityId);
    setSelectedDistrict(null); // Reset xã
    setWards([]); // Reset xã

    const selectedCity = districts.find((city) => city.id === parseInt(cityId));
    if (selectedCity) {
      setWards(selectedCity.wards || []); // Lấy xã từ wards
      console.log("Wards for city:", selectedCity.wards); // Debug: Kiểm tra xã của huyện
    }
  };

  const handleSubmit = () => {
    // Kiểm tra giá trị rỗng hoặc không hợp lệ
    if (!jobTitle || !description || !schedule || !level || !selectedWard || !selectedJobType || selectedSkills.length === 0) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
    }
  
    // Chuẩn bị dữ liệu
    const jobPostingData = {
      title: jobTitle,
      description: description,
      schedule: schedule.toUpperCase(), // Chuyển thành chữ in hoa
      level: level.toUpperCase(), // Chuyển thành chữ in hoa
      minSalary: parseInt(minSalary, 10) || 0,
      maxSalary: parseInt(maxSalary, 10) || 0,
      allowance: parseInt(allowance, 10) || 0,
      numberOfApplicants: parseInt(numOfAppli, 10) || 1,
      wardId: parseInt(selectedWard, 10),
      jobTypeId: parseInt(selectedJobType, 10),
      skills: selectedSkills.map((skillId) => {
        // Lấy tên của kỹ năng dựa trên `id`
        const skill = skills.find((s) => s.id === parseInt(skillId));
        return skill ? skill.name : null;
      }).filter((skillName) => skillName !== null), // Loại bỏ null nếu không tìm thấy kỹ năng
      companyId: 2, // Giá trị cố định hoặc lấy từ tài khoản công ty đăng nhập
    };
  
    console.log("Dữ liệu gửi đi:", jobPostingData);
  
    fetch("/jobpostings", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(jobPostingData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Đăng tuyển thành công:", data);
        alert("Đăng tuyển thành công!");
      })
      .catch((error) => {
        console.error("Lỗi khi đăng tuyển:", error);
        alert("Đã xảy ra lỗi, vui lòng thử lại!");
      });
  };


  return (
    <div className="post-job">
      <h2>Đăng Tuyển Việc Làm</h2>
      {/* Các trường khác */}
      <div className="form-group">
        <label>Tiêu đề</label>
        <input
         type="text" 
         placeholder="Nhập tiêu đề công việc"
         value={jobTitle}
        onChange={(e) => setJobTitle(e.target.value)} 
        />
      </div>
      <div className="form-group">
        <label>Mức lương tối thiểu</label>
        <input
          type="number"
          placeholder="Nhập mức lương tối thiểu (VND)"
          value={minSalary}
          onChange={(e) => setMinSalary(e.target.value)}
        />
      </div>
      <div className="form-group">
        <label>Mức lương tối đa</label>
        <input
          type="number"
          placeholder="Nhập mức lương tối đa (VND)"
          value={maxSalary}
          onChange={(e) => setMaxSalary(e.target.value)}
        />
      </div>
      <div className="form-group">
        <label>Trợ cấp (nếu có)</label>
        <input
          type="number"
          placeholder="Nhập trợ cấp (VND)"
          value={allowance}
          onChange={(e) => setAllowance(e.target.value)}
        />
      </div>
      <div className="form-group">
        <label>Số lượng tuyển</label>
        <input type="number" placeholder="Nhập số lượng tuyển" />
      </div>
      <div className="form-group">
        <label>Lịch làm việc</label>
        <select
          value={schedule}
          onChange={(e) => setSchedule(e.target.value)}
        >
          <option value="">Chọn lịch làm việc</option>
          <option value="FULLTIME">Full-time</option>
          <option value="PARTTIME">Part-time</option>
          <option value="INTERNSHIP">Internship</option>
          <option value="FREELANCE">Freelance</option>
          <option value="CONTRACT">Contract</option>
        </select>
      </div>
      <div className="form-group">
        <label>Level</label>
        <select
          value={level}
          onChange={(e) => setLevel(e.target.value)}
        >
           <option value="">Chọn cấp bậc</option>
            <option value="INTERN">Intern</option>
            <option value="FRESHER">Fresher</option>
            <option value="JUNIOR">Junior</option>
            <option value="SENIOR">Senior</option>
            <option value="LEAD">Lead</option>
            <option value="MANAGER">Manager</option>
            <option value="DIRECTOR">Director</option>
        </select>
      </div>


      {/* Lĩnh vực */}
      <div className="form-group">
        <label>Lĩnh vực</label>
        <select
          value={selectedField || "null"}
          onChange={(e) => handleFieldChange(e.target.value)}
        >
          <option value="">Chọn lĩnh vực</option>
          {fields.map((field) => (
            <option key={field.id} value={field.id}>
              {field.name}
            </option>
          ))}
        </select>
      </div>

      {/* Công việc */}
      <div className="form-group">
        <label>Công việc</label>
        <select
          value={selectedJobType || ""}
          onChange={(e) => handleJobTypeChange(e.target.value)}
          disabled={!selectedField} // Disable công việc nếu chưa chọn lĩnh vực
        >
          <option value="">Chọn công việc</option>
          {jobTypes.map((job) => (
            <option key={job.id} value={job.id}>
              {job.name}
            </option>
          ))}
        </select>
      </div>

      {/* Kỹ năng */}
      <div className="form-group">
        <label>
          Kỹ năng
          <button type="button" onClick={addSkillSelect} className="add-skill-btn">
            Thêm kỹ năng
          </button>
        </label>
        
        <div className="skills-container">
          {selectedSkills.map((skillId, index) => (
            <div key={index} className="skill-row">
              <select
                value={skillId}
                onChange={(e) => handleSkillChange(e.target.value, index)}
                disabled={!selectedJobType} // Disable kỹ năng nếu chưa chọn công việc
              >
                <option value="">Chọn kỹ năng</option>
                {getAvailableSkills().map((skill) => (
                  <option key={skill.id} value={skill.id}>
                    {skill.name}
                  </option>
                ))}
              </select>
              {index > 0 && (
                <button type="button" onClick={() => removeSkill(index)} className="remove-btn">
                  Xóa
                </button>
              )}
            </div>
          ))}
        </div>
      </div>

      <div className="form-group">
        <label>Tỉnh</label>
        <select
          value={selectedProvince || ""}
          onChange={(e) => handleProvinceChange(e.target.value)}
        >
          <option value="">Chọn tỉnh</option>
          {provinces.map((province) => (
            <option key={province.id} value={province.id}>
              {province.name}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label>Huyện</label>
        <select
          value={selectedCity || ""}
          onChange={(e) => handleCityChange(e.target.value)}
          disabled={!selectedProvince} // Disable nếu chưa chọn tỉnh
        >
          <option value="">Chọn huyện</option>
          {districts.length > 0 ? (
            districts.map((city) => (
              <option key={city.id} value={city.id}>
                {city.name}
              </option>
            ))
          ) : (
            <option value="">Không có huyện nào</option> // Thêm option nếu không có huyện
          )}
        </select>
      </div>

      <div className="form-group">
        <label>Xã/Phường</label>
        <select
          value={selectedWard || ""}
          onChange={(e) => setSelectedWard(e.target.value)}
          disabled={!selectedCity} // Disable nếu chưa chọn huyện
        >
          <option value="">Chọn xã/phường</option>
          {wards.length > 0 ? (
            wards.map((ward) => (
              <option key={ward.id} value={ward.id}>
                {ward.name}
              </option>
            ))
          ) : (
            <option value="">Không có xã/phường nào</option> // Thêm option nếu không có xã/phường
          )}
        </select>
      </div>



      <div className="form-group">
        <label>Mô tả công việc</label>
        <textarea
          rows="5" 
          placeholder="Nhập chi tiết bao gồm: Mô tả công việc, Yêu cầu ứng viên, Quyền lợi, Địa điểm làm việc, Thời gian làm việc, Cách thưc ứng tuyển. Mỗi ý là 1 gạch đầu dòng"
          value={description}
          onChange={(e) => setDescription(e.target.value)} 
          ></textarea>
      </div>
      <button onClick={handleSubmit} className="post-btn">Đăng Tuyển</button>
    </div>
  );
};

export default CompanyPostJob;
