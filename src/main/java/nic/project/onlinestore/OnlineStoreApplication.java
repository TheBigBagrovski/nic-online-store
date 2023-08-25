package nic.project.onlinestore;

import nic.project.onlinestore.model.Category;
import nic.project.onlinestore.model.Filter;
import nic.project.onlinestore.model.FilterValue;
import nic.project.onlinestore.model.Image;
import nic.project.onlinestore.model.Product;
import nic.project.onlinestore.repository.CategoryRepository;
import nic.project.onlinestore.repository.FilterRepository;
import nic.project.onlinestore.repository.FilterValueRepository;
import nic.project.onlinestore.repository.ImageRepository;
import nic.project.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class OnlineStoreApplication implements WebMvcConfigurer {

    @Value("${product_images_path}")
    private String PRODUCTS_IMAGES_PATH;

    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApplication.class, args);
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
                    Image.builder().id(1L).path(PRODUCTS_IMAGES_PATH + "/1/iphone13.jpg").build(),
                    Image.builder().id(2L).path(PRODUCTS_IMAGES_PATH + "/1/iphone-13-2.jpg").build(),
                    Image.builder().id(3L).path(PRODUCTS_IMAGES_PATH + "/3/iphone14.jpg").build(),
                    Image.builder().id(4L).path(PRODUCTS_IMAGES_PATH + "/4/iphone14pro.jpg").build(),
                    Image.builder().id(5L).path(PRODUCTS_IMAGES_PATH + "/5/iphone14promax.jpg").build(),
                    Image.builder().id(6L).path(PRODUCTS_IMAGES_PATH + "/5/iphone14promax-2.jpg").build(),
                    Image.builder().id(7L).path(PRODUCTS_IMAGES_PATH + "/6/iphone11.jpg").build(),
                    Image.builder().id(8L).path(PRODUCTS_IMAGES_PATH + "/6/iphone11-2.jpg").build(),
                    Image.builder().id(9L).path(PRODUCTS_IMAGES_PATH + "/7/iphone11pro.jpg").build(),
                    Image.builder().id(10L).path(PRODUCTS_IMAGES_PATH + "/7/iphone11pro-2.jpg").build(),
                    Image.builder().id(11L).path(PRODUCTS_IMAGES_PATH + "/8/iphone12.jpg").build(),
                    Image.builder().id(12L).path(PRODUCTS_IMAGES_PATH + "/8/iphone12-2.jpg").build(),
                    Image.builder().id(13L).path(PRODUCTS_IMAGES_PATH + "/9/iphone12pro.jpg").build(),
                    Image.builder().id(14L).path(PRODUCTS_IMAGES_PATH + "/9/iphone12pro-2.jpg").build(),
                    Image.builder().id(15L).path(PRODUCTS_IMAGES_PATH + "/11/samsunggalaxys8.jpg").build(),
                    Image.builder().id(16L).path(PRODUCTS_IMAGES_PATH + "/11/samsunggalaxys8-2.jpg").build(),
                    Image.builder().id(17L).path(PRODUCTS_IMAGES_PATH + "/12/samsunggalaxya54.jpg").build(),
                    Image.builder().id(18L).path(PRODUCTS_IMAGES_PATH + "/12/samsunggalaxya54-2.jpg").build(),
                    Image.builder().id(19L).path(PRODUCTS_IMAGES_PATH + "/13/samsunggalaxyszflip4.jpg").build(),
                    Image.builder().id(20L).path(PRODUCTS_IMAGES_PATH + "/14/samsunggalaxys23ultra.jpg").build(),
                    Image.builder().id(21L).path(PRODUCTS_IMAGES_PATH + "/15/samsunggalaxys22ultra.jpg").build(),
                    Image.builder().id(22L).path(PRODUCTS_IMAGES_PATH + "/16/opnordce2.jpg").build(),
                    Image.builder().id(23L).path(PRODUCTS_IMAGES_PATH + "/16/opnordce2-2.jpg").build(),
                    Image.builder().id(24L).path(PRODUCTS_IMAGES_PATH + "/17/huaweinova9.jpg").build(),
                    Image.builder().id(25L).path(PRODUCTS_IMAGES_PATH + "/18/huaweip50.jpg").build(),
                    Image.builder().id(26L).path(PRODUCTS_IMAGES_PATH + "/20/honor70.jpg").build(),
                    Image.builder().id(27L).path(PRODUCTS_IMAGES_PATH + "/22/airpodspro.jpg").build(),
                    Image.builder().id(28L).path(PRODUCTS_IMAGES_PATH + "/22/airpodspro-2.jpg").build(),
                    Image.builder().id(29L).path(PRODUCTS_IMAGES_PATH + "/23/airpodspro2.jpg").build(),
                    Image.builder().id(30L).path(PRODUCTS_IMAGES_PATH + "/23/airpodspro2-2.jpg").build(),
                    Image.builder().id(31L).path(PRODUCTS_IMAGES_PATH + "/25/redmibuds3.jpg").build(),
                    Image.builder().id(32L).path(PRODUCTS_IMAGES_PATH + "/28/Creative SXFI TRIO.jpg").build(),
                    Image.builder().id(33L).path(PRODUCTS_IMAGES_PATH + "/29/Sennheiser CX 300S.jpg").build(),
                    Image.builder().id(34L).path(PRODUCTS_IMAGES_PATH + "/32/Bluetooth-гарнитура Sony WI-C200.jpg").build(),
                    Image.builder().id(35L).path(PRODUCTS_IMAGES_PATH + "/33/Bluetooth-гарнитура Sony WH-1000XM4.jpg").build(),
                    Image.builder().id(36L).path(PRODUCTS_IMAGES_PATH + "/33/Bluetooth-гарнитура Sony WH-1000XM4-2.jpg").build(),
                    Image.builder().id(37L).path(PRODUCTS_IMAGES_PATH + "/34/Bluetooth-гарнитура Sony WH-1000XM5.jpg").build(),
                    Image.builder().id(38L).path(PRODUCTS_IMAGES_PATH + "/34/Bluetooth-гарнитура Sony WH-1000XM5-2.jpg").build(),
                    Image.builder().id(39L).path(PRODUCTS_IMAGES_PATH + "/35/Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL.jpg").build(),
                    Image.builder().id(40L).path(PRODUCTS_IMAGES_PATH + "/37/Bluetooth-гарнитура JBL Tune 115BT.jpg").build(),
                    Image.builder().id(41L).path(PRODUCTS_IMAGES_PATH + "/41/Чехол-книжка DF для OPPO A17.jpg").build(),
                    Image.builder().id(42L).path(PRODUCTS_IMAGES_PATH + "/42/Чехол-книжка DF для realme C35.jpg").build(),
                    Image.builder().id(43L).path(PRODUCTS_IMAGES_PATH + "/48/Умная колонка Яндекс Станция 2.jpg").build(),
                    Image.builder().id(44L).path(PRODUCTS_IMAGES_PATH + "/49/Умная колонка Яндекс Станция Макс.jpg").build(),
                    Image.builder().id(45L).path(PRODUCTS_IMAGES_PATH + "/50/Умная колонка Яндекс Станция Мини с часами.jpg").build(),
                    Image.builder().id(46L).path(PRODUCTS_IMAGES_PATH + "/54/Умная колонка VK Капсула.jpg").build(),
                    Image.builder().id(47L).path(PRODUCTS_IMAGES_PATH + "/55/Умная колонка SberBoom.jpg").build(),
                    Image.builder().id(48L).path(PRODUCTS_IMAGES_PATH + "/60/Колонки 2.1 SVEN MS-2050.jpg").build(),
                    Image.builder().id(49L).path(PRODUCTS_IMAGES_PATH + "/62/Колонки 2.0 F&D T-70X.jpg").build(),
                    Image.builder().id(50L).path(PRODUCTS_IMAGES_PATH + "/66/Стиральная машина Бирюса WM-ME610-04.jpg").build(),
                    Image.builder().id(51L).path(PRODUCTS_IMAGES_PATH + "/68/Стиральная машина Beko WRS5512BWW.jpg").build(),
                    Image.builder().id(52L).path(PRODUCTS_IMAGES_PATH + "/70/Пылесос DEXP H-1600.jpg").build(),
                    Image.builder().id(53L).path(PRODUCTS_IMAGES_PATH + "/73/Пылесос Supra VCS-1410.jpg").build(),
                    Image.builder().id(54L).path(PRODUCTS_IMAGES_PATH + "/76/Холодильник HIBERG RFS-480DX.jpg").build(),
                    Image.builder().id(55L).path(PRODUCTS_IMAGES_PATH + "/76/Холодильник HIBERG RFS-480DX-2.jpg").build(),
                    Image.builder().id(56L).path(PRODUCTS_IMAGES_PATH + "/77/Холодильник Tesler RCD-545I.jpg").build()
            };
            Product[] products = new Product[]{
                    Product.builder().id(1L).name("Смартфон Apple iPhone 13").images(Arrays.asList(images[0], images[1])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(80999.0).quantity(5).build(),
                    Product.builder().id(2L).name("Смартфон Apple iPhone 13 PRO").categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(80999.0).quantity(5).build(),
                    Product.builder().id(3L).name("Смартфон Apple iPhone 14").images(Collections.singletonList(images[2])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(80999.0).quantity(10).build(),
                    Product.builder().id(4L).name("Смартфон Apple iPhone 14 PRO").images(Collections.singletonList(images[3])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(89999.0).quantity(3).build(),
                    Product.builder().id(5L).name("Смартфон Apple iPhone 14 PRO MAX").images(Arrays.asList(images[4], images[5])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(115999.0).quantity(2).build(),
                    Product.builder().id(6L).name("Смартфон Apple iPhone 11").images(Arrays.asList(images[6], images[7])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(45999.0).quantity(15).build(),
                    Product.builder().id(7L).name("Смартфон Apple iPhone 11 PRO").images(Arrays.asList(images[8], images[9])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(70999.0).quantity(10).build(),
                    Product.builder().id(8L).name("Смартфон Apple iPhone 12").images(Arrays.asList(images[10], images[11])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(10).build(),
                    Product.builder().id(9L).name("Смартфон Apple iPhone 12 PRO").images(Arrays.asList(images[12], images[13])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(80999.0).quantity(10).build(),
                    Product.builder().id(10L).name("Смартфон Apple iPhone SE 2020").categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(20).build(),
                    Product.builder().id(11L).name("Смартфон Samsung Galaxy S8").images(Arrays.asList(images[14], images[15])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(45999.0).quantity(8).build(),
                    Product.builder().id(12L).name("Смартфон Samsung Galaxy A54").images(Arrays.asList(images[16], images[17])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(8).build(),
                    Product.builder().id(13L).name("Смартфон Samsung Galaxy Z Flip4").images(Collections.singletonList(images[18])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(89999.0).quantity(3).build(),
                    Product.builder().id(14L).name("Смартфон Samsung Galaxy S23 Ultra").images(Collections.singletonList(images[19])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(45999.0).quantity(4).build(),
                    Product.builder().id(15L).name("Смартфон Samsung Galaxy S22 Ultra").images(Collections.singletonList(images[20])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(5).build(),
                    Product.builder().id(16L).name("Смартфон OnePlus Nord CE2").images(Arrays.asList(images[21], images[22])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(8).build(),
                    Product.builder().id(17L).name("Смартфон HUAWEI Nova 9").images(Collections.singletonList(images[23])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(34999.0).quantity(8).build(),
                    Product.builder().id(18L).name("Смартфон HUAWEI P50").images(Collections.singletonList(images[24])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(34999.0).quantity(15).build(),
                    Product.builder().id(19L).name("Смартфон Google Pixel 6").categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(5).build(),
                    Product.builder().id(20L).name("Смартфон Honor 70").images(Collections.singletonList(images[25])).categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(4).build(),
                    Product.builder().id(21L).name("Смартфон OPPO Reno8 T").categories(new HashSet<Category>() {{
                        add(categories[0]);
                    }}).price(55999.0).quantity(4).build(),
                    Product.builder().id(22L).name("TWS-наушники Apple AirPods Pro").images(Arrays.asList(images[26], images[27])).categories(new HashSet<Category>() {{
                        add(au_headphones[0]);
                        add(ag_headphones[0]);
                    }}).price(11999.0).quantity(25).build(),
                    Product.builder().id(23L).name("TWS-наушники Apple AirPods Pro 2").images(Arrays.asList(images[28], images[29])).categories(new HashSet<Category>() {{
                        add(au_headphones[0]);
                        add(ag_headphones[0]);
                    }}).price(11999.0).quantity(50).build(),
                    Product.builder().id(24L).name("TWS-наушники Xiaomi Redmi Buds 3 Lite").categories(new HashSet<Category>() {{
                        add(au_headphones[0]);
                        add(ag_headphones[0]);
                    }}).price(1399.0).quantity(50).build(),
                    Product.builder().id(25L).name("TWS-наушники Xiaomi Redmi Buds 3").images(Collections.singletonList(images[30])).categories(new HashSet<Category>() {{
                        add(au_headphones[0]);
                        add(ag_headphones[0]);
                    }}).price(3999.0).quantity(50).build(),
                    Product.builder().id(26L).name("TWS-наушники Xiaomi Mi True Wireless Earphones 2 Pro").categories(new HashSet<Category>() {{
                        add(au_headphones[0]);
                        add(ag_headphones[0]);
                    }}).price(3999.0).quantity(50).build(),
                    Product.builder().id(27L).name("TWS-наушники Honor Choice Earbuds X3").categories(new HashSet<Category>() {{
                        add(au_headphones[0]);
                        add(ag_headphones[0]);
                    }}).price(2999.0).quantity(50).build(),
                    Product.builder().id(28L).name("Проводная гарнитура Creative SXFI TRIO").images(Collections.singletonList(images[31])).categories(new HashSet<Category>() {{
                        add(au_headphones[2]);
                        add(ag_headphones[2]);
                    }}).price(5999.0).quantity(20).build(),
                    Product.builder().id(29L).name("Проводная гарнитура Sennheiser CX 300S").images(Collections.singletonList(images[32])).categories(new HashSet<Category>() {{
                        add(au_headphones[2]);
                        add(ag_headphones[2]);
                    }}).price(1999.0).quantity(20).build(),
                    Product.builder().id(30L).name("Проводная гарнитура HyperX Cloud Earbuds HX-HSCEB-RD").categories(new HashSet<Category>() {{
                        add(au_headphones[2]);
                        add(ag_headphones[2]);
                    }}).price(15999.0).quantity(20).build(),
                    Product.builder().id(31L).name("Bluetooth-гарнитура Sony WI-SP500").categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(1999.0).quantity(20).build(),
                    Product.builder().id(32L).name("Bluetooth-гарнитура Sony WI-C200").images(Collections.singletonList(images[33])).categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(2999.0).quantity(10).build(),
                    Product.builder().id(33L).name("Bluetooth-гарнитура Sony WH-1000XM4").images(Arrays.asList(images[34], images[35])).categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(31999.0).quantity(5).build(),
                    Product.builder().id(34L).name("Bluetooth-гарнитура Sony WH-1000XM5").images(Arrays.asList(images[36], images[37])).categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(41999.0).quantity(2).build(),
                    Product.builder().id(35L).name("Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL").images(Collections.singletonList(images[38])).categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(11999.0).quantity(2).build(),
                    Product.builder().id(36L).name("Bluetooth-гарнитура Sennheiser IE 100 PRO Wireless").categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(12999.0).quantity(10).build(),
                    Product.builder().id(37L).name("Bluetooth-гарнитура JBL Tune 115BT").images(Collections.singletonList(images[39])).categories(new HashSet<Category>() {{
                        add(au_headphones[1]);
                        add(ag_headphones[1]);
                    }}).price(5999.0).quantity(20).build(),
                    Product.builder().id(38L).name("Чехол для Huawei P50").categories(new HashSet<Category>() {{
                        add(addgoods[1]);
                    }}).price(999.0).quantity(30).build(),
                    Product.builder().id(39L).name("Чехол-книжка Aceline Strap для Xiaomi Redmi 10C").categories(new HashSet<Category>() {{
                        add(addgoods[1]);
                    }}).price(999.0).quantity(30).build(),
                    Product.builder().id(40L).name("Чехол-книжка Samsung Smart S View Wallet Cover для Samsung Galaxy A53").images(null).categories(new HashSet<Category>() {{
                        add(addgoods[1]);
                    }}).price(999.0).quantity(30).build(),
                    Product.builder().id(41L).name("Чехол-книжка DF для OPPO A17").images(Collections.singletonList(images[40])).categories(new HashSet<Category>() {{
                        add(addgoods[1]);
                    }}).price(999.0).quantity(30).build(),
                    Product.builder().id(42L).name("Чехол-книжка DF для realme C35").images(Collections.singletonList(images[41])).categories(new HashSet<Category>() {{
                        add(addgoods[1]);
                    }}).price(999.0).quantity(30).build(),
                    Product.builder().id(43L).name("Накладка Apple Clear Case with MagSafe для Apple iPhone 14 Pro Max").images(null).categories(new HashSet<Category>() {{
                        add(addgoods[2]);
                    }}).price(5999.0).quantity(30).build(),
                    Product.builder().id(44L).name("Накладка Apple Leather Case with MagSafe для Apple iPhone 14 Pro").images(null).categories(new HashSet<Category>() {{
                        add(addgoods[2]);
                    }}).price(5999.0).quantity(30).build(),
                    Product.builder().id(45L).name("Накладка DF для Samsung Galaxy A73").images(null).categories(new HashSet<Category>() {{
                        add(addgoods[2]);
                    }}).price(499.0).quantity(30).build(),
                    Product.builder().id(46L).name("Накладка DF для Xiaomi Redmi 10C").images(null).categories(new HashSet<Category>() {{
                        add(addgoods[2]);
                    }}).price(499.0).quantity(30).build(),
                    Product.builder().id(47L).name("Умная колонка Яндекс Станция").images(null).categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(2999.0).quantity(10).build(),
                    Product.builder().id(48L).name("Умная колонка Яндекс Станция 2").images(Collections.singletonList(images[42])).categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(2999.0).quantity(5).build(),
                    Product.builder().id(49L).name("Умная колонка Яндекс Станция Макс").images(Collections.singletonList(images[43])).categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(2999.0).quantity(5).build(),
                    Product.builder().id(50L).name("Умная колонка Яндекс Станция Мини с часами").images(Collections.singletonList(images[44])).categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(3999.0).quantity(2).build(),
                    Product.builder().id(51L).name("Умная колонка Xiaomi Mi Smart Speaker").categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(3999.0).quantity(8).build(),
                    Product.builder().id(52L).name("Умная колонка VK Капсула Мини").categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(2999.0).quantity(4).build(),
                    Product.builder().id(53L).name("Умная колонка VK Капсула Нео").categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(4999.0).quantity(8).build(),
                    Product.builder().id(54L).name("Умная колонка VK Капсула").images(Collections.singletonList(images[45])).categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(3999.0).quantity(2).build(),
                    Product.builder().id(55L).name("Умная колонка SberBoom").images(Collections.singletonList(images[46])).categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(4399.0).quantity(4).build(),
                    Product.builder().id(56L).name("Умная колонка LG XBOOM AI ThinQ WK7Y").categories(new HashSet<Category>() {{
                        add(audio[0]);
                    }}).price(3999.0).quantity(10).build(),
                    Product.builder().id(57L).name("Колонки 2.0 DEXP R610").categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(8999.0).quantity(10).build(),
                    Product.builder().id(58L).name("Колонки 2.0 Edifier S3000Pro").categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(5999.0).quantity(10).build(),
                    Product.builder().id(59L).name("Колонки 2.0 Edifier R1855DB").categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(15999.0).quantity(10).build(),
                    Product.builder().id(60L).name("Колонки 2.1 SVEN MS-2050").images(Collections.singletonList(images[47])).categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(3999.0).quantity(5).build(),
                    Product.builder().id(61L).name("Колонки 2.1 Edifier M601DB").categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(4999.0).quantity(5).build(),
                    Product.builder().id(62L).name("Колонки 2.0 F&D T-70X").images(Collections.singletonList(images[48])).categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(2999.0).quantity(10).build(),
                    Product.builder().id(63L).name("Колонки 2.0 SVEN MC-30").categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(4999.0).quantity(5).build(),
                    Product.builder().id(64L).name("Колонки 2.0 F&D T-60X").categories(new HashSet<Category>() {{
                        add(audio[1]);
                    }}).price(7999.0).quantity(10).build(),
                    Product.builder().id(65L).name("Стиральная машина DEXP WM-F610NTMA/WW").categories(new HashSet<Category>() {{
                        add(house[0]);
                    }}).price(14999.0).quantity(5).build(),
                    Product.builder().id(66L).name("Стиральная машина Бирюса WM-ME610/04").images(Collections.singletonList(images[49])).categories(new HashSet<Category>() {{
                        add(house[0]);
                    }}).price(14999.0).quantity(10).build(),
                    Product.builder().id(67L).name("Стиральная машина Indesit IWSD 51051 CIS").categories(new HashSet<Category>() {{
                        add(house[0]);
                    }}).price(9999.0).quantity(20).build(),
                    Product.builder().id(68L).name("Стиральная машина Beko WRS5512BWW").images(Collections.singletonList(images[50])).categories(new HashSet<Category>() {{
                        add(house[0]);
                    }}).price(10999.0).quantity(18).build(),
                    Product.builder().id(69L).name("Стиральная машина TCL TWF60-G103061A03").categories(new HashSet<Category>() {{
                        add(house[0]);
                    }}).price(15999.0).quantity(17).build(),
                    Product.builder().id(70L).name("Пылесос DEXP H-1600").images(Collections.singletonList(images[51])).categories(new HashSet<Category>() {{
                        add(house[1]);
                    }}).price(2999.0).quantity(10).build(),
                    Product.builder().id(71L).name("Пылесос ECON ECO-1414VB").categories(new HashSet<Category>() {{
                        add(house[1]);
                    }}).price(2999.0).quantity(2).build(),
                    Product.builder().id(72L).name("Пылесос Starwind SCB1112").categories(new HashSet<Category>() {{
                        add(house[1]);
                    }}).price(3999.0).quantity(5).build(),
                    Product.builder().id(73L).name("Пылесос Supra VCS-1410").images(Collections.singletonList(images[52])).categories(new HashSet<Category>() {{
                        add(house[1]);
                    }}).price(3999.0).quantity(5).build(),
                    Product.builder().id(74L).name("Пылесос BQ VC1802B").categories(new HashSet<Category>() {{
                        add(house[1]);
                    }}).price(4999.0).quantity(5).build(),
                    Product.builder().id(75L).name("Холодильник Liebherr CBNd 5223").categories(new HashSet<Category>() {{
                        add(kitchen[0]);
                    }}).price(75999.0).quantity(2).build(),
                    Product.builder().id(76L).name("Холодильник HIBERG RFS-480DX").images(Arrays.asList(images[53], images[54])).categories(new HashSet<Category>() {{
                        add(kitchen[0]);
                    }}).price(75999.0).quantity(2).build(),
                    Product.builder().id(77L).name("Холодильник Tesler RCD-545I").images(Collections.singletonList(images[55])).categories(new HashSet<Category>() {{
                        add(kitchen[0]);
                    }}).price(76999.0).quantity(3).build(),
                    Product.builder().id(78L).name("Холодильник ZUGEL ZRSS630W").categories(new HashSet<Category>() {{
                        add(kitchen[0]);
                    }}).price(79999.0).quantity(4).build(),
                    Product.builder().id(79L).name("Холодильник Liebherr CNsfd 5223").categories(new HashSet<Category>() {{
                        add(kitchen[0]);
                    }}).price(70999.0).quantity(10).build()
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
            products[1].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[2].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[3].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[4].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[5].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[6].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[7].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[8].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[9].addFilterProperty(smartphonesBrand, filterValues.get(0));
            products[10].addFilterProperty(smartphonesBrand, filterValues.get(1));
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
