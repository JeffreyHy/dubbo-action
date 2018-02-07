package com.huang.test;

import com.huang.context.ConsumerApplication;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by JeffreyHy on 2018/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerApplication.class)
@SpringBootConfiguration
@FixMethodOrder
public class AbstractTestCase extends TestCase {
}
