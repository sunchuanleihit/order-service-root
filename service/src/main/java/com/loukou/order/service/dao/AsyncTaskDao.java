package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.AsyncTask;

public interface AsyncTaskDao extends CrudRepository<AsyncTask,Integer>{
	public List<AsyncTask> findByStatus(int status);
}
