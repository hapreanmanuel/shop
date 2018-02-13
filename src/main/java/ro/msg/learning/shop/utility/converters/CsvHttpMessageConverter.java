package ro.msg.learning.shop.utility.converters;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class CsvHttpMessageConverter extends AbstractGenericHttpMessageConverter<List>{

    private final CsvConverterUtil converter = new CsvConverterUtil();

    public CsvHttpMessageConverter() {
        super(new MediaType("text","csv"));
    }


    @Override
    protected void writeInternal(List list, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
        try{
            converter.toCsv(Class.forName(actualType.getTypeName()), list,outputMessage.getBody());

        } catch (Exception e){
            throw new RuntimeException("Unable to convert to CSV", e);
        }
    }

    @Override
    protected List readInternal(Class<? extends List> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return converter.fromCsv(clazz, inputMessage.getBody());
    }

    @Override
    public List read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
        try{
            return converter.fromCsv(Class.forName(actualType.getTypeName()),inputMessage.getBody());

        } catch (Exception e){
            throw new RuntimeException("Unable to convert to CSV", e);
        }
    }


    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        boolean isList = false;
        if (type instanceof ParameterizedType) {
            isList = ((ParameterizedType) type).getRawType().getTypeName().equalsIgnoreCase(List.class.getName());
        }

        return isList && super.canWrite(type, contextClass, mediaType);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        boolean isList = false;
        if (type instanceof ParameterizedType) {
            isList = ((ParameterizedType) type).getRawType().getTypeName().equalsIgnoreCase(List.class.getName());
        }

        return isList && super.canWrite(type, clazz, mediaType);
    }

}
