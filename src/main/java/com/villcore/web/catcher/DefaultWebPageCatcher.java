package com.villcore.web.catcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class DefaultWebPageCatcher extends AbstractWebPageCatcher implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebPageCatcher.class);

    public DefaultWebPageCatcher(ExecutorService executor) {
        super(executor);
    }

    @Override
    protected void catchPageFinish() {

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
    public Page nextPage() {
        return null;
    }

    @Override
    public boolean needVerifyCode() {
        return false;
    }

    @Override
    public boolean needLogin(ResponseBundle responseBundle) {
        return false;
    }

    @Override
    public boolean needReply() {
        return false;
    }

    @Override
    public long replyInterval() {
        return 0;
    }

    @Override
    public boolean login(RequestBundle requestBundle) {
        return false;
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
    public ResponseBundle refresh(RequestBundle requestBundle) {
        return null;
    }

    @Override
    public ResponseBundle reply(RequestBundle requestBundle) {
        return null;
    }

    @Override
    public Map<ContentTypeEnum, List<Content>> parseContent(ResponseBundle responseBundle) {
        return null;
    }

    @Override
    protected void recordContents(Map<ContentTypeEnum, List<Content>> contents) {

    }

    @Override
    protected void statis(Map<ContentTypeEnum, List<Content>> contents) {

    }

    @Override
    protected RequestBundle itemRequestWithReply(Item item) {
        return null;
    }

    @Override
    protected RequestBundle itemRequest(Item item) {
        return null;
    }

    @Override
    protected String getVerifyCode() {
        return null;
    }

    @Override
    protected RequestBundle pageRequest(Page page) {
        return null;
    }

    @Override
    protected RequestBundle loginRequest() {
        return null;
    }

    //url
    //pageUrl template
    //loginUrl
}
