package com.demo.proworks.sse.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;

@Controller
public class SseController {

	// 사용자별 emitter 관리
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	@ElService(key = "user/notice")
	@RequestMapping(value = "/user/notice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	@ElDescription(sub = "메일 발송을 전달", desc = "메일 발송을 전달")
	public SseEmitter connect() {
		SseEmitter emitter = new SseEmitter(10 * 60 * 1000L); // 10분 타임아웃
//		String emitterId = UUID.randomUUID().toString(); // 임시 식별자
//
//		emitters.put(emitterId, emitter);
//
//		emitter.onCompletion(() -> emitters.remove(emitterId));
//		emitter.onTimeout(() -> emitters.remove(emitterId));
//		emitter.onError((e) -> emitters.remove(emitterId));

		return emitter;
	}

	public void broadcast(String message) {
		List<String> deadEmitters = new ArrayList<>();

		emitters.forEach((id, emitter) -> {
			try {
				emitter.send(SseEmitter.event().name("mail-event").data(message));
			} catch (Exception e) {
				deadEmitters.add(id); // 실패한 emitter 제거 대상
			}
		});

		deadEmitters.forEach(emitters::remove);
	}

	// 전체 사용자에게 알림 전송
	public void sendNotification(String message) {
		System.out.println("알림 도착");
		System.out.println(message);

		// 끊긴 emitter를 따로 수집
		emitters.forEach((userId, emitter) -> {
			try {
				emitter.send(SseEmitter.event().name("task-reminder").data(message));
			} catch (IOException e) {
				emitter.completeWithError(e);
				emitters.remove(userId);
			}
		});
	}

	// 특정 사용자에게만 알림 전송
	public void sendToUser(String userId, String message) {
		SseEmitter emitter = emitters.get(userId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("task-reminder").data(message));
			} catch (IOException e) {
				emitter.completeWithError(e);
				emitters.remove(userId);
			}
		}
	}
}
