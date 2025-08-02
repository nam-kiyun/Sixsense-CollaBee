package com.demo.proworks.sse.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.demo.proworks.email.vo.EmailVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import org.springframework.web.bind.annotation.RequestMethod;
import com.inswave.elfw.annotation.ElValidator;

@Controller
public class SseController {
	private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private static final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

	@ElService(key = "user/notice")
	@RequestMapping(value = "user/notice")
	@ElDescription(sub = "메일 발송을 전달", desc = "메일 발송을 전달")
	public SseEmitter connect(@RequestParam String userId) {
		SseEmitter emitter = new SseEmitter(600_000L); 
		String emitterId = UUID.randomUUID().toString(); 
		emitters.put(emitterId, emitter);

		emitter.onCompletion(() -> {
			emitters.remove(emitterId);
		});

		emitter.onTimeout(() -> {
			emitters.remove(emitterId);
		});

		emitter.onError((e) -> {
			emitters.remove(emitterId);
		});

		return emitter;
	}

	@PostConstruct
	public void startHeartbeatScheduler() {
		heartbeatExecutor.scheduleAtFixedRate(() -> {
			emitters.forEach((id, em) -> {
				try {
					em.send(SseEmitter.event().name("heartBeat").data("alive"));
				} catch (IOException e) {
					em.completeWithError(e);
				}
			});
		}, 0, 10, TimeUnit.SECONDS);
	}

	public void sendNotification(EmailVo message) {
		String notice = message.getUserName() + "님 오늘의 할일 메일이 발송되었습니다";
		emitters.forEach((id, em) -> {
			try {
				em.send(SseEmitter.event().name(message.getUserId()).data(notice));
			} catch (IOException e) {
				em.completeWithError(e);
			}
		});
	}

}
