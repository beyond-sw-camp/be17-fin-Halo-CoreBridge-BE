package org.example.corebridgebatch.notification.batch;

//@Component
//@RequiredArgsConstructor
//@Log4j2
//public class NotificationBatch {
//
//    private final NotificationRepository repository;
//    private final NotificationService service;
//
//    /** 5분마다 UNSENT 알림 재전송 시도 */
//    @Scheduled(fixedDelay = 300_000)
//    public void resendUnsent() {
//        log.info("[Batch] Start resend...");
//        int page = 0;
//        Page<Notification> result;
//        do {
//            result = repository.findForRetry(List.of(DeliveryStatus.UNSENT), PageRequest.of(page++, 100));
//            result.forEach(service::tryDeliver);
//        } while (!result.isEmpty());
//        log.info("[Batch] Completed resend cycle.");
//    }
//}
