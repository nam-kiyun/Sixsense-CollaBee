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
	
	private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
	

	@ElService(key = "user/notice")
	@RequestMapping(value = "/user/notice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ElDescription(sub = "메일 발송을 전달", desc = "메일 발송을 전달")
	public SseEmitter connect() {
		System.out.println("📡 SSE 접속됨");
		SseEmitter emitter = new SseEmitter(null); // 10분 타임아웃
		System.out.println("여기까지 왔어요");
		String emitterId = UUID.randomUUID().toString(); // 임시 식별자
		System.out.println("emitterId: " + emitterId);

		emitters.put(emitterId, emitter);
		System.out.println("emitter: " + emitters.size());
		emitter.onCompletion(() -> {
			System.out.println("❌ emitter 완료됨: " + emitterId);
			emitters.remove(emitterId);
		});

		emitter.onTimeout(() -> {
			System.out.println("⌛ emitter 타임아웃됨: " + emitterId);
			emitters.remove(emitterId);
		});

		emitter.onError((e) -> {
			System.out.println("⚠️ emitter 에러 발생: " + emitterId + " / " + e.getMessage());
			emitters.remove(emitterId);
		});

		try {
			emitter.send(SseEmitter.event().name("task-reminder").data("테스트 알림입니다!"));
		} catch (IOException e) {
			emitter.completeWithError(e);
		}

		System.out.println("emitter: " + emitter);
		System.out.println("emitter 등록됨: " + emitterId);
		System.out.println("전체 emitter 수: " + emitters.size());
		System.out.println("Emitter timeout 설정값: {}" + emitter.getTimeout()); // 실제 값 확인
		System.out.println("sendNotification 호출 후 emitter 수: " + emitters.size());

		return emitter;
	}

	@PostConstruct
	public void startHeartbeatScheduler() {
		heartbeatExecutor.scheduleAtFixedRate(() -> {
			emitters.forEach((id, em) -> {
				try {
					em.send(":\n");
				} catch (IOException e) {
					em.completeWithError(e);
				}
			});
		}, 0, 10, TimeUnit.SECONDS);
	}

	// 전체 사용자에게 알림 전송
	public void sendNotification(String message) {
		System.out.println("알림 도착");
		System.out.println(message);

		// 끊긴 emitter를 따로 수집
		emitters.forEach((userId, emitter) -> {
			System.out.println(userId);
			System.out.println(emitter);
			try {
				emitter.send(SseEmitter.event().name("task-reminder").data(message));
				System.out.println("보내고 있음");
			} catch (IOException e) {
				emitter.completeWithError(e);
				emitters.remove(userId);
				System.out.println("보내지지 않네요...." + e);
			}
		});
		System.out.println(emitters.size());
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
