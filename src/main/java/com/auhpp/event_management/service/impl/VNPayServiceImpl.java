package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.configuration.VNPayConfiguration;
import com.auhpp.event_management.constant.WalletType;
import com.auhpp.event_management.dto.request.BookingPaymentRequest;
import com.auhpp.event_management.dto.request.CheckPaymentRequest;
import com.auhpp.event_management.dto.response.BookingConfirmPaymentResponse;
import com.auhpp.event_management.dto.response.BookingResponse;
import com.auhpp.event_management.dto.response.VnPayQueryDrResponse;
import com.auhpp.event_management.service.BookingService;
import com.auhpp.event_management.service.VNPayService;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VNPayServiceImpl implements VNPayService {
    private final VNPayConfiguration vnpConfig;
    private final BookingService bookingService;
    private final RestClient restClient;

    public VNPayServiceImpl(@Qualifier("vnPayRestClient") RestClient restClient,
                            VNPayConfiguration vnPayConfiguration,
                            BookingService bookingService) {
        this.restClient = restClient;
        this.vnpConfig = vnPayConfiguration;
        this.bookingService = bookingService;
    }

    @Override
    public String createPaymentUrl(Long bookingId, BookingPaymentRequest request, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        Double finalAmount = bookingService.calculateFinalAmount(bookingId, request);
        if (finalAmount == 0) {
            bookingService.updatePaymentInfoBooking(bookingId, request);
            bookingService.updatePaymentBooking(bookingId);
            return "";
        }
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "booking-type";
        String vnp_IpAddr = vnpConfig.getIpAddress(httpServletRequest);
        String vnp_TmnCode = vnpConfig.vnp_TmnCode;

        long currentTime = System.currentTimeMillis();
        String vnp_TxnRef = bookingId + "_" + currentTime;

        // Update payment info for booking
        request.setFinalAmount(finalAmount);
        request.setTransactionId(vnp_TxnRef);
        bookingService.updatePaymentInfoBooking(bookingId, request);

        long amountLong = (long) (finalAmount * 100);
        String amount = String.valueOf(amountLong);
        String otherInfo = "Thanh toan don hang " + finalAmount;
        Map<String, Object> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", otherInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        vnp_Params.put("vnp_ReturnUrl", vnpConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = LocalDateTime.now().format(formatter);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // calculate expire time
        BookingResponse bookingResponse = bookingService.getBookingById(bookingId);
        String vnp_ExpireDate = bookingResponse.getExpiredAt().format(formatter);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        //Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpConfig.hmacSHA512(vnpConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnpConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @Override
    public BookingConfirmPaymentResponse confirmPaymentBooking(CheckPaymentRequest checkPaymentRequest,
                                                               HttpServletRequest httpServletRequest) {
        String randomUUID = UUID.randomUUID().toString();
        String vnp_RequestId = randomUUID.replace("-", "");
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = vnpConfig.vnp_TmnCode;
        String vnp_TxnRef = checkPaymentRequest.getVnpTxnRef();
        String vnp_OrderInfo = "Kiem tra thanh toan GD OrderId:" + vnp_TxnRef;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        String vnp_TransactionDate = formatter.format(cld.getTime());

        String vnp_IpAddr = vnpConfig.getIpAddress(httpServletRequest);

        Map<String, Object> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_RequestId", vnp_RequestId);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);


        vnp_Params.put("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TxnRef, vnp_TransactionDate,
                vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = vnpConfig.hmacSHA512(vnpConfig.vnp_HashSecret, hash_Data);

        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        Gson gson = new Gson();
        String jsonBody = gson.toJson(vnp_Params);
        VnPayQueryDrResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonBody)
                .retrieve()
                .body(VnPayQueryDrResponse.class);
        BookingResponse bookingResponse;
        if (response != null) {
            bookingResponse = bookingService.getBookingByTransactionId(vnp_TxnRef, WalletType.VNPay);
            if (response.getTransactionStatus().equals("00")) {
                bookingResponse = bookingService.updatePaymentBooking(bookingResponse.getId());
            }
            return BookingConfirmPaymentResponse.builder()
                    .transactionCode(response.getTransactionStatus())
                    .booking(
                            bookingResponse
                    )
                    .build();
        }
        return null;
    }
}
