package webtoon.main.utils;

public class PhoneUtil {

    public static boolean validateVNPhoneNumber(String phoneNumber) {
        String regex = "^(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})$";
        return phoneNumber.matches(regex);
    }

}
