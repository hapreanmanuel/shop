package ro.msg.learning.shop.utility.converter;

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

    public CsvHttpMessageConverter() {
        super(new MediaType("text","csv"));
    }

    @Override
    protected void writeInternal(List list, Type type, HttpOutputMessage outputMessage) {
        Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
        try{
            CsvConverterUtil.toCsv(Class.forName(actualType.getTypeName()), list,outputMessage.getBody());

        } catch (Exception e){
            throw new HttpMessageNotWritableException("Unable to write CSV",e);
        }
    }

    @Override
    protected List readInternal(Class<? extends List> clazz, HttpInputMessage inputMessage) throws IOException {
        try{
            return CsvConverterUtil.fromCsv(clazz, inputMessage.getBody());
        } catch (Exception e){
            throw new HttpMessageNotReadableException("Unable to read CSV", e);
        }
    }

    @Override
    public List read(Type type, Class contextClass, HttpInputMessage inputMessage) {
        Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
        try{
            return CsvConverterUtil.fromCsv(Class.forName(actualType.getTypeName()),inputMessage.getBody());
        } catch (Exception e){
            throw new HttpMessageNotReadableException("Unable to read CSV", e);
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
