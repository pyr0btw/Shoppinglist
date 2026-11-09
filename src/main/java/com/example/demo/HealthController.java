package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Öffentlicher Endpunkt ohne Login. Ein Dienst wie cron-job.org ruft ihn alle 10 Minuten auf,
// damit das Backend auf Render (Free-Tier) nicht einschläft und kein Kaltstart beim Login nötig ist.
@RestController
public class HealthController {

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "ok");
	}

}
