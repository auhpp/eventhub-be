package com.auhpp.event_management.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtils {
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String noWhiteSpace = input.trim().toLowerCase();

        //Khử dấu tiếng Việt (Sử dụng Normalizer)
        //Loại bỏ các dấu thanh (sắc, huyền, hỏi, ngã, nặng) và dấu mũ (â, ă, ê, ô, ơ, ư).
        //Tách một ký tự có dấu thành 2 phần: ký tự gốc và dấu. Ví dụ: Chữ ế sẽ bị tách thành chữ e và các dấu ^, ´
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        //tìm tất cả các "dấu" vừa được tách ra ở bước trên.
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        //Xóa tất cả các dấu đó đi, chỉ để lại ký tự gốc (chữ e).
        String noAccents = pattern.matcher(normalized).replaceAll("");

        //Xử lý trường hợp đặc biệt: Chữ 'đ'
        noAccents = noAccents.replace("đ", "d");
        //Thay thế các ký tự đặc biệt bằng dấu gạch ngang
        String slug = noAccents.replaceAll("[^a-z0-9]+", "-");
        //Dọn dẹp dấu gạch ngang thừa
        return slug.replaceAll("^-|-$", "");
    }
}
