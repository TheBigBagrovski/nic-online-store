package nic.project.onlinestore;

import nic.project.onlinestore.model.*;
import nic.project.onlinestore.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@SpringBootApplication
public class OnlineStoreApplication implements WebMvcConfigurer {

    @Value("${product_images_path}")
    private String productImagesPath;

    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ApplicationRunner dataLoader(CategoryRepository categoryRepo,
                                        ProductRepository productRepo,
                                        ImageRepository productImageRepo,
                                        FilterRepository filterRepo,
                                        FilterValueRepository filterValueRepo
    ) {
        return args -> {
            Category[] categories = new Category[]{
                    new Category(1L, "Смартфоны", null),
                    new Category(2L, "Аудиотехника", null),
                    new Category(3L, "Бытовая техника", null)
            };
            Category[] smartphones = new Category[]{
                    new Category(4L, "Сопутствующие товары", categories[0])
            };
            Category[] addgoods = new Category[]{
                    new Category(5L, "Наушники", smartphones[0]),
                    new Category(6L, "Чехлы", smartphones[0]),
                    new Category(7L, "Накладки", smartphones[0])
            };
            Category[] audio = new Category[]{
                    new Category(8L, "Портативные колонки", categories[1]),
                    new Category(9L, "Колонки", categories[1]),
                    new Category(10L, "Наушники", categories[1])
            };
            Category[] au_headphones = new Category[]{
                    new Category(11L, "TWS-наушники", audio[2]),
                    new Category(12L, "Bluetooth-гарнитура", audio[2]),
                    new Category(13L, "Проводная гарнитура", audio[2]),
            };
            Category[] ag_headphones = new Category[]{
                    new Category(14L, "TWS-наушники", addgoods[0]),
                    new Category(15L, "Bluetooth-гарнитура", addgoods[0]),
                    new Category(16L, "Проводная гарнитура", addgoods[0]),
            };
            Category[] domestic = new Category[]{
                    new Category(17L, "Техника для кухни", categories[2]),
                    new Category(18L, "Техника для дома", categories[2])
            };
            Category[] house = new Category[]{
                    new Category(19L, "Стиральные машины", domestic[1]),
                    new Category(20L, "Пылесосы", domestic[1])
            };
            Category[] kitchen = new Category[]{
                    new Category(21L, "Холодильники", domestic[0])
            };
            Image[] images = {
                    Image.builder().id(1L).path(productImagesPath + "iphone13.jpg").build(),
                    Image.builder().id(2L).path(productImagesPath + "iphone13-2.jpg").build(),
                    Image.builder().id(3L).path(productImagesPath + "iphone14.jpg").build(),
                    Image.builder().id(4L).path(productImagesPath + "iphone14pro.jpg").build(),
                    Image.builder().id(5L).path(productImagesPath + "iphone14promax.jpg").build(),
                    Image.builder().id(6L).path(productImagesPath + "iphone14promax-2.jpg").build(),
                    Image.builder().id(7L).path(productImagesPath + "iphone11.jpg").build(),
                    Image.builder().id(8L).path(productImagesPath + "iphone11-2.jpg").build(),
                    Image.builder().id(9L).path(productImagesPath + "iphone11pro.jpg").build(),
                    Image.builder().id(10L).path(productImagesPath + "iphone11pro-2.jpg").build(),
                    Image.builder().id(11L).path(productImagesPath + "iphone12.jpg").build(),
                    Image.builder().id(12L).path(productImagesPath + "iphone12-2.jpg").build(),
                    Image.builder().id(13L).path(productImagesPath + "iphone12pro.jpg").build(),
                    Image.builder().id(14L).path(productImagesPath + "iphone12pro-2.jpg").build(),
                    Image.builder().id(15L).path(productImagesPath + "samsunggalaxys8.jpg").build(),
                    Image.builder().id(16L).path(productImagesPath + "samsunggalaxys8-2.jpg").build(),
                    Image.builder().id(17L).path(productImagesPath + "samsunggalaxya54.jpg").build(),
                    Image.builder().id(18L).path(productImagesPath + "samsunggalaxya54-2.jpg").build(),
                    Image.builder().id(19L).path(productImagesPath + "samsunggalaxyszflip4.jpg").build(),
                    Image.builder().id(20L).path(productImagesPath + "samsunggalaxys23ultra.jpg").build(),
                    Image.builder().id(21L).path(productImagesPath + "samsunggalaxys22ultra.jpg").build(),
                    Image.builder().id(22L).path(productImagesPath + "opnordce2.jpg").build(),
                    Image.builder().id(23L).path(productImagesPath + "opnordce2-2.jpg").build(),
                    Image.builder().id(24L).path(productImagesPath + "huaweinova9.jpg").build(),
                    Image.builder().id(25L).path(productImagesPath + "huaweip50.jpg").build(),
                    Image.builder().id(26L).path(productImagesPath + "honor70.jpg").build(),
                    Image.builder().id(27L).path(productImagesPath + "airpodspro.jpg").build(),
                    Image.builder().id(28L).path(productImagesPath + "airpodspro-2.jpg").build(),
                    Image.builder().id(29L).path(productImagesPath + "airpodspro2.jpg").build(),
                    Image.builder().id(30L).path(productImagesPath + "airpodspro2-2.jpg").build(),
                    Image.builder().id(31L).path(productImagesPath + "redmibuds3.jpg").build(),
                    Image.builder().id(32L).path(productImagesPath + "Creative SXFI TRIO.jpg").build(),
                    Image.builder().id(33L).path(productImagesPath + "Sennheiser CX 300S.jpg").build(),
                    Image.builder().id(34L).path(productImagesPath + "Bluetooth-гарнитура Sony WI-C200.jpg").build(),
                    Image.builder().id(35L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM4.jpg").build(),
                    Image.builder().id(36L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM4-2.jpg").build(),
                    Image.builder().id(37L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM5.jpg").build(),
                    Image.builder().id(38L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM5-2.jpg").build(),
                    Image.builder().id(39L).path(productImagesPath + "Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL.jpg").build(),
                    Image.builder().id(40L).path(productImagesPath + "Bluetooth-гарнитура JBL Tune 115BT.jpg").build(),
                    Image.builder().id(41L).path(productImagesPath + "Чехол-книжка DF для OPPO A17.jpg").build(),
                    Image.builder().id(42L).path(productImagesPath + "Чехол-книжка DF для realme C35.jpg").build(),
                    Image.builder().id(43L).path(productImagesPath + "Умная колонка Яндекс Станция 2.jpg").build(),
                    Image.builder().id(44L).path(productImagesPath + "Умная колонка Яндекс Станция Макс.jpg").build(),
                    Image.builder().id(45L).path(productImagesPath + "Умная колонка Яндекс Станция Мини с часами.jpg").build(),
                    Image.builder().id(46L).path(productImagesPath + "Умная колонка VK Капсула.jpg").build(),
                    Image.builder().id(47L).path(productImagesPath + "Умная колонка SberBoom.jpg").build(),
                    Image.builder().id(48L).path(productImagesPath + "Колонки 2.1 SVEN MS-2050.jpg").build(),
                    Image.builder().id(49L).path(productImagesPath + "Колонки 2.0 F&D T-70X.jpg").build(),
                    Image.builder().id(50L).path(productImagesPath + "Стиральная машина Бирюса WM-ME610-04.jpg").build(),
                    Image.builder().id(51L).path(productImagesPath + "Стиральная машина Beko WRS5512BWW.jpg").build(),
                    Image.builder().id(52L).path(productImagesPath + "Пылесос DEXP H-1600.jpg").build(),
                    Image.builder().id(53L).path(productImagesPath + "Пылесос Supra VCS-1410.jpg").build(),
                    Image.builder().id(54L).path(productImagesPath + "Холодильник HIBERG RFS-480DX.jpg").build(),
                    Image.builder().id(55L).path(productImagesPath + "Холодильник HIBERG RFS-480DX-2.jpg").build(),
                    Image.builder().id(56L).path(productImagesPath + "Холодильник Tesler RCD-545I.jpg").build()
            };
            Product[] products = new Product[]{
                    Product.builder().id(1L).name("Смартфон Apple iPhone 13").images(Arrays.asList(images[0], images[1])).categories(Collections.singletonList(categories[0])).price(80999.0).quantity(5).build(),
                    Product.builder().id(2L).name("Смартфон Apple iPhone 13 PRO").categories(Collections.singletonList(categories[0])).price(80999.0).quantity(5).build(),
                    Product.builder().id(3L).name("Смартфон Apple iPhone 14").images(Collections.singletonList(images[2])).categories(Collections.singletonList(categories[0])).price(80999.0).quantity(10).build(),
                    Product.builder().id(4L).name("Смартфон Apple iPhone 14 PRO").images(Collections.singletonList(images[3])).categories(Collections.singletonList(categories[0])).price(89999.0).quantity(3).build(),
                    Product.builder().id(5L).name("Смартфон Apple iPhone 14 PRO MAX").images(Arrays.asList(images[4], images[5])).categories(Collections.singletonList(categories[0])).price(115999.0).quantity(2).build(),
                    Product.builder().id(6L).name("Смартфон Apple iPhone 11").images(Arrays.asList(images[6], images[7])).categories(Collections.singletonList(categories[0])).price(45999.0).quantity(15).build(),
                    Product.builder().id(7L).name("Смартфон Apple iPhone 11 PRO").images(Arrays.asList(images[8], images[9])).categories(Collections.singletonList(categories[0])).price(70999.0).quantity(10).build(),
                    Product.builder().id(8L).name("Смартфон Apple iPhone 12").images(Arrays.asList(images[10], images[11])).categories(Collections.singletonList(categories[0])).price(55999.0).quantity(10).build(),
                    Product.builder().id(9L).name("Смартфон Apple iPhone 12 PRO").images(Arrays.asList(images[12], images[13])).categories(Collections.singletonList(categories[0])).price(80999.0).quantity(10).build(),
                    Product.builder().id(10L).name("Смартфон Apple iPhone SE 2020").categories(Collections.singletonList(categories[0])).price(55999.0).quantity(20).build(),
                    Product.builder().id(11L).name("Смартфон Samsung Galaxy S8").images(Arrays.asList(images[14], images[15])).categories(Collections.singletonList(categories[0])).price(45999.0).quantity(8).build(),
                    Product.builder().id(12L).name("Смартфон Samsung Galaxy A54").images(Arrays.asList(images[16], images[17])).categories(Collections.singletonList(categories[0])).price(55999.0).quantity(8).build(),
                    Product.builder().id(13L).name("Смартфон Samsung Galaxy Z Flip4").images(Collections.singletonList(images[18])).categories(Collections.singletonList(categories[0])).price(89999.0).quantity(3).build(),
                    Product.builder().id(14L).name("Смартфон Samsung Galaxy S23 Ultra").images(Collections.singletonList(images[19])).categories(Collections.singletonList(categories[0])).price(45999.0).quantity(4).build(),
                    Product.builder().id(15L).name("Смартфон Samsung Galaxy S22 Ultra").images(Collections.singletonList(images[20])).categories(Collections.singletonList(categories[0])).price(55999.0).quantity(5).build(),
                    Product.builder().id(16L).name("Смартфон OnePlus Nord CE2").images(Arrays.asList(images[21], images[22])).categories(Collections.singletonList(categories[0])).price(55999.0).quantity(8).build(),
                    Product.builder().id(17L).name("Смартфон HUAWEI Nova 9").images(Collections.singletonList(images[23])).categories(Collections.singletonList(categories[0])).price(34999.0).quantity(8).build(),
                    Product.builder().id(18L).name("Смартфон HUAWEI P50").images(Collections.singletonList(images[24])).categories(Collections.singletonList(categories[0])).price(34999.0).quantity(15).build(),
                    Product.builder().id(19L).name("Смартфон Google Pixel 6").categories(Collections.singletonList(categories[0])).price(55999.0).quantity(5).build(),
                    Product.builder().id(20L).name("Смартфон Honor 70").images(Collections.singletonList(images[25])).categories(Collections.singletonList(categories[0])).price(55999.0).quantity(4).build(),
                    Product.builder().id(21L).name("Смартфон OPPO Reno8 T").categories(Collections.singletonList(categories[0])).price(55999.0).quantity(4).build(),
                    Product.builder().id(22L).name("TWS-наушники Apple AirPods Pro").images(Arrays.asList(images[26], images[27])).categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(11999.0).quantity(25).build(),
                    Product.builder().id(23L).name("TWS-наушники Apple AirPods Pro 2").images(Arrays.asList(images[28], images[29])).categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(11999.0).quantity(50).build(),
                    Product.builder().id(24L).name("TWS-наушники Xiaomi Redmi Buds 3 Lite").categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(1399.0).quantity(50).build(),
                    Product.builder().id(25L).name("TWS-наушники Xiaomi Redmi Buds 3").images(Collections.singletonList(images[30])).categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(3999.0).quantity(50).build(),
                    Product.builder().id(26L).name("TWS-наушники Xiaomi Mi True Wireless Earphones 2 Pro").categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(3999.0).quantity(50).build(),
                    Product.builder().id(27L).name("TWS-наушники Honor Choice Earbuds X3").categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(2999.0).quantity(50).build(),
                    Product.builder().id(28L).name("Проводная гарнитура Creative SXFI TRIO").images(Collections.singletonList(images[31])).categories(Arrays.asList(au_headphones[2], ag_headphones[2])).price(5999.0).quantity(20).build(),
                    Product.builder().id(29L).name("Проводная гарнитура Sennheiser CX 300S").images(Collections.singletonList(images[32])).categories(Arrays.asList(au_headphones[2], ag_headphones[2])).price(1999.0).quantity(20).build(),
                    Product.builder().id(30L).name("Проводная гарнитура HyperX Cloud Earbuds HX-HSCEB-RD").categories(Arrays.asList(au_headphones[2], ag_headphones[2])).price(15999.0).quantity(20).build(),
                    Product.builder().id(31L).name("Bluetooth-гарнитура Sony WI-SP500").categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(1999.0).quantity(20).build(),
                    Product.builder().id(32L).name("Bluetooth-гарнитура Sony WI-C200").images(Collections.singletonList(images[33])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(2999.0).quantity(10).build(),
                    Product.builder().id(33L).name("Bluetooth-гарнитура Sony WH-1000XM4").images(Arrays.asList(images[34], images[35])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(31999.0).quantity(5).build(),
                    Product.builder().id(34L).name("Bluetooth-гарнитура Sony WH-1000XM5").images(Arrays.asList(images[36], images[37])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(41999.0).quantity(2).build(),
                    Product.builder().id(35L).name("Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL").images(Collections.singletonList(images[38])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(11999.0).quantity(2).build(),
                    Product.builder().id(36L).name("Bluetooth-гарнитура Sennheiser IE 100 PRO Wireless").categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(12999.0).quantity(10).build(),
                    Product.builder().id(37L).name("Bluetooth-гарнитура JBL Tune 115BT").images(Collections.singletonList(images[39])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(5999.0).quantity(20).build(),
                    Product.builder().id(38L).name("Чехол для Huawei P50").categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(39L).name("Чехол-книжка Aceline Strap для Xiaomi Redmi 10C").categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(40L).name("Чехол-книжка Samsung Smart S View Wallet Cover для Samsung Galaxy A53").images(null).categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(41L).name("Чехол-книжка DF для OPPO A17").images(Collections.singletonList(images[40])).categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(42L).name("Чехол-книжка DF для realme C35").images(Collections.singletonList(images[41])).categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(43L).name("Накладка Apple Clear Case with MagSafe для Apple iPhone 14 Pro Max").images(null).categories(Collections.singletonList(addgoods[2])).price(5999.0).quantity(30).build(),
                    Product.builder().id(44L).name("Накладка Apple Leather Case with MagSafe для Apple iPhone 14 Pro").images(null).categories(Collections.singletonList(addgoods[2])).price(5999.0).quantity(30).build(),
                    Product.builder().id(45L).name("Накладка DF для Samsung Galaxy A73").images(null).categories(Collections.singletonList(addgoods[2])).price(499.0).quantity(30).build(),
                    Product.builder().id(46L).name("Накладка DF для Xiaomi Redmi 10C").images(null).categories(Collections.singletonList(addgoods[2])).price(499.0).quantity(30).build(),
                    Product.builder().id(47L).name("Умная колонка Яндекс Станция").images(null).categories(Collections.singletonList(audio[0])).price(2999.0).quantity(10).build(),
                    Product.builder().id(48L).name("Умная колонка Яндекс Станция 2").images(Collections.singletonList(images[42])).categories(Collections.singletonList(audio[0])).price(2999.0).quantity(5).build(),
                    Product.builder().id(49L).name("Умная колонка Яндекс Станция Макс").images(Collections.singletonList(images[43])).categories(Collections.singletonList(audio[0])).price(2999.0).quantity(5).build(),
                    Product.builder().id(50L).name("Умная колонка Яндекс Станция Мини с часами").images(Collections.singletonList(images[44])).categories(Collections.singletonList(audio[0])).price(3999.0).quantity(2).build(),
                    Product.builder().id(51L).name("Умная колонка Xiaomi Mi Smart Speaker").categories(Collections.singletonList(audio[0])).price(3999.0).quantity(8).build(),
                    Product.builder().id(52L).name("Умная колонка VK Капсула Мини").categories(Collections.singletonList(audio[0])).price(2999.0).quantity(4).build(),
                    Product.builder().id(53L).name("Умная колонка VK Капсула Нео").categories(Collections.singletonList(audio[0])).price(4999.0).quantity(8).build(),
                    Product.builder().id(54L).name("Умная колонка VK Капсула").images(Collections.singletonList(images[45])).categories(Collections.singletonList(audio[0])).price(3999.0).quantity(2).build(),
                    Product.builder().id(55L).name("Умная колонка SberBoom").images(Collections.singletonList(images[46])).categories(Collections.singletonList(audio[0])).price(4399.0).quantity(4).build(),
                    Product.builder().id(56L).name("Умная колонка LG XBOOM AI ThinQ WK7Y").categories(Collections.singletonList(audio[0])).price(3999.0).quantity(10).build(),
                    Product.builder().id(57L).name("Колонки 2.0 DEXP R610").categories(Collections.singletonList(audio[1])).price(8999.0).quantity(10).build(),
                    Product.builder().id(58L).name("Колонки 2.0 Edifier S3000Pro").categories(Collections.singletonList(audio[1])).price(5999.0).quantity(10).build(),
                    Product.builder().id(59L).name("Колонки 2.0 Edifier R1855DB").categories(Collections.singletonList(audio[1])).price(15999.0).quantity(10).build(),
                    Product.builder().id(60L).name("Колонки 2.1 SVEN MS-2050").images(Collections.singletonList(images[47])).categories(Collections.singletonList(audio[1])).price(3999.0).quantity(5).build(),
                    Product.builder().id(61L).name("Колонки 2.1 Edifier M601DB").categories(Collections.singletonList(audio[1])).price(4999.0).quantity(5).build(),
                    Product.builder().id(62L).name("Колонки 2.0 F&D T-70X").images(Collections.singletonList(images[48])).categories(Collections.singletonList(audio[1])).price(2999.0).quantity(10).build(),
                    Product.builder().id(63L).name("Колонки 2.0 SVEN MC-30").categories(Collections.singletonList(audio[1])).price(4999.0).quantity(5).build(),
                    Product.builder().id(64L).name("Колонки 2.0 F&D T-60X").categories(Collections.singletonList(audio[1])).price(7999.0).quantity(10).build(),
                    Product.builder().id(65L).name("Стиральная машина DEXP WM-F610NTMA/WW").categories(Collections.singletonList(house[0])).price(14999.0).quantity(5).build(),
                    Product.builder().id(66L).name("Стиральная машина Бирюса WM-ME610/04").images(Collections.singletonList(images[49])).categories(Collections.singletonList(house[0])).price(14999.0).quantity(10).build(),
                    Product.builder().id(67L).name("Стиральная машина Indesit IWSD 51051 CIS").categories(Collections.singletonList(house[0])).price(9999.0).quantity(20).build(),
                    Product.builder().id(68L).name("Стиральная машина Beko WRS5512BWW").images(Collections.singletonList(images[50])).categories(Collections.singletonList(house[0])).price(10999.0).quantity(18).build(),
                    Product.builder().id(69L).name("Стиральная машина TCL TWF60-G103061A03").categories(Collections.singletonList(house[0])).price(15999.0).quantity(17).build(),
                    Product.builder().id(70L).name("Пылесос DEXP H-1600").images(Collections.singletonList(images[51])).categories(Collections.singletonList(house[1])).price(2999.0).quantity(10).build(),
                    Product.builder().id(71L).name("Пылесос ECON ECO-1414VB").categories(Collections.singletonList(house[1])).price(2999.0).quantity(2).build(),
                    Product.builder().id(72L).name("Пылесос Starwind SCB1112").categories(Collections.singletonList(house[1])).price(3999.0).quantity(5).build(),
                    Product.builder().id(73L).name("Пылесос Supra VCS-1410").images(Collections.singletonList(images[52])).categories(Collections.singletonList(house[1])).price(3999.0).quantity(5).build(),
                    Product.builder().id(74L).name("Пылесос BQ VC1802B").categories(Collections.singletonList(house[1])).price(4999.0).quantity(5).build(),
                    Product.builder().id(75L).name("Холодильник Liebherr CBNd 5223").categories(Collections.singletonList(kitchen[0])).price(75999.0).quantity(2).build(),
                    Product.builder().id(76L).name("Холодильник HIBERG RFS-480DX").images(Arrays.asList(images[53], images[54])).categories(Collections.singletonList(kitchen[0])).price(75999.0).quantity(2).build(),
                    Product.builder().id(77L).name("Холодильник Tesler RCD-545I").images(Collections.singletonList(images[55])).categories(Collections.singletonList(kitchen[0])).price(76999.0).quantity(3).build(),
                    Product.builder().id(78L).name("Холодильник ZUGEL ZRSS630W").categories(Collections.singletonList(kitchen[0])).price(79999.0).quantity(4).build(),
                    Product.builder().id(79L).name("Холодильник Liebherr CNsfd 5223").categories(Collections.singletonList(kitchen[0])).price(70999.0).quantity(10).build()
            };
            Filter smartphonesBrand = Filter.builder()
                    .id(1L)
                    .name("brand")
                    .category(categories[0])
                    .build();
            List<FilterValue> filterValues = new ArrayList<>();
            filterValues.add(FilterValue.builder().id(1L).value("Apple").filter(smartphonesBrand).build());
            filterValues.add(FilterValue.builder().id(2L).value("Samsung").filter(smartphonesBrand).build());
            filterValues.add(FilterValue.builder().id(3L).value("OnePlus").filter(smartphonesBrand).build());
            filterValues.add(FilterValue.builder().id(4L).value("Huawei").filter(smartphonesBrand).build());
            filterValues.add(FilterValue.builder().id(5L).value("Google").filter(smartphonesBrand).build());
            filterValues.add(FilterValue.builder().id(6L).value("Honor").filter(smartphonesBrand).build());
            filterValues.add(FilterValue.builder().id(7L).value("OPPO").filter(smartphonesBrand).build());
            products[0].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[1].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[2].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[3].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[4].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[5].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[6].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[7].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[8].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[9].addFilterProperty(smartphonesBrand,  filterValues.get(0));
            products[10].addFilterProperty(smartphonesBrand,  filterValues.get(1));
            products[11].addFilterProperty(smartphonesBrand, filterValues.get(1));
            products[12].addFilterProperty(smartphonesBrand, filterValues.get(1));
            products[13].addFilterProperty(smartphonesBrand, filterValues.get(1));
            products[14].addFilterProperty(smartphonesBrand, filterValues.get(1));
            products[15].addFilterProperty(smartphonesBrand, filterValues.get(2));
            products[16].addFilterProperty(smartphonesBrand, filterValues.get(3));
            products[17].addFilterProperty(smartphonesBrand, filterValues.get(3));
            products[18].addFilterProperty(smartphonesBrand, filterValues.get(4));
            products[19].addFilterProperty(smartphonesBrand, filterValues.get(5));
            products[20].addFilterProperty(smartphonesBrand, filterValues.get(6));
            productImageRepo.saveAll(Arrays.asList(images));
            categoryRepo.saveAll(Arrays.asList(categories));
            categoryRepo.saveAll(Arrays.asList(smartphones));
            categoryRepo.saveAll(Arrays.asList(addgoods));
            categoryRepo.saveAll(Arrays.asList(audio));
            categoryRepo.saveAll(Arrays.asList(au_headphones));
            categoryRepo.saveAll(Arrays.asList(ag_headphones));
            categoryRepo.saveAll(Arrays.asList(domestic));
            categoryRepo.saveAll(Arrays.asList(house));
            categoryRepo.saveAll(Arrays.asList(kitchen));
            filterRepo.save(smartphonesBrand);
            filterValueRepo.saveAll(filterValues);
            productRepo.saveAll(Arrays.asList(products));
        };
    }

}
