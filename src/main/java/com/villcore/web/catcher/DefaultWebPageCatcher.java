package com.villcore.web.catcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

public class DefaultWebPageCatcher extends AbstractWebPageCatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebPageCatcher.class);

    private ExecutorService executor;
    private ScheduledExecutorService scheduledExecutor;

    public DefaultWebPageCatcher(ExecutorService executor, ScheduledExecutorService scheduledExecutor) {
        super(executor, scheduledExecutor);
    }

    private String getPageUrl(long pageNum) {
        return String.format(pageUrlTemplate, pageNum);
    }

    public void stop() {
        stateEnum = CatcherStateEnum.STOP;
        LOGGER.info("stop.");
    }

    public void pause() {
        stateEnum = CatcherStateEnum.PAUSE;
        LOGGER.info("pause.");
    }

    public void resume() {
        stateEnum = CatcherStateEnum.NEW;
        LOGGER.info("resume.");
    }

    @Override
    public void run() {
        process();
    }

    @Override
    protected boolean shouldTriggerNextPage() {
        return shouldTriggerNextPage;
    }

    @Override
    protected void triggerNextPage() {
        executor.submit(this);
    }

    @Override
    protected void catchPageFinish() {
        LOGGER.info("catch finish.");
    }

    @Override
    public Page nextPage() {
        String pageUrl = getPageUrl(pageNum++);
        Page page = new Page();
        page.setPageUrl(pageUrl);
        return page;
    }

    @Override
    public boolean needVerifyCode() {
        return needVerifyCode;
    }

    @Override
    public ResponseBundle requestVerifyCode(RequestBundle requestBundle) {
        return null;
    }

    @Override
    public boolean needLogin(ResponseBundle responseBundle) {
        return needLogin;
    }

    @Override
    public boolean needReply() {
        return needReply;
    }

    @Override
    public long replyInterval() {
        return replyInterval;
    }

    @Override
    public boolean login(RequestBundle requestBundle) {
        //return checkLoginState();
        return false;
    }

    private boolean checkLoginState() {
        return true;
    }

    @Override
    public ResponseBundle request(RequestBundle requestBundle) {
        return null;
    }

    @Override
    public List<Item> parseItems(ResponseBundle responseBundle) {
        return null;
    }

    @Override
    public ResponseBundle requestPage(RequestBundle requestBundle) {
        return null;
    }

    @Override
    public ResponseBundle requestItem(RequestBundle requestBundle) {
        return null;
    }


    @Override
    public boolean reply(RequestBundle requestBundle) {
        return true;
    }

    @Override
    public Map<ContentTypeEnum, List<Content>> parseContent(ResponseBundle responseBundle) {
        return parseContent(responseBundle.toHtml());
    }

    @Override
    protected void recordContents(Map<ContentTypeEnum, List<Content>> contents) {

    }

    @Override
    protected void statis(Map<ContentTypeEnum, List<Content>> contents) {
    }

    @Override
    protected RequestBundle itemRequestWithReply(Item item) {
        RequestBundle itemReplyRequest = new RequestBundle();
        itemReplyRequest.setRequestUrl(getItemReplyUrl(item.getItemUrl()));
        itemReplyRequest.setHttpTypeEnum(itemReplyHttpType);
        itemReplyRequest.setHeaders(createItemReplyHeaders());
        itemReplyRequest.setParams(createItemReplyParams());
        return itemReplyRequest;
    }

    @Override
    protected RequestBundle itemRequest(Item item) {
        RequestBundle itemRequest = new RequestBundle();
        itemRequest.setRequestUrl(item.getItemUrl());
        itemRequest.setHttpTypeEnum(itemHttpType);
        itemRequest.setHeaders(createItemHeaders());
        itemRequest.setParams(createItemParams());
        return itemRequest;
    }

    @Override
    protected String getVerifyCode() {
        int waitRetry = 0;
        synchronized (verifyCodeLock) {
            while (verifyCode.isEmpty() && waitRetry++ < 4) {
                try {
                    verifyCode.wait(30 * 1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        return verifyCode;
    }

    protected void setVerifyCode(String verifyCode) {
        synchronized (verifyCodeLock) {
            this.verifyCode = verifyCode;
            this.verifyCode.notifyAll();
        }
    }

    @Override
    protected RequestBundle pageRequest(Page page) {
        RequestBundle pageRequest = new RequestBundle();
        pageRequest.setRequestUrl(getPageUrl(super.pageNum));
        pageRequest.setHttpTypeEnum(pageHttpType);
        pageRequest.setHeaders(createLoginHeaders());
        pageRequest.setParams(createLoginParams());
        return pageRequest;
    }

    @Override
    protected RequestBundle loginRequest() {
        RequestBundle loginRequest = new RequestBundle();
        loginRequest.setRequestUrl(loginUrl);
        loginRequest.setHttpTypeEnum(loginHttpType);
        loginRequest.setHeaders(createLoginHeaders());
        loginRequest.setParams(createLoginParams());
        return loginRequest;
    }

    private Map<String, String> commonHeaders = new HashMap<>();
    //login
    //verify
    //item
    //reply

    private boolean shouldTriggerNextPage;
    private String pageUrlTemplate;

    private boolean needVerifyCode;
    private final Object verifyCodeLock = new Object();
    private String verifyCode = "";

    private boolean needLogin;
    private String loginUrl;
    private HttpTypeEnum loginHttpType;
    private Map<String, String> loginHeaders;
    private Map<String, String> loginParams;
    private String needLoginPattern;
    private Pattern pattern;

    private HttpTypeEnum pageHttpType;
    private Map<String, String> pageHeaders;
    private Map<String, String> pageParams;

    private HttpTypeEnum itemHttpType;
    private HttpTypeEnum itemReplyHttpType;
    private boolean needReply;
    private long replyInterval;

    public Map<String, String> createLoginHeaders() {
        return Collections.emptyMap();
    }

    public Map<String, String> createLoginParams() {
        return Collections.emptyMap();
    }

    public Map<String, String> createPageHeaders() {
        return Collections.emptyMap();
    }

    public Map<String, String> createPageParams() {
        return Collections.emptyMap();
    }

    public Map<String, String> createVerifyHeaders() {
        return Collections.emptyMap();
    }

    public Map<String, String> createVerifyParams() {
        return Collections.emptyMap();
    }

    public Map<String, String> createItemHeaders() {
        return Collections.emptyMap();
    }

    public Map<String, String> createItemParams() {
        return Collections.emptyMap();
    }

    public String getItemReplyUrl(String itemUrl) {
        return null;
    }

    public Map<String, String> createItemReplyHeaders() {
        return Collections.emptyMap();
    }

    public Map<String, String> createItemReplyParams() {
        return Collections.emptyMap();
    }

    public Map<ContentTypeEnum, List<Content>> parseContent(String html) {
        return Collections.emptyMap();
    }
}
