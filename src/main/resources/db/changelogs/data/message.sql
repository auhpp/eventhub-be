INSERT INTO message (id, content, status, path_url, public_id, type, seen_at, created_at, updated_at, reply_message_id, sender_id, recipient_id) VALUES

-- C1: Mem 1 & 2
(1, 'Chào BTC, sự kiện AI mua vé xong là có mã QR luôn đúng không ạ?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-01 10:05:00', '2026-03-01 10:00:00', '2026-03-01 10:05:00', NULL, 1, 2),
(2, 'Đúng rồi bạn Vũ, sau khi thanh toán thành công, bạn vào mục "Vé của tôi" trên web để lấy mã QR nhé.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-01 10:20:00', '2026-03-01 10:15:00', '2026-03-01 10:20:00', 1, 2, 1),
(3, 'Bên mình có Voucher giảm giá cho sinh viên không ạ?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 14:10:00', '2026-03-05 14:00:00', '2026-03-05 14:10:00', NULL, 1, 2),
(4, 'Bạn nhập mã SV_TECH2026 ở màn hình thanh toán để được giảm 20k nhé.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 14:20:00', '2026-03-05 14:15:00', '2026-03-05 14:20:00', 3, 2, 1),
(5, 'Tuyệt vời, mình đã mua được vé rẻ hơn. Cảm ơn ad!', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 14:30:00', '2026-03-05 14:30:00', NULL, 1, 2),

-- C2: Mem 3 & 4
(6, 'Chào ad, cho mình hỏi bãi đỗ xe của khách sạn Rex nằm ở hầm nào ạ?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 09:20:00', '2026-03-10 09:15:00', '2026-03-10 09:20:00', NULL, 3, 4),
(7, 'Chào Tâm, bãi gửi xe nằm ở Tầng hầm B2 nhé.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 09:50:00', '2026-03-10 09:45:00', '2026-03-10 09:50:00', 6, 4, 3),
(8, 'Sự kiện này mình quét mã QR trên điện thoại để vào cổng luôn phải không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 10:00:00', '2026-03-10 09:55:00', '2026-03-10 10:00:00', NULL, 3, 4),
(9, 'Đúng rồi bạn.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 10:15:00', '2026-03-10 10:15:00', 8, 4, 3),

-- C3: Mem 5 & 6
(10, 'Giá vé VVIP Nha Trang bao nhiêu vậy ad ơi?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-15 16:10:00', '2026-03-15 16:00:00', '2026-03-15 16:10:00', NULL, 5, 6),
(11, 'Chào Thảo, vé VVIP là 1.500.000 VNĐ.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-16 08:05:00', '2026-03-16 08:00:00', '2026-03-16 08:05:00', 10, 6, 5),
(12, 'Mình thấy trên web không giảm giá khi mua nhiều vé hả ad?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-16 08:30:00', '2026-03-16 08:20:00', '2026-03-16 08:30:00', NULL, 5, 6),
(13, 'Nhóm trên 5 người dùng Voucher VMF_GROUP nhé.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-16 08:45:00', '2026-03-16 08:40:00', '2026-03-16 08:45:00', 12, 6, 5),
(14, 'Cảm ơn ad.', 'SENT', NULL, NULL, 'TEXT', NULL, '2026-03-16 08:50:00', '2026-03-16 08:50:00', NULL, 5, 6),

-- C4: Mem 7 & 8
(15, 'Combo 3 ngày dùng chung 1 mã QR?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-20 11:05:00', '2026-03-20 11:00:00', '2026-03-20 11:05:00', NULL, 7, 8),
(16, 'Đúng rồi Đạt, 1 mã quét cho 3 ngày.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-20 11:35:00', '2026-03-20 11:30:00', '2026-03-20 11:35:00', 15, 8, 7),
(17, 'Có gửi mã về SĐT không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-20 11:45:00', '2026-03-20 11:40:00', '2026-03-20 11:45:00', NULL, 7, 8),
(18, 'Gửi qua Email và lưu trên Web nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-20 11:55:00', '2026-03-20 11:55:00', 17, 8, 7),

-- C5: Mem 9 & 10
(19, 'Trẻ em dưới 10 tuổi cần mua vé không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 14:15:00', '2026-04-01 14:00:00', '2026-04-01 14:15:00', NULL, 9, 10),
(20, 'Dưới 6 tuổi mới được miễn phí ạ.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 14:30:00', '2026-04-01 14:20:00', '2026-04-01 14:30:00', 19, 10, 9),
(21, 'Loại vé trẻ em bị ẩn rồi.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 14:45:00', '2026-04-01 14:40:00', '2026-04-01 14:45:00', NULL, 9, 10),
(22, 'Do hết vé trẻ em rồi, bạn mua vé GA nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 15:10:00', '2026-04-01 15:00:00', 21, 10, 9),

-- C8: Mem 15 & 16 (Saigon Indie)
(23, 'Show tối nay mở check-in lúc nào?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:05:00', '2026-03-05 09:00:00', '2026-03-05 09:05:00', NULL, 15, 16),
(24, 'Mở từ 19:00 nha Vũ.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:20:00', '2026-03-05 09:15:00', '2026-03-05 09:20:00', 23, 16, 15),
(25, 'Quét chung 1 điện thoại cho 5 người được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:30:00', '2026-03-05 09:25:00', '2026-03-05 09:30:00', NULL, 15, 16),
(26, 'Được, quét lần lượt từng vé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 09:45:00', '2026-03-05 09:45:00', 25, 16, 15),

-- C10: Mem 19 & 20 (Hanoi Workshop)
(27, 'Xem mã QR ở đâu ad?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-20 14:10:00', '2026-02-20 14:00:00', '2026-02-20 14:10:00', NULL, 19, 20),
(28, 'Vào menu "Vé cá nhân" nha Thúy.', 'SEEN', NULL, NULL, 'TEXT', '2026-02-20 14:30:00', '2026-02-20 14:20:00', '2026-02-20 14:30:00', 27, 20, 19),
(29, 'Ảnh vé như này ok không?', 'SEEN', 'https://images.unsplash.com/photo-1614850523296-d8c1af93d400?w=400', 'msg/ticket_sample', 'IMAGE', '2026-02-20 14:45:00', '2026-02-20 14:40:00', '2026-02-20 14:45:00', NULL, 19, 20),
(30, 'Chuẩn rồi đó.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-20 15:00:00', '2026-02-20 15:00:00', 29, 20, 19),

-- C11: Mem 21 & 22
(31, 'Chưa trừ tiền trong ví?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 10:45:00', '2026-03-10 10:30:00', '2026-03-10 10:45:00', NULL, 21, 22),
(32, 'Check mục "Lịch sử thanh toán" nhé.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 11:00:00', '2026-03-10 10:50:00', '2026-03-10 11:00:00', 31, 22, 21),
(33, 'Báo đang xử lý ạ.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 11:15:00', '2026-03-10 11:05:00', '2026-03-10 11:15:00', NULL, 21, 22),
(34, 'Đợi vài phút hệ thống đồng bộ nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 11:30:00', '2026-03-10 11:30:00', 33, 22, 21),

-- C14: Mem 27 & 28 (Cloud Computing)
(35, 'Có Voucher 50% không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 16:10:00', '2026-03-05 16:00:00', '2026-03-05 16:10:00', NULL, 27, 28),
(36, 'Chỉ có mã 20% tối đa thôi.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 16:25:00', '2026-03-05 16:15:00', '2026-03-05 16:25:00', 35, 28, 27),
(37, 'Cho mình xin mã 20%.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 16:40:00', '2026-03-05 16:30:00', '2026-03-05 16:40:00', NULL, 27, 28),
(38, 'Mã là CLOUD20 nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 17:00:00', '2026-03-05 17:00:00', 37, 28, 27),

-- C40: Mem 79 & 80 (Coffee Expo)
(39, 'Có thanh toán Napas không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 10:15:00', '2026-04-01 10:05:00', '2026-04-01 10:15:00', NULL, 79, 80),
(40, 'Có hỗ trợ đầy đủ nha.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 10:30:00', '2026-04-01 10:20:00', '2026-04-01 10:30:00', 39, 80, 79),
(41, 'Có cần in QR ra giấy không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 10:45:00', '2026-04-01 10:35:00', '2026-04-01 10:45:00', NULL, 79, 80),
(42, 'Không cần, lưu điện thoại là được.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 11:00:00', '2026-04-01 11:00:00', 41, 80, 79),

-- C36: Mem 71 & 72
(43, 'Check-in ở sảnh nào?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 09:10:00', '2026-04-07 09:00:00', '2026-04-07 09:10:00', NULL, 71, 72),
(44, 'Sảnh A lầu 2 Landmark 81.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 09:20:00', '2026-04-07 09:15:00', '2026-04-07 09:20:00', 43, 72, 71),

-- C26: Mem 51 & 52
(45, 'Voucher giảm bao nhiêu?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 09:30:00', '2026-04-07 09:25:00', '2026-04-07 09:30:00', NULL, 51, 52),
(46, 'Giảm trực tiếp 100k.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 09:40:00', '2026-04-07 09:40:00', 45, 52, 51),

-- C8 (Tiếp): Mem 15 & 16
(47, 'Hệ thống báo hết lượt mua?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 10:00:00', '2026-04-07 09:50:00', '2026-04-07 10:00:00', NULL, 15, 16),
(48, 'Đã bán hết trên web rồi ạ.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 10:10:00', '2026-04-07 10:10:00', 47, 16, 15),

-- C38: Mem 75 & 76
(49, 'Nhượng vé thế nào?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 10:20:00', '2026-04-07 10:15:00', '2026-04-07 10:20:00', NULL, 75, 76),
(50, 'Chọn nút "Nhượng vé" trong chi tiết vé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 10:30:00', '2026-04-07 10:30:00', 49, 76, 75),

-- C40: Mem 79 & 80
(51, 'Vào muộn quét mã được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 14:10:00', '2026-04-07 14:05:00', '2026-04-07 14:10:00', NULL, 79, 80),
(52, 'Được, link live mở suốt giờ diễn.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 14:20:00', '2026-04-07 14:20:00', 51, 80, 79),

-- C6: Mem 11 & 12
(53, 'Mã WELCOME áp dụng loại nào?', 'SEEN', NULL, NULL, 'TEXT', '2026-01-10 09:00:00', '2026-01-10 08:30:00', '2026-01-10 09:00:00', NULL, 11, 12),
(54, 'Tất cả hạng vé của Alpha Event.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-10 09:15:00', '2026-01-10 09:15:00', 53, 12, 11),
(55, 'Lỗi ví MoMo?', 'SEEN', NULL, NULL, 'TEXT', '2026-01-10 09:30:00', '2026-01-10 09:20:00', '2026-01-10 09:30:00', NULL, 11, 12),
(56, 'Dùng QR Pay thử xem nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-10 09:45:00', '2026-01-10 09:45:00', 55, 12, 11),

-- C7: Mem 13 & 14
(57, 'Hủy bớt 5 vé được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 16:00:00', '2026-02-15 15:45:00', '2026-02-15 16:00:00', NULL, 13, 14),
(58, 'Chỉ hỗ trợ hoàn toàn bộ đơn hàng.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-15 16:15:00', '2026-02-15 16:15:00', 57, 14, 13),
(59, 'Tặng vé thế nào?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 16:30:00', '2026-02-15 16:20:00', '2026-02-15 16:30:00', NULL, 13, 14),
(60, 'Nhập email người nhận ở nút "Tặng vé".', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-15 16:45:00', '2026-02-15 16:45:00', 59, 14, 13),

-- C9: Mem 17 & 18
(61, 'Giới hạn bao nhiêu vé?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-02 20:15:00', '2026-04-02 20:05:00', '2026-04-02 20:15:00', NULL, 17, 18),
(62, 'Tối đa 4 vé/tài khoản.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-02 20:30:00', '2026-04-02 20:30:00', 61, 18, 17),
(63, 'Cảm ơn ad.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-02 20:45:00', '2026-04-02 20:40:00', '2026-04-02 20:45:00', NULL, 17, 18),
(64, 'Dạ không có gì.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-02 21:00:00', '2026-04-02 21:00:00', 63, 18, 17),

-- C12: Mem 23 & 24
(65, 'Check-in xong QR còn hiệu lực?', 'SEEN', NULL, NULL, 'TEXT', '2026-01-25 15:10:00', '2026-01-25 15:00:00', '2026-01-25 15:10:00', NULL, 23, 24),
(66, 'Sẽ bị vô hiệu hóa sau khi quét thành công.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-25 15:25:00', '2026-01-25 15:25:00', 65, 24, 23),

-- C14 (Tiếp): Mem 27 & 28
(67, 'Voucher INDIE_LOVE xài sao?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-06 08:40:00', '2026-03-06 08:30:00', '2026-03-06 08:40:00', NULL, 27, 28),
(68, 'Nhập ở bước thanh toán nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-06 08:50:00', '2026-03-06 08:50:00', 67, 28, 27),

-- C15: Mem 29 & 30
(69, 'Dùng ví Wallet có ưu đãi gì?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 14:40:00', '2026-04-01 14:30:00', '2026-04-01 14:40:00', NULL, 29, 30),
(70, 'Xử lý vé nhanh và ưu tiên hoàn tiền.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 14:55:00', '2026-04-01 14:55:00', 69, 30, 29),

-- C33: Mem 65 & 66
(71, 'Thuê tai nghe VR ở đâu?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-05 10:00:00', '2026-04-05 09:45:00', '2026-04-05 10:00:00', NULL, 65, 66),
(72, 'Mua "Gói dịch vụ tai nghe" trên web nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-05 10:15:00', '2026-04-05 10:15:00', 71, 66, 65),

-- C40 (Tiếp): Mem 79 & 80
(73, 'Vé hiển thị trên App không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 10:40:00', '2026-04-01 10:30:00', '2026-04-01 10:40:00', NULL, 79, 80),
(74, 'Có, đăng nhập là thấy.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 10:55:00', '2026-04-01 10:55:00', 73, 80, 79),

-- C27: Mem 53 & 54
(75, 'Xin sơ đồ VIP.', 'SEEN', NULL, NULL, 'TEXT', '2026-01-20 11:30:00', '2026-01-20 11:15:00', '2026-01-20 11:30:00', NULL, 53, 54),
(76, '', 'SEEN', 'https://images.unsplash.com/photo-1549451371-64aa98a6f660?w=400', 'msg/seating_map', 'IMAGE', '2026-01-20 11:45:00', '2026-01-20 11:35:00', '2026-01-20 11:45:00', 75, 54, 53),
(77, 'Cảm ơn ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-20 12:00:00', '2026-01-20 12:00:00', 76, 53, 54),

-- C28: Mem 55 & 56
(78, 'Đổi ý không đi có hoàn tiền?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 15:50:00', '2026-02-15 15:40:00', '2026-02-15 15:50:00', NULL, 55, 56),
(79, 'Tùy chính sách từng event nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-15 16:05:00', '2026-02-15 16:05:00', 78, 56, 55),

-- C29: Mem 57 & 58
(80, 'Dùng Voucher tặng được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 08:40:00', '2026-03-10 08:30:00', '2026-03-10 08:40:00', NULL, 57, 58),
(81, 'Được, Voucher không định danh tài khoản.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 08:55:00', '2026-03-10 08:55:00', 80, 58, 57),

-- C16 (SỬA LẠI ĐÚNG MỚI): Mem 31 & 32 (User 5 & Danang Tech Run)
(82, 'Mã QR vé của mình bị người khác chụp lại rồi, ad hỗ trợ khóa được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-05 09:20:00', '2026-04-05 09:10:00', '2026-04-05 09:20:00', NULL, 31, 32),
(83, 'Cung cấp CCCD để BTC reset mã mới nha Tâm.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-05 09:40:00', '2026-04-05 09:40:00', 82, 32, 31),

-- C40 (Tiếp): Mem 79 & 80
(84, 'Check Voucher CAFE_CONG.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 10:10:00', '2026-04-01 10:00:00', '2026-04-01 10:10:00', NULL, 79, 80),
(85, 'Vẫn còn 50 lượt dùng nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 10:25:00', '2026-04-01 10:25:00', 84, 80, 79),

-- C2: Mem 3 & 4
(86, 'Lễ hội bia có bán vé tại cổng không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-06 11:00:00', '2026-04-06 10:45:00', '2026-04-06 11:00:00', NULL, 3, 4),
(87, 'Có, nhưng mua trên Web rẻ hơn 20k.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-06 11:15:00', '2026-04-06 11:15:00', 86, 4, 3),
(88, 'Cảm ơn ad, mình mua trên Web.', 'SENT', NULL, NULL, 'TEXT', NULL, '2026-04-06 11:20:00', '2026-04-06 11:20:00', NULL, 3, 4),

-- C10: Mem 19 & 20
(89, 'Check-in xong được ra ngoài không?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-21 09:10:00', '2026-02-21 09:00:00', '2026-02-21 09:10:00', NULL, 19, 20),
(90, 'Được, nhớ xin đóng dấu mộc nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-21 09:25:00', '2026-02-21 09:25:00', 89, 20, 19),

-- C14: Mem 27 & 28
(91, 'Lỗi ngân hàng VCB?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 16:50:00', '2026-03-05 16:40:00', '2026-03-05 16:50:00', NULL, 27, 28),
(92, 'VCB đang bảo trì, bạn thử QR Pay nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 17:05:00', '2026-03-05 17:05:00', 91, 28, 27),

-- C38: Mem 75 & 76
(93, 'Người nhận vé nhượng cần làm gì?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-25 16:45:00', '2026-02-25 16:30:00', '2026-02-25 16:45:00', NULL, 75, 76),
(94, 'Xác nhận qua email là xong.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-25 17:00:00', '2026-02-25 17:00:00', 93, 76, 75),

-- C24: Mem 47 & 48
(95, 'Mua 10 vé có Voucher không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 16:15:00', '2026-03-05 16:00:00', '2026-03-05 16:15:00', NULL, 47, 48),
(96, 'Mã là GROUP_10_OFF nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 16:30:00', '2026-03-05 16:30:00', 95, 48, 47),
(97, 'Mình mua xong rồi.', 'SENT', NULL, NULL, 'TEXT', NULL, '2026-03-05 16:40:00', '2026-03-05 16:40:00', NULL, 47, 48),
(98, 'Dạ ok.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 16:45:00', '2026-03-05 16:45:00', 97, 48, 47),

-- C22: Mem 43 & 44
(99, 'Đơn hàng bị hủy do quá hạn?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-20 10:45:00', '2026-02-20 10:30:00', '2026-02-20 10:45:00', NULL, 43, 44),
(100, 'Bạn có 15 phút để thanh toán thôi ạ.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-20 11:00:00', '2026-02-20 11:00:00', 99, 44, 43),

-- C39: Mem 77 & 78
(101, 'Check nick test_01 có vé chưa?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 16:20:00', '2026-03-10 16:10:00', '2026-03-10 16:20:00', NULL, 77, 78),
(102, 'Đã thành công vé Green Life.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 16:35:00', '2026-03-10 16:35:00', 101, 78, 77),

-- C26: Mem 51 & 52
(103, 'Có nước uống miễn phí?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-25 09:30:00', '2026-03-25 09:15:00', '2026-03-25 09:30:00', NULL, 51, 52),
(104, 'Có cây nước lọc ở sảnh nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-25 09:45:00', '2026-03-25 09:45:00', 103, 52, 51),

-- C28: Mem 55 & 56
(105, 'In vé bản cứng được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 15:55:00', '2026-02-15 15:45:00', '2026-02-15 15:55:00', NULL, 55, 56),
(106, 'Dùng QR trên điện thoại là đủ.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-15 16:10:00', '2026-02-15 16:10:00', 105, 56, 55),
(107, 'Ok ủng hộ bảo vệ môi trường.', 'SENT', NULL, NULL, 'TEXT', NULL, '2026-02-15 16:15:00', '2026-02-15 16:15:00', 106, 55, 56),

-- C27: Mem 53 & 54
(108, 'Dùng iPad check-in được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-01-20 11:20:00', '2026-01-20 11:10:00', '2026-01-20 11:20:00', NULL, 53, 54),
(109, 'Được, thiết bị nào vào được web là quét được.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-20 11:35:00', '2026-01-20 11:35:00', 108, 54, 53),

-- C32: Mem 63 & 64
(110, 'Lỡ lộ mã QR lên Facebook.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-15 16:45:00', '2026-03-15 16:35:00', '2026-03-15 16:45:00', NULL, 63, 64),
(111, 'Nhắn mã đơn để mình Reset nha.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-15 17:00:00', '2026-03-15 16:50:00', '2026-03-15 17:00:00', 110, 64, 63),
(112, '#COV-32-123. Giúp mình với.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-15 17:15:00', '2026-03-15 17:15:00', NULL, 63, 64),
(113, 'Đã reset mã mới.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 10:30:00', '2026-03-10 10:30:00', 112, 64, 63),
(114, 'Cảm ơn ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-15 17:35:00', '2026-03-15 17:35:00', 113, 63, 64),

-- C34: Mem 67 & 68
(115, 'Sự kiện có cho hoàn vé?', 'SEEN', NULL, NULL, 'TEXT', '2026-01-25 10:20:00', '2026-01-25 10:10:00', '2026-01-25 10:20:00', NULL, 67, 68),
(116, 'Check phần mô tả event nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-25 10:35:00', '2026-01-25 10:35:00', 115, 68, 67),

-- C35: Mem 69 & 70
(117, 'Mất nút Nhượng vé?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-10 15:25:00', '2026-02-10 15:15:00', '2026-02-10 15:25:00', NULL, 69, 70),
(118, 'Sắp đến giờ check-in nên hệ thống khóa nút nhượng rồi.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-15 17:15:00', '2026-01-15 17:15:00', 117, 70, 69),

-- C30: Mem 59 & 60
(119, 'Bé 3 tuổi miễn phí không?', 'SEEN', NULL, NULL, 'TEXT', '2026-01-10 09:20:00', '2026-01-10 09:05:00', '2026-01-10 09:20:00', NULL, 59, 60),
(120, 'Được nha, bé ngồi chung ghế với ba mẹ.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-10 09:35:00', '2026-01-10 09:35:00', 119, 60, 59),

-- C31: Mem 61 & 62
(121, 'Muốn mua thêm suất ăn cho bé.', 'SEEN', NULL, NULL, 'TEXT', '2026-02-20 14:20:00', '2026-02-20 14:10:00', '2026-02-20 14:20:00', NULL, 61, 62),
(122, 'Chọn thêm "Suất ăn trẻ em" trong list vé nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-10 15:30:00', '2026-02-10 15:30:00', 121, 62, 61),

-- C17: Mem 33 & 34
(123, 'Vé VIP có tặng áo không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-20 17:50:00', '2026-03-20 17:40:00', '2026-03-20 17:50:00', NULL, 33, 34),
(124, 'Có kèm 1 áo và 1 nón nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-20 18:25:00', '2026-03-20 18:25:00', 123, 34, 33),

-- C8 (Tiếp): Mem 15 & 16
(125, 'Có cho mang đồ uống vào không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:20:00', '2026-03-05 09:10:00', '2026-03-05 09:20:00', NULL, 15, 16),
(126, 'Không được mang từ ngoài vào khu vực Indie Hub nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 09:35:00', '2026-03-05 09:35:00', 125, 16, 15),
(127, 'Có bán đĩa CD nghệ sĩ không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:40:00', '2026-03-05 09:30:00', '2026-03-05 09:40:00', NULL, 15, 16),
(128, 'Có quầy Merch ở sảnh chờ nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-25 10:20:00', '2026-03-25 10:20:00', 127, 16, 15),

-- C9: Mem 17 & 18
(129, 'Có chỗ gửi túi cồng kềnh?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-02 20:20:00', '2026-04-02 20:10:00', '2026-04-02 20:20:00', NULL, 17, 18),
(130, 'Có tủ locker 20k/lần nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-20 12:00:00', '2026-01-20 12:00:00', 129, 18, 17),

-- C26: Mem 51 & 52
(131, 'Mua 2 vé nhưng 1 người bận?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-25 09:20:00', '2026-03-25 09:10:00', '2026-03-25 09:20:00', NULL, 51, 52),
(132, 'Liên hệ hotline để hỗ trợ tách đơn nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-20 12:00:00', '2026-01-20 12:00:00', 131, 52, 51),

-- C33: Mem 65 & 66
(133, 'Link live bị lỗi 404.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-05 09:25:00', '2026-04-05 09:15:00', '2026-04-05 09:25:00', NULL, 65, 66),
(134, 'Vào lại vé lấy link mới nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-05 09:40:00', '2026-04-05 09:40:00', 133, 66, 65),
(135, 'Thấy rồi, cảm ơn ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-05 09:50:00', '2026-04-05 09:50:00', 134, 65, 66),

-- C35: Mem 69 & 70
(136, 'Hỏi về quy trình nhượng vé Resale.', 'SEEN', NULL, NULL, 'TEXT', '2026-02-10 15:20:00', '2026-02-10 15:10:00', '2026-02-10 15:20:00', NULL, 69, 70),
(137, 'Chọn nút "Resale" và nhập giá muốn bán.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-15 16:45:00', '2026-02-15 16:45:00', 136, 70, 69),
(138, 'Có thu phí không?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-10 15:50:00', '2026-02-10 15:40:00', '2026-02-10 15:50:00', NULL, 69, 70),
(139, 'Phí dịch vụ 5% nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-10 16:05:00', '2026-02-10 16:05:00', 138, 70, 69),

-- C36: Mem 71 & 72
(140, 'Mua qua sàn Resale có an toàn QR không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-20 09:10:00', '2026-03-20 09:00:00', '2026-03-20 09:10:00', NULL, 71, 72),
(141, 'Rất an toàn, QR cũ sẽ bị hủy và cấp mã mới cho Trí.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 09:40:00', '2026-03-10 09:40:00', 140, 72, 71),
(142, 'Cảm ơn hệ thống.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-20 09:35:00', '2026-03-20 09:35:00', 141, 71, 72),

-- C39: Mem 77 & 78
(143, 'Đổi rác lấy cây có cần vé 0đ?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 16:10:00', '2026-03-10 16:00:00', '2026-03-10 16:10:00', NULL, 77, 78),
(144, 'Cần, để BTC chuẩn bị đủ cây nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-10 09:55:00', '2026-01-10 09:55:00', 143, 78, 77),
(145, 'Voucher GREEN2026 áp dụng cho phí ship không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 16:40:00', '2026-03-10 16:30:00', '2026-03-10 16:40:00', NULL, 77, 78),
(146, 'Không, chỉ áp dụng cho sản phẩm sự kiện thôi.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 16:55:00', '2026-03-10 16:55:00', 145, 78, 77),
(147, 'Ok mình hiểu rồi.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 17:05:00', '2026-03-10 17:05:00', 146, 77, 78),

-- C40: Mem 79 & 80
(148, 'Người thân vào xem Barista được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 10:20:00', '2026-04-01 10:10:00', '2026-04-01 10:20:00', NULL, 79, 80),
(149, 'Mua vé "Khán giả" giá 50k nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-20 15:10:00', '2026-02-20 15:10:00', 148, 80, 79),
(150, 'Ok mình mua 2 vé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 10:45:00', '2026-04-01 10:45:00', 149, 79, 80),

-- C1: Mem 1 & 2
(151, 'Bận đột xuất có được hoàn vé?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-01 10:10:00', '2026-03-01 10:00:00', '2026-03-01 10:10:00', NULL, 1, 2),
(152, 'Check nút "Yêu cầu hoàn vé" trong info vé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-01 10:25:00', '2026-03-01 10:25:00', 151, 2, 1),
(153, 'Thấy rồi, cảm ơn ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-15 17:40:00', '2026-03-15 17:40:00', 152, 1, 2),

-- C8 (Tiếp): Mem 15 & 16
(154, 'Mã QR lỗi không hiện ảnh.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:25:00', '2026-03-05 09:15:00', '2026-03-05 09:25:00', NULL, 15, 16),
(155, 'Đăng nhập lại hoặc check mạng nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-05 10:20:00', '2026-04-05 10:20:00', 154, 16, 15),
(156, 'Được rồi ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 09:50:00', '2026-03-05 09:50:00', 155, 15, 16),

-- C3: Mem 5 & 6
(157, 'Vé Standard ngồi khu lầu 1 không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-15 16:20:00', '2026-03-15 16:10:00', '2026-03-15 16:20:00', NULL, 5, 6),
(158, 'Chỉ ngồi tầng trệt nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-25 11:00:00', '2026-01-25 11:00:00', 157, 6, 5),
(159, 'FIRST_EVENT giảm bao nhiêu?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-15 16:45:00', '2026-03-15 16:35:00', '2026-03-15 16:45:00', NULL, 5, 6),
(160, 'Giảm 10% cho đơn đầu tiên.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-15 17:00:00', '2026-03-15 17:00:00', 159, 6, 5),

-- C4: Mem 7 & 8
(161, 'Sai email đăng ký, giúp sửa với.', 'SEEN', NULL, NULL, 'TEXT', '2026-03-20 11:10:00', '2026-03-20 11:00:00', '2026-03-20 11:10:00', NULL, 7, 8),
(162, 'Cung cấp SĐT và mã đơn thanh toán.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-10 16:25:00', '2026-02-10 16:25:00', 161, 8, 7),
(163, 'SĐT 0988xxx, đơn #VMF-123. Email: dat.dinh@gmail.com.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-20 11:40:00', '2026-03-20 11:40:00', 162, 7, 8),
(164, 'Đã cập nhật email rồi đó.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-20 11:55:00', '2026-03-20 11:55:00', 163, 8, 7),

-- C5: Mem 9 & 10
(165, 'Hoàn vé bằng tiền mặt được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 14:20:00', '2026-04-01 14:10:00', '2026-04-01 14:20:00', NULL, 9, 10),
(166, 'Không, chỉ hoàn qua ví Wallet hoặc thẻ.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-20 10:00:00', '2026-03-20 10:00:00', 165, 10, 9),
(167, 'Có khu ký gửi đồ?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-01 14:50:00', '2026-04-01 14:40:00', '2026-04-01 14:50:00', NULL, 9, 10),
(168, 'Có tủ locker, thuê trên web luôn nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-01-15 15:40:00', '2026-01-15 15:40:00', 167, 10, 9),

-- C28: Mem 55 & 56
(169, 'Mua combo nhận áo ở đâu?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 16:10:00', '2026-02-15 16:00:00', '2026-02-15 16:10:00', NULL, 55, 56),
(170, 'Nhận tại quầy Check-in luôn nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-20 09:45:00', '2026-02-20 09:45:00', 169, 56, 55),
(171, 'Có size XL không ad?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 16:35:00', '2026-02-15 16:30:00', '2026-02-15 16:35:00', NULL, 55, 56),
(172, 'Điền size trong info vé trên web nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 17:15:00', '2026-03-10 17:15:00', 171, 56, 55),
(173, 'Điền nhầm size rồi.', 'SEEN', NULL, NULL, 'TEXT', '2026-02-15 17:00:00', '2026-02-15 16:55:00', '2026-02-15 17:00:00', NULL, 55, 56),
(174, 'Báo nhân viên lúc nhận để đổi trực tiếp.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-01 11:30:00', '2026-04-01 11:30:00', 173, 56, 55),

-- C8 (Tiếp): Mem 15 & 16
(175, 'Được mang máy ảnh chuyên nghiệp không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:30:00', '2026-03-05 09:20:00', '2026-03-05 09:30:00', NULL, 15, 16),
(176, 'Chỉ vé Photographer Pass mới được mang.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 17:15:00', '2026-04-07 17:15:00', 175, 16, 15),
(177, 'Giá vé đó bao nhiêu?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:55:00', '2026-03-05 09:50:00', '2026-03-05 09:55:00', NULL, 15, 16),
(178, 'Giá là 500k nha Vũ.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 10:10:00', '2026-03-05 10:10:00', 177, 16, 15),

-- C9: Mem 17 & 18
(179, 'Thanh toán lỗi nhưng bị trừ tiền?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-03 08:30:00', '2026-04-03 08:15:00', '2026-04-03 08:30:00', NULL, 17, 18),
(180, 'Gửi ảnh màn hình trừ tiền ad check.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-03 08:45:00', '2026-04-03 08:45:00', 179, 18, 17),
(181, 'Ảnh nè ad.', 'SEEN', 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=400', 'msg/trans_error', 'IMAGE', '2026-04-03 08:55:00', '2026-04-03 08:50:00', '2026-04-03 08:55:00', NULL, 17, 18),
(182, 'Ad đã update vé manually rồi, check web nhé.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-03 09:10:00', '2026-04-03 09:10:00', 181, 18, 17),
(183, 'Thấy vé rồi, cảm ơn ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-03 09:20:00', '2026-04-03 09:20:00', 182, 17, 18),

-- C2: Mem 3 & 4
(184, 'Check-in thủ công lâu không?', 'SEEN', NULL, NULL, 'TEXT', '2026-04-07 10:15:00', '2026-04-07 10:00:00', '2026-04-07 10:15:00', NULL, 3, 4),
(185, 'Quét QR mất 3 giây thôi bạn.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 10:30:00', '2026-04-07 10:30:00', 184, 4, 3),

-- C10: Mem 19 & 20
(186, 'Đổi SĐT trong vé được không?', 'SEEN', NULL, NULL, 'TEXT', '2026-02-21 09:30:00', '2026-02-21 09:20:00', '2026-02-21 09:30:00', NULL, 19, 20),
(187, 'Tự cập nhật trong mục Hồ sơ nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-02-21 09:45:00', '2026-02-21 09:45:00', 186, 20, 19),

-- C11: Mem 21 & 22
(188, 'Voucher WELCOM_HANOI dùng mấy người?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-10 11:35:00', '2026-03-10 11:25:00', '2026-03-10 11:35:00', NULL, 21, 22),
(189, 'Chỉ 1 lần thanh toán duy nhất.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-10 11:50:00', '2026-03-10 11:50:00', 188, 22, 21),

-- C8 (Tiếp): Mem 15 & 16
(190, 'Báo hết hạn thanh toán liên tục?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-05 09:50:00', '2026-03-05 09:40:00', '2026-03-05 09:50:00', NULL, 15, 16),
(191, 'Bạn có 10 phút để hoàn tất thanh toán thôi.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 10:05:00', '2026-03-05 10:05:00', 190, 16, 15),
(192, 'Lần này được rồi.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-05 10:15:00', '2026-03-05 10:15:00', 191, 15, 16),

-- C3: Mem 5 & 6
(193, 'Có vé bản in lưu niệm không?', 'SEEN', NULL, NULL, 'TEXT', '2026-03-16 09:10:00', '2026-03-16 09:00:00', '2026-03-16 09:10:00', NULL, 5, 6),
(194, 'Có Postcard tặng kèm lúc quét QR cổng nha.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-16 09:25:00', '2026-03-16 09:25:00', 193, 6, 5),
(195, 'Tuyệt vời.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-03-16 09:35:00', '2026-03-16 09:35:00', 194, 5, 6),

-- C33: Mem 65 & 66
(196, 'Mua nhầm tài khoản, giúp chuyển vé.', 'SEEN', NULL, NULL, 'TEXT', '2026-04-05 09:55:00', '2026-04-05 09:45:00', '2026-04-05 09:55:00', NULL, 65, 66),
(197, 'Dùng tính năng "Tặng vé" cho email kia là xong.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-05 10:10:00', '2026-04-05 10:10:00', 196, 66, 65),

-- C8 (Tiếp): Mem 15 & 16
(198, 'Dạ ok ad.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 16:50:00', '2026-04-07 16:50:00', NULL, 15, 16),

-- C13: Mem 25 & 26
(199, 'Tài liệu đã được up.', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 17:00:00', '2026-04-07 17:00:00', NULL, 26, 25),

-- C1: Mem 1 & 2
(200, 'Cảm ơn ad hỗ trợ nhiệt tình!', 'RECEIVED', NULL, NULL, 'TEXT', NULL, '2026-04-07 17:15:00', '2026-04-07 17:15:00', NULL, 1, 2)
;

-- =====================================================================
-- BẢNG 3: UPDATE LATEST_MESSAGE (ĐÃ SỬA LẠI TOÀN BỘ LOGIC ID)
-- =====================================================================
UPDATE conversation SET latest_message_id = 200, updated_at = '2026-04-07 17:15:00' WHERE id = 1;
UPDATE conversation SET latest_message_id = 88, updated_at = '2026-04-06 11:20:00' WHERE id = 2;
UPDATE conversation SET latest_message_id = 195, updated_at = '2026-03-16 09:35:00' WHERE id = 3;
UPDATE conversation SET latest_message_id = 164, updated_at = '2026-03-20 11:55:00' WHERE id = 4;
UPDATE conversation SET latest_message_id = 168, updated_at = '2026-01-15 15:40:00' WHERE id = 5;
UPDATE conversation SET latest_message_id = 56, updated_at = '2026-01-10 09:45:00' WHERE id = 6;
UPDATE conversation SET latest_message_id = 60, updated_at = '2026-02-15 16:45:00' WHERE id = 7;
UPDATE conversation SET latest_message_id = 198, updated_at = '2026-04-07 16:50:00' WHERE id = 8;
UPDATE conversation SET latest_message_id = 183, updated_at = '2026-04-03 09:20:00' WHERE id = 9;
UPDATE conversation SET latest_message_id = 187, updated_at = '2026-02-21 09:45:00' WHERE id = 10;
UPDATE conversation SET latest_message_id = 189, updated_at = '2026-03-10 11:50:00' WHERE id = 11;
UPDATE conversation SET latest_message_id = 66, updated_at = '2026-01-25 15:25:00' WHERE id = 12;
UPDATE conversation SET latest_message_id = 199, updated_at = '2026-04-07 17:00:00' WHERE id = 13;
UPDATE conversation SET latest_message_id = 92, updated_at = '2026-03-05 17:05:00' WHERE id = 14;
UPDATE conversation SET latest_message_id = 70, updated_at = '2026-04-01 14:55:00' WHERE id = 15;
UPDATE conversation SET latest_message_id = 83, updated_at = '2026-04-05 09:40:00' WHERE id = 16;
UPDATE conversation SET latest_message_id = 124, updated_at = '2026-03-20 18:25:00' WHERE id = 17;
UPDATE conversation SET latest_message_id = 36, updated_at = '2026-01-10 20:00:00' WHERE id = 18; -- Mượn logic ban đầu
UPDATE conversation SET latest_message_id = 38, updated_at = '2026-02-25 15:30:00' WHERE id = 19;
UPDATE conversation SET latest_message_id = 40, updated_at = '2026-03-15 09:00:00' WHERE id = 20;
UPDATE conversation SET latest_message_id = 42, updated_at = '2026-01-05 14:45:00' WHERE id = 21;
UPDATE conversation SET latest_message_id = 100, updated_at = '2026-02-20 11:00:00' WHERE id = 22;
UPDATE conversation SET latest_message_id = 46, updated_at = '2026-03-10 09:45:00' WHERE id = 23;
UPDATE conversation SET latest_message_id = 98, updated_at = '2026-03-05 16:45:00' WHERE id = 24;
UPDATE conversation SET latest_message_id = 50, updated_at = '2026-02-10 15:00:00' WHERE id = 25;
UPDATE conversation SET latest_message_id = 132, updated_at = '2026-01-20 12:00:00' WHERE id = 26;
UPDATE conversation SET latest_message_id = 109, updated_at = '2026-01-20 11:35:00' WHERE id = 27;
UPDATE conversation SET latest_message_id = 174, updated_at = '2026-04-01 11:30:00' WHERE id = 28;
UPDATE conversation SET latest_message_id = 81, updated_at = '2026-03-10 08:55:00' WHERE id = 29;
UPDATE conversation SET latest_message_id = 120, updated_at = '2026-01-10 09:35:00' WHERE id = 30;
UPDATE conversation SET latest_message_id = 122, updated_at = '2026-02-10 15:30:00' WHERE id = 31;
UPDATE conversation SET latest_message_id = 114, updated_at = '2026-03-15 17:35:00' WHERE id = 32;
UPDATE conversation SET latest_message_id = 197, updated_at = '2026-04-05 10:10:00' WHERE id = 33;
UPDATE conversation SET latest_message_id = 116, updated_at = '2026-01-25 10:35:00' WHERE id = 34;
UPDATE conversation SET latest_message_id = 139, updated_at = '2026-02-10 16:05:00' WHERE id = 35;
UPDATE conversation SET latest_message_id = 142, updated_at = '2026-03-20 09:35:00' WHERE id = 36;
UPDATE conversation SET latest_message_id = 178, updated_at = '2026-03-05 10:10:00' WHERE id = 37;
UPDATE conversation SET latest_message_id = 94, updated_at = '2026-02-25 17:00:00' WHERE id = 38;
UPDATE conversation SET latest_message_id = 147, updated_at = '2026-03-10 17:05:00' WHERE id = 39;
UPDATE conversation SET latest_message_id = 150, updated_at = '2026-04-01 10:45:00' WHERE id = 40;