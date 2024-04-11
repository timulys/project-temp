
/**
 * 고객 상품 Repository
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.13 / asher.shin   / 신규
 */
package com.kep.portal.repository.product;

import com.kep.portal.model.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {

}
