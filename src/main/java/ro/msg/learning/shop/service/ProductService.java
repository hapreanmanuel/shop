package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.tables.Product;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){this.productRepository=productRepository;}

    public Product getProduct(int productId){return productRepository.findOne(productId);}

    public List<Product> getAllProducts(){return productRepository.findAll();}

    public void addProduct(Product product){productRepository.save(product);}

}
