
INSERT INTO booking (id, total_amount, discount_amount, final_amount, status, transaction_id, customer_name,
                     customer_email, customer_phone, note, wallet_type, expired_at, type, is_fund_released, created_at,
                     updated_at, app_user_id, coupon_id, resale_post_id)
VALUES

-- Booking cho Invitation ID 1 (User Vũ, Ticket VIP)
(1, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lê Hoàng Vũ', 'hoangvu.le@gmail.com', '0974444444', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-03-02 08:30:00', '2026-03-02 08:30:00', 4, NULL, NULL),

-- Booking cho Invitation ID 29 (User Tâm, Ticket Standard)
(2, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phạm Minh Tâm', 'minhtam.it@gmail.com', '0965555555', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-16 09:15:00', '2026-03-16 09:15:00', 5, NULL, NULL),

-- Booking cho Invitation ID 30 (User Bảo, Ticket Standard)
(3, 0.0, 0.0, 0.0, 'PAID', NULL, 'Trần Quốc Bảo', 'bao.tq@coffeecode.com', '0866444444', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-17 10:00:00', '2026-03-17 10:00:00', 19, NULL, NULL),

-- Booking cho Invitation ID 31 (User Minh, Ticket Standard)
(4, 0.0, 0.0, 0.0, 'PAID', NULL, 'Nguyễn Nhật Minh', 'nhatminh.57@gmail.com', '0933000057', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-16 11:30:00', '2026-03-16 11:30:00', 57, NULL, NULL),

-- Booking cho Invitation ID 4 (User Bằng, Ticket Nhà Đầu Tư)
(5, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lý Bằng', 'lybang.88@gmail.com', '0966000088', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-03-16 09:00:00', '2026-03-16 09:00:00', 88, NULL, NULL),

-- Booking cho Invitation ID 32 (User Hùng, Ticket Nhà Đầu Tư)
(6, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hồ Xuân Hùng', 'xuanhung.69@gmail.com', '0944000069', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-02 14:00:00', '2026-04-02 14:00:00', 69, NULL, NULL),

-- Booking cho Invitation ID 33 (User Tuấn, Ticket Nhà Đầu Tư)
(7, 0.0, 0.0, 0.0, 'PAID', NULL, 'Võ Đình Tuấn', 'dinhtuan.65@gmail.com', '0944000065', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-03 08:45:00', '2026-04-03 08:45:00', 65, NULL, NULL),

-- Booking cho Invitation ID 7 (User Thảo, Ticket VVIP)
(8, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đặng Thu Thảo', 'thuthao.dang@gmail.com', '0948888888', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-16 11:00:00', '2026-03-16 11:00:00', 8, NULL, NULL),

-- Booking cho Invitation ID 34 (User Ly, Ticket VVIP Day 2)
(9, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đặng Cẩm ly', 'camly.66@gmail.com', '0944000066', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-03-21 09:00:00', '2026-03-21 09:00:00', 66, NULL, NULL),

-- Booking cho Invitation ID 35 (User Dũng, Ticket VVIP Day 2)
(10, 0.0, 0.0, 0.0, 'PAID', NULL, 'Bùi Quang Dũng', 'quangdung.51@gmail.com', '0933000051', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-22 15:30:00', '2026-03-22 15:30:00', 51, NULL, NULL),

-- Booking cho Invitation ID 36 (User Tùng, Ticket Combo 3 Day)
(11, 0.0, 0.0, 0.0, 'PAID', NULL, 'Bùi Thanh Tùng', 'thanhtung.67@gmail.com', '0988777666', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-23 11:00:00', '2026-03-23 11:00:00', 67, NULL, NULL),

-- Booking cho Invitation ID 37 (User Vũ, Ticket Combo 3 Day)
(12, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lê Hoàng Vũ', 'hoangvu.le@gmail.com', '0974444444', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-03-21 14:20:00', '2026-03-21 14:20:00', 4, NULL, NULL),

-- Booking cho Invitation ID 38 (User Hiếu, Ticket Combo 3 Day)
(13, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lê Trọng Hiếu', 'tronghieu.91@gmail.com', '0977000091', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-24 08:15:00', '2026-03-24 08:15:00', 91, NULL, NULL),

-- Booking cho Invitation ID 9 (User Hùng, Ticket CEO VIP)
(14, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hồ Xuân Hùng', 'xuanhung.69@gmail.com', '0944000069', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-11 14:20:00', '2026-03-11 14:20:00', 69, NULL, NULL),

-- Booking cho Invitation ID 39 (User Hùng, Ticket CEO VIP)
(15, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đặng Văn Hùng', 'vanhung.82@gmail.com', '0966000082', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-02 09:00:00', '2026-04-02 09:00:00', 82, NULL, NULL),

-- Booking cho Invitation ID 40 (User Bằng, Ticket CEO VIP)
(16, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lý Bằng', 'lybang.88@gmail.com', '0966000088', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-03 16:45:00', '2026-04-03 16:45:00', 88, NULL, NULL),

-- Booking cho Invitation ID 41 (User Tùng, Ticket Rock)
(17, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lê Đình Tùng', 'dinhtung.43@gmail.com', '0933000043', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 14:30:00', '2026-04-06 14:30:00', 43, NULL, NULL),

-- Booking cho Invitation ID 42 (User Hoàng, Ticket Rock)
(18, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phan Việt Hoàng', 'viethoang.63@gmail.com', '0944000063', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-07 09:15:00', '2026-04-07 09:15:00', 63, NULL, NULL),

-- Booking cho Invitation ID 13 (User Nhi, Ticket Fashion Show)
(19, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hoàng Yến Nhi', 'yennhi.77@gmail.com', '0955000077', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-03-06 15:30:00', '2026-03-06 15:30:00', 77, NULL, NULL),

-- Booking cho Invitation ID 43 (User Giang, Ticket Fashion Show)
(20, 0.0, 0.0, 0.0, 'PAID', NULL, 'Vũ Hương Giang', 'huonggiang.96@gmail.com', '0977000096', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-02 11:20:00', '2026-04-02 11:20:00', 96, NULL, NULL),

-- Booking cho Invitation ID 44 (User Bích, Ticket Fashion Show)
(21, 0.0, 0.0, 0.0, 'PAID', NULL, 'Trần Ngọc Bích', 'ngocbich.90@gmail.com', '0966000090', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-03 15:45:00', '2026-04-03 15:45:00', 90, NULL, NULL),

-- Booking cho Invitation ID 15 (User Mai, Ticket Food Fest)
(22, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hoàng Thanh Mai', 'thanhmai.foodie@yahoo.com', '0936666666', 'Nhận vé mời từ BTC',
 NULL, NULL, 'INVITE', false, '2026-03-11 08:30:00', '2026-03-11 08:30:00', 6, NULL, NULL),

-- Booking cho Invitation ID 45 (User Linh, Ticket Food Fest)
(23, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hoàng Mỹ Linh', 'mylinh.45@gmail.com', '0933000045', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 12:30:00', '2026-04-06 12:30:00', 45, NULL, NULL),

-- Booking cho Invitation ID 46 (User Anh, Ticket Food Fest)
(24, 0.0, 0.0, 0.0, 'PAID', NULL, 'Bùi Kim Anh', 'kimanh.83@gmail.com', '0966000083', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-07 09:10:00', '2026-04-07 09:10:00', 83, NULL, NULL),

-- Booking cho Invitation ID 47 (User Lan, Ticket Edtech)
(25, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đặng Phương Lan', 'phuonglan.50@gmail.com', '0933000050', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-07 14:20:00', '2026-04-07 14:20:00', 50, NULL, NULL),

-- Booking cho Invitation ID 48 (User Dũng, Ticket Edtech)
(26, 0.0, 0.0, 0.0, 'PAID', NULL, 'Trần Tiến Dũng', 'tiendung.74@gmail.com', '0955000074', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-07 16:45:00', '2026-04-07 16:45:00', 74, NULL, NULL),

-- Booking cho Invitation ID 18 (User Đạt, Ticket Coffee Expo)
(27, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lê Hữu Đạt', 'dat.wine@gmail.com', '0877888888', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-03-06 10:30:00', '2026-03-06 10:30:00', 33, NULL, NULL),

-- Booking cho Invitation ID 49 (User An, Ticket Coffee Expo)
(28, 0.0, 0.0, 0.0, 'PAID', NULL, 'Nguyễn Văn An', 'vanan.41@gmail.com', '0933000041', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-06 08:30:00', '2026-04-06 08:30:00', 41, NULL, NULL),

-- Booking cho Invitation ID 50 (User Vinh, Ticket Coffee Expo)
(29, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phạm Quang Vinh', 'quangvinh.76@gmail.com', '0955000076', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 11:20:00', '2026-04-06 11:20:00', 76, NULL, NULL),

-- Booking cho Invitation ID 51 (User Hà, Ticket Coffee Expo)
(30, 0.0, 0.0, 0.0, 'PAID', NULL, 'Võ Thu Hà', 'thuha.81@gmail.com', '0966000081', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-07 09:15:00', '2026-04-07 09:15:00', 81, NULL, NULL),

-- Booking cho Invitation ID 52 (User Ngân, Ticket Lễ Hội Thả Diều)
(31, 0.0, 0.0, 0.0, 'PAID', NULL, 'Vũ Kim Ngân', 'kimngan.64@gmail.com', '0944000064', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-06 14:30:00', '2026-04-06 14:30:00', 64, NULL, NULL),

-- Booking cho Invitation ID 53 (User Anh, Ticket Lễ Hội Thả Diều)
(32, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đặng Tú Anh', 'tuanh.98@gmail.com', '0977000098', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-07 08:20:00', '2026-04-07 08:20:00', 98, NULL, NULL),

-- Booking cho Invitation ID 54 (User Hùng, Ticket Danang Run)
(33, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phạm Tuấn Hùng', 'tuanhung.44@gmail.com', '0933000044', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-02 10:30:00', '2026-04-02 10:30:00', 44, NULL, NULL),

-- Booking cho Invitation ID 55 (User Tuấn, Ticket Danang Run)
(34, 0.0, 0.0, 0.0, 'PAID', NULL, 'Nguyễn Văn Tuấn', 'tuan.bike@gmail.com', '0877555555', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-03 08:15:00', '2026-04-03 08:15:00', 30, NULL, NULL),

-- Booking cho Invitation ID 56 (User Bảo, Ticket Danang Run)
(35, 0.0, 0.0, 0.0, 'PAID', NULL, 'Ngô Đình Bảo', 'dinhbao.86@gmail.com', '0966000086', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-04 15:40:00', '2026-04-04 15:40:00', 86, NULL, NULL),

-- Booking cho Invitation ID 58 (User Kiên, Ticket Bóng rổ)
(36, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hoàng Trung Kiên', 'trungkien.61@gmail.com', '0944000061', 'Nhận vé mời từ BTC',
 NULL, NULL, 'INVITE', false, '2026-04-06 09:30:00', '2026-04-06 09:30:00', 61, NULL, NULL),

-- Booking cho Invitation ID 59 (User Kiên, Ticket Bóng rổ)
(37, 0.0, 0.0, 0.0, 'PAID', NULL, 'Võ Hoàng Kiên', 'hoangkien.49@gmail.com', '0933000049', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 14:15:00', '2026-04-06 14:15:00', 49, NULL, NULL),

-- Booking cho Invitation ID 60 (User Long, Ticket Bóng rổ)
(38, 0.0, 0.0, 0.0, 'PAID', NULL, 'Võ Nhật Long', 'nhatlong.97@gmail.com', '0977000097', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-07 10:50:00', '2026-04-07 10:50:00', 97, NULL, NULL),

-- Booking cho Invitation ID 62 (User Huyền, Ticket Sự kiện rác)
(39, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đỗ Ngọc Huyền', 'ngochuyen.68@gmail.com', '0944000068', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-03 09:00:00', '2026-04-03 09:00:00', 68, NULL, NULL),

-- Booking cho Invitation ID 63 (User Ngọc, Ticket Sự kiện rác)
(40, 0.0, 0.0, 0.0, 'PAID', NULL, 'Hoàng Kim Ngọc', 'ngoc.hk@chodocu.vn', '0866999999', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-04 11:20:00', '2026-04-04 11:20:00', 24, NULL, NULL),

-- Booking cho Invitation ID 64 (User Đăng, Ticket Giao lưu điện ảnh)
(41, 0.0, 0.0, 0.0, 'PAID', NULL, 'Trương Minh Đăng', 'dang.kichnoi@gmail.com', '0888333333', 'Nhận vé mời từ BTC',
 NULL, NULL, 'INVITE', false, '2026-04-06 08:30:00', '2026-04-06 08:30:00', 38, NULL, NULL),

-- Booking cho Invitation ID 65 (User Phượng, Ticket Giao lưu điện ảnh)
(42, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phạm Đan Phượng', 'danphuong.92@gmail.com', '0977000092', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 14:45:00', '2026-04-06 14:45:00', 92, NULL, NULL),

-- Booking cho Invitation ID 66 (User Phụng, Ticket Giao lưu điện ảnh)
(43, 0.0, 0.0, 0.0, 'PAID', NULL, 'Vũ Trọng Phụng', 'trongphung.80@gmail.com', '0955000080', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-07 09:20:00', '2026-04-07 09:20:00', 80, NULL, NULL),

-- Booking cho Invitation ID 68 (User Dũng, Ticket Lễ hội Bia)
(44, 0.0, 0.0, 0.0, 'PAID', NULL, 'Bùi Quang Dũng', 'quangdung.51@gmail.com', '0933000051', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 09:10:00', '2026-04-06 09:10:00', 51, NULL, NULL),

-- Booking cho Invitation ID 69 (User Hoàng, Ticket Lễ hội Bia)
(45, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phan Việt Hoàng', 'viethoang.63@gmail.com', '0944000063', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 15:40:00', '2026-04-06 15:40:00', 63, NULL, NULL),

-- Booking cho Invitation ID 70 (User Đạt, Ticket Lễ hội Bia)
(46, 0.0, 0.0, 0.0, 'PAID', NULL, 'Huỳnh Bá Đạt', 'badat.78@gmail.com', '0955000078', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-07 10:20:00', '2026-04-07 10:20:00', 78, NULL, NULL),

-- Booking cho Invitation ID 71 (User EdTech, Ticket Diễn Đàn AI)
(47, 0.0, 0.0, 0.0, 'PAID', NULL, 'EduTech Hội Thảo', 'nghia.bt@edutech.vn', '0866888888', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-02 08:30:00', '2026-04-02 08:30:00', 23, NULL, NULL),

-- Booking cho Invitation ID 72 (User Tâm, Ticket Diễn Đàn AI)
(48, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đỗ Minh Tâm', 'minhtam.100@gmail.com', '0977000100', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-03 14:15:00', '2026-04-03 14:15:00', 100, NULL, NULL),

-- Booking cho Invitation ID 73 (User Bảo, Ticket Diễn Đàn AI)
(49, 0.0, 0.0, 0.0, 'PAID', NULL, 'Trần Quốc Bảo', 'bao.tq@coffeecode.com', '0866444444', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-04 09:50:00', '2026-04-04 09:50:00', 19, NULL, NULL),

-- Booking cho Invitation ID 74 (User Lan, Ticket Diễn Đàn AI)
(50, 0.0, 0.0, 0.0, 'PAID', NULL, 'Đặng Phương Lan', 'phuonglan.50@gmail.com', '0933000050', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-05 11:20:00', '2026-04-05 11:20:00', 50, NULL, NULL),

-- Booking cho Invitation ID 75 (User Minh, Ticket Hội chợ Mỹ thuật)
(51, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phạm Văn Minh', 'minh.pv@fineart.vn', '0866333333', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-06 09:15:00', '2026-04-06 09:15:00', 18, NULL, NULL),

-- Booking cho Invitation ID 76 (User Thảo, Ticket Hội chợ Mỹ thuật)
(52, 0.0, 0.0, 0.0, 'PAID', NULL, 'Ngô Bích Thảo', 'bichthao.54@gmail.com', '0933000054', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-07 08:40:00', '2026-04-07 08:40:00', 54, NULL, NULL),

-- Booking cho Invitation ID 77 (User Trí, Ticket Hội chợ Mỹ thuật)
(53, 0.0, 0.0, 0.0, 'PAID', NULL, 'Phan Đức Trí', 'ductri.47@gmail.com', '0933000047', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-07 14:50:00', '2026-04-07 14:50:00', 47, NULL, NULL),

-- Booking cho Invitation ID 78 (User Oanh, Ticket Cắm trại)
(54, 0.0, 0.0, 0.0, 'PAID', NULL, 'Lê Kiều Oanh', 'kieuoanh.75@gmail.com', '0955000075', 'Nhận vé mời từ BTC', NULL,
 NULL, 'INVITE', false, '2026-04-06 10:20:00', '2026-04-06 10:20:00', 75, NULL, NULL),

-- Booking cho Invitation ID 79 (User Bảo, Ticket Acoustic)
(55, 0.0, 0.0, 0.0, 'PAID', NULL, 'Trần Thị Bảo', 'thibao.42@gmail.com', '0933000042', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-06 15:30:00', '2026-04-06 15:30:00', 42, NULL, NULL),

-- Booking cho Invitation ID 80 (User Anh, Ticket FoodFest Thái)
(56, 0.0, 0.0, 0.0, 'PAID', NULL, 'Bùi Kim Anh', 'kimanh.83@gmail.com', '0966000083', 'Nhận vé mời từ BTC', NULL, NULL,
 'INVITE', false, '2026-04-07 11:15:00', '2026-04-07 11:15:00', 83, NULL, NULL);

-- =====================================================================
-- BẢNG 2: CẬP NHẬT BOOKING_ID VÀO BẢNG EVENT_INVITATION
-- =====================================================================
UPDATE event_invitation
SET booking_id = 1
WHERE id = 1;
UPDATE event_invitation
SET booking_id = 2
WHERE id = 29;
UPDATE event_invitation
SET booking_id = 3
WHERE id = 30;
UPDATE event_invitation
SET booking_id = 4
WHERE id = 31;
UPDATE event_invitation
SET booking_id = 5
WHERE id = 4;
UPDATE event_invitation
SET booking_id = 6
WHERE id = 32;
UPDATE event_invitation
SET booking_id = 7
WHERE id = 33;
UPDATE event_invitation
SET booking_id = 8
WHERE id = 7;
UPDATE event_invitation
SET booking_id = 9
WHERE id = 34;
UPDATE event_invitation
SET booking_id = 10
WHERE id = 35;
UPDATE event_invitation
SET booking_id = 11
WHERE id = 36;
UPDATE event_invitation
SET booking_id = 12
WHERE id = 37;
UPDATE event_invitation
SET booking_id = 13
WHERE id = 38;
UPDATE event_invitation
SET booking_id = 14
WHERE id = 9;
UPDATE event_invitation
SET booking_id = 15
WHERE id = 39;
UPDATE event_invitation
SET booking_id = 16
WHERE id = 40;
UPDATE event_invitation
SET booking_id = 17
WHERE id = 41;
UPDATE event_invitation
SET booking_id = 18
WHERE id = 42;
UPDATE event_invitation
SET booking_id = 19
WHERE id = 13;
UPDATE event_invitation
SET booking_id = 20
WHERE id = 43;
UPDATE event_invitation
SET booking_id = 21
WHERE id = 44;
UPDATE event_invitation
SET booking_id = 22
WHERE id = 15;
UPDATE event_invitation
SET booking_id = 23
WHERE id = 45;
UPDATE event_invitation
SET booking_id = 24
WHERE id = 46;
UPDATE event_invitation
SET booking_id = 25
WHERE id = 47;
UPDATE event_invitation
SET booking_id = 26
WHERE id = 48;
UPDATE event_invitation
SET booking_id = 27
WHERE id = 18;
UPDATE event_invitation
SET booking_id = 28
WHERE id = 49;
UPDATE event_invitation
SET booking_id = 29
WHERE id = 50;
UPDATE event_invitation
SET booking_id = 30
WHERE id = 51;
UPDATE event_invitation
SET booking_id = 31
WHERE id = 52;
UPDATE event_invitation
SET booking_id = 32
WHERE id = 53;
UPDATE event_invitation
SET booking_id = 33
WHERE id = 54;
UPDATE event_invitation
SET booking_id = 34
WHERE id = 55;
UPDATE event_invitation
SET booking_id = 35
WHERE id = 56;
UPDATE event_invitation
SET booking_id = 36
WHERE id = 58;
UPDATE event_invitation
SET booking_id = 37
WHERE id = 59;
UPDATE event_invitation
SET booking_id = 38
WHERE id = 60;
UPDATE event_invitation
SET booking_id = 39
WHERE id = 62;
UPDATE event_invitation
SET booking_id = 40
WHERE id = 63;
UPDATE event_invitation
SET booking_id = 41
WHERE id = 64;
UPDATE event_invitation
SET booking_id = 42
WHERE id = 65;
UPDATE event_invitation
SET booking_id = 43
WHERE id = 66;
UPDATE event_invitation
SET booking_id = 44
WHERE id = 68;
UPDATE event_invitation
SET booking_id = 45
WHERE id = 69;
UPDATE event_invitation
SET booking_id = 46
WHERE id = 70;
UPDATE event_invitation
SET booking_id = 47
WHERE id = 71;
UPDATE event_invitation
SET booking_id = 48
WHERE id = 72;
UPDATE event_invitation
SET booking_id = 49
WHERE id = 73;
UPDATE event_invitation
SET booking_id = 50
WHERE id = 74;
UPDATE event_invitation
SET booking_id = 51
WHERE id = 75;
UPDATE event_invitation
SET booking_id = 52
WHERE id = 76;
UPDATE event_invitation
SET booking_id = 53
WHERE id = 77;
UPDATE event_invitation
SET booking_id = 54
WHERE id = 78;
UPDATE event_invitation
SET booking_id = 55
WHERE id = 79;
UPDATE event_invitation
SET booking_id = 56
WHERE id = 80;