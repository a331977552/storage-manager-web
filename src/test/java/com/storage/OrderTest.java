package com.storage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.storage.entity.custom.OrderStatis;
import com.storage.entity.custom.OrderWrap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class OrderTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url;
	@Before
	public void contexLoads() throws Exception {
		url="http://localhost:" + port + "/stOrder/getInfoFromOrder/150";
		
	}

	@Test
	public void getOrderWrap() throws Exception {

	
		try {
			ResponseEntity<OrderWrap> forEntity = this.restTemplate.getForEntity(url, OrderWrap.class);
			System.out.println(forEntity.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		assertThat(this.restTemplate.put(url, objectToJson));
	}
	@Test
	public void getOrderStatis() throws Exception {
		
		url="http://localhost:" + port + "/stOrder/getStatistics";
		try {
			ResponseEntity<OrderStatis> forEntity = this.restTemplate.getForEntity(url, OrderStatis.class);
			System.out.println(forEntity.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		assertThat(this.restTemplate.put(url, objectToJson));
	}
	
	


}
