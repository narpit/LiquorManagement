package com.liquormanagement.pcs.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.liquormanagement.pcs.exception.RecordNotFoundException;
import com.liquormanagement.pcs.model.Inventory;
import com.liquormanagement.pcs.model.Product;
import com.liquormanagement.pcs.model.Review;
import com.liquormanagement.pcs.service.ProductCatalogueService;
import com.liquormanagement.pcs.service.ProductCatalogueServiceImpl;

@RestController
public class ProductController {
	
	@Autowired
	private ProductCatalogueServiceImpl productCatalogueServiceimpl; 
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	//Get All products
		@SuppressWarnings("unchecked")
		@RequestMapping(value="/products")	
		public ResponseEntity<List<Product>> getAllProducts(){
				List<Product> products = null;
				try {
					products = productCatalogueServiceimpl.getAllProducts();
				} catch (RecordNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				if(products.isEmpty()) {
//					return ResponseEntity.ok(products);
//				}else {
//					return ResponseEntity.notFound().build();
//				}
			//	return new ResponseEntity<List<Product>>(products,new HttpHeaders(),HttpStatus.OK);
				return new ResponseEntity<>(products, HttpStatus.OK);
			}
	
	//Get Product by ID: An API to retrieve detailed information about a specific product based on its unique identifier.
	@RequestMapping(value="/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) throws RecordNotFoundException{	
		Product product =  productCatalogueServiceimpl.getProductById(id);
		if(product!=null) {
			return ResponseEntity.ok(product);
		}else {
			return ResponseEntity.notFound().build();
		}
		//return new ResponseEntity<EmployeeEntity>(product,new HttpHeaders(),HttpStatus.Ok);
	}
	//Get Products by Category: An API to fetch a list of products belonging to a particular category or type.
	@RequestMapping(value="/products/category/{category}")
	public List<Product> getProductByCategory(@PathVariable String category) throws RecordNotFoundException{
		return productCatalogueServiceimpl.getProductByCategory(category);
	}
	
	//Search Products: An API that allows users to search for products based on keywords, filters, or specific criteria.
	
	//Add Product: An API to add a new product to the catalog. This API would typically accept product details as input and generate a unique identifier for the new product.
	@RequestMapping(value="/products",method=RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@RequestBody Product product){
		Product addedProduct =  productCatalogueServiceimpl.addProduct(product);
		return ResponseEntity.created(URI.create("/products"+addedProduct.getId())).body(addedProduct);
	}
	//Update Product: An API to update the details of an existing product in the catalog. This API would typically accept the product ID and the updated information as input.
	@RequestMapping(value="/products",method=RequestMethod.PUT)
	public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody Product product) throws RecordNotFoundException{
		Product updatedProduct = productCatalogueServiceimpl.updateProduct(product);
		if(updatedProduct!=null) {
			return ResponseEntity.ok(updatedProduct);
		}else {
			return ResponseEntity.notFound().build();
		}
		
		//return new ResponseEntity<Product>(updatedProduct,new HttpHeaders(), HttpStatus.OK);
	}
	//Delete Product: An API to remove a product from the catalog based on its unique identifier.
	@RequestMapping(value="/products/{productId}",method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws RecordNotFoundException{
		productCatalogueServiceimpl.deleteProductById(productId);
		  return ResponseEntity.ok("Product deleted successfully");	
		  //return HttpStatus.FORBIDDEN;
	}
	//Get Product Reviews: An API to retrieve the reviews and ratings associated with a particular product.
	public List<Review> getProductReviews(@PathVariable Long productId){
		return productCatalogueServiceimpl.getProductReviews(productId);
	}
	//Add Product Review: An API to allow users to add reviews and ratings for a specific product.

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Review> addProductReview(@PathVariable String productId, @RequestBody Review review) {
        Review addedReview = productCatalogueServiceimpl.addProductReview(productId, review);
        return ResponseEntity.created(URI.create("/products/" + productId + "/reviews/" + addedReview.getId()))
                .body(addedReview);
    }

    
	
	//Get Product Recommendations: An API that provides recommendations for related or similar products based on the current product or user preferences.
	
    
    //Get Product Inventory: An API to retrieve the current stock levels and availability of a particular product.
    @GetMapping("/{productId}/inventory")
    public Inventory getProductInventory(@PathVariable String productId) {
        return productCatalogueServiceimpl.getProductInventory(productId);
    }
}
