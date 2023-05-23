package webtoon.main;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webtoon.main.account.entities.UserEntity;
import webtoon.main.account.enums.EAccountType;
import webtoon.main.account.enums.EStatus;
import webtoon.main.account.repositories.IRoleRepository;
import webtoon.main.account.repositories.IUserRepository;
import webtoon.main.payment.entities.SubscriptionPackEntity;
import webtoon.main.payment.repositories.IOrderRepository;
import webtoon.main.payment.repositories.ISubscriptionPackRepository;
import webtoon.main.utils.ASCIIConverter;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@RestController
//@RequestMapping("fakeData")
//@Transactional
public class ComponentLoaderConfiguration {

    public  List<String> fullNameList = new ArrayList<>();
    private final List<SubscriptionPackEntity> subscriptionPackEntities;
    private final IUserRepository userRepository;
    private final ISubscriptionPackRepository subscriptionPackRepository;
    private final IOrderRepository orderRepository;

    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ComponentLoaderConfiguration(IUserRepository userRepository, ISubscriptionPackRepository subscriptionPackRepository, IOrderRepository orderRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder) throws IOException {
        this.userRepository = userRepository;
        this.subscriptionPackRepository = subscriptionPackRepository;
        this.orderRepository = orderRepository;

        subscriptionPackEntities = subscriptionPackRepository.findAll();
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

        readFileName();
    }

    @RequestMapping
    public String fake()  {
        for (int i = 1; i < 6; i++) {
            java.util.Date createdAt =  Date.valueOf("2023-05-23");
            UserEntity userEntity = generateUser();
            userEntity.setUsername("tests"+i);

//            int subIndex = generateRange(0, 2);
//            while (subIndex < 0 || subIndex > 2){
//                subIndex = generateRange(0, 2);
//            }
//            SubscriptionPackEntity subs = subscriptionPackEntities.get(subIndex);
//
//            String madonhang = VnPayConfig.getRandomNumber(10); // need check
//
//            while (this.orderRepository.getByMaDonHang(madonhang) != null)
//                madonhang = VnPayConfig.getRandomNumber(10);
//
//            OrderEntity order = OrderEntity.builder()
//                    .orderType(EOrderType.NEW)
//                    .user_id(userEntity)
//                    .paymentMethod(EPaymentMethod.ATM)
//                    .finalPrice(subs.getPrice())
//                    .maDonHang(madonhang)
//                    .modifiedBy(userEntity)
//                    .subs_pack_id(subs)
//                    .status(EOrderStatus.USER_CONFIRMED_BANKING)
//                    .createAtFake(createdAt)
//                    .build();

            userEntity.setCreateAtFake(createdAt);
//            userEntity.setCurrentUsedSubsId(subs.getId());
//            userEntity.setFirstBoughtSubsDate(createdAt);

//            Calendar canReadUntil = Calendar.getInstance();
//            canReadUntil.setTime(createdAt);
//            canReadUntil.set(Calendar.MONTH, canReadUntil.get(Calendar.MONTH) + subs.getMonthCount());
//            userEntity.setCanReadUntilDate(canReadUntil.getTime());

            this.userRepository.saveAndFlush(userEntity);
//            this.orderRepository.saveAndFlush(order);
        }

        return "ok";
    }

    public static void main(String[] args) {

        Double d = Double.valueOf("50000");
        System.out.println(d*0.10);
    }


    UserEntity generateUser() {
        String fName = fullNameList.get(generateIndex().intValue());
        String uName = ASCIIConverter.removeAccent(fName.toLowerCase().replace(" ", "")) + generateNumber();
        String email = ASCIIConverter.removeAccent(fName.toLowerCase().replace(" ", "")) + generateNumber() + "@gmail.com";
        String phone = generatePhone();

        while (this.userRepository.findByUsername(uName).isPresent())
            uName = ASCIIConverter.removeAccent(fName.toLowerCase().replace(" ", "")) + generateNumber();

        while (this.userRepository.findByEmail(email).isPresent())
            email = ASCIIConverter.removeAccent(fName.toLowerCase().replace(" ", "")) + generateNumber() + "@gmail.com";

        while (this.userRepository.findByPhone(phone).isPresent())
            phone = generatePhone();

        UserEntity userEntity = UserEntity.builder()
                .fullName(fName)
                .username(uName)
                .email(email)
                .phone(phone)
                .password(passwordEncoder.encode("123456"))
                .accountType(EAccountType.FAKE)
                .role(roleRepository.findById(3l).get())
                .hasBlocked(false)
                .status(EStatus.ACTIVED)
                .build();

        return userEntity;
    }

    static int generateRange(int min, int max) {
        int i = min + (int) (Math.random() * ((max - min) + min));
        return i;
    }

    long generateNumber() {
        return Math.round(Math.random() * 10000);
    }

    String generatePhone() {

        Long number = Math.round((1000000 + (Math.random() * ((9999999 - 1000000) + 1000000))));

        String phone = "032" + number;
        return phone;
    }

    Long generateIndex() {
        return (long) (1 + (int) (Math.random() * ((415 - 1) + 1)));
    }

    void readFileName() throws IOException {
        File file = new File("names.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String st;

        // Condition holds true till
        // there is character in a string
        while ((st = reader.readLine()) != null)
            fullNameList.add(st.trim());

        reader.close();
    }


}
