package com.gyf.bookstore.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.gyf.bookstore.domain.Product;
import com.gyf.bookstore.utils.C3P0Utils;
import com.gyf.bookstore.utils.ManagerThreadLocal;
import com.mchange.v2.resourcepool.ResourcePool.Manager;

public class ProductDao{
	public long count(String category) throws SQLException{
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from products where 1=1";
		
		long count = 0;
		if(category != null && !"".equals(category)){
			sql+= " and  category = ?";
			count = (long) qr.query(sql, new ScalarHandler(1), category);
		}else{
			count = (long) qr.query(sql, new ScalarHandler(1));
		}
		
		return count;
	}
	public  List<Product> findBooks(int currentPage,int pageCount,String category) throws SQLException{
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		List<Object> prmts = new ArrayList<>();
		
		String sql = "select * from products where 1=1";
		
		if(category!=null){
			sql += " and category = ?";
			prmts.add(category);
		}
		
		sql += " limit ?,?";
		int start = (currentPage - 1) * pageCount;
		prmts.add(start);
		prmts.add(pageCount);
		
		return qr.query(sql, new BeanListHandler<Product>(Product.class), prmts.toArray());
	}
	
	/**
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Product findBookById(String id) throws SQLException{
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from products where id = ?";
		return qr.query(sql, new BeanHandler<Product>(Product.class),id);
	}

	/**
	 * @param id
	 * @param buynum
	 * @throws SQLException 
	 */
	public void updateProductNum(int id, int buynum) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "update products set pnum = pnum - ? where id = ?";
		QueryRunner qr = new QueryRunner();
		qr.update(ManagerThreadLocal.getConnection(), sql,buynum,id);
	}
	public List<Product> findBookByname(String bookname)throws SQLException{
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from products where name like ?";
		
		return qr.query(sql, new BeanListHandler<Product>(Product.class),"%"+bookname+"%");
	}
}
