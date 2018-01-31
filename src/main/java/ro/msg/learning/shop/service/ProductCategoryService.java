package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.tables.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;

import java.util.List;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository){this.productCategoryRepository=productCategoryRepository;}

    public ProductCategory getProductCategory(int categoryId){return productCategoryRepository.findOne(categoryId);}

    public List<ProductCategory> getAllProductCategories() {return productCategoryRepository.findAll();}

    public void addProductCategory(ProductCategory productCategory){productCategoryRepository.save(productCategory);}

    public void updateProductCategory(ProductCategory productCategory){productCategoryRepository.save(productCategory);}

    public void deleteProductCategory(ProductCategory productCategory){productCategoryRepository.delete(productCategory);}
}
