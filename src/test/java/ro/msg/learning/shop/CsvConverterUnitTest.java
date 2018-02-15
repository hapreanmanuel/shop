package ro.msg.learning.shop;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.domain.StockKey;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.utility.converter.CsvConverterUtil;
import ro.msg.learning.shop.utility.converter.CsvHttpMessageConverter;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvConverterUnitTest {

    private CsvHttpMessageConverter csvConverter = new CsvHttpMessageConverter();

    private CsvMapper mapper = new CsvMapper();

    private CsvSchema schema;

    private List<Stock> targetStocks;

    private CsvConverterUtil converter = new CsvConverterUtil();

    @Test
    public void toCsv(){
        Stock s1 = new Stock(); s1.setStockKey(new StockKey(1,1)); s1.setQuantity(10);
        Stock s2 = new Stock(); s1.setStockKey(new StockKey(2,1)); s1.setQuantity(20);
        Stock s3 = new Stock(); s1.setStockKey(new StockKey(3,1)); s1.setQuantity(30);

        targetStocks.add(s1); targetStocks.add(s2); targetStocks.add(s3);

    }

    @Test
    public void fromCsv(){

    }

    @Test
    public void printMediaTypeToString(){

        System.out.println(new MediaType("text", "csv").toString());

    }

}
