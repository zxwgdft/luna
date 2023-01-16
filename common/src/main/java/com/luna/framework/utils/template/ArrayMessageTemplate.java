package com.luna.framework.utils.template;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 按顺序填入参数后创建消息
 *
 * @author TontoZhou
 */
@SuppressWarnings("ALL")
public class ArrayMessageTemplate implements MessageTemplate {

    protected String template;

    public ArrayMessageTemplate(String template) {
        setTemplate(template);
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public String createMessage(Object... args) {
        if (args == null || args.length == 0) {
            return template;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str : mapper) {
            sb.append(str);
            if (i < args.length) {
                sb.append(args[i++]);
            }
        }
        return sb.toString();
    }

    // 模板分割后的字符串
    protected List<String> mapper;

    // 初始化
    @SuppressWarnings("AlibabaAvoidPatternCompileInMethod")
    protected void init() {
        if (template != null) {
            Pattern pattern = Pattern.compile("\\{\\}");
            mapper = new ArrayList<>();
            Matcher matcher = pattern.matcher(template);
            int i = 0;
            while (matcher.find()) {
                mapper.add(template.substring(i, matcher.start()));
                i = matcher.end();
            }
        }
    }

    public void setTemplate(String template) {
        this.template = template;
        init();
    }


}
