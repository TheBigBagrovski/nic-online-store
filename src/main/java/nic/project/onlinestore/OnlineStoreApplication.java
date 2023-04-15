package nic.project.onlinestore;

import nic.project.onlinestore.models.Category;
import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.ProductImage;
import nic.project.onlinestore.repositories.CategoryRepository;
import nic.project.onlinestore.repositories.ProductImageRepository;
import nic.project.onlinestore.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class OnlineStoreApplication implements WebMvcConfigurer {

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
            ProductImage[] productImages = {
                    new ProductImage(1L, "images/product-images/iphone13.jpg"),
                    new ProductImage(2L, "images/product-images/iphone13-2.jpg"),
                    new ProductImage(3L, "images/product-images/iphone14.jpg"),
                    new ProductImage(4L, "images/product-images/iphone14pro.jpg"),
                    new ProductImage(5L, "images/product-images/iphone14promax.jpg"),
                    new ProductImage(6L, "images/product-images/iphone14promax-2.jpg"),
                    new ProductImage(7L, "images/product-images/iphone11.jpg"),
                    new ProductImage(8L, "images/product-images/iphone11-2.jpg"),
                    new ProductImage(9L, "images/product-images/iphone11pro.jpg"),
                    new ProductImage(10L, "images/product-images/iphone11pro-2.jpg"),
                    new ProductImage(11L, "images/product-images/iphone12.jpg"),
                    new ProductImage(12L, "images/product-images/iphone12-2.jpg"),
                    new ProductImage(13L, "images/product-images/iphone12pro.jpg"),
                    new ProductImage(14L, "images/product-images/iphone12pro-2.jpg"),
                    new ProductImage(15L, "images/product-images/samsunggalaxys8.jpg"),
                    new ProductImage(16L, "images/product-images/samsunggalaxys8-2.jpg"),
                    new ProductImage(17L, "images/product-images/samsunggalaxya54.jpg"),
                    new ProductImage(18L, "images/product-images/samsunggalaxya54-2.jpg"),
                    new ProductImage(19L, "images/product-images/samsunggalaxyszflip4.jpg"),
                    new ProductImage(20L, "images/product-images/samsunggalaxys23ultra.jpg"),
                    new ProductImage(21L, "images/product-images/samsunggalaxys22ultra.jpg"),
                    new ProductImage(22L, "images/product-images/opnordce2.jpg"),
                    new ProductImage(23L, "images/product-images/opnordce2-2.jpg"),
                    new ProductImage(24L, "images/product-images/huaweinova9.jpg"),
                    new ProductImage(25L, "images/product-images/huaweip50.jpg"),
                    new ProductImage(26L, "images/product-images/honor70.jpg"),
                    new ProductImage(27L, "images/product-images/airpodspro.jpg"),
                    new ProductImage(28L, "images/product-images/airpodspro-2.jpg"),
                    new ProductImage(29L, "images/product-images/airpodspro2.jpg"),
                    new ProductImage(30L, "images/product-images/airpodspro2-2.jpg"),
                    new ProductImage(31L, "images/product-images/redmibuds3.jpg"),
                    new ProductImage(32L, "images/product-images/Creative SXFI TRIO.jpg"),
                    new ProductImage(33L, "images/product-images/Sennheiser CX 300S.jpg"),
                    new ProductImage(34L, "images/product-images/Bluetooth-гарнитура Sony WI-C200.jpg"),
                    new ProductImage(35L, "images/product-images/Bluetooth-гарнитура Sony WH-1000XM4.jpg"),
                    new ProductImage(36L, "images/product-images/Bluetooth-гарнитура Sony WH-1000XM4-2.jpg"),
                    new ProductImage(37L, "images/product-images/Bluetooth-гарнитура Sony WH-1000XM5.jpg"),
                    new ProductImage(38L, "images/product-images/Bluetooth-гарнитура Sony WH-1000XM5-2.jpg"),
                    new ProductImage(39L, "images/product-images/Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL.jpg"),
                    new ProductImage(40L, "images/product-images/Bluetooth-гарнитура JBL Tune 115BT.jpg"),

                    new ProductImage(41L, "images/product-images/Чехол-книжка DF для OPPO A17.jpg"),
                    new ProductImage(42L, "images/product-images/Чехол-книжка DF для realme C35.jpg"),
                    new ProductImage(43L, "images/product-images/Умная колонка Яндекс Станция 2.jpg"),
                    new ProductImage(44L, "images/product-images/Умная колонка Яндекс Станция Макс.jpg"),
                    new ProductImage(45L, "images/product-images/Умная колонка Яндекс Станция Мини с часами.jpg"),
                    new ProductImage(46L, "images/product-images/Умная колонка VK Капсула.jpg"),
                    new ProductImage(47L, "images/product-images/Умная колонка SberBoom.jpg"),
                    new ProductImage(48L, "images/product-images/Колонки 2.1 SVEN MS-2050.jpg"),
                    new ProductImage(49L, "images/product-images/Колонки 2.0 F&D T-70X.jpg"),
                    new ProductImage(50L, "images/product-images/Стиральная машина Бирюса WM-ME610-04.jpg"),
                    new ProductImage(51L, "images/product-images/Стиральная машина Beko WRS5512BWW.jpg"),
                    new ProductImage(52L, "images/product-images/Пылесос DEXP H-1600.jpg"),
                    new ProductImage(53L, "images/product-images/Пылесос Supra VCS-1410.jpg"),
                    new ProductImage(54L, "images/product-images/Холодильник HIBERG RFS-480DX.jpg"),
                    new ProductImage(55L, "images/product-images/Холодильник HIBERG RFS-480DX-2.jpg"),
                    new ProductImage(56L, "images/product-images/Холодильник Tesler RCD-545I.jpg")
            };
            Product[] products = new Product[]{
                    new Product(1L, "Смартфон Apple iPhone 13", Arrays.asList(productImages[0], productImages[1]), Collections.singletonList(smartphones[0]), 80999.0, 5),
                    new Product(2L, "Смартфон Apple iPhone 13 PRO", null, Collections.singletonList(smartphones[0]), 80999.0, 5),
                    new Product(3L, "Смартфон Apple iPhone 14", Collections.singletonList(productImages[2]), Collections.singletonList(smartphones[0]), 80999.0, 10),
                    new Product(4L, "Смартфон Apple iPhone 14 PRO", Collections.singletonList(productImages[3]), Collections.singletonList(smartphones[0]), 89999.0, 3),
                    new Product(5L, "Смартфон Apple iPhone 14 PRO MAX", Arrays.asList(productImages[4], productImages[5]), Collections.singletonList(smartphones[0]), 115999.0, 2),
                    new Product(6L, "Смартфон Apple iPhone 11", Arrays.asList(productImages[6], productImages[7]), Collections.singletonList(smartphones[0]), 45999.0, 15),
                    new Product(7L, "Смартфон Apple iPhone 11 PRO", Arrays.asList(productImages[8], productImages[9]), Collections.singletonList(smartphones[0]), 70999.0, 10),
                    new Product(8L, "Смартфон Apple iPhone 12", Arrays.asList(productImages[10], productImages[11]), Collections.singletonList(smartphones[0]), 55999.0, 10),
                    new Product(9L, "Смартфон Apple iPhone 12 PRO", Arrays.asList(productImages[12], productImages[13]), Collections.singletonList(smartphones[0]), 80999.0, 10),
                    new Product(10L, "Смартфон Apple iPhone SE 2020", null, Collections.singletonList(smartphones[0]), 55999.0, 20),
                    new Product(11L, "Смартфон Samsung Galaxy S8", Arrays.asList(productImages[14], productImages[15]), Collections.singletonList(smartphones[1]), 45999.0, 8),
                    new Product(12L, "Смартфон Samsung Galaxy A54", Arrays.asList(productImages[16], productImages[17]), Collections.singletonList(smartphones[1]), 55999.0, 8),
                    new Product(13L, "Смартфон Samsung Galaxy Z Flip4", Collections.singletonList(productImages[18]), Collections.singletonList(smartphones[1]), 89999.0, 3),
                    new Product(14L, "Смартфон Samsung Galaxy S23 Ultra", Collections.singletonList(productImages[19]), Collections.singletonList(smartphones[1]), 45999.0, 4),
                    new Product(15L, "Смартфон Samsung Galaxy S22 Ultra", Collections.singletonList(productImages[20]), Collections.singletonList(smartphones[1]), 55999.0, 5),
                    new Product(16L, "Смартфон OnePlus Nord CE2", Arrays.asList(productImages[21], productImages[22]), Collections.singletonList(smartphones[3]), 55999.0, 8),
                    new Product(17L, "Смартфон HUAWEI Nova 9", Collections.singletonList(productImages[23]), Collections.singletonList(smartphones[4]), 34999.0, 8),
                    new Product(18L, "Смартфон HUAWEI P50", Collections.singletonList(productImages[24]), Collections.singletonList(smartphones[4]), 34999.0, 15),
                    new Product(19L, "Смартфон Google Pixel 6", null, Collections.singletonList(smartphones[2]), 55999.0, 5),
                    new Product(20L, "Смартфон Honor 70", Collections.singletonList(productImages[25]), Collections.singletonList(categories[0]), 55999.0, 4),
                    new Product(21L, "Смартфон OPPO Reno8 T", null, Collections.singletonList(categories[0]), 55999.0, 4),
                    new Product(22L, "TWS-наушники Apple AirPods Pro", Arrays.asList(productImages[26], productImages[27]), Arrays.asList(au_headphones[0], ag_headphones[0]), 11999.0, 25),
                    new Product(23L, "TWS-наушники Apple AirPods Pro 2", Arrays.asList(productImages[28], productImages[29]), Arrays.asList(au_headphones[0], ag_headphones[0]), 11999.0, 50),
                    new Product(24L, "TWS-наушники Xiaomi Redmi Buds 3 Lite", null, Arrays.asList(au_headphones[0], ag_headphones[0]), 1399.0, 50),
                    new Product(25L, "TWS-наушники Xiaomi Redmi Buds 3", Collections.singletonList(productImages[30]), Arrays.asList(au_headphones[0], ag_headphones[0]), 3999.0, 50),
                    new Product(26L, "TWS-наушники Xiaomi Mi True Wireless Earphones 2 Pro", null, Arrays.asList(au_headphones[0], ag_headphones[0]), 3999.0, 50),
                    new Product(27L, "TWS-наушники Honor Choice Earbuds X3", null, Arrays.asList(au_headphones[0], ag_headphones[0]), 2999.0, 50),
                    new Product(28L, "Проводная гарнитура Creative SXFI TRIO", Collections.singletonList(productImages[31]), Arrays.asList(au_headphones[2], ag_headphones[2]), 5999.0, 20),
                    new Product(29L, "Проводная гарнитура Sennheiser CX 300S", Collections.singletonList(productImages[32]), Arrays.asList(au_headphones[2], ag_headphones[2]), 1999.0, 20),
                    new Product(30L, "Проводная гарнитура HyperX Cloud Earbuds HX-HSCEB-RD", null, Arrays.asList(au_headphones[2], ag_headphones[2]), 15999.0, 20),
                    new Product(31L, "Bluetooth-гарнитура Sony WI-SP500", null, Arrays.asList(au_headphones[1], ag_headphones[1]), 1999.0, 20),
                    new Product(32L, "Bluetooth-гарнитура Sony WI-C200", Collections.singletonList(productImages[33]), Arrays.asList(au_headphones[1], ag_headphones[1]), 2999.0, 10),
                    new Product(33L, "Bluetooth-гарнитура Sony WH-1000XM4", Arrays.asList(productImages[34], productImages[35]), Arrays.asList(au_headphones[1], ag_headphones[1]), 31999.0, 5),
                    new Product(34L, "Bluetooth-гарнитура Sony WH-1000XM5", Arrays.asList(productImages[36], productImages[37]), Arrays.asList(au_headphones[1], ag_headphones[1]), 41999.0, 2),
                    new Product(35L, "Bluetooth-гарнитура Sennheiser MOMENTUM Wireless M3AEBTXL", Collections.singletonList(productImages[38]), Arrays.asList(au_headphones[1], ag_headphones[1]), 11999.0, 2),
                    new Product(36L, "Bluetooth-гарнитура Sennheiser IE 100 PRO Wireless", null, Arrays.asList(au_headphones[1], ag_headphones[1]), 12999.0, 10),
                    new Product(37L, "Bluetooth-гарнитура JBL Tune 115BT", Collections.singletonList(productImages[39]), Arrays.asList(au_headphones[1], ag_headphones[1]), 5999.0, 20),
                    new Product(38L, "Чехол для Huawei P50", null, Collections.singletonList(addgoods[1]), 999.0, 30),
                    new Product(39L, "Чехол-книжка Aceline Strap для Xiaomi Redmi 10C", null, Collections.singletonList(addgoods[1]), 999.0, 30),
                    new Product(40L, "Чехол-книжка Samsung Smart S View Wallet Cover для Samsung Galaxy A53", null, Collections.singletonList(addgoods[1]), 999.0, 30),
                    new Product(41L, "Чехол-книжка DF для OPPO A17", Collections.singletonList(productImages[40]), Collections.singletonList(addgoods[1]), 999.0, 30),
                    new Product(42L, "Чехол-книжка DF для realme C35", Collections.singletonList(productImages[41]), Collections.singletonList(addgoods[1]), 999.0, 30),
                    new Product(43L, "Накладка Apple Clear Case with MagSafe для Apple iPhone 14 Pro Max", null, Collections.singletonList(addgoods[2]), 5999.0, 30),
                    new Product(44L, "Накладка Apple Leather Case with MagSafe для Apple iPhone 14 Pro", null, Collections.singletonList(addgoods[2]), 5999.0, 30),
                    new Product(45L, "Накладка DF для Samsung Galaxy A73", null, Collections.singletonList(addgoods[2]), 499.0, 30),
                    new Product(46L, "Накладка DF для Xiaomi Redmi 10C", null, Collections.singletonList(addgoods[2]), 499.0, 30),
                    new Product(47L, "Умная колонка Яндекс Станция", null, Collections.singletonList(audio[0]), 2999.0, 10),
                    new Product(48L, "Умная колонка Яндекс Станция 2", Collections.singletonList(productImages[42]), Collections.singletonList(audio[0]), 2999.0, 5),
                    new Product(49L, "Умная колонка Яндекс Станция Макс", Collections.singletonList(productImages[43]), Collections.singletonList(audio[0]), 2999.0, 5),
                    new Product(50L, "Умная колонка Яндекс Станция Мини с часами", Collections.singletonList(productImages[44]), Collections.singletonList(audio[0]), 3999.0, 2),
                    new Product(51L, "Умная колонка Xiaomi Mi Smart Speaker", null, Collections.singletonList(audio[0]), 3999.0, 8),
                    new Product(52L, "Умная колонка VK Капсула Мини", null, Collections.singletonList(audio[0]), 2999.0, 4),
                    new Product(53L, "Умная колонка VK Капсула Нео", null, Collections.singletonList(audio[0]), 4999.0, 8),
                    new Product(54L, "Умная колонка VK Капсула", Collections.singletonList(productImages[45]), Collections.singletonList(audio[0]), 3999.0, 2),
                    new Product(55L, "Умная колонка SberBoom", Collections.singletonList(productImages[46]), Collections.singletonList(audio[0]), 4399.0, 4),
                    new Product(56L, "Умная колонка LG XBOOM AI ThinQ WK7Y", null, Collections.singletonList(audio[0]), 3999.0, 10),
                    new Product(57L, "Колонки 2.0 DEXP R610", null, Collections.singletonList(audio[1]), 8999.0, 10),
                    new Product(58L, "Колонки 2.0 Edifier S3000Pro", null, Collections.singletonList(audio[1]), 5999.0, 10),
                    new Product(59L, "Колонки 2.0 Edifier R1855DB", null, Collections.singletonList(audio[1]), 15999.0, 10),
                    new Product(60L, "Колонки 2.1 SVEN MS-2050", Collections.singletonList(productImages[47]), Collections.singletonList(audio[1]), 3999.0, 5),
                    new Product(61L, "Колонки 2.1 Edifier M601DB", null, Collections.singletonList(audio[1]), 4999.0, 5),
                    new Product(62L, "Колонки 2.0 F&D T-70X", Collections.singletonList(productImages[48]), Collections.singletonList(audio[1]), 2999.0, 10),
                    new Product(63L, "Колонки 2.0 SVEN MC-30", null, Collections.singletonList(audio[1]), 4999.0, 5),
                    new Product(64L, "Колонки 2.0 F&D T-60X", null, Collections.singletonList(audio[1]), 7999.0, 10),
                    new Product(65L, "Стиральная машина DEXP WM-F610NTMA/WW", null, Collections.singletonList(house[0]), 14999.0, 5),
                    new Product(66L, "Стиральная машина Бирюса WM-ME610/04", Collections.singletonList(productImages[49]), Collections.singletonList(house[0]), 14999.0, 10),
                    new Product(67L, "Стиральная машина Indesit IWSD 51051 CIS", null, Collections.singletonList(house[0]), 9999.0, 20),
                    new Product(68L, "Стиральная машина Beko WRS5512BWW", Collections.singletonList(productImages[50]), Collections.singletonList(house[0]), 10999.0, 18),
                    new Product(69L, "Стиральная машина TCL TWF60-G103061A03", null, Collections.singletonList(house[0]), 15999.0, 17),
                    new Product(70L, "Пылесос DEXP H-1600", Collections.singletonList(productImages[51]), Collections.singletonList(house[1]), 2999.0, 10),
                    new Product(71L, "Пылесос ECON ECO-1414VB", null, Collections.singletonList(house[1]), 2999.0, 2),
                    new Product(72L, "Пылесос Starwind SCB1112", null, Collections.singletonList(house[1]), 3999.0, 5),
                    new Product(73L, "Пылесос Supra VCS-1410", Collections.singletonList(productImages[52]), Collections.singletonList(house[1]), 3999.0, 5),
                    new Product(74L, "Пылесос BQ VC1802B", null, Collections.singletonList(house[1]), 4999.0, 5),
                    new Product(75L, "Холодильник Liebherr CBNd 5223", null, Collections.singletonList(kitchen[0]), 75999.0, 2),
                    new Product(76L, "Холодильник HIBERG RFS-480DX", Arrays.asList(productImages[53], productImages[54]), Collections.singletonList(kitchen[0]), 75999.0, 2),
                    new Product(77L, "Холодильник Tesler RCD-545I", Collections.singletonList(productImages[55]), Collections.singletonList(kitchen[0]), 76999.0, 3),
                    new Product(78L, "Холодильник ZUGEL ZRSS630W", null, Collections.singletonList(kitchen[0]), 79999.0, 4),
                    new Product(79L, "Холодильник Liebherr CNsfd 5223", null, Collections.singletonList(kitchen[0]), 70999.0, 10)
            };
            productImageRepo.saveAll(Arrays.asList(productImages));
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
