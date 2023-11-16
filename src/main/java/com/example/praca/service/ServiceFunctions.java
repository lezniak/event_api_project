package com.example.praca.service;

import com.example.praca.PracaApplication;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Daniel Lezniak
 */
public class ServiceFunctions<T> {
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String PHONE_NUMBER_REGEX = "(0|91)?[7-9][0-9]{9}";

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean validEmail(String email) {
        if (isNull(email))
            return false;
        Pattern pattern = Pattern.compile(EMAIL_REGEX,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    public static boolean validPhoneNumber(String phoneNumber) {
        if (isNull(phoneNumber))
            return false;
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.find();
    }

    public static boolean isNull(String text) {
        if (StringUtil.isNullOrEmpty(text))
            return true;
        return text.isBlank() || text.isEmpty();
    }

    public static <T> boolean isNull(T obj) {
        return obj == null;
    }

    public static Timestamp parseTimestamp(String timestamp) {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(timestamp).getTime());
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Timestamp exception: " + ex.getMessage());
        }
    }

    public static String[] parseToArray(String value) {
        return value.split("-");
    }

    public static boolean dateBefore(Timestamp startDate) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return startDate.before(now);
    }

    public static Date dateTimeToDate(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date stringToDate(String date) {
        String correctDate = null;
        if (date.contains(".")) {
            correctDate = date.replaceAll("\\.", "-");
        } else {
            correctDate = date;
        }

        try {
            return DATE_FORMAT.parse(correctDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static MultipartFile convertByteArrayToMultiPartFile(byte[] image) {
        InputStream inputStream = new ByteArrayInputStream(image);
        MultipartFile file = null;
        try {
            file = new MockMultipartFile("User photo" ,"user photo" , ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    public static byte[] convertBaseToByteArray(String base64) {
        return Base64.getEncoder().encode(base64.getBytes());
    }

    public static String getDefaultUserAvatar() {
//        Resource resource = new ClassPathResource("classpath:defaultAvatar.png");
//        try {
//            InputStream avatarStream = resource.getInputStream();
//            return FileCopyUtils.copyToByteArray(avatarStream);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        InputStream avatarStream = PracaApplication.class.getResourceAsStream("/defaultAvatar.png");
        try {
            return Base64.getEncoder().encodeToString(IOUtils.toByteArray(avatarStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
