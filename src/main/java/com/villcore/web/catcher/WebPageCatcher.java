package com.villcore.web.catcher;

import java.util.List;
import java.util.Map;

public interface WebPageCatcher {

    boolean needVerifyCode();

    ResponseBundle requestVerifyCode(RequestBundle requestBundle);

    boolean needLogin(ResponseBundle responseBundle);

    boolean login(RequestBundle requestBundle);

    ResponseBundle request(RequestBundle requestBundle);

    Page nextPage();

    ResponseBundle requestPage(RequestBundle requestBundle);

    ResponseBundle requestItem(RequestBundle requestBundle);

    List<Item> parseItems(ResponseBundle responseBundle);

    boolean needReply();

    boolean reply(RequestBundle requestBundle);

    Map<ContentTypeEnum, List<Content>> parseContent(ResponseBundle responseBundle);

}
