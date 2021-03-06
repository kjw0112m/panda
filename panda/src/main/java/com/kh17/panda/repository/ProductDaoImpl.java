package com.kh17.panda.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh17.panda.entity.ProductDto;

@Repository
public class ProductDaoImpl implements ProductDao{

	@Autowired
	SqlSession sqlSession;
	
	@Override
	public int getSequenceNumber() {
		return sqlSession.selectOne("product.seq");
	}

	@Override
	public void insert(ProductDto productDto) {
		sqlSession.insert("product.regist", productDto);
	}

	@Override
	public ProductDto get(int id) {
		return sqlSession.selectOne("product.get", id);
	}

	@Override
	public void edit(ProductDto productDto) {
		sqlSession.update("product.edit", productDto);
	}

	@Override
	public void delete(int id) {
		sqlSession.delete("product.delete", id);
	}

	@Override
	public List<ProductDto> list(String seller_id) {
		return sqlSession.selectList("product.list", seller_id);
	}


	
	
	
}
