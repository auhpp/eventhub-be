
INSERT INTO event_invitation (id, status, token, email, message, initial_quantity, rejection_message, expired_at, created_at, updated_at, app_user_id, ticket_id, booking_id) VALUES

-- Nhóm 1: Sự kiện 1 - VIP & Standard (Ticket ID = 2, 3)
(1, 'ACCEPTED', 'tk-vip1-001', 'hoangvu.le@gmail.com', 'Thân mời anh Vũ tham dự hàng ghế VIP sự kiện AI.', 2, NULL, '2026-03-05 10:00:00', '2026-03-01 10:00:00', '2026-03-02 08:30:00', 4, 3, NULL),
(2, 'PENDING', 'tk-vip1-002', 'khachvip@example.com', 'Mời tham gia tiệc tối Networking dành cho VIP.', 1, NULL, '2026-05-10 12:00:00', '2026-04-01 09:00:00', '2026-04-01 09:00:00', NULL, 3, NULL),
(3, 'REJECTED', 'tk-vip1-003', 'banron@example.com', 'Thân mời đối tác tham dự.', 2, 'Rất tiếc tôi đang công tác tại nước ngoài.', '2026-03-20 12:00:00', '2026-03-10 10:00:00', '2026-03-11 15:00:00', NULL, 3, NULL),
(29, 'ACCEPTED', 'tk-std1-001', 'minhtam.it@gmail.com', 'Tặng bạn vé mời tham dự hội thảo AI 2025.', 1, NULL, '2026-04-20 12:00:00', '2026-03-15 08:00:00', '2026-03-16 09:15:00', 5, 2, NULL),
(30, 'ACCEPTED', 'tk-std1-002', 'bao.tq@coffeecode.com', 'Kính mời diễn giả tech.', 2, NULL, '2026-04-20 12:00:00', '2026-03-15 08:00:00', '2026-03-17 10:00:00', 19, 2, NULL),
(31, 'ACCEPTED', 'tk-std1-003', 'nhatminh.57@gmail.com', 'Vé mời tham quan triển lãm AI.', 1, NULL, '2026-04-20 12:00:00', '2026-03-15 08:00:00', '2026-03-16 11:30:00', 57, 2, NULL),

-- Nhóm 2: Sự kiện 2 - Vé Nhà Đầu Tư (Ticket ID = 5, Quota = 20)
(4, 'ACCEPTED', 'tk-shark-001', 'lybang.88@gmail.com', 'Mời anh Bằng - Nhà đầu tư thiên thần tham gia chấm điểm các dự án.', 1, NULL, '2026-03-20 10:00:00', '2026-03-15 10:00:00', '2026-03-16 09:00:00', 88, 5, NULL),
(5, 'EXPIRED', 'tk-shark-002', 'sharktank@example.com', 'Kính mời đại diện Quỹ Đầu Tư.', 3, NULL, '2026-03-25 23:59:59', '2026-03-20 09:00:00', '2026-03-26 00:00:01', NULL, 5, NULL),
(6, 'PENDING', 'tk-shark-003', 'investor_vip@example.com', 'Mời tham gia Pitching Day.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 08:30:00', '2026-04-05 08:30:00', NULL, 5, NULL),
(32, 'ACCEPTED', 'tk-shark-004', 'xuanhung.69@gmail.com', 'Mời Doanh nhân trẻ tham dự Pitching.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-02 14:00:00', 69, 5, NULL),
(33, 'ACCEPTED', 'tk-shark-005', 'dinhtuan.65@gmail.com', 'Kính mời tham dự tìm kiếm Startup tiềm năng.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-03 08:45:00', 65, 5, NULL),

-- Nhóm 3: Sự kiện 5 & 6 - VMF (Ticket ID = 11, 13, 14)
(7, 'ACCEPTED', 'tk-vmf-001', 'thuthao.dang@gmail.com', 'Tặng vé VVIP cho Kols âm nhạc.', 2, NULL, '2026-03-20 10:00:00', '2026-03-15 09:00:00', '2026-03-16 11:00:00', 8, 11, NULL),
(8, 'PENDING', 'tk-vmf-002', 'nhataitro@vmf.com', 'Vé VVIP tri ân Nhà Tài Trợ.', 5, NULL, '2026-05-15 23:59:59', '2026-04-01 10:00:00', '2026-04-01 10:00:00', NULL, 11, NULL),
(34, 'ACCEPTED', 'tk-vmf-003', 'camly.66@gmail.com', 'Mời bạn tham dự đêm nhạc biển.', 1, NULL, '2026-05-01 23:59:59', '2026-03-20 10:00:00', '2026-03-21 09:00:00', 66, 13, NULL),
(35, 'ACCEPTED', 'tk-vmf-004', 'quangdung.51@gmail.com', 'Tặng bạn vé trải nghiệm VVIP.', 1, NULL, '2026-05-01 23:59:59', '2026-03-20 10:00:00', '2026-03-22 15:30:00', 51, 13, NULL),
(36, 'ACCEPTED', 'tk-vmf-005', 'thanhtung.67@gmail.com', 'Tri ân đối tác, vé combo 3 ngày.', 2, NULL, '2026-05-01 23:59:59', '2026-03-20 10:00:00', '2026-03-23 11:00:00', 67, 14, NULL),
(37, 'ACCEPTED', 'tk-vmf-006', 'hoangvu.le@gmail.com', 'Vé tặng thành viên tích cực cộng đồng.', 2, NULL, '2026-05-01 23:59:59', '2026-03-20 10:00:00', '2026-03-21 14:20:00', 4, 14, NULL),
(38, 'ACCEPTED', 'tk-vmf-007', 'tronghieu.91@gmail.com', 'Mời bạn tham dự chuỗi sự kiện VMF.', 1, NULL, '2026-05-01 23:59:59', '2026-03-20 10:00:00', '2026-03-24 08:15:00', 91, 14, NULL),

-- Nhóm 4: Sự kiện 9 - CEO Networking (Ticket ID = 18, Quota = 20)
(9, 'ACCEPTED', 'tk-ceo-001', 'xuanhung.69@gmail.com', 'Mời Doanh nhân trẻ tham gia Networking.', 1, NULL, '2026-03-15 10:00:00', '2026-03-10 09:00:00', '2026-03-11 14:20:00', 69, 18, NULL),
(10, 'PENDING', 'tk-ceo-002', 'giamdoc@company.vn', 'Mời đại diện Ban Giám Đốc.', 2, NULL, '2026-04-20 10:00:00', '2026-04-01 08:00:00', '2026-04-01 08:00:00', NULL, 18, NULL),
(11, 'REJECTED', 'tk-ceo-003', 'busi_partner@company.vn', 'Mời đối tác thân thiết.', 1, 'Trùng lịch họp HĐQT.', '2026-04-15 10:00:00', '2026-03-20 09:00:00', '2026-03-25 10:00:00', NULL, 18, NULL),
(39, 'ACCEPTED', 'tk-ceo-004', 'vanhung.82@gmail.com', 'Mời chuyên gia BĐS tham dự giao lưu.', 1, NULL, '2026-04-20 10:00:00', '2026-04-01 08:00:00', '2026-04-02 09:00:00', 82, 18, NULL),
(40, 'ACCEPTED', 'tk-ceo-005', 'lybang.88@gmail.com', 'Thân mời tham dự tiệc tối CEO.', 1, NULL, '2026-04-20 10:00:00', '2026-04-01 08:00:00', '2026-04-03 16:45:00', 88, 18, NULL),

-- Nhóm 5: Sự kiện 14 - Rock Xuyên Đêm (Ticket ID = 24, Quota = 10)
(12, 'PENDING', 'tk-rock-001', 'rock_fan@example.com', 'Tặng vé tham dự đêm nhạc Indie Rock.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 10:00:00', '2026-04-05 10:00:00', NULL, 24, NULL),
(41, 'ACCEPTED', 'tk-rock-002', 'dinhtung.43@gmail.com', 'Vé tặng dành cho thành viên VIP.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 10:00:00', '2026-04-06 14:30:00', 43, 24, NULL),
(42, 'ACCEPTED', 'tk-rock-003', 'viethoang.63@gmail.com', 'Tri ân khách hàng thân thiết.', 1, NULL, '2026-05-01 10:00:00', '2026-04-05 10:00:00', '2026-04-07 09:15:00', 63, 24, NULL),

-- Nhóm 6: Sự kiện 17 - Fashion Show Mùa Thu (Ticket ID = 43, Quota = 10)
(13, 'ACCEPTED', 'tk-fashion-001', 'yennhi.77@gmail.com', 'Mời KOL thời trang tham dự hàng ghế Front Row.', 2, NULL, '2026-03-10 10:00:00', '2026-03-05 09:00:00', '2026-03-06 15:30:00', 77, 43, NULL),
(14, 'EXPIRED', 'tk-fashion-002', 'model@fashion.com', 'Mời Model dự sự kiện.', 1, NULL, '2026-03-20 10:00:00', '2026-03-15 09:00:00', '2026-03-20 10:00:01', NULL, 43, NULL),
(43, 'ACCEPTED', 'tk-fashion-003', 'huonggiang.96@gmail.com', 'Mời dự lễ ra mắt bộ sưu tập mới.', 1, NULL, '2026-04-10 10:00:00', '2026-04-01 09:00:00', '2026-04-02 11:20:00', 96, 43, NULL),
(44, 'ACCEPTED', 'tk-fashion-004', 'ngocbich.90@gmail.com', 'Kính mời chuyên gia Makeup dự sự kiện.', 2, NULL, '2026-04-10 10:00:00', '2026-04-01 09:00:00', '2026-04-03 15:45:00', 90, 43, NULL),

-- Nhóm 7: Sự kiện 26 - Lễ Hội Ẩm Thực (Ticket ID = 50, Quota = 20)
(15, 'ACCEPTED', 'tk-food-001', 'thanhmai.foodie@yahoo.com', 'Mời Food Reviewer đến dùng thử các món ăn.', 2, NULL, '2026-03-15 10:00:00', '2026-03-10 09:00:00', '2026-03-11 08:30:00', 6, 50, NULL),
(16, 'PENDING', 'tk-food-002', 'vlogger_amthuc@gmail.com', 'Vé mời dành cho Vlogger.', 4, NULL, '2026-05-10 10:00:00', '2026-04-05 09:00:00', '2026-04-05 09:00:00', NULL, 50, NULL),
(45, 'ACCEPTED', 'tk-food-003', 'mylinh.45@gmail.com', 'Mời bạn tham dự Lễ hội ẩm thực đường phố.', 2, NULL, '2026-05-10 10:00:00', '2026-04-05 09:00:00', '2026-04-06 12:30:00', 45, 50, NULL),
(46, 'ACCEPTED', 'tk-food-004', 'kimanh.83@gmail.com', 'Thân mời tham dự sự kiện buffet ngọt.', 1, NULL, '2026-05-10 10:00:00', '2026-04-05 09:00:00', '2026-04-07 09:10:00', 83, 50, NULL),

-- Nhóm 8: Sự kiện 31 - EdTech Summit (Ticket ID = 67, Quota = 10)
(17, 'PENDING', 'tk-edtech-001', 'giaovien@truonghoc.edu.vn', 'Mời thầy/cô tham dự diễn đàn giáo dục AI.', 1, NULL, '2026-04-20 10:00:00', '2026-04-06 09:00:00', '2026-04-06 09:00:00', NULL, 67, NULL),
(47, 'ACCEPTED', 'tk-edtech-002', 'phuonglan.50@gmail.com', 'Mời đại diện trường mầm non tham dự.', 2, NULL, '2026-04-20 10:00:00', '2026-04-06 09:00:00', '2026-04-07 14:20:00', 50, 67, NULL),
(48, 'ACCEPTED', 'tk-edtech-003', 'tiendung.74@gmail.com', 'Vé mời dành cho phòng nhân sự và đào tạo.', 2, NULL, '2026-04-20 10:00:00', '2026-04-06 09:00:00', '2026-04-07 16:45:00', 74, 67, NULL),

-- Nhóm 9: Sự kiện 45 - Coffee Expo (Ticket ID = 105, Quota = 50)
(18, 'ACCEPTED', 'tk-coffee-001', 'dat.wine@gmail.com', 'Vé mời chuyên gia thưởng thức cà phê.', 2, NULL, '2026-03-10 10:00:00', '2026-03-05 09:00:00', '2026-03-06 10:30:00', 33, 105, NULL),
(19, 'PENDING', 'tk-coffee-002', 'barista_chuyennghiep@gmail.com', 'Mời Giám Khảo Barista.', 5, NULL, '2026-05-01 10:00:00', '2026-04-05 09:00:00', '2026-04-05 09:00:00', NULL, 105, NULL),
(49, 'ACCEPTED', 'tk-coffee-003', 'vanan.41@gmail.com', 'Thân mời tham dự triển lãm Coffee Expo.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 09:00:00', '2026-04-06 08:30:00', 41, 105, NULL),
(50, 'ACCEPTED', 'tk-coffee-004', 'quangvinh.76@gmail.com', 'Tặng vé khách mời sự kiện F&B.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 09:00:00', '2026-04-06 11:20:00', 76, 105, NULL),
(51, 'ACCEPTED', 'tk-coffee-005', 'thuha.81@gmail.com', 'Mời đối tác ngành dịch vụ đồ uống.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 09:00:00', '2026-04-07 09:15:00', 81, 105, NULL),

-- Nhóm 10: Lễ Hội Thả Diều (Ticket ID = 114, Quota = 20)
(20, 'REJECTED', 'tk-kite-001', 'doithu@example.com', 'Mời khách mời danh dự.', 4, 'Bận lịch trình khác.', '2026-03-10 10:00:00', '2026-03-01 09:00:00', '2026-03-05 14:00:00', NULL, 114, NULL),
(52, 'ACCEPTED', 'tk-kite-002', 'kimngan.64@gmail.com', 'Tặng gia đình vé tham dự Lễ hội diều.', 4, NULL, '2026-04-20 10:00:00', '2026-04-05 09:00:00', '2026-04-06 14:30:00', 64, 114, NULL),
(53, 'ACCEPTED', 'tk-kite-003', 'tuanh.98@gmail.com', 'Mời mẹ và bé tham gia sự kiện cuối tuần.', 3, NULL, '2026-04-20 10:00:00', '2026-04-05 09:00:00', '2026-04-07 08:20:00', 98, 114, NULL),

-- Nhóm 11: Danang Tech Run (Ticket 33 - Bib chạy 21km, Quota 20)
(54, 'ACCEPTED', 'tk-run-001', 'tuanhung.44@gmail.com', 'Tặng vé BIB cho Runner có thành tích xuất sắc.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-02 10:30:00', 44, 33, NULL),
(55, 'ACCEPTED', 'tk-run-002', 'tuan.bike@gmail.com', 'Mời tham gia giải Marathon mùa hè.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-03 08:15:00', 30, 33, NULL),
(56, 'ACCEPTED', 'tk-run-003', 'dinhbao.86@gmail.com', 'Vé tặng từ nhà tài trợ giải chạy.', 2, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-04 15:40:00', 86, 33, NULL),
(57, 'PENDING', 'tk-run-004', 'clb_runner_danang@run.vn', 'Vé mời cho CLB chạy bộ Đà Nẵng.', 4, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-01 09:00:00', NULL, 33, NULL),

-- Nhóm 12: Sự kiện Giải Bóng Rổ Sinh Viên (Ticket 75 - Khán Đài A, Quota 50)
(58, 'ACCEPTED', 'tk-basket-001', 'trungkien.61@gmail.com', 'Mời cựu sinh viên yêu thể thao xem giải.', 2, NULL, '2026-04-20 10:00:00', '2026-04-05 08:00:00', '2026-04-06 09:30:00', 61, 75, NULL),
(59, 'ACCEPTED', 'tk-basket-002', 'hoangkien.49@gmail.com', 'Vé VIP khán đài A cho sinh viên năm nhất.', 2, NULL, '2026-04-20 10:00:00', '2026-04-05 08:00:00', '2026-04-06 14:15:00', 49, 75, NULL),
(60, 'ACCEPTED', 'tk-basket-003', 'nhatlong.97@gmail.com', 'Mời tham dự cổ vũ đội tuyển trường.', 2, NULL, '2026-04-20 10:00:00', '2026-04-05 08:00:00', '2026-04-07 10:50:00', 97, 75, NULL),
(61, 'PENDING', 'tk-basket-004', 'hoidong_trongtai@sport.vn', 'Vé mời khu vực Ban giám khảo / Trọng tài.', 10, NULL, '2026-04-20 10:00:00', '2026-04-05 08:00:00', '2026-04-05 08:00:00', NULL, 75, NULL),

-- Nhóm 13: Hội Chợ Không Nhựa Một Ngày (Ticket 84 - Quota 5)
(62, 'ACCEPTED', 'tk-noplastic-001', 'ngochuyen.68@gmail.com', 'Thân mời đại sứ dự án tham dự sự kiện Sống Xanh.', 1, NULL, '2026-04-15 10:00:00', '2026-04-02 08:00:00', '2026-04-03 09:00:00', 68, 84, NULL),
(63, 'ACCEPTED', 'tk-noplastic-002', 'ngoc.hk@chodocu.vn', 'Vé mời đặc biệt dành cho Blogger môi trường.', 1, NULL, '2026-04-15 10:00:00', '2026-04-02 08:00:00', '2026-04-04 11:20:00', 24, 84, NULL),

-- Nhóm 14: Sự Kiện Khách Mời VIP (Ticket 44 - Quota 50) - Giao lưu Đạo Diễn Trẻ
(64, 'ACCEPTED', 'tk-movie-001', 'dang.kichnoi@gmail.com', 'Thân mời sinh viên SKĐA tham dự.', 2, NULL, '2026-04-20 10:00:00', '2026-04-05 09:00:00', '2026-04-06 08:30:00', 38, 44, NULL),
(65, 'ACCEPTED', 'tk-movie-002', 'danphuong.92@gmail.com', 'Mời tham gia giao lưu nghệ thuật.', 2, NULL, '2026-04-20 10:00:00', '2026-04-05 09:00:00', '2026-04-06 14:45:00', 92, 44, NULL),
(66, 'ACCEPTED', 'tk-movie-003', 'trongphung.80@gmail.com', 'Thư mời nhà văn chia sẻ về kịch bản.', 1, NULL, '2026-04-20 10:00:00', '2026-04-05 09:00:00', '2026-04-07 09:20:00', 80, 44, NULL),
(67, 'REJECTED', 'tk-movie-004', 'daodien_vip@movie.vn', 'Mời Đạo diễn gạo cội.', 2, 'Kẹt lịch bấm máy phim mới.', '2026-04-20 10:00:00', '2026-04-05 09:00:00', '2026-04-06 10:00:00', NULL, 44, NULL),

-- Nhóm 15: Lễ Hội Bia Craft (Ticket 50 - Quota 20)
(68, 'ACCEPTED', 'tk-beer-001', 'quangdung.51@gmail.com', 'Mời bạn tham dự đêm hội Craft Beer & BBQ.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 08:00:00', '2026-04-06 09:10:00', 51, 50, NULL),
(69, 'ACCEPTED', 'tk-beer-002', 'viethoang.63@gmail.com', 'Vé mời khu vực VIP Lễ hội bia.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 08:00:00', '2026-04-06 15:40:00', 63, 50, NULL),
(70, 'ACCEPTED', 'tk-beer-003', 'badat.78@gmail.com', 'Mời tham gia sự kiện ẩm thực cuối tuần.', 2, NULL, '2026-05-01 10:00:00', '2026-04-05 08:00:00', '2026-04-07 10:20:00', 78, 50, NULL),

-- Nhóm 16: Diễn đàn AI trong Giáo Dục (Ticket 51 - Quota 50)
(71, 'ACCEPTED', 'tk-ai-edu-001', 'nghia.bt@edutech.vn', 'Vé mời chuyên gia tham gia tọa đàm.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-02 08:30:00', 23, 51, NULL),
(72, 'ACCEPTED', 'tk-ai-edu-002', 'minhtam.100@gmail.com', 'Mời bạn sinh viên yêu công nghệ tham dự.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-03 14:15:00', 100, 51, NULL),
(73, 'ACCEPTED', 'tk-ai-edu-003', 'bao.tq@coffeecode.com', 'Vé mời lập trình viên tham gia thảo luận AI.', 2, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-04 09:50:00', 19, 51, NULL),
(74, 'ACCEPTED', 'tk-ai-edu-004', 'phuonglan.50@gmail.com', 'Vé mời giáo viên cập nhật xu hướng công nghệ.', 1, NULL, '2026-05-01 10:00:00', '2026-04-01 09:00:00', '2026-04-05 11:20:00', 50, 51, NULL),

-- Nhóm 17: Hội chợ thiết kế nghệ thuật (Ticket 42 - Quota 10)
(75, 'ACCEPTED', 'tk-design-001', 'minh.pv@fineart.vn', 'Mời họa sĩ tham dự hội chợ thiết kế.', 1, NULL, '2026-05-10 10:00:00', '2026-04-05 08:00:00', '2026-04-06 09:15:00', 18, 42, NULL),
(76, 'ACCEPTED', 'tk-design-002', 'bichthao.54@gmail.com', 'Vé mời dành cho nhà thiết kế đồ họa.', 2, NULL, '2026-05-10 10:00:00', '2026-04-05 08:00:00', '2026-04-07 08:40:00', 54, 42, NULL),
(77, 'ACCEPTED', 'tk-design-003', 'ductri.47@gmail.com', 'Mời nhiếp ảnh gia tác nghiệp tại triển lãm.', 1, NULL, '2026-05-10 10:00:00', '2026-04-05 08:00:00', '2026-04-07 14:50:00', 47, 42, NULL),

-- Nhóm 18: Các ticket khác
(78, 'ACCEPTED', 'tk-camp-002', 'kieuoanh.75@gmail.com', 'Vé tặng thành viên kỳ cựu nhóm cắm trại.', 2, NULL, '2026-05-10 10:00:00', '2026-04-05 09:00:00', '2026-04-06 10:20:00', 75, 48, NULL),
(79, 'ACCEPTED', 'tk-indie-003', 'thibao.42@gmail.com', 'Tặng vé đêm nhạc Indie cho Fan cứng.', 2, NULL, '2026-05-10 10:00:00', '2026-04-05 09:00:00', '2026-04-06 15:30:00', 42, 24, NULL),
(80, 'ACCEPTED', 'tk-food-005', 'kimanh.83@gmail.com', 'Vé mời tham dự Lễ hội Ẩm thực Thái Lan.', 2, NULL, '2026-05-10 10:00:00', '2026-04-05 09:00:00', '2026-04-07 11:15:00', 83, 39, NULL);

-- =====================================================================
-- BẢNG 2: CẬP NHẬT TỔNG SỐ LƯỢNG ĐÃ MỜI (invited_quantity) VÀO BẢNG TICKET
-- Logic: Chỉ cộng dồn những Invitation có status là PENDING hoặc ACCEPTED
-- =====================================================================
UPDATE ticket
SET invited_quantity = (
    SELECT COALESCE(SUM(initial_quantity), 0)
    FROM event_invitation
    WHERE ticket.id = event_invitation.ticket_id
      AND event_invitation.status IN ('PENDING', 'ACCEPTED')
)
WHERE invitation_quota > 0;