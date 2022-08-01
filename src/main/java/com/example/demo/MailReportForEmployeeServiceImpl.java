//package com.example.demo;
//
//import static ru.gazprombank.omni.oks.loyal.program.data.LoyalProgramContextField.GUID;
//import static ru.gazprombank.omni.oks.loyal.program.data.LoyalProgramContextField.MASK_NUM;
//import static ru.gazprombank.omni.oks.loyal.program.data.LoyalProgramContextField.NEW_LOYALTY_PROGRAM_NAME;
//
//import ru.gazprombank.omni.logging.context.MdcContextUtils;
//import ru.gazprombank.omni.oks.loyal.program.data.HtmlMessageData;
//import ru.gazprombank.omni.oks.loyal.program.entity.ApplicationList;
//import ru.gazprombank.omni.oks.loyal.program.entity.DataValue;
//import ru.gazprombank.omni.oks.loyal.program.enumerate.EmailStatusEnum;
//import ru.gazprombank.omni.oks.loyal.program.repository.ApplicationDataRepository;
//import ru.gazprombank.omni.oks.loyal.program.repository.ApplicationListRepository;
//
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//public class MailReportForEmployeeServiceImpl {
//
//
//    public void sendReport() {
//        Mono<Void> sendReportMono = Mono.defer(() -> Mono.just(prepareHtmlTemplateVariables(appList)))
//                .map(this::prepareHtmlMessage)
//                .flatMap(mailSenderService::sendHtml)
//                .doOnSuccess(el -> changeMailStatus(appList))
//                .doOnError(throwable -> {
//                    if (Objects.nonNull(throwable.getCause())) {
//                        throwable = throwable.getCause();
//                    }
//                    log.error("Не удалось подготовить и отправить email сообщение {}", throwable.getMessage());
//                })
//                .onErrorResume(Throwable.class, e -> Mono.empty());
//
//        MdcContextUtils.runWithMdcContext(sendReportMono, MDC.getCopyOfContextMap()).subscribe();
//    }
//
//    private void changeMailStatus(List<ApplicationList> appList) {
//        for (ApplicationList list : appList) {
//            list.setMailStatus(EmailStatusEnum.SUCCESS);
//        }
//        applicationListRepository.saveAll(appList);
//    }
//
//    private Map<String, Object> prepareHtmlTemplateVariables(List<ApplicationList> appList) {
//        var listEmailModel = new ArrayList<>();
//        for (ApplicationList list : appList) {
//            var orderId = list.getOrderId();
//            var appData = applicationDataRepository.findById(orderId).orElse(null);
//            var mapValues = new HashMap<String, String>();
//            for (DataValue value : Objects.requireNonNull(appData).getDataValues()) {
//                mapValues.put(value.getId(), value.getValue());
//            }
//            listEmailModel.add(EmailModel.builder()
//                    .orderId(orderId)
//                    .branchCode(appData.getBranchCode())
//                    .guid(mapValues.get(GUID.getKey()))
//                    .maskNum(mapValues.get(MASK_NUM.getKey()))
//                    .newLoyaltyProgram(mapValues.get(NEW_LOYALTY_PROGRAM_NAME.getKey()))
//                    .messageStatus(list.getMessageStatus())
//                    .build());
//        }
//
//        return Map.of("applicationList", listEmailModel);
//    }
//
//    @Builder
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    private static class EmailModel {
//
//        String orderId;
//        String branchCode;
//        String guid;
//        String maskNum;
//        String newLoyaltyProgram;
//        String messageStatus;
//
//    }
//
//    private String process(Map<String, Object> variables) {
//        var context = new Context();
//        context.setVariables(variables);
//        return templateEngine.process("5ntAcceptToActiveError.html", context);
//    }
//
//    private HtmlMessageData prepareHtmlMessage(Map<String, Object> variables) {
//        var html = process(variables);
//
//        log.debug("Сообщение для отправки на email: '{}'", html);
//
//        return new HtmlMessageData()
//                .setSubject("Ошибки обработки заявок на изменения программы лояльности")
//                .setHtml(html);
//    }
//
//}
