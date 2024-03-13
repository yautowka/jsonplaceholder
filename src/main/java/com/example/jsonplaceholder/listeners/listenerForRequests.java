package com.example.jsonplaceholder.listeners;

import com.example.jsonplaceholder.models.Audit;
import com.example.jsonplaceholder.repos.AuditsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.RequestHandledEvent;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class listenerForRequests implements ApplicationListener<RequestHandledEvent> {
    private final AuditsRepository auditsRepository;

    private String getParams() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder s = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
//            System.out.println("Parameter Name: " + paramName + ", Parameter Value: " + paramValue);
            s.append(paramName)
                    .append("=")
                    .append(paramValue)
                    .append(", ");
        }
        if (!s.isEmpty()) s.delete(s.length() - 2, s.length());
        return s.toString();
    }

    private Map<String, String> getData(RequestHandledEvent request) {
        Map<String, String> res = new HashMap<>();
        for (String part :
                request.getDescription().split("; ")) {
            String[] tmp = part.split("=");
            if (tmp.length > 1) res.put(tmp[0], removeBracket(tmp[1]));
        }
        return res;
    }

    private String removeBracket(String s) {
        return s.substring(1, s.length() - 1);
    }

    @Override
    public void onApplicationEvent(RequestHandledEvent e) {

        Map<String, String> data = getData(e);

        auditsRepository.save(Audit
                .builder()
                .time(LocalDateTime.now())
                .access(true)
                .method(data.get("method"))
                .url(data.get("url"))
                .username(data.get("user"))
                .params(getParams())
                .build());

    }
}
