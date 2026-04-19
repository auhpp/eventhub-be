INSERT INTO coupon (id, name, code, start_date_time, end_date_time, content, avatar_url, avatar_public_id, discount_type, value, max_discount_amount, maximum_usage, maximum_booking, minimum_ticket_in_booking, maximum_ticket_in_booking, status, has_public, created_at, updated_at) VALUES

-- 1. Ưu đãi cho sự kiện TechFest (Giảm %) - 100 lượt
(1, 'Ưu đãi TechFest Sớm', 'TECHFEST2026', '2025-10-01 00:00:00', '2026-05-15 23:59:59', 'Giảm 10% tối đa 100.000đ cho tất cả vé TechFest.', NULL, NULL, 'PERCENTAGE', 10.0, 100000, 100, 1, 1, 10, 'ACTIVE', true, '2025-09-01 10:00:00', '2025-09-01 10:00:00'),

-- 2. Ưu đãi cho sự kiện TechFest (Giảm tiền cố định cho vé nhóm) - 50 lượt
(2, 'Vé Nhóm TechFest', 'GROUP_TECH', '2025-10-01 00:00:00', '2026-05-15 23:59:59', 'Giảm trực tiếp 200.000đ khi mua nhóm từ 5 vé trở lên.', NULL, NULL, 'FIXED_AMOUNT', 200000.0, NULL, 50, 2, 5, 20, 'ACTIVE', true, '2025-09-01 10:00:00', '2025-09-01 10:00:00'),

-- 3. Ưu đãi nhóm cho Vietnam Music Festival Nha Trang (Giảm %) - 200 lượt
(3, 'Quẩy Cùng Hội Bạn', 'VMF_GROUP', '2026-01-01 00:00:00', '2026-07-20 23:59:59', 'Giảm 15% tối đa 500.000đ khi đi nhóm 4 người trở lên.', 'https://images.unsplash.com/photo-1543791959-12b3f543282a?w=400', 'coupon/vmf1', 'PERCENTAGE', 15.0, 500000, 200, 1, 4, 10, 'ACTIVE', true, '2025-12-01 08:00:00', '2025-12-01 08:00:00'),

-- 4. Mã ẩn dành cho Fan cứng của Vietnam Music Festival (Giảm tiền cố định) - 50 lượt
(4, 'Tri Ân Fan Cứng VMF', 'VMF_THANKYOU', '2026-01-01 00:00:00', '2026-07-20 23:59:59', 'Giảm trực tiếp 50.000đ cho khách hàng mua vé sớm.', NULL, NULL, 'FIXED_AMOUNT', 50000.0, NULL, 50, 1, 1, 5, 'ACTIVE', false, '2025-12-01 08:00:00', '2025-12-01 08:00:00'),

-- 5. Ưu đãi cho sự kiện Startup Pitching Day (Giảm %) - 20 lượt
(5, 'Đầu Tư Khởi Nghiệp', 'STARTUP26', '2026-01-01 00:00:00', '2026-06-10 23:59:59', 'Giảm 20% tối đa 300k cho vé VIP Nhà Đầu Tư dự án khởi nghiệp.', NULL, NULL, 'PERCENTAGE', 20.0, 300000, 20, 1, 1, 2, 'ACTIVE', true, '2026-01-01 08:00:00', '2026-01-01 08:00:00'),

-- 6. Ưu đãi sự kiện Trekking Fansipan (Giảm tiền cố định) - 30 lượt
(6, 'Chinh Phục Đỉnh Cao', 'TREK_FANSI', '2026-01-01 00:00:00', '2026-03-20 23:59:59', 'Giảm 100.000đ cho gói Trekking Fansipan tiêu chuẩn.', 'https://images.unsplash.com/photo-1522345094982-f5847422f281?w=400', 'coupon/trek1', 'FIXED_AMOUNT', 100000.0, NULL, 30, 1, 1, 5, 'ACTIVE', true, '2026-01-10 08:00:00', '2026-01-10 08:00:00'),

-- 7. Ưu đãi sự kiện Nhạc Indie (Acoustic) (Giảm %) - 100 lượt
(7, 'Tình Ca Phố Thị', 'INDIE_LOVE', '2026-03-01 00:00:00', '2026-07-05 23:59:59', 'Giảm 10% tối đa 30.000đ cho đêm nhạc Acoustic.', NULL, NULL, 'PERCENTAGE', 10.0, 30000, 100, 1, 2, 4, 'ACTIVE', true, '2026-02-15 08:00:00', '2026-02-15 08:00:00'),

-- 8. Ưu đãi sự kiện Rock Xuyên Đêm (Giảm tiền cố định) - 150 lượt
(8, 'Rock Xuyên Đêm', 'ROCK_ON', '2026-03-01 00:00:00', '2026-08-12 23:59:59', 'Tặng ngay 50.000đ khi mua vé đêm nhạc Rock Indie.', NULL, NULL, 'FIXED_AMOUNT', 50000.0, NULL, 150, 1, 1, 5, 'ACTIVE', true, '2026-02-15 08:00:00', '2026-02-15 08:00:00'),

-- 9. Ưu đãi Giải chạy bộ Đà Nẵng Tech Run (Giảm %) - 500 lượt
(9, 'Marathon Bứt Phá', 'RUN2026', '2026-02-01 00:00:00', '2026-06-21 23:59:59', 'Giảm 15% tối đa 100.000đ đăng ký giải chạy Đà Nẵng.', 'https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=400', 'coupon/run1', 'PERCENTAGE', 15.0, 100000, 500, 1, 1, 10, 'ACTIVE', true, '2026-01-20 08:00:00', '2026-01-20 08:00:00'),

-- 10. Ưu đãi Giải đấu Valorant Campus (Giảm tiền cố định) - 300 lượt
(10, 'Sinh Viên E-Sport', 'SV_ESPORT', '2026-05-01 00:00:00', '2026-10-25 23:59:59', 'Mã giảm giá đặc biệt 20.000đ dành cho học sinh/sinh viên.', NULL, NULL, 'FIXED_AMOUNT', 20000.0, NULL, 300, 1, 1, 5, 'ACTIVE', false, '2026-04-01 08:00:00', '2026-04-01 08:00:00'),

-- 11. Lễ hội ẩm thực Food Fest (Giảm %) - 1000 lượt
(11, 'Lễ Hội Ẩm Thực', 'FOOD_FEST', '2026-02-01 00:00:00', '2026-07-11 23:59:59', 'Giảm 5% tối đa 50k khi mua từ 2 vé trở lên tham gia Food Fest.', NULL, NULL, 'PERCENTAGE', 5.0, 50000, 1000, 2, 2, 10, 'ACTIVE', true, '2026-01-15 08:00:00', '2026-01-15 08:00:00'),

-- 12. Trại hè KidCamp Asia (Giảm tiền cố định) - 50 lượt
(12, 'Mùa Hè Cho Bé', 'KIDCAMP26', '2026-02-01 00:00:00', '2026-06-15 23:59:59', 'Giảm trực tiếp 150.000đ khi đăng ký trại hè.', 'https://images.unsplash.com/photo-1473496169904-658ba37448eb?w=400', 'coupon/kid1', 'FIXED_AMOUNT', 150000.0, NULL, 50, 1, 1, 4, 'ACTIVE', true, '2026-01-10 08:00:00', '2026-01-10 08:00:00'),

-- 13. Sự kiện Beer & BBQ (Giảm %) - 200 lượt
(13, 'Chill Cuối Tuần', 'BEER_BBQ', '2026-02-01 00:00:00', '2026-05-15 23:59:59', 'Giảm 10% tối đa 40.000đ cho Lễ Hội Bia.', NULL, NULL, 'PERCENTAGE', 10.0, 40000, 200, 1, 2, 10, 'ACTIVE', true, '2026-01-20 08:00:00', '2026-01-20 08:00:00'),

-- 14. Diễn đàn EdTech Summit (Giảm tiền cố định) - 100 lượt
(14, 'EdTech Tiên Phong', 'EDTECH25', '2026-03-01 00:00:00', '2026-08-21 23:59:59', 'Giảm 50.000đ cho giáo viên và những người làm trong ngành.', NULL, NULL, 'FIXED_AMOUNT', 50000.0, NULL, 100, 1, 1, 5, 'ACTIVE', false, '2026-02-15 08:00:00', '2026-02-15 08:00:00'),

-- 15. Mã chung áp dụng rộng rãi cho một số sự kiện phổ thông (Giảm %) - 5000 lượt
(15, 'Tuần Lễ Mua Sắm', 'SALE_SAP_SAN', '2026-03-01 00:00:00', '2026-12-31 23:59:59', 'Giảm 10% tối đa 30.000đ áp dụng cho hầu hết các sự kiện hội chợ.', NULL, NULL, 'PERCENTAGE', 10.0, 30000, 5000, 3, 1, 10, 'ACTIVE', true, '2026-02-10 08:00:00', '2026-02-10 08:00:00'),

-- 16. Sự kiện Khóa Tu Mùa Hè (Giảm %) - 150 lượt
(16, 'An Tịnh Mùa Hè', 'KHOATU2026', '2026-03-15 00:00:00', '2026-12-31 23:59:59', 'Giảm 10% tối đa 50.000đ khi mua vé Khóa tu.', NULL, NULL, 'PERCENTAGE', 10.0, 50000, 150, 1, 1, 5, 'ACTIVE', true, '2026-03-10 08:00:00', '2026-03-10 08:00:00'),

-- 17. Sự kiện Nếm Rượu Vang (Giảm tiền cố định) - 50 lượt
(17, 'Wine Lover', 'WINE_LOVER', '2026-03-15 00:00:00', '2026-12-31 23:59:59', 'Giảm trực tiếp 50.000đ cho vé Nếm Rượu Vang Pháp.', 'https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?w=400', 'coupon/wine1', 'FIXED_AMOUNT', 50000.0, NULL, 50, 1, 1, 2, 'ACTIVE', true, '2026-03-10 08:00:00', '2026-03-10 08:00:00'),

-- 18. Hội Thảo Bất Động Sản (Giảm %) - 200 lượt
(18, 'Nhà Đầu Tư Thông Thái', 'BDS2026', '2026-04-05 00:00:00', '2026-12-31 23:59:59', 'Giảm 20% tối đa 50k cho khách hàng mua vé Hội thảo BĐS.', NULL, NULL, 'PERCENTAGE', 20.0, 50000, 200, 1, 1, 10, 'ACTIVE', true, '2026-04-01 08:00:00', '2026-04-01 08:00:00'),

-- 19. Sự kiện Chạy Bộ Cùng Thú Cưng (Giảm tiền cố định) - 100 lượt
(19, 'Sen & Boss', 'PETRUN_BOSS', '2026-03-15 00:00:00', '2026-12-31 23:59:59', 'Giảm 30.000đ khi mua từ 2 vé chạy Pet Run.', NULL, NULL, 'FIXED_AMOUNT', 30000.0, NULL, 100, 1, 2, 5, 'ACTIVE', true, '2026-03-10 08:00:00', '2026-03-10 08:00:00'),

-- 20. Lễ Hội Thả Diều (Giảm %) - 300 lượt
(20, 'Gió Mùa Hè', 'KITE_FEST', '2026-02-15 00:00:00', '2026-12-31 23:59:59', 'Giảm 10% tối đa 20k khi mua nhóm 4 người tham gia thả diều.', 'https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?w=400', 'coupon/kite1', 'PERCENTAGE', 10.0, 20000, 300, 2, 4, 20, 'ACTIVE', true, '2026-02-10 08:00:00', '2026-02-10 08:00:00'),

-- 21. Workshop Nhiếp Ảnh (Giảm tiền cố định) - 30 lượt
(21, 'Nhiếp Ảnh Gia', 'PHOTO_PRO', '2026-03-15 00:00:00', '2026-12-31 23:59:59', 'Tặng 20.000đ khi mua vé Workshop Nhiếp ảnh.', NULL, NULL, 'FIXED_AMOUNT', 20000.0, NULL, 30, 1, 1, 2, 'ACTIVE', true, '2026-03-10 08:00:00', '2026-03-10 08:00:00'),

-- 22. Giải Bóng Rổ Sinh Viên (Giảm %) - 200 lượt
(22, 'Cổ Vũ Nhiệt Huyết', 'BASKET_SV', '2026-03-15 00:00:00', '2026-12-31 23:59:59', 'Giảm 15% tối đa 50k cho vé xem giải bóng rổ.', NULL, NULL, 'PERCENTAGE', 15.0, 50000, 200, 1, 1, 10, 'ACTIVE', true, '2026-03-10 08:00:00', '2026-03-10 08:00:00'),

-- 23. Tuần Lễ Văn Hóa Nhật Bản (Giảm tiền cố định) - 150 lượt
(23, 'Văn Hóa Nhật Bản', 'JAPAN_FEST', '2026-03-15 00:00:00', '2026-12-31 23:59:59', 'Giảm trực tiếp 25.000đ khi mua vé Lễ hội Nhật.', 'https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=400', 'coupon/japan1', 'FIXED_AMOUNT', 25000.0, NULL, 150, 1, 1, 5, 'ACTIVE', true, '2026-03-10 08:00:00', '2026-03-10 08:00:00'),

-- 24. Cuộc Thi Cờ Vua Đỉnh Cao (Giảm %) - 100 lượt
(24, 'Đấu Trí Cờ Vua', 'CHESS_MASTER', '2026-02-20 00:00:00', '2026-12-31 23:59:59', 'Giảm 10% tối đa 50k vé đăng ký thi đấu cờ vua.', NULL, NULL, 'PERCENTAGE', 10.0, 50000, 100, 1, 1, 4, 'ACTIVE', true, '2026-02-15 08:00:00', '2026-02-15 08:00:00'),

-- 25. Cắm Trại Săn Mây Tà Xùa (Giảm %) - 200 lượt
(25, 'Săn Mây Tà Xùa', 'TAXUA_CAMP', '2026-02-15 00:00:00', '2026-12-31 23:59:59', 'Giảm 15% tối đa 60k khi đi cắm trại nhóm 2 người.', NULL, NULL, 'PERCENTAGE', 15.0, 60000, 200, 1, 2, 10, 'ACTIVE', true, '2026-02-10 08:00:00', '2026-02-10 08:00:00'),

-- 26. Lễ Hội Bia Craft (Giảm tiền cố định) - 300 lượt
(26, 'Uống Vui Chơi Đã', 'CHEERS26', '2026-02-15 00:00:00', '2026-05-14 23:59:59', 'Giảm ngay 20.000đ cho anh em tham gia Lễ hội Bia.', NULL, NULL, 'FIXED_AMOUNT', 20000.0, NULL, 300, 1, 1, 10, 'ACTIVE', true, '2026-02-10 08:00:00', '2026-02-10 08:00:00'),

-- 27. Giải Chạy Đà Nẵng 21KM (Giảm tiền cố định) - 500 lượt
(27, 'Bứt Phá Giới Hạn', 'RUN_21KM', '2026-02-15 00:00:00', '2026-06-15 23:59:59', 'Giảm 50.000đ khi đăng ký Bib 21KM sớm.', 'https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=400', 'coupon/run21', 'FIXED_AMOUNT', 50000.0, NULL, 500, 1, 1, 5, 'ACTIVE', true, '2026-02-10 08:00:00', '2026-02-10 08:00:00'),

-- 28. Giải Đấu Valorant Campus (Giảm %) - 400 lượt
(28, 'Gamer Bách Khoa', 'GAMER_VN', '2026-08-15 00:00:00', '2026-10-24 23:59:59', 'Giảm 10% giá vé vào cổng xem chung kết (Tối đa 15k).', NULL, NULL, 'PERCENTAGE', 10.0, 15000, 400, 1, 1, 10, 'ACTIVE', true, '2026-08-01 08:00:00', '2026-08-01 08:00:00'),

-- 29. Hội Thảo BĐS (Giảm tiền cố định) - 20 lượt
(29, 'Khách Mời Ưu Tiên BĐS', 'REAL_ESTATE', '2026-02-20 00:00:00', '2026-04-07 08:30:00', 'Mã nội bộ: Giảm 50.000đ cho khách mời đặc biệt.', NULL, NULL, 'FIXED_AMOUNT', 50000.0, NULL, 20, 1, 1, 2, 'ACTIVE', false, '2026-02-15 08:00:00', '2026-02-15 08:00:00'),

-- 30. Mã Chung Cuối Tuần (Giảm %) - 1000 lượt
(30, 'Vui Chơi Cuối Tuần', 'WEEKEND_DEAL', '2026-02-15 00:00:00', '2026-12-31 23:59:59', 'Nhập mã giảm ngay 10% tối đa 50k vào dịp cuối tuần.', NULL, NULL, 'PERCENTAGE', 10.0, 50000, 1000, 3, 1, 10, 'ACTIVE', true, '2026-02-10 08:00:00', '2026-02-10 08:00:00');