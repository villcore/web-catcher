package com.villcore.web.catcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class AbstractWebPageCatcher implements WebPageCatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebPageCatcher.class);

    private static final long SESSION_EXPIRED_TIME_MS = TimeUnit.MILLISECONDS.convert(5L, TimeUnit.MINUTES);

    static enum CatcherStateEnum {
        NEW, NEED_LOGIN, LOGIN_SUCESS, LOGIN_FAILED, NORMAL, EXPIRED, PAUSE, STOP, FINISH
    }

    //stat, metrics
    private final long startup = System.currentTimeMillis();
    private long lastCatchTime;
    private long totalRequestPages;
    private long totalRequestItems;
    private Map<ContentTypeEnum, Long> contentTotal;


    protected volatile CatcherStateEnum stateEnum = CatcherStateEnum.NEW;
    protected long pageNum = 1;
    protected volatile boolean finish;
    protected int emptyPageCount;

    protected ExecutorService executor;
    protected ScheduledExecutorService scheduledExecutor;

    public AbstractWebPageCatcher(ExecutorService executor, ScheduledExecutorService scheduledExecutor) {
        this.executor = executor;
        this.scheduledExecutor = scheduledExecutor;
    }

    /** invoke multitmes */
    protected void process() {
        if (stateEnum == CatcherStateEnum.PAUSE) {
            LOGGER.info("last catch pause. will exit!");
            return;
        }

        if (stateEnum == CatcherStateEnum.FINISH) {
            LOGGER.info("last catch finish. will exit!");
            return;
        }

        if (stateEnum == CatcherStateEnum.STOP) {
            LOGGER.info("last catch stop. will exit!");
            return;
        }

        if (stateEnum == CatcherStateEnum.LOGIN_FAILED) {
            LOGGER.info("last catch login failed. will exit!");
            return;
        }

        lastCatchTime = System.currentTimeMillis();
        int loginRetry = 0;
        Page page = nextPage();

        ResponseBundle pageResp = null;

        Outer:
        while (true) {
            pageResp = requestPage(pageRequest(page));
            if (stateEnum == CatcherStateEnum.NEW
                    || stateEnum == CatcherStateEnum.EXPIRED
                    || stateEnum == CatcherStateEnum.LOGIN_FAILED) {
                while (needLogin(pageResp) && loginRetry++ < 4) {
                    if (needVerifyCode()) {
                        LOGGER.info("need verifycode wait it.");
                        verifyCode = getVerifyCode();
                        LOGGER.info("get verifycode = {}.", verifyCode);
                    }
                    if (login(loginRequest())) {
                        stateEnum = CatcherStateEnum.LOGIN_SUCESS;
                        LOGGER.info("login success.");
                        continue Outer;
                    } else {
                        stateEnum = CatcherStateEnum.LOGIN_FAILED;
                        LOGGER.info("login failed.");
                        continue Outer;
                    }
                }

                if (loginRetry >= 4) {
                    stateEnum = CatcherStateEnum.LOGIN_FAILED;
                    LOGGER.info("login failed.");
                    return;
                } else {
                    stateEnum = CatcherStateEnum.LOGIN_SUCESS;
                    LOGGER.info("no need login.");
                    break Outer;
                }
            }

            if (stateEnum == CatcherStateEnum.LOGIN_SUCESS) {
                break Outer;
            }
        }

        if (pageResp == null) {
            LOGGER.warn("page resp is null, page = [{}].", page);
        }

        List<Item> itemList = parseItems(pageResp);
        if (itemList.isEmpty()) {
            emptyPage++;
        } else {
            resetEmptyPage();
        }
        if (emptyPage > 5) {
            stateEnum = CatcherStateEnum.FINISH;
            return;
        }

        final AtomicInteger countDown = new AtomicInteger(itemList.size());

        for (Item item : itemList) {
            final RequestBundle itemRequest = needReply() ? itemRequest(item) : itemRequestWithReply(item);
            scheduledExecutor.schedule(() -> {
                requestItemTask(itemRequest, countDown);
            }, replyInterval(),TimeUnit.MILLISECONDS);
        }
        if (System.currentTimeMillis() - lastCatch > SESSION_EXPIRED_TIME_MS) {
            stateEnum = CatcherStateEnum.EXPIRED;
        }
    }

    private void requestItemTask(RequestBundle itemRequest, AtomicInteger countDown) {
        CompletableFuture<ResponseBundle> completeResp = CompletableFuture.supplyAsync(()-> {
            return request(itemRequest);
        }, executor);
        completeResp.thenAccept(new Consumer<ResponseBundle>() {
            @Override
            public void accept(ResponseBundle responseBundle) {
                Map<ContentTypeEnum, List<Content>> contents = parseContent(responseBundle);
                recordContents(contents);
                statis(contents);
                countDown.decrementAndGet();

                if (countDown.get() == 0) {
                    catchPageFinish();
                    if (shouldTriggerNextPage()) {
                        triggerNextPage();
                    }
                }
            }
        });
    }

    protected abstract boolean shouldTriggerNextPage();

    protected abstract void triggerNextPage();

    protected abstract void catchPageFinish();

    private int resetEmptyPage() {
        emptyPage = 0;
        return emptyPage;
    };

    protected abstract void recordContents(Map<ContentTypeEnum, List<Content>> contents);

    protected abstract void statis(Map<ContentTypeEnum,List<Content>> contents);

    protected abstract RequestBundle itemRequestWithReply(Item item);

    protected abstract RequestBundle itemRequest(Item item);

    /**
     * wait util get input verify code.
     *
     * @return
     */
    protected abstract String getVerifyCode();

    protected abstract RequestBundle pageRequest(Page page);

    protected abstract RequestBundle loginRequest();

    protected abstract long replyInterval();

    public long getStartup() {
        return startup;
    }

    public long getLastCatch() {
        return lastCatch;
    }

    public void setLastCatch(long lastCatch) {
        this.lastCatch = lastCatch;
    }

    public long getRequestPages() {
        return requestPages;
    }

    public void setRequestPages(long requestPages) {
        this.requestPages = requestPages;
    }

    public long getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(long requestItems) {
        this.requestItems = requestItems;
    }

    public Map<ContentTypeEnum, Long> getContentCounts() {
        return contentCounts;
    }

    public void setContentCounts(Map<ContentTypeEnum, Long> contentCounts) {
        this.contentCounts = contentCounts;
    }

    public CatcherStateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(CatcherStateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public boolean finish() {
        return finish;
    }
}
