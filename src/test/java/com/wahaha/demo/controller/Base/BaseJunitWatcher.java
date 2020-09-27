package com.wahaha.demo.controller.Base;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


public abstract class BaseJunitWatcher extends AbstractJUnit4SpringContextTests {


    @Rule
    public ReporterWatcher reporter = ReporterWatcher.getSingleton();

    @BeforeClass
    public static void onBefore() {
        ReporterWatcher.getSingleton().init("新门店");
    }

    @AfterClass
    public static void onAfter() {
        ReporterWatcher.getSingleton().finish();
    }


}
