package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.tables.Supplier;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {this.supplierRepository=supplierRepository;}

    public Supplier getSupplier(int supplierId){return supplierRepository.findOne(supplierId);}

    public List<Supplier> getAllSuppliers() { return supplierRepository.findAll(); }

    public void addSupplier(Supplier supplier) { supplierRepository.save(supplier);}

    public void updateSupplier(Supplier supplier) { supplierRepository.save(supplier);}

    public void deleteSupplier(Supplier supplier) { supplierRepository.delete(supplier);}
}
