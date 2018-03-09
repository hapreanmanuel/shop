package ro.msg.learning.shop.unit;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.utility.converter.CsvConverterUtil;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
public class CsvConverterUnitTest {

    private final List<StockDto> targetStocks = Arrays.asList(
            new StockDto(1,1,10),
            new StockDto(1,2,20),
            new StockDto(2,1,5)
    );
    private final String targetStocksCsvString = "locationId,productId,quantity\n1,1,10\n2,1,20\n1,2,5\n";

    @Test
    public void toCsv() throws Exception{
        OutputStream os = new ByteArrayOutputStream();

        CsvConverterUtil.toCsv(StockDto.class, targetStocks, os);

        log.info("To CSV expected output: ");
        log.info(targetStocksCsvString);
        log.info("To CSV actual output: ");
        log.info(os.toString());

        assertThat(os.toString()).isEqualTo(targetStocksCsvString);
    }

    @Test
    public void fromCsv() throws Exception{
        InputStream is = new ByteArrayInputStream(targetStocksCsvString.getBytes());

        List result = CsvConverterUtil.fromCsv(StockDto.class, is);

        log.info("From CSV expected output: ");
        log.info(targetStocks.toString());
        log.info("From CSV actual output: ");
        log.info(result.toString());

        assertThat(result).isEqualTo(targetStocks);
    }

}
