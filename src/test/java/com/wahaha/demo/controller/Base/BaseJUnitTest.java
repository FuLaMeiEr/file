package com.wahaha.demo.controller.Base;

import com.wahaha.demo.DemoApplication;
import com.wahaha.demo.contents.HttpStatic;
import com.wahaha.demo.controller.utils.StringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.Map;

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
//public abstract class BaseJUnitTest extends AbstractTransactionalJUnit4SpringContextTests {
public abstract class BaseJUnitTest extends BaseJunitWatcher {


    private static final SharedHttpSessionConfigurer SHARED_HTTP_SESSION_CONFIGURER = new SharedHttpSessionConfigurer();
    private static final MockMvcConfigurer SHARED_TOKEN_CONFIGURER = new MockMvcConfigurer() {

        private String token;

        @Override
        public void afterConfigurerAdded(ConfigurableMockMvcBuilder<?> builder) {
            builder.alwaysDo((result) -> {
                final String token = result.getResponse().getHeader(HttpStatic.Header.AUTHORIZATION);
                if (!StringUtils.isEmpty(token)) {
                    this.token = token;
                }
            });
        }

        @Override
        public RequestPostProcessor beforeMockMvcCreated(ConfigurableMockMvcBuilder<?> builder,
                                                         WebApplicationContext context) {
            return (request) -> {
                if (!StringUtils.isEmpty(this.token)) {
                    request.addHeader(HttpStatic.Header.AUTHORIZATION, this.token);
                }
                return request;
            };
        }
    };

    private static MockMvc sMockMvc;

    /**
     * @see <a href="http://blog.csdn.net/dounine/article/details/72953463">集成测试参考连接</a>
     */
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private WebApplicationContext context;

    @Before()
    public void setup() {
        if (sMockMvc == null) {
            final Map<String, BaseFilter> map = context.getBeansOfType(BaseFilter.class, false, false);
            final Collection<BaseFilter> collection = map.values();
            sMockMvc = MockMvcBuilders.webAppContextSetup(context)
                    .apply(SHARED_HTTP_SESSION_CONFIGURER)
                    .apply(SHARED_TOKEN_CONFIGURER)
                    .addFilters(collection.toArray(new Filter[collection.size()]))
                    .build();  //初始化MockMvc对象
        }
    }


    public MockMvc getMockMvc() {
        return sMockMvc;
    }

    public TestRestTemplate getRestTemplate() {
        return testRestTemplate;
    }

}
