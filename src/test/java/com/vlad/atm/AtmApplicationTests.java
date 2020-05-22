package com.vlad.atm;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
class AtmApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(AtmApplicationTests.class);
	@Autowired
	private BillUtil billUtil;
	@Test
	void contextLoads() {
	}

	@Test
	void billUtil_splits_correctly() throws Exception {
		int amount = ((int) Math.random() * 100 + 50);
		logger.debug("splitting" + amount);
		assertThat(billUtil.split(amount).orElseThrow(() -> new Exception()).stream().reduce(0,Integer::sum))
				.isEqualTo(amount);
	}

	@Test
	void billUtil_will_not_split_amount_too_low(){
		int amount = 15; // <50
		assertThat(billUtil.split(amount)).isNotPresent();
	}

	@Test
	void billUtil_will_not_check_fake_bills(){
		List<Integer> bills = Arrays.asList(new Integer[]{1, 2, 5, 10, 20, 100, 7});
		Collections.shuffle(bills);
		assertThat(billUtil.checkBills(bills)).isEqualTo(false);
	}

}
