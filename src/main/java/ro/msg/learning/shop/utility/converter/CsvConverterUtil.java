package ro.msg.learning.shop.utility.converter;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface CsvConverterUtil {

    static List fromCsv(Class pojoClass, InputStream inputStream) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(pojoClass).withHeader();

        MappingIterator iterator = mapper.readerFor(pojoClass).with(schema).readValues(inputStream);

        return iterator.readAll();
    }

    static void toCsv(Class pojoClass, List list, OutputStream outputStream) throws IOException{
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(pojoClass).withHeader().withColumnReordering(false);

        ObjectWriter writer = mapper.writerFor(pojoClass).with(schema);

        writer.writeValuesAsArray(outputStream).writeAll(list);

        outputStream.close();

    }
}
