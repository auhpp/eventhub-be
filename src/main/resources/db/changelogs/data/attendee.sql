
INSERT INTO attendee (id, ticket_code, check_in_at, price, discount_amount, final_price, status, type, source_type,
                      created_at, updated_at, booking_id, ticket_id, event_staff_id, owner_id, resale_post_id,
                      parent_attendee_id)
VALUES

-- 1. Booking ID 1 (Event_Invitation 1: User Vũ, Ticket 3, Qty: 2) -> Sinh 2 vé
(1, 'TCK-VIP1-V01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-02 08:30:00', '2026-03-02 08:30:00',
 1, 3, NULL, 4, NULL, NULL),
(2, 'TCK-VIP1-V02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-02 08:30:00', '2026-03-02 08:30:00',
 1, 3, NULL, 4, NULL, NULL),

-- 2. Booking ID 2 (Event_Invitation 29: User Tâm, Ticket 2, Qty: 1)
(3, 'TCK-STD1-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-16 09:15:00', '2026-03-16 09:15:00',
 2, 2, NULL, 5, NULL, NULL),

-- 3. Booking ID 3 (Event_Invitation 30: User Bảo, Ticket 2, Qty: 2)
(4, 'TCK-STD1-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-17 10:00:00', '2026-03-17 10:00:00',
 3, 2, NULL, 19, NULL, NULL),
(5, 'TCK-STD1-B02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-17 10:00:00', '2026-03-17 10:00:00',
 3, 2, NULL, 19, NULL, NULL),

-- 4. Booking ID 4 (Event_Invitation 31: User Minh, Ticket 2, Qty: 1)
(6, 'TCK-STD1-M01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-16 11:30:00', '2026-03-16 11:30:00',
 4, 2, NULL, 57, NULL, NULL),

-- 5. Booking ID 5 (Event_Invitation 4: User Bằng, Ticket 5, Qty: 1)
(7, 'TCK-SHK-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-16 09:00:00', '2026-03-16 09:00:00',
 5, 5, NULL, 88, NULL, NULL),

-- 6. Booking ID 6 (Event_Invitation 32: User Hùng, Ticket 5, Qty: 1)
(8, 'TCK-SHK-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-02 14:00:00', '2026-04-02 14:00:00',
 6, 5, NULL, 69, NULL, NULL),

-- 7. Booking ID 7 (Event_Invitation 33: User Tuấn, Ticket 5, Qty: 1)
(9, 'TCK-SHK-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 08:45:00', '2026-04-03 08:45:00',
 7, 5, NULL, 65, NULL, NULL),

-- 8. Booking ID 8 (Event_Invitation 7: User Thảo, Ticket 11, Qty: 2)
(10, 'TCK-VMF-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-16 11:00:00', '2026-03-16 11:00:00',
 8, 11, NULL, 8, NULL, NULL),
(11, 'TCK-VMF-T02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-16 11:00:00', '2026-03-16 11:00:00',
 8, 11, NULL, 8, NULL, NULL),

-- 9. Booking ID 9 (Event_Invitation 34: User Ly, Ticket 13, Qty: 1)
(12, 'TCK-VMF2-L01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-21 09:00:00', '2026-03-21 09:00:00',
 9, 13, NULL, 66, NULL, NULL),

-- 10. Booking ID 10 (Event_Invitation 35: User Dũng, Ticket 13, Qty: 1)
(13, 'TCK-VMF2-D01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-22 15:30:00', '2026-03-22 15:30:00',
 10, 13, NULL, 51, NULL, NULL),

-- 11. Booking ID 11 (Event_Invitation 36: User Tùng, Ticket 14, Qty: 2)
(14, 'TCK-VMF3-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-23 11:00:00', '2026-03-23 11:00:00',
 11, 14, NULL, 67, NULL, NULL),
(15, 'TCK-VMF3-T02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-23 11:00:00', '2026-03-23 11:00:00',
 11, 14, NULL, 67, NULL, NULL),

-- 12. Booking ID 12 (Event_Invitation 37: User Vũ, Ticket 14, Qty: 2)
(16, 'TCK-VMF3-V01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-21 14:20:00', '2026-03-21 14:20:00',
 12, 14, NULL, 4, NULL, NULL),
(17, 'TCK-VMF3-V02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-21 14:20:00', '2026-03-21 14:20:00',
 12, 14, NULL, 4, NULL, NULL),

-- 13. Booking ID 13 (Event_Invitation 38: User Hiếu, Ticket 14, Qty: 1)
(18, 'TCK-VMF3-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-24 08:15:00', '2026-03-24 08:15:00',
 13, 14, NULL, 91, NULL, NULL),

-- 14. Booking ID 14 (Event_Invitation 9: User Hùng, Ticket 18, Qty: 1)
(19, 'TCK-CEO-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-11 14:20:00', '2026-03-11 14:20:00',
 14, 18, NULL, 69, NULL, NULL),

-- 15. Booking ID 15 (Event_Invitation 39: User Hùng, Ticket 18, Qty: 1)
(20, 'TCK-CEO-H02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-02 09:00:00', '2026-04-02 09:00:00',
 15, 18, NULL, 82, NULL, NULL),

-- 16. Booking ID 16 (Event_Invitation 40: User Bằng, Ticket 18, Qty: 1)
(21, 'TCK-CEO-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 16:45:00', '2026-04-03 16:45:00',
 16, 18, NULL, 88, NULL, NULL),

-- 17. Booking ID 17 (Event_Invitation 41: User Tùng, Ticket 24, Qty: 2)
(22, 'TCK-ROCK-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:30:00', '2026-04-06 14:30:00',
 17, 24, NULL, 43, NULL, NULL),
(23, 'TCK-ROCK-T02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:30:00', '2026-04-06 14:30:00',
 17, 24, NULL, 43, NULL, NULL),

-- 18. Booking ID 18 (Event_Invitation 42: User Hoàng, Ticket 24, Qty: 1)
(24, 'TCK-ROCK-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 09:15:00', '2026-04-07 09:15:00',
 18, 24, NULL, 63, NULL, NULL),

-- 19. Booking ID 19 (Event_Invitation 13: User Nhi, Ticket 43, Qty: 2)
(25, 'TCK-FAS-N01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-06 15:30:00', '2026-03-06 15:30:00',
 19, 43, NULL, 77, NULL, NULL),
(26, 'TCK-FAS-N02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-06 15:30:00', '2026-03-06 15:30:00',
 19, 43, NULL, 77, NULL, NULL),

-- 20. Booking ID 20 (Event_Invitation 43: User Giang, Ticket 43, Qty: 1)
(27, 'TCK-FAS-G01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-02 11:20:00', '2026-04-02 11:20:00',
 20, 43, NULL, 96, NULL, NULL),

-- 21. Booking ID 21 (Event_Invitation 44: User Bích, Ticket 43, Qty: 2)
(28, 'TCK-FAS-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 15:45:00', '2026-04-03 15:45:00',
 21, 43, NULL, 90, NULL, NULL),
(29, 'TCK-FAS-B02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 15:45:00', '2026-04-03 15:45:00',
 21, 43, NULL, 90, NULL, NULL),

-- 22. Booking ID 22 (Event_Invitation 15: User Mai, Ticket 50, Qty: 2)
(30, 'TCK-FOOD-M01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-11 08:30:00', '2026-03-11 08:30:00',
 22, 50, NULL, 6, NULL, NULL),
(31, 'TCK-FOOD-M02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-11 08:30:00', '2026-03-11 08:30:00',
 22, 50, NULL, 6, NULL, NULL),

-- 23. Booking ID 23 (Event_Invitation 45: User Linh, Ticket 50, Qty: 2)
(32, 'TCK-FOOD-L01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 12:30:00', '2026-04-06 12:30:00',
 23, 50, NULL, 45, NULL, NULL),
(33, 'TCK-FOOD-L02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 12:30:00', '2026-04-06 12:30:00',
 23, 50, NULL, 45, NULL, NULL),

-- 24. Booking ID 24 (Event_Invitation 46: User Anh, Ticket 50, Qty: 1)
(34, 'TCK-FOOD-A01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 09:10:00', '2026-04-07 09:10:00',
 24, 50, NULL, 83, NULL, NULL),

-- 25. Booking ID 25 (Event_Invitation 47: User Lan, Ticket 67, Qty: 2)
(35, 'TCK-EDU-L01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 14:20:00', '2026-04-07 14:20:00',
 25, 67, NULL, 50, NULL, NULL),
(36, 'TCK-EDU-L02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 14:20:00', '2026-04-07 14:20:00',
 25, 67, NULL, 50, NULL, NULL),

-- 26. Booking ID 26 (Event_Invitation 48: User Dũng, Ticket 67, Qty: 2)
(37, 'TCK-EDU-D01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 16:45:00', '2026-04-07 16:45:00',
 26, 67, NULL, 74, NULL, NULL),
(38, 'TCK-EDU-D02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 16:45:00', '2026-04-07 16:45:00',
 26, 67, NULL, 74, NULL, NULL),

-- 27. Booking ID 27 (Event_Invitation 18: User Đạt, Ticket 105, Qty: 2)
(39, 'TCK-COF-D01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-06 10:30:00', '2026-03-06 10:30:00',
 27, 105, NULL, 33, NULL, NULL),
(40, 'TCK-COF-D02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-03-06 10:30:00', '2026-03-06 10:30:00',
 27, 105, NULL, 33, NULL, NULL),

-- 28. Booking ID 28 (Event_Invitation 49: User An, Ticket 105, Qty: 2)
(41, 'TCK-COF-A01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 08:30:00', '2026-04-06 08:30:00',
 28, 105, NULL, 41, NULL, NULL),
(42, 'TCK-COF-A02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 08:30:00', '2026-04-06 08:30:00',
 28, 105, NULL, 41, NULL, NULL),

-- 29. Booking ID 29 (Event_Invitation 50: User Vinh, Ticket 105, Qty: 2)
(43, 'TCK-COF-V01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 11:20:00', '2026-04-06 11:20:00',
 29, 105, NULL, 76, NULL, NULL),
(44, 'TCK-COF-V02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 11:20:00', '2026-04-06 11:20:00',
 29, 105, NULL, 76, NULL, NULL),

-- 30. Booking ID 30 (Event_Invitation 51: User Hà, Ticket 105, Qty: 2)
(45, 'TCK-COF-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 09:15:00', '2026-04-07 09:15:00',
 30, 105, NULL, 81, NULL, NULL),
(46, 'TCK-COF-H02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 09:15:00', '2026-04-07 09:15:00',
 30, 105, NULL, 81, NULL, NULL),

-- 31. Booking ID 31 (Event_Invitation 52: User Ngân, Ticket 114, Qty: 4)
(47, 'TCK-KITE-N01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:30:00', '2026-04-06 14:30:00',
 31, 114, NULL, 64, NULL, NULL),
(48, 'TCK-KITE-N02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:30:00', '2026-04-06 14:30:00',
 31, 114, NULL, 64, NULL, NULL),
(49, 'TCK-KITE-N03', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:30:00', '2026-04-06 14:30:00',
 31, 114, NULL, 64, NULL, NULL),
(50, 'TCK-KITE-N04', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:30:00', '2026-04-06 14:30:00',
 31, 114, NULL, 64, NULL, NULL),

-- 32. Booking ID 32 (Event_Invitation 53: User Anh, Ticket 114, Qty: 3)
(51, 'TCK-KITE-A01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 08:20:00', '2026-04-07 08:20:00',
 32, 114, NULL, 98, NULL, NULL),
(52, 'TCK-KITE-A02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 08:20:00', '2026-04-07 08:20:00',
 32, 114, NULL, 98, NULL, NULL),
(53, 'TCK-KITE-A03', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 08:20:00', '2026-04-07 08:20:00',
 32, 114, NULL, 98, NULL, NULL),

-- 33. Booking ID 33 (Event_Invitation 54: User Hùng, Ticket 33, Qty: 1)
(54, 'TCK-RUN-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-02 10:30:00', '2026-04-02 10:30:00',
 33, 33, NULL, 44, NULL, NULL),

-- 34. Booking ID 34 (Event_Invitation 55: User Tuấn, Ticket 33, Qty: 1)
(55, 'TCK-RUN-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 08:15:00', '2026-04-03 08:15:00',
 34, 33, NULL, 30, NULL, NULL),

-- 35. Booking ID 35 (Event_Invitation 56: User Bảo, Ticket 33, Qty: 2)
(56, 'TCK-RUN-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-04 15:40:00', '2026-04-04 15:40:00',
 35, 33, NULL, 86, NULL, NULL),
(57, 'TCK-RUN-B02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-04 15:40:00', '2026-04-04 15:40:00',
 35, 33, NULL, 86, NULL, NULL),

-- 36. Booking ID 36 (Event_Invitation 58: User Kiên, Ticket 75, Qty: 2)
(58, 'TCK-BSK-K01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 09:30:00', '2026-04-06 09:30:00',
 36, 75, NULL, 61, NULL, NULL),
(59, 'TCK-BSK-K02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 09:30:00', '2026-04-06 09:30:00',
 36, 75, NULL, 61, NULL, NULL),

-- 37. Booking ID 37 (Event_Invitation 59: User Kiên 49, Ticket 75, Qty: 2)
(60, 'TCK-BSK-K03', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:15:00', '2026-04-06 14:15:00',
 37, 75, NULL, 49, NULL, NULL),
(61, 'TCK-BSK-K04', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:15:00', '2026-04-06 14:15:00',
 37, 75, NULL, 49, NULL, NULL),

-- 38. Booking ID 38 (Event_Invitation 60: User Long, Ticket 75, Qty: 2)
(62, 'TCK-BSK-L01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 10:50:00', '2026-04-07 10:50:00',
 38, 75, NULL, 97, NULL, NULL),
(63, 'TCK-BSK-L02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 10:50:00', '2026-04-07 10:50:00',
 38, 75, NULL, 97, NULL, NULL),

-- 39. Booking ID 39 (Event_Invitation 62: User Huyền, Ticket 84, Qty: 1)
(64, 'TCK-NP-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 09:00:00', '2026-04-03 09:00:00',
 39, 84, NULL, 68, NULL, NULL),

-- 40. Booking ID 40 (Event_Invitation 63: User Ngọc, Ticket 84, Qty: 1)
(65, 'TCK-NP-N01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-04 11:20:00', '2026-04-04 11:20:00',
 40, 84, NULL, 24, NULL, NULL),

-- 41. Booking ID 41 (Event_Invitation 64: User Đăng, Ticket 44, Qty: 2)
(66, 'TCK-MOV-D01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 08:30:00', '2026-04-06 08:30:00',
 41, 44, NULL, 38, NULL, NULL),
(67, 'TCK-MOV-D02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 08:30:00', '2026-04-06 08:30:00',
 41, 44, NULL, 38, NULL, NULL),

-- 42. Booking ID 42 (Event_Invitation 65: User Phượng, Ticket 44, Qty: 2)
(68, 'TCK-MOV-P01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:45:00', '2026-04-06 14:45:00',
 42, 44, NULL, 92, NULL, NULL),
(69, 'TCK-MOV-P02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 14:45:00', '2026-04-06 14:45:00',
 42, 44, NULL, 92, NULL, NULL),

-- 43. Booking ID 43 (Event_Invitation 66: User Phụng, Ticket 44, Qty: 1)
(70, 'TCK-MOV-P03', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 09:20:00', '2026-04-07 09:20:00',
 43, 44, NULL, 80, NULL, NULL),

-- 44. Booking ID 44 (Event_Invitation 68: User Dũng, Ticket 50, Qty: 2)
(71, 'TCK-BEER-D01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 09:10:00', '2026-04-06 09:10:00',
 44, 50, NULL, 51, NULL, NULL),
(72, 'TCK-BEER-D02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 09:10:00', '2026-04-06 09:10:00',
 44, 50, NULL, 51, NULL, NULL),

-- 45. Booking ID 45 (Event_Invitation 69: User Hoàng, Ticket 50, Qty: 2)
(73, 'TCK-BEER-H01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 15:40:00', '2026-04-06 15:40:00',
 45, 50, NULL, 63, NULL, NULL),
(74, 'TCK-BEER-H02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 15:40:00', '2026-04-06 15:40:00',
 45, 50, NULL, 63, NULL, NULL),

-- 46. Booking ID 46 (Event_Invitation 70: User Đạt, Ticket 50, Qty: 2)
(75, 'TCK-BEER-D03', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 10:20:00', '2026-04-07 10:20:00',
 46, 50, NULL, 78, NULL, NULL),
(76, 'TCK-BEER-D04', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 10:20:00', '2026-04-07 10:20:00',
 46, 50, NULL, 78, NULL, NULL),

-- 47. Booking ID 47 (Event_Invitation 71: User EdTech, Ticket 51, Qty: 1)
(77, 'TCK-AI-E01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-02 08:30:00', '2026-04-02 08:30:00',
 47, 51, NULL, 23, NULL, NULL),

-- 48. Booking ID 48 (Event_Invitation 72: User Tâm, Ticket 51, Qty: 1)
(78, 'TCK-AI-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-03 14:15:00', '2026-04-03 14:15:00',
 48, 51, NULL, 100, NULL, NULL),

-- 49. Booking ID 49 (Event_Invitation 73: User Bảo, Ticket 51, Qty: 2)
(79, 'TCK-AI-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-04 09:50:00', '2026-04-04 09:50:00',
 49, 51, NULL, 19, NULL, NULL),
(80, 'TCK-AI-B02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-04 09:50:00', '2026-04-04 09:50:00',
 49, 51, NULL, 19, NULL, NULL),

-- 50. Booking ID 50 (Event_Invitation 74: User Lan, Ticket 51, Qty: 1)
(81, 'TCK-AI-L01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-05 11:20:00', '2026-04-05 11:20:00',
 50, 51, NULL, 50, NULL, NULL),

-- 51. Booking ID 51 (Event_Invitation 75: User Minh, Ticket 42, Qty: 1)
(82, 'TCK-DSG-M01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 09:15:00', '2026-04-06 09:15:00',
 51, 42, NULL, 18, NULL, NULL),

-- 52. Booking ID 52 (Event_Invitation 76: User Thảo, Ticket 42, Qty: 2)
(83, 'TCK-DSG-T01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 08:40:00', '2026-04-07 08:40:00',
 52, 42, NULL, 54, NULL, NULL),
(84, 'TCK-DSG-T02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 08:40:00', '2026-04-07 08:40:00',
 52, 42, NULL, 54, NULL, NULL),

-- 53. Booking ID 53 (Event_Invitation 77: User Trí, Ticket 42, Qty: 1)
(85, 'TCK-DSG-T03', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 14:50:00', '2026-04-07 14:50:00',
 53, 42, NULL, 47, NULL, NULL),

-- 54. Booking ID 54 (Event_Invitation 78: User Oanh, Ticket 48, Qty: 2)
(86, 'TCK-CMP-O01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 10:20:00', '2026-04-06 10:20:00',
 54, 48, NULL, 75, NULL, NULL),
(87, 'TCK-CMP-O02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 10:20:00', '2026-04-06 10:20:00',
 54, 48, NULL, 75, NULL, NULL),

-- 55. Booking ID 55 (Event_Invitation 79: User Bảo, Ticket 24, Qty: 2)
(88, 'TCK-IND-B01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 15:30:00', '2026-04-06 15:30:00',
 55, 24, NULL, 42, NULL, NULL),
(89, 'TCK-IND-B02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-06 15:30:00', '2026-04-06 15:30:00',
 55, 24, NULL, 42, NULL, NULL),

-- 56. Booking ID 56 (Event_Invitation 80: User Anh, Ticket 39, Qty: 2)
(90, 'TCK-FFT-A01', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 11:15:00', '2026-04-07 11:15:00',
 56, 39, NULL, 83, NULL, NULL),
(91, 'TCK-FFT-A02', NULL, 0.0, 0.0, 0.0, 'VALID', 'INVITE', 'INVITATION', '2026-04-07 11:15:00', '2026-04-07 11:15:00',
 56, 39, NULL, 83, NULL, NULL);
