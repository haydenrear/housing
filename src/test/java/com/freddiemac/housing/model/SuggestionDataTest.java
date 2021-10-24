package com.freddiemac.housing.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = {SuggestionTestData.class})
@ExtendWith(SpringExtension.class)
public class SuggestionDataTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testLoadSuggestionDataPostConstruct()
    {
        SuggestionTestData bean = applicationContext.getBean(SuggestionTestData.class);
        assertThat(bean.data.length).isNotEqualTo(0);
    }

}
