package nic.project.onlinestore;

import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.ProductImage;
import nic.project.onlinestore.repositories.CategoryRepository;
import nic.project.onlinestore.repositories.ProductImageRepository;
import nic.project.onlinestore.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

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
                                        ProductImageRepository productImageRepo) {
        return args -> {
            Category[] categories = new Category[]{
                    new Category(1L, "Смартфоны", null),
                    new Category(2L, "Аудиотехника", null),
                    new Category(3L, "Бытовая техника", null)
            };
            Category[] smartphones = new Category[]{
                    new Category(4L, "Apple", categories[0]),
                    new Category(5L, "Samsung", categories[0]),
                    new Category(6L, "Google", categories[0]),
                    new Category(7L, "OnePlus", categories[0]),
                    new Category(8L, "Huawei", categories[0]),
                    new Category(9L, "Сопутствующие товары", categories[0])
            };
            Category[] addgoods = new Category[]{
                    new Category(10L, "Наушники", smartphones[5]),
                    new Category(11L, "Чехлы", smartphones[5]),
                    new Category(12L, "Накладки", smartphones[5])
            };
            Category[] audio = new Category[]{
                    new Category(13L, "Портативные колонки", categories[1]),
                    new Category(14L, "Колонки", categories[1]),
                    new Category(15L, "Наушники", categories[1])
            };
            Category[] au_headphones = new Category[]{
                    new Category(16L, "TWS-наушники", audio[2]),
                    new Category(16L, "Bluetooth-гарнитура", audio[2]),
                    new Category(17L, "Проводная гарнитура", audio[2]),
            };
            Category[] ag_headphones = new Category[]{
                    new Category(18L, "TWS-наушники", addgoods[0]),
                    new Category(19L, "Bluetooth-гарнитура", addgoods[0]),
                    new Category(20L, "Проводная гарнитура", addgoods[0]),
            };
            Category[] domestic = new Category[]{
                    new Category(21L, "Техника для кухни", categories[2]),
                    new Category(22L, "Техника для дома", categories[2])
            };
            Category[] house = new Category[]{
                    new Category(23L, "Стиральные машины", domestic[1]),
                    new Category(24L, "Пылесосы", domestic[1])
            };
            Category[] kitchen = new Category[]{
                    new Category(25L, "Холодильники", domestic[0])
            };
            ProductImage[] images = {
                    ProductImage.builder().id(1L).path(productImagesPath + "iphone13.jpg").build(),
                    ProductImage.builder().id(2L).path(productImagesPath + "iphone13-2.jpg").build(),
                    ProductImage.builder().id(3L).path(productImagesPath + "iphone14.jpg").build(),
                    ProductImage.builder().id(4L).path(productImagesPath + "iphone14pro.jpg").build(),
                    ProductImage.builder().id(5L).path(productImagesPath + "iphone14promax.jpg").build(),
                    ProductImage.builder().id(6L).path(productImagesPath + "iphone14promax-2.jpg").build(),
                    ProductImage.builder().id(7L).path(productImagesPath + "iphone11.jpg").build(),
                    ProductImage.builder().id(8L).path(productImagesPath + "iphone11-2.jpg").build(),
                    ProductImage.builder().id(9L).path(productImagesPath + "iphone11pro.jpg").build(),
                    ProductImage.builder().id(10L).path(productImagesPath + "iphone11pro-2.jpg").build(),
                    ProductImage.builder().id(11L).path(productImagesPath + "iphone12.jpg").build(),
                    ProductImage.builder().id(12L).path(productImagesPath + "iphone12-2.jpg").build(),
                    ProductImage.builder().id(13L).path(productImagesPath + "iphone12pro.jpg").build(),
                    ProductImage.builder().id(14L).path(productImagesPath + "iphone12pro-2.jpg").build(),
                    ProductImage.builder().id(15L).path(productImagesPath + "samsunggalaxys8.jpg").build(),
                    ProductImage.builder().id(16L).path(productImagesPath + "samsunggalaxys8-2.jpg").build(),
                    ProductImage.builder().id(17L).path(productImagesPath + "samsunggalaxya54.jpg").build(),
                    ProductImage.builder().id(18L).path(productImagesPath + "samsunggalaxya54-2.jpg").build(),
                    ProductImage.builder().id(19L).path(productImagesPath + "samsunggalaxyszflip4.jpg").build(),
                    ProductImage.builder().id(20L).path(productImagesPath + "samsunggalaxys23ultra.jpg").build(),
                    ProductImage.builder().id(21L).path(productImagesPath + "samsunggalaxys22ultra.jpg").build(),
                    ProductImage.builder().id(22L).path(productImagesPath + "opnordce2.jpg").build(),
                    ProductImage.builder().id(23L).path(productImagesPath + "opnordce2-2.jpg").build(),
                    ProductImage.builder().id(24L).path(productImagesPath + "huaweinova9.jpg").build(),
                    ProductImage.builder().id(25L).path(productImagesPath + "huaweip50.jpg").build(),
                    ProductImage.builder().id(26L).path(productImagesPath + "honor70.jpg").build(),
                    ProductImage.builder().id(27L).path(productImagesPath + "airpodspro.jpg").build(),
                    ProductImage.builder().id(28L).path(productImagesPath + "airpodspro-2.jpg").build(),
                    ProductImage.builder().id(29L).path(productImagesPath + "airpodspro2.jpg").build(),
                    ProductImage.builder().id(30L).path(productImagesPath + "airpodspro2-2.jpg").build(),
                    ProductImage.builder().id(31L).path(productImagesPath + "redmibuds3.jpg").build(),
                    ProductImage.builder().id(32L).path(productImagesPath + "Creative SXFI TRIO.jpg").build(),
                    ProductImage.builder().id(33L).path(productImagesPath + "Sennheiser CX 300S.jpg").build(),
                    ProductImage.builder().id(34L).path(productImagesPath + "Bluetooth-гарнитура Sony WI-C200.jpg").build(),
                    ProductImage.builder().id(35L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM4.jpg").build(),
                    ProductImage.builder().id(36L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM4-2.jpg").build(),
                    ProductImage.builder().id(37L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM5.jpg").build(),
                    ProductImage.builder().id(38L).path(productImagesPath + "Bluetooth-гарнитура Sony WH-1000XM5-2.jpg").build(),
                    ProductImage.builder().id(39L).path(productImagesPath + "Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL.jpg").build(),
                    ProductImage.builder().id(40L).path(productImagesPath + "Bluetooth-гарнитура JBL Tune 115BT.jpg").build(),
                    ProductImage.builder().id(41L).path(productImagesPath + "Чехол-книжка DF для OPPO A17.jpg").build(),
                    ProductImage.builder().id(42L).path(productImagesPath + "Чехол-книжка DF для realme C35.jpg").build(),
                    ProductImage.builder().id(43L).path(productImagesPath + "Умная колонка Яндекс Станция 2.jpg").build(),
                    ProductImage.builder().id(44L).path(productImagesPath + "Умная колонка Яндекс Станция Макс.jpg").build(),
                    ProductImage.builder().id(45L).path(productImagesPath + "Умная колонка Яндекс Станция Мини с часами.jpg").build(),
                    ProductImage.builder().id(46L).path(productImagesPath + "Умная колонка VK Капсула.jpg").build(),
                    ProductImage.builder().id(47L).path(productImagesPath + "Умная колонка SberBoom.jpg").build(),
                    ProductImage.builder().id(48L).path(productImagesPath + "Колонки 2.1 SVEN MS-2050.jpg").build(),
                    ProductImage.builder().id(49L).path(productImagesPath + "Колонки 2.0 F&D T-70X.jpg").build(),
                    ProductImage.builder().id(50L).path(productImagesPath + "Стиральная машина Бирюса WM-ME610-04.jpg").build(),
                    ProductImage.builder().id(51L).path(productImagesPath + "Стиральная машина Beko WRS5512BWW.jpg").build(),
                    ProductImage.builder().id(52L).path(productImagesPath + "Пылесос DEXP H-1600.jpg").build(),
                    ProductImage.builder().id(53L).path(productImagesPath + "Пылесос Supra VCS-1410.jpg").build(),
                    ProductImage.builder().id(54L).path(productImagesPath + "Холодильник HIBERG RFS-480DX.jpg").build(),
                    ProductImage.builder().id(55L).path(productImagesPath + "Холодильник HIBERG RFS-480DX-2.jpg").build(),
                    ProductImage.builder().id(56L).path(productImagesPath + "Холодильник Tesler RCD-545I.jpg").build()
            };
            Product[] products = new Product[]{
                    Product.builder().id(1L).name("Смартфон Apple iPhone 13").productImages(Arrays.asList(images[0], images[1])).categories(Collections.singletonList(smartphones[0])).price(80999.0).quantity(5).build(),
                    Product.builder().id(2L).name("Смартфон Apple iPhone 13 PRO").categories(Collections.singletonList(smartphones[0])).price(80999.0).quantity(5).build(),
                    Product.builder().id(3L).name("Смартфон Apple iPhone 14").productImages(Collections.singletonList(images[2])).categories(Collections.singletonList(smartphones[0])).price(80999.0).quantity(10).build(),
                    Product.builder().id(4L).name("Смартфон Apple iPhone 14 PRO").productImages(Collections.singletonList(images[3])).categories(Collections.singletonList(smartphones[0])).price(89999.0).quantity(3).build(),
                    Product.builder().id(5L).name("Смартфон Apple iPhone 14 PRO MAX").productImages(Arrays.asList(images[4], images[5])).categories(Collections.singletonList(smartphones[0])).price(115999.0).quantity(2).build(),
                    Product.builder().id(6L).name("Смартфон Apple iPhone 11").productImages(Arrays.asList(images[6], images[7])).categories(Collections.singletonList(smartphones[0])).price(45999.0).quantity(15).build(),
                    Product.builder().id(7L).name("Смартфон Apple iPhone 11 PRO").productImages(Arrays.asList(images[8], images[9])).categories(Collections.singletonList(smartphones[0])).price(70999.0).quantity(10).build(),
                    Product.builder().id(8L).name("Смартфон Apple iPhone 12").productImages(Arrays.asList(images[10], images[11])).categories(Collections.singletonList(smartphones[0])).price(55999.0).quantity(10).build(),
                    Product.builder().id(9L).name("Смартфон Apple iPhone 12 PRO").productImages(Arrays.asList(images[12], images[13])).categories(Collections.singletonList(smartphones[0])).price(80999.0).quantity(10).build(),
                    Product.builder().id(10L).name("Смартфон Apple iPhone SE 2020").categories(Collections.singletonList(smartphones[0])).price(55999.0).quantity(20).build(),
                    Product.builder().id(11L).name("Смартфон Samsung Galaxy S8").productImages(Arrays.asList(images[14], images[15])).categories(Collections.singletonList(smartphones[1])).price(45999.0).quantity(8).build(),
                    Product.builder().id(12L).name("Смартфон Samsung Galaxy A54").productImages(Arrays.asList(images[16], images[17])).categories(Collections.singletonList(smartphones[1])).price(55999.0).quantity(8).build(),
                    Product.builder().id(13L).name("Смартфон Samsung Galaxy Z Flip4").productImages(Collections.singletonList(images[18])).categories(Collections.singletonList(smartphones[1])).price(89999.0).quantity(3).build(),
                    Product.builder().id(14L).name("Смартфон Samsung Galaxy S23 Ultra").productImages(Collections.singletonList(images[19])).categories(Collections.singletonList(smartphones[1])).price(45999.0).quantity(4).build(),
                    Product.builder().id(15L).name("Смартфон Samsung Galaxy S22 Ultra").productImages(Collections.singletonList(images[20])).categories(Collections.singletonList(smartphones[1])).price(55999.0).quantity(5).build(),
                    Product.builder().id(16L).name("Смартфон OnePlus Nord CE2").productImages(Arrays.asList(images[21], images[22])).categories(Collections.singletonList(smartphones[3])).price(55999.0).quantity(8).build(),
                    Product.builder().id(17L).name("Смартфон HUAWEI Nova 9").productImages(Collections.singletonList(images[23])).categories(Collections.singletonList(smartphones[4])).price(34999.0).quantity(8).build(),
                    Product.builder().id(18L).name("Смартфон HUAWEI P50").productImages(Collections.singletonList(images[24])).categories(Collections.singletonList(smartphones[4])).price(34999.0).quantity(15).build(),
                    Product.builder().id(19L).name("Смартфон Google Pixel 6").categories(Collections.singletonList(smartphones[2])).price(55999.0).quantity(5).build(),
                    Product.builder().id(20L).name("Смартфон Honor 70").productImages(Collections.singletonList(images[25])).categories(Collections.singletonList(categories[0])).price(55999.0).quantity(4).build(),
                    Product.builder().id(21L).name("Смартфон OPPO Reno8 T").categories(Collections.singletonList(categories[0])).price(55999.0).quantity(4).build(),
                    Product.builder().id(22L).name("TWS-наушники Apple AirPods Pro").productImages(Arrays.asList(images[26], images[27])).categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(11999.0).quantity(25).build(),
                    Product.builder().id(23L).name("TWS-наушники Apple AirPods Pro 2").productImages(Arrays.asList(images[28], images[29])).categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(11999.0).quantity(50).build(),
                    Product.builder().id(24L).name("TWS-наушники Xiaomi Redmi Buds 3 Lite").categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(1399.0).quantity(50).build(),
                    Product.builder().id(25L).name("TWS-наушники Xiaomi Redmi Buds 3").productImages(Collections.singletonList(images[30])).categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(3999.0).quantity(50).build(),
                    Product.builder().id(26L).name("TWS-наушники Xiaomi Mi True Wireless Earphones 2 Pro").categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(3999.0).quantity(50).build(),
                    Product.builder().id(27L).name("TWS-наушники Honor Choice Earbuds X3").categories(Arrays.asList(au_headphones[0], ag_headphones[0])).price(2999.0).quantity(50).build(),
                    Product.builder().id(28L).name("Проводная гарнитура Creative SXFI TRIO").productImages(Collections.singletonList(images[31])).categories(Arrays.asList(au_headphones[2], ag_headphones[2])).price(5999.0).quantity(20).build(),
                    Product.builder().id(29L).name("Проводная гарнитура Sennheiser CX 300S").productImages(Collections.singletonList(images[32])).categories(Arrays.asList(au_headphones[2], ag_headphones[2])).price(1999.0).quantity(20).build(),
                    Product.builder().id(30L).name("Проводная гарнитура HyperX Cloud Earbuds HX-HSCEB-RD").categories(Arrays.asList(au_headphones[2], ag_headphones[2])).price(15999.0).quantity(20).build(),
                    Product.builder().id(31L).name("Bluetooth-гарнитура Sony WI-SP500").categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(1999.0).quantity(20).build(),
                    Product.builder().id(32L).name("Bluetooth-гарнитура Sony WI-C200").productImages(Collections.singletonList(images[33])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(2999.0).quantity(10).build(),
                    Product.builder().id(33L).name("Bluetooth-гарнитура Sony WH-1000XM4").productImages(Arrays.asList(images[34], images[35])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(31999.0).quantity(5).build(),
                    Product.builder().id(34L).name("Bluetooth-гарнитура Sony WH-1000XM5").productImages(Arrays.asList(images[36], images[37])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(41999.0).quantity(2).build(),
                    Product.builder().id(35L).name("Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL").productImages(Collections.singletonList(images[38])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(11999.0).quantity(2).build(),
                    Product.builder().id(36L).name("Bluetooth-гарнитура Sennheiser IE 100 PRO Wireless").categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(12999.0).quantity(10).build(),
                    Product.builder().id(37L).name("Bluetooth-гарнитура JBL Tune 115BT").productImages(Collections.singletonList(images[39])).categories(Arrays.asList(au_headphones[1], ag_headphones[1])).price(5999.0).quantity(20).build(),
                    Product.builder().id(38L).name("Чехол для Huawei P50").categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(39L).name("Чехол-книжка Aceline Strap для Xiaomi Redmi 10C").categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(40L).name("Чехол-книжка Samsung Smart S View Wallet Cover для Samsung Galaxy A53").productImages(null).categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(41L).name("Чехол-книжка DF для OPPO A17").productImages(Collections.singletonList(images[40])).categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(42L).name("Чехол-книжка DF для realme C35").productImages(Collections.singletonList(images[41])).categories(Collections.singletonList(addgoods[1])).price(999.0).quantity(30).build(),
                    Product.builder().id(43L).name("Накладка Apple Clear Case with MagSafe для Apple iPhone 14 Pro Max").productImages(null).categories(Collections.singletonList(addgoods[2])).price(5999.0).quantity(30).build(),
                    Product.builder().id(44L).name("Накладка Apple Leather Case with MagSafe для Apple iPhone 14 Pro").productImages(null).categories(Collections.singletonList(addgoods[2])).price(5999.0).quantity(30).build(),
                    Product.builder().id(45L).name("Накладка DF для Samsung Galaxy A73").productImages(null).categories(Collections.singletonList(addgoods[2])).price(499.0).quantity(30).build(),
                    Product.builder().id(46L).name("Накладка DF для Xiaomi Redmi 10C").productImages(null).categories(Collections.singletonList(addgoods[2])).price(499.0).quantity(30).build(),
                    Product.builder().id(47L).name("Умная колонка Яндекс Станция").productImages(null).categories(Collections.singletonList(audio[0])).price(2999.0).quantity(10).build(),
                    Product.builder().id(48L).name("Умная колонка Яндекс Станция 2").productImages(Collections.singletonList(images[42])).categories(Collections.singletonList(audio[0])).price(2999.0).quantity(5).build(),
                    Product.builder().id(49L).name("Умная колонка Яндекс Станция Макс").productImages(Collections.singletonList(images[43])).categories(Collections.singletonList(audio[0])).price(2999.0).quantity(5).build(),
                    Product.builder().id(50L).name("Умная колонка Яндекс Станция Мини с часами").productImages(Collections.singletonList(images[44])).categories(Collections.singletonList(audio[0])).price(3999.0).quantity(2).build(),
                    Product.builder().id(51L).name("Умная колонка Xiaomi Mi Smart Speaker").categories(Collections.singletonList(audio[0])).price(3999.0).quantity(8).build(),
                    Product.builder().id(52L).name("Умная колонка VK Капсула Мини").categories(Collections.singletonList(audio[0])).price(2999.0).quantity(4).build(),
                    Product.builder().id(53L).name("Умная колонка VK Капсула Нео").categories(Collections.singletonList(audio[0])).price(4999.0).quantity(8).build(),
                    Product.builder().id(54L).name("Умная колонка VK Капсула").productImages(Collections.singletonList(images[45])).categories(Collections.singletonList(audio[0])).price(3999.0).quantity(2).build(),
                    Product.builder().id(55L).name("Умная колонка SberBoom").productImages(Collections.singletonList(images[46])).categories(Collections.singletonList(audio[0])).price(4399.0).quantity(4).build(),
                    Product.builder().id(56L).name("Умная колонка LG XBOOM AI ThinQ WK7Y").categories(Collections.singletonList(audio[0])).price(3999.0).quantity(10).build(),
                    Product.builder().id(57L).name("Колонки 2.0 DEXP R610").categories(Collections.singletonList(audio[1])).price(8999.0).quantity(10).build(),
                    Product.builder().id(58L).name("Колонки 2.0 Edifier S3000Pro").categories(Collections.singletonList(audio[1])).price(5999.0).quantity(10).build(),
                    Product.builder().id(59L).name("Колонки 2.0 Edifier R1855DB").categories(Collections.singletonList(audio[1])).price(15999.0).quantity(10).build(),
                    Product.builder().id(60L).name("Колонки 2.1 SVEN MS-2050").productImages(Collections.singletonList(images[47])).categories(Collections.singletonList(audio[1])).price(3999.0).quantity(5).build(),
                    Product.builder().id(61L).name("Колонки 2.1 Edifier M601DB").categories(Collections.singletonList(audio[1])).price(4999.0).quantity(5).build(),
                    Product.builder().id(62L).name("Колонки 2.0 F&D T-70X").productImages(Collections.singletonList(images[48])).categories(Collections.singletonList(audio[1])).price(2999.0).quantity(10).build(),
                    Product.builder().id(63L).name("Колонки 2.0 SVEN MC-30").categories(Collections.singletonList(audio[1])).price(4999.0).quantity(5).build(),
                    Product.builder().id(64L).name("Колонки 2.0 F&D T-60X").categories(Collections.singletonList(audio[1])).price(7999.0).quantity(10).build(),
                    Product.builder().id(65L).name("Стиральная машина DEXP WM-F610NTMA/WW").categories(Collections.singletonList(house[0])).price(14999.0).quantity(5).build(),
                    Product.builder().id(66L).name("Стиральная машина Бирюса WM-ME610/04").productImages(Collections.singletonList(images[49])).categories(Collections.singletonList(house[0])).price(14999.0).quantity(10).build(),
                    Product.builder().id(67L).name("Стиральная машина Indesit IWSD 51051 CIS").categories(Collections.singletonList(house[0])).price(9999.0).quantity(20).build(),
                    Product.builder().id(68L).name("Стиральная машина Beko WRS5512BWW").productImages(Collections.singletonList(images[50])).categories(Collections.singletonList(house[0])).price(10999.0).quantity(18).build(),
                    Product.builder().id(69L).name("Стиральная машина TCL TWF60-G103061A03").categories(Collections.singletonList(house[0])).price(15999.0).quantity(17).build(),
                    Product.builder().id(70L).name("Пылесос DEXP H-1600").productImages(Collections.singletonList(images[51])).categories(Collections.singletonList(house[1])).price(2999.0).quantity(10).build(),
                    Product.builder().id(71L).name("Пылесос ECON ECO-1414VB").categories(Collections.singletonList(house[1])).price(2999.0).quantity(2).build(),
                    Product.builder().id(72L).name("Пылесос Starwind SCB1112").categories(Collections.singletonList(house[1])).price(3999.0).quantity(5).build(),
                    Product.builder().id(73L).name("Пылесос Supra VCS-1410").productImages(Collections.singletonList(images[52])).categories(Collections.singletonList(house[1])).price(3999.0).quantity(5).build(),
                    Product.builder().id(74L).name("Пылесос BQ VC1802B").categories(Collections.singletonList(house[1])).price(4999.0).quantity(5).build(),
                    Product.builder().id(75L).name("Холодильник Liebherr CBNd 5223").categories(Collections.singletonList(kitchen[0])).price(75999.0).quantity(2).build(),
                    Product.builder().id(76L).name("Холодильник HIBERG RFS-480DX").productImages(Arrays.asList(images[53], images[54])).categories(Collections.singletonList(kitchen[0])).price(75999.0).quantity(2).build(),
                    Product.builder().id(77L).name("Холодильник Tesler RCD-545I").productImages(Collections.singletonList(images[55])).categories(Collections.singletonList(kitchen[0])).price(76999.0).quantity(3).build(),
                    Product.builder().id(78L).name("Холодильник ZUGEL ZRSS630W").categories(Collections.singletonList(kitchen[0])).price(79999.0).quantity(4).build(),
                    Product.builder().id(79L).name("Холодильник Liebherr CNsfd 5223").categories(Collections.singletonList(kitchen[0])).price(70999.0).quantity(10).build()
            };
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
            productRepo.saveAll(Arrays.asList(products));
        };
    }

}
