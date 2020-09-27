package com.wahaha.demo.controller.Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ResourceCDN;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ReporterWatcher extends TestWatcher {

    private static final ReporterWatcher SINGLETON = new ReporterWatcher();

    private final ExtentReports mExtentReports = new ExtentReports();

    private final Map<String, ExtentTest> mUnits = new LinkedHashMap<>();
    private final Map<String, ExtentTest> mNodes = new LinkedHashMap<>();


    private final ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();

    private static final PrintStream OUT = System.out;
    private static final PrintStream ERR = System.err;


    private static final String TIME_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    private static final String TARGET_DEFAULT = "src/main/resources-public/static/test/index.html";
    private File mHtml;

    private ReporterWatcher() {


    }


    protected static ReporterWatcher getSingleton() {
        return SINGLETON;
    }

    protected void init() {
        init(null);
    }

    protected void init(final String applicationName) {
        final String title = (StringUtils.isEmpty(applicationName) ? "" : applicationName + "-") + "测试报告";
        init(title, TARGET_DEFAULT);
    }

    /**
     * @param title  显示在浏览器标签栏和显示在页面大标题
     * @param target html 输出文件位置
     */
    protected void init(final String title, final String target) {
        init(title, title, target);
    }

    /**
     * @param documentTitle 显示在浏览器标签栏
     * @param reportName    显示在页面大标题
     * @param target        html 输出文件位置
     */
    protected void init(final String documentTitle, final String reportName, final String target) {

        Assert.assertNotNull(documentTitle);
        Assert.assertNotNull(reportName);
        Assert.assertNotNull(target);

        final File project = new File("").getAbsoluteFile();
        final File html = new File(project, target);
        mHtml = html;

        if (!html.getParentFile().exists() && !html.getParentFile().mkdirs()) {
            throw new RuntimeException(new IOException());
        }
        if (html.exists() && !html.delete()) {
            throw new RuntimeException(new IOException());
        }


        final ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(html);
        htmlReporter.config().setEncoding(StandardCharsets.UTF_8.toString());
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setAutoCreateRelativePathMedia(true);
        htmlReporter.config().setDocumentTitle(documentTitle);
        htmlReporter.config().setReportName(reportName);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat(TIME_FORMAT);
        mExtentReports.attachReporter(htmlReporter);
        mExtentReports.setReportUsesManualConfiguration(true);


        mUnits.clear();
        mNodes.clear();

        System.setOut(new ConsoleStream(OUT, mBuffer));
        System.setErr(new ConsoleStream(ERR, mBuffer));

    }

    protected void finish() {
        mExtentReports.flush();
        try {
            final String html = FileUtils.readFileToString(mHtml);

            final String regex = ".*cdn\\.rawgit\\.com.*";

            FileUtils.write(mHtml, html.replaceAll(regex, ""));
        } catch (Exception e) {

        }
    }


    @Override
    protected void starting(Description description) {
        super.starting(description);
        mBuffer.reset();

        final String key = description.getClassName();

        final ExtentTest unit;
        if (!mUnits.containsKey(key)) {
            unit = mExtentReports.createTest(key);
            unit.getModel().setStartTime(new Date());
            mUnits.put(key, unit);
        } else {
            unit = mUnits.get(key);
        }
        final ExtentTest node = unit.createNode(description.getMethodName());
        node.getModel().setStartTime(new Date());
        mNodes.put(description.getMethodName(), node);

    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);

        final ExtentTest node = mNodes.get(description.getMethodName());

        final String details = mBuffer.toString();

        node.pass(MarkupHelper.createCodeBlock(details));

    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
        super.skipped(e, description);
        final ExtentTest node = mNodes.get(description.getMethodName());

        final String details = mBuffer.toString();

        node.skip(MarkupHelper.createCodeBlock(details));
    }


    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);

        final ExtentTest node = mNodes.get(description.getMethodName());

        final String details = mBuffer.toString();
        if (!StringUtils.isEmpty(details)) {
            node.fail(MarkupHelper.createCodeBlock(details));
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        node.fail(MarkupHelper.createCodeBlock(baos.toString()));
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);

        final ExtentTest node = mNodes.get(description.getMethodName());
        node.getModel().setEndTime(new Date());

        final ExtentTest unit = mUnits.get(description.getClassName());
        unit.getModel().setEndTime(new Date());

        mBuffer.reset();

    }

    private static class ConsoleStream extends PrintStream {

        private final ByteArrayOutputStream buffer;

        ConsoleStream( OutputStream out, ByteArrayOutputStream buffer) {
            super(out);
            this.buffer = buffer;
        }

        @Override
        public void write(int b) {
            super.write(b);
            buffer.write(b);
        }

        @Override
        public void write( byte[] buf, int off, int len) {
            super.write(buf, off, len);
            buffer.write(buf, off, len);
        }

    }
}
