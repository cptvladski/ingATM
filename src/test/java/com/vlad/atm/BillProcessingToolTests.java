package com.vlad.atm;

import com.vlad.atm.util.BillProcessingTool;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class BillProcessingToolTests {

	private final Logger logger = LoggerFactory.getLogger(BillProcessingToolTests.class);
	@Autowired
	private BillProcessingTool billProcessingTool;

	@Test
	void billUtil_splits_correctly() throws Exception {
		int amount = (int)(Math.random() * 100 + 50);
		logger.debug("splitting" + amount);
		assertThat(billProcessingTool.split(amount)
				.orElseThrow(() -> new Exception())
				.stream()
				.reduce(0,Integer::sum))
				.isEqualTo(amount);
	}

	@Test
	void billUtil_will_not_split_amount_too_low(){
		int amount = 15; // <50
		assertThat(billProcessingTool.split(amount)).isNotPresent();
	}

	@Test
	void billUtil_will_not_check_fake_bills(){
		List<Integer> bills = Arrays.asList(new Integer[]{1, 2, 5, 10, 20, 100, 7});
		Collections.shuffle(bills);
		assertThat(billProcessingTool.checkBills(bills)).isEqualTo(Optional.empty());
	}

}
