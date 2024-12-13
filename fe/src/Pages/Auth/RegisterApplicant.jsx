import React, { useState, useEffect } from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { FaHome } from "react-icons/fa";
import axios from 'axios';
import { Button } from "../../components/Button";
import { Input } from "../../components/input";
//import { ToastProvider, ToastViewport } from "../../components/toast";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "../../components/select";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from "../../components/form";

import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import './custom-toast.css'; // Import custom CSS

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { PlusCircle, Trash2 } from "lucide-react";

const registrationSchema = z.object({
  username: z.string().min(4, "Tên người dùng phải có ít nhất 4 kí tự"),
  password: z.string().min(8, "Mật khẩu phải có ít nhất 8 kí tự"),
  confirmPassword: z.string(),

  firstName: z.string().min(1, "Vui lòng nhập tên"),
  lastName: z.string().min(1, "Vui lòng nhập họ"),
  specificAddress: z.string().min(1, "Vui lòng nhập địa chỉ cụ thể"),
  emails: z.array(z.object({
    value: z.string().email("Email không hợp lệ")
  })).min(1, "Phải có ít nhất một email"),
  phoneNumbers: z.array(z.object({
    value: z.string().regex(/^0\d{9}$/, "Số điện thoại phải có 10 số và bắt đầu bằng 0")
  })).min(1, "Phải có ít nhất một số điện thoại"),
  ward: z.object({
    id: z.number(),
    name: z.string()
  })
}).refine((data) => data.password === data.confirmPassword, {
  message: "Mật khẩu xác nhận không khớp",
  path: ["confirmPassword"],
});

export default function ApplicantRegistrationForm() {
  const navigate = useNavigate();
  const [provinces, setProvinces] = useState([]);
  const [cities, setCities] = useState([]);
  const [wards, setWards] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState(null);
  const [selectedCity, setSelectedCity] = useState(null);
  //const [state, setState] = useState({ /* initial state */ });
  //const { toast } = useToast();
  const form = useForm({
    resolver: zodResolver(registrationSchema),
    defaultValues: {
      username: "",
      password: "",
      confirmPassword: "",
      firstName: "",
      lastName: "",
      specificAddress: "",
      emails: [{ value: "" }],
      phoneNumbers: [{ value: "" }],
      ward: null
    }
  });

  const { fields: emailFields, append: appendEmail, remove: removeEmail } = useFieldArray({
    control: form.control,
    name: "emails"
  });

  const { fields: phoneFields, append: appendPhone, remove: removePhone } = useFieldArray({
    control: form.control,
    name: "phoneNumbers"
  });

  useEffect(() => {
    // Fetch locations
    const fetchLocations = async () => {
      try {
        const response = await axios.get('http://47.128.243.193:8080/public/locations');
        setProvinces(response.data);
      } catch (error) {
        toast.error("Không thể tải danh sách địa điểm");////////
      }
    };
    fetchLocations();
  }, []);

  const handleProvinceChange = (provinceId) => {
    const selectedProvince = provinces.find(p => p.id === parseInt(provinceId));
    setSelectedProvince(selectedProvince);
    setCities(selectedProvince?.cities || []);
    form.setValue('ward', null);
  };

  const handleCityChange = (cityId) => {
    const selectedCity = cities.find(c => c.id === parseInt(cityId));
    setSelectedCity(selectedCity);
    setWards(selectedCity?.wards || []);
    form.setValue('ward', null);
  };

  const CustomTransition = ({ children, ...props }) => {
    // Remove unnecessary props
    const { isIn, ...restProps } = props;

    return (
      <div
        {...restProps}
        style={{
          animation: `${isIn ? 'slideIn' : 'slideOut'} 0.3s ease-in-out`,
        }}
      >
        {children}
      </div>
    );
  };



  const onSubmit = async (data) => {
    // Kiểm tra email trùng lặp
    const emailSet = new Set();
    const hasDuplicateEmails = data.emails.some(email => {
      if (emailSet.has(email.value)) {
        toast.error("Email này đã được nhập trước đó");
        return true;
      }
      emailSet.add(email.value);
      return false;
    });

    if (hasDuplicateEmails) {
      return;
    }

    // Kiểm tra số điện thoại trùng lặp
    const phoneSet = new Set();
    const hasDuplicatePhones = data.phoneNumbers.some(phone => {
      if (phoneSet.has(phone.value)) {
        toast.error("Số điện thoại bị trùng lặp trong form");
        return true;
      }
      phoneSet.add(phone.value);
      return false;
    });

    if (hasDuplicatePhones) {
      return;
    }

    // Chuẩn bị payload
    const payload = {
      ...data,
      emails: data.emails.map(e => e.value),
      phoneNumbers: data.phoneNumbers.map(p => p.value)
    };

    try {
      const response = await axios.post('http://47.128.243.193:8080/register/applicant', payload);

      // Thông báo đăng ký thành công
      toast.success("Tài khoản của bạn đã được tạo");

      // Chuyển trang sau khi đăng ký
      setTimeout(() => {
        navigate('/login');
      }, 3000);

    } catch (error) {
      console.error('Registration Error:', error);
      console.error('Error Response:', error.response);

      if (error.response && error.response.data) {
        const { message, detail } = error.response.data;

        // Nếu có mảng `detail`, duyệt từng phần tử và hiển thị lỗi tương ứng
        if (Array.isArray(detail) && detail.length > 0) {
          detail.forEach((errorDetail) => {
            if (errorDetail.toLowerCase().includes("email")) {
              toast.error("Email đã tồn tại. Vui lòng nhập email khác.");
            } else if (errorDetail.toLowerCase().includes("username")) {
              toast.error("Tên người dùng đã tồn tại. Vui lòng chọn tên khác.");
            } else if (errorDetail.toLowerCase().includes("phone")) {
              toast.error("Số điện thoại đã tồn tại. Vui lòng nhập số khác.");
            } else {
              toast.error(errorDetail); // Hiển thị lỗi không xác định
            }
          });
        }
        // Nếu chỉ có `message`
        else if (message) {
          toast.error(message);
        }
        // Fallback nếu không có chi tiết lỗi
        else {
          toast.error("Có lỗi xảy ra. Vui lòng thử lại.");
        }
      } else {
        toast.error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối.");
      }
    }
  };

  return (
    // <ToastProvider>
    <>
      <ToastContainer
        position="top-center"
        autoClose={3000}
        limit={3} // Giới hạn số lượng toast hiển thị
        hideProgressBar={false}
        newestOnTop={true}
        closeOnClick
        rtl={false}
        pauseOnHover
        draggable
        pauseOnFocusLoss
        transition={CustomTransition} // Nếu cần
        className="custom-toast-container"
        toastClassName="custom-toast"
        bodyClassName="custom-toast-body"
      />
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br"
        style={{
          backgroundImage: 'linear-gradient(45deg, var(--thirdColor), var(--themeColor))',
        }}>

        <div
          className="max-w-2xl mx-auto p-6 bg-white shadow-lg rounded-lg"
          style={{
            zIndex: 1,
            backgroundColor: '#FFFFFF', // Màu trắng sáng
            boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)', // Hiệu ứng nổi
          }}
        >
          <div className="text-center mb-6">
            <h2 className="text-2xl font-bold text-blue-900">Đăng Ký Ứng Viên</h2>
            <p className="text-gray-600 mt-2 text-red-500">Vui lòng điền đầy đủ thông tin để đăng ký</p>
          </div>
          {/* {state.toasts.map((toast) => ( */}
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 bg-white p-6 rounded-lg shadow-sm">
              <div className="grid grid-cols-2 gap-4">
                <FormField
                  control={form.control}
                  name="firstName"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-gray-700">Tên</FormLabel>
                      <FormControl>
                        <Input
                          placeholder="Nhập tên"
                          {...field}
                          className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                        />
                      </FormControl>
                      <FormMessage className="text-red-500" />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="lastName"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-gray-700">Họ</FormLabel>
                      <FormControl>
                        <Input placeholder="Nhập họ" {...field}
                          className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                        />
                      </FormControl>
                      <FormMessage className="text-red-500" />
                    </FormItem>
                  )}
                />
              </div>

              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-700">Tên đăng nhập</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="Chọn tên đăng nhập"
                        {...field}
                        className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                      />
                    </FormControl>
                    <FormDescription className="text-gray-600">Tên đăng nhập phải là duy nhất</FormDescription>
                    <FormMessage className="text-red-500" />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-700">Mật khẩu</FormLabel>
                    <FormControl>
                      <Input
                        type="password"
                        placeholder="Nhập mật khẩu"
                        {...field}
                        className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                      />
                    </FormControl>
                    <FormMessage className="text-red-500" />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="confirmPassword"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-700">Xác nhận mật khẩu</FormLabel>
                    <FormControl>
                      <Input
                        type="password"
                        placeholder="Nhập lại mật khẩu"
                        {...field}
                        className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                      />
                    </FormControl>
                    <FormMessage className="text-red-500" />
                  </FormItem>
                )}
              />

              <div className="grid grid-cols-3 gap-4">
                <FormItem>
                  <FormLabel className="text-gray-700">Tỉnh/Thành phố</FormLabel>
                  <Select
                    onValueChange={handleProvinceChange}
                    className="bg-white text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                  >
                    <SelectTrigger className="text-gray-800">
                      <SelectValue placeholder="Chọn tỉnh/thành" />
                    </SelectTrigger>
                    <SelectContent>
                      {provinces.map((province) => (
                        <SelectItem
                          key={province.id}
                          value={String(province.id)}
                          className="text-gray-800 hover:bg-gray-100"
                        >
                          {province.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </FormItem>

                <FormItem>
                  <FormLabel className="text-gray-700">Quận/Huyện</FormLabel>
                  <Select
                    onValueChange={handleCityChange}
                    disabled={!selectedProvince}
                    className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500 text-gray-600"
                  >
                    <SelectTrigger className="text-gray-800">
                      <SelectValue placeholder="Chọn quận/huyện" />
                    </SelectTrigger>
                    <SelectContent>
                      {cities.map((city) => (
                        <SelectItem key={city.id} value={String(city.id)} className="text-gray-800 hover:bg-gray-100">
                          {city.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </FormItem>

                <FormField
                  control={form.control}
                  name="ward"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-gray-700">Phường/Xã</FormLabel>
                      <Select
                        onValueChange={(value) => {
                          const selectedWard = wards.find(w => w.id === parseInt(value));
                          field.onChange(selectedWard);
                        }}
                        disabled={!selectedCity}
                        className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500 text-gray-600"
                      >
                        <SelectTrigger className="text-gray-800">
                          <SelectValue placeholder="Chọn phường/xã" />
                        </SelectTrigger>
                        <SelectContent>
                          {wards.map((ward) => (
                            <SelectItem key={ward.id} value={String(ward.id)} className="text-gray-800 hover:bg-gray-100">
                              {ward.name}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <FormField
                control={form.control}
                name="specificAddress"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-700">Địa chỉ cụ thể</FormLabel>
                    <FormControl>
                      <Input placeholder="Nhập địa chỉ cụ thể" {...field} className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div>
                <div className="flex justify-between items-center mb-2">
                  <FormLabel className="text-gray-700">Email</FormLabel>
                  <Button
                    type="button"
                    variant="ghost"
                    onClick={() => appendEmail({ value: "" })}
                    className="text-sm text-gray-600"
                  >
                    <PlusCircle className="mr-2 h-4 w-4 text-gray-600" /> Thêm email
                  </Button>
                </div>
                {emailFields.map((field, index) => (
                  <div key={field.id} className="flex space-x-2 mb-2">
                    <FormField
                      control={form.control}
                      name={`emails.${index}.value`}
                      render={({ field }) => (
                        <FormItem className="flex-grow">
                          <FormControl>
                            <div className="flex items-center space-x-2">
                              <Input placeholder="Nhập email" {...field} className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500" />
                              {index > 0 && (
                                <Button
                                  type="button"
                                  variant="destructive"
                                  size="icon"
                                  onClick={() => removeEmail(index)}
                                  className="text-red-500 text-white"
                                >
                                  <Trash2 className="h-4 w-4 text-white" />
                                </Button>
                              )}
                            </div>
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                ))}
              </div>


              <div>
                <div className="flex justify-between items-center mb-2">
                  <FormLabel className="text-gray-700">Số điện thoại</FormLabel>
                  <Button
                    type="button"
                    variant="ghost"
                    onClick={() => appendPhone({ value: "" })}
                    className="text-sm text-gray-600"
                  >
                    <PlusCircle className="mr-2 h-4 w-4 text-gray-600" /> Thêm số điện thoại
                  </Button>
                </div>
                {phoneFields.map((field, index) => (
                  <div key={field.id} className="flex space-x-2 mb-2">
                    <FormField
                      control={form.control}
                      name={`phoneNumbers.${index}.value`}
                      render={({ field }) => (
                        <FormItem className="flex-grow">
                          <FormControl>
                            <div className="flex items-center space-x-2">
                              <Input placeholder="Nhập số điện thoại" {...field} className="bg-gray-50 text-gray-800 border-gray-300 focus:ring-blue-500 focus:border-blue-500" />
                              {index > 0 && (
                                <Button
                                  type="button"
                                  variant="destructive"
                                  size="icon"
                                  onClick={() => removePhone(index)}
                                  className="bg-red-500 text-white"
                                >
                                  <Trash2 className="h-4 w-4 text-white" />
                                </Button>
                              )}
                            </div>
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                ))}
              </div>

              <Button
                type="submit"
                className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg transition-colors duration-300"
              >
                Đăng Ký Ngay
              </Button>
              <Button
                type="button"
                className="w-full bg-gray-600 hover:bg-gray-700 text-white py-4 rounded-lg transition-all duration-300 flex items-center justify-center gap-2 text-lg"
                onClick={() => navigate('/')} // Quay về trang chủ
              >
                <FaHome size={24} className="text-white" /> {/* Icon "Trang chủ" */}
                Quay về trang chủ
              </Button>

            </form>
          </Form>
          {/* <ToastContainer
  position="top-center"
  autoClose={3000}
  hideProgressBar={false}
  newestOnTop={true}
  closeOnClick
  draggable
  transition="bounce" // Thêm hiệu ứng 'bounce'
/> */}

          {/* ))} */}
        </div>
      </div>

      {/* <ToastViewport />
     </ToastProvider> */}
    </>
  );
}

