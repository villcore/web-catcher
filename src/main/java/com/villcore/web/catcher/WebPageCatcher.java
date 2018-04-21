package com.villcore.web.catcher;

import java.util.List;
import java.util.Map;

public interface WebPageCatcher {

    Page nextPage();

    boolean needVerifyCode();

    boolean needLogin(ResponseBundle responseBundle);

    boolean needReply();

    long replyInterval();

    boolean login(RequestBundle requestBundle);

    ResponseBundle request(RequestBundle requestBundle);

    List<Item> parseItems(ResponseBundle responseBundle);

    ResponseBundle requestPage(RequestBundle requestBundle);

    ResponseBundle requestItem(RequestBundle requestBundle);

    ResponseBundle refresh(RequestBundle requestBundle);

    ResponseBundle reply(RequestBundle requestBundle);

    Map<ContentTypeEnum, List<Content>> parseContent(ResponseBundle responseBundle);

}
