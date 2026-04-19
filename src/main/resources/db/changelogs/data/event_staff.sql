INSERT INTO event_staff (id, status, email, token, expired_at, message, rejection_message, created_at, updated_at, role_id, event_id, app_user_id) VALUES

-- Nhóm 1: TechFest Vietnam (App User ID = 2)
(1, 'ACTIVE', 'truong.btc@techfest.vn', NULL, NULL, NULL, NULL, '2024-03-10 10:00:00', '2024-03-10 10:00:00', 4, 1, 2),
(2, 'ACTIVE', 'truong.btc@techfest.vn', NULL, NULL, NULL, NULL, '2024-12-01 09:00:00', '2024-12-01 09:00:00', 4, 2, 2),
(3, 'ACTIVE', 'truong.btc@techfest.vn', NULL, NULL, NULL, NULL, '2025-01-20 14:00:00', '2025-01-20 14:00:00', 4, 3, 2),
(4, 'ACTIVE', 'truong.btc@techfest.vn', NULL, NULL, NULL, NULL, '2026-03-01 08:00:00', '2026-03-01 08:00:00', 4, 4, 2),
(67, 'ACTIVE', 'truong.btc@techfest.vn', NULL, NULL, NULL, NULL, '2026-04-04 09:00:00', '2026-04-04 09:00:00', 4, 67, 2),
(72, 'ACTIVE', 'truong.btc@techfest.vn', NULL, NULL, NULL, NULL, '2026-04-05 10:00:00', '2026-04-05 10:00:00', 4, 72, 2),

-- Nhóm 2: Vietnam Music Festival (App User ID = 3)
(5, 'ACTIVE', 'hong.tran@vmf.com', NULL, NULL, NULL, NULL, '2024-05-15 15:00:00', '2024-05-15 15:00:00', 4, 5, 3),
(6, 'ACTIVE', 'hong.tran@vmf.com', NULL, NULL, NULL, NULL, '2024-06-10 10:00:00', '2024-06-10 10:00:00', 4, 6, 3),
(7, 'ACTIVE', 'hong.tran@vmf.com', NULL, NULL, NULL, NULL, '2025-08-15 08:00:00', '2025-08-15 08:00:00', 4, 7, 3),
(8, 'ACTIVE', 'hong.tran@vmf.com', NULL, NULL, NULL, NULL, '2025-11-01 10:00:00', '2025-11-01 10:00:00', 4, 8, 3),
(96, 'ACTIVE', 'hong.tran@vmf.com', NULL, NULL, NULL, NULL, '2026-03-25 09:00:00', '2026-03-25 09:00:00', 4, 96, 3),

-- Nhóm 3: Alpha Event Company (App User ID = 7)
(9, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2025-01-20 15:00:00', '2025-01-20 15:00:00', 4, 9, 7),
(10, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2025-02-10 09:00:00', '2025-02-10 09:00:00', 4, 10, 7),
(11, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2025-06-05 10:00:00', '2025-06-05 10:00:00', 4, 11, 7),
(12, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2025-10-15 14:00:00', '2025-10-15 14:00:00', 4, 12, 7),
(47, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2025-05-10 08:00:00', '2025-05-10 08:00:00', 4, 47, 7),
(60, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2025-11-20 08:00:00', '2025-11-20 08:00:00', 4, 60, 7),
(74, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2026-03-12 10:00:00', '2026-03-12 10:00:00', 4, 74, 7),
(79, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2026-04-06 09:00:00', '2026-04-06 09:00:00', 4, 79, 7),
(95, 'ACTIVE', 'duchai.alpha@event.com', NULL, NULL, NULL, NULL, '2026-04-07 08:00:00', '2026-04-07 08:00:00', 4, 95, 7),

-- Nhóm 4: Saigon Indie Hub (App User ID = 13)
(13, 'ACTIVE', 'contact@indiehub.vn', NULL, NULL, NULL, NULL, '2025-05-10 10:00:00', '2025-05-10 10:00:00', 4, 13, 13),
(14, 'ACTIVE', 'contact@indiehub.vn', NULL, NULL, NULL, NULL, '2025-05-20 10:00:00', '2025-05-20 10:00:00', 4, 14, 13),
(15, 'ACTIVE', 'contact@indiehub.vn', NULL, NULL, NULL, NULL, '2025-06-01 09:00:00', '2025-06-01 09:00:00', 4, 15, 13),
(70, 'ACTIVE', 'contact@indiehub.vn', NULL, NULL, NULL, NULL, '2026-03-05 08:00:00', '2026-03-05 08:00:00', 4, 70, 13),
(82, 'ACTIVE', 'contact@indiehub.vn', NULL, NULL, NULL, NULL, '2026-03-01 09:00:00', '2026-03-01 09:00:00', 4, 82, 13),
(88, 'ACTIVE', 'contact@indiehub.vn', NULL, NULL, NULL, NULL, '2026-03-05 10:00:00', '2026-03-05 10:00:00', 4, 88, 13),

-- Nhóm 5: Hanoi Workshop Club (App User ID = 14)
(16, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2025-06-15 08:00:00', '2025-06-15 08:00:00', 4, 16, 14),
(17, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2025-08-20 10:00:00', '2025-08-20 10:00:00', 4, 17, 14),
(18, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2026-04-01 09:00:00', '2026-04-01 09:00:00', 4, 18, 14),
(48, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2026-04-01 09:00:00', '2026-04-01 09:00:00', 4, 48, 14),
(52, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2025-12-05 08:00:00', '2025-12-05 08:00:00', 4, 52, 14),
(63, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2026-03-01 09:00:00', '2026-03-01 09:00:00', 4, 63, 14),
(77, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2026-03-25 09:00:00', '2026-03-25 09:00:00', 4, 77, 14),
(94, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2026-03-12 10:00:00', '2026-03-12 10:00:00', 4, 94, 14),
(98, 'ACTIVE', 'hello@hanoiworkshop.com', NULL, NULL, NULL, NULL, '2026-03-20 10:00:00', '2026-03-20 10:00:00', 4, 98, 14),

-- Nhóm 6: Cloud Computing VN (App User ID = 16)
(19, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2025-10-10 14:00:00', '2025-10-10 14:00:00', 4, 19, 16),
(20, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2025-11-22 09:00:00', '2025-11-22 09:00:00', 4, 20, 16),
(21, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2026-01-10 08:00:00', '2026-01-10 08:00:00', 4, 21, 16),
(51, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2025-11-01 09:00:00', '2025-11-01 09:00:00', 4, 51, 16),
(83, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2026-04-06 08:00:00', '2026-04-06 08:00:00', 4, 83, 16),
(91, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2026-01-22 08:00:00', '2026-01-22 08:00:00', 4, 91, 16),
(100, 'ACTIVE', 'contact@cloudvn.vn', NULL, NULL, NULL, NULL, '2026-04-06 10:00:00', '2026-04-06 10:00:00', 4, 100, 16),

-- Nhóm 7: Danang Tech Run (App User ID = 17)
(22, 'ACTIVE', 'phong.nh@danangtech.vn', NULL, NULL, NULL, NULL, '2025-08-15 10:00:00', '2025-08-15 10:00:00', 4, 22, 17),
(23, 'ACTIVE', 'phong.nh@danangtech.vn', NULL, NULL, NULL, NULL, '2025-09-01 09:00:00', '2025-09-01 09:00:00', 4, 23, 17),
(69, 'ACTIVE', 'phong.nh@danangtech.vn', NULL, NULL, NULL, NULL, '2026-02-15 09:00:00', '2026-02-15 09:00:00', 4, 69, 17),
(78, 'ACTIVE', 'phong.nh@danangtech.vn', NULL, NULL, NULL, NULL, '2026-02-15 08:00:00', '2026-02-15 08:00:00', 4, 78, 17),

-- Nhóm 8: Saigon eSports (App User ID = 20)
(24, 'ACTIVE', 'haanh.do@esports.vn', NULL, NULL, NULL, NULL, '2025-09-20 15:00:00', '2025-09-20 15:00:00', 4, 24, 20),
(25, 'ACTIVE', 'haanh.do@esports.vn', NULL, NULL, NULL, NULL, '2026-01-15 08:00:00', '2026-01-15 08:00:00', 4, 25, 20),
(26, 'ACTIVE', 'haanh.do@esports.vn', NULL, NULL, NULL, NULL, '2026-03-10 10:00:00', '2026-03-10 10:00:00', 4, 26, 20),
(57, 'ACTIVE', 'haanh.do@esports.vn', NULL, NULL, NULL, NULL, '2026-04-02 08:00:00', '2026-04-02 08:00:00', 4, 57, 20),
(81, 'ACTIVE', 'haanh.do@esports.vn', NULL, NULL, NULL, NULL, '2025-11-25 10:00:00', '2025-11-25 10:00:00', 4, 81, 20),

-- Nhóm 9: VN Yoga Festival (App User ID = 21)
(27, 'ACTIVE', 'mai.nq@vnyoga.com', NULL, NULL, NULL, NULL, '2025-03-10 09:00:00', '2025-03-10 09:00:00', 4, 27, 21),
(28, 'ACTIVE', 'mai.nq@vnyoga.com', NULL, NULL, NULL, NULL, '2025-10-25 10:00:00', '2025-10-25 10:00:00', 4, 28, 21),
(85, 'ACTIVE', 'mai.nq@vnyoga.com', NULL, NULL, NULL, NULL, '2026-03-25 10:00:00', '2026-03-25 10:00:00', 4, 85, 21),
(97, 'ACTIVE', 'mai.nq@vnyoga.com', NULL, NULL, NULL, NULL, '2026-02-05 08:00:00', '2026-02-05 08:00:00', 4, 97, 21),

-- Nhóm 10: EduTech Hội Thảo (App User ID = 23)
(29, 'ACTIVE', 'nghia.bt@edutech.vn', NULL, NULL, NULL, NULL, '2025-12-10 08:00:00', '2025-12-10 08:00:00', 4, 29, 23),
(30, 'ACTIVE', 'nghia.bt@edutech.vn', NULL, NULL, NULL, NULL, '2026-04-05 09:00:00', '2026-04-05 09:00:00', 4, 30, 23),
(55, 'ACTIVE', 'nghia.bt@edutech.vn', NULL, NULL, NULL, NULL, '2026-02-05 09:00:00', '2026-02-05 09:00:00', 4, 55, 23),
(64, 'ACTIVE', 'nghia.bt@edutech.vn', NULL, NULL, NULL, NULL, '2026-01-20 10:00:00', '2026-01-20 10:00:00', 4, 64, 23),
(86, 'ACTIVE', 'nghia.bt@edutech.vn', NULL, NULL, NULL, NULL, '2025-11-05 09:00:00', '2025-11-05 09:00:00', 4, 86, 23),

-- Nhóm 11: CineViet Nam (App User ID = 25)
(31, 'ACTIVE', 'tai.lt@cineviet.vn', NULL, NULL, NULL, NULL, '2026-02-25 14:00:00', '2026-02-25 14:00:00', 4, 31, 25),
(32, 'ACTIVE', 'tai.lt@cineviet.vn', NULL, NULL, NULL, NULL, '2026-03-15 08:00:00', '2026-03-15 08:00:00', 4, 32, 25),

-- Nhóm 12: Food Fest VN (App User ID = 26)
(33, 'ACTIVE', 'contact@foodfest.vn', NULL, NULL, NULL, NULL, '2025-02-20 10:00:00', '2025-02-20 10:00:00', 4, 33, 26),
(34, 'ACTIVE', 'contact@foodfest.vn', NULL, NULL, NULL, NULL, '2025-04-10 09:00:00', '2025-04-10 09:00:00', 4, 34, 26),
(76, 'ACTIVE', 'contact@foodfest.vn', NULL, NULL, NULL, NULL, '2026-01-20 10:00:00', '2026-01-20 10:00:00', 4, 76, 26),

-- Nhóm 13: KidCamp Asia (App User ID = 27)
(35, 'ACTIVE', 'hello@kidcamp.asia', NULL, NULL, NULL, NULL, '2025-03-15 08:00:00', '2025-03-15 08:00:00', 4, 35, 27),
(75, 'ACTIVE', 'hello@kidcamp.asia', NULL, NULL, NULL, NULL, '2025-12-05 08:00:00', '2025-12-05 08:00:00', 4, 75, 27),
(89, 'ACTIVE', 'hello@kidcamp.asia', NULL, NULL, NULL, NULL, '2026-02-12 09:00:00', '2026-02-12 09:00:00', 4, 89, 27),
(93, 'ACTIVE', 'hello@kidcamp.asia', NULL, NULL, NULL, NULL, '2026-02-05 08:00:00', '2026-02-05 08:00:00', 4, 93, 27),

-- Nhóm 14: Saigon Beer Festival (App User ID = 29)
(36, 'ACTIVE', 'info@sgbeerfest.vn', NULL, NULL, NULL, NULL, '2025-04-20 15:00:00', '2025-04-20 15:00:00', 4, 36, 29),

-- Nhóm 15: EdTech Summit VN (App User ID = 31)
(37, 'ACTIVE', 'contact@edutechsummit.vn', NULL, NULL, NULL, NULL, '2025-05-25 09:00:00', '2025-05-25 09:00:00', 4, 37, 31),

-- Nhóm 16: Family Weekend Guide (App User ID = 32)
(38, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2025-06-10 10:00:00', '2025-06-10 10:00:00', 4, 38, 32),
(39, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2026-03-25 08:00:00', '2026-03-25 08:00:00', 4, 39, 32),
(56, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2026-02-20 10:00:00', '2026-02-20 10:00:00', 4, 56, 32),
(59, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2026-03-05 09:00:00', '2026-03-05 09:00:00', 4, 59, 32),
(65, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2026-02-10 08:00:00', '2026-02-10 08:00:00', 4, 65, 32),
(71, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2025-11-10 09:00:00', '2025-11-10 09:00:00', 4, 71, 32),
(73, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2026-02-25 09:00:00', '2026-02-25 09:00:00', 4, 73, 32),
(80, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2026-01-20 08:00:00', '2026-01-20 08:00:00', 4, 80, 32),
(92, 'ACTIVE', 'admin@familyweekend.vn', NULL, NULL, NULL, NULL, '2025-10-15 09:00:00', '2025-10-15 09:00:00', 4, 92, 32),

-- Nhóm 17: Blockchain VN Expo (App User ID = 34)
(40, 'ACTIVE', 'info@blockchainexpo.vn', NULL, NULL, NULL, NULL, '2025-07-20 09:00:00', '2025-07-20 09:00:00', 4, 40, 34),
(61, 'ACTIVE', 'info@blockchainexpo.vn', NULL, NULL, NULL, NULL, '2026-04-03 09:00:00', '2026-04-03 09:00:00', 4, 61, 34),
(99, 'ACTIVE', 'info@blockchainexpo.vn', NULL, NULL, NULL, NULL, '2026-03-25 14:00:00', '2026-03-25 14:00:00', 4, 99, 34),

-- Nhóm 18: Color Me Run (App User ID = 36)
(41, 'ACTIVE', 'hello@colormerun.vn', NULL, NULL, NULL, NULL, '2025-01-15 10:00:00', '2025-01-15 10:00:00', 4, 41, 36),
(62, 'ACTIVE', 'hello@colormerun.vn', NULL, NULL, NULL, NULL, '2026-02-15 08:00:00', '2026-02-15 08:00:00', 4, 62, 36),

-- Nhóm 19: Bách Hóa Design (App User ID = 37)
(42, 'ACTIVE', 'contact@bachhoadesign.vn', NULL, NULL, NULL, NULL, '2025-09-20 09:00:00', '2025-09-20 09:00:00', 4, 42, 37),
(43, 'ACTIVE', 'contact@bachhoadesign.vn', NULL, NULL, NULL, NULL, '2025-11-10 09:00:00', '2025-11-10 09:00:00', 4, 43, 37),
(49, 'ACTIVE', 'contact@bachhoadesign.vn', NULL, NULL, NULL, NULL, '2025-09-01 08:00:00', '2025-09-01 08:00:00', 4, 49, 37),
(53, 'ACTIVE', 'contact@bachhoadesign.vn', NULL, NULL, NULL, NULL, '2026-03-20 09:00:00', '2026-03-20 09:00:00', 4, 53, 37),
(66, 'ACTIVE', 'contact@bachhoadesign.vn', NULL, NULL, NULL, NULL, '2026-03-10 10:00:00', '2026-03-10 10:00:00', 4, 66, 37),

-- Nhóm 20: Green Life Charity (App User ID = 39)
(44, 'ACTIVE', 'donate@greenlife.vn', NULL, NULL, NULL, NULL, '2025-11-15 09:00:00', '2025-11-15 09:00:00', 4, 44, 39),
(50, 'ACTIVE', 'donate@greenlife.vn', NULL, NULL, NULL, NULL, '2025-10-10 10:00:00', '2025-10-10 10:00:00', 4, 50, 39),
(54, 'ACTIVE', 'donate@greenlife.vn', NULL, NULL, NULL, NULL, '2026-01-10 08:00:00', '2026-01-10 08:00:00', 4, 54, 39),
(68, 'ACTIVE', 'donate@greenlife.vn', NULL, NULL, NULL, NULL, '2026-01-25 10:00:00', '2026-01-25 10:00:00', 4, 68, 39),
(84, 'ACTIVE', 'donate@greenlife.vn', NULL, NULL, NULL, NULL, '2026-02-15 08:00:00', '2026-02-15 08:00:00', 4, 84, 39),
(87, 'ACTIVE', 'donate@greenlife.vn', NULL, NULL, NULL, NULL, '2026-01-10 08:00:00', '2026-01-10 08:00:00', 4, 87, 39),

-- Nhóm 21: Vietnam Coffee Expo (App User ID = 40)
(45, 'ACTIVE', 'expo@vietnamcoffee.vn', NULL, NULL, NULL, NULL, '2025-12-15 09:00:00', '2025-12-15 09:00:00', 4, 45, 40),
(46, 'ACTIVE', 'expo@vietnamcoffee.vn', NULL, NULL, NULL, NULL, '2026-01-10 10:00:00', '2026-01-10 10:00:00', 4, 46, 40),
(90, 'ACTIVE', 'expo@vietnamcoffee.vn', NULL, NULL, NULL, NULL, '2026-04-04 10:00:00', '2026-04-04 10:00:00', 4, 90, 40);