package com.loukou.order;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath*:order-service-appcontext.xml",
})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public abstract class AbstractTestObject {
	public static final ObjectMapper mapper = new ObjectMapper();
	
	public String object2String(Object object) {
		
		String str = "";
		try {
			str = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// ignore
		}
		return str;
	}
}
