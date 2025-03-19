import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message, Space, Typography,Checkbox } from 'antd';
import { EditOutlined, DeleteOutlined, UserAddOutlined ,PhoneOutlined, LockOutlined} from '@ant-design/icons';
import axios from 'axios';
import styled from 'styled-components';
import TokenManager from '../utils/tokenManager';

const { Title } = Typography;
//const API_URL = '/api';
const StyledDashboard = styled.div`
  padding: 24px;
  
  .header {
    margin-bottom: 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .ant-table {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  }

  .ant-btn-primary {
    background: #1890ff;
    border-radius: 6px;
    height: 40px;
    font-size: 16px;
  }
`;

const Dashboard = () => {
  const [users, setUsers] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [form] = Form.useForm();

  // Lấy danh sách users
  const fetchUsers = async () => {
  try {
    // const encryptedToken = localStorage.getItem('auth_token');
    // const decryptedToken = TokenManager.decrypt(encryptedToken);
    const token = TokenManager.getToken(); // Lấy token từ TokenManager
    //console.log('Token being sent:', token.value);
    
    const response = await fetch(`/admins/applicants`, {
      headers: {
        'Authorization': `Bearer ${token.value}`,
        'Content-Type': 'application/json'
      },
      credentials: 'include'
    });
    // const tokenString = localStorage.getItem('auth_token');
    // const tokenData = JSON.parse(tokenString);
    
    //console.log('Token string from localStorage:', tokenString);
    //console.log('Parsed token data:', tokenData);

    // if (!tokenData) {
    //   throw new Error('Missing token data!');
    // }

    // const response = await fetch(`/users`, {
    //   headers: {
    //     'Authorization': `Bearer ${tokenData.value}`, // Sử dụng token.value
    //     'Content-Type': 'application/json'
    //   },
    //   credentials: 'include'
    // });


if (!response.ok) {
  throw new Error(`Request failed with status ${response.status}`);
}

    console.log('Response status:', response.status);
    if (!response.ok) {
      const errorData = await response.text();
      console.error('Error response:', errorData);
      throw new Error(errorData || 'Unauthorized');
    }
    
    const data = await response.json();
    console.log('Received data:', data);
    setUsers(data);
  } catch (error) {
    console.error('Full error:', error);
    message.error('Không thể tải danh sách người dùng');
  }
};
  useEffect(() => {
    fetchUsers();
  }, []);

  // Xử lý thêm/sửa user
  const handleSubmit = async (values) => {
    try {
      const token = TokenManager.getToken().value;
      if (editingUser) {
        const response = await fetch(`http://localhost:8080/users/users/${editingUser.id}`, {
          method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(values)
        });
        const data = await response.json();
        if (data) {
          message.success('Cập nhật thành công!');
        } else {
          throw new Error('Không tìm thấy người dùng');
        }
      } else {
        const queryParams = new URLSearchParams({
          phone: values.phone,
          email: values.email
        }).toString();
        
        const response = await fetch(`http://localhost:8080/users/users?${queryParams}`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(values)
        });
        const data = await response.json();
        if (data) {
          message.success('Thêm mới thành công!');
        } else {
          throw new Error('Tài khoản đã tồn tại');
        }
      }
      setModalVisible(false);
      fetchUsers();
      form.resetFields();
    } catch (error) {
      message.error(error.message || 'Có lỗi xảy ra!');
    }
  };
  // Xử lý xóa user
  const handleDelete = async (id) => {
    try {
      const token = TokenManager.getToken().value;
      const response = await fetch(`http://localhost:8080/users/users/${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const data = await response.json();
      if (data?.message === "Delete successfully") {
        message.success('Xóa thành công!');
        fetchUsers();
      }
    } catch (error) {
      message.error('Có lỗi xảy ra khi xóa!');
    }
  };


  const columns = [
    {
      title: 'Username',
      dataIndex: 'username',
      key: 'username',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Số điện thoại',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: 'Vai trò',
      dataIndex: 'role',
      key: 'role',
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Địa chỉ',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Ảnh',
      dataIndex: 'image',
      key: 'image',
    },
    {
      title: 'Trạng thái',
      key: 'status',
      render: (_, record) => (
        <Space>
          {record.isPublic ? 'Công khai' : 'Riêng tư'}
          {record.isBanned ? ' | Đã bị cấm' : ''}
        </Space>
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button 
            type="primary" 
            icon={<EditOutlined />}
            onClick={() => {
              setEditingUser(record);
              form.setFieldsValue(record);
              setModalVisible(true);
            }}
          >
          Sửa
          </Button>
          <Button 
            danger 
            icon={<DeleteOutlined />}
            onClick={() => {
              Modal.confirm({
                title: 'Xác nhận xóa',
                content: 'Bạn có chắc chắn muốn xóa người dùng này?',
                onOk: () => handleDelete(record.id)
              });
            }}
          >
            Xóa
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <StyledDashboard>
      <div className="header">
        <Title level={2}>Quản lý người dùng</Title>
        <Button 
          type="primary" 
          icon={<UserAddOutlined />}
          onClick={() => {
            setEditingUser(null);
            form.resetFields();
            setModalVisible(true);
          }}
        >
          Thêm người dùng
        </Button>
      </div>

      <Table 
        columns={columns} 
        dataSource={users}
        rowKey="id"
        pagination={{ pageSize: 10 }}
      />

      <Modal
        title={editingUser ? "Sửa người dùng" : "Thêm người dùng mới"}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <Form
          form={form}
          onFinish={handleSubmit}
          layout="vertical"
        >
          <Form.Item
            name="username"
            label="Username"
            rules={[{ required: true, message: 'Vui lòng nhập username!' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Vui lòng nhập email!' },
              { type: 'email', message: 'Email không hợp lệ!' }
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
    name="description"
    label="Mô tả"
  >
    <Input.TextArea 
      placeholder="Nhập mô tả"
      style={{ borderRadius: '6px' }}
    />
  </Form.Item>

  <Form.Item
    name="address"
    label="Địa chỉ"
  >
    <Input 
      placeholder="Nhập địa chỉ"
      style={{ borderRadius: '6px', height: '40px' }}
    />
  </Form.Item>

  <Form.Item
    name="image"
    label="Ảnh"
    initialValue="bg.jpg"
  >
    <Input 
      placeholder="Nhập đường dẫn ảnh"
      style={{ borderRadius: '6px', height: '40px' }}
    />
  </Form.Item>

  <Form.Item
    name="isPublic"
    valuePropName="checked"
    initialValue={true}
  >
    <Checkbox>Công khai tài khoản</Checkbox>
  </Form.Item>

  {editingUser && (
    <Form.Item
      name="isBanned"
      valuePropName="checked"
      initialValue={false}
    >
      <Checkbox>Cấm tài khoản</Checkbox>
    </Form.Item>
  )}
          <Form.Item
          name="phone"
          label="Số điện thoại"
          rules={[
            { required: true, message: 'Vui lòng nhập số điện thoại!' },
            { pattern: /^[0-9]{10}$/, message: 'Số điện thoại không hợp lệ!' }
          ]}
        >
          <Input 
            prefix={<PhoneOutlined style={{ color: '#bfbfbf' }} />}
            placeholder="Nhập số điện thoại"
            maxLength={10}
            style={{ borderRadius: '6px', height: '40px' }}
          />
        </Form.Item>
          <Form.Item
            name="role"
            label="Loại tài khoản"
            rules={[{ required: true, message: 'Vui lòng chọn loại tài khoản!' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
  name="password"
  label="Mật khẩu"
  rules={[
    { required: !editingUser, message: 'Vui lòng nhập mật khẩu!' },
    { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' }
  ]}
>
  <Input.Password 
    prefix={<LockOutlined style={{ color: '#bfbfbf' }} />}
    placeholder="Nhập mật khẩu"
    style={{ borderRadius: '6px', height: '40px' }}
  />
</Form.Item>
<Form.Item style={{ marginTop: '24px', textAlign: 'right' }}>
  <Space size="middle">
    <Button 
      onClick={() => setModalVisible(false)}
      style={{ 
        borderRadius: '6px',
        height: '40px',
        padding: '0 24px'
      }}
    >
      Hủy
    </Button>
    <Button 
      type="primary" 
      htmlType="submit"
      style={{ 
        borderRadius: '6px',
        height: '40px',
        padding: '0 24px',
        fontWeight: 500
      }}
    >
      {editingUser ? 'Cập nhật' : 'Thêm mới'}
    </Button>
  </Space>
</Form.Item>
        </Form>
      </Modal>
    </StyledDashboard>
  );
};

export default Dashboard;