package ro.msg.learning.shop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.domain.Supplier;
import ro.msg.learning.shop.repository.jdbc.ProductCategoryRepository;
import ro.msg.learning.shop.repository.jdbc.ProductRepository;
import ro.msg.learning.shop.repository.jdbc.SupplierRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

@EntityScan("ro.msg.learning.shop.domain")
@ComponentScan("ro.msg.learning.shop")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	Row Mapping logic should be defined in another class
	 */
	public List<Product> getAllProductsRowMapper(){
		return jdbcTemplate.query("SELECT * FROM PRODUCT", (rs, rowNumber) -> {
            Product p = new Product();

            p.setProductId(rs.getInt("PRODUCTID"));
            p.setCategoryId(rs.getInt("CATEGORYID"));
            p.setSupplierId(rs.getInt("SUPPLIERID"));
            p.setName(rs.getString("NAME"));
            p.setDescription(rs.getString("DESCRIPTION"));
            p.setPrice(rs.getBigDecimal("PRICE"));
            p.setWeight(rs.getDouble("WEIGHT"));
            return p;
        });
	}

	public String readFileAsString(String path, Charset encoding) {
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(path), encoding);

			StringBuilder fileAsString = new StringBuilder();
			String line;

			while( (line = reader.readLine()) !=null){
				fileAsString.append(line);
			}

			return fileAsString.toString();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void scriptFilePath(){
		String scriptFilePath = "/home/vagrant/Downloads/shop/src/main/resources/sql-scripts/schema.sql";
		Charset encoding = StandardCharsets.UTF_8;

		System.out.println(readFileAsString(scriptFilePath,encoding));

	}

	/*
	This test creates a connection to the database, a prepared statement for creating all the tables,
	A new entry is added to the ProductCategory table		(required FK in 'Product')
	A new entry is added to the Suppliers table				(required FK in 'Product')
	2 new entries are added to the Product table (via productRepositorry (CrudRepository.save())
	The objects are retrieved thereafter with JdbcTemplate and RowMapper
	 */
	@Test
	public void JDBCInitialization() throws SQLException {

		// initialize script path
		String scriptFilePath = "/home/vagrant/Downloads/shop/src/main/resources/sql-scripts/schema.sql";
		Charset encoding = StandardCharsets.UTF_8;

		Connection con = null;

		try {
			// load driver class for h2
			Class.forName("org.h2.Driver");
			// create connection
			con = DriverManager.getConnection("jdbc:h2:~/hapreanm/Database/shop",
					"sa", "");
			PreparedStatement dbInitialization = con.prepareStatement(readFileAsString(scriptFilePath,encoding));

			dbInitialization.execute();

			System.out.println("Tables created succesfully.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close db connection
			if (con != null) {
				con.close();
			}
		}

		/*
		Hand-written repositories or extensions of CrudRepository< , > ??
		 */

		//Define a new product categroy
		ProductCategory pc = new ProductCategory();
		pc.setCategoryId(0);
		pc.setDescription("Test category");
		pc.setName("Hope it works!");
		productCategoryRepository.save(pc);

		assertThat(productCategoryRepository.findAll()).isNotEmpty(); System.out.println("Product category 'test' succesfully added to the table");

		Supplier supplier = new Supplier();
		supplier.setSupplierId(0);
		supplier.setName("Sponsor");
		supplierRepository.save(supplier);

		assertThat(supplierRepository.findAll()).isNotEmpty(); System.out.println("Supplier 'Sponsor' succesfully added to the table.");

		Product p1 = new Product();
		Product p2 = new Product();

		p1.setProductId(1);
		p1.setCategoryId(0);
		p1.setSupplierId(0);
		p1.setName("Dummy");
		p1.setDescription("1st gen");
		p1.setPrice(BigDecimal.valueOf(123.33));
		p1.setWeight(12.3);

		p2.setProductId(2);
		p2.setCategoryId(0);
		p2.setSupplierId(0);
		p2.setName("Dumbo");
		p2.setDescription("2nd ver");
		p2.setPrice(BigDecimal.valueOf(177.73));
		p2.setWeight(1.3);

		productRepository.save(p1);	System.out.println("P1 succesfully added to Product table.");
		productRepository.save(p2);	System.out.println("P2 succesfully added to Product table.");

		//Row mapper
		List<Product> allProducts = getAllProductsRowMapper();

		//Assertions go here
		assertThat(allProducts).size().isEqualTo(2);
		assertThat(allProducts).contains(p1); System.out.println(p1.toString());
		assertThat(allProducts).contains(p2); System.out.println(p2.toString());

	}
}


