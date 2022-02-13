package com.example.udemywebbackend.brands;

import java.util.List;
import java.util.NoSuchElementException;

import com.example.udemywebbackend.Exception.BrandNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BrandsServiceImp implements BrandsService{

    @Autowired
    private BrandsRepository brandRepo;

    @Override
    public Brands saveBrand(Brands brands) {
        return brandRepo.save(brands);
    }

    @Override
    public List<Brands> getListBrand() {
        return (List<Brands>) brandRepo.findAll();
    }

    @Override
    public Brands getBrandByID(int id) throws BrandNotFoundException {
        try{
            Brands getBrand=brandRepo.findById(id).get();
            return getBrand;
        }
        catch(NoSuchElementException ex){
            throw new BrandNotFoundException("Can't find any brand with ID" +id);
        }
    
    }

    @Override
    public void updateBrand(Brands brands) {
        Brands brandUpdate=brandRepo.findById(brands.getId()).get();
        String logo=brandUpdate.getLogo();
        if(brands.getLogo() != null){
            brandUpdate.setLogo(brands.getLogo());
        }else{
            brandUpdate.setLogo(logo);
        }
        brandUpdate.setName(brands.getName());
        brandUpdate.setCategories(brands.getCategories());

        brandRepo.save(brandUpdate);
        
    }

    @Override
    public boolean checkNameBrand(String name,Integer id) {
        
        Brands brand=brandRepo.findByName(name);
        if(id == null || id ==0){
            if(brand != null){
                return false;
            }else return true;
        }else{
            if(brand !=null && brand.getId() !=id) return false;
            else return true;
        }
    }

    @Override
    public void deleteBrand(int id) throws BrandNotFoundException {
        Long countById=brandRepo.countById(id);

        if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any category with ID " + id);
		}
		
		brandRepo.deleteById(id);
    }

    @Override
    public Page<Brands> listBrandByPage(int pageNum, String sortDir, String sortField, String keyword) {
        Sort sort=Sort.by(sortField);

        sort=sortDir.equals("asc") ?sort.ascending()  : sort.descending();
        Pageable pageable= PageRequest.of(pageNum-1,USER_BY_PAGE,sort);


        if(keyword == null){
            return brandRepo.findAll(pageable);
        }else{
            return brandRepo.findBrandSearch(keyword,pageable);
        }
    }
    
}
