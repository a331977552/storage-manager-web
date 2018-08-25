package com.storage.service.imp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.entity.Category;
import com.storage.entity.Product;
import com.storage.entity.ProductExample;
import com.storage.entity.ProductExample.Criteria;
import com.storage.entity.Productimg;
import com.storage.entity.ProductimgExample;
import com.storage.entity.custom.CustomOrder;
import com.storage.entity.custom.CustomProduct;
import com.storage.entity.custom.CustomeProductName;
import com.storage.entity.custom.OrderWrap;
import com.storage.entity.custom.PageBean;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.CategoryMapper;
import com.storage.mapper.ProductMapper;
import com.storage.mapper.ProductimgMapper;
import com.storage.service.ProductService;
import com.storage.utils.JsonUtils;
import com.storage.utils.jedis.JedisClientPool;

@Service
@PropertySource("classpath:myapp.properties")
public class ProductServiceImp implements ProductService {
	private static final Integer PRODUCT_DELETE = 1;
	private static final Integer PRODUCT_NORNAL = 2;
	@Autowired
	ProductMapper productMapper;
	@Autowired
	ProductimgMapper productimgMapper;
	@Autowired
	CategoryMapper categoryMapper;
	@Value("${seaweeddfs.url}")
	String seaweeddfsUrl;

	/* @Value("${pageSize}") */
	Integer pageSize = 24;
	// @Value("${product_warning_quantity}")
	Integer product_warning_quantity = 10;
	NumberFormat format;

	@Autowired
	JedisClientPool jedisPool;
	ObjectMapper mapper = new ObjectMapper();

	public ProductServiceImp() {
		format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
	}

	@Override
	public StorageResult addProduct(Product product) {
		if (product == null)
			return StorageResult.failed("invalid parameter");

		product.setStatus(PRODUCT_NORNAL);
		this.productMapper.insert(product);
		return StorageResult.succeed();
	}

	@Override
	public StorageResult updateProduct(Product product) {
		if (product == null || product.getId() == null || product.getId() < 0)
			return StorageResult.failed("invalid parameter ");

		this.productMapper.updateByPrimaryKey(product);

		return StorageResult.succeed();
	}

	@Override
	public StorageResult deleteProductById(Integer id) {
		if (id == null || id < 0)
			return StorageResult.failed("invalid id");
		Product record=new Product();
		record.setStatus(PRODUCT_DELETE);
		record.setId(id);
		this.productMapper.updateByPrimaryKeySelective(record);
		return StorageResult.succeed();
	}

	@Override
	public StorageResult getProductByExample(Product product) {
		return this.getProductByExample(product, 0, this.pageSize);

	}

	@Override
	public StorageResult getProductById(Integer id) {
		if (id == null || id < 0)
			return StorageResult.failed("invalid id");

		CustomProduct customProduct = new CustomProduct();
		Product product = this.productMapper.selectByPrimaryKey(id);
		float sell = (float) product.getSellingprice() / 100;
		float buy = (float) product.getBuyingprice() / 100;
		product.setSellingprice_(Float.valueOf(format.format(sell)));
		product.setBuyingprice_(Float.valueOf(format.format(buy)));
		customProduct.setProduct(product);
		ProductimgExample example = new ProductimgExample();
		example.createCriteria().andProdcutidEqualTo(product.getId());
		List<Productimg> selectByExample = this.productimgMapper.selectByExample(example);
		for (Productimg productimg : selectByExample) {
			productimg.setUrl(seaweeddfsUrl + ":8080/" + productimg.getUrl());
		}
		customProduct.setList(selectByExample);
		return StorageResult.succeed(customProduct);
	}

	@Override
	public StorageResult updateProductSelective(Product product) {
		if (product == null || product.getId() == null || product.getId() < 0)
			return StorageResult.failed("invalid parameter ");

		this.productMapper.updateByPrimaryKeySelective(product);
		return StorageResult.succeed();

	}

	@Override
	public StorageResult count(ProductExample example) {
		long count = this.productMapper.countByExample(example);
		return StorageResult.succeed(count);
	}

	@Override
	public StorageResult addProduct(CustomProduct cusproduct) {
		if (cusproduct == null || cusproduct.getProduct() == null || cusproduct.getList() == null)
			return StorageResult.failed("empty products or imgs");
		Product product = cusproduct.getProduct();
		String barcode = cusproduct.getProduct().getBarcode().trim();
		if (StringUtils.isEmpty(barcode))
			return StorageResult.failed("empty barcode");

		if (StringUtils.isEmpty(product.getName()))
			return StorageResult.failed("product name is empty");

		if (product.getBuyingprice_() == null)
			return StorageResult.failed("buying price is empty");
		else {
			product.setBuyingprice((int) (product.getBuyingprice_() * 100));
		}

		if (product.getSellingprice_() == null)
			return StorageResult.failed("selling price is empty");
		else {
			product.setSellingprice((int) (product.getSellingprice_() * 100));
		}
		if (product.getQuantity_() == null) {
			product.setQuantity(0);
		} else {
			Float quantity_ = product.getQuantity_();
			product.setQuantity(quantity_.intValue());
		}

		if (product.getVat() == null) {
			product.setVat(0.0f);
		}

		ProductExample example = new ProductExample();
		com.storage.entity.ProductExample.Criteria criteria = example.createCriteria();
		criteria.andBarcodeEqualTo(barcode);
		List<Product> selectByExample = this.productMapper.selectByExample(example);
		if (selectByExample != null && selectByExample.size() > 0)
			return StorageResult.failed("barcode: " + barcode + " already exists");

		product.setCreatedtime(new Date());
		product.setUpdatetime(new Date());
		product.setStatus(PRODUCT_NORNAL);
		this.productMapper.insert(product);

		List<Productimg> list = cusproduct.getList();
		for (Productimg productimg : list) {
			productimg.setProdcutid(product.getId());
			this.productimgMapper.insert(productimg);
		}
		return StorageResult.succeed();
	}

	@Override
	public StorageResult getProductByExample(Product product, Integer currentPage, Integer pageSize) {

		ProductExample example = new ProductExample();

		Criteria criteria = example.createCriteria();
		if (product.getId() != null) {
			criteria.andIdEqualTo(product.getId());
		}

		if (!com.storage.utils.StringUtils.isEmpty(product.getName())) {
			criteria.andNameLike("%" + product.getName() + "%");

		}

		if (product.getQuantity() != null) {
			criteria.andQuantityEqualTo(product.getQuantity());
		}

		if (product.getBuyingprice() != null) {
			criteria.andBuyingpriceEqualTo(product.getBuyingprice());
		}

		if (product.getSellingprice() != null) {
			criteria.andSellingpriceEqualTo(product.getSellingprice());
		}

		if (product.getCreatedtime() != null) {
			criteria.andCreatedtimeEqualTo(product.getCreatedtime());
		}

		if (!StringUtils.isEmpty(product.getSupplier())) {
			criteria.andSupplierEqualTo(product.getSupplier());
		}

		if (product.getVat() != null) {
			criteria.andVatEqualTo(product.getVat());
		}

		if (product.getUpdatetime() != null) {
			criteria.andUpdatetimeEqualTo(product.getUpdatetime());
		}
		if (product.getCategory() != null && product.getCategory() != -1) {
			criteria.andCategoryEqualTo(product.getCategory());
		}

		if (pageSize == null) {
			pageSize = this.pageSize;
		}
		PageBean<Product> bean = new PageBean<>(currentPage, ((Long) this.count(example).getResult()).intValue(),
				pageSize);
		Integer startCount = bean.getStartCount();
		pageSize = bean.getPageSize();
		example.setOrderByClause("updateTime desc limit " + startCount + "," + pageSize);
		criteria.andStatusNotEqualTo(PRODUCT_DELETE);
		List<Product> results = this.productMapper.selectByExample(example);
		if (results.size() == 0 && !com.storage.utils.StringUtils.isEmpty(product.getName())) {
			example = new ProductExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andBarcodeEqualTo(product.getName());
			createCriteria.andStatusNotEqualTo(1);
			results = this.productMapper.selectByExample(example);
		}
		Iterator<Product> iterator = results.iterator();
		while (iterator.hasNext()) {
			Product product2 = iterator.next();
				product2.setProduct_warning_quantity(this.product_warning_quantity);
				ProductimgExample example2 = new ProductimgExample();
				com.storage.entity.ProductimgExample.Criteria createCriteria = example2.createCriteria();
				createCriteria.andProdcutidEqualTo(product2.getId());
				List<Productimg> selectByExample = this.productimgMapper.selectByExample(example2);
				if (selectByExample.size() > 0) {
					product2.setImgUrl(seaweeddfsUrl + ":8080/" + selectByExample.get(0).getUrl());
				}

		}

		bean.setBeans(results);

		return StorageResult.succeed(bean);

	}

	@Override
	public StorageResult getProductNamesByCategory(Integer categoryId) {

		String string = jedisPool.get("productNameByCategory" + categoryId);
		if (string != null) {
			List<CustomeProductName> jsonToList = JsonUtils.jsonToList(string, CustomeProductName.class);
			return StorageResult.succeed(jsonToList);
		}

		ProductExample example = new ProductExample();
		if (categoryId != null) {
			Category selectByPrimaryKey = this.categoryMapper.selectByPrimaryKey(categoryId);
			if (selectByPrimaryKey != null) {
				Criteria createCriteria = example.createCriteria();
				createCriteria.andCategoryEqualTo(categoryId);
			}
		}
		List<com.storage.entity.custom.CustomeProductName> selectNameByExample = this.productMapper
				.selectNameByExample(example);

		try {
			String writeValueAsString = mapper.writeValueAsString(selectNameByExample);
			System.out.println(writeValueAsString);
			jedisPool.set("productNameByCategory" + categoryId, writeValueAsString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return StorageResult.succeed(selectNameByExample);
	}

	@Override
	public StorageResult getProductByExample(List<CustomOrder> jsonToList) {
		List<CustomProduct> list = new ArrayList<>();
		OrderWrap orderWrap = new OrderWrap();
		double totalPrice = 0;
		NumberFormat numberInstance = new DecimalFormat("0.00");

		numberInstance.setMaximumFractionDigits(2);
		if (jsonToList == null)
			return StorageResult.failed("Repeat submission");
		for (CustomOrder customOrder : jsonToList) {
			Integer id = customOrder.getId();
			if (id != null && id > 0) {
				Product product = this.productMapper.selectByPrimaryKey(id);
				CustomProduct customProduct = new CustomProduct();
				customProduct.setProduct(product);
				customProduct.setQty(customOrder.getQty());
				Integer sellingprice = product.getSellingprice();
				double subtotal = (double) (sellingprice * customOrder.getQty()) / 100;
				Double valueOf = Double.valueOf(numberInstance.format(subtotal));
				totalPrice += valueOf;
				customProduct.setSubtotal(valueOf);
				list.add(customProduct);
			}
		}
		orderWrap.setList(list);
		orderWrap.setTotalPrice(Double.valueOf(numberInstance.format(totalPrice)));
		return StorageResult.succeed(orderWrap);
	}

	@Override
	public StorageResult getProductByBarCode(String barcode) {

		ProductExample example = new ProductExample();
		example.createCriteria().andBarcodeEqualTo(barcode);
		List<Product> selectByExample = this.productMapper.selectByExample(example);
		if (selectByExample.size() == 0)
			return StorageResult.failed("didnot find the item");
		return StorageResult.succeed(selectByExample.get(0));
	}

	@Override
	public StorageResult getStockReminder() {

		ProductExample example = new ProductExample();
		Criteria criteria = example.createCriteria();
		criteria.andQuantityLessThanOrEqualTo(this.product_warning_quantity);

		List<Product> selectByExample = this.productMapper.selectByExample(example);

		return StorageResult.succeed(selectByExample);
	}

	@Override
	public StorageResult updateProduct(CustomProduct cusproduct) {

		if (cusproduct == null || cusproduct.getProduct() == null)
			return StorageResult.failed("product required");
		Product product = cusproduct.getProduct();
		product.setBuyingprice((int) (product.getBuyingprice_() * 100));
		product.setSellingprice((int) (product.getSellingprice_() * 100));

		if (product.getQuantity_() == null) {
			product.setQuantity(0);
		} else {
			Float quantity_ = product.getQuantity_();
			product.setQuantity(quantity_.intValue());
		}

		if (product.getVat() == null) {
			product.setVat(0.0f);
		}

		product.setUpdatetime(new Date());
		this.productMapper.updateByPrimaryKeySelective(product);
		List<Productimg> list = cusproduct.getList();
		if (list != null) {
			ProductimgExample example = new ProductimgExample();
			example.createCriteria().andProdcutidEqualTo(product.getId());
			this.productimgMapper.deleteByExample(example);
			for (Productimg productimg : list) {
				productimg.setProdcutid(product.getId());
				this.productimgMapper.insert(productimg);
			}
		}
		return StorageResult.succeed();
	}

}
