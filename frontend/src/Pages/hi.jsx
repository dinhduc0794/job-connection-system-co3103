import LocationSearch from "./test";

const locations = [
    { province: "Hồ Chí Minh", city: "Quận Tân Phú", ward: "Hiệp Tân" },
    { province: "Hồ Chí Minh", city: "Quận 1", ward: "Bến Nghé" },
    // thêm các địa điểm khác
];

const MyComponent = () => {
    const handleLocationSelect = (location) => {
        console.log(location); // Xử lý khi chọn địa điểm
    };

    return (
        <LocationSearch
            locations={locations}
            onLocationSelect={handleLocationSelect}
        />
    );
};

export default MyComponent;